package org.openxava.actions;

import java.util.*;

import javax.ejb.*;

import org.openxava.model.*;
import org.openxava.validators.*;

/**
 * @author Javier Paniza
 */

public class SaveNewAction extends UpdateReferenceBaseAction {

	public void execute() throws Exception {
		try {
			Map key = null;
			if (getView().isKeyEditable()) {
				// Create
				key = MapFacade.createReturningKey(getView().getModelName(),
						getValuesToSave());
			} else {
				// Modify, usually when collection exists, because 
				// saving an element collection saves automatically the main entity
				key = getView().getKeyValues();
				MapFacade.setValues(getView().getModelName(), key,
						getValuesToSave());
			}
			returnsToPreviousViewUpdatingReferenceView(key);
		} 
		catch (ValidationException ex) {
			addErrors(ex.getErrors());
		} 
		catch (DuplicateKeyException ex) {
			addError("no_create_exists");
		}

		/* tmp original
		try {
			// Create
			Map key = MapFacade.createReturningKey(getView().getModelName(),
					getValuesToSave());
			returnsToPreviousViewUpdatingReferenceView(key);
		} catch (ValidationException ex) {
			addErrors(ex.getErrors());
		} catch (DuplicateKeyException ex) {
			addError("no_create_exists");
		}
		*/
		// tmp ini
		/*
		Map values = null;
		Map key = null;

		try {
			// Create
			if (getView().isKeyEditable()) {
				// Create
				if (isResetAfter()) {
					MapFacade.create(getView().getModelName(),
							getValuesToSave());
				} else {
					key = MapFacade.createReturningKey(
							getView().getModelName(), getValuesToSave());
				}
			} else {
				// Modify
				key = getView().getKeyValues();
				MapFacade.setValues(getView().getModelName(), key,
						getValuesToSave());
			}

			returnsToPreviousViewUpdatingReferenceView(key);
		} catch (ValidationException ex) {
			addErrors(ex.getErrors());
		} catch (DuplicateKeyException ex) {
			addError("no_create_exists");
		}
		*/
		// tmp fin
	}

}
