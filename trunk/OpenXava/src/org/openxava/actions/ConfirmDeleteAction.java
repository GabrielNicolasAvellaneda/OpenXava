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
		try {
			MapFacade.remove(getModelName(), getView().getKeyValues());
			resetDescriptionsCache();
		}
		catch (ValidationException ex) {
			addErrors(ex.getErrors());	
			return;
		}		
		addMessage("object_deleted");
		getView().clear(); 
		getView().setEditable(true);
		getView().setKeyEditable(false);		
		setNextControllers(PREVIOUS_CONTROLLERS);		
		setCustomView(DEFAULT_VIEW);
		boolean seleccionado = false;
		if (getTab().hasSelected()) {
			quitarSeleccionado();
			seleccionado = true;
		}
		else getTab().reset();		 		
		super.execute(); // viewDetail
		if (isNoElementsInList()) {
			if (
				(!seleccionado && getTab().getTotalSize() > 0) ||
				(seleccionado && getTab().getSelected().length > 0)
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

	private void quitarSeleccionado() {
		int fila = getRow();		
		int [] seleccionados = getTab().getSelected();
		if (Arrays.binarySearch(seleccionados, fila) < 0) return;		
		int [] nuevos = new int[seleccionados.length-1];
		int j = 0;		
		for (int i = 0; i < nuevos.length; i++) {
			int v = seleccionados[j];
			if (v == fila) {
				j++; i--;				
			} 
			else  {				
				nuevos[i] = v;
				j++;
			}					
		}
		getTab().setAllSelected(nuevos);
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


