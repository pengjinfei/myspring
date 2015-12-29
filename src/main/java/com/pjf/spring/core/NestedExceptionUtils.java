package com.pjf.spring.core;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月26日
 */
public abstract class NestedExceptionUtils {
	
	public static String buildMessage(String message, Throwable cause){
		if(cause!=null){
			StringBuilder stringBuilder=new StringBuilder();
			if(message!=null){
				stringBuilder.append(message).append("; ");
			}
			stringBuilder.append("nested exception is ").append(cause);
			return stringBuilder.toString();
		}else {
			return message;
		}
	}
}
