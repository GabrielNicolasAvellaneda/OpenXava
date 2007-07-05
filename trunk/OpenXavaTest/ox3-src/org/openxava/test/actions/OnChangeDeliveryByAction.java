package org.openxava.test.actions;

import org.openxava.actions.*;
import org.openxava.test.model.*;

/**
 * @author Javier Paniza
 */
public class OnChangeDeliveryByAction extends OnChangePropertyBaseAction {

	public void execute() throws Exception {
		Delivery.DeliveredBy deliveryBy = (Delivery.DeliveredBy) getNewValue();			
		getView().setHidden("employee", deliveryBy != Delivery.DeliveredBy.EMPLOYEE);
		getView().setHidden("carrier", deliveryBy != Delivery.DeliveredBy.CARRIER);
	}

}
