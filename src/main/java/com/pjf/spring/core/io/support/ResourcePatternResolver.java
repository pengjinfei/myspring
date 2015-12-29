package com.pjf.spring.core.io.support;

import java.io.IOException;

import com.pjf.spring.core.io.Resource;
import com.pjf.spring.core.io.ResourceLoader;

/**
 * 解析一个正则表达式表示的路径下的资源
 * @author pengjinfei
 * @date 2015年12月27日
 */
public interface ResourcePatternResolver extends ResourceLoader {

	String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

	Resource[] getResources(String locationPattern) throws IOException;
}
