package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * In list mode return to initial state (if we have navigated). <p>
 * 
 * This action works well also in the case of list that is not the
 * main list mode of the module, for example, from list used for
 * searching references.
 * 
 * @author Javier Paniza
 */

public class CancelInListAction extends BaseAction implements IChangeModeAction, INavigationAction {
	
	private static Log log = LogFactory.getLog(CancelInListAction.class);
	
	public String getNextMode() {
		// PREVIOUS_MODE (instead of LIST) in order to work also in list for searching references
		return IChangeModeAction.PREVIOUS_MODE;
	}

	public void execute() throws Exception {		
	}


	public String[] getNextControllers() {		
		return PREVIOUS_CONTROLLERS;
	}

	public String getCustomView() {		
		return PREVIOUS_VIEW;
	}

}
