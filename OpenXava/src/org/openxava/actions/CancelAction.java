package org.openxava.actions;

import java.util.*;

import org.openxava.view.*;

/**
 * @author Javier Paniza
 */

public class CancelAction extends ViewBaseAction implements INavigationAction {
	
	private boolean keyEditable = false;
	private boolean editable = true;
	private Stack previousViews;
	private boolean restoreEditable = false;
		 	
	public void execute() throws Exception {
		 
		if (getPreviousViews() != null && !getPreviousViews().empty()) {
			setView((View) getPreviousViews().pop());
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
		return "xava/detail";
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
}
