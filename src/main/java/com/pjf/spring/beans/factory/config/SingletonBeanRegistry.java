package com.pjf.spring.beans.factory.config;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月28日
 */
public interface SingletonBeanRegistry {

	void registerSingleton(String beanName, Object singletonObject);

	Object getSingleton(String beanName);

	boolean containsSingleton(String beanName);

	String[] getSingletonNames();

	int getSingletonCount();
}
