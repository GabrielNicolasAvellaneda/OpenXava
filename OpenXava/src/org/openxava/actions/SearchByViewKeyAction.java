package org.openxava.actions;

import java.util.Map;

import javax.ejb.ObjectNotFoundException;

import org.openxava.model.MapFacade;
import org.openxava.util.Maps;

/**
 * @author Javier Paniza
 */

public class SearchByViewKeyAction extends ViewBaseAction {
	
	private static final long serialVersionUID = 1L;

	public void execute() throws Exception {
		try {						
			Map keys = getView().getKeyValues();
			Map values = null;
			if (Maps.isEmptyOrZero(keys)) { 
				try {
					values = MapFacade.getValuesByAnyProperty(getModelName(), getValuesFromView(), getMemberNames());
				}
				catch (ObjectNotFoundException ex) {
					// This is for the case of key with 0 as valid value
					values = MapFacade.getValues(getModelName(), keys, getMemberNames());
				}
			}
			else {				
				values = MapFacade.getValues(getModelName(), keys, getMemberNames());
			}
			getView().setEditable(true);	
			getView().setKeyEditable(false);			
			setValuesToView(values); 		
		}
		catch (ObjectNotFoundException ex) {
			getView().clear();
			addError("object_not_found");			
		}						
		catch (Exception ex) {
			ex.printStackTrace();
			addError("system_error");			
		}						
	}
	
	protected void setValuesToView(Map values) throws Exception {
		getView().setValues(values);
	}

	/** 
	 * Allows to refine the members that will be including for the search of the object 
	 */
	protected Map getMemberNames() throws Exception{
		return getView().getMembersNamesWithHidden();
	}
	
	/**
	 * Allows to refine the values received from the view, in order to prepare the searh condition<br>
	 * Blank and zeroes are ignored.<br>
	 * By default assumed all data currently displayed to the user.<br>
	 */
	protected Map getValuesFromView() throws Exception {
		return getView().getValues();
	}

	/**
	 * Allows to refine the key values received from the view<br> 
	 */
	protected Map getKeyValuesFromView() throws Exception {
		return getView().getKeyValues();
	}

				
}
