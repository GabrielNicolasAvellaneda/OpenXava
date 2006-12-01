package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.session.*;

/**
 * 
 * @author Javier Paniza
 */

public class MinimizeImageAction extends BaseAction {
	
	private Gallery gallery;
	private Log log = LogFactory.getLog(MinimizeImageAction.class);

	public void execute() throws Exception {
		gallery.setMaximized(false);
	}

	public Gallery getGallery() {
		return gallery;
	}

	public void setGallery(Gallery gallery) {
		this.gallery = gallery;
	}

}
