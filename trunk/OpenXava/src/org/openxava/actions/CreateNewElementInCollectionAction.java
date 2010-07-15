package org.openxava.actions;

import java.util.Iterator;

import org.openxava.controller.meta.MetaAction;


/**
 * @author Javier Paniza
 * 4m5 creates two buttons for the save action, ones that closes the dialog and the other that stays.
 */

public class CreateNewElementInCollectionAction extends CollectionElementViewBaseAction {
	
	@SuppressWarnings("unchecked")
	public void execute() throws Exception {
		if (getCollectionElementView().isRepresentsAggregate()) {
			getCollectionElementView().reset();				
		}
		getCollectionElementView().setCollectionDetailVisible(true);
		getCollectionElementView().setCollectionEditingRow(-1);
		showDialog(getCollectionElementView());		
		if (getCollectionElementView().isCollectionEditable() || 
			getCollectionElementView().isCollectionMembersEditables()) 
		{ 
			// The Collection.saveAndStay will function as trapper for the save action,
			// and will prevent the dialog to close while clearing the form and filling with default values.
			addActions(getCollectionElementView().getSaveCollectionElementAction(), "Collection.saveAndStay");
		} 		
		Iterator itDetailActions = getCollectionElementView().getActionsNamesDetail().iterator();		
		while (itDetailActions.hasNext()) {			
			addActions(itDetailActions.next().toString());			
		}
		addActions(getCollectionElementView().getHideCollectionElementAction());
	}
	
}
