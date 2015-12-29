package com.pjf.spring.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月29日
 */
public interface ParameterNameDiscoverer {

	String[] getParameterNames(Method method);
	
	String[] getParameterNames(Constructor<?> constructor);
}
