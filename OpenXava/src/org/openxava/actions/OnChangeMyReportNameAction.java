package org.openxava.actions;

import javax.inject.*;

import org.openxava.session.*;

/**
 * 
 * @author Javier Paniza 
 */

public class OnChangeMyReportNameAction extends TabBaseAction implements IOnChangePropertyAction  {
	
	@Inject
	private MyReport myReport;

	private String name;
	
	public void execute() throws Exception {
		myReport = MyReport.find(getTab(), name);
		getView().setModel(myReport);		
	}

	public void setChangedProperty(String propertyName) {
	}

	public void setNewValue(Object value) {
		name = (String) value;		
	}

}
