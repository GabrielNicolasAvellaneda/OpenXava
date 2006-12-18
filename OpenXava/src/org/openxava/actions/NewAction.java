package org.openxava.actions;




/**
 * @author Javier Paniza
 */

public class NewAction extends ViewBaseAction implements IChangeModeAction {
	
	
	
	public void execute() throws Exception {
		getView().setKeyEditable(true);
		getView().setEditable(true);
		getView().reset();
	}
	
	public String getNextMode() {
		return IChangeModeAction.DETAIL;
	}
	
}
