package org.openxava.actions;

import org.openxava.session.*;

/**
 * 
 * @author Javier Paniza
 */

public class MinimizeImageAction extends BaseAction {
	
	private Gallery gallery;

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
