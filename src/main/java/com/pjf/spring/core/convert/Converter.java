package com.pjf.spring.core.convert;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月27日 
 * @param <S>
 * @param <T>
 */
public interface Converter<S, T> {

	T convert(S source);
}
