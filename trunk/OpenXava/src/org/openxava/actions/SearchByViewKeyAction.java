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
			Map keys = getView().getKeyValuesWithValue();
			Map values = null;
			if (Maps.isEmptyOrZero(keys)) {
				values = MapFacade.getValuesByAnyProperty(getModelName(), getView().getValues(), memberNames);
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
				
}
