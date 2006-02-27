package org.openxava.test.tests;

import java.rmi.*;

import org.openxava.hibernate.*;
import org.openxava.test.model.*;
import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class SellersTest extends ModuleTestBase {
	
	private Customer customer2;
	private Customer customer1;

	public SellersTest(String testName) {
		super(testName, "OpenXavaTest", "Sellers");		
	}
	
	public void testCustomEditorWithMultipleValuesFormatter() throws Exception {
		String [] emptyRegions = {};
		String [] regions = { "1", "3" };
		String [] oneRegion = { "2" };
		
		execute("CRUD.new");
		assertValues("regions", emptyRegions);
		setValue("number", "66");
		setValue("name", "SELLER JUNIT 66");
		setValue("level.id", "A");
		setValues("regions", regions);
		assertValues("regions", regions);
		
		execute("CRUD.save");
		assertNoErrors();
		assertValues("regions", emptyRegions);		
		
		setValue("number", "66");
		execute("CRUD.search");
		assertValues("regions", regions);
		
		setValues("regions", oneRegion);
		execute("CRUD.save");
		assertNoErrors();
		assertValues("regions", emptyRegions);

		setValue("number", "66");
		execute("CRUD.search");
		assertValues("regions", oneRegion);
		
		execute("CRUD.delete");
		assertMessage("Seller deleted successfully");
	}
	
	public void testCollectionOfEntityReferencesElementsNotEditables() throws Exception {
		execute("Mode.detailAndFirst");
		execute("Collection.edit", "row=0,viewObject=xava_view_customers");
		assertEditable("customers.number");
		assertNoEditable("customers.name");
		assertNoAction("Collection.new"); // of deliveryPlaces
	}
	
	public void testCustomizeListSupportsRecursiveReferences() throws Exception {
		execute("List.customize");
		execute("List.addColumns");
		assertAction("AddColumns.addColumns");
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
	
	public void testSearchElementInReferencesCollectionUsingList() throws Exception {
		execute("CRUD.new");
		execute("Collection.new", "viewObject=xava_view_customers");
		execute("Reference.search", "keyProperty=xava.Seller.customers.number"); 
		String name = getValueInList(1, 0);
		execute("ReferenceSearch.choose", "row=1");
		assertValue("customers.name", name);
	}
	
	public void testCreateElementInReferencesCollectionUsingList() throws Exception {
		execute("CRUD.new");
		execute("Collection.new", "viewObject=xava_view_customers");
		execute("Reference.createNew", "model=Customer,keyProperty=xava.Seller.customers.number");
		assertAction("NewCreation.saveNew");
		assertAction("NewCreation.cancel");
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
		assertValueIgnoringCase("customers.name", getCustomer1().getName()); 
		assertCollectionRowCount("customers", 0);
		execute("Collection.save", "viewObject=xava_view_customers");
		assertCollectionRowCount("customers", 1);
		
		execute("Collection.new", "viewObject=xava_view_customers");
		assertEditable("customers.number");
		assertNoEditable("customers.name");
		setValue("customers.number", getCustomerNumber2());		
		assertValueIgnoringCase("customers.name", getCustomer2().getName());
		execute("Collection.save", "viewObject=xava_view_customers");
		assertCollectionRowCount("customers",2);
		
		assertValueInCollection("customers", 0, 0, getCustomerNumber1());
		assertValueInCollectionIgnoringCase("customers", 0, 1, getCustomer1().getName());
		assertValueInCollection("customers", 0, 2, getCustomer1().getRemarks());
		assertValueInCollection("customers", 0, 3, getCustomer1().getRelationWithSeller());
		assertValueInCollection("customers", 0, 4, getCustomer1().getSeller().getLevel().getDescription());
		
		assertValueInCollection("customers", 1, 0, getCustomerNumber2());
		assertValueInCollectionIgnoringCase("customers", 1, 1, getCustomer2().getName());
		assertValueInCollection("customers", 1, 2, getCustomer2().getRemarks());
		assertValueInCollection("customers", 1, 3, getCustomer2().getRelationWithSeller());
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
		assertValueIgnoringCase("customers.name", getCustomer2().getName());
		assertCollectionRowCount("customers",0);
		execute("Collection.save", "viewObject=xava_view_customers");
		assertCollectionRowCount("customers",1);
		
		assertValueInCollection("customers", 0, 0, getCustomerNumber2());
		assertValueInCollectionIgnoringCase("customers", 0, 1, getCustomer2().getName());
		assertValueInCollection("customers", 0, 2, getCustomer2().getRemarks());
		assertValueInCollection("customers", 0, 3, getCustomer2().getRelationWithSeller());
		assertValueInCollection("customers", 0, 4, getCustomer2().getSeller().getLevel().getDescription());
	}
	
	private void verifySeller66() throws Exception {
		execute("CRUD.new");
		setValue("number", "66");
		execute("CRUD.search");
		assertNoErrors();
		assertCollectionRowCount("customers", 1);
		assertValueInCollection("customers", 0, 0, getCustomerNumber1());
		assertValueInCollectionIgnoringCase("customers", 0, 1, getCustomer1().getName());
		assertValueInCollection("customers", 0, 2, getCustomer1().getRemarks());
		assertValueInCollection("customers", 0, 3, getCustomer1().getRelationWithSeller());
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
		assertNoErrors();
		assertMessage("Seller deleted successfully");
		assertExists("number"); // A bug did that the screen remained in blank after delete		
	}
	
	private void deleteCustomers() throws RemoteException, Exception {
		XHibernate.getSession().delete(getCustomer1());
		XHibernate.getSession().delete(getCustomer2());
		XHibernate.commit();
	}

	
	
	private ICustomer getCustomer1() throws Exception {
		if (customer1 == null) {
			createCustomers();
		}
		return customer1;
	}
	
	private ICustomer getCustomer2() throws Exception {
		if (customer2 == null) {
			createCustomers();
		}
		return customer2;
	}
	
		
	private void createCustomers() throws Exception {
		customer1 = new Customer();
		customer1.setNumber(66);
		customer1.setName("CUSTOMER JUNIT 66");
		customer1.setRemarks("REMARKS JUNIT 66");
		customer1.setRelationWithSeller("RELATION JUNIT 66");
		XHibernate.getSession().save(customer1);		

		customer2 = new Customer();
		customer2.setNumber(67);
		customer2.setName("CUSTOMER JUNIT 67");
		customer2.setRemarks("REMARKS JUNIT 67");
		customer2.setRelationWithSeller("RELATION JUNIT 67");
		XHibernate.getSession().save(customer2);				
	}
	
	private String getCustomerNumber1() throws Exception {
		return String.valueOf(getCustomer1().getNumber());
	}
	
	private String getCustomerNumber2() throws Exception {
		return String.valueOf(getCustomer2().getNumber());
	}
			
}
