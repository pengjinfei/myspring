package com.pjf.spring.core.io;

import com.pjf.spring.util.ResourceUtils;

/**
 * 能够导入资源的类的接口
 * @author pengjinfei
 * @date 2015年12月26日
 */
public interface ResourceLoader {

	String CLASSPATH_URL_PREFIX = ResourceUtils.CLASSPATH_URL_PREFIX;

	Resource getResource(String location);

	ClassLoader getClassLoader();
}
