package org.openxava.actions;

import java.util.*;

import org.openxava.model.*;
import org.openxava.tab.*;
import org.openxava.validators.*;

/**
 * @author Javier Paniza
 */

public class DeleteSelectedAction extends BaseAction implements IModelAction {
	
	private Tab tab;
	private String model;

	public void execute() throws Exception {				
		int [] selectedOnes = tab.getSelected();
		if (selectedOnes != null) {						
			for (int i = 0; i < selectedOnes.length; i++) {				
				Map clave = (Map) getTab().getTableModel().getObjectAt(selectedOnes[i]);
				try {									
					MapFacade.remove(model, clave);					
				}
				catch (ValidationException ex) {
					addError("no_delete_row", new Integer(i), clave);
					addErrors(ex.getErrors());
				}								
				catch (Exception ex) {
					addError("no_delete_row", new Integer(i), clave);
				}				
			}
			getTab().deselectAll();
			resetDescriptionsCache();
		}
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab web) {
		tab = web;
	}

	public void setModel(String modelName) {
		this.model = modelName;		
	}

}
