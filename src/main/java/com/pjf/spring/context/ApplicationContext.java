package com.pjf.spring.context;

import com.pjf.spring.beans.factory.HierarchicalBeanFactory;
import com.pjf.spring.beans.factory.ListableBeanFactory;
import com.pjf.spring.beans.factory.config.AutowireCapableBeanFactory;
import com.pjf.spring.core.env.EnvironmentCapable;
import com.pjf.spring.core.io.support.ResourcePatternResolver;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月27日
 */
public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, HierarchicalBeanFactory,
MessageSource, ApplicationEventPublisher, ResourcePatternResolver{

	String getId();
	
	String getApplicationName();
	
	String getDisplayName();
	
	long getStartupDate();
	
	ApplicationContext getParent();
	
	AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException;
}
