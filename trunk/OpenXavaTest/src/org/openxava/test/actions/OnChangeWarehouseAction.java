package org.openxava.test.actions;

import org.openxava.actions.*;


/**
 * @author Javier Paniza
 */
public class OnChangeWarehouseAction extends OnChangePropertyBaseAction {
	
	private final static Integer ONE = new Integer(1);

	public void execute() throws Exception {			
		getView().setHidden("zoneOne", !ONE.equals(getNewValue()));		
	}

}
