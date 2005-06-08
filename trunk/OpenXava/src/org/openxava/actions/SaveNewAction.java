package org.openxava.actions;

import java.util.*;

import javax.ejb.*;

import org.openxava.model.*;
import org.openxava.util.XavaException;
import org.openxava.validators.*;
import org.openxava.view.*;

/**
 * @author Javier Paniza
 */

public class SaveNewAction extends BaseAction implements INavigationAction {
	
	private View view;	
	private String [] nextControllers = null;
	private String nextView = null;
	private Stack previousViews;
	
	public void execute() throws Exception {
		nextView = SAME_VIEW;		
		try {					
			// Create
			MapFacade.create(view.getModelName(), getValuesToSave());
			nextControllers = PREVIOUS_CONTROLLERS;
			nextView = DEFAULT_VIEW;				
			if (!getPreviousViews().empty()) {
				setView((View) getPreviousViews().pop());
			}
			resetDescriptionsCache();
		}
		catch (ValidationException ex) {			
			addErrors(ex.getErrors());
		}
		catch (DuplicateKeyException ex) {
			addError("no_create_exists");
		}
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
