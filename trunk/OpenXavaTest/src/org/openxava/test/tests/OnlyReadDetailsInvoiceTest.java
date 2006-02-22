package org.openxava.test.tests;

import java.util.*;
import javax.rmi.*;

import org.openxava.hibernate.*;
import org.openxava.test.model.*;
import org.openxava.tests.*;
import org.openxava.util.*;


/**
 * @author Javier Paniza
 */

public class OnlyReadDetailsInvoiceTest extends ModuleTestBase {
	
	public OnlyReadDetailsInvoiceTest(String testName) {
		super(testName, "OpenXavaTest", "OnlyReadDetailsInvoice");		
	}
	
	public void testAggregatesCollectionReadOnly() throws Exception {
		execute("CRUD.new");
		String [] initActions = {
			"Navigation.previous",
			"Navigation.first",
			"Navigation.next",
			"CRUD.new",
			"CRUD.save",
			"CRUD.delete",
			"CRUD.search",			
			"Mode.list"						
		};		
		assertActions(initActions);
		
		IInvoice invoice = getInvoice();	
		setValue("year", String.valueOf(invoice.getYear()));
		setValue("number", String.valueOf(invoice.getNumber()));
		execute("CRUD.search");
		assertNoErrors();

		String [] aggregatListActions = {
			"Navigation.previous",
			"Navigation.first",
			"Navigation.next",
			"CRUD.new",
			"CRUD.save",
			"CRUD.delete",
			"CRUD.search",						
			"Mode.list",
			"Collection.view"			
		};		
		assertActions(aggregatListActions);
		
		execute("Collection.view", "row=0,viewObject=xava_view_details");
		
		String [] aggregateDetailActions = {
			"Navigation.previous",
			"Navigation.first",
			"Navigation.next",
			"CRUD.new",
			"CRUD.save",
			"CRUD.delete",
			"CRUD.search",			
			"Mode.list",						
			"Collection.view",
			"Collection.hiddenDetail"
		};		
		assertActions(aggregateDetailActions);
		
		assertNoEditable("details.serviceType");		
		assertNoEditable("details.product.number");
		assertNoEditable("details.product.description");						
	}
	
	public void testLevel4ReferenceInList() throws Exception {
		// Look for a invoice with seller level
		int c = getListRowCount();
		int row = -1;
		for (int i=0; i<c; i++) {
			String sellerLevel = getValueInList(i, "customer.seller.level.description");			
			if (!Is.emptyString(sellerLevel)) {
				row = i;
				break;
			}
		}
		assertTrue("It's required a invoice with seller level for run this test", row >= 0);
		String year = getValueInList(row, "year");
		String number = getValueInList(row, "number");
		Invoice key = new Invoice();
		key.setYear(Integer.parseInt(year));
		key.setNumber(Integer.parseInt(number));
		IInvoice invoice = (IInvoice) XHibernate.getSession().get(Invoice.class, key);		
		assertValueInList(row, "customer.seller.level.description", invoice.getCustomer().getSeller().getLevel().getDescription());		
	}

	private Invoice getInvoice() throws Exception {
		Iterator it = Invoice.findAll().iterator();
		while (it.hasNext()) {			
			Invoice invoice = (Invoice) it.next();
			if (invoice.getDetailsCount() > 0) {
				return invoice; 
			}			
		}
		fail("It must to exist at least one invoice with details for run this test");
		return null;
	}
	
	
								
}
