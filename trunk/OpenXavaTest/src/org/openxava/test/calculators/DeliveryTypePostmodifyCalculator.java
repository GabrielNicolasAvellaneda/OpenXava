package org.openxava.test.calculators;

import java.rmi.*;

import org.openxava.calculators.*;
import org.openxava.test.ejb.*;

/**
 * @author Javier Paniza
 */

public class DeliveryTypePostmodifyCalculator implements IEntityCalculator {
	
	private IDeliveryType deliveryType;

	public Object calculate() throws Exception {
		deliveryType.setDescription(deliveryType.getDescription() + " MODIFIED");
		return null;
	}

	public void setEntity(Object entity) throws RemoteException {
		deliveryType = (IDeliveryType) entity;		
	}

}
