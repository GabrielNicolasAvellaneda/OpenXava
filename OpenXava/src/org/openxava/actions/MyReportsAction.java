package org.openxava.actions;

import javax.inject.*;
import org.openxava.session.*;

/**
 * 
 * @author Javier Paniza 
 */
public class MyReportsAction extends TabBaseAction {
	
	@Inject
	private MyReport myReport; 

	public void execute() throws Exception {
		setNextMode(DETAIL);
		showDialog();	
		getView().setTitleId("myReports");
		myReport = MyReport.createEmpty(getTab()); 
		getView().setModel(myReport);			
		if (myReport.getAllNames().length > 0) {
			getView().setEditable("name", false);
			getView().addActionForProperty("name", "MyReport.createNew");
			getView().setValueNotifying("name", myReport.getLastName()); 
			myReport = (MyReport) getView().getModel(); 
		}
		else {
			myReport = MyReport.create(getTab()); 
			getView().setModel(myReport);			
		}
		getView().addActionForProperty("name", "MyReport.remove");						
		setControllers("MyReport");
	}
	
}
