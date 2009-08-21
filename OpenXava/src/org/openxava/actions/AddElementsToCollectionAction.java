package org.openxava.actions;

import java.util.*;

import javax.ejb.*;

import org.apache.commons.logging.*;
import org.hibernate.validator.*;
import org.openxava.tab.*;
import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * This is for the case of collections of entities without @AsEmbedded (or without as-aggregate="true"). <p>
 * 
 * The remainings cases are treated by {@link SaveElementInCollectionAction}.<br> 
 * This case add a group of entities from a list.<br>
 *  
 * @author Javier Paniza
 */

public class AddElementsToCollectionAction extends SaveElementInCollectionAction implements INavigationAction {
	
	private static Log log = LogFactory.getLog(AddElementsToCollectionAction.class);
	 	
	private Tab tab;
	private int added;
	private int failed;
	private int row = -1;
	private String currentCollectionLabel;
	
	public void execute() throws Exception {
		saveIfNotExists(getCollectionElementView().getParent());		
		if (row >= 0) {
			associateEntityInRow(row);
		}
		else {
			int [] selectedOnes = getTab().getSelected();					
			if (selectedOnes != null) {						
				for (int i = 0; i < selectedOnes.length; i++) {
					associateEntityInRow(selectedOnes[i]);
				}
			}		
		}
		addMessage("elements_added_to_collection", new Integer(added), currentCollectionLabel);
		if (failed > 0) addError("elements_not_added_to_collection", new Integer(failed), currentCollectionLabel);
		getTab().deselectAll();
		resetDescriptionsCache(); 
		
	}

	private void associateEntityInRow(int row) throws FinderException, Exception {
		Map key = (Map) getTab().getTableModel().getObjectAt(row);
		try {									
			associateEntity(key); 					
			added++;
		}
		catch (Exception ex) {
			addValidationMessage(ex); 
			failed++;
			log.error(
				XavaResources.getString("add_collection_element_error", 
						getCollectionElementView().getModelName(), 
						getCollectionElementView().getParent().getModelName()), 
					ex);			
		}
	}
	
	private void addValidationMessage(Exception ex) { 
		if (ex instanceof ValidationException) {		
			addErrors(((ValidationException)ex).getErrors());
		}
		else if (ex instanceof InvalidStateException) {
			InvalidValue [] invalidValues = ((InvalidStateException) ex).getInvalidValues();
			for (int i=0; i<invalidValues.length; i++) {
				addError("invalid_state", 
						invalidValues[i].getPropertyName(), 
						Classes.getSimpleName(invalidValues[i].getBeanClass()), 
						invalidValues[i].getMessage(), 
						invalidValues[i].getValue());			
			}
		}		
	}

	public String getNextAction() throws Exception { 
		// In order to annul the chaining of the action
		return null;
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab web) {
		tab = web;
	}


	public String[] getNextControllers() {		
		return added > 0?PREVIOUS_CONTROLLERS:SAME_CONTROLLERS;
	}

	public String getCustomView() {		
		return added > 0?DEFAULT_VIEW:SAME_VIEW;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public String getCurrentCollectionLabel() {
		return currentCollectionLabel;
	}

	public void setCurrentCollectionLabel(String currentCollectionLabel) {
		this.currentCollectionLabel = currentCollectionLabel;
	}

}
