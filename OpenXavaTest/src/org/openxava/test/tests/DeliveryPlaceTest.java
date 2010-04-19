package org.openxava.test.tests;

import org.openxava.tests.ModuleTestBase;

/**
 * Create on 16/04/2010 (15:27:30)
 * @author Ana Andres
 */
public class DeliveryPlaceTest extends ModuleTestBase {
	
	public DeliveryPlaceTest(String testName) {
		super(testName, "DeliveryPlace");		
	}
	
	public void testFilterToDescriptionsListWithBaseConditionAndFilter() throws Exception {
		assertLabelInList(3, "Name of Preferred warehouse");
		assertValueInList(0, 3, "CENTRAL VALENCIA");
		setConditionValues(new String[] { "", "", "", "[.1.1.]"} );
		execute("List.filter");
		assertListNotEmpty();
	}

}
