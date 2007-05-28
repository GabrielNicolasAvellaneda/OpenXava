package org.openxava.actions;

import java.util.*;
import org.openxava.util.*;
import org.openxava.view.*;

/**
 * @author Javier Paniza
 */

public class ViewDetailAction extends TabBaseAction implements IChainAction, IModelAction {
	
	private int row;
	private int increment;
	private Map key;	
	private boolean goFirst = false;
	private String nextAction;
	private boolean atListBegin;
	private boolean noElementsInList;
	private String model;
	private View view;
	
	public void execute() throws Exception {		
		getView().setModelName(model); 
		setAtListBegin(false);
		setNoElementsInList(false);				
		if (increment < 0 && row == 0) {
			setAtListBegin(true);
			addError("at_list_begin");			
			return;
		}		
		int previous = row;
		row = goFirst?0:row + increment;		
		int [] selectedOnes = getTab().getSelected();
		boolean lastSelectedPassed = false;		
		if (selectedOnes != null && selectedOnes.length > 0) {
			if (increment >= 0) {				
				int last = selectedOnes[selectedOnes.length - 1];				
				if (row > last) lastSelectedPassed = true;
				else {					
					while (Arrays.binarySearch(selectedOnes, row) < 0 && row < last) { 
						row++;
					} 		
				}	
			}
			else {
				int first = selectedOnes[0];
				if (row < first) lastSelectedPassed = true;
				else {
					while (Arrays.binarySearch(selectedOnes, row) < 0 && row > first) { 
						row--;
					} 		
				}					
			}
		}		
		if (lastSelectedPassed) {
			key = null;
		}
		else {
			key = (Map) getTab().getTableModel().getObjectAt(row);			
		}
		if (key == null) {
			setNoElementsInList(true);
			addError("no_list_elements");
			row = previous;
		}		
		if (key != null) {		
			getView().setValues(key);						
		}		
	}

	public int getRow() {
		return row;
	}

	public void setRow(int i) {
		row = i;		
	}
	
	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
			
	protected String getModelName() {
		return view.getModelName();
	}	

	public String getNextAction() throws XavaException {
		if (Is.emptyString(nextAction)) {
			return getEnvironment().getValue("XAVA_SEARCH_ACTION");
		} 		
		return nextAction;
	}
	
	public void setNextAction(String string) {
		nextAction = string;
	}
	
	public int getIncrement() {
		return increment;
	}

	public void setIncrement(int i) {
		increment = i;
	}

	public boolean isGoFirst() {
		return goFirst;
	}

	public void setGoFirst(boolean b) {
		goFirst = b;
	}


	public boolean isNoElementsInList() {
		return noElementsInList;
	}

	public boolean isAtListBegin() {
		return atListBegin;
	}

	private void setNoElementsInList(boolean b) {
		noElementsInList = b;
	}

	private void setAtListBegin(boolean b) {
		atListBegin = b;
	}

	public void setModel(String modelName) { 
		this.model = modelName;		
	}

}
