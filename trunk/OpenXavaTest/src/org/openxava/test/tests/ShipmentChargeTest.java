package org.openxava.test.tests;

import org.openxava.jpa.*;
import org.openxava.test.model.*;
import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class ShipmentChargeTest extends ModuleTestBase {
			
	public ShipmentChargeTest(String testName) {
		super(testName, "ShipmentCharge");		
	}
	
	public void testOverlappedReferencesWithConverterInANotOverlappedColumn() throws Exception {
		deleteShipmentCharges();
		// Creating
		execute("CRUD.new");
		setValue("mode", usesAnnotatedPOJO()?"0":"1");
		String shipment = toKeyString(getShipment()); 
		setValue("shipment.KEY", shipment);
		setValue("amount", "150");
		execute("CRUD.save");
		assertNoErrors();
		assertValue("shipment.KEY", "");
		
		// Searching and verifying
		execute("Mode.list");
		execute("Mode.detailAndFirst");		
		assertValue("mode", usesAnnotatedPOJO()?"0":"1"); 
		assertValue("shipment.KEY", shipment);
		assertValue("amount", "150.00");
	}
	
	public void testFilterToDescriptionsList() throws Exception {
		assertListRowCount(1);
		assertLabelInList(2, "Number of Shipment");
		assertLabelInList(3, "Slow");	// fail filter: calculated property and descriptions list
		assertLabelInList(4, "Description of Shipment");
		
		// reference property: descriptionsList
		setConditionValues(new String[] { "", "", "", "[.MEDIUM.5.INTERNAL.]"} ); // For JPA entities
//		setConditionValues(new String[] { "", "", "", "[.2.5.1.]"} ); // For XML components
		execute("List.filter");
		assertListRowCount(0);
		
		setConditionValues(new String[] { "", "", "", "[.SLOW.1.INTERNAL.]"} ); // For JPA entities
//		setConditionValues(new String[] { "", "", "", "[.1.1.1.]"} ); // For XML Components 
		execute("List.filter");
		assertListRowCount(1);
		
		// reference property: normal
		setConditionValues(new String[] { "", "", "1"} );
		execute("List.filter");
		assertListRowCount(1);
		
		setConditionValues(new String[] { "", "", "10"} );
		execute("List.filter");
		assertListRowCount(0);
	}
	
	private void deleteShipmentCharges() {
		XPersistence.getManager().createQuery("delete from ShipmentCharge").executeUpdate();
		XPersistence.commit();
	}
	
	private Shipment getShipment() {
		return (Shipment) Shipment.findByMode(usesAnnotatedPOJO()?0:1).iterator().next();
	}
}
