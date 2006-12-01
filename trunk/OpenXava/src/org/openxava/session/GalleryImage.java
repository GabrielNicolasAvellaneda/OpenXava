package org.openxava.session;

import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Used by Gallery. <p>
 * 
 * It's persistent, using Hibernate.<br>
 * 
 * @author Javier Paniza
 */

public class GalleryImage implements Serializable {
	
	private String oid;
	private String galleryOid;
	private byte [] image;
	
	private Log log = LogFactory.getLog(GalleryImage.class);
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getGalleryOid() {
		return galleryOid;
	}
	public void setGalleryOid(String galleryOid) {
		this.galleryOid = galleryOid;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	
}
