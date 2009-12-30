package org.openxava.actions;

import java.util.*;



import org.openxava.model.*;
import org.openxava.util.*;


/**
 * @author Javier Paniza
 */

public class EditElementInCollectionAction extends CollectionElementViewBaseAction  {
	
	private int row;
	
	
	public void execute() throws Exception {
		getCollectionElementView().setCollectionDetailVisible(true);
		Collection elements = getCollectionElementView().getCollectionValues(); 
		if (elements == null) return;
		if (elements instanceof List) {
			Map keys = (Map) ((List) elements).get(getRow());			
			Map	values = MapFacade.getValues(getCollectionElementView().getModelName(), keys, getCollectionElementView().getMembersNames());
			getCollectionElementView().setValues(values);			
			getCollectionElementView().setCollectionEditingRow(getRow());
		}
		else {
			throw new XavaException("only_list_collection_for_aggregates");
		}
		showDialog(getCollectionElementView());
		if (getCollectionElementView().isCollectionEditable() || 
			getCollectionElementView().isCollectionMembersEditables()) 
		{ 
			addActions(getCollectionElementView().getSaveCollectionElementAction());
		}
		if (getCollectionElementView().isCollectionEditable()) { 
			addActions(getCollectionElementView().getRemoveCollectionElementAction());
		} 	
		Iterator itDetailActions = getCollectionElementView().getActionsNamesDetail().iterator();
		while (itDetailActions.hasNext()) {		
			addActions(itDetailActions.next().toString());			
		}
		addActions(getCollectionElementView().getHideCollectionElementAction());					
	}
	
	public int getRow() {
		return row;
	}

	public void setRow(int i) {
		row = i;
	}

}
