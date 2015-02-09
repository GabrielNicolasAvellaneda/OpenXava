package org.openxava.test.actions;

import org.openxava.actions.*;
import org.openxava.test.model.*;

/**
 * 
 * @author Javier Paniza
 */

public class OnChangeDeliveryTypeAction extends OnChangePropertyBaseAction {

	public void execute() throws Exception {
		addMessage("type=" + getNewValue());
		Delivery delivery = (Delivery) getView().getEntity();
		addMessage("type.description=" + delivery.getType().getDescription());
	}

}
