package org.openxava.actions;

import org.openxava.view.*;

/**
 * @author Javier Paniza
 */

abstract public class OnChangePropertyBaseAction
	extends BaseAction
	implements IOnChangePropertyAction {
		
	private String changedProperty;
	private Object newValue;
	private View view;	

	public Object getNewValue() {
		return newValue;
	}

	public String getChangedProperty() {
		return changedProperty;
	}

	public View getView() {
		return view;
	}

	public void setNewValue(Object object) {
		newValue = object;
	}

	public void setChangedProperty(String string) {
		changedProperty = string;
	}

	public void setView(View view) {
		this.view = view;
	}

}
