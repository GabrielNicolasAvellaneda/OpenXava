package org.openxava.actions;

import org.openxava.model.*;
import org.openxava.validators.*;

/**
 * @author Javier Paniza
 */

public class RemoveElementFromCollectionAction extends CollectionElementViewBaseAction {
		
	public void execute() throws Exception {
		try {											
			if (!getCollectionElementView().getKeyValuesWithValue().isEmpty()) {				
				MapFacade.removeCollectionElement(getCollectionElementView().getParent().getModelName(), getCollectionElementView().getParent().getKeyValues(), getCollectionElementView().getMemberName(), getCollectionElementView().getKeyValues());
			}
			getCollectionElementView().setCollectionEditingRow(-1);
			getCollectionElementView().clear();
			getView().recalculateProperties();
		}
		catch (ValidationException ex) {			
			addErrors(ex.getErrors());
		}				
	}
		
}
