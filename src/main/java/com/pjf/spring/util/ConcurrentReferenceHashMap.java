package com.pjf.spring.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月29日
 */
public class ConcurrentReferenceHashMap<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {

	private static final int DEFAULT_INITIAL_CAPACITY = 16;

	private static final float DEFAULT_LOAD_FACTOR = 0.75f;

	private static final int DEFAULT_CONCURRENCY_LEVEL = 16;

	private static final ReferenceType DEFAULT_REFERENCE_TYPE = ReferenceType.SOFT;

	private static final int MAXIMUM_CONCURRENCY_LEVEL = 1 << 16;

	private static final int MAXIMUM_SEGMENT_SIZE = 1 << 30;

	public static enum ReferenceType {
		SOFT, WEAK
	}

	private final Segment[] segments;
	
	/**
	 * When the average number of references per table exceeds this value resize will be attempted.
	 */
	private final float loadFactor;

	/**
	 * The reference type: SOFT or WEAK.
	 */
	private final ReferenceType referenceType;

	/**
	 * The shift value used to calculate the size of the segments array and an index from the hash.
	 */
	private final int shift;

	/**
	 * Late binding entry set.
	 */
	private Set<Map.Entry<K, V>> entrySet;

	protected static interface Reference<K, V> {


		/**
		 * Returns the referenced entry or {@code null} if the entry is no
		 * longer available.
		 * 
		 * @return the entry or {@code null}
		 */
		Entry<K, V> get();

		/**
		 * Returns the hash for the reference.
		 * 
		 * @return the hash
		 */
		int getHash();

		/**
		 * Returns the next reference in the chain or {@code null}
		 * 
		 * @return the next reference of {@code null}
		 */
		Reference<K, V> getNext();

		/**
		 * Release this entry and ensure that it will be returned from
		 * {@code ReferenceManager#pollForPurge()}.
		 */
		void release();
	}

	private static final class WeakEntryReference<K, V> extends WeakReference<Entry<K, V>> implements Reference<K, V> {

		private final int hash;

		private final Reference<K, V> nextReference;

		public WeakEntryReference(Entry<K, V> entry, int hash, Reference<K, V> next, ReferenceQueue<Entry<K, V>> queue) {
			super(entry, queue);
			this.hash = hash;
			this.nextReference = next;
		}

		public int getHash() {
			return this.hash;
		}

		public Reference<K, V> getNext() {
			return this.nextReference;
		}

		public void release() {
			enqueue();
			clear();
		}
	}
	
	private static final class SoftEntryReference<K, V> extends SoftReference<Entry<K, V>> implements Reference<K, V> {

		private final int hash;

		private final Reference<K, V> nextReference;

		public SoftEntryReference(Entry<K, V> entry, int hash, Reference<K, V> next, ReferenceQueue<Entry<K, V>> queue) {
			super(entry, queue);
			this.hash = hash;
			this.nextReference = next;
		}

		public int getHash() {
			return this.hash;
		}

		public Reference<K, V> getNext() {
			return this.nextReference;
		}

		public void release() {
			enqueue();
			clear();
		}
	}
	
	protected class ReferenceManager {

		private final ReferenceQueue<Entry<K, V>> queue = new ReferenceQueue<Entry<K, V>>();

		/**
		 * Factory method used to create a new {@link Reference}.
		 * 
		 * @param entry
		 *            the entry contained in the reference
		 * @param hash
		 *            the hash
		 * @param next
		 *            the next reference in the chain or {@code null}
		 * @return a new {@link Reference}
		 */
		public Reference<K, V> createReference(Entry<K, V> entry, int hash, Reference<K, V> next) {
			if (ConcurrentReferenceHashMap.this.referenceType == ReferenceType.WEAK) {
				return new WeakEntryReference<K, V>(entry, hash, next, this.queue);
			}
			return new SoftEntryReference<K, V>(entry, hash, next, this.queue);
		}

		/**
		 * Return any reference that has been garbage collected and can be
		 * purged from the underlying structure or {@code null} if no references
		 * need purging. This method must be thread safe and ideally should not
		 * block when returning {@code null}. References should be returned once
		 * and only once.
		 * 
		 * @return a reference to purge or {@code null}
		 */
		@SuppressWarnings("unchecked")
		public Reference<K, V> pollForPurge() {
			return (Reference<K, V>) this.queue.poll();
		}
	}
	
	private class EntryIterator implements Iterator<Map.Entry<K, V>> {

		private int segmentIndex;

		private int referenceIndex;

		private Reference<K, V>[] references;

		private Reference<K, V> reference;

		private Entry<K, V> next;

		private Entry<K, V> last;

		public EntryIterator() {
			moveToNextSegment();
		}

		public boolean hasNext() {
			getNextIfNecessary();
			return (this.next != null);
		}

		public Entry<K, V> next() {
			getNextIfNecessary();
			if (this.next == null) {
				throw new NoSuchElementException();
			}
			this.last = this.next;
			this.next = null;
			return this.last;
		}

		private void getNextIfNecessary() {
			while (this.next == null) {
				moveToNextReference();
				if (this.reference == null) {
					return;
				}
				this.next = this.reference.get();
			}
		}

		private void moveToNextReference() {
			if (this.reference != null) {
				this.reference = this.reference.getNext();
			}
			while (this.reference == null && this.references != null) {
				if (this.referenceIndex >= this.references.length) {
					moveToNextSegment();
					this.referenceIndex = 0;
				}
				else {
					this.reference = this.references[this.referenceIndex];
					this.referenceIndex++;
				}
			}
		}

		private void moveToNextSegment() {
			this.reference = null;
			this.references = null;
			if (this.segmentIndex < ConcurrentReferenceHashMap.this.segments.length) {
				this.references = ConcurrentReferenceHashMap.this.segments[this.segmentIndex].references;
				this.segmentIndex++;
			}
		}

		public void remove() {
			Assert.state(this.last != null);
			ConcurrentReferenceHashMap.this.remove(this.last.getKey());
		}
	}
	
	@SuppressWarnings("serial")
	protected final class Segment extends ReentrantLock {

		private final ReferenceManager referenceManager;

		private final int initialSize;

		/**
		 * Array of references indexed using the low order bits from the hash. This
		 * property should only be set via {@link #setReferences} to ensure that the
		 * {@code resizeThreshold} is maintained.
		 */
		private volatile Reference<K, V>[] references;

		/**
		 * The total number of references contained in this segment. This includes chained
		 * references and references that have been garbage collected but not purged.
		 */
		private volatile int count = 0;

		/**
		 * The threshold when resizing of the references should occur. When {@code count}
		 * exceeds this value references will be resized.
		 */
		private int resizeThreshold;

		public Segment(int initialCapacity) {
			this.referenceManager = createReferenceManager();
			this.initialSize = 1 << calculateShift(initialCapacity, MAXIMUM_SEGMENT_SIZE);
			setReferences(createReferenceArray(this.initialSize));
		}

		public Reference<K, V> getReference(Object key, int hash, Restructure restructure) {
			if (restructure == Restructure.WHEN_NECESSARY) {
				restructureIfNecessary(false);
			}
			if (this.count == 0) {
				return null;
			}
			// Use a local copy to protect against other threads writing
			Reference<K, V>[] references = this.references;
			int index = getIndex(hash, references);
			Reference<K, V> head = references[index];
			return findInChain(head, key, hash);
		}

		/**
		 * Apply an update operation to this segment.
		 * The segment will be locked during the update.
		 * @param hash the hash of the key
		 * @param key the key
		 * @param task the update operation
		 * @return the result of the operation
		 */
		public <T> T doTask(final int hash, final Object key, final Task<T> task) {
			boolean resize = task.hasOption(TaskOption.RESIZE);
			if (task.hasOption(TaskOption.RESTRUCTURE_BEFORE)) {
				restructureIfNecessary(resize);
			}
			if (task.hasOption(TaskOption.SKIP_IF_EMPTY) && this.count == 0) {
				return task.execute(null, null, null);
			}
			lock();
			try {
				final int index = getIndex(hash, this.references);
				final Reference<K, V> head = this.references[index];
				Reference<K, V> reference = findInChain(head, key, hash);
				Entry<K, V> entry = (reference != null ? reference.get() : null);
				Entries entries = new Entries() {
					@Override
					public void add(V value) {
						@SuppressWarnings("unchecked")
						Entry<K, V> newEntry = new Entry<K, V>((K) key, value);
						Reference<K, V> newReference = Segment.this.referenceManager.createReference(newEntry, hash, head);
						Segment.this.references[index] = newReference;
						Segment.this.count++;
					}
				};
				return task.execute(reference, entry, entries);
			}
			finally {
				unlock();
				if (task.hasOption(TaskOption.RESTRUCTURE_AFTER)) {
					restructureIfNecessary(resize);
				}
			}
		}

		/**
		 * Clear all items from this segment.
		 */
		public void clear() {
			if (this.count == 0) {
				return;
			}
			lock();
			try {
				setReferences(createReferenceArray(this.initialSize));
				this.count = 0;
			}
			finally {
				unlock();
			}
		}

		/**
		 * Restructure the underlying data structure when it becomes necessary. This
		 * method can increase the size of the references table as well as purge any
		 * references that have been garbage collected.
		 * @param allowResize if resizing is permitted
		 */
		protected final void restructureIfNecessary(boolean allowResize) {
			boolean needsResize = ((this.count > 0) && (this.count >= this.resizeThreshold));
			Reference<K, V> reference = this.referenceManager.pollForPurge();
			if ((reference != null) || (needsResize && allowResize)) {
				lock();
				try {
					int countAfterRestructure = this.count;

					Set<Reference<K, V>> toPurge = Collections.emptySet();
					if (reference != null) {
						toPurge = new HashSet<Reference<K, V>>();
						while (reference != null) {
							toPurge.add(reference);
							reference = this.referenceManager.pollForPurge();
						}
					}
					countAfterRestructure -= toPurge.size();

					// Recalculate taking into account count inside lock and items that
					// will be purged
					needsResize = (countAfterRestructure > 0 && countAfterRestructure >= this.resizeThreshold);
					boolean resizing = false;
					int restructureSize = this.references.length;
					if (allowResize && needsResize && restructureSize < MAXIMUM_SEGMENT_SIZE) {
						restructureSize <<= 1;
						resizing = true;
					}

					// Either create a new table or reuse the existing one
					Reference<K, V>[] restructured = (resizing ? createReferenceArray(restructureSize) : this.references);

					// Restructure
					for (int i = 0; i < this.references.length; i++) {
						reference = this.references[i];
						if (!resizing) {
							restructured[i] = null;
						}
						while (reference != null) {
							if (!toPurge.contains(reference) && (reference.get() != null)) {
								int index = getIndex(reference.getHash(), restructured);
								restructured[index] = this.referenceManager.createReference(
										reference.get(), reference.getHash(),
										restructured[index]);
							}
							reference = reference.getNext();
						}
					}

					// Replace volatile members
					if (resizing) {
						setReferences(restructured);
					}
					this.count = Math.max(countAfterRestructure, 0);
				}
				finally {
					unlock();
				}
			}
		}

		private Reference<K, V> findInChain(Reference<K, V> reference, Object key, int hash) {
			while (reference != null) {
				if (reference.getHash() == hash) {
					Entry<K, V> entry = reference.get();
					if (entry != null) {
						K entryKey = entry.getKey();
						if (entryKey == key || entryKey.equals(key)) {
							return reference;
						}
					}
				}
				reference = reference.getNext();
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		private Reference<K, V>[] createReferenceArray(int size) {
			return (Reference<K, V>[]) Array.newInstance(Reference.class, size);
		}

		private int getIndex(int hash, Reference<K, V>[] references) {
			return (hash & (references.length - 1));
		}

		/**
		 * Replace the references with a new value, recalculating the resizeThreshold.
		 * @param references the new references
		 */
		private void setReferences(Reference<K, V>[] references) {
			this.references = references;
			this.resizeThreshold = (int) (references.length * getLoadFactor());
		}

		/**
		 * @return the size of the current references array
		 */
		public final int getSize() {
			return this.references.length;
		}

		/**
		 * @return the total number of references in this segment
		 */
		public final int getCount() {
			return this.count;
		}
	}
	
	private abstract class Task<T> {

		private final EnumSet<TaskOption> options;

		public Task(TaskOption... options) {
			this.options = (options.length == 0 ? EnumSet.noneOf(TaskOption.class) : EnumSet.of(options[0], options));
		}

		public boolean hasOption(TaskOption option) {
			return this.options.contains(option);
		}

		/**
		 * Execute the task.
		 * @param reference the found reference or {@code null}
		 * @param entry the found entry or {@code null}
		 * @param entries access to the underlying entries
		 * @return the result of the task
		 * @see #execute(Reference, Entry)
		 */
		protected T execute(Reference<K, V> reference, Entry<K, V> entry, Entries entries) {
			return execute(reference, entry);
		}

		/**
		 * Convenience method that can be used for tasks that do not need access to {@link Entries}.
		 * @param reference the found reference or {@code null}
		 * @param entry the found entry or {@code null}
		 * @return the result of the task
		 * @see #execute(Reference, Entry, Entries)
		 */
		protected T execute(Reference<K, V> reference, Entry<K, V> entry) {
			return null;
		}
	}
	
	/**
	 * Various options supported by a {@code Task}.
	 */
	private static enum TaskOption {

		RESTRUCTURE_BEFORE, RESTRUCTURE_AFTER, SKIP_IF_EMPTY, RESIZE
	}
	
	private abstract class Entries {

		/**
		 * Add a new entry with the specified value.
		 * @param value the value to add
		 */
		public abstract void add(V value);
	}
	
	private class EntrySet extends AbstractSet<Map.Entry<K, V>> {

		public Iterator<Map.Entry<K, V>> iterator() {
			return new EntryIterator();
		}

		public boolean contains(Object o) {
			if (o != null && o instanceof Map.Entry<?, ?>) {
				Map.Entry<?, ?> entry = (java.util.Map.Entry<?, ?>) o;
				Reference<K, V> reference = ConcurrentReferenceHashMap.this.getReference(entry.getKey(), Restructure.NEVER);
				Entry<K, V> other = (reference != null ? reference.get() : null);
				if (other != null) {
					return ObjectUtils.nullSafeEquals(entry.getValue(), other.getValue());
				}
			}
			return false;
		}

		public boolean remove(Object o) {
			if (o instanceof Map.Entry<?, ?>) {
				Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
				return ConcurrentReferenceHashMap.this.remove(entry.getKey(), entry.getValue());
			}
			return false;
		}

		public int size() {
			return ConcurrentReferenceHashMap.this.size();
		}

		public void clear() {
			ConcurrentReferenceHashMap.this.clear();
		}
	}
	
	/**
	 * The types of restructuring that can be performed.
	 */
	protected static enum Restructure {

		WHEN_NECESSARY, NEVER
	}

	public V putIfAbsent(K key, V value) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean remove(Object key, Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean replace(K key, V oldValue, V newValue) {
		// TODO Auto-generated method stub
		return false;
	}

	public V replace(K key, V value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

}
