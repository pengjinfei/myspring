package com.pjf.spring.beans.factory.config;

import com.pjf.spring.core.BeansException;

/**
 * 工厂钩子：自定义修改bean实例
 * @author pengjinfei
 * @date 2015年12月28日
 */
public interface BeanPostProcessor {

	Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;
	
	Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}
