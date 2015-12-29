package com.pjf.spring.core.io;

import java.net.MalformedURLException;
import java.net.URL;

import com.pjf.spring.util.Assert;
import com.pjf.spring.util.ClassUtils;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月26日
 */
public class DefaultResourceLoader implements ResourceLoader {

	private ClassLoader classLoader;

	public DefaultResourceLoader() {
		this.classLoader=ClassUtils.getDefaultClassLoader();
	}

	
	public DefaultResourceLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}


	public Resource getResource(String location) {
		Assert.notNull(location,"Location must not be null");
		if(location.startsWith("/")){
			return getResourceByPath(location);
		}
		else if(location.startsWith(CLASSPATH_URL_PREFIX)){
			return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()),getClassLoader());
		}
		else {
			try {
				URL url=new URL(location);
				return new UrlResource(url);
			} catch (MalformedURLException e) {
				return getResourceByPath(location);
			}
		}
	}

	protected Resource getResourceByPath(String path) {
		return new ClassPathResource(path,getClassLoader());
	}


	public ClassLoader getClassLoader() {
		return this.classLoader==null?ClassUtils.getDefaultClassLoader():this.classLoader;
	}

}
