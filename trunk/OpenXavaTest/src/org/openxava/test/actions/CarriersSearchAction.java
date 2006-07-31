package org.openxava.test.actions;

import java.util.HashMap;
import java.util.Map;

import org.openxava.actions.SearchByViewKeyAction;

public class CarriersSearchAction extends SearchByViewKeyAction {
	private static final long serialVersionUID = 1L;
	private static final String NAME = "TRES";
	
	protected Map getCriteriaSearchValues() throws Exception {
		Map values = new HashMap(super.getCriteriaSearchValues());		
		values.put("name", NAME);		
		return values;
	}

}
