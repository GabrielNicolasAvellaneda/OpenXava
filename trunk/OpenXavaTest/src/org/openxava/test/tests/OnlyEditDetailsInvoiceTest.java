package org.openxava.test.tests;

import java.util.*;
import javax.rmi.*;

import org.openxava.test.model.*;
import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class OnlyEditDetailsInvoiceTest extends ModuleTestBase {
	
	public OnlyEditDetailsInvoiceTest(String testName) {
		super(testName, "OpenXavaTest", "OnlyEditDetailsInvoice");		
	}
	
	public void testAggregatesCollectionEditOnly() throws Exception {
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

		String [] aggregateListActions = {
			"Navigation.previous",
			"Navigation.first",
			"Navigation.next",
			"CRUD.new",
			"CRUD.save",
			"CRUD.delete",
			"CRUD.search",						
			"Mode.list",
			"Collection.edit"			
		};		
		assertActions(aggregateListActions);
		
		execute("Collection.edit", "row=0,viewObject=xava_view_details");
		
		String [] aggregateDetailActions = {
			"Navigation.previous",
			"Navigation.first",
			"Navigation.next",
			"CRUD.new",
			"CRUD.save",
			"CRUD.delete",
			"CRUD.search",			
			"Mode.list",			
			"Reference.search",
			"Reference.createNew",
			"Collection.edit",
			"Collection.save",
			"Collection.hiddenDetail"
		};		
		assertActions(aggregateDetailActions);
		
		assertEditable("details.serviceType");						
	}

	private InvoiceKey getInvoiceKey() throws Exception {
		Iterator it = InvoiceUtil.getHome().findAll().iterator();
		while (it.hasNext()) {			
			InvoiceRemote invoice = (InvoiceRemote) PortableRemoteObject.narrow(it.next(), InvoiceRemote.class);
			if (invoice.getDetailsCount() > 0) {
				return (InvoiceKey) invoice.getPrimaryKey(); 
			}			
		}
		fail("It must to exist at least one invoice with details to run this test");
		return null;
	}
	
	
								
}
