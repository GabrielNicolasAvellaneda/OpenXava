package org.openxava.actions;


/**
 * @author Javier Paniza
 */

public class NewAction extends ViewBaseAction implements IChangeModeAction {
	
		
	public void execute() throws Exception {		
		getView().reset();
		getView().setKeyEditable(true);
		getView().setEditable(true);
	}
	
	public String getNextMode() {
		return IChangeModeAction.DETAIL;
	}
	
}
