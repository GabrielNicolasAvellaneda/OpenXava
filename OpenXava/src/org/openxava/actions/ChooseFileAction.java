package org.openxava.actions;

import javax.inject.*;

/**
 * Logic of AttachedFile.change action in default-controllers.xml
 * 
 * @author Jeromy Altuna
 */
public class ChooseFileAction extends ViewBaseAction implements ILoadFileAction {
	
	@Inject
	private String newFileProperty;	
	
	@Override
	public void execute() throws Exception {
		showDialog();
	}
	
	@Override
	public String[] getNextControllers() throws Exception {
		return new String [] { "UploadFile" };
	}
	
	@Override
	public String getCustomView() throws Exception {
		return "xava/editors/chooseFile";
	}
	
	@Override
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
