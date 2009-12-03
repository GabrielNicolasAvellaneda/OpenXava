package org.openxava.invoicing.actions;

import java.util.*;
import org.openxava.actions.*;
import org.openxava.model.*;
import org.openxava.model.meta.*;
import org.openxava.validators.*;

public class InvoicingDeleteSelectedAction 
	extends TabBaseAction 
	implements IChainAction {
	
	private String nextAction = null;

	public void execute() throws Exception {		
		if (!getMetaModel().containsMetaProperty("deleted")) {
			nextAction = "CRUD.deleteSelected"; 			
			return;
		}		
		markSelectedEntitiesAsDeleted();
	}

	private MetaModel getMetaModel() {
		return MetaModel.get(getTab().getModelName());
	}

	public String getNextAction() throws Exception {
		return nextAction;
	}
	
	private void markSelectedEntitiesAsDeleted() throws Exception {
		int [] selectedOnes = getTab().getSelected();
		if (selectedOnes != null) {
			Map values = new HashMap();
			values.put("deleted", true);
			for (int i = 0; i < selectedOnes.length; i++) {				
				Map key = (Map) getTab().getTableModel().getObjectAt(selectedOnes[i]);
				try {									
					MapFacade.setValues(
						getTab().getModelName(), 
						key, 
						values);					
				}
				catch (ValidationException ex) {
					addError("no_delete_row", new Integer(i), key);
					addErrors(ex.getErrors());
				}								
				catch (Exception ex) {
					addError("no_delete_row", new Integer(i), key);
				}				
			}
			getTab().deselectAll();
			resetDescriptionsCache();
		}		
	}

}
