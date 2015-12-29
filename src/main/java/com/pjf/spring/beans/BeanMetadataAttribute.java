package com.pjf.spring.beans;

import com.pjf.spring.util.Assert;
import com.pjf.spring.util.ObjectUtils;

/**
 * 以name-value的形式存储bean的元数据，source存放配置源
 * @author pengjinfei
 * @date 2015年12月28日
 */
public class BeanMetadataAttribute implements BeanMetadataElement {

	private final String name;
	
	private final Object value;
	
	private Object source;
	
	
	public BeanMetadataAttribute(String name, Object value) {
		Assert.notNull(name, "Name must not be null");
		this.name = name;
		this.value = value;
	}

	public Object getValue(){
		return this.value;
	}

	public String getName() {
		return name;
	}



	public void setSource(Object source) {
		this.source = source;
	}



	public Object getSource() {
		return this.source;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof BeanMetadataAttribute)) {
			return false;
		}
		BeanMetadataAttribute otherMa = (BeanMetadataAttribute) other;
		return (this.name.equals(otherMa.name) &&
				ObjectUtils.nullSafeEquals(this.value, otherMa.value) &&
				ObjectUtils.nullSafeEquals(this.source, otherMa.source));
	}

	@Override
	public int hashCode() {
		return this.name.hashCode() * 29 + ObjectUtils.nullSafeHashCode(this.value);
	}

	@Override
	public String toString() {
		return "metadata attribute '" + this.name + "'";
	}

}
