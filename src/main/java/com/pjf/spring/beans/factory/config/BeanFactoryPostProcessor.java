package com.pjf.spring.beans.factory.config;

import com.pjf.spring.core.BeansException;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月28日
 */
public interface BeanFactoryPostProcessor {

	void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
