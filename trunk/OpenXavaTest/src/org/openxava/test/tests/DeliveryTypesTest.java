package org.openxava.test.tests;

import org.openxava.test.ejb.*;
import org.openxava.tests.*;
import org.openxava.util.*;
import org.openxava.test.ejb.*;


/**
 * @author Javier Paniza
 */

public class DeliveryTypesTest extends ModuleTestBase {
	
	public DeliveryTypesTest(String testName) {
		super(testName, "OpenXavaTest", "DeliveryTypes");		
	}
	
	public void testPostmodifiyCalculatorNotOnRead() throws Exception {
		assertListNotEmpty();
		execute("Mode.detailAndFirst");
		assertNoErrors();
		String description = getValue("description");
		assertTrue("Description must have value", !Is.emptyString(description));
		execute("Mode.list");
		assertNoErrors();
		execute("Mode.detailAndFirst");
		assertNoErrors();
		assertValue("description", description); // No changed on read
	}
	
	public void testRemoveValidator_postcreateCalculator_postmodifyCalculator() throws Exception {
		execute("CRUD.new");
		setValue("number", "66");
		setValue("description", "JUNIT");
		execute("CRUD.save");
		assertNoErrors();
		
		DeliveryValue deliveryValue = new DeliveryValue();
		deliveryValue.setInvoice_year(2002);
		deliveryValue.setInvoice_number(1);
		deliveryValue.setType_number(66);		
		deliveryValue.setNumber(66);
		deliveryValue.setDescription("JUNIT FOR DELIVERY TYPE");		
		Delivery delivery = DeliveryUtil.getHome().create(deliveryValue);
		
		setValue("number", "66");
		execute("CRUD.search");
		assertValue("number", "66");
		assertValue("description", "JUNIT CREATED"); // 'CREATED' is added in postcreate
		setValue("description", "JUNIT");
		execute("CRUD.save");
		assertNoErrors();
		
		setValue("number", "66");
		execute("CRUD.search");
		assertValue("number", "66");
		assertValue("description", "JUNIT MODIFIED"); // 'MODIFIED' is added in postmodify
				
		execute("CRUD.delete");
		execute("ConfirmDelete.confirmDelete");
		assertError("Delivery type can not delete because it is used in deliveries");
		
		delivery.remove();
		
		execute("ConfirmDelete.confirmDelete");
		assertNoErrors();
		assertMessage("Object deleted successfully");				 		
	}
	
			
}
