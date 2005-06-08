package org.openxava.actions;

import java.util.Map;

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
				MapFacade.create(getModelName(), getValuesToSave());				
			}
			else {
				// Modify				
				MapFacade.setValues(getModelName(), getView().getKeyValues(), getValuesToSave());				
			}
			if (isResetAfter()) {
				getView().reset();
				getView().setKeyEditable(true);
			}
			else {
				getView().setKeyEditable(false);
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
	
	protected Map getValuesToSave() throws Exception {
		return getView().getValues();
	}
	
	public boolean isResetAfter() {
		return resetAfter;
	}

	public void setResetAfter(boolean b) {
		resetAfter = b;
	}

}
