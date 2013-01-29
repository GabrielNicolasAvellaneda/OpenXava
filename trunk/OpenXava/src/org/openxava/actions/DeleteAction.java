package org.openxava.actions;

import java.util.*;



import org.openxava.model.*;
import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * @author Javier Paniza
 */

public class DeleteAction extends ViewDetailAction {
	
	
	
	public DeleteAction() {
		setIncrement(0);
	}

	public void execute() throws Exception { 
		if (getView().isKeyEditable()) {
			addError("no_delete_not_exists");
			return;
		}
		Map keyValues = getView().getKeyValues();
		try {
			MapFacade.remove(getModelName(), keyValues);
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
			removeSelected(keyValues);
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
		getErrors().clearAndClose(); // If removal is done, any additional error message may be confused
	}

	private void removeSelected(Map keyValues) throws XavaException {
		getTab().getSelectedKeys().remove(keyValues);
	}

}


