package org.openxava.test.tests;

import java.util.*;

import javax.persistence.*;

import org.openxava.jpa.*;
import org.openxava.test.model.*;
import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class InvoicesFromCustomersTest extends ModuleTestBase {
	
		
	private Collection invoices;

	public InvoicesFromCustomersTest(String testName) {
		super(testName, "InvoicesFromCustomers");		
	}
	
	public void testSelectedFromCalledModuleNotPropagateToCallerModule() throws Exception {
		assertRowUnchecked(0);
		execute("Invoices.listOfCustomer", "row=0");
		assertListNotEmpty();
		checkRow(0);
		assertRowChecked(0);
		execute("CustomerInvoices.return");
		assertRowUnchecked(0);
	}
	
	public void testListActionsIncludedWhenModeControllerUsed() throws Exception {
		executeDefaultAction();
		assertNoPopup();		
	}
	
	public void testModuleChange() throws Exception {
		// Verifying in initial module
		assertAction("Invoices.listOfCustomer");
		assertNoAction("CustomerInvoices.return");
		assertValueInList(0, 0, "Javi");		
				
		// Go to another module
		execute("Invoices.listOfCustomer", "row=0");
		assertNoAction("Invoices.listOfCustomer");
		assertAction("CustomerInvoices.return");				
		assertInvoices();
		execute("Mode.detailAndFirst");
		assertNoErrors();
		execute("Mode.list");
		assertInvoices();
				
		// Return to initial module
		execute("CustomerInvoices.return");
		assertAction("Invoices.listOfCustomer");
		assertNoAction("CustomerInvoices.return");
		assertValueInList(0, 0, "Javi");		
				
		// Go again to the other module
		execute("Invoices.listOfCustomer", "row=0");
		assertNoAction("Invoices.listOfCustomer");
		assertAction("CustomerInvoices.return");
		assertInvoices();
		execute("Mode.detailAndFirst"); // to detail
		
		// Return to initial module
		execute("CustomerInvoices.return");
		assertAction("Invoices.listOfCustomer");
		assertNoAction("CustomerInvoices.return");
		assertValueInList(0, 0, "Javi");
		
		// Go again to the other module, now we should to entry in list, not in detail
		execute("Invoices.listOfCustomer", "row=0");
		assertNoAction("Invoices.listOfCustomer");
		assertAction("CustomerInvoices.return");
		assertInvoices();		
	}
	
	private void assertInvoices() throws Exception {
		assertListRowCount(getInvoices().size());		
		Iterator it = getInvoices().iterator();
		while (it.hasNext()) {
			Invoice invoice = (Invoice) it.next();
			assertInvoiceInList(invoice);			
		}
	}

	private void assertInvoiceInList(Invoice invoice) throws Exception {
		int rowCount = getListRowCount();
		String year = String.valueOf(invoice.getYear());
		String number = String.valueOf(invoice.getNumber());
		for (int i = 0; i < rowCount; i++) {
			if (year.equals(getValueInList(i, 0)) && number.equals(getValueInList(i, 1))) return;
		}	
		fail("Invoice " + year + "/" + number + " debería estar presente en la lista");
	}

	private Collection getInvoices() throws Exception {
		if (invoices == null) {
			Query query = XPersistence.getManager().createQuery("select i from Invoice as i where i.customer.number=1" );	
			invoices = query.getResultList();    // Javi   
		} 
		return invoices;		
	}
		
}
