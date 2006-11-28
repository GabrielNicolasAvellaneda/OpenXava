package org.openxava.actions;

/**
 * @author Javier Paniza
 */

public class CreateNewFromReferenceAction extends NavigationFromReferenceBaseAction implements INavigationAction, IRequestAction, IChainAction {

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
