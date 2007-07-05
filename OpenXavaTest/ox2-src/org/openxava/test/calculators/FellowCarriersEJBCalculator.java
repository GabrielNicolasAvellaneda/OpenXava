package org.openxava.test.calculators;

import java.rmi.*;

import org.openxava.calculators.*;
import org.openxava.test.model.*;

/**
 * @author Javier Paniza
 */
public class FellowCarriersEJBCalculator implements IModelCalculator {

	private ICarrier carrier;

	public Object calculate() throws Exception {
		return CarrierUtil.getHome().findFellowCarriersOfCarrier(
			carrier.getWarehouse().getZoneNumber(),
			new Integer(carrier.getWarehouse().getNumber()),
			new Integer(carrier.getNumber())
		);
	}

	public void setModel(Object entity) throws RemoteException {
		carrier = (ICarrier) entity;		
	}

}
 