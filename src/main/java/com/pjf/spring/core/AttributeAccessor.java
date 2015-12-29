package com.pjf.spring.core;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月28日
 */
public interface AttributeAccessor {
	
	void setAttribute(String name, Object value);
	
	Object getAttribute(String name);

	Object removeAttribute(String name);

	boolean hasAttribute(String name);

	String[] attributeNames();
}
