package com.pjf.spring.beans.factory;

import java.lang.annotation.Annotation;
import java.util.Map;

import com.pjf.spring.core.BeansException;

/**
 * 可以枚举所有的bean实例的benfactory，而不是通过名称一个一个的查询所得的实例
 * @author pengjinfei
 * @date 2015年12月26日
 */
public interface ListableBeanFactory extends BeanFactory{

	boolean containsBeanDefinition(String beanName);
	
	int getBeanDefinitionCount();
	
	String[] getBeanDefinitionNames();
	
	String[] getBeanNamesForType(Class<?> type);
	
	String[] getBeanNamesForType(Class<?> type,boolean includeNonSingletons,boolean allowEagerInit);
	
	<T> Map<String, T> getBeansOfType(Class<?> type) throws BeansException;
	
	String[] getBeanNamesForAnnotation(Class<? extends Annotation> annotationType);
	
	Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws BeansException;
	
	<A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType)
			throws NoSuchBeanDefinitionException;
}
