package org.openxava.test.tests;

import java.rmi.*;

import org.openxava.test.ejb.*;
import org.openxava.tests.*;
import org.openxava.test.ejb.*;


/**
 * @author Javier Paniza
 */

public class SellersTest extends ModuleTestBase {
	
	private Customer customer2;
	private Customer customer1;
	private CustomerValue customerValue1;
	private CustomerValue customerValue2;

	public SellersTest(String testName) {
		super(testName, "OpenXavaTest", "Sellers");		
	}
	
	public void testOnChangeListDescriptionReferenceWithStringSingleKey() throws Exception {
		execute("CRUD.new");
		setValue("level.id", "A");
		assertNoErrors();
		setValue("level.id", "");
		assertNoErrors();
	}
	
	public void testEntityReferenceCollections() throws Exception {
		createSeller66();
		createSeller67();
		verifySeller66();
		deleteSeller("66");
		deleteSeller("67");		
		deleteCustomers();						
	}
	
	private void createSeller66() throws Exception {
		execute("CRUD.new");
		setValue("number", "66");
		setValue("name", "SELLER JUNIT 66");
		setValue("level.id", "A");
		execute("Collection.new", "viewObject=xava_view_customers");
		assertEditable("customers.number");
		assertNoEditable("customers.name");
		setValue("customers.number", getCustomerNumber1());
		// Comparing name with ignore case because a formatter in customer name
		assertValueIgnoringCase("customers.name", getCustomerValue1().getName()); 
		assertCollectionRowCount("customers",0);
		execute("Collection.save", "viewObject=xava_view_customers");
		assertCollectionRowCount("customers",1);
		
		execute("Collection.new", "viewObject=xava_view_customers");
		assertEditable("customers.number");
		assertNoEditable("customers.name");
		setValue("customers.number", getCustomerNumber2());		
		assertValueIgnoringCase("customers.name", getCustomerValue2().getName());
		execute("Collection.save", "viewObject=xava_view_customers");
		assertCollectionRowCount("customers",2);
		
		assertValueInCollection("customers", 0, 0, getCustomerNumber1());
		assertValueInCollectionIgnoringCase("customers", 0, 1, getCustomerValue1().getName());
		assertValueInCollection("customers", 0, 2, getCustomerValue1().getRemarks());
		assertValueInCollection("customers", 0, 3, getCustomerValue1().getRelationWithSeller());
		assertValueInCollection("customers", 0, 4, getCustomer1().getSeller().getLevel().getDescription());
		
		assertValueInCollection("customers", 1, 0, getCustomerNumber2());
		assertValueInCollectionIgnoringCase("customers", 1, 1, getCustomerValue2().getName());
		assertValueInCollection("customers", 1, 2, getCustomerValue2().getRemarks());
		assertValueInCollection("customers", 1, 3, getCustomerValue2().getRelationWithSeller());
		assertValueInCollection("customers", 1, 4, getCustomer2().getSeller().getLevel().getDescription());
	}
	
	private void createSeller67() throws Exception {
		execute("CRUD.new");
		setValue("number", "67");
		setValue("name", "SELLER JUNIT 67");
		setValue("level.id", "B");
		execute("Collection.new", "viewObject=xava_view_customers");
		assertEditable("customers.number");
		assertNoEditable("customers.name");
		setValue("customers.number", getCustomerNumber2());
		assertValueIgnoringCase("customers.name", getCustomerValue2().getName());
		assertCollectionRowCount("customers",0);
		execute("Collection.save", "viewObject=xava_view_customers");
		assertCollectionRowCount("customers",1);
		
		assertValueInCollection("customers", 0, 0, getCustomerNumber2());
		assertValueInCollectionIgnoringCase("customers", 0, 1, getCustomerValue2().getName());
		assertValueInCollection("customers", 0, 2, getCustomerValue2().getRemarks());
		assertValueInCollection("customers", 0, 3, getCustomerValue2().getRelationWithSeller());
		assertValueInCollection("customers", 0, 4, getCustomer2().getSeller().getLevel().getDescription());
	}
	
	private void verifySeller66() throws Exception {
		execute("CRUD.new");
		setValue("number", "66");
		execute("CRUD.search");
		assertNoErrors();
		assertCollectionRowCount("customers", 1);
		assertValueInCollection("customers", 0, 0, getCustomerNumber1());
		assertValueInCollectionIgnoringCase("customers", 0, 1, getCustomerValue1().getName());
		assertValueInCollection("customers", 0, 2, getCustomerValue1().getRemarks());
		assertValueInCollection("customers", 0, 3, getCustomerValue1().getRelationWithSeller());
		assertValueInCollection("customers", 0, 4, getCustomer1().getSeller().getLevel().getDescription());
		
		execute("Collection.edit", "row=0,viewObject=xava_view_customers");
		execute("Collection.remove", "viewObject=xava_view_customers");
		assertNoErrors();
		assertCollectionRowCount("customers", 0);		
	}
		
	private void deleteSeller(String number) throws Exception {
		execute("CRUD.new");
		setValue("number", number);
		execute("CRUD.search");
		assertNoErrors();

		execute("CRUD.delete");	
		execute("ConfirmDelete.confirmDelete");											
		assertNoErrors();
		assertMessage("Object deleted successfully");
		assertExists("number"); // A bug did that the screen remained in blank after delete		
	}
	
	private void deleteCustomers() throws RemoteException, Exception {
		getCustomer1().remove();
		getCustomer2().remove();		
	}

	
	
	private CustomerValue getCustomerValue1() throws Exception {
		if (customerValue1 == null) {
			createCustomers();
		}
		return customerValue1;
	}
	
	private CustomerValue getCustomerValue2() throws Exception {
		if (customerValue2 == null) {
			createCustomers();
		}
		return customerValue2;
	}
	
	private Customer getCustomer1() throws Exception {
		if (customer1 == null) {
			createCustomers();
		}
		return customer1;
	}
		
	private Customer getCustomer2() throws Exception {
		if (customer2 == null) {
			createCustomers();
		}
		return customer2;
	}

	private void createCustomers() throws Exception {
		customerValue1 = new CustomerValue();
		customerValue1.setNumber(66);
		customerValue1.setName("CUSTOMER JUNIT 66");
		customerValue1.setRemarks("REMARKS JUNIT 66");
		customerValue1.setRelationWithSeller("RELATION JUNIT 66");		
		customer1= CustomerUtil.getHome().create(customerValue1);

		customerValue2 = new CustomerValue();
		customerValue2.setNumber(67);
		customerValue2.setName("CUSTOMER JUNIT 67");
		customerValue2.setRemarks("REMARKS JUNIT 67");
		customerValue2.setRelationWithSeller("RELATION JUNIT 67");		
		customer2= CustomerUtil.getHome().create(customerValue2);		
	}
	
	private String getCustomerNumber1() throws Exception {
		return String.valueOf(getCustomerValue1().getNumber());
	}
	
	private String getCustomerNumber2() throws Exception {
		return String.valueOf(getCustomerValue2().getNumber());
	}
			
}
