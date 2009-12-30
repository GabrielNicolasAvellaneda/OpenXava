package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class SellerCannotCreateCustomerTest extends ModuleTestBase {
	

	public SellerCannotCreateCustomerTest(String testName) {
		super(testName, "SellerCannotCreateCustomer");		
	}

	/* Since v2.2 this does not apply. See at testEntityReferenceCollections to
	 * see the current entity collection behaviour
	public void testNotCreateNewReferenceFromCollection() throws Exception {
		execute("CRUD.new");
		execute("Collection.new", "viewObject=xava_view_section0_customers");
		assertExists("customers.number"); // to verify that collection element view is opened
		assertNoAction("Reference.createNew");
		assertNoAction("Reference.modify");
	}
	*/
	
	public void testMembersOfReferenceToEntityNotEditableInSection_notCreateNewReferenceFromCollectionOnEdit() throws Exception {
		execute("Mode.detailAndFirst");
		execute("Collection.view", "row=0,viewObject=xava_view_section0_customers");
		assertNoEditable("number");
		assertNoEditable("name");	
		assertNoAction("Reference.createNew");
		assertNoAction("Reference.modify");
	}
			
}
