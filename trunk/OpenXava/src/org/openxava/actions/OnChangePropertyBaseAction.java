package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

	private Log log = LogFactory.getLog(OnChangePropertyBaseAction.class);
	
	public Object getNewValue() {
		return newValue;
	}

	public String getChangedProperty() {
		return changedProperty;
	}

	/**
	 * The view where the on-change is declared. <p>
	 * 
	 * This may be the main view or the module (if property-view : on-change
	 * is declared in main view) or an subview (if it's declared inside a
	 * aggregate view, for example). 
	 * 
	 * @return
	 */
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
