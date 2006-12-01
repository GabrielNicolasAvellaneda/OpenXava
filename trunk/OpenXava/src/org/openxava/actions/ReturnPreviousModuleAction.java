package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Javier Paniza
 */

public class ReturnPreviousModuleAction extends BaseAction implements IChangeModuleAction {

	private Log log = LogFactory.getLog(ReturnPreviousModuleAction.class);
	
	public void execute() throws Exception {
	}

	public String getNextModule() {
		return PREVIOUS_MODULE;
	}

	public boolean hasReinitNextModule() { 
		return false;
	}

}
