package com.pjf.spring.core.convert;

/**
 * A factory for "ranged" converters that can convert objects from S to subtypes of R.
 * @author pengjinfei
 * @date 2015年12月27日 
 * @param <S>
 * @param <R>
 */
public interface ConverterFactory<S, R> {

	<T extends R> Converter<S, T> getConverter(Class<T> targetType);
}
