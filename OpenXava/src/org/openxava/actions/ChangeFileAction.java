package org.openxava.actions;

import javax.inject.*;

/**
 * Logic of action AttachedFiles.change in default-controllers.xml
 * 
 * @author Jeromy Altuna
 */
public class ChangeFileAction extends ViewBaseAction implements ILoadFileAction {
	
	@Inject
	private String newFileProperty;	
	
	public String[] getNextControllers() throws Exception {
		return new String [] { "UploadFile" };
	}

	public void execute() throws Exception {
		showDialog();
	}

	public String getCustomView() throws Exception {
		return "xava/editors/changeFile";
	}

	public boolean isLoadFile() {
		return true;
	}

	public String getNewFileProperty() {
		return newFileProperty;
	}

	public void setNewFileProperty(String newFileProperty) {
		this.newFileProperty = newFileProperty;
	}		
}
