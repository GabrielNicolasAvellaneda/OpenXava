package org.openxava.actions;

import java.util.*;

import javax.ejb.*;

import org.openxava.model.*;
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
	
	private void saveEntity(Map containerKey) throws Exception {
		if (getCollectionElementView().isEditable()) {
			Map parentKey = new HashMap();
			parentKey.put(Strings.firstLower(getCollectionElementView().getParent().getModelName()), containerKey);
			Map values = getCollectionElementView().getValues();
			values.putAll(parentKey);
			try {
				MapFacade.setValues(getCollectionElementView().getModelName(), getCollectionElementView().getKeyValues(), values);
				addMessage("entity_modified", getCollectionElementView().getModelName());
			}
			catch (ObjectNotFoundException ex) {
				MapFacade.create(getCollectionElementView().getModelName(), values);
				addMessage("entity_created_and_associated", getCollectionElementView().getModelName(), getCollectionElementView().getParent().getModelName()); 
			}						
		}
		else {
			Map parentKey = new HashMap();
			parentKey.put(Strings.firstLower(getCollectionElementView().getParent().getModelName()), containerKey);		
			MapFacade.setValues(getCollectionElementView().getModelName(), getCollectionElementView().getKeyValues(), parentKey);
			addMessage("entity_associated" , getCollectionElementView().getModelName(), getCollectionElementView().getParent().getModelName()); 
		}
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
				addMessage("entity_created");
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
				addMessage("aggregate_created");
				view.addValues(key);									
				return key;										
			}
			else {				
				return view.getKeyValues();
			}
		}
	}


}
