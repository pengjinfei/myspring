package com.pjf.spring.core.env;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月26日
 */
public interface PropertyResolver {
	
	boolean containsProperty(String key);
	
	String getProperty(String key);
	
	String getProperty(String key,String defaultValue);
	
	<T> T getProperty(String key,Class<?> targetType);
	
	<T> T getProperty(String key,Class<?> targetType,T defaultValue);
	
	<T> Class<T> getPropertyAsClass(String key, Class<T> targetType);
	
	String getRequiredProperty(String key) throws IllegalStateException;
	
	<T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException;
	
	String resolvePlaceholders(String text);
	
	String resolveRequiredPlaceholders(String text) throws IllegalArgumentException;
}
