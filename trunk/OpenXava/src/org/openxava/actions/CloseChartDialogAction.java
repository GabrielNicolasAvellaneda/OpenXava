package org.openxava.actions;

/**
 * 
 * @author Javier Paniza
 */

public class CloseChartDialogAction extends CancelDialogAction {
	
	public void execute() throws Exception {
		super.execute();
		getRequest().getSession().removeAttribute("xava_chartTab");
	}
	
	

}
