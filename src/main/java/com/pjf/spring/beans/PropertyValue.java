package com.pjf.spring.beans;

import java.beans.PropertyDescriptor;
import java.io.Serializable;

import com.pjf.spring.util.Assert;
import com.pjf.spring.util.ObjectUtils;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月28日
 */
@SuppressWarnings("serial")
public class PropertyValue extends BeanMetadataAttributeAccessor implements Serializable {

	private final String name;

	private final Object value;

	private Object source;

	private boolean optional = false;

	private boolean converted = false;

	private Object convertedValue;

	volatile Boolean conversionNecessary;

	transient volatile Object resolvedTokens;

	transient volatile PropertyDescriptor resolvedDescriptor;

	public PropertyValue(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public PropertyValue(PropertyValue original) {
		Assert.notNull(original, "Original must not be null");
		this.name = original.getName();
		this.value = original.getValue();
		this.source = original.getSource();
		this.optional = original.isOptional();
		this.converted = original.converted;
		this.convertedValue = original.convertedValue;
		this.conversionNecessary = original.conversionNecessary;
		this.resolvedTokens = original.resolvedTokens;
		this.resolvedDescriptor = original.resolvedDescriptor;
		copyAttributesFrom(original);
	}


	public PropertyValue(PropertyValue original, Object newValue) {
		Assert.notNull(original, "Original must not be null");
		this.name = original.getName();
		this.value = newValue;
		this.source = original;
		this.optional = original.isOptional();
		this.conversionNecessary = original.conversionNecessary;
		this.resolvedTokens = original.resolvedTokens;
		this.resolvedDescriptor = original.resolvedDescriptor;
		copyAttributesFrom(original);
	}
	
	public String getName() {
		return this.name;
	}

	public Object getValue() {
		return this.value;
	}
	
	public PropertyValue getOriginalPropertyValue() {
		PropertyValue original = this;
		while (original.source instanceof PropertyValue && original.source != original) {
			original = (PropertyValue) original.source;
		}
		return original;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public boolean isOptional() {
		return this.optional;
	}

	public synchronized boolean isConverted() {
		return this.converted;
	}

	public synchronized void setConvertedValue(Object value) {
		this.converted = true;
		this.convertedValue = value;
	}

	public synchronized Object getConvertedValue() {
		return this.convertedValue;
	}


	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof PropertyValue)) {
			return false;
		}
		PropertyValue otherPv = (PropertyValue) other;
		return (this.name.equals(otherPv.name) &&
				ObjectUtils.nullSafeEquals(this.value, otherPv.value) &&
				ObjectUtils.nullSafeEquals(this.source, otherPv.source));
	}

	@Override
	public int hashCode() {
		return this.name.hashCode() * 29 + ObjectUtils.nullSafeHashCode(this.value);
	}

	@Override
	public String toString() {
		return "bean property '" + this.name + "'";
	}
}
