package org.openxava.actions;

import java.util.*;

import javax.inject.*;

import org.apache.commons.logging.*;
import org.openxava.util.*;
import org.openxava.view.*;

/**
 * 
 * @author Javier Paniza
 */

abstract public class ViewBaseAction extends BaseAction {
	
	private static Log log = LogFactory.getLog(ViewBaseAction.class);
		
	@Inject 
	private View view;
	@Inject
	private Stack previousViews;
	private boolean dialogShown = false; 
	
	/**
	 * Creates a new view and shows it. <p>
	 * 
	 * After it if you call to getView() it will return this new view.<br>
	 * 
	 * @since 4m1
	 */
	protected void showNewView() { 
		showView(new View());			
	}
	
	/**
	 * Shows the specified view. <p>
	 * 
	 * After it if you call to getView() it will be the specified view.<br>
	 * 
	 * @since 4m2
	 */	
	protected void showView(View newView) {  
		getView().putObject("xava.mode", getManager().getModeName());	
		newView.setRequest(getRequest());
		newView.setErrors(getErrors()); 
		newView.setMessages(getMessages());
		getPreviousViews().push(getView());
		setView(newView);		
		setNextMode(DETAIL);
	}

	/**
	 * Shows the specified view inside a dialog. <p>
	 * 
	 * After it if you call to getView() it will be the specified view.<br>
	 * 
	 * @since 4m2
	 */		
	protected void showDialog(View viewToShowInDialog) throws Exception { 
		showView(viewToShowInDialog);
		if (getNextControllers() == null) {			
			clearActions();
		}
		getManager().showDialog();
		dialogShown = true;
	}

	/**
	 * Creates a new view and shows it inside a dialog. <p>
	 * 
	 * After it if you call to getView() it will return this new view.<br>
	 * 
	 * @since 4m2
	 */	
	protected void showDialog() throws Exception { 
		showDialog(new View());
	}
	
	/**
	 * @since 4m2
	 */
	protected void closeDialog() { 
		returnToPreviousView();
		returnToPreviousControllers();
		getManager().closeDialog();
		dialogShown = false;
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
		return (View) getPreviousViews().peek();					
	}
		
	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
			
	protected String getModelName() {
		return getView().getModelName(); 
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
	
	
	protected void setControllers(String... controllers) {
		if (dialogShown) getManager().restorePreviousControllers(); 
		super.setControllers(controllers);
	}
	
}
