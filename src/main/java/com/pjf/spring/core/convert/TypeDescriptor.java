package com.pjf.spring.core.convert;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import com.pjf.spring.core.ResolvableType;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月27日
 */
@SuppressWarnings("serial")
public class TypeDescriptor implements Serializable{

	static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];

	private static final Map<Class<?>, TypeDescriptor> commonTypesCache = new HashMap<Class<?>, TypeDescriptor>(18);

	private static final Class<?>[] CACHED_COMMON_TYPES = {
			boolean.class, Boolean.class, byte.class, Byte.class, char.class, Character.class,
			double.class, Double.class, int.class, Integer.class, long.class, Long.class,
			float.class, Float.class, short.class, Short.class, String.class, Object.class};

	static {
		for (Class<?> preCachedClass : CACHED_COMMON_TYPES) {
			commonTypesCache.put(preCachedClass, valueOf(preCachedClass));
		}
	}


	private final Class<?> type;

	private final ResolvableType resolvableType;

	private final Annotation[] annotations;

}
