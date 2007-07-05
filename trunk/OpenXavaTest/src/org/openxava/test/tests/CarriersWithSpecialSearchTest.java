package org.openxava.test.tests;

import org.openxava.tests.ModuleTestBase;
import org.openxava.jpa.*;
import org.openxava.test.model.*;


public class CarriersWithSpecialSearchTest extends ModuleTestBase {
	public CarriersWithSpecialSearchTest(String testName) {
		super(testName, "CarriersWithSpecialSearch");		
	}
	
	protected void setUp() throws Exception {
		deleteCarriers();
		createCarriers();  		
		super.setUp();
	}
	
	public void testSearchCarrierWithDefaultValue() throws Exception {
		execute("CRUD.new");
		execute("CRUD.search");
		assertNoErrors();
		assertValue("name", "TRES");
		assertValue("number", "3");
		assertNoErrors();
	}

	
	private void createCarriers() throws Exception {
		Warehouse wh = new Warehouse();
		wh.setNumber(1);
		wh.setZoneNumber(1);
		Carrier c1 = new Carrier();
		c1.setWarehouse(wh);
		// driving licence is not set to test converters in references
		c1.setNumber(1);
		c1.setName("UNO");
		XPersistence.getManager().persist(c1);
		
		Carrier c2 = new Carrier();
		c2.setWarehouse(wh);
		c2.setNumber(2);
		c2.setName("DOS");
		XPersistence.getManager().persist(c2);				

		Carrier c3 = new Carrier();
		c3.setWarehouse(wh);
		c3.setNumber(3);
		c3.setName("TRES");
		XPersistence.getManager().persist(c3);
	
		Carrier c4 = new Carrier();
		c4.setWarehouse(wh);
		c4.setNumber(4);
		c4.setName("CUATRO");
		XPersistence.getManager().persist(c4);
		
		Warehouse wh2 = new Warehouse();
		wh2.setNumber(1);
		wh2.setZoneNumber(2);
		
		Carrier c5 = new Carrier();
		c5.setWarehouse(wh2);
		c5.setNumber(5);
		c5.setName("Cinco");
		XPersistence.getManager().persist(c5);
		
		XPersistence.commit();
	}
	
	private void deleteCarriers()
		throws Exception {
		XPersistence.getManager().createQuery("delete from Carrier").executeUpdate();
		XPersistence.commit();
	}
}
