package org.openxava.invoicing.actions;

import java.util.*;
import org.openxava.actions.*;
import org.openxava.model.*;

public class InvoicingDeleteAction extends ViewBaseAction {

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
