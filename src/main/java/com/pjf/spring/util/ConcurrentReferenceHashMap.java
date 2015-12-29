package com.pjf.spring.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import javax.swing.text.Segment;

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
