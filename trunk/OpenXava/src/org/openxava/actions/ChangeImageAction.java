package org.openxava.actions;

/**
 * @author Javier Paniza
 */

public class ChangeImageAction extends BaseAction implements ILoadFileAction {
		
	private String newImageProperty;	

	public void execute() throws Exception {		
	}

	public String[] getNextControllers() {		
		return new String [] { "LoadImage" }; 
	}

	public String getCustomView() {		
		return "xava/editors/changeImage";
	}

	public boolean isLoadFile() {		
		return true;
	}

	public String getNewImageProperty() {
		return newImageProperty;
	}

	public void setNewImageProperty(String string) {
		newImageProperty = string;
	}

}
