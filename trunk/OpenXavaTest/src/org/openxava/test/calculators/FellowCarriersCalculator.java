package org.openxava.test.calculators;

import java.rmi.*;

import org.hibernate.*;
import org.openxava.calculators.*;
import org.openxava.hibernate.XHibernate;
import org.openxava.model.impl.*;
import org.openxava.test.model.*;

/**
 * @author Javier Paniza
 */
public class FellowCarriersCalculator implements IEntityCalculator {

	private ICarrier carrier;

	public Object calculate() throws Exception {
		Session session = XHibernate.getSession();
		Query query = session.createQuery("from Carrier as o where " +
				"o.warehouse.zoneNumber = :warehouseZone AND " +
				"o.warehouse.number = :warehouseNumber AND " +
				"NOT (o.number = :number)");
		query.setInteger("warehouseZone", carrier.getWarehouse().getZoneNumber());
		query.setInteger("warehouseNumber", carrier.getWarehouse().getNumber());
		query.setInteger("number", carrier.getNumber()); 
		return query.list();		
	}

	public void setEntity(Object entity) throws RemoteException {
		carrier = (ICarrier) entity;		
	}

}
 