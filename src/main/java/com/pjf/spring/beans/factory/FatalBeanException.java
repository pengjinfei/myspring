package com.pjf.spring.beans.factory;

import com.pjf.spring.core.BeansException;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月28日
 */
@SuppressWarnings("serial")
public class FatalBeanException extends BeansException {

	public FatalBeanException(String msg) {
		super(msg);
	}

	public FatalBeanException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
