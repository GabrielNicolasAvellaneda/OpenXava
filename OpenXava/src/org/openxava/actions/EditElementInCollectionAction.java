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
		Collection agregados = getCollectionElementView().getCollectionValues(); 
		if (agregados == null) return;
		if (agregados instanceof List) {
			Map valores = (Map) ((List) agregados).get(getRow());			
			if (!valores.keySet().equals(getCollectionElementView().getValues().keySet())) {
				valores = MapFacade.getValues(getCollectionElementView().getModelName(), valores, getCollectionElementView().getMembersNames());
			}								
			getCollectionElementView().setValues(valores);			
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
