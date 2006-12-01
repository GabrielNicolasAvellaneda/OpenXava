package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Javier Paniza
 */

public class CreateNewFromReferenceAction extends NavigationFromReferenceBaseAction implements INavigationAction, IRequestAction, IChainAction {

	private Log log = LogFactory.getLog(CreateNewFromReferenceAction.class);
	
	public String getCustomController() {	
		return getModel() + "Creation";
	}
	
	public String getDefaultController() {
		return "NewCreation";
	}
		
	public String getNextAction() throws Exception {
		return getController() + ".new";
	}
	
}
