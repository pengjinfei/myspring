package com.pjf.spring.core.env;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月27日
 */
public class MissingRequiredPropertiesException extends IllegalStateException {

	private static final long serialVersionUID = -5046254550063464786L;

	private final Set<String> missingRequiredProperties = new LinkedHashSet<String>();
	
	public Set<String> getMissingRequiredProperties() {
		return missingRequiredProperties;
	}
	
	void addMissingRequiredProperty(String key) {
		missingRequiredProperties.add(key);
	}
	
	@Override
	public String getMessage() {
		return String.format(
				"The following properties were declared as required but could " +
				"not be resolved: %s", this.getMissingRequiredProperties());
	}
}
