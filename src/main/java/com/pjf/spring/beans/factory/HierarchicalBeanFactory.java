package com.pjf.spring.beans.factory;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月26日
 */
public interface HierarchicalBeanFactory extends BeanFactory{

	BeanFactory getParentBeanFactory();
	
	boolean containsLocalBean(String name);
}
