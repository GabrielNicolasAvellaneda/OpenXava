package org.openxava.actions;




/**
 * @author Javier Paniza
 */
public class AddImageToGalleryAction extends BaseAction implements ILoadFileAction {

	
	
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
