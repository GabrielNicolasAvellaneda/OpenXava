package org.openxava.test.calculators;

import java.rmi.*;

import org.openxava.calculators.*;
import org.openxava.test.ejb.*;
import org.openxava.test.ejb.*;

/**
 * @author Javier Paniza
 */
public class FellowCarriersCalculator implements IEntityCalculator {

	private ICarrier carrier;

	public Object calculate() throws Exception {
		return CarrierUtil.getHome().findFellowCarriersDeCarrier(
			carrier.getWarehouseKey().getZoneNumber(),
			carrier.getWarehouseKey().get_Number(),
			new Integer(carrier.getNumber())
		);
	}

	public void setEntity(Object entity) throws RemoteException {
		carrier = (ICarrier) entity;		
	}

}
 