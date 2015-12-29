package com.pjf.spring.beans;

import java.lang.reflect.Field;

import com.pjf.spring.core.MethodParameter;

/**
 * 定义类型转换方法
 * @author pengjinfei
 * @date 2015年12月27日
 */
public interface TypeConverter {

	<T> T convertIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException;

	<T> T convertIfNecessary(Object value, Class<T> requiredType, MethodParameter methodParam)
			throws TypeMismatchException;

	<T> T convertIfNecessary(Object value, Class<T> requiredType, Field field)
			throws TypeMismatchException;
}
