package com.pjf.spring.core.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.pjf.spring.util.Assert;
import com.pjf.spring.util.ClassUtils;
import com.pjf.spring.util.ObjectUtils;
import com.pjf.spring.util.StringUtils;

/**
 * 使用指定的class或者指定的Classloader导入类路径资源，
 * 
 * @author pengjinfei
 * @date 2015年12月27日
 */
public class ClassPathResource extends AbstractFileResolvingResource {

	private final String path;

	private ClassLoader classLoader;

	private Class<?> clazz;

	public ClassPathResource(String path) {
		this(path, null);
	}

	public ClassPathResource(String path, ClassLoader classLoader) {
		Assert.notNull(path, "Path must not null");
		String pathToUse = StringUtils.cleanPath(path);
		if (pathToUse.startsWith("/")) {
			pathToUse = pathToUse.substring(1);
		}
		this.path = pathToUse;
		this.classLoader = (classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader);
	}

	public ClassPathResource(String path, ClassLoader classLoader, Class<?> clazz) {
		this.path = StringUtils.cleanPath(path);
		this.classLoader = classLoader;
		this.clazz = clazz;
	}

	public String getPath() {
		return path;
	}

	public ClassLoader getClassLoader() {
		return classLoader;
	}

	protected URL resolveURL() {
		if (this.clazz != null) {
			return this.clazz.getResource(this.path);
		} else if (this.classLoader != null) {
			return this.classLoader.getResource(this.path);
		} else {
			return ClassLoader.getSystemResource(this.path);
		}
	}

	@Override
	public boolean exists() {
		return (resolveURL() != null);
	}

	public String getDescription() {
		StringBuilder builder = new StringBuilder("class path resource [");
		String pathToUse = path;
		if (this.clazz != null && !pathToUse.startsWith("/")) {
			builder.append(ClassUtils.classPackageAsResourcePath(this.clazz));
			builder.append("/");
		}
		if(pathToUse.startsWith("/")){
			pathToUse=pathToUse.substring(1);
		}
		builder.append(pathToUse);
		builder.append("]");
		return builder.toString();
	}

	public InputStream getInputStream() throws IOException {
		InputStream is;
		if (this.clazz != null) {
			is = this.clazz.getResourceAsStream(this.path);
		} else if (this.classLoader != null) {
			is = this.classLoader.getResourceAsStream(this.path);
		} else {
			is = ClassLoader.getSystemResourceAsStream(this.path);
		}
		if (is == null) {
			throw new FileNotFoundException(getDescription() + " cannot be opened because it does not exist");
		}
		return is;
	}

	@Override
	public URL getURL() throws IOException {
		URL url = resolveURL();
		if (url == null) {
			throw new FileNotFoundException(getDescription() + " cannot be resolved to URL because it does not exist");
		}
		return url;
	}

	@Override
	public Resource createRelative(String relativePath) throws IOException {
		String pathToUse = StringUtils.applyRelativePath(this.path, relativePath);
		return new ClassPathResource(pathToUse, this.classLoader, this.clazz);
	}

	@Override
	public String getFilename() {
		return StringUtils.getFileName(this.path);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof ClassPathResource) {
			ClassPathResource otherRes = (ClassPathResource) obj;
			return (this.path.equals(otherRes.path) &&
					ObjectUtils.nullSafeEquals(this.classLoader, otherRes.classLoader) &&
					ObjectUtils.nullSafeEquals(this.clazz, otherRes.clazz));
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.path.hashCode();
	}
}
