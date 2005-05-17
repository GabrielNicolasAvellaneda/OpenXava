package org.openxava.util;

import java.io.*;
import java.net.*;

/**
 * @author Javier Paniza
 */
public class Resources {
	
	public static String loadAsString(Class baseClass, String resourceName) throws IOException {
		URL resource = baseClass.getClassLoader().getResource(resourceName);
		if (resource == null) {
			throw new IOException(XavaResources.getString("resource_not_found", resourceName));
		}
		InputStream is = resource.openStream();
		StringBuffer sb = new StringBuffer();
		byte [] buf = new byte[500];
		int c = is.read(buf);
		while (c >= 0) {
			sb.append(new String(buf, 0, c));
			c = is.read(buf);
		}
		return sb.toString();
	}

}
