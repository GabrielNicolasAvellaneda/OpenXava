package org.openxava.actions;

import java.util.*;

import javax.servlet.http.*;

import org.apache.commons.fileupload.*;
import org.openxava.util.*;
import org.openxava.view.*;


/**
 * @author Javier Paniza
 */

public class LoadImageAction extends BaseAction implements INavigationAction, IRequestAction {
	
	private HttpServletRequest request;
	private View view;
	private String newImageProperty;

	public void execute() throws Exception {		
		DiskFileUpload fu = new DiskFileUpload();
		fu.setSizeMax(1000000);
		fu.setSizeThreshold(4096);			
		String dir = System.getProperty("java.io.tmpdir");
		if (Is.emptyString(dir)) {
			dir = System.getProperty("user.home");
		}		
		
		fu.setRepositoryPath(dir);				
		List fileItems = fu.parseRequest(request);
		
		Iterator i = fileItems.iterator();
		while (i.hasNext()) {
			FileItem fi = (FileItem)i.next();
			String fileName = fi.getName();
			if (fileName != null) {
				getView().setValue(getNewImageProperty(), fi.get());
			}			
		}
	}

	public String[] getNextControllers() {		
		return DEFAULT_CONTROLLERS;
	}

	public String getCustomView() {		
		return DEFAULT_VIEW;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;		
	}
		
	public View getView() {
		return view;
	}

	public void setView(View vista) {
		this.view = vista;
	}

	public String getNewImageProperty() {
		return newImageProperty;
	}

	public void setNewImageProperty(String string) {
		newImageProperty = string;	
	}

}
