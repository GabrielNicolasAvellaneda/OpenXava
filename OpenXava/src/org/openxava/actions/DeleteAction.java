package org.openxava.actions;




/**
 * @author Javier Paniza
 */

public class DeleteAction extends ViewBaseAction implements IChangeControllersAction {
	
	
	private String[] siguientesControladores;

	public void execute() throws Exception {
		siguientesControladores = null;
		if (getView().isKeyEditable()) {
			addError("no_delete_not_exists");
		}		
		else {
			getView().setEditable(false);
			getView().setKeyEditable(false);		
			siguientesControladores = new String [] { "ConfirmDelete" }; 
		}		
	}
	
	public String [] getNextControllers() {
		return siguientesControladores;
	}
	
}
