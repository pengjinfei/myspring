package com.pjf.spring.beans.factory.config;

import com.pjf.spring.beans.factory.ObjectFactory;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月28日
 */
public interface Scope {

	Object get(String name, ObjectFactory<?> objectFactory);
	
	Object remove(String name);
	
	void registerDestructionCallback(String name, Runnable callback);
	
	Object resolveContextualObject(String key);
	
	String getConversationId();
}
