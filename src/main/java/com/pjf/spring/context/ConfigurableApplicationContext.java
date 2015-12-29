package com.pjf.spring.context;

import java.io.Closeable;

import com.pjf.spring.core.env.ConfigurableEnvironment;
import com.pjf.spring.core.env.Environment;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月27日
 */
public interface ConfigurableApplicationContext extends ApplicationContext, Lifecycle, Closeable {

	/** 配置路径的分隔符 */
	String CONFIG_LOCATION_DELIMITERS = ",; \t\n";
	
	/** ConversionService bean的名称 */
	String CONVERSION_SERVICE_BEAN_NAME = "conversionService";
	
	/** Name of the LoadTimeWeaver bean in the factory */
	String LOAD_TIME_WEAVER_BEAN_NAME = "loadTimeWeaver";
	
	/**
	 * Name of the {@link Environment} bean in the factory.
	 */
	String ENVIRONMENT_BEAN_NAME = "environment";

	/**
	 * Name of the System properties bean in the factory.
	 * @see java.lang.System#getProperties()
	 */
	String SYSTEM_PROPERTIES_BEAN_NAME = "systemProperties";

	/**
	 * Name of the System environment bean in the factory.
	 * @see java.lang.System#getenv()
	 */
	String SYSTEM_ENVIRONMENT_BEAN_NAME = "systemEnvironment";
	
	void setId(String id);
	
	void setParent(ApplicationContext parent);
	
	
	ConfigurableEnvironment getEnvironment();
	
	void setEnvironment(ConfigurableEnvironment environment);
	//TODO
	
}
