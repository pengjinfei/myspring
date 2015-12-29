package com.pjf.spring.core.convert;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月27日
 */
public interface ConversionService {

	boolean canConvert(Class<?> sourceType, Class<?> targetType);

	boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType);

	<T> T convert(Object source, Class<T> targetType);


	Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);
}
