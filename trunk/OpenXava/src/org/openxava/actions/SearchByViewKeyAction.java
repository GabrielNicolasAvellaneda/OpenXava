package org.openxava.actions;

import java.util.*;
import javax.ejb.*;

import org.openxava.model.*;

/**
 * @author Javier Paniza
 */

public class SearchByViewKeyAction extends ViewBaseAction {
	
	public void execute() throws Exception {
		try {						
			Map memberNames = getView().getMemberNamesWithHidden();
			Map values = MapFacade.getValues(getModelName(), getView().getKeyValues(), memberNames);									
			getView().setEditable(true);	
			getView().setKeyEditable(false);			
			getView().setValues(values);
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
				
}
