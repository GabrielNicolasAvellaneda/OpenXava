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
		Collection aggregates = getCollectionElementView().getCollectionValues(); 
		if (aggregates == null) return;
		if (aggregates instanceof List) {
			Map values = (Map) ((List) aggregates).get(getRow());			
			if (!values.keySet().equals(getCollectionElementView().getValues().keySet())) {
				values = MapFacade.getValues(getCollectionElementView().getModelName(), values, getCollectionElementView().getMembersNames());
			}								
			getCollectionElementView().setValues(values);			
			getCollectionElementView().setCollectionEditingRow(getRow());
		}
		else {
			throw new XavaException("only_list_collection_for_aggregates");
		}
	}
	
	public int getRow() {
		return row;
	}

	public void setRow(int i) {
		row = i;
	}

}
