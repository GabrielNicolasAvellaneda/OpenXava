package org.openxava.actions;


import java.util.*;

import javax.servlet.http.*;

import org.openxava.controller.meta.*;
import org.openxava.util.*;
import org.openxava.view.*;

/**
 * @author Javier Paniza
 */

public class CreateNewFromReferenceAction extends BaseAction implements INavigationAction, IRequestAction, IChainAction {
		
	private String model;
	private final static String DEFAULT_CREATION_CONTROLLER = "NewCreation";
	private String controller;
	private HttpServletRequest request;	
	private View view;
	private Stack previousViews;
	private String keyProperty;
	
	public void execute() throws Exception {
		View viewReference = new View();		
		viewReference.setModelName(getModel());
		viewReference.setRequest(request);
		getPreviousViews().push(getView());
		setView(viewReference);
		
		// Next line is for reset the cache		
		request.getSession().removeAttribute(getKeyProperty() + ".descriptionsCalculator");				
	}

	public String[] getNextControllers() throws Exception {		
		return new String[] { getController() };
	}

	public String getCustomView() {		
		return SAME_VIEW;
	}
	
	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
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

	public String getKeyProperty() {
		return keyProperty;
	}

	public void setKeyProperty(String string) {
		keyProperty = string;
	}

	public String getController() throws XavaException {
		if (controller == null) {
			String controllerModel = getModel() + "Creation";			
			if (MetaControllers.contains(controllerModel)) {
				controller = controllerModel;
			}
			else {
				controller = DEFAULT_CREATION_CONTROLLER;
			}
		}
		return controller;
	}

	public void setController(String string) {
		controller = string;
	}

	public String getNextAction() throws Exception {
		return getController() + ".new";
	}

	public Stack getPreviousViews() {
		return previousViews;
	}
	public void setPreviousViews(Stack previousViews) {
		this.previousViews = previousViews;
	}
}
