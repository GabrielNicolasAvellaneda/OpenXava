package org.openxava.actions;

import javax.inject.*;

import org.apache.commons.logging.*;
import org.openxava.session.*;

/**
 * 
 * @author Javier Paniza 
 */
public class MyReportsAction extends TabBaseAction {
	private static Log log = LogFactory.getLog(MyReportsAction.class);
	
	@Inject
	private MyReport myReport; 
	
	private boolean fromAdminReportsAction = false;

	public void execute() throws Exception {
		getContext().put(getRequest(), "xava_fromAdminReportsAction", fromAdminReportsAction);
		setNextMode(DETAIL);
		showDialog();
		getView().setTitleId(fromAdminReportsAction ? "adminReports" : "myReports");
		myReport = MyReport.createEmpty(getTab(), fromAdminReportsAction);
		getView().setModel(myReport);
		if (myReport.getAllNames(fromAdminReportsAction).length > 0) {
			getView().setEditable("name", false);
			getView().addActionForProperty("name", "MyReport.createNew");
			getView().setValueNotifying("name", myReport.getLastName(fromAdminReportsAction));
			myReport = (MyReport) getView().getModel();
		}	
		else {
			myReport = MyReport.create(getTab());
			getView().setModel(myReport);			
		}
		setControllers("MyReport");
	}

	public boolean isAdminAction() {
		return fromAdminReportsAction;
	}

	public void setAdminAction(boolean adminAction) {
		this.fromAdminReportsAction = adminAction;
	}

}
