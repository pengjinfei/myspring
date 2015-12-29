package com.pjf.spring.core;

import com.pjf.spring.util.ObjectUtils;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月26日
 */
public class BeansException extends NestedRuntimeException{

	private static final long serialVersionUID = 4036977171883462876L;

	public BeansException(String message) {
		super(message);
	}

	public BeansException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public int hashCode() {
		return getMessage().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this==obj) {
			return true;
		}
		if(!(obj instanceof BeansException)){
			return false;
		}
		BeansException beansException=(BeansException)obj;
		return this.getMessage().equals(beansException.getMessage()) && ObjectUtils.nullSafeEquals(getCause(), beansException.getCause());
	}
	
	
	
	
}
