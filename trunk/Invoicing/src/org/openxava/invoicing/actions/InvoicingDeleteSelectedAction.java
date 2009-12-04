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
	private boolean restore; // tmp
	private int row; // tmp

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
		Map values = new HashMap();
		// tmp values.put("deleted", true); 
		values.put("deleted", !isRestore());		
		for (Map key: getTab().getSelectedKeys()) {
			try {									
				MapFacade.setValues(
					getTab().getModelName(), 
					key, 
					values);					
			}
			catch (ValidationException ex) {
				addError("no_delete_row", "", key);
				addErrors(ex.getErrors());
			}								
			catch (Exception ex) {
				addError("no_delete_row", "", key);
			}				
		}
		getTab().deselectAll();
		resetDescriptionsCache();				
	}

	public boolean isRestore() {
		return restore;
	}

	public void setRestore(boolean restore) {
		this.restore = restore;
	}
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}
	

}
