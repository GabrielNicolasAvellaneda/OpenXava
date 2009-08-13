package org.openxava.actions;

import java.util.*;

import org.apache.commons.fileupload.*;


import org.openxava.util.*;
import org.openxava.view.*;

/**
 * @author Javier Paniza
 */

public class LoadImageAction extends BaseAction implements INavigationAction, IProcessLoadedFileAction {
	
	private List fileItems;
	private View view;
	private String newImageProperty;
	

	public void execute() throws Exception {
		Iterator i = getFileItems().iterator();
		while (i.hasNext()) {
			FileItem fi = (FileItem)i.next();
			String fileName = fi.getName();			
			if (!Is.emptyString(fileName)) {
				String propertyName = Strings.lastToken(getNewImageProperty(), ".");
				findView().setValue(propertyName, fi.get());
			}			
		}		
	}
	
	private View findView() { 
		String property = getNewImageProperty(); 
		String []m = property.split( "\\." );  
		View result = view; 
		for( int i = 0; i < m.length-1; i++ ) { 
			result = result.getSubview(m[i]); 
		} 		
		return result; 
	} 
		
	public String[] getNextControllers() {				
		return PREVIOUS_CONTROLLERS;
	}

	public String getCustomView() {		
		return PREVIOUS_VIEW;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public String getNewImageProperty() {
		return newImageProperty;
	}

	public void setNewImageProperty(String string) {
		newImageProperty = string;	
	}

	public List getFileItems() {
		return fileItems;
	}

	public void setFileItems(List fileItems) {
		this.fileItems = fileItems;
	}

}
