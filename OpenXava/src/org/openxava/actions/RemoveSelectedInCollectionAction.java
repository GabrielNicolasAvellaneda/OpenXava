package org.openxava.actions;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.model.MapFacade;
import org.openxava.validators.ValidationException;

/**
 * Create on 05/09/2006 (9:12:51)
 * @autor Ana Andrés
 */

public class RemoveSelectedInCollectionAction extends CollectionBaseAction {

	private Log log = LogFactory.getLog(RemoveSelectedInCollectionAction.class);
	
	public void execute() throws Exception {
		try{
			Collection seleccionados = getMapsSelectedValues();	
			
			if (!seleccionados.isEmpty()){
				Iterator it = seleccionados.iterator();
				while(it.hasNext()){
					Map valores = (Map) it.next();
					MapFacade.remove(getCollectionElementView().getModelName(), valores);
				}
				
				if (isEntityReferencesCollection()) {
					addMessage("association_removed", getCollectionElementView().getModelName(), 
							getCollectionElementView().getParent().getModelName());
				}
				else {
					addMessage("aggregate_removed", getCollectionElementView().getModelName());
				}
				getView().recalculateProperties(); 
			}
		}
		catch (ValidationException ex) {			
			addErrors(ex.getErrors());
		}
	}

}