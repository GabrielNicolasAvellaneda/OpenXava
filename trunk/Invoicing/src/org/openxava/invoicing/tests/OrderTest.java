package org.openxava.invoicing.tests;

import java.math.*;
import java.util.*;

import javax.persistence.*;

import org.openxava.invoicing.model.*;
import org.openxava.jpa.*;
import org.openxava.util.*;


public class OrderTest extends CommercialDocumentTest {
	
	public OrderTest(String testName) { 
		super(testName, "Order"); 				
	}
	
	public void testCreateInvoiceFromSelectedOrders() throws Exception { 
		assertOrder(2010,  9, 2, 362);
		assertOrder(2010, 10, 1, 126);
				
		execute("List.orderBy", "property=number");
		checkRow(
			getDocumentRowInList("2010",  "9") 
		);
		checkRow(
			getDocumentRowInList("2010", "10") 
		);
		
		execute("Order.createInvoiceFromSelectedOrders");
		
		String invoiceYear = getValue("year");
		String invoiceNumber = getValue("number");
		assertMessage("Invoice " + invoiceYear + "/" + invoiceNumber + 
			" created from orders: [2010/9, 2010/10]");
		assertCollectionRowCount("details", 3);
		assertValue("baseAmount", "488.00");
		execute("Sections.change", "activeSection=1");
		assertCollectionRowCount("orders", 2);
		assertValueInCollection("orders", 0, 0, "2010");
		assertValueInCollection("orders", 0, 1, "9");
		assertValueInCollection("orders", 1, 0, "2010");
		assertValueInCollection("orders", 1, 1, "10");
		
		assertAction("CurrentInvoiceEdition.save");		
		assertAction("CurrentInvoiceEdition.return");
		
		checkRowCollection("orders", 0);
		checkRowCollection("orders", 1);
		execute("Collection.removeSelected", "viewObject=xava_view_section1_orders");
		assertNoErrors();
		
		execute("CurrentInvoiceEdition.return");
		assertDocumentInList("2010",  "9");
		assertDocumentInList("2010", "10");
	}
	
	private void assertOrder(int year, int number, int detailsCount, int baseAmount) {
		Order order = findOrder("year = " + year + " and number=" + number);
		assertEquals("To run this test the order " + 
			order + " must have " +	detailsCount + " details", 
			detailsCount, order.getDetails().size());
		assertTrue("To run this test the order " + 
			order + " must have " +	baseAmount + " as base amount", 
			order.getBaseAmount().compareTo(new BigDecimal(baseAmount)) == 0);
	}

	public void testCreateInvoiceFromOrder() throws Exception {
		// Looking for the order
		searchOrderSusceptibleToBeInvoiced();
		assertValue("delivered", "true"); 
		int orderDetailsCount = getCollectionRowCount("details");		
		execute("Sections.change", "activeSection=1");
		assertValue("invoice.year", "");
		assertValue("invoice.number", "");
				
		// Creating the invoice
		execute("Order.createInvoice");
		String invoiceYear = getValue("invoice.year");
		assertTrue("Invoice year must have value", 
			!Is.emptyString(invoiceYear));
		String invoiceNumber = getValue("invoice.number");
		assertTrue("Invoice number must have value", 
			!Is.emptyString(invoiceNumber));		
		assertMessage("Invoice " + invoiceYear + "/" + invoiceNumber + 
			" created from current order"); 
		assertCollectionRowCount("invoice.details", orderDetailsCount);		
		
		// Restoring the order for running the test the next time
		setValue("invoice.year", "");
		setValue("invoice.number", ""); 
		assertValue("invoice.number", "");
		assertCollectionRowCount("invoice.details", 0);
		execute("CRUD.save");
		assertNoErrors();
	}
	
	public void testHidesCreateInvoiceFromOrderWhenNotApplicable() throws Exception { 		
		searchOrderUsingList("delivered = true and invoice <> null"); 
		assertNoAction("Order.createInvoice");		
		
		execute("Mode.list");
		
		searchOrderUsingList("delivered = false and invoice = null"); 
		assertNoAction("Order.createInvoice");		
				
		execute("CRUD.new");
		assertNoAction("Order.createInvoice");		
	}
	
	public void testCreateInvoiceFromOrderExceptions() throws Exception { 
		assertCreateInvoiceFromOrderException(
			"delivered = true and invoice <> null",
			"Impossible to create invoice: the order already has an invoice"
		);
		
		assertCreateInvoiceFromOrderException(
			"delivered = false and invoice = null",
			"Impossible to create invoice: the order is not delivered yet"
		);	
	}
	
	private void assertCreateInvoiceFromOrderException(String condition, String message) throws Exception { 
		Order order = findOrder(condition);
		int row = getDocumentRowInList(
			String.valueOf(order.getYear()), 
			String.valueOf(order.getNumber())
		); 
		checkRow(row);			
		execute("Order.createInvoiceFromSelectedOrders");
		assertError(message);
		uncheckRow(row);
	}
	
	private void searchOrderSusceptibleToBeInvoiced() throws Exception {
		searchOrderUsingList("delivered = true and invoice = null");		
	}
	
	private void searchOrderUsingList(String condition) throws Exception {		
		Order order = findOrder(condition);
		String year = String.valueOf(order.getYear());
		String number = String.valueOf(order.getNumber());
		setConditionValues(new String [] { year, number });
		execute("List.filter");
		assertListRowCount(1);		
		execute("Mode.detailAndFirst");
		assertValue("year", year);
		assertValue("number", number);						
	}
	
	private Order findOrder(String condition) {
		Query query = XPersistence.getManager().createQuery(
			"from Order o where o.deleted = false and " + condition);
		List orders = query.getResultList();
		if (orders.isEmpty()) {
			fail("To run this test you must have some order with " + condition);
		}
		return (Order) orders.get(0);
	}
	
	public void testOnChangeInvoice() throws Exception { 
		execute("CRUD.new");
		assertValue("customer.number", "");
		execute("Sections.change", "activeSection=1");		
		execute("Order.searchInvoice", 
			"keyProperty=invoice.number");
		
		execute("List.orderBy", "property=customer.number");		
		String customer1Number = getValueInList(0, "customer.number");		
		String invoiceYear1 = getValueInList(0, "year");
		String invoiceNumber1 = getValueInList(0, "number");
		execute("List.orderBy", "property=customer.number");		
		String customer2Number = getValueInList(0, "customer.number");
		String customer2Name = getValueInList(0, "customer.name");
		assertNotEquals("Must be invoices of different customer",
			customer1Number, customer2Number);
		
		execute("ReferenceSearch.choose", "row=0");
		execute("Sections.change", "activeSection=0");
		assertValue("customer.number", customer2Number);
		assertValue("customer.name", customer2Name);
		
		execute("Sections.change", "activeSection=1");
		setValue("invoice.year", invoiceYear1);
		setValue("invoice.number", invoiceNumber1);
				
		assertError("Customer Nº " + customer1Number + " of invoice " +
			invoiceYear1 + "/" + invoiceNumber1 + 
			" does not match with Customer Nº " +
			customer2Number + " of the current order");
		
		assertValue("invoice.year", "");
		assertValue("invoice.number", "");
		assertValue("invoice.date", "");		
	}
	
	public void testSearchInvoiceFromOrder() throws Exception { 
		execute("CRUD.new");
		setValue("customer.number", "1");
		execute("Sections.change", "activeSection=1");
		execute("Order.searchInvoice", 
			"keyProperty=invoice.number");
		assertCustomerInList("1");
		execute("ReferenceSearch.cancel");
		execute("Sections.change", "activeSection=0");
		setValue("customer.number", "2");
		execute("Sections.change", "activeSection=1");
		execute("Order.searchInvoice", 
			"keyProperty=invoice.number");
		assertCustomerInList("2");		
	}
	
	/* tmp
	private void assertCustomerInList(String customerNumber) throws Exception { 
		assertListNotEmpty();
		int c = getListRowCount();
		for (int i=0; i<c; i++) {
			if (!customerNumber.equals(getValueInList(i, "customer.number"))) {
				fail("Customer in row " + i + " is not of customer " + customerNumber); 
			}
		}		
	}
	*/

	public void testSetInvoice() throws Exception {
		assertListNotEmpty();
		execute("List.orderBy", "property=number"); 
		execute("Mode.detailAndFirst");
		assertValue("delivered", "false"); 
		execute("Sections.change", "activeSection=1");
		assertValue("invoice.number", "");
		assertValue("invoice.year", "");
		execute("Order.searchInvoice", 
			"keyProperty=invoice.number"); 		
		execute("List.orderBy", "property=number"); 
		String year = getValueInList(0, "year");
		String number = getValueInList(0, "number");		
		execute("ReferenceSearch.choose", "row=0");
		assertValue("invoice.year", year);
		assertValue("invoice.number", number);

		// Not delivered order cannot have invoice 
		execute("CRUD.save"); 
		assertErrorsCount(1); 
		setValue("delivered", "true");
		execute("CRUD.save");
		assertNoErrors();
		
		// Order with invoice cannot be deleted
		execute("Mode.list");
		execute("Mode.detailAndFirst");		
		execute("Invoicing.delete"); 
		assertErrorsCount(1);
		
		// Restore values
		setValue("delivered", "false");
		setValue("invoice.year", "");
		setValue("invoice.number", ""); 
		execute("CRUD.save");
		assertNoErrors();
	}
			
}
