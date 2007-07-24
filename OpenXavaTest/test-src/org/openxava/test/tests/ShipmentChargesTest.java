package org.openxava.test.tests;

import org.openxava.jpa.*;
import org.openxava.test.model.*;
import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class ShipmentChargesTest extends ModuleTestBase {
			
	public ShipmentChargesTest(String testName) {
		super(testName, "ShipmentCharges");		
	}
	
	public void testOverlappedReferencesWithConverterInANotOverlappedColumn() throws Exception {
		deleteShipmentCharges();
		// Creating
		execute("CRUD.new");
		setValue("mode", isOX3()?"0":"1");
		String shipment = toKeyString(getShipment()); 
		setValue("shipment.KEY", shipment);
		setValue("amount", "150");
		execute("CRUD.save");
		assertNoErrors();
		assertValue("shipment.KEY", "");
		
		// Searching and verifying
		execute("Mode.list");
		execute("Mode.detailAndFirst");		
		assertValue("mode", isOX3()?"0":"1"); 
		assertValue("shipment.KEY", shipment);
		assertValue("amount", "150.00");
	}
	
	private void deleteShipmentCharges() {
		XPersistence.getManager().createQuery("delete from ShipmentCharge").executeUpdate();
		XPersistence.commit();
	}
	
	private Shipment getShipment() {
		return (Shipment) Shipment.findByMode(isOX3()?0:1).iterator().next();
	}
}
