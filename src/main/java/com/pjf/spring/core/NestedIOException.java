package com.pjf.spring.core;

import java.io.IOException;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月26日
 */
public class NestedIOException extends IOException {

	private static final long serialVersionUID = 2341005552585234467L;

	public NestedIOException(String message, Throwable cause) {
		super(message, cause);
	}

	public NestedIOException(String message) {
		super(message);
	}

	@Override
	public String getMessage() {
		return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
	}

	
}
