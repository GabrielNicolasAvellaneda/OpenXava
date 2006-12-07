package org.openxava.actions;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.tab.*;
import org.openxava.view.*;

/**
 * @author Javier Paniza
 */

public class ChooseReferenceAction extends ViewBaseAction implements INavigationAction {
	 	
	private Tab tab;
	private View referenceSubview;
	private int row = -1;
	private boolean	chosen = true;
	private static Log log = LogFactory.getLog(ChooseReferenceAction.class);
	
	public void execute() throws Exception {						
		int [] selectedOnes = tab.getSelected();
		Map key = null;
		if (row >= 0) {
			key = (Map) getTab().getTableModel().getObjectAt(row);
		}				
		else if (selectedOnes != null && selectedOnes.length > 0) {			
			key = (Map) getTab().getTableModel().getObjectAt(selectedOnes[0]);
		}		
		if (key == null) {
			chosen = false;
			return;
		}
		getReferenceSubview().setValuesNotifying(key); 
		getTab().setModelName(getView().getModelName());
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab web) {
		tab = web;
	}

	public View getReferenceSubview() {
		return referenceSubview;
	}

	public void setReferenceSubview(View view) {
		referenceSubview = view;
	}

	public String[] getNextControllers() {		
		return chosen?PREVIOUS_CONTROLLERS:SAME_CONTROLLERS;
	}

	public String getCustomView() {		
		return chosen?DEFAULT_VIEW:SAME_VIEW;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int i) {
		row = i;
	}

}
