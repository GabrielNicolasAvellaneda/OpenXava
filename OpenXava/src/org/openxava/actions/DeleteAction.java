package org.openxava.actions;

/**
 * @author Javier Paniza
 */

public class DeleteAction extends ViewBaseAction implements IChangeControllersAction {
	
	
	private String[] nextControllers;

	public void execute() throws Exception {
		nextControllers = null;
		if (getView().isKeyEditable()) {
			addError("no_delete_not_exists");
		}		
		else {
			getView().setEditable(false);
			getView().setKeyEditable(false);		
			nextControllers = new String [] { "ConfirmDelete" }; 
		}		
	}
	
	public String [] getNextControllers() {
		return nextControllers;
	}
	
}
