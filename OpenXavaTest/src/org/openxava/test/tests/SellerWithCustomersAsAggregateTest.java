package org.openxava.test.tests;

import org.openxava.jpa.*;
import org.openxava.test.model.*;
import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class SellerWithCustomersAsAggregateTest extends ModuleTestBase {
	
	public SellerWithCustomersAsAggregateTest(String testName) {
		super(testName, "SellerWithCustomersAsAggregate");		
	}
	
	public void testEntityCollectionAsAggregate() throws Exception {
		execute("CRUD.new");
		setValue("number", "3");
		execute("CRUD.search");
		assertValue("name", "ELISEO FERNANDEZ");
		assertCollectionRowCount("customers", 0);
						
		assertCustomerNotExists(66);
		execute("Collection.new", "viewObject=xava_view_customers");
		assertEditable("customers.number");
		assertEditable("customers.name");
		assertValue("customers.number", ""); // Test if clear the fields
		assertValue("customers.type", usesAnnotatedPOJO()?"0":"1"); // Test if execute default value calculators
		
		// Creating
		setValue("customers.number", "66");
		setValue("customers.name", "JUNIT 66");
		setValue("customers.type", usesAnnotatedPOJO()?"0":"1");
		setValue("customers.address.street", "AV. CONSTITUCION");
		setValue("customers.address.zipCode", "46540");
		setValue("customers.address.city", "EL PUIG");
		setValue("customers.address.state.id", "CA");
		
		execute("Collection.save", "viewObject=xava_view_customers");
		assertMessage("Customer created and associated to Seller");
		assertCollectionRowCount("customers", 1);
		assertValueInCollection("customers", 0, "name", "Junit 66");
		assertCustomerExists(66);
		
		// Modifiying
		execute("Collection.edit", "row=0,viewObject=xava_view_customers");
		setValue("customers.name", "JUNIT 66x");
		execute("Collection.save", "viewObject=xava_view_customers");
		assertMessage("Customer modified successfully");
		assertValueInCollection("customers", 0, "name", "Junit 66x");
		
		// Removing
		execute("Collection.edit", "row=0,viewObject=xava_view_customers");
		execute("Collection.remove", "viewObject=xava_view_customers");
		assertMessage("Association between Customer and Seller has been removed, but Customer is still in database");
		assertCollectionRowCount("customers", 0);
		// Object is not remove from database, only the association is removed
		// This is because is a collection of entities, although is managed as an aggregate
		assertCustomerExists(66); 									
		removeCustomer(66);
	}

	private void removeCustomer(int number) throws Exception {
		XPersistence.getManager().remove(XPersistence.getManager().find(Customer.class, number)); 		
	}

	private void assertCustomerNotExists(int number) {
		if (XPersistence.getManager().find(Customer.class, new Integer(number)) != null) {		
			fail("Customer " + number + " exists and it shouldn't");
		}
				
	}
	
	private void assertCustomerExists(int number) {
		if (XPersistence.getManager().find(Customer.class, new Integer(number)) == null) {		
			fail("Customer " + number + " does not exist and it should");
		}		
	}
	
				
}