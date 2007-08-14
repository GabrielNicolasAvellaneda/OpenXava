package org.openxava.test.tests;

import org.openxava.jpa.*;
import org.openxava.test.model.*;
import org.openxava.tests.*;
import org.openxava.util.*;


/**
 * @author Javier Paniza
 */

public class DeliveryTypeTest extends ModuleTestBase {
	
	public DeliveryTypeTest(String testName) {
		super(testName, "DeliveryType");		
	}
	
	public void testParseObjectWithMultipleKeyThatAreReferenceInStereotypes() throws Exception {
		execute("CRUD.new");
		Delivery delivery = findDelivery();
		setValue("comboDeliveries", toKeyString(delivery));
		execute("DeliveryType.assertComboDeliveries");
		assertNoErrors();
		assertMessage("comboDeliveries=" + toKeyString(delivery));
		assertValue("comboDeliveries", toKeyString(delivery));
	}
			
	public void testSaveActionNotResetRefreshData() throws Exception {
		execute("CRUD.new");
		setValue("number", "66");
		setValue("description", "JUNIT");
		execute("DeliveryType.saveNotReset");
		assertNoErrors();
		
		assertValue("number", "66");		
		assertValue("description", "JUNIT CREATED"); // 'CREATED' is added in postcreate
		assertNoEditable("number");
		assertEditable("description");
		
		setValue("description", "JUNIT M CREATED"); // We modify because if it is not modified it is not saved hence calculators are not executed
		execute("DeliveryType.saveNotReset");
		assertValue("number", "66");		
		assertValue("description", "JUNIT M CREATED MODIFIED"); // 'MODIFIED' is added in postcreate
		assertNoEditable("number");
		assertEditable("description");
		
		
		execute("CRUD.delete");		
		assertNoErrors();
		assertMessage("Delivery type deleted successfully");				 				
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
		assertMessage("Delivery type created successfully");
				
		setValue("number", "66");
		execute("CRUD.search");
		assertValue("number", "66");
		assertValue("description", "JUNIT CREATED"); // 'CREATED' is added in postcreate
		setValue("description", "JUNIT");
		execute("CRUD.save");
		assertNoErrors();
		assertMessage("Delivery type modified successfully");
		
		setValue("number", "66");
		execute("CRUD.search");
		assertValue("number", "66");
		assertValue("description", "JUNIT MODIFIED"); // 'MODIFIED' is added in postmodify
		
		Delivery delivery = new Delivery();
		Invoice invoice = new Invoice();
		invoice.setYear(2002);
		invoice.setNumber(1);
		delivery.setInvoice(invoice);
		DeliveryType deliveryType = new DeliveryType();
		deliveryType.setNumber(66);
		delivery.setType(deliveryType);
		delivery.setNumber(66);
		delivery.setDescription("JUNIT FOR DELIVERY TYPE");
		XPersistence.getManager().persist(delivery);		
		XPersistence.commit();				
				
		execute("CRUD.delete");		
		assertError("Delivery type 66 can not delete because it is used in deliveries");
		assertEditable("description"); // because return to main view (and controllers)
				
		delivery = XPersistence.getManager().merge(delivery); 
		XPersistence.getManager().remove(delivery);		
		XPersistence.commit();		
		
		execute("CRUD.delete");		
		assertNoErrors();
		assertMessage("Delivery type deleted successfully");		
	}
	
	private Delivery findDelivery() {
		return (Delivery) Delivery.findAll().iterator().next();
	}

					
}
