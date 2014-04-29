package org.openxava.actions;

/**
 * Logic of action AttachedFiles.delete in default-controllers.xml
 * 
 * @author Jeromy Altuna
 */
public class DeleteFileAction extends ViewBaseAction {
	
	private String newFileProperty;
	
	public void execute() throws Exception {
		getView().setValue(getNewFileProperty(), null);		
	}

	public String getNewFileProperty() {
		return newFileProperty;
	}

	public void setNewFileProperty(String newFileProperty) {
		this.newFileProperty = newFileProperty;
	}
}
