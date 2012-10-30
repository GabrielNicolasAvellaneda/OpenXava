package org.openxava.actions;

import java.util.*;

import javax.inject.*;

import org.openxava.model.inner.*;

/**
 * tmp ¿Unir con CustomReportColumnUpAction?
 * 
 * @author Javier Paniza
 */
public class CustomReportColumnUpAction extends CollectionBaseAction {
	
	@Inject
	private CustomReport customReport; 
	
	public void execute() throws Exception {
		if (getRow() == 0) return;
		if (getMapValues().size() == 1) return;
		Collections.swap(customReport.getColumns(), getRow(), getRow() - 1);
	}

}
