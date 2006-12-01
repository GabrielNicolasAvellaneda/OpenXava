package org.openxava.actions;

import java.util.*;

import javax.ejb.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.model.*;
import org.openxava.validators.*;
import org.openxava.view.*;

/**
 * Update (update, create, etc) a reference from another view, and returns to 
 * the previous view after it. <p>
 * 
 * @author Javier Paniza
 */

public abstract class UpdateReferenceBaseAction extends BaseAction implements INavigationAction {
	
	private View view;	
	private String [] nextControllers = null;
	private String nextView = SAME_VIEW;
	private Stack previousViews;
	private Log log = LogFactory.getLog(UpdateReferenceBaseAction.class);
	
	protected void returnsToPreviousViewUpdatingReferenceView(Map key) throws Exception {
		nextControllers = PREVIOUS_CONTROLLERS;
		nextView = DEFAULT_VIEW;				
		if (!getPreviousViews().empty()) {				
			View referenceSubview = (View) getView().getObject("xava.referenceSubview");
			referenceSubview.setValuesNotifying(key); 				
			referenceSubview.findObject();	
			View previousView = (View) getPreviousViews().pop();
			setView(previousView);
		}
		resetDescriptionsCache();
	}
	
	protected Map getValuesToSave() throws Exception {
		return getView().getValues();
	}
	
	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public String[] getNextControllers() {		
		return nextControllers;
	}

	public String getCustomView() {				
		return nextView;
	}

	public Stack getPreviousViews() {
		return previousViews;
	}
	public void setPreviousViews(Stack previousViews) {
		this.previousViews = previousViews;
	}
	
}
