package org.openxava.actions;

import java.util.Iterator;

import javax.inject.Inject;

import org.openxava.session.MyChart;
import org.openxava.session.MyChartColumn;


/**
 * 
 * @author Federico Alcantara
 * @see org.openxava.actions.EditMyReportColumnAction
 */

public class EditMyChartColumnAction extends CollectionElementViewBaseAction  {
	
	@Inject
	private MyChart myChart; 
	
	private int row;
		
	@SuppressWarnings("rawtypes")
	public void execute() throws Exception {
		getCollectionElementView().clear(); 
		getCollectionElementView().removeObject("xava.myReportColumnShowAllColumns");  
		getCollectionElementView().setCollectionDetailVisible(true);
		getCollectionElementView().setCollectionEditingRow(getRow());
		MyChartColumn column = myChart.getColumns().get(row); 
		getCollectionElementView().setModel(column); 
		getCollectionElementView().setValueNotifying("name", column.getName()); // To throw the change event  
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
