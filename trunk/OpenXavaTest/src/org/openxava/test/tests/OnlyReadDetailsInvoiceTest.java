package org.openxava.test.tests;

import java.util.*;
import javax.rmi.*;

import org.openxava.test.ejb.*;
import org.openxava.tests.*;
import org.openxava.test.ejb.*;


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
		
		InvoiceKey key = getInvoiceKey();	
		setValue("year", String.valueOf(key.getYear()));
		setValue("number", String.valueOf(key.getNumber()));
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

	private InvoiceKey getInvoiceKey() throws Exception {
		Iterator it = InvoiceUtil.getHome().findAll().iterator();
		while (it.hasNext()) {			
			Invoice invoice = (Invoice) PortableRemoteObject.narrow(it.next(), Invoice.class);
			if (invoice.getDetailsCount() > 0) {
				return (InvoiceKey) invoice.getPrimaryKey(); 
			}			
		}
		fail("It must to exist at least one invoice with details for run this test");
		return null;
	}
	
	
								
}
