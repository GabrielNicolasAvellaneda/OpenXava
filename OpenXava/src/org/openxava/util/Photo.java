package org.openxava.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Wraps a byte array to allow store photos in
 * a database with no support to <code>byte []</code> o
 * BLOBs, but yes <code>java.lang.Object</code>. <p>
 * 
 * Serialize object for long term persistence is discourage,
 * hence use this class only in extreme cases.<br> 
 * 
 * @author Javier Paniza
 */
public class Photo implements java.io.Serializable {

	public byte[] data;
	
	private Log log = LogFactory.getLog(Photo.class);

	public Photo(byte[] data) {
		this.data = data;
	}

}