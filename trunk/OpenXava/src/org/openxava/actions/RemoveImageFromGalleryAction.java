package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.session.*;

/**
 * 
 * @author Javier Paniza
 */

public class RemoveImageFromGalleryAction extends BaseAction {
	
	private Gallery gallery;
	private String oid;
	private Log log = LogFactory.getLog(RemoveImageFromGalleryAction.class);
	
	public void execute() throws Exception {
		gallery.removeImage(oid);
	}

	public Gallery getGallery() {
		return gallery;
	}

	public void setGallery(Gallery gallery) {
		this.gallery = gallery;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

}
