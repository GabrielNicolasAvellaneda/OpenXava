package org.openxava.util;

import java.io.*;
import java.net.*;

/**
 * @author Javier Paniza
 */
public class Resources {
	
	public static String loadAsString(Class claseBase, String nombreRecurso) throws IOException {
		URL recurso = claseBase.getClassLoader().getResource(nombreRecurso);
		if (recurso == null) {
			throw new IOException(XavaResources.getString("resource_not_found", nombreRecurso));
		}
		InputStream is = recurso.openStream();
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
