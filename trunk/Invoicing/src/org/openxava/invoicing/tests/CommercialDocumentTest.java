package org.openxava.invoicing.tests;

import java.text.*;
import java.util.*;
import javax.persistence.*;
import org.openxava.tests.*;
import org.openxava.util.*;

import static org.openxava.jpa.XPersistence.*;

abstract public class CommercialDocumentTest extends ModuleTestBase { 
	
	private String number; 
	private String model;

	public CommercialDocumentTest(String testName, String moduleName) { 
		super(testName, "Invoicing", moduleName);
		this.model = moduleName;
	}
			
	public void testCreate() throws Exception {
		calculateNumber(); 
		verifyDefaultValues();
		chooseCustomer();		
		addDetails();		
		setOtherProperties();		
		save(); 
		verifyAmountAndEstimatedProfit(); 
		verifyCreated();				
		remove(); 
	}

	private void verifyAmountAndEstimatedProfit() throws Exception { 
		execute("Mode.list");
		setConditionValues(new String [] { getCurrentYear(), getNumber() } );
		execute("List.filter");
		assertValueInList(0, 0, getCurrentYear());
		assertValueInList(0, 1, getNumber());
		assertValueInList(0, "amount", "83.52");
		assertValueInList(0, "estimatedProfit", "8.35");
		execute("Mode.detailAndFirst");
	}

	private void remove() throws Exception {
		// tmp execute("CRUD.delete");
		execute("Invoicing.delete"); // tmp
		assertNoErrors();
	}

	private void verifyCreated() throws Exception {
		assertValue("year", getCurrentYear()); 
		assertValue("number", getNumber());
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
		assertValueInCollection("details", 1, "product.description", "Arco iris de lágrimas");
		assertValueInCollection("details", 1, "quantity", "1");		
		
		assertValue("remarks", "This is a JUNIT test");
	}

	private void save() throws Exception {
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
		execute("Collection.new", "viewObject=xava_view_section0_details"); 
		setValue("details.product.number", "1");
		assertValue("details.product.description", "Peopleware: Productive Projects and Teams");
		assertValue("details.pricePerUnit", "31.00"); 
		setValue("details.quantity", "2");
		assertValue("details.amount", "62.00"); 
		execute("Collection.save", "viewObject=xava_view_section0_details"); 
		assertNoErrors();
		assertCollectionRowCount("details", 1);
		
		// On saving first detail the document is saved,
		// then we verify that number is calculated
		assertValue("number", getNumber());
		
		// Verifying calculated properties of document
		assertValue("baseAmount", "62.00");
		assertValue("vat", "9.92");
		assertValue("totalAmount", "71.92"); 
		
		// Adding another detail		
		setValue("details.product.number", "2");
		assertValue("details.product.description", "Arco iris de lágrimas");
		assertValue("details.pricePerUnit", "15.00"); 
		setValue("details.pricePerUnit", "10.00"); 
		setValue("details.quantity", "1");
		assertValue("details.amount", "10.00"); 
		execute("Collection.save", "viewObject=xava_view_section0_details"); 
		assertNoErrors();
		assertCollectionRowCount("details", 2);
		
		// Verifying calculated properties of document
		assertValue("baseAmount", "72.00");
		assertValue("vat", "11.52");
		assertValue("totalAmount", "83.52"); 
	}

	private void chooseCustomer() throws Exception {
		setValue("customer.number", "1");
		assertValue("customer.name", "FRANCISCO JAVIER PANIZA LUCAS");
	}

	private void verifyDefaultValues() throws Exception {
		execute("CRUD.new");
		assertValue("year", getCurrentYear());		
		assertValue("number", ""); 
		assertValue("date", getCurrentDate());
		assertValue("vatPercentage", "16");
	}	
	
	private String getCurrentYear() {
		return new SimpleDateFormat("yyyy").format(new Date());
	}
	
	private String getCurrentDate() {
		return DateFormat.getDateInstance(DateFormat.SHORT).format(new Date());
	}
				
	private void calculateNumber() {		
		Query query = getManager().
			createQuery(
				"select max(i.number) from " + model + 
				" i where i.year = :year"); 
		query.setParameter("year", Dates.getYear(new Date()));
		Integer lastNumber = (Integer) query.getSingleResult();
		if (lastNumber == null) lastNumber = 0;
		number = Integer.toString(lastNumber + 1);
	}
	
	private String getNumber() {
		return number;
	}
		
}
