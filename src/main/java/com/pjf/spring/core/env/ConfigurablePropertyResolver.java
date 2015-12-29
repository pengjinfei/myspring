package com.pjf.spring.core.env;

import com.pjf.spring.core.convert.support.ConfigurableConversionService;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月27日
 */
public interface ConfigurablePropertyResolver extends PropertyResolver {
	
	ConfigurableConversionService getConversionService();
	
	void setConversionService(ConfigurableConversionService conversionService);
	
	void setPlaceholderPrefix(String placeholderPrefix);
	
	void setPlaceholderSuffix(String placeholderSuffix);
	
	void setValueSeparator(String valueSeparator);
	
	void setIgnoreUnresolvableNestedPlaceholders(boolean ignoreUnresolvableNestedPlaceholders);
	
	void setRequiredProperties(String... requiredProperties);
	
	void validateRequiredProperties() throws MissingRequiredPropertiesException;
}
