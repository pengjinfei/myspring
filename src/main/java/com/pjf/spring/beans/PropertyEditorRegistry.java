package com.pjf.spring.beans;

import java.beans.PropertyEditor;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月28日
 */
public interface PropertyEditorRegistry {

	void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor);
	
	void registerCustomEditor(Class<?> requiredType, String propertyPath, PropertyEditor propertyEditor);
	
	PropertyEditor findCustomEditor(Class<?> requiredType, String propertyPath);
}
