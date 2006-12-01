package org.openxava.actions;

import java.util.*;

import javax.ejb.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.model.*;
import org.openxava.validators.*;

/**
 * @author Javier Paniza
 */

public class SaveNewAction extends UpdateReferenceBaseAction {
	
	private Log log = LogFactory.getLog(SaveNewAction.class);
	
	public void execute() throws Exception {		
		try {					
			// Create
			Map key = MapFacade.createReturningKey(getView().getModelName(), getValuesToSave());
			returnsToPreviousViewUpdatingReferenceView(key);
		}
		catch (ValidationException ex) {			
			addErrors(ex.getErrors());
		}
		catch (DuplicateKeyException ex) {
			addError("no_create_exists");
		}
	}
		
}
