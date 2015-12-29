package com.pjf.spring;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

public class PhantomReferenceTest {
	public static boolean run=true;
	public static void main(String[] args) {
		Object obj = new TestObject();
	    ReferenceQueue<Object> refQueue = new ReferenceQueue<Object>();
	    PhantomReference<Object> phanRef = new PhantomReference<Object>(obj, refQueue);
	    System.out.println(phanRef.get());
	    System.out.println(refQueue.poll());
	    obj = null;
	    System.gc();
	    System.out.println(phanRef.get());
	    System.runFinalization();   
	    System.out.println(refQueue.poll());
	    
	}
}

class TestObject {
	@Override
	protected void finalize() throws Throwable {
		System.out.println("finalize method executed");
		super.finalize();
	}
}