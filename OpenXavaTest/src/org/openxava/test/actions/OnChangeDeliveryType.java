package org.openxava.test.actions;

import org.openxava.actions.*;

/**
 * 
 * @author Javier Paniza
 */

public class OnChangeDeliveryType extends OnChangePropertyBaseAction {

	public void execute() throws Exception {
		addMessage("type=" + getNewValue());		
	}

}
