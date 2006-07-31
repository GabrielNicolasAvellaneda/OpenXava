package org.openxava.actions;

import java.util.*;

import javax.ejb.*;

import org.openxava.model.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */

public class SearchByViewKeyAction extends ViewBaseAction {
	
	public void execute() throws Exception {
		try {			
			Map memberNames = getView().getMembersNamesWithHidden();
			Map keys = getView().getKeyValues();
			Map values = null;
			if (Maps.isEmptyOrZero(keys)) { 
				try {
					values = MapFacade.getValuesByAnyProperty(getModelName(), getCriteriaSearchValues(), memberNames);
				}
				catch (ObjectNotFoundException ex) {
					// This is for the case of key with 0 as valid value
					values = MapFacade.getValues(getModelName(), keys, memberNames);
				}
			}
			else {				
				values = MapFacade.getValues(getModelName(), keys, memberNames);
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
	 * The values used in order to prepare the searh condition. <p>
	 * 
	 * Blank and zeroes are ignored.<br>
	 * By default assumed all data currently displayed to the user.<br>
	 */
	protected Map getCriteriaSearchValues() throws Exception {
		return getView().getValues();
	}

				
}
