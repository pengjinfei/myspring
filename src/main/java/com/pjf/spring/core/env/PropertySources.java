package com.pjf.spring.core.env;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月27日
 */
public interface PropertySources extends Iterable<PropertySource<?>>{

	boolean contains(String name);
	
	PropertySource<?> get(String name);
}
