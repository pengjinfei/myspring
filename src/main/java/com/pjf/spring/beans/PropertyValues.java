package com.pjf.spring.beans;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月29日
 */
public interface PropertyValues {

	PropertyValue[] getPropertyValues();
	
	PropertyValue getPropertyValue(String propertyName);

	PropertyValues changesSince(PropertyValues old);

	boolean contains(String propertyName);

	boolean isEmpty();
}
