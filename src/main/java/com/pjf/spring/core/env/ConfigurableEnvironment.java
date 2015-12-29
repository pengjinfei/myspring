package com.pjf.spring.core.env;

import java.util.Map;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月27日
 */
public interface ConfigurableEnvironment extends Environment, ConfigurablePropertyResolver {

	void setActiveProfiles(String...profiles);
	
	void addActiveProfile(String profile);
	
	void setDefaultProfiles(String...profiles);
	
	MutablePropertySources getPropertySources();
	
	Map<String, Object> getSystemEnvironment();
	
	Map<String, Object> getSystemProperties();
	
	void merge(ConfigurableEnvironment parent);
}
