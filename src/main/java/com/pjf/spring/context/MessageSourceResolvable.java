package com.pjf.spring.context;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月26日
 */
public interface MessageSourceResolvable {
	String[] getCodes();

	Object[] getArguments();

	String getDefaultMessage();
}
