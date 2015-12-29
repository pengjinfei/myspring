package com.pjf.spring.context;

/**
 * ApplicationContext的父接口，封装时间发布功能
 * @author pengjinfei
 * @date 2015年12月27日
 */
public interface ApplicationEventPublisher {

	void publishEvent(ApplicationEvent event);
}
