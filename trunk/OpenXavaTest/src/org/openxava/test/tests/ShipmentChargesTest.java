package org.openxava.test.tests;

import org.openxava.hibernate.*;
import org.openxava.test.model.*;
import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class ShipmentChargesTest extends ModuleTestBase {
			
	public ShipmentChargesTest(String testName) {
		super(testName, "OpenXavaTest", "ShipmentCharges");		
	}
	
	public void testOverlappedReferencesWithConverterInANotOverlappedColumn() throws Exception {
		deleteShipmentCharges();
		// Creating
		execute("CRUD.new");
		setValue("mode", "1");
		String shipment = getShipment().toString(); 
		setValue("shipment.KEY", shipment);
		setValue("amount", "150");
		execute("CRUD.save");
		assertNoErrors();
		assertValue("shipment.KEY", "");
		
		// Searching and verifying
		execute("Mode.list");
		execute("Mode.detailAndFirst");		
		assertValue("mode", "1"); 
		assertValue("shipment.KEY", shipment);
		assertValue("amount", "150");
	}
	
	private void deleteShipmentCharges() {
		XHibernate.getSession().createQuery("delete from ShipmentCharge").executeUpdate();
		XHibernate.commit();
	}
	
	private Shipment getShipment() {
		return (Shipment) Shipment.findByMode(1).iterator().next();
	}
}
