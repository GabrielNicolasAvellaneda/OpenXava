package org.openxava.actions;

import javax.inject.*;

import org.apache.commons.lang.*;
import org.openxava.model.*;
import org.openxava.util.*;

/**
 * Logic of Library.choose action in default-controllers.xml
 * 
 * @author Jeromy Altuna
 */
public class ChooseFilesForLibraryAction extends ViewBaseAction implements ILoadFileAction{
	
	@Inject
	private String newLibraryProperty;
	
	@Override
	public void execute() throws Exception {
		String libraryId = getView().getValueString(getNewLibraryProperty());
		if(Is.emptyString(libraryId)) {
			libraryId = (String) new org.openxava.calculators.UUIDCalculator().calculate();
			getView().setValue(getNewLibraryProperty(), libraryId);
			if(!getView().isKeyEditable()) {
				MapFacade.setValues(getView().getModelName(), 
									getView().getKeyValues(), 
									ArrayUtils.toMap(new String[][] { 
										{ getNewLibraryProperty(), libraryId }  
									}));
			}
		}
		showDialog();
	}
	
	@Override
	public String[] getNextControllers() throws Exception {
		return new String[]{ "UploadFileIntoLibrary" };
	}

	@Override
	public String getCustomView() throws Exception {
		return "xava/editors/chooseFiles";
	}

	@Override
	public boolean isLoadFile() {
		return true;
	}
	
	public String getNewLibraryProperty() {
		return newLibraryProperty;
	}
	public void setNewLibraryProperty(String newLibraryProperty) {
		this.newLibraryProperty = newLibraryProperty;
	}
}
