package org.openxava.actions;

import javax.inject.*;
import org.openxava.session.*;

/**
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
		getView().setModel(customReport);		
		setControllers("CustomReport");
	}

}
