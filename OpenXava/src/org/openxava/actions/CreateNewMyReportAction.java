package org.openxava.actions;

import javax.inject.*;

import org.openxava.session.*;

/**
 * 
 * @author Javier Paniza
 */
public class CreateNewMyReportAction extends TabBaseAction {
	
	@Inject
	private MyReport myReport; 

	public void execute() throws Exception {
		myReport = MyReport.create(getTab());
		getView().setModel(myReport);		
		getView().setEditable("name", true);			
		getView().removeActionForProperty("name", "MyReport.createNew");
	}

}
