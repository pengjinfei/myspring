package com.pjf.spring.beans.factory;

import com.pjf.spring.core.BeansException;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月28日 
 * @param <T>
 */
public interface ObjectFactory<T> {

	T getObject() throws BeansException;
}
