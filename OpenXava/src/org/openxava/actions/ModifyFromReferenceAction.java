package org.openxava.actions;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.view.*;



/**
 * @author Javier Paniza
 */

public class ModifyFromReferenceAction extends NavigationFromReferenceBaseAction  {
	
	private boolean exists = true;
	private Log log = LogFactory.getLog(ModifyFromReferenceAction.class);
	
	public void execute() throws Exception {
		super.execute();
		Map key = getReferenceSubview().getKeyValuesWithValue();
		if (key.isEmpty()) {
			addError("cannot_modify_empty_reference");
			View view = (View) getPreviousViews().pop();
			view.setRequest(getView().getRequest());
			setView(view);
			exists = false;
			return;
		}
		getView().setKeyEditable(false);
		getView().setValues(key);
	}
		
	public String getCustomController() {	
		return getModel() + "Modification";
	}
	
	public String getDefaultController() {
		return "Modification";
	}
	
	public String[] getNextControllers() throws Exception {		
		return exists?super.getNextControllers():SAME_CONTROLLERS;
	}
	
	public String getNextAction() throws Exception {
		return exists?getController() + ".search":null;
	}

}
