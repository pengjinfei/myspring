package com.pjf.spring.core.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

import com.pjf.spring.util.Assert;
import com.pjf.spring.util.ResourceUtils;
import com.pjf.spring.util.StringUtils;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月26日
 */
public class UrlResource extends AbstractFileResolvingResource {

	private final URI uri;

	private final URL url;

	private final URL cleanedUrl;

	public UrlResource(URI uri) throws MalformedURLException {
		Assert.notNull(uri, "URI must not be null");
		this.uri = uri;
		this.url = uri.toURL();
		this.cleanedUrl = getCleanedUrl(this.url, uri.toString());
	}

	public UrlResource(URL url) {
		Assert.notNull(url, "URL must not be null");
		this.url = url;
		this.cleanedUrl = getCleanedUrl(this.url, url.toString());
		this.uri = null;
	}

	public UrlResource(String path) throws MalformedURLException {
		Assert.notNull(path, "Path must not be null");
		this.uri = null;
		this.url = new URL(path);
		this.cleanedUrl = getCleanedUrl(this.url, path);
	}

	public UrlResource(String protocol, String location) throws MalformedURLException {
		this(protocol, location, null);
	}

	public UrlResource(String protocol, String location, String fragment) throws MalformedURLException {
		try {
			this.uri = new URI(protocol, location, fragment);
			this.url = this.uri.toURL();
			this.cleanedUrl = getCleanedUrl(this.url, this.uri.toString());
		} catch (URISyntaxException ex) {
			MalformedURLException exToThrow = new MalformedURLException(ex.getMessage());
			exToThrow.initCause(ex);
			throw exToThrow;
		}
	}

	private URL getCleanedUrl(URL originalUrl, String originalPath) {
		try {
			return new URL(StringUtils.cleanPath(originalPath));
		} catch (MalformedURLException ex) {
			return originalUrl;
		}
	}

	public String getDescription() {
		return "URL [" + this.url + "]";
	}

	public InputStream getInputStream() throws IOException {
		URLConnection connection = this.url.openConnection();
		ResourceUtils.useCachesIfNecessary(connection);
		try {
			return connection.getInputStream();
		} catch (IOException e) {
			if (connection instanceof HttpURLConnection) {
				((HttpURLConnection) connection).disconnect();
			}
			throw e;
		}
	}

	@Override
	public URL getURL() throws IOException {
		return this.url;
	}

	@Override
	public URI getURI() throws IOException {
		if (this.uri != null)
			return this.uri;
		else {
			return super.getURI();
		}
	}

	@Override
	public File getFile() throws IOException {
		if(this.uri!=null)
			return super.getFile(this.uri);
		else {
			return super.getFile();
		}
	}
	
	
	@Override
	public Resource createRelative(String relativePath) throws MalformedURLException {
		if (relativePath.startsWith("/")) {
			relativePath = relativePath.substring(1);
		}
		return new UrlResource(new URL(this.url, relativePath));
	}

	@Override
	public String getFilename() {
		return new File(this.url.getFile()).getName();
	}


	@Override
	public boolean equals(Object obj) {
		return (obj == this ||
			(obj instanceof UrlResource && this.cleanedUrl.equals(((UrlResource) obj).cleanedUrl)));
	}

	@Override
	public int hashCode() {
		return this.cleanedUrl.hashCode();
	}
}
