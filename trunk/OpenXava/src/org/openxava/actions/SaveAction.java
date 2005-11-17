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
			Map values = null;
			if (getView().isKeyEditable()) {
				// Create
				if (isResetAfter()) {
					MapFacade.create(getModelName(), getValuesToSave());					
				}
				else {					
					values = MapFacade.createReturningValues(getModelName(), getValuesToSave());	
				}								
			}
			else {
				// Modify
				MapFacade.setValues(getModelName(), getView().getKeyValues(), getValuesToSave());
				if (!isResetAfter()) {
					values = MapFacade.getValues(getModelName(), getView().getKeyValues(), getValuesToSave());
				}
			}
			
			if (isResetAfter()) {
				getView().reset();
				getView().setKeyEditable(true);
			}
			else {							
				getView().setValues(values);
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
	
	/**
	 * If <tt>true</tt> reset the form after save, else refresh the
	 * form from database displayed the recently saved data. <p>
	 * 
	 * The default value is <tt>true</tt>.
	 */
	public boolean isResetAfter() {
		return resetAfter;
	}

	/**
	 * If <tt>true</tt> reset the form after save, else refresh the
	 * form from database displayed the recently saved data. <p>
	 * 
	 * The default value is <tt>true</tt>.
	 */
	public void setResetAfter(boolean b) {
		resetAfter = b;
	}

}
