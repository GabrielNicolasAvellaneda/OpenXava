package org.openxava.test.tests;

import org.openxava.test.model.*;
import org.openxava.tests.*;
import org.openxava.util.*;


/**
 * @author Javier Paniza
 */

public class DeliveryTypesTest extends ModuleTestBase {
	
	public DeliveryTypesTest(String testName) {
		super(testName, "OpenXavaTest", "DeliveryTypes");		
	}
	
	public void testSaveActionNotResetRefreshData() throws Exception {
		execute("CRUD.new");
		setValue("number", "66");
		setValue("description", "JUNIT");
		execute("DeliveryTypes.saveNotReset");
		assertNoErrors();
		
		assertValue("number", "66");		
		assertValue("description", "JUNIT CREATED"); // 'CREATED' is added in postcreate
		assertNoEditable("number");
		assertEditable("description");
		
		execute("CRUD.delete");		
		assertNoErrors();
		assertMessage("DeliveryType deleted successfully");				 				
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
		DeliveryRemote delivery = DeliveryUtil.getHome().create(deliveryValue);
		
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
		assertError("Delivery type can not delete because it is used in deliveries");
		assertEditable("description"); // because return to main view (and controllers)
		
		delivery.remove();
		
		execute("CRUD.delete");		
		assertNoErrors();
		assertMessage("DeliveryType deleted successfully");				 		
	}
	
			
}
