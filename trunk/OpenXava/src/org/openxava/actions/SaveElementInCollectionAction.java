package org.openxava.actions;


import java.util.*;

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
			Map claveContenedor = saveIfNotExists(getCollectionElementView().getParent());
			if (isEntityReferencesCollection()) saveEntity(claveContenedor);
			else saveAggregate(claveContenedor); 			
			getCollectionElementView().setCollectionEditingRow(-1);
			getCollectionElementView().clear();
			resetDescriptionsCache();
		}
		catch (ValidationException ex) {			
			addErrors(ex.getErrors());
		}		
	}

	private void saveEntity(Map containerKey) throws Exception {				
		Map clavePapa = new HashMap();
		clavePapa.put(Strings.firstLower(getCollectionElementView().getParent().getModelName()), containerKey);
		MapFacade.setValues(getCollectionElementView().getModelName(), getCollectionElementView().getKeyValues(), clavePapa);		
	}

	private void saveAggregate(Map containerKey) throws Exception{
		if (getCollectionElementView().getKeyValuesWithValue().isEmpty()) {				
			int fila = getCollectionElementView().getCollectionValues().size();			
			MapFacade.createAggregate(
				getCollectionElementView().getModelName(),						
				containerKey, fila+1, // +1 for start in 1, because 0 is equals to no value					
				getCollectionElementView().getValues() );												 								
		}
		else {										
			MapFacade.setValues(getCollectionElementView().getModelName(), getCollectionElementView().getKeyValues(), getCollectionElementView().getValues());								
		}								
	}

	/**
	 * @return The saved object 
	 */
	private Map saveIfNotExists(View view) throws Exception {					
		if (getView() == view) {
			if (view.isKeyEditable()) {				
				Map clave = MapFacade.createReturningKey(getModelName(), view.getValues());
				view.addValues(clave);
				view.setKeyEditable(false);								
				return clave;								
			}			
			else {				
				return view.getKeyValues();									
			}
		}			
		else {
			if (view.getKeyValuesWithValue().isEmpty()) {
				Map clavePadre = saveIfNotExists(view.getParent());
				Map clave = MapFacade.createAggregateReturningKey( 
					view.getModelName(),
					clavePadre, 0,					
					view.getValues() );																				 								
				view.addValues(clave);									
				return clave;										
			}
			else {				
				return view.getKeyValues();
			}
		}
	}


}
