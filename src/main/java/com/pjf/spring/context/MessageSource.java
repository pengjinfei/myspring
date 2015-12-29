package com.pjf.spring.context;

import java.util.Locale;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月26日
 */
public interface MessageSource {

	String getMessage(String code, Object[] args, String defaultMessage, Locale locale);
	
	String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException;
	
	String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException;
}
