package com.pjf.spring;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;

import org.junit.Test;

public class ReferenceTest {

	@Test
	public void test() {
		Object obj = new Object();
		ReferenceQueue<Object> refQueue = new ReferenceQueue<Object>();
		PhantomReference<Object> phanRef = new PhantomReference<Object>(obj, refQueue);
		System.out.println(phanRef.get());
		System.out.println(refQueue.poll());
		obj = null;
		System.gc();
		System.out.println(phanRef.get());
		Object o=refQueue.poll();
		System.out.println(refQueue.poll());
		System.out.println(refQueue.poll());
		System.out.println(refQueue.poll());

	}

}
