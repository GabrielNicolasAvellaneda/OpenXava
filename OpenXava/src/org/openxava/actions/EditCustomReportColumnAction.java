package org.openxava.actions;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.*;

import org.openxava.model.MapFacade;
import org.openxava.model.inner.*;
import org.openxava.util.XavaException;


/**
 * tmp ¿Hacerlo genérico para cualquier entidad transitoria?
 * 
 * @author Javier Paniza 
 */

public class EditCustomReportColumnAction extends CollectionElementViewBaseAction  {
	
	@Inject
	private CustomReport customReport; 
	
	private int row;
		
	@SuppressWarnings("unchecked")
	public void execute() throws Exception {
		getCollectionElementView().clear(); 
		getCollectionElementView().setCollectionDetailVisible(true);
		Collection elements;
		Map keys = null;
		Map	values = null;
		/* tmp original
		if (getCollectionElementView().isCollectionCalculated()) {				
			elements = getCollectionElementView().getCollectionValues();
			if (elements == null) return;
			if (elements instanceof List) {
				keys = (Map) ((List) elements).get(getRow());			
			}	
		} else {
			keys = (Map) getCollectionElementView().getCollectionTab().getTableModel().getObjectAt(row);
		}
		if (keys != null) {
			values = MapFacade.getValues(getCollectionElementView().getModelName(), keys, getCollectionElementView().getMembersNames());
			getCollectionElementView().setValues(values);
		 */									
			getCollectionElementView().setCollectionEditingRow(getRow());
			// tmp ini
			CustomReportColumn column = customReport.getColumns().get(row); 
			getCollectionElementView().setPOJO(column); 
			getCollectionElementView().setValueNotifying("columnName", column.getColumnName()); // To throw the change event  
			// tmp fin			
		 /*	
		} else {
			throw new XavaException("only_list_collection_for_aggregates");
		}
		 */
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
