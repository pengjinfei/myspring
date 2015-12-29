package com.pjf.spring.beans.factory;

import com.pjf.spring.core.BeansException;

/**
 * bean工厂类的接口 
 * @author pengjinfei
 * @date 2015年12月26日
 */
public interface BeanFactory {
	
	/**
	 * 获取工厂ben的前缀
	 */
	String FACTORY_BEAN_PREFIX="&";
	
	/**
	 * 根据名称查找对象
	 * @param name
	 * @return
	 * @throws BeansException
	 */
	Object getBean(String name) throws BeansException;
	
	/**
	 * 根据名称和类查找对象
	 * @param name
	 * @param requiredType
	 * @return
	 * @throws BeansException
	 */
	<T> T getBean(String name,Class<T> requiredType) throws BeansException;
	
	/**
	 * 根据类查找对象
	 * @param requiredType
	 * @return
	 * @throws BeansException
	 */
	<T> T getBean(Class<T> requiredType) throws BeansException;
	
	/**
	 * 根据名称和构造方法参数或者工厂参数查找对象
	 * @param name
	 * @param args
	 * @return
	 * @throws BeansException
	 */
	Object getBean(String name,Object... args) throws BeansException;
	
	/**
	 * 根据类和构造方法参数或者工厂参数查找对象
	 * @param requiredType
	 * @param args
	 * @return
	 * @throws BeansException
	 */
	<T> T getBean(Class<T> requiredType,Object... args) throws BeansException;
	
	/**
	 * 是否包含某个名称的对象
	 * @param name
	 * @return
	 */
	boolean containsBean(String name);
	
	/**
	 * 判断某个名称的对象是否为单例
	 * @param name
	 * @return
	 * @throws NoSuchBeanDefinitionException
	 */
	boolean isSingleton(String name) throws NoSuchBeanDefinitionException;
	
	/**
	 * 判断某个名称的对象是否为多例
	 * @param name
	 * @return
	 * @throws NoSuchBeanDefinitionException
	 */
	boolean isPrototype(String name) throws NoSuchBeanDefinitionException;
	
	/**
	 * 判断某个名称是否属于指定的类
	 * @param name
	 * @param targetType
	 * @return
	 * @throws NoSuchBeanDefinitionException
	 */
	boolean isTypeMatch(String name,Class<?> targetType) throws NoSuchBeanDefinitionException;
	
	/**
	 * 获得指定名称的类
	 * @param name
	 * @return
	 * @throws NoSuchBeanDefinitionException
	 */
	Class<?> getType(String name) throws NoSuchBeanDefinitionException;
	
	/**
	 * 获得指定名称的所有别名
	 * @param name
	 * @return
	 */
	String[] getAliases(String name);
}
