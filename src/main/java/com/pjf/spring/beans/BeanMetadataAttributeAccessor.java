package com.pjf.spring.beans;

import com.pjf.spring.core.AttributeAccessorSupport;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月28日
 */
@SuppressWarnings("serial")
public class BeanMetadataAttributeAccessor extends AttributeAccessorSupport implements BeanMetadataElement{

	private Object source;
	
	public Object getSource() {
		return this.source;
	}

	public void setSource(Object source) {
		this.source = source;
	}
	
	public void addMetadataAttribute(BeanMetadataAttribute attribute){
		super.setAttribute(attribute.getName(), attribute);
	}
	
	public BeanMetadataAttribute getMetadataAttribute(String name){
		return (BeanMetadataAttribute)super.getAttribute(name);
	}

	@Override
	public void setAttribute(String name, Object value) {
		super.setAttribute(name, new BeanMetadataAttribute(name, value));
	}

	@Override
	public Object getAttribute(String name) {
		BeanMetadataAttribute attribute=(BeanMetadataAttribute)super.getAttribute(name);
		return (attribute!=null?attribute.getValue():null);
	}

	@Override
	public Object removeAttribute(String name) {
		BeanMetadataAttribute attribute=(BeanMetadataAttribute)super.removeAttribute(name);
		return (attribute!=null?attribute.getValue():null);
	}
	
	
	
}
