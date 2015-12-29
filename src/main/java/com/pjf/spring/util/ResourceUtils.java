package com.pjf.spring.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 资源工具类
 * @author pengjinfei
 * @date 2015年12月27日
 */
public abstract class ResourceUtils {

	/** classpath:application.xml */
	public static final String CLASSPATH_URL_PREFIX = "classpath:";

	/** protocol for JBoss VFS resource */
	public static final String URL_PROTOCOL_VFS = "vfs";

	/** file resource "file: " */
	public static final String FILE_URL_PREFIX = "file:";

	/** protocol for file resource */
	public static final String URL_PROTOCOL_FILE = "file";

	/** protocol for WebSphere jar file */
	public static final String URL_PROTOCOL_WSJAR = "wsjar";

	/** protocol for zip file */
	public static final String URL_PROTOCOL_ZIP = "zip";

	/** protocol for jar file */
	public static final String URL_PROTOCOL_JAR = "jar";

	/** protocol for JBoss jar file */
	public static final String URL_PROTOCOL_VFSZIP = "vfszip";

	/** Separator between JAR URL and file path */
	public static final String JAR_URL_SEPARATOR = "!/";

	/** protocol for  JBoss file */
	private static final Object URL_PROTOCOL_VFSFILE = "vfsfile";

	public static URI toURI(URL url) throws URISyntaxException {
		return toURI(url.toString());
	}

	/**
	 * 将一个给定的地址转换为URI,用"%20"替代空格
	 * @param location
	 * @return
	 * @throws URISyntaxException
	 */
	public static URI toURI(String location) throws URISyntaxException {
		return new URI(StringUtils.replace(location, " ", "%20"));
	}
	
	public static File getFile(URI resourceUri) throws FileNotFoundException {
		return getFile(resourceUri, "URI");
	}
	
	public static File getFile(URI resourceUri, String description) throws FileNotFoundException {
		Assert.notNull(resourceUri, "Resource URI must not be null");
		if (!URL_PROTOCOL_FILE.equals(resourceUri.getScheme())) {
			throw new FileNotFoundException(
					description + " cannot be resolved to absolute file path " +
					"because it does not reside in the file system: " + resourceUri);
		}
		return new File(resourceUri.getSchemeSpecificPart());
	}
	
	public static File getFile(URL resourceUrl) throws FileNotFoundException {
		return getFile(resourceUrl, "URL");
	}

	public static File getFile(URL url, String description) throws FileNotFoundException {
		Assert.notNull(url, "Resource URL must not be null");
		if (!URL_PROTOCOL_FILE.equals(url.getProtocol()))
			throw new FileNotFoundException(description + " cannot be resolved to absolute file path "
					+ "because it does not reside in the file system: " + url);
		try {
			return new File(toURI(url).getSchemeSpecificPart());
		} catch (URISyntaxException e) {
			return new File(url.getFile());
		}
	}

	public static boolean isJarURL(URL url) {
		String protocol = url.getProtocol();
		return (URL_PROTOCOL_JAR.equals(protocol) || URL_PROTOCOL_ZIP.equals(protocol)
				|| URL_PROTOCOL_VFSZIP.equals(protocol) || URL_PROTOCOL_WSJAR.equals(protocol));
	}

	/**
	 * 从一个URL中提出jar文件的URL地址，如"jar:file:/c:/almanac/my.jar!/com/mycompany/MyClass.class"
	 * 或者"jar:http://hostname/my.jar!/com/mycompany/MyClass.class"
	 * @param jarUrl
	 * @return
	 * @throws MalformedURLException
	 */
	public static URL extractJarFileURL(URL jarUrl) throws MalformedURLException {
		String urlFile = jarUrl.getFile();
		int separatorIndex= urlFile.indexOf(JAR_URL_SEPARATOR);
		if(separatorIndex!=-1){
			String jarFile=urlFile.substring(0, separatorIndex);
			try {
				return new URL(jarFile);
			} catch (MalformedURLException e) {
				// Probably no protocol in original jar URL, like "jar:C:/mypath/myjar.jar".
				// This usually indicates that the jar file resides in the file system.
				if(!jarFile.startsWith("/")){
					jarFile="/"+jarFile;
				}
				return new URL(FILE_URL_PREFIX+jarFile);
			}
		}else {
			return jarUrl;
		}
	}

	public static boolean isFileURL(URL url) {
		String protocol = url.getProtocol();
		return (URL_PROTOCOL_FILE.equals(protocol) || URL_PROTOCOL_VFSFILE.equals(protocol) ||
				URL_PROTOCOL_VFS.equals(protocol));
	}

	public static void useCachesIfNecessary(URLConnection connection) {
		//TODO 为什么JNLP可以使用Cache
		connection.setUseCaches(connection.getClass().getSimpleName().startsWith("JNLP"));
	}

}
