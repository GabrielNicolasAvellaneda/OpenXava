package org.openxava.actions;


import java.util.*;

import javax.servlet.http.*;



import org.openxava.controller.meta.*;
import org.openxava.util.*;
import org.openxava.view.*;

/**
 * @author Javier Paniza
 */

abstract public class NavigationFromReferenceBaseAction extends ReferenceBaseAction implements IChangeControllersAction, IRequestAction, IChainAction {
		
	private String model;	
	private String controller;
	private HttpServletRequest request;	
	private Stack previousViews;
	
	abstract public String getNextAction() throws Exception;
	abstract protected String getCustomController();
	abstract protected String getDefaultController();
	
	
	
	public void execute() throws Exception {
		super.execute();
		View viewReference = new View();			
		viewReference.setModelName(getModel());
		viewReference.setRequest(request);
		viewReference.setErrors(getErrors()); 
		viewReference.setMessages(getMessages());
		viewReference.putObject("xava.referenceSubview", getReferenceSubview());
		getPreviousViews().push(getView());
		setView(viewReference);		
		
		// Next line is for reset the cache		
		request.getSession().removeAttribute(getKeyProperty() + ".descriptionsCalculator");				
	}

	public String[] getNextControllers() throws Exception {		
		return new String[] { getController() };
	}
	
	public String getModel() {
		return model;
	}

	public void setModel(String string) {
		model = string;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;		
	}

	public String getController() throws XavaException {
		if (controller == null) {
			String controllerModel = getCustomController();			
			if (MetaControllers.contains(controllerModel)) {
				controller = controllerModel;
			}
			else {
				controller = getDefaultController();
			}
		}
		return controller;
	}

	public void setController(String string) {
		controller = string;
	}	

	public Stack getPreviousViews() {
		return previousViews;
	}
	public void setPreviousViews(Stack previousViews) {
		this.previousViews = previousViews;
	}
	
}
