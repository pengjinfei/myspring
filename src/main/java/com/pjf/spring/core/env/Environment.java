package com.pjf.spring.core.env;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月26日
 */
public interface Environment {

	String[] getActiveProfiles();
	
	String[] getDefaultProfiles();

	boolean acceptsProfiles(String... profiles);
}
