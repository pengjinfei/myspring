package com.pjf.spring.beans.factory.config;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.pjf.spring.core.MethodParameter;
import com.pjf.spring.util.Assert;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月27日
 */
@SuppressWarnings("serial")
public class DependencyDescriptor implements Serializable {

	private transient MethodParameter methodParameter;

	private transient Field field;

	private Class<?> declaringClass;

	private Class<?> containingClass;

	private String methodName;

	private Class<?>[] parameterTypes;

	private int parameterIndex;

	private String fieldName;

	private final boolean required;

	private final boolean eager;

	private int nestingLevel = 1;

	private transient Annotation[] fieldAnnotations;
	
	public DependencyDescriptor(MethodParameter methodParameter, boolean required) {
		this(methodParameter, required, true);
	}

	public DependencyDescriptor(MethodParameter methodParameter, boolean required, boolean eager) {
		Assert.notNull(methodParameter, "MethodParameter must not be null");
		this.methodParameter = methodParameter;
		this.declaringClass = methodParameter.getDeclaringClass();
		this.containingClass = methodParameter.getContainingClass();
		if (this.methodParameter.getMethod() != null) {
			this.methodName = methodParameter.getMethod().getName();
			this.parameterTypes = methodParameter.getMethod().getParameterTypes();
		}
		else {
			this.parameterTypes = methodParameter.getConstructor().getParameterTypes();
		}
		this.parameterIndex = methodParameter.getParameterIndex();
		this.required = required;
		this.eager = eager;
	}

	public DependencyDescriptor(Field field, boolean required) {
		this(field, required, true);
	}

	public DependencyDescriptor(Field field, boolean required, boolean eager) {
		Assert.notNull(field, "Field must not be null");
		this.field = field;
		this.declaringClass = field.getDeclaringClass();
		this.fieldName = field.getName();
		this.required = required;
		this.eager = eager;
	}

	public DependencyDescriptor(DependencyDescriptor original) {
		this.methodParameter = (original.methodParameter != null ? new MethodParameter(original.methodParameter) : null);
		this.field = original.field;
		this.declaringClass = original.declaringClass;
		this.containingClass = original.containingClass;
		this.methodName = original.methodName;
		this.parameterTypes = original.parameterTypes;
		this.parameterIndex = original.parameterIndex;
		this.fieldName = original.fieldName;
		this.required = original.required;
		this.eager = original.eager;
		this.nestingLevel = original.nestingLevel;
		this.fieldAnnotations = original.fieldAnnotations;
	}
}
