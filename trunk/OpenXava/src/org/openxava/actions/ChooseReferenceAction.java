package org.openxava.actions;

import java.util.*;

import org.openxava.tab.*;
import org.openxava.view.*;

/**
 * @author Javier Paniza
 */

public class ChooseReferenceAction extends ViewBaseAction implements INavigationAction {
	 	
	private Tab tab;
	private View referenceSubview;
	private int row = -1;
	
	public void execute() throws Exception {						
		int [] selectedOnes = tab.getSelected();
		Map clave = null;
		if (row >= 0) {
			clave = (Map) getTab().getTableModel().getObjectAt(row);
		}				
		else if (selectedOnes != null && selectedOnes.length > 0) {			
			clave = (Map) getTab().getTableModel().getObjectAt(selectedOnes[0]);
		}		
		getReferenceSubview().setValuesNotifying(clave);
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

	public void setReferenceSubview(View vista) {
		referenceSubview = vista;
	}

	public String[] getNextControllers() {		
		return PREVIOUS_CONTROLLERS;
	}

	public String getCustomView() {		
		return DEFAULT_VIEW;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int i) {
		row = i;
	}

}
