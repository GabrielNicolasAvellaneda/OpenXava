package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Javier Paniza
 */
public class AddImageToGalleryAction extends BaseAction implements ILoadFileAction {

	private static Log log = LogFactory.getLog(AddImageToGalleryAction.class);
	
	public void execute() throws Exception {
		
	}
	
	public String getCustomView() {		
		return "xava/editors/addImages";
	}

	public boolean isLoadFile() {		
		return true;
	}

	public String[] getNextControllers() throws Exception {
		return new String [] { "LoadImageIntoGallery" };
	}
	
}
