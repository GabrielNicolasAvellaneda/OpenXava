package org.openxava.test.tests;

import org.openxava.test.model.*;


/**
 * @author Javier Paniza
 */

public class ProductsWithSectionTest extends ProductsTest {
		
	public ProductsWithSectionTest(String testName) {
		super(testName, "ProductsWithSection");		
	}
	
	public void testEmptyCombosUsingComposeKeys() throws Exception {
		execute("CRUD.new");
		execute("Products.setLimitZoneTo1"); 
		
		WarehouseKey key1 = new WarehouseKey();
		key1.setZoneNumber(1);
		key1.set_Number(new Integer(1));
		WarehouseKey key2 = new WarehouseKey();
		key2.setZoneNumber(1);
		key2.set_Number(new Integer(2));
		WarehouseKey key3 = new WarehouseKey();
		key3.setZoneNumber(1);
		key3.set_Number(new Integer(3));		
		
		String [][] zone1WarehouseValues = new String [][] {
			{ "", "" },
			{ key1.toString(), "CENTRAL VALENCIA" },
			{ key3.toString(), "VALENCIA NORTE" },
			{ key2.toString(), "VALENCIA SURETE" }
		};
			
		assertValidValues("warehouseKey", zone1WarehouseValues);
		
		execute("Products.setLimitZoneTo0");		
		String [][] zoneEmptyWarehouseValues = new String [][] { 
			{ "", "" },		
		};		
		assertValidValues("warehouseKey", zoneEmptyWarehouseValues);
		
	}
	
}
