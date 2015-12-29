package com.pjf.spring.beans;

import java.beans.PropertyChangeEvent;

import com.pjf.spring.core.BeansException;
import com.pjf.spring.core.ErrorCoded;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月27日
 */
public abstract class PropertyAccessException extends BeansException implements ErrorCoded {

	private static final long serialVersionUID = 3917221765223253774L;

	private transient PropertyChangeEvent propertyChangeEvent;

	public PropertyAccessException(PropertyChangeEvent propertyChangeEvent, String msg, Throwable cause) {
		super(msg, cause);
		this.propertyChangeEvent = propertyChangeEvent;
	}

	public PropertyAccessException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public PropertyChangeEvent getPropertyChangeEvent() {
		return this.propertyChangeEvent;
	}

	public String getPropertyName() {
		return (this.propertyChangeEvent != null ? this.propertyChangeEvent.getPropertyName() : null);
	}

	public Object getValue() {
		return (this.propertyChangeEvent != null ? this.propertyChangeEvent.getNewValue() : null);
	}

}
