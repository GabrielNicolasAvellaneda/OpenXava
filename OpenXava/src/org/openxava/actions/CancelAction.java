package org.openxava.actions;

import java.util.*;

import javax.servlet.http.*;



import org.openxava.view.*;

/**
 * @author Javier Paniza
 */

public class CancelAction extends ViewBaseAction implements INavigationAction, IRequestAction {
	
	private boolean keyEditable = false;
	private boolean editable = true;
	private Stack previousViews;
	private boolean restoreEditable = false;
	private HttpServletRequest request;
	
	
	public void execute() throws Exception {
		 
		if (getPreviousViews() != null && !getPreviousViews().empty()) {
			View view = (View) getPreviousViews().pop();
			view.setRequest(request);
			setView(view);
		}
		if (restoreEditable && getView() != null) {
			getView().setKeyEditable(keyEditable);
			getView().setEditable(editable);			
		}
	}
	
	public String [] getNextControllers() {
		return PREVIOUS_CONTROLLERS;		
	}
	
	public String getCustomView() {
		return PREVIOUS_VIEW; 
	}

	public Stack getPreviousViews() {
		return previousViews;
	}
	public void setPreviousViews(Stack previousViews) {
		this.previousViews = previousViews;
	}
	public boolean isRestoreEditable() {
		return restoreEditable;
	}
	public void setRestoreEditable(boolean restoreEditable) {
		this.restoreEditable = restoreEditable;
	}
	
	public void setRequest(HttpServletRequest request) {	
		super.setRequest(request);
		this.request = request;
	}
	
}
