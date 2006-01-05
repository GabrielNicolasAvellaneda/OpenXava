package org.openxava.actions;

import java.util.*;

import javax.servlet.http.*;

import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.disk.*;
import org.apache.commons.fileupload.servlet.*;
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
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1000000);
					
		ServletFileUpload upload = new ServletFileUpload(factory);
		List fileItems = upload.parseRequest(request);
		
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

	public void setView(View view) {
		this.view = view;
	}

	public String getNewImageProperty() {
		return newImageProperty;
	}

	public void setNewImageProperty(String string) {
		newImageProperty = string;	
	}

}
