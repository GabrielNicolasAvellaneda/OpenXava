package org.openxava.actions;

import javax.inject.*;

import org.openxava.model.inner.*;

/**
 * tmp
 * 
 * @author Javier Paniza 
 */
public class CustomReportAction extends TabBaseAction {
	
	@Inject
	private CustomReport customReport; 

	public void execute() throws Exception {
		setNextMode(DETAIL);
		showDialog();			
		customReport = CustomReport.create(getTab());
		getView().setPOJO(customReport);		
		setControllers("CustomReport");
	}

}
