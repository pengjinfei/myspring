package com.pjf.spring.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月26日
 */
public interface InputStreamSource {

	InputStream getInputStream() throws IOException;
}
