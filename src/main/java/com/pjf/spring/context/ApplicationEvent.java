package com.pjf.spring.context;

import java.util.EventObject;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月27日
 */
public abstract class ApplicationEvent extends EventObject{

	private static final long serialVersionUID = 8882301548341705476L;

	private final long timestamp;
	
	public ApplicationEvent(Object source) {
		super(source);
		this.timestamp=System.currentTimeMillis();
	}

	public long getTimestamp() {
		return timestamp;
	}
	
}
