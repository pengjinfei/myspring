package com.pjf.spring.beans.factory.config;

import com.pjf.spring.beans.BeanMetadataElement;
import com.pjf.spring.beans.MutablePropertyValues;
import com.pjf.spring.core.AttributeAccessor;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月28日
 */
public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {

	String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

	String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;
	
	int ROLE_APPLICATION = 0;

	int ROLE_SUPPORT = 1;

	int ROLE_INFRASTRUCTURE = 2;
	
	String getParentName();

	void setParentName(String parentName);

	String getBeanClassName();

	void setBeanClassName(String beanClassName);

	String getFactoryBeanName();

	void setFactoryBeanName(String factoryBeanName);

	String getFactoryMethodName();

	void setFactoryMethodName(String factoryMethodName);

	String getScope();

	void setScope(String scope);

	boolean isLazyInit();

	void setLazyInit(boolean lazyInit);

	String[] getDependsOn();

	void setDependsOn(String... dependsOn);

	boolean isAutowireCandidate();

	void setAutowireCandidate(boolean autowireCandidate);

	boolean isPrimary();

	void setPrimary(boolean primary);

	ConstructorArgumentValues getConstructorArgumentValues();

	MutablePropertyValues getPropertyValues();

	boolean isSingleton();

	boolean isPrototype();

	boolean isAbstract();

	int getRole();

	String getDescription();

	String getResourceDescription();

	BeanDefinition getOriginatingBeanDefinition();
	
	
}
