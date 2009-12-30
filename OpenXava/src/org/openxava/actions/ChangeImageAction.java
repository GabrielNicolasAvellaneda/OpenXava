package org.openxava.actions;




/**
 * @author Javier Paniza
 */

public class ChangeImageAction extends ViewBaseAction implements ILoadFileAction { 
		
	private String newImageProperty;	
	
	public void execute() throws Exception {
		showDialog(); 
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
