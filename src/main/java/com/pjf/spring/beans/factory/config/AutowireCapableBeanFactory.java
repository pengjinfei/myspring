package com.pjf.spring.beans.factory.config;

import java.util.Set;

import com.pjf.spring.beans.TypeConverter;
import com.pjf.spring.beans.factory.BeanFactory;
import com.pjf.spring.core.BeansException;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月27日
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

	int AUTOWIRE_NO = 0;

	int AUTOWIRE_BY_NAME = 1;

	int AUTOWIRE_BY_TYPE = 2;

	int AUTOWIRE_CONSTRUCTOR = 3;
	
	<T> T createBean(Class<T> beanClass) throws BeansException;
	
	void autowireBean(Object existingBean) throws BeansException;
	
	Object configureBean(Object existingBean, String beanName) throws BeansException;
	
	Object resolveDependency(DependencyDescriptor descriptor, String beanName) throws BeansException;
	
	Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException;
	
	Object autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws BeansException;
	
	void autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck)
			throws BeansException;
	
	void applyBeanPropertyValues(Object existingBean, String beanName) throws BeansException;
	
	Object initializeBean(Object existingBean, String beanName) throws BeansException;
	
	Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
			throws BeansException;
	
	Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
			throws BeansException;
	
	void destroyBean(Object existingBean);
	
	Object resolveDependency(DependencyDescriptor descriptor, String beanName,
			Set<String> autowiredBeanNames, TypeConverter typeConverter) throws BeansException;

}
