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
	private boolean restore;
	private int row = -1;

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
		values.put("deleted", !isRestore());		
		for (Map key: getSelectedKeys()) {
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
	
	private Map [] getSelectedKeys() throws Exception {
		System.out
				.println("[InvoicingDeleteSelectedAction.getSelectedKeys] row=" + row); // tmp
		if (row < 0) {
			System.out
					.println("[InvoicingDeleteSelectedAction.getSelectedKeys] getTab().getSelectedKeys()=" + getTab().getSelectedKeys()); // tmp
			return getTab().getSelectedKeys();		
		}
		return new Map [] { (Map) getTab().getTableModel().getObjectAt(row) };
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
