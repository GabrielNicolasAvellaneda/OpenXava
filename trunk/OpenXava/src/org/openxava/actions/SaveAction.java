package org.openxava.actions;

import javax.ejb.*;

import org.openxava.model.*;
import org.openxava.validators.*;

/**
 * @author Javier Paniza
 */

public class SaveAction extends ViewBaseAction {
		
	private boolean resetAfter = true;

	public void execute() throws Exception {
		try {					
			if (getView().isKeyEditable()) {
				// Create
				MapFacade.create(getModelName(), getView().getValues());				
			}
			else {
				// Modify				
				MapFacade.setValues(getModelName(), getView().getKeyValues(), getView().getValues());				
			}
			if (isResetAfter()) {
				getView().reset();
				getView().setKeyEditable(true);
			}
			resetDescriptionsCache();
		}
		catch (ValidationException ex) {			
			addErrors(ex.getErrors());
		}
		catch (ObjectNotFoundException ex) {
			addError("no_modify_no_exists");
		}
		catch (DuplicateKeyException ex) {
			addError("no_create_exists");
		}
	}
	
	public boolean isResetAfter() {
		return resetAfter;
	}

	public void setResetAfter(boolean b) {
		resetAfter = b;
	}

}
