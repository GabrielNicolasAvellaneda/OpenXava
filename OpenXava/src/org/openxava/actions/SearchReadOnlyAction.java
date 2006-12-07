package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Javier Paniza
 */

public class SearchReadOnlyAction extends SearchByViewKeyAction {
	
	private static Log log = LogFactory.getLog(SearchReadOnlyAction.class);
	
	public void execute() throws Exception {
		super.execute();
		getView().setEditable(false);
	}

}
