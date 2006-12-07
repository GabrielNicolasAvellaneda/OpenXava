package org.openxava.actions;

import java.util.*;

import org.apache.commons.fileupload.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.session.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */

public class LoadImageIntoGalleryAction extends BaseAction implements INavigationAction, IProcessLoadedFileAction {

	private List fileItems;
	private Gallery gallery;
	private static Log log = LogFactory.getLog(LoadImageIntoGalleryAction.class);

	public void execute() throws Exception {		
		Iterator i = getFileItems().iterator();
		int c = 0;
		while (i.hasNext()) {
			FileItem fi = (FileItem)i.next();					
			if (!Is.emptyString(fi.getName())) {
				getGallery().addImage(fi.get());
				c++;
			}			
		}		
		if (c == 1)	addMessage("image_added_to_gallery");
		else if (c > 1) addMessage("images_added_to_gallery", new Integer(c));
	}

	public String[] getNextControllers() {		
		return PREVIOUS_CONTROLLERS;
	}

	public String getCustomView() {		
		return PREVIOUS_VIEW; 
	}

	public List getFileItems() {
		return fileItems;
	}

	public void setFileItems(List fileItems) {
		this.fileItems = fileItems;
	}

	public Gallery getGallery() {
		return gallery;
	}

	public void setGallery(Gallery gallery) {
		this.gallery = gallery;
	}

}
