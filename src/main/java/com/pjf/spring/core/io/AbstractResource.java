package com.pjf.spring.core.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.pjf.spring.core.NestedIOException;
import com.pjf.spring.util.Assert;
import com.pjf.spring.util.ResourceUtils;

/**
 * Resource类的抽象实现
 * @author pengjinfei
 * @date 2015年12月27日
 */
public abstract class AbstractResource implements Resource {

	
	public boolean exists() {
		try {
			return getFile().exists();
		} catch (IOException e) {
			try {
				InputStream iStream = this.getInputStream();
				iStream.close();
				return true;
			} catch (IOException e1) {
				return false;
			}
		}
	}

	public boolean isReadable() {
		return true;
	}

	public boolean isOpen() {
		return false;
	}

	public URL getURL() throws IOException {
		throw new FileNotFoundException(getDescription() + " cannot be resolved to URL");
	}

	/**
	 * 根据URL得到URI
	 */
	public URI getURI() throws IOException {
		URL url = getURL();
		try {
			return ResourceUtils.toURI(url);
		} catch (URISyntaxException e) {
			throw new NestedIOException("Invalid URI [" + url + "]", e);
		}
	}

	public File getFile() throws IOException {
		throw new FileNotFoundException(getDescription() + " cannot be resolved to absolute file path");
	}

	
	public long contentLength() throws IOException {
		InputStream is = this.getInputStream();
		Assert.state(is != null, "resource input stream must not be null");
		try {
			long size = 0;
			byte[] buf = new byte[255];
			int read;
			while ((read = is.read(buf)) != -1) {
				size += read;
			}
			return size;
		} finally {
			try {
				is.close();
			} catch (Exception e) {
			}
		}
	}

	public long lastModified() throws IOException {
		long lastModified = getFileForLastModifiedCheck().lastModified();
		if(lastModified==0L){
			throw new FileNotFoundException(getDescription() +
					" cannot be resolved in the file system for resolving its last-modified timestamp");
		}
		return lastModified;
	}

	
	protected File getFileForLastModifiedCheck() throws IOException {
		return getFile();
	}

	public Resource createRelative(String relativePath) throws IOException {
		throw new FileNotFoundException("Cannot create a relative resource for " + getDescription());
	}

	public String getFilename() {
		return null;
	}

	@Override
	public int hashCode() {
		return getDescription().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==this)
			return true;
		if(!(obj instanceof Resource))
			return false;
		return ((Resource)obj).getDescription().equals(getDescription());
	}

	@Override
	public String toString() {
		return getDescription();
	}
	
	

}
