package org.openxava.actions;

import java.util.*;

import org.apache.commons.logging.*;
import org.openxava.util.*;
import org.openxava.view.*;

/**
 * 
 * @author Javier Paniza
 */

abstract public class ViewBaseAction extends BaseAction  {
	
	private static Log log = LogFactory.getLog(ViewBaseAction.class);
	
	private View view;
	private Stack previousViews; 

	/**
	 * @since 4m1
	 */
	protected void showNewView() { 
		assertPreviousViews("showNewView()");
		getView().putObject("xava.mode", getManager().getModeName());
		View newView = new View();			
		newView.setRequest(getRequest());
		newView.setErrors(getErrors()); 
		newView.setMessages(getMessages());
		getPreviousViews().push(getView());
		setView(newView);		
		setNextMode(DETAIL);
	}

	/**
	 * @since 4m1
	 */	
	protected void returnToPreviousView() {		
		if (getPreviousViews() != null && !getPreviousViews().empty()) {				
			View previousView = (View) getPreviousViews().pop();
			previousView.setRequest(getRequest());
			setView(previousView);
			setNextMode((String) getView().getObject("xava.mode"));
		}
		else {
			log.warn(XavaResources.getString(getRequest(), 
				"use_object_previousViews_required", "returnToPreviousView()", getClass().getName())); 
		}
	}
	
	/**
	 * @since 4m1
	 */	
	protected View getPreviousView() {
		assertPreviousViews("getPreviousView()");
		return (View) getPreviousViews().peek();					
	}

		
	private void assertPreviousViews(String method) { 
		if (previousViews == null) {
			throw new XavaException("use_object_previousViews_required", method, getClass().getName());
		}		
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
			
	protected String getModelName() {
		return view.getModelName();
	}
	
	/**
	 * Reset the cache of all descriptions-list and 
	 * others uses of descriptionsEditors.	 
	 */
	protected void resetDescriptionsCache() {
		super.resetDescriptionsCache();
		getView().refreshDescriptionsLists();
	}
			
	public Stack getPreviousViews() {
		return previousViews;
	}


	public void setPreviousViews(Stack previousViews) {
		this.previousViews = previousViews;
	}
		
	
}
