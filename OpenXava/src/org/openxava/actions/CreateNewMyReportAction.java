package org.openxava.actions;

import javax.inject.*;

import org.apache.commons.logging.*;
import org.openxava.session.*;

/**
 * 
 * @author Javier Paniza
 */
public class CreateNewMyReportAction extends TabBaseAction {
	private static Log log = LogFactory.getLog(CreateNewMyReportAction.class);
	
	@Inject
	private MyReport myReport; 

	public void execute() throws Exception {
		Boolean fromAdminReportsAction = (Boolean)getContext().get(getRequest(), "xava_fromAdminReportsAction");
		myReport = MyReport.create(getTab());
		myReport.setAdmin(fromAdminReportsAction);
		getView().setModel(myReport);		
		getView().setEditable("name", true);			
		getView().removeActionForProperty("name", "MyReport.createNew");
	}

}
