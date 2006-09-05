package org.openxava.actions;

import java.util.*;

import javax.ejb.*;

import org.openxava.model.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;
import org.openxava.view.*;

/**
 * @author Javier Paniza
 */

public class SaveElementInCollectionAction extends CollectionElementViewBaseAction {
	
	public void execute() throws Exception {		
		try {				
			Map containerKey = saveIfNotExists(getCollectionElementView().getParent());
			if (isEntityReferencesCollection()) saveEntity(containerKey);
			else saveAggregate(containerKey); 			
			getCollectionElementView().setCollectionEditingRow(-1);
			getCollectionElementView().clear();
			resetDescriptionsCache();
			getView().recalculateProperties(); 			
		}
		catch (ValidationException ex) {			
			addErrors(ex.getErrors());
		}		
	}
	
	private void validateMaximum() throws ValidationException, XavaException {
		MetaCollection metaCollection = getMetaCollection();
		int maximum = metaCollection.getMaximum(); 
		if (maximum > 0) {
			if (getCollectionElementView().getCollectionValues().size() >= maximum) {
				Messages errors = new Messages();
				errors.add("maximum_elements", new Integer(maximum), metaCollection.getName(), metaCollection.getMetaModel().getName());
				throw new ValidationException(errors);
			}
		}		
	}

	private void saveEntity(Map containerKey) throws Exception {
		if (getCollectionElementView().isEditable()) {
			// Entity reference used as aggregate
			Map parentKey = new HashMap();
			MetaCollection metaCollection = getMetaCollection();
			parentKey.put(metaCollection.getMetaReference().getRole(), containerKey);
			Map values = getCollectionElementView().getValues();
			values.putAll(parentKey);
			try {
				MapFacade.setValues(getCollectionElementView().getModelName(), getCollectionElementView().getKeyValues(), values);
				addMessage("entity_modified", getCollectionElementView().getModelName());
			}
			catch (ObjectNotFoundException ex) {
				validateMaximum();
				MapFacade.create(getCollectionElementView().getModelName(), values);
				addMessage("entity_created_and_associated", getCollectionElementView().getModelName(), getCollectionElementView().getParent().getModelName()); 
			}						
		}
		else {
			// Entity reference used in the standard way
			validateMaximum();
			MapFacade.addCollectionElement(
					getCollectionElementView().getParent().getMetaModel().getName(),
					getCollectionElementView().getParent().getKeyValues(),
					getCollectionElementView().getMemberName(),  
					getCollectionElementView().getKeyValues());
			addMessage("entity_associated" , getCollectionElementView().getModelName(), getCollectionElementView().getParent().getModelName()); 
		}
	}

	private MetaCollection getMetaCollection() throws ElementNotFoundException, XavaException {
		return getCollectionElementView().getParent().getMetaModel().getMetaCollection(getCollectionElementView().getMemberName());
	}

	private void saveAggregate(Map containerKey) throws Exception{
		if (getCollectionElementView().getKeyValuesWithValue().isEmpty()) {
			createAggregate(containerKey);			
		}
		else {				
			try {				
				MapFacade.setValues(getCollectionElementView().getModelName(), getCollectionElementView().getKeyValues(), getCollectionElementView().getValues());
				addMessage("aggregate_modified", getCollectionElementView().getModelName());
			}
			catch (ObjectNotFoundException ex) {
				// In case not hidden primary key in aggregate
				createAggregate(containerKey);								
			}
		}								
	}
	
	private void createAggregate(Map containerKey) throws Exception {
		validateMaximum();
		int row = getCollectionElementView().getCollectionValues().size();
		MapFacade.createAggregate(
			getCollectionElementView().getModelName(),						
			containerKey, row+1, // +1 for start in 1, because 0 is equals to no value					
			getCollectionElementView().getValues() );
		addMessage("aggregate_created", getCollectionElementView().getModelName());		
	}

	/**
	 * @return The saved object 
	 */
	private Map saveIfNotExists(View view) throws Exception {					
		if (getView() == view) {
			if (view.isKeyEditable()) {				
				Map key = MapFacade.createReturningKey(getModelName(), view.getValues());
				addMessage("entity_created", getModelName());
				view.addValues(key);
				view.setKeyEditable(false);								
				return key;								
			}			
			else {				
				return view.getKeyValues();									
			}
		}			
		else {
			if (view.getKeyValuesWithValue().isEmpty()) {
				Map parentKey = saveIfNotExists(view.getParent());
				Map key = MapFacade.createAggregateReturningKey( 
					view.getModelName(),
					parentKey, 0,					
					view.getValues() );
				addMessage("aggregate_created", view.getModelName());
				view.addValues(key);									
				return key;										
			}
			else {				
				return view.getKeyValues();
			}
		}
	}


}
