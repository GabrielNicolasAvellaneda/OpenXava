package org.openxava.actions;

import javax.inject.*;

import org.apache.commons.logging.*;
import org.openxava.session.*;

/**
 * 
 * @author Javier Paniza 
 */

public class OnChangeMyReportNameAction extends TabBaseAction implements IOnChangePropertyAction  {
	private static Log log = LogFactory.getLog(OnChangeMyReportNameAction.class);
	
	@Inject
	private MyReport myReport;

	private String name;
	
	public void execute() throws Exception {
		Boolean fromAdminReports = (Boolean)getContext().get(getRequest(), "xava_fromAdminReportsAction");
		boolean adminReport = fromAdminReports ? true : name.endsWith(MyReport.ADMIN_REPORT);
		myReport = MyReport.find(getTab(), name, adminReport);
		getView().setModel(myReport);
		
		if (fromAdminReports|| !myReport.isAdmin()) getView().addActionForProperty("name", "MyReport.remove");
		else getView().removeActionForProperty("name", "MyReport.remove");
	}

	public void setChangedProperty(String propertyName) {
	}

	public void setNewValue(Object value) {
		name = (String) value;		
	}
	
}
