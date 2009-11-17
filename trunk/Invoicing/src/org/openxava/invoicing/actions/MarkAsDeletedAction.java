package org.openxava.invoicing.actions;

import static org.openxava.jpa.XPersistence.*;

import java.util.*;

import org.openxava.actions.*;
import org.openxava.invoicing.model.*;
import org.openxava.model.*;
import org.openxava.validators.*;

public class MarkAsDeletedAction extends ViewBaseAction {

	public void execute() throws Exception {
		if (getView().getKeyValuesWithValue().isEmpty()) {
			addError("no_delete_not_exists");
			return;
		}
		Map values = new HashMap();
		values.put("deleted", true);
		MapFacade.setValues(getModelName(), getView().getKeyValues(), values);
		resetDescriptionsCache();
		addMessage("object_deleted", getModelName());
		getView().clear();
		getView().setEditable(false);
	}

}
