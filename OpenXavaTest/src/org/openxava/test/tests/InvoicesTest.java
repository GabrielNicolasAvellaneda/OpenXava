package org.openxava.test.tests;

import java.math.*;
import java.rmi.*;
import java.text.*;
import java.util.*;

import javax.rmi.*;

import org.openxava.test.calculators.*;
import org.openxava.test.ejb.*;
import org.openxava.tests.*;
import org.openxava.util.*;


/**
 * @author Javier Paniza
 */

public class InvoicesTest extends ModuleTestBase {
	
	private InvoiceKey invoiceKey;
	private BigDecimal productUnitPriceDB;
	private String productUnitPricePlus10;
	private String productUnitPrice;
	private String sellerNumber;
	private Seller seller;
	private String productUnitPriceInPesetas;
	private String productDescription;
	private String productNumber;
	private Product product;

	public InvoicesTest(String testName) {
		super(testName, "OpenXavaTest", "Invoices");		
	}
	
	public void testTestingCheckBox() throws Exception {
		// Demo for make tests with checkbox
		
		// Create
		execute("CRUD.new");
		
		String year = getValue("year");		
		setValue("number", "66");
		
		setValue("customer.number", "1");
		
		// First vat percentage for no validation error on save first detail
		execute("Sections.change", "activeSection=2");
		setValue("vatPercentage", "23");
		
		setValue("paid", "true"); // assign true to checkbox
		
		execute("CRUD.save");
		assertNoErrors();
		assertValue("paid", "false"); // assert if checkbox is false
		
		// Consult
		setValue("year", year);
		setValue("number", "66");
		execute("CRUD.search");
		assertValue("paid", "true");
		
		// Changing the boolean value
		setValue("paid", "false"); // assign false to checkbox
		execute("CRUD.save");
		assertNoErrors();
		
		// Consult again
		setValue("year", year);
		setValue("number", "66");
		execute("CRUD.search");
		assertNoErrors();		
		assertValue("paid", "false"); // assert if checkbox is false
		
		// Delete
		execute("CRUD.delete");
		execute("ConfirmDelete.confirmDelete");
		assertMessage("Invoice deleted successfully");				
	}
	
	public void testCustomizeList_addColumns() throws Exception {
		assertListColumnCount(8);
		assertLabelInList(0, "Year");
		assertLabelInList(1, "Number");
		assertLabelInList(2, "Date");
		assertLabelInList(3, "Amounts sum");
		assertLabelInList(4, "V.A.T.");
		assertLabelInList(5, "Details count");
		assertLabelInList(6, "Paid");
		assertLabelInList(7, "Importance");
		
		execute("List.customize");
		execute("List.addColumns");
		
		assertCollectionRowCount("xavaPropertiesList", 20);
		assertValueInCollection("xavaPropertiesList",  0, 1, "vatPercentage");
		assertValueInCollection("xavaPropertiesList",  1, 1, "comment");
		assertValueInCollection("xavaPropertiesList",  2, 1, "customerDiscount");
		assertValueInCollection("xavaPropertiesList",  3, 1, "customerTypeDiscount");
		assertValueInCollection("xavaPropertiesList",  4, 1, "yearDiscount");
		assertValueInCollection("xavaPropertiesList",  5, 1, "customer.number");
		assertValueInCollection("xavaPropertiesList",  6, 1, "customer.name");
		assertValueInCollection("xavaPropertiesList",  7, 1, "customer.type");
		assertValueInCollection("xavaPropertiesList",  8, 1, "customer.photo");
		assertValueInCollection("xavaPropertiesList",  9, 1, "customer.remarks");
		assertValueInCollection("xavaPropertiesList", 10, 1, "customer.relationWithSeller");
		assertValueInCollection("xavaPropertiesList", 11, 1, "customer.seller.number");
		assertValueInCollection("xavaPropertiesList", 12, 1, "customer.seller.name");
		assertValueInCollection("xavaPropertiesList", 13, 1, "customer.seller.level.id");
		assertValueInCollection("xavaPropertiesList", 14, 1, "customer.seller.level.description");
		assertValueInCollection("xavaPropertiesList", 15, 1, "customer.address.street");
		assertValueInCollection("xavaPropertiesList", 16, 1, "customer.address.zipCode");
		assertValueInCollection("xavaPropertiesList", 17, 1, "customer.address.city");
		assertValueInCollection("xavaPropertiesList", 18, 1, "customer.address.state.id");
		assertValueInCollection("xavaPropertiesList", 19, 1, "customer.address.state.name");		
		
		checkRow("selectedProperties", "yearDiscount"); 
		checkRow("selectedProperties", "customer.address.city"); 
		
		execute("AddColumns.addColumns");
		
		assertListColumnCount(10);
		assertLabelInList(0, "Year");
		assertLabelInList(1, "Number");
		assertLabelInList(2, "Date");
		assertLabelInList(3, "Amounts sum");
		assertLabelInList(4, "V.A.T.");
		assertLabelInList(5, "Details count");
		assertLabelInList(6, "Paid");
		assertLabelInList(7, "Importance");
		assertLabelInList(8, "Year discount");
		assertLabelInList(9, "City");
		
		execute("List.addColumns");

		assertCollectionRowCount("xavaPropertiesList", 18);
		assertValueInCollection("xavaPropertiesList",  0, 1, "vatPercentage");
		assertValueInCollection("xavaPropertiesList",  1, 1, "comment");
		assertValueInCollection("xavaPropertiesList",  2, 1, "customerDiscount");
		assertValueInCollection("xavaPropertiesList",  3, 1, "customerTypeDiscount");		
		assertValueInCollection("xavaPropertiesList",  4, 1, "customer.number");
		assertValueInCollection("xavaPropertiesList",  5, 1, "customer.name");
		assertValueInCollection("xavaPropertiesList",  6, 1, "customer.type");
		assertValueInCollection("xavaPropertiesList",  7, 1, "customer.photo");
		assertValueInCollection("xavaPropertiesList",  8, 1, "customer.remarks");
		assertValueInCollection("xavaPropertiesList",  9, 1, "customer.relationWithSeller");
		assertValueInCollection("xavaPropertiesList", 10, 1, "customer.seller.number");
		assertValueInCollection("xavaPropertiesList", 11, 1, "customer.seller.name");
		assertValueInCollection("xavaPropertiesList", 12, 1, "customer.seller.level.id");
		assertValueInCollection("xavaPropertiesList", 13, 1, "customer.seller.level.description");		
		assertValueInCollection("xavaPropertiesList", 14, 1, "customer.address.street");
		assertValueInCollection("xavaPropertiesList", 15, 1, "customer.address.zipCode");
		assertValueInCollection("xavaPropertiesList", 16, 1, "customer.address.state.id");		
		assertValueInCollection("xavaPropertiesList", 17, 1, "customer.address.state.name");		
		
		execute("AddColumns.cancel");
		
		assertListColumnCount(10);
		assertLabelInList(0, "Year");
		assertLabelInList(1, "Number");
		assertLabelInList(2, "Date");
		assertLabelInList(3, "Amounts sum");
		assertLabelInList(4, "V.A.T.");
		assertLabelInList(5, "Details count");
		assertLabelInList(6, "Paid");
		assertLabelInList(7, "Importance");
		assertLabelInList(8, "Year discount");
		assertLabelInList(9, "City");		
	}
	
	public void testCustomizeList_storePreferences() throws Exception {
		// This test trusts that 'testCustomizeList_addColumns' is executed before
		assertListColumnCount(10);
		assertLabelInList(0, "Year");
		assertLabelInList(1, "Number");
		assertLabelInList(2, "Date");
		assertLabelInList(3, "Amounts sum");
		assertLabelInList(4, "V.A.T.");
		assertLabelInList(5, "Details count");
		assertLabelInList(6, "Paid");
		assertLabelInList(7, "Importance");
		assertLabelInList(8, "Year discount");
		assertLabelInList(9, "City");		
		
		// Restoring, for next time that test execute
		execute("List.customize");
		execute("List.removeColumn","columnIndex=9");
		execute("List.removeColumn","columnIndex=8");
		
		assertListColumnCount(8);
		assertLabelInList(0, "Year");
		assertLabelInList(1, "Number");
		assertLabelInList(2, "Date");
		assertLabelInList(3, "Amounts sum");
		assertLabelInList(4, "V.A.T.");
		assertLabelInList(5, "Details count");
		assertLabelInList(6, "Paid");
		assertLabelInList(7, "Importance");
	}
	
	
	public void testGenerateExcel() throws Exception {
		String year = getValueInList(0, 0);
		String number = getValueInList(0, 1);		
		DateFormat dfEs = new SimpleDateFormat("dd/MM/yyyy"); // at momment in spanish format
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		String date = df.format(dfEs.parse(getValueInList(0, 2)));
		String amountsSum = formatBigDecimal(getValueInList(0, 3));
		String vat = formatBigDecimal(getValueInList(0, 4));
		String detailsCount = getValueInList(0, 5);
		String paid = getValueInList(0, 6);
		String importance = Strings.firstUpper(getValueInList(0, 7).toLowerCase());
		String expectedLine = year + ";" + number + ";" + 
			date + ";" + amountsSum + ";" + 
			vat + ";" + detailsCount + ";" +
			paid + ";" + importance;
		
		execute("Print.generateExcel");
		assertContentTypeForPopup("text/x-csv");
		
		StringTokenizer excel = new StringTokenizer(getPopupText(), "\n\r");
		String header = excel.nextToken();
		assertEquals("header", "Year;Number;Date;Amounts sum;V.A.T.;Details count;Paid;Importance", header);		
		String line1 = excel.nextToken();
		assertEquals("line1", expectedLine, line1);
	}
	

	public void testFilterByDate() throws Exception {
		String date = getValueInList(0, "date");		
		String [] conditionValues = { " ", " ", date, "true" };
		setConditionValues(conditionValues);
		execute("List.filter");
		assertDateInList(date);
		
		String [] yearComparators = { "=", "=", "year_comparator", ""};
		setConditionComparators(yearComparators);
		
		String [] condition2002 = { " ", " ", "2002", "true" }; // We supussed that there are invoices in 2002
		setConditionValues(condition2002);
		execute("List.filter");
		assertYearInList("2002");

		String [] condition2004 = { " ", " ", "2004", "true" }; // We supussed that there are invoices in 2004
		setConditionValues(condition2004);
		execute("List.filter");
		assertYearInList("2004");		
	}
	
	public void testFilterByBoolean() throws Exception {
		int total = InvoiceUtil.getHome().findAll().size();
		int paidOnes = InvoiceUtil.getHome().findPaidOnes().size();
		int notPaidOnes = InvoiceUtil.getHome().findNotPaidOnes().size();
		assertTrue("It has to have invoices for run this test", total > 0);
		assertTrue("It has to have paid invoices for run this test", paidOnes > 0);
		assertTrue("It has to have not paid invoices for run this test", notPaidOnes > 0);
		assertTrue("The sum of paid and not paid invoices has to match with the total count", total == (paidOnes + notPaidOnes));
		assertTrue("It has to have less than 10 invoices to run this test", total < 10);
		assertListRowCount(total);
		
		String [] paidComparators = { "=", "=", "=", "="};
		String [] paidConditions = { "", "", "", "true"	};
		setConditionComparators(paidComparators);
		setConditionValues(paidConditions);
		execute("List.filter");
		assertListRowCount(paidOnes);
		
		String [] notPaidComparatos = { "=", "=", "=", "<>"};
		String [] notPaidConditions = { " ", " ", " ", "true" }; // For dark reasons it is necessary to leave a blank space so it runs.
		setConditionComparators(notPaidComparatos);
		setConditionValues(notPaidConditions);		
		execute("List.filter");
		assertNoErrors();
		assertListRowCount(notPaidOnes);		
		
		String [] totalComparators = { "=", "=", "=", ""};
		String [] totalCondition = { " ", " ", " ", "true" }; // Por razones oscuras hay que dejar un espacio en blanco para que funcione.
		setConditionComparators(totalComparators);
		setConditionValues(totalCondition);		
		execute("List.filter");
		assertNoErrors();
		assertListRowCount(total);				
	}
	
	public void testCreateFromReference() throws Exception {
		execute("CRUD.new");		
		execute("Reference.createNew", "model=Customer,keyProperty=xava.Invoice.customer.number");
		assertNoErrors();
		assertAction("NewCreation.saveNew");
		assertAction("NewCreation.cancel");	
		assertValue("Customer", "type", "1");
		execute("Reference.search", "keyProperty=xava.Customer.alternateSeller.number");
		assertNoErrors();
		execute("ReferenceSearch.cancel");
		assertAction("NewCreation.saveNew");
		assertAction("NewCreation.cancel");	
		assertValue("Customer", "type", "1");		
		execute("NewCreation.cancel");
		assertExists("year");
		assertExists("number");
	}
	
	public void testChangeTab() throws Exception {		
		assertListColumnCount(8);
		execute("Invoices.changeTab");
		assertNoErrors();
		assertListColumnCount(3);
	}
	
	public void testDateFormatter() throws Exception {		
		execute("CRUD.new");
		setValue("year", String.valueOf(getInvoiceKey().getYear()));
		setValue("number", String.valueOf(getInvoiceKey().getNumber()));
		execute("CRUD.search");
		assertNoErrors();
		String originalDate = getValue("date"); // For restore at end
		
		setValue("date", "1/1/2004");
		execute("CRUD.save");
		assertNoErrors();
		setValue("year", String.valueOf(getInvoiceKey().getYear()));
		setValue("number", String.valueOf(getInvoiceKey().getNumber()));
		execute("CRUD.search");
		assertNoErrors();
		assertValue("date", "01/01/2004");
		
		setValue("date", "02012004");
		execute("CRUD.save");
		assertNoErrors();
		setValue("year", String.valueOf(getInvoiceKey().getYear()));
		setValue("number", String.valueOf(getInvoiceKey().getNumber()));
		execute("CRUD.search");
		assertNoErrors();
		assertValue("date", "02/01/2004");
		
		setValue("date", "3.1.2004");
		execute("CRUD.save");
		assertNoErrors();
		setValue("year", String.valueOf(getInvoiceKey().getYear()));
		setValue("number", String.valueOf(getInvoiceKey().getNumber()));
		execute("CRUD.search");
		assertNoErrors();
		assertValue("date", "03/01/2004");
		
		setValue("date", "4-1-2004");
		execute("CRUD.save");
		assertNoErrors();
		setValue("year", String.valueOf(getInvoiceKey().getYear()));
		setValue("number", String.valueOf(getInvoiceKey().getNumber()));
		execute("CRUD.search");
		assertNoErrors();
		assertValue("date", "04/01/2004");
		
		// Restore original date
		setValue("date", originalDate);
		execute("CRUD.save");
		assertNoErrors();		 				
	}
	
	public void testReadOnlyCollectionWithPropertiesInDetailIncludedInListWithoutAction() throws Exception {
		deleteInvoiceDeliveries();
		Delivery delivery = createDelivery();
		
		execute("CRUD.new");
		setValue("year", String.valueOf(getInvoiceKey().getYear()));
		setValue("number", String.valueOf(getInvoiceKey().getNumber()));
		execute("CRUD.search");
		assertNoErrors();
		
		execute("Sections.change", "activeSection=3");
		assertCollectionRowCount("deliveries", 1);
	
		assertAction("Collection.view");
		// The next action display in detail only properties that already are in list
		execute("Invoices.removeViewDeliveryInInvoice");
		
		assertNoAction("Collection.view"); 
		
		execute("Invoices.addViewDeliveryInInvoice");
		assertAction("Collection.view");
	}
	
	public void testValidateExistsRequiredReference() throws Exception {
		execute("CRUD.new");
		String a�o = getValue("year");		
		setValue("number", "66");
		execute("Sections.change", "activeSection=2");
		setValue("vatPercentage", "24");		
		execute("CRUD.save");
		assertError("Value for Customer in Invoice is required");				
	}
	
	public void testNotEditableCustomerData() throws Exception {
		execute("CRUD.new");
		execute("Sections.change", "activeSection=0");
		assertEditable("customer.number");
		assertNoEditable("customer.name");
		assertNoEditable("customer.address.street");
	}
	
	public void testSearchReferenceWithListInsideSection() throws Exception {
		execute("CRUD.new");
		execute("Sections.change", "activeSection=0");
				
		execute("Reference.search", "keyProperty=xava.Invoice.customer.number");
		String customerName = getValueInList(0, 0);		
		checkRow(0);		
		execute("ReferenceSearch.choose");
		assertValue("customer.name", customerName);				
	}
	
	public void testSections_aggregateCollection_orderedCollectionsInModel_posdeleteCollectionElement() throws Exception { 
		
		// Create
		execute("CRUD.new");
		execute("Sections.change", "activeSection=0");				
		assertExists("customer.number");
		assertNotExists("vatPercentage");
		
		String year = getValue("year");		
		setValue("number", "66");
		
		setValue("customer.number", "1");
		assertValue("customer.number", "1");
		assertValue("customer.name", "Javi");
		
		// First vat percentage for no validation error on save first detail
		execute("Sections.change", "activeSection=2");
		assertNotExists("customer.number");
		assertExists("vatPercentage");						
		setValue("vatPercentage", "23");
				
		execute("Sections.change", "activeSection=1");
		assertNotExists("customer.number");
		assertNotExists("vatPercentage");
		
		assertCollectionRowCount("details", 0);
		
		execute("Collection.new", "viewObject=xava_view_section1_details");
		setValue("details.serviceType", "0");
		setValue("details.quantity", "20");
		setValue("details.unitPrice", getProductUnitPrice());
		assertValue("details.amount", getProductUnitPriceFor("20"));
		setValue("details.product.number", getProductNumber());
		assertValue("details.product.description", getProductDescription());
		setValue("details.deliveryDate", "18/03/2004"); // Testing multiple-mapping in aggregate
		setValue("details.soldBy.number", getProductNumber());
		execute("Collection.save", "viewObject=xava_view_section1_details");
		assertNoErrors();		
		assertNotExists("details.serviceType"); // Testing hide detail on save
		assertCollectionRowCount("details", 1);
		assertNoEditable("year"); // Testing header is saved
		assertNoEditable("number");
				
		execute("Collection.new", "viewObject=xava_view_section1_details");
		setValue("details.serviceType", "1");
		setValue("details.quantity", "200");
		setValue("details.unitPrice", getProductUnitPrice());		
		assertValue("details.amount", getProductUnitPriceFor("200"));
		setValue("details.product.number", getProductNumber());
		assertValue("details.product.description", getProductDescription());
		setValue("details.deliveryDate", "19/03/2004"); // Testing multiple-mapping in aggregate
		setValue("details.soldBy.number", getProductNumber());
		execute("Collection.save", "viewObject=xava_view_section1_details");
		assertCollectionRowCount("details", 2);

		execute("Collection.new", "viewObject=xava_view_section1_details");
		setValue("details.serviceType", "2");
		setValue("details.quantity", "2");
		setValue("details.unitPrice", getProductUnitPrice());
		assertValue("details.amount", getProductUnitPriceFor("2"));
		setValue("details.product.number", getProductNumber());
		assertValue("details.product.description", getProductDescription());
		setValue("details.deliveryDate", "20/03/2004"); // Testing multiple-mapping in aggregate		
		execute("Collection.save", "viewObject=xava_view_section1_details");
		assertCollectionRowCount("details", 3);
				
		assertValueInCollection("details", 0, 0, "Urgent");
		assertValueInCollection("details", 0, 1, getProductDescription());
		assertValueInCollection("details", 0, 2, getProductUnitPriceInPesetas());
		assertValueInCollection("details", 0, 3, "2");
		assertValueInCollection("details", 0, 4, getProductUnitPrice());
		assertValueInCollection("details", 0, 5, getProductUnitPriceFor("2"));

		assertValueInCollection("details", 1, 0, "Special");
		assertValueInCollection("details", 1, 1, getProductDescription());
		assertValueInCollection("details", 1, 2, getProductUnitPriceInPesetas());
		assertValueInCollection("details", 1, 3, "200");
		assertValueInCollection("details", 1, 4, getProductUnitPrice());
		assertValueInCollection("details", 1, 5, getProductUnitPriceFor("200"));

		assertValueInCollection("details", 2, 0, "");
		assertValueInCollection("details", 2, 1, getProductDescription());
		assertValueInCollection("details", 2, 2, getProductUnitPriceInPesetas());
		assertValueInCollection("details", 2, 3, "20");
		assertValueInCollection("details", 2, 4, getProductUnitPrice());
		assertValueInCollection("details", 2, 5, getProductUnitPriceFor("20"));
										
		execute("CRUD.save");
		assertNoErrors();
		assertValue("number", "");
		execute("Sections.change", "activeSection=0");
		assertValue("customer.number", "");
		assertValue("customer.name", "");
		execute("Sections.change", "activeSection=1");				
		assertCollectionRowCount("details", 0);
		execute("Sections.change", "activeSection=2");
		assertValue("vatPercentage", "");
	
		// Consulting	
		setValue("year", year);
		setValue("number", "66");
		execute("CRUD.search");
		assertValue("year", year);
		assertValue("number", "66");
		execute("Sections.change", "activeSection=0");
		assertValue("customer.number", "1");
		assertValue("customer.name", "Javi");
		execute("Sections.change", "activeSection=1");
		assertCollectionRowCount("details", 3);
		
		assertValueInCollection("details", 0, 0, "Urgent");
		assertValueInCollection("details", 0, 1, getProductDescription());
		assertValueInCollection("details", 0, 2, getProductUnitPriceInPesetas());
		assertValueInCollection("details", 0, 3, "2");
		assertValueInCollection("details", 0, 4, getProductUnitPrice());
		assertValueInCollection("details", 0, 5, getProductUnitPriceFor("2"));

		assertValueInCollection("details", 1, 0, "Special");
		assertValueInCollection("details", 1, 1, getProductDescription());
		assertValueInCollection("details", 1, 2, getProductUnitPriceInPesetas());
		assertValueInCollection("details", 1, 3, "200");
		assertValueInCollection("details", 1, 4, getProductUnitPrice());
		assertValueInCollection("details", 1, 5, getProductUnitPriceFor("200"));

		assertValueInCollection("details", 2, 0, "");
		assertValueInCollection("details", 2, 1, getProductDescription());
		assertValueInCollection("details", 2, 2, getProductUnitPriceInPesetas());
		assertValueInCollection("details", 2, 3, "20");
		assertValueInCollection("details", 2, 4, getProductUnitPrice());
		assertValueInCollection("details", 2, 5, getProductUnitPriceFor("20"));
				
		execute("Sections.change", "activeSection=2");		
		assertValue("vatPercentage", "23");
		
		// Edit line
		execute("Sections.change", "activeSection=1");		
		assertNotExists("details.product.description");
		assertNotExists("details.quantity");
		assertNotExists("details.deliveryDate");
		execute("Invoices.editDetail", "row=1,viewObject=xava_view_section1_details");
		assertValue("details.product.description", getProductDescription());
		assertValue("details.quantity", "200");
		assertValue("details.deliveryDate", "19/03/2004");
		setValue("details.quantity", "234");
		setValue("details.deliveryDate", "23/04/2004");
		execute("Collection.save", "viewObject=xava_view_section1_details");
		assertNoErrors();
		assertValueInCollection("details", 1, 3, "234");		
		assertNotExists("details.product.description");
		assertNotExists("details.quantity");
		assertNotExists("details.deliveryDate");
		execute("Invoices.editDetail", "row=1,viewObject=xava_view_section1_details");
		assertValue("details.product.description", getProductDescription());
		assertValue("details.quantity", "234");
		assertValue("details.deliveryDate", "23/04/2004");
		
		// Return to save and consult for see if the line is edited
		execute("CRUD.save");
		assertNoErrors();
		setValue("year", year);
		setValue("number", "66");
		execute("CRUD.search");
		assertNoErrors();
		execute("Sections.change", "activeSection=1");
		assertValueInCollection("details", 1, 0, "Special");
		assertValueInCollection("details", 1, 1, getProductDescription());
		assertValueInCollection("details", 1, 2, getProductUnitPriceInPesetas());
		assertValueInCollection("details", 1, 3, "234");
		assertValueInCollection("details", 1, 4, getProductUnitPrice());
		assertValueInCollection("details", 1, 5, getProductUnitPriceFor("234"));
		assertNotExists("details.product.description");
		assertNotExists("details.quantity");
		assertNotExists("details.deliveryDate");
		execute("Invoices.editDetail", "row=1,viewObject=xava_view_section1_details");
		assertValue("details.product.description", getProductDescription());
		assertValue("details.quantity", "234");
		assertValue("details.deliveryDate", "23/04/2004");
		
		// Verifying that it do not delete member in collection that not are in list
		execute("CRUD.new");
		setValue("year", year);
		setValue("number", "66");
		execute("CRUD.search");
		assertNoErrors();
		execute("CRUD.save");
		assertNoErrors();
		setValue("year", year);
		setValue("number", "66");		
		execute("CRUD.search");
		assertNoErrors();
		execute("Sections.change", "activeSection=1");				
		execute("Invoices.editDetail", "row=1,viewObject=xava_view_section1_details");
		assertValue("details.product.description", getProductDescription());
		assertValue("details.quantity", "234");
		assertValue("details.deliveryDate", "23/04/2004");
		
		// Remove a row from collection
		assertCollectionRowCount("details", 3);
		execute("Collection.remove", "viewObject=xava_view_section1_details");
		assertCollectionRowCount("details", 2);
		assertValueInCollection("details", 0, 0, "Urgent");
		assertValueInCollection("details", 0, 1, getProductDescription());
		assertValueInCollection("details", 0, 2, getProductUnitPriceInPesetas());
		assertValueInCollection("details", 0, 3, "2");
		assertValueInCollection("details", 0, 4, getProductUnitPrice());
		assertValueInCollection("details", 0, 5, getProductUnitPriceFor("2"));

		assertValueInCollection("details", 1, 0, "");
		assertValueInCollection("details", 1, 1, getProductDescription());
		assertValueInCollection("details", 1, 2, getProductUnitPriceInPesetas());
		assertValueInCollection("details", 1, 3, "20");
		assertValueInCollection("details", 1, 4, getProductUnitPrice());
		assertValueInCollection("details", 1, 5, getProductUnitPriceFor("20"));
		
		
		//ejecutar("CRUD.save"); // It is not necessary delete for record the deleted of a row 		
		execute("CRUD.new");
		assertNoErrors();
		assertCollectionRowCount("details", 0);
		
		// Verifying that line is deleted
		setValue("year", year);
		setValue("number", "66");
		execute("CRUD.search");
		assertNoErrors();
		execute("Sections.change", "activeSection=1");

		assertCollectionRowCount("details", 2);
		assertValueInCollection("details", 0, 0, "Urgent");
		assertValueInCollection("details", 0, 1, getProductDescription());
		assertValueInCollection("details", 0, 2, getProductUnitPriceInPesetas());
		assertValueInCollection("details", 0, 3, "2");
		assertValueInCollection("details", 0, 4, getProductUnitPrice());
		assertValueInCollection("details", 0, 5, getProductUnitPriceFor("2"));

		assertValueInCollection("details", 1, 0, "");
		assertValueInCollection("details", 1, 1, getProductDescription());
		assertValueInCollection("details", 1, 2, getProductUnitPriceInPesetas());
		assertValueInCollection("details", 1, 3, "20");
		assertValueInCollection("details", 1, 4, getProductUnitPrice());
		assertValueInCollection("details", 1, 5, getProductUnitPriceFor("20"));
		
		assertValue("comment", "DETAIL DELETED"); // verifying postdelete-calculator in collection
								
		// Delete		
		execute("CRUD.delete");
		execute("ConfirmDelete.confirmDelete");
		assertMessage("Invoice deleted successfully");
	}
	
	public void testAggregateValidatorUsingReferencesToContainer() throws Exception { 
		
		// Create
		execute("CRUD.new");
		execute("Sections.change", "activeSection=0");				
		
		String a�o = getValue("year");		
		setValue("number", "66");
		setValue("paid", "true");
		setValue("customer.number", "1");
		
		// First, vat percentage for not validate errors on save first detail  
		execute("Sections.change", "activeSection=2");
		setValue("vatPercentage", "23");
				
		execute("Sections.change", "activeSection=1");
		
		execute("Collection.new", "viewObject=xava_view_section1_details");
		setValue("details.serviceType", "0");
		setValue("details.quantity", "20");
		setValue("details.unitPrice", getProductUnitPrice());
		assertValue("details.amount", getProductUnitPriceFor("20"));
		setValue("details.product.number", getProductNumber());
		assertValue("details.product.description", getProductDescription());
		setValue("details.deliveryDate", "18/03/2004");
		setValue("details.soldBy.number", getProductNumber());
		execute("Collection.save", "viewObject=xava_view_section1_details");
		assertError("It is not possible to add details, the invoice is paid");		
		
		// Delete invoice
		execute("CRUD.delete");
		execute("ConfirmDelete.confirmDelete");
		assertMessage("Invoice deleted successfully");
	}
	
	
	public void testValidationOnSaveAggregateAndModelValidatorReceivesReference() throws Exception {
		
		// Create
		execute("CRUD.new");
		execute("Sections.change", "activeSection=0");				
		assertExists("customer.number");
		assertNotExists("vatPercentage");
		
		String a�o = getValue("year");		
		setValue("number", "66");
		
		setValue("customer.number", "1");
		assertValue("customer.number", "1");
		assertValue("customer.name", "Javi");
		
		// First, vat percentage for not validation errors on save first detail
		execute("Sections.change", "activeSection=2");
		assertNotExists("customer.number");
		assertExists("vatPercentage");						
		setValue("vatPercentage", "23");
				
		execute("Sections.change", "activeSection=1");
		assertNotExists("customer.number");
		assertNotExists("vatPercentage");
		
		assertCollectionRowCount("details", 0);
		
		execute("Collection.new", "viewObject=xava_view_section1_details");
		setValue("details.serviceType", "0");
		setValue("details.quantity", "20");
		setValue("details.unitPrice", getProductUnitPricePlus10());
		assertValue("details.amount", "600");
		assertValue("details.product.number", "0");
		assertValue("details.product.description", "");
		setValue("details.deliveryDate", "18/03/2004"); 
		setValue("details.soldBy.number", getProductNumber());
		execute("Collection.save", "viewObject=xava_view_section1_details");
		assertError("It si needed specify a product for a valid invoice detail");
		
		setValue("details.product.number", getProductNumber()); 
		assertValue("details.product.description", getProductDescription());
		execute("Collection.save", "viewObject=xava_view_section1_details");
		assertError("Invoice price of a product can not be greater to official price of the product");
		
		setValue("details.unitPrice", getProductUnitPrice());
		execute("Collection.save", "viewObject=xava_view_section1_details");
		assertNoErrors();
								
		// Delete		
		execute("CRUD.delete");
		execute("ConfirmDelete.confirmDelete");
		assertMessage("Invoice deleted successfully");
	}
	
	
	public void testDefaultValueCalculation() throws Exception {		
		execute("CRUD.new");
		assertValue("year", getCurrentYear());		
		assertValue("date", getCurrentDate());
		assertValue("yearDiscount", getYearDiscount(getCurrentYear()));
		setValue("year", "2002");
		assertValue("yearDiscount", getYearDiscount("2002"));		
	}
	
	public void testCalculatedValuesFromSubviewToUpperView() throws Exception {
		execute("CRUD.new");
		execute("Sections.change", "activeSection=0");
		assertValue("customerDiscount", "0");
		assertValue("customerTypeDiscount", "0");
		assertValue("customer.number", "0");
		assertValue("customer.name", "");
		setValue("customer.number", "1");
		assertValue("customer.number", "1");
		assertValue("customer.name", "Javi");
		assertValue("customerDiscount", "11.5");
		//assertValue("customerTypeDiscount", "30"); // Still not supported: customer type 
					// changes at same time that number, and to throw the change  
					// of 2 properties at same time still is not supported
		setValue("customer.number", "2");
		assertValue("customer.number", "2");		
		assertValue("customerDiscount", "22.75");
		setValue("customer.number", "3");
		assertValue("customer.number", "3");		
		assertValue("customerDiscount", "0.25");				
	}
	
	public void testCalculatedValueOnChangeBoolean() throws Exception {
		execute("CRUD.new");
		execute("Sections.change", "activeSection=0");
		assertValue("customerDiscount", "0");
		setValue("paid", "true");
		assertValue("customerDiscount", "77");				
	}
		
	public void testEditableCollectionActions() throws Exception {
		execute("CRUD.new");
		String [] initialActions = {
			"Navigation.previous",
			"Navigation.first",
			"Navigation.next",
			"CRUD.new",
			"CRUD.save",
			"CRUD.delete",
			"CRUD.search",
			"Invoices.removeViewDeliveryInInvoice",
			"Invoices.addViewDeliveryInInvoice",			
			"Sections.change",
			"Reference.search",
			"Reference.createNew",			
			"Mode.list"						
		};		
		assertActions(initialActions);
		
		InvoiceKey key = getInvoiceKey();				
		setValue("year", String.valueOf(key.getYear()));
		setValue("number", String.valueOf(key.getNumber()));
		execute("CRUD.search");
		assertNoErrors();
		
		execute("Sections.change", "activeSection=1");

		String [] aggregateListActions = {
			"Navigation.previous",
			"Navigation.first",
			"Navigation.next",
			"CRUD.new",
			"CRUD.save",
			"CRUD.delete",
			"CRUD.search",
			"Invoices.removeViewDeliveryInInvoice",
			"Invoices.addViewDeliveryInInvoice",									
			"Mode.list",
			"Sections.change",
			"Invoices.editDetail", // because it is overwrite, otherwise 'Collection.edit'
			"Collection.new"			
		};		
		assertActions(aggregateListActions);
		
		execute("Invoices.editDetail", "row=0,viewObject=xava_view_section1_details");
		
		String [] aggregateDetailActions = {
			"Navigation.previous",
			"Navigation.first",
			"Navigation.next",
			"CRUD.new",
			"CRUD.save",
			"CRUD.delete",
			"CRUD.search",			
			"Invoices.removeViewDeliveryInInvoice",
			"Invoices.addViewDeliveryInInvoice",			
			"Mode.list",
			"Sections.change",			
			"Reference.createNew",
			"Reference.search",
			"Invoices.editDetail", // because it is overwrite, otherwise 'Collection.edit'
			"Collection.save",
			"Collection.remove",
			"Collection.hiddenDetail",
			"Invoices.viewProduct"
		};		
		assertActions(aggregateDetailActions);
		
		assertEditable("details.serviceType");						
	}
	
	public void testDetailActionInCollection_overwriteEditAction_goAndReturnToAnotherXavaView() throws Exception {
		assertNoListTitle();
		execute("CRUD.new");		
		InvoiceKey key = getInvoiceKey();				
		setValue("year", String.valueOf(key.getYear()));
		setValue("number", String.valueOf(key.getNumber()));
		execute("CRUD.search");
		assertNoErrors();		
		execute("Sections.change", "activeSection=1");		
		execute("Invoices.editDetail", "row=0,viewObject=xava_view_section1_details");
		assertNoErrors();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		assertValue("details.remarks", "Edit at " + df.format(new java.util.Date()));
		
		String productNumber = getValue("details.product.number");
		assertTrue("Detail must to have product number", !Is.emptyString(productNumber));
		String productDescription = getValue("details.product.description");
		assertTrue("Detail must to have product description", !Is.emptyString(productDescription));
		
		execute("Invoices.viewProduct", "viewObject=xava_view_section1_details");
		assertNoErrors();
		assertNoAction("CRUD.new");
		assertAction("ProductFromInvoice.return");
		assertValue("Product", "number", productNumber);
		assertValue("Product", "description", productDescription);
		
		execute("ProductFromInvoice.return");
		assertNoErrors();
		assertAction("CRUD.new");
		assertNoAction("ProductFromInvoice.return");
		assertValue("year", String.valueOf(key.getYear()));
		assertValue("number", String.valueOf(key.getNumber()));									
	}
	
	
	public void testViewCollectionElementWithKeyWithReference() throws Exception {
		deleteInvoiceDeliveries();
		Delivery delivery = createDelivery();
		
		execute("CRUD.new");
		setValue("year", String.valueOf(getInvoiceKey().getYear()));
		setValue("number", String.valueOf(getInvoiceKey().getNumber()));
		
		execute("CRUD.search");
		assertNoErrors();
		
		execute("Sections.change", "activeSection=3");
		assertCollectionRowCount("deliveries", 1);
		
		assertNotExists("deliveries.number");
		assertNotExists("deliveries.date");
		assertNotExists("deliveries.description");
		execute("Collection.view", "row=0,viewObject=xava_view_section3_deliveries");
		assertValue("deliveries.number", "666");		
		assertValue("deliveries.date", "22/02/2004");		
		assertValue("deliveries.description", "DELIVERY JUNIT 666");
		assertNoEditable("deliveries.number"); 
		assertNoEditable("deliveries.date"); 		
		assertNoEditable("deliveries.description"); 
	}
	
	public void testDefaultValueInDetailCollection() throws Exception {
		execute("CRUD.new");
		execute("Sections.change", "activeSection=1");		
		execute("Collection.new", "viewObject=xava_view_section1_details");
		assertValue("details.deliveryDate", getCurrentDate());
	}
				
	public void testCalculatedPropertiesInSection() throws Exception {
		execute("Mode.detailAndFirst");
		execute("Sections.change", "activeSection=2");		
		String samountsSum = getValue("amountsSum");		
		BigDecimal amountsSum = stringToBigDecimal(samountsSum);
		assertTrue("Amounts sum not must be zero", amountsSum.compareTo(new BigDecimal("0")) != 0);
		String svatPercentage = getValue("vatPercentage"); 		
		BigDecimal vatPercentage = stringToBigDecimal(svatPercentage);
		BigDecimal newVatPercentage = vatPercentage.add(new BigDecimal("1")).setScale(0);
		setValue("vatPercentage", newVatPercentage.toString());		
		BigDecimal vat = amountsSum.multiply(newVatPercentage).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		String svat = nf.format(vat);
		assertValue("vat", svat);
	}			
	
	private BigDecimal stringToBigDecimal(String s) throws ParseException {
		NumberFormat nf = NumberFormat.getInstance();
		Number n = nf.parse(s);
		return new BigDecimal(n.toString());
	}
		
	private void deleteInvoiceDeliveries() throws Exception {
		Invoice invoice = InvoiceUtil.getHome().findByPrimaryKey(getInvoiceKey());
		Iterator it = invoice.getDeliveries().iterator();
		while (it.hasNext()) {
			Delivery delivery = (Delivery) PortableRemoteObject.narrow(it.next(), Delivery.class);
			delivery.remove();	
		}		
	}

	private Delivery createDelivery() throws Exception {
		DeliveryValue deliveryValue = new DeliveryValue();
		deliveryValue.setInvoice_year(getInvoiceKey().getYear());
		deliveryValue.setInvoice_number(getInvoiceKey().getNumber());
		deliveryValue.setType_number(1);		
		deliveryValue.setNumber(666);
		deliveryValue.setDate(Dates.create(22,2,2004));
		deliveryValue.setDescription("Delivery JUNIT 666");
				
		return DeliveryUtil.getHome().create(deliveryValue);				
	}
	
	
	private String getProductNumber() throws Exception {
		if (productNumber == null) {
			productNumber = String.valueOf(getProduct().getNumber());
		}
		return productNumber;
	}

	private String getProductDescription() throws Exception {
		if (productDescription == null) {
			productDescription = getProduct().getDescription();
		}
		return productDescription;
	}
	
	private String getSellerNumber() throws Exception {
		if (sellerNumber == null) {
			sellerNumber = String.valueOf(getSeller().getNumber());
		}
		return sellerNumber;
	}
	
	
	private String getProductUnitPriceInPesetas() throws Exception {
		if (productUnitPriceInPesetas == null) {			
			productUnitPriceInPesetas = DecimalFormat.getInstance().format(getProduct().getUnitPriceInPesetas());
		}
		return productUnitPriceInPesetas;
		
	}
	
	private String getProductUnitPrice() throws Exception {
		if (productUnitPrice == null) {			
			productUnitPrice = DecimalFormat.getInstance().format(getProductUnitPriceDB());
		}
		return productUnitPrice;		
	}
	
	private BigDecimal getProductUnitPriceDB() throws RemoteException, Exception {
		if (productUnitPriceDB == null) {
			productUnitPriceDB = getProduct().getUnitPrice();
		}
		return productUnitPriceDB;
	}
	
	private String getProductUnitPricePlus10() throws Exception {
		if (productUnitPricePlus10 == null) {			
			productUnitPricePlus10 = DecimalFormat.getInstance().format(getProductUnitPriceDB().add(new BigDecimal("10")));
		}
		return productUnitPricePlus10;		
	}
	
	private String getProductUnitPriceFor(String cantidad) throws Exception {
		return DecimalFormat.getInstance().format(getProductUnitPriceDB().multiply(new BigDecimal(cantidad)));
	}
	
	private Product getProduct() throws Exception {
		if (product == null) {
			Collection products = ProductUtil.getHome().findAll();
			if (products.isEmpty()) {
				fail("It must to have products to run this test");
			}
			product = (Product) PortableRemoteObject.narrow(products.iterator().next(), Product.class);
		}
		return product;
	}
	
	private Seller getSeller() throws Exception {
		if (seller == null) {
			Collection sellers = SellerUtil.getHome().findAll();
			if (sellers.isEmpty()) {
				fail("It must to have sellers to run this test");
			}
			seller = (Seller) PortableRemoteObject.narrow(sellers.iterator().next(), Seller.class);
		}
		return seller;
	}
	
	
	private String getCurrentDate() {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		return df.format(new java.util.Date());
	}
	
	private String getCurrentYear() {
		DateFormat df = new SimpleDateFormat("yyyy");
		return df.format(new java.util.Date());
	}
	
	private String getYearDiscount(String syear) throws Exception {
		int year = Integer.parseInt(syear);
		YearInvoiceDiscountCalculator calculator = new YearInvoiceDiscountCalculator();
		calculator.setYear(year);
		BigDecimal bd = (BigDecimal) calculator.calculate();		
		return bd.setScale(0, BigDecimal.ROUND_DOWN).toString();
	}

	private InvoiceKey getInvoiceKey() throws Exception {
		if (invoiceKey == null) {		
			Iterator it = InvoiceUtil.getHome().findAll().iterator();
			while (it.hasNext()) {			
				Invoice invoice = (Invoice) PortableRemoteObject.narrow(it.next(), Invoice.class);
				if (invoice.getDetailsCount() > 0) {
					invoiceKey = (InvoiceKey) invoice.getPrimaryKey();
					break;
				}			
			}
			if (invoiceKey == null) {
				fail("It must to exists at least one invoice with details for run this test");
			}
		}
		return invoiceKey;
	}
	
	private void assertDateInList(String date) throws Exception {
		int c = getListRowCount();
		for (int i=0; i<c; i++) {
			assertValueInList(i, "date", date);
		}
	}
	
	private void assertYearInList(String year) throws Exception {
		int c = getListRowCount();
		for (int i=0; i<c; i++) {
			String date = getValueInList(i, "date");
			assertTrue(date + " is not of " + year, date.endsWith(year));
		}
	}
	
	private String formatBigDecimal(String value) throws Exception {
		NumberFormat nf = NumberFormat.getNumberInstance(Locale.UK);
		nf.setMinimumFractionDigits(2);
		Number b = nf.parse(value);
		return nf.format(b);
	}
								
}
