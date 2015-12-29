package com.pjf.spring.core.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import com.pjf.spring.util.ResourceUtils;

/**
 * File资源的抽象实现
 * @author pengjinfei
 * @date 2015年12月26日
 */
public abstract class AbstractFileResolvingResource extends AbstractResource {

	@Override
	public File getFile() throws IOException {
		URL url = getURL();
		if (url.getProtocol().startsWith(ResourceUtils.URL_PROTOCOL_VFS)) {
			//TODO 根据VFS资源获得文件
		}
		return ResourceUtils.getFile(url, getDescription());
	}

	protected File getFile(URI uri) throws IOException{
		/*if (uri.getScheme().startsWith(ResourceUtils.URL_PROTOCOL_VFS)) {
			return VfsResourceDelegate.getResource(uri).getFile();
		}*/
		return ResourceUtils.getFile(uri, getDescription());
	}
	
	@Override
	protected File getFileForLastModifiedCheck() throws IOException {
		URL url = getURL();
		if (ResourceUtils.isJarURL(url)) {
			URL actualUrl = ResourceUtils.extractJarFileURL(url);
			// todo VfsResource
			/*
			 * if(actualUrl.getProtocol().startsWith(ResourceUtils.
			 * URL_PROTOCOL_VFS)){ return
			 * VfsResourceDelegate.getResource(actualUrl).getFile(); }
			 */
			return ResourceUtils.getFile(actualUrl, "Jar URL");
		} else {
			return getFile();
		}

	}

	@Override
	public boolean exists() {
		try {
			URL url = getURL();
			if (ResourceUtils.isFileURL(url)) {
				return getFile().exists();
			} else {
				URLConnection connection = url.openConnection();
				customizeConnection(connection);
				HttpURLConnection httpURLConnection = (connection instanceof HttpURLConnection
						? (HttpURLConnection) connection : null);
				if(httpURLConnection!=null){
					int code=httpURLConnection.getResponseCode();
					if(code==HttpURLConnection.HTTP_OK){
						return true;
					}else if (code==HttpURLConnection.HTTP_NOT_FOUND) {
						return false;
					}
				}
				if(connection.getContentLength()>=0){
					return true;
				}
				if(httpURLConnection!=null){
					httpURLConnection.disconnect();
					return false;
				}else {
					InputStream is = getInputStream();
					is.close();
					return true;
				}
			}
		} catch (IOException e) {
			return false;
		}
	}

	protected void customizeConnection(URLConnection connection) throws IOException {
		ResourceUtils.useCachesIfNecessary(connection);
		if (connection instanceof HttpURLConnection) {
			customizeConnection((HttpURLConnection) connection);
		}
	}

	protected void customizeConnection(HttpURLConnection connection) throws IOException {
		//TODO connection.setRequestMethod("HEAD")的作用？
		connection.setRequestMethod("HEAD");
	}

	@Override
	public boolean isReadable() {
		try {
			URL url=getURL();
			if(ResourceUtils.isFileURL(url)){
				File file = getFile();
				return (file.canRead()&&!file.isDirectory());
			}else {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public long contentLength() throws IOException {
		URL url=getURL();
		if(ResourceUtils.isFileURL(url)){
			return getFile().length();
		}else {
			URLConnection connection=url.openConnection();
			customizeConnection(connection);
			return connection.getContentLength();
		}
	}

	@Override
	public long lastModified() throws IOException {
		URL url = getURL();
		if (ResourceUtils.isFileURL(url) || ResourceUtils.isJarURL(url)) {
			return super.lastModified();
		}
		else {
			URLConnection con = url.openConnection();
			customizeConnection(con);
			return con.getLastModified();
		}
	}

	
	
	
	/*
	 * private static class VfsResourceDelegate{
	 * 
	 * public static Resource getResource(URL url) throws IOException { return
	 * new VfsResource(VfsUtils.getRoot(url)); }
	 * 
	 * public static Resource getResource(URI uri) throws IOException{ return
	 * new VfsResource(VfsUtils.getRoot(uri)); } }
	 */
}
