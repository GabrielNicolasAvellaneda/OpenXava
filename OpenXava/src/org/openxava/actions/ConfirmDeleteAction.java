package org.openxava.actions;

import java.util.*;

import org.openxava.model.*;
import org.openxava.validators.*;

/**
 * @author Javier Paniza
 */

public class ConfirmDeleteAction extends ViewDetailAction implements INavigationAction{
	
	private String [] nextControllers;
	private String nextView;
	
	public ConfirmDeleteAction() {
		setIncrement(0);
	}

	public void execute() throws Exception { 
		getView().setEditable(true);
		getView().setKeyEditable(false);		
		setNextControllers(PREVIOUS_CONTROLLERS);		
		setCustomView(DEFAULT_VIEW);
		try {
			MapFacade.remove(getModelName(), getView().getKeyValues());
			resetDescriptionsCache();
		}
		catch (ValidationException ex) {
			addErrors(ex.getErrors());	
			return;
		}		
		addMessage("object_deleted", getModelName());
		getView().clear();
		boolean selected = false;
		if (getTab().hasSelected()) {
			removeSelected();
			selected = true;
		}
		else getTab().reset();		 		
		super.execute(); // viewDetail
		if (isNoElementsInList()) {
			if (
				(!selected && getTab().getTotalSize() > 0) ||
				(selected && getTab().getSelected().length > 0)
			) {				
				setIncrement(-1);
				getErrors().remove("no_list_elements");								
				super.execute();													
			}
			else {							
				getView().setKeyEditable(false);
				getView().setEditable(false);
			}
		}
	}

	private void removeSelected() {
		int row = getRow();		
		int [] selectedOnes = getTab().getSelected();
		if (Arrays.binarySearch(selectedOnes, row) < 0) return;		
		int [] news = new int[selectedOnes.length-1];
		int j = 0;		
		for (int i = 0; i < news.length; i++) {
			int v = selectedOnes[j];
			if (v == row) {
				j++; i--;				
			} 
			else  {				
				news[i] = v;
				j++;
			}					
		}
		getTab().setAllSelected(news);
	}

	public String [] getNextControllers() {
		return nextControllers;
	}

	public String getCustomView() {		
		return nextView;
	}

	public void setNextControllers(String [] string) {
		nextControllers = string;
	}
	
	public void setCustomView(String string) {
		nextView = string;		
	}

}


