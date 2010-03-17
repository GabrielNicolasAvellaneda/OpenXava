package org.openxava.invoicing.tests;

import javax.persistence.*;

import org.openxava.invoicing.model.*;
import org.openxava.jpa.*;
import org.openxava.util.*;

import sun.reflect.ReflectionFactory.*;

public class OrderTest extends CommercialDocumentTest {
	
	public OrderTest(String testName) { 
		super(testName, "Order"); 				
	}
	
	public void testCreateInvoiceFromOrder() throws Exception {
		// Looking for the order
		searchOrderSusceptibleToBeInvoiced();
		int orderDetailCount = getCollectionRowCount("details");		
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
		assertCollectionRowCount("invoice.details", orderDetailCount);		
		
		// Restoring the order for running the test the next time
		setValue("invoice.year", "");
		assertValue("invoice.number", "");
		assertCollectionRowCount("invoice.details", 0);
		execute("CRUD.save");
		assertNoErrors();
	}
	
	private void searchOrderSusceptibleToBeInvoiced() throws Exception {
		Order order = getOrderSusceptibleToBeInvoiced();
		String year = String.valueOf(order.getYear());
		String number = String.valueOf(order.getNumber());
		setConditionValues(new String [] { year, number });
		execute("List.filter");
		assertListRowCount(1);		
		execute("Mode.detailAndFirst");
		assertValue("year", year);
		assertValue("number", number);
		assertValue("delivered", "true");				
	}
	
	private Order getOrderSusceptibleToBeInvoiced() {
		Query query = XPersistence.getManager().createQuery(
			"from Order o where " +
			"o.delivered = true and o.invoice = null");
		return (Order) query.getResultList().get(0);
	}
	
	public void testSetInvoice() throws Exception {
		assertListNotEmpty();
		execute("List.orderBy", "property=number"); 
		execute("Mode.detailAndFirst");
		assertValue("delivered", "false"); 
		execute("Sections.change", "activeSection=1");
		assertValue("invoice.number", "");
		assertValue("invoice.year", "");
		execute("Reference.search", 
			"keyProperty=invoice.year");
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
		execute("CRUD.save");
		assertNoErrors();
	}
			
}
