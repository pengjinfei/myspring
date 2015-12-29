package com.pjf.spring.core.convert;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月27日
 */
public interface ConverterRegistry {

	void addConverter(Converter<?, ?> converter);

	void addConverter(Class<?> sourceType, Class<?> targetType, Converter<?, ?> converter);

	void addConverter(GenericConverter converter);

	void addConverterFactory(ConverterFactory<?, ?> converterFactory);

	void removeConvertible(Class<?> sourceType, Class<?> targetType);
}
