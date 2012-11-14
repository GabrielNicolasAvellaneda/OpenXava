package org.openxava.actions;

import javax.inject.*;
import org.openxava.session.*;

/**
 * 
 * @author Javier Paniza
 */
public class RemoveCustomReportColumnAction extends CollectionBaseAction {
	
	@Inject
	private CustomReport customReport; 
	
	public void execute() throws Exception {
		if (getRow() == 0) return;
		if (getMapValues().size() == 1) return;
		customReport.getColumns().remove(getRow());		
	}

}
