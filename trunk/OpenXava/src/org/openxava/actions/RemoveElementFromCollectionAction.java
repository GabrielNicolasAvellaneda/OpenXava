package org.openxava.actions;



import org.openxava.model.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * @author Javier Paniza
 * @author Jeromy Altuna
 */

public class RemoveElementFromCollectionAction extends CollectionElementViewBaseAction {
	
	
	
	public void execute() throws Exception {
		try {											
			if (!getCollectionElementView().getKeyValuesWithValue().isEmpty()) {
				validateMinimum();
				MapFacade.removeCollectionElement(getCollectionElementView().getParent().getModelName(), getCollectionElementView().getParent().getKeyValues(), getCollectionElementView().getMemberName(), getCollectionElementView().getKeyValues());
				if (isEntityReferencesCollection()) {
					addMessage("association_removed", getCollectionElementView().getModelName(), getCollectionElementView().getParent().getModelName());
				}
				else {
					addMessage("aggregate_removed", getCollectionElementView().getModelName());
				}
			}			
			getCollectionElementView().setCollectionEditingRow(-1);
			getCollectionElementView().clear();
			getView().recalculateProperties();
			closeDialog(); 
		}
		catch (ValidationException ex) {			
			addErrors(ex.getErrors());
		}				
	}
	
	private void validateMinimum() throws ValidationException, XavaException{ 
		MetaCollection metaCollection = getMetaCollection();
		int minimum = metaCollection.getMinimum();
		if(minimum > 0 && (getCollectionElementView().getCollectionSize()) <= minimum){
			Messages errors = new Messages();
			errors.add("minimum_elements", new Integer(minimum), minimum == 1?"element":"elements",	metaCollection.getName());				
			throw new ValidationException(errors);
		}	
	}		
}
