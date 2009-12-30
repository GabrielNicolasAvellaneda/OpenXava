package org.openxava.actions;

import java.util.*;


/**
 * @author Javier Paniza
 */

public class CreateNewElementInCollectionAction extends CollectionElementViewBaseAction {
	
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
			addActions(getCollectionElementView().getSaveCollectionElementAction());
		} 		
		Iterator itDetailActions = getCollectionElementView().getActionsNamesDetail().iterator();		
		while (itDetailActions.hasNext()) {			
			addActions(itDetailActions.next().toString());			
		}
		addActions(getCollectionElementView().getHideCollectionElementAction());
	}
	
}
