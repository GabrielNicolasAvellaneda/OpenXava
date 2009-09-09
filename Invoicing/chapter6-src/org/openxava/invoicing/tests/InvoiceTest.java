package org.openxava.invoicing.tests;

import java.text.*;
import java.util.*;
import javax.persistence.*;
import org.openxava.tests.*;
import org.openxava.util.*;

import static org.openxava.jpa.XPersistence.*;

public class InvoiceTest extends ModuleTestBase {
	
	private String invoiceNumber; // tmp invoiceNumber --> number

	public InvoiceTest(String testName) {
		super(testName, "Invoicing", "Invoice");				
	}
		
	public void testCreateInvoice() throws Exception { // tmp createInvoice --> create	 
		verifyDefaultValues();
		chooseCustomer();		
		addDetails();		
		setOtherProperties();		
		saveInvoice(); // tmp saveInvoice() --> save()
		verifyCreatedInvoice();	//tmp verifyCreatedInvoice() --> verifyCreated()			
		removeInvoice(); // tmp removeInvoice() --> remove()
	}

	private void removeInvoice() throws Exception {
		execute("CRUD.delete");
		assertNoErrors();
	}

	private void verifyCreatedInvoice() throws Exception {
		setValue("year", getCurrentYear());
		setValue("number", getInvoiceNumber());
		execute("CRUD.search");
		
		assertValue("year", getCurrentYear()); 
		assertValue("number", getInvoiceNumber());
		assertValue("date", getCurrentDate());
		
		assertValue("customer.number", "1");
		assertValue("customer.name", "FRANCISCO JAVIER PANIZA LUCAS");
		
		assertCollectionRowCount("details", 2);
		
		// Row 0
		assertValueInCollection("details", 0, "product.number", "1");
		assertValueInCollection("details", 0, "product.description", "Peopleware: Productive Projects and Teams");
		assertValueInCollection("details", 0, "quantity", "2");
		
		// Row 1
		assertValueInCollection("details", 1, "product.number", "2");
		assertValueInCollection("details", 1, "product.description", "Arco iris de l�grimas");
		assertValueInCollection("details", 1, "quantity", "1");		
		
		assertValue("remarks", "This is a JUNIT test");
	}

	private void saveInvoice() throws Exception {
		execute("CRUD.save");
		assertNoErrors();
		
		assertValue("customer.number", "");
		assertCollectionRowCount("details", 0);
		assertValue("remarks", "");
	}

	private void setOtherProperties() throws Exception {
		setValue("remarks", "This is a JUNIT test");
	}

	private void addDetails() throws Exception {
		// Adding a detail line
		assertCollectionRowCount("details", 0);
		execute("Collection.new", "viewObject=xava_view_details");
		setValue("details.product.number", "1");
		assertValue("details.product.description", "Peopleware: Productive Projects and Teams");
		setValue("details.quantity", "2");
		execute("Collection.save", "viewObject=xava_view_details");
		assertNoErrors();
		assertCollectionRowCount("details", 1);
		
		// Adding another detail		
		setValue("details.product.number", "2");
		assertValue("details.product.description", "Arco iris de l�grimas");
		setValue("details.quantity", "1");
		execute("Collection.save", "viewObject=xava_view_details");
		assertNoErrors();
		assertCollectionRowCount("details", 2);
	}

	private void chooseCustomer() throws Exception {
		setValue("customer.number", "1");
		assertValue("customer.name", "FRANCISCO JAVIER PANIZA LUCAS");
	}

	private void verifyDefaultValues() throws Exception {
		execute("CRUD.new");
		assertValue("year", getCurrentYear());		
		assertValue("number", getInvoiceNumber());
		assertValue("date", getCurrentDate());
	}	
	
	private String getCurrentYear() {
		return new SimpleDateFormat("yyyy").format(new Date());
	}
	
	private String getCurrentDate() {
		return DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());
	}
		
	private String getInvoiceNumber() {
		if (invoiceNumber == null) {
			Query query = getManager().
				createQuery(
					"select max(i.number) from Invoice i where i.year = :year");
			query.setParameter("year", Dates.getYear(new Date()));
			Integer lastNumber = (Integer) query.getSingleResult();
			if (lastNumber == null) lastNumber = 0;
			invoiceNumber = Integer.toString(lastNumber + 1);
		}
		return invoiceNumber;
	}
		
}