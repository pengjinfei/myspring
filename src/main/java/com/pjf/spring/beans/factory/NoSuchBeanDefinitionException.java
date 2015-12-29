package com.pjf.spring.beans.factory;

import com.pjf.spring.core.BeansException;
import com.pjf.spring.util.StringUtils;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月26日
 */
public class NoSuchBeanDefinitionException extends BeansException {

	private static final long serialVersionUID = 6671608539039263306L;

	private String beanName;

	private Class<?> beanType;

	public NoSuchBeanDefinitionException(String name) {
		super("No bean named '" + name + "' is defined");
		this.beanName = name;
	}

	public NoSuchBeanDefinitionException(String name, String message) {
		super("No bean named '" + name + "' is defined: " + message);
		this.beanName = name;
	}

	public NoSuchBeanDefinitionException(Class<?> type) {
		super("No qualifying bean of type ['" + type.getName() + "'] is defined");
		this.beanType = type;
	}

	public NoSuchBeanDefinitionException(Class<?> type, String message) {
		super("No qualifying bean of type ['" + type.getName() + "'] is defined: " + message);
		this.beanType = type;
	}

	public NoSuchBeanDefinitionException(Class<?> type, String dependencyDiscription, String message) {
		super("No qualifying bean of type ['" + type.getName() + "'] found for dependency"
				+ (StringUtils.hasText(dependencyDiscription) ? " [" + dependencyDiscription + "] " : "") + message);
		this.beanType = type;
	}
	
	public String getBeanName() {
		return beanName;
	}
	
	public Class<?> getBeanType() {
		return beanType;
	}
	
}
