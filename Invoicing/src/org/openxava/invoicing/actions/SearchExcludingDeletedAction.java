package org.openxava.invoicing.actions;

import java.util.*;

import org.openxava.actions.*;

public class SearchExcludingDeletedAction extends SearchByViewKeyAction {
		
	protected Map getValuesFromView() throws Exception {	
		Map values = super.getValuesFromView();
		values.put("deleted", false);
		return values;
	}

}
