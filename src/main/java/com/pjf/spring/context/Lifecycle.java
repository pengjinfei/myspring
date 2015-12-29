package com.pjf.spring.context;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月27日
 */
public interface Lifecycle {

	void start();

	void stop();

	boolean isRunning();
}
