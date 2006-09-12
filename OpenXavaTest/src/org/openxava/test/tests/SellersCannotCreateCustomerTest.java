package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class SellersCannotCreateCustomerTest extends ModuleTestBase {
	

	public SellersCannotCreateCustomerTest(String testName) {
		super(testName, "SellersCannotCreateCustomer");		
	}

	public void testNotCreateNewReferenceFromCollection() throws Exception {
		execute("CRUD.new");
		execute("Collection.new", "viewObject=xava_view_customers");
		assertNoAction("Reference.createNew");
	}
	
	public void testMembersOfReferenceToEntityNotEditableInSection() throws Exception {
		execute("Mode.detailAndFirst");
		execute("Collection.edit", "row=0,viewObject=xava_view_section0_customers");
		assertEditable("customers.number");
		assertNoEditable("customers.name");
	}
			
}
