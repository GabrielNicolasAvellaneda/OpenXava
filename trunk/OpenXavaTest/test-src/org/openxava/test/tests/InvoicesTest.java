package org.openxava.test.tests;

import java.math.*;
import java.rmi.*;
import java.text.*;
import java.util.*;

import javax.rmi.*;

import org.openxava.formatters.*;
import org.openxava.jpa.*;
import org.openxava.test.calculators.*;
import org.openxava.test.model.*;
import org.openxava.tests.*;
import org.openxava.util.*;


/**
 * @author Javier Paniza
 */

public class InvoicesTest extends ModuleTestBase {
	
	private Invoice invoice;
	private BigDecimal productUnitPriceDB;
	private String productUnitPricePlus10;
	private String productUnitPrice;
	private String productUnitPriceInPesetas;
	private String productDescription;
	private String productNumber;
	private Product product;	
	
	static {
		// To force to finder of the pojo to use hibernate, although jpa is configured
		XavaPreferences.getInstance().setJPAPersistence(false);
	}

	public InvoicesTest(String testName) {
		super(testName, "Invoices");		
	}
	
	public void testPaginationInCollections() throws Exception {
		// The invoice 2007/14 has 14 detail lines
		execute("CRUD.new");
		setValue("year", "2007");
		setValue("number", "14");
		execute("CRUD.search");
		assertNoErrors();		
		assertValue("comment", "MORE THAN 10 LINES");
		execute("Sections.change", "activeSection=1");
		assertCollectionRowCount("details", 10);
		execute("List.goNextPage", "collection=details");
		assertCollectionRowCount("details", 4);
		execute("List.goPreviousPage", "collection=details");
		assertCollectionRowCount("details", 10);
		execute("List.goPage", "page=2,collection=details");
		assertCollectionRowCount("details", 4);
		execute("List.goPage", "page=1,collection=details");
		assertCollectionRowCount("details", 10);
	}
	
	public void testCollectionElementHiddenOnChangeObject_generatePdfAggregateCollection() throws Exception {
		execute("Mode.detailAndFirst");
		execute("Sections.change", "activeSection=1");
		
		// Verifying that detail is closed
		assertAction("Collection.new");
		assertNoAction("Collection.save");
		
		// Editing the first detail
		execute("Invoices.editDetail", "row=0,viewObject=xava_view_section1_details");
		
		// Verifying that detail is opened
		assertNoAction("Collection.new");
		assertAction("Collection.save");
		
		// Go to next invoice
		execute("Navigation.next");
		
		// Verifying that detail is closed
		assertAction("Collection.new");
		assertNoAction("Collection.save");	
		
		// Print a collection of aggregates
		execute("Print.generatePdf", "viewObject=xava_view_section1_details");
		assertContentTypeForPopup("application/pdf");
	}
	
	public void testSearchByPropertyWithConverterInDetailMode() throws Exception {
		execute("CRUD.new");
		setValue("year", "");
		setValue("date", "");
		setValue("paid", "true");
		execute("CRUD.search");
		assertNoErrors();
	}
	
	public void testI18nOfLabelOfAConcreteView_alwaysEnabledActions() throws Exception {
		execute("CRUD.new");
		assertLabel("customer.number", "Little code");
		assertAction("Customers.changeNameLabel");
		assertAction("Customers.prefixStreet");
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
				
		createOneDetail(); // Because at least one detail is required
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
		assertMessage("Invoice deleted successfully");				
	}
	
	public void testCustomizeList() throws Exception {
		doTestCustomizeList_addColumns();
		tearDown(); setUp();
		doTestCustomizeList_storePreferences();
	}
	
	private void doTestCustomizeList_addColumns() throws Exception {
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
		
		assertCollectionRowCount("xavaPropertiesList", 33);
		execute("AddColumns.sort");		
		
		assertValueInCollection("xavaPropertiesList",  0, 0, "comment");
		assertValueInCollection("xavaPropertiesList",  1, 0, "considerable");
		assertValueInCollection("xavaPropertiesList",  2, 0, "customer.address.asString");
		assertValueInCollection("xavaPropertiesList",  3, 0, "customer.address.city");
		assertValueInCollection("xavaPropertiesList",  4, 0, "customer.address.state.fullName");
		assertValueInCollection("xavaPropertiesList",  5, 0, "customer.address.state.id");		
		assertValueInCollection("xavaPropertiesList",  6, 0, "customer.address.state.name");		
		assertValueInCollection("xavaPropertiesList",  7, 0, "customer.address.street");		
		assertValueInCollection("xavaPropertiesList",  8, 0, "customer.address.zipCode");
		assertValueInCollection("xavaPropertiesList",  9, 0, "customer.alternateSeller.level.description");
		assertValueInCollection("xavaPropertiesList", 10, 0, "customer.alternateSeller.level.id");			
		assertValueInCollection("xavaPropertiesList", 11, 0, "customer.alternateSeller.name");				
		assertValueInCollection("xavaPropertiesList", 12, 0, "customer.alternateSeller.number");
		assertValueInCollection("xavaPropertiesList", 13, 0, "customer.alternateSeller.regions");		
		assertValueInCollection("xavaPropertiesList", 14, 0, "customer.city");
		assertValueInCollection("xavaPropertiesList", 15, 0, "customer.local");
		assertValueInCollection("xavaPropertiesList", 16, 0, "customer.name");
		assertValueInCollection("xavaPropertiesList", 17, 0, "customer.number");
		assertValueInCollection("xavaPropertiesList", 18, 0, "customer.photo");
		assertValueInCollection("xavaPropertiesList", 19, 0, "customer.relationWithSeller");
		assertValueInCollection("xavaPropertiesList", 20, 0, "customer.remarks");		
		assertValueInCollection("xavaPropertiesList", 21, 0, "customer.seller.level.description");
		assertValueInCollection("xavaPropertiesList", 22, 0, "customer.seller.level.id");				
		assertValueInCollection("xavaPropertiesList", 23, 0, "customer.seller.name");
		assertValueInCollection("xavaPropertiesList", 24, 0, "customer.seller.number");
		assertValueInCollection("xavaPropertiesList", 25, 0, "customer.seller.regions");		
		assertValueInCollection("xavaPropertiesList", 26, 0, "customer.type");
		assertValueInCollection("xavaPropertiesList", 27, 0, "customerDiscount");
		assertValueInCollection("xavaPropertiesList", 28, 0, "customerTypeDiscount");		
		assertValueInCollection("xavaPropertiesList", 29, 0, "sellerDiscount");
		assertValueInCollection("xavaPropertiesList", 30, 0, "total");
		assertValueInCollection("xavaPropertiesList", 31, 0, "vatPercentage");
		assertValueInCollection("xavaPropertiesList", 32, 0, "yearDiscount");
		
		checkRow("selectedProperties", "customer.address.city");
		checkRow("selectedProperties", "yearDiscount"); 
		 		
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
		assertLabelInList(8, "City");
		assertLabelInList(9, "Year discount");
		
		
		execute("List.addColumns");

		assertCollectionRowCount("xavaPropertiesList", 31);
		assertValueInCollection("xavaPropertiesList",  0, 0, "comment");
		assertValueInCollection("xavaPropertiesList",  1, 0, "considerable");
		assertValueInCollection("xavaPropertiesList",  2, 0, "customer.address.asString");		
		assertValueInCollection("xavaPropertiesList",  3, 0, "customer.address.state.fullName");
		assertValueInCollection("xavaPropertiesList",  4, 0, "customer.address.state.id");		
		assertValueInCollection("xavaPropertiesList",  5, 0, "customer.address.state.name");		
		assertValueInCollection("xavaPropertiesList",  6, 0, "customer.address.street");		
		assertValueInCollection("xavaPropertiesList",  7, 0, "customer.address.zipCode");
		assertValueInCollection("xavaPropertiesList",  8, 0, "customer.alternateSeller.level.description");
		assertValueInCollection("xavaPropertiesList",  9, 0, "customer.alternateSeller.level.id");			
		assertValueInCollection("xavaPropertiesList", 10, 0, "customer.alternateSeller.name");				
		assertValueInCollection("xavaPropertiesList", 11, 0, "customer.alternateSeller.number");
		assertValueInCollection("xavaPropertiesList", 12, 0, "customer.alternateSeller.regions");		
		assertValueInCollection("xavaPropertiesList", 13, 0, "customer.city");
		assertValueInCollection("xavaPropertiesList", 14, 0, "customer.local");
		assertValueInCollection("xavaPropertiesList", 15, 0, "customer.name");
		assertValueInCollection("xavaPropertiesList", 16, 0, "customer.number");
		assertValueInCollection("xavaPropertiesList", 17, 0, "customer.photo");
		assertValueInCollection("xavaPropertiesList", 18, 0, "customer.relationWithSeller");
		assertValueInCollection("xavaPropertiesList", 19, 0, "customer.remarks");		
		assertValueInCollection("xavaPropertiesList", 20, 0, "customer.seller.level.description");
		assertValueInCollection("xavaPropertiesList", 21, 0, "customer.seller.level.id");				
		assertValueInCollection("xavaPropertiesList", 22, 0, "customer.seller.name");
		assertValueInCollection("xavaPropertiesList", 23, 0, "customer.seller.number");
		assertValueInCollection("xavaPropertiesList", 24, 0, "customer.seller.regions");		
		assertValueInCollection("xavaPropertiesList", 25, 0, "customer.type");
		assertValueInCollection("xavaPropertiesList", 26, 0, "customerDiscount");
		assertValueInCollection("xavaPropertiesList", 27, 0, "customerTypeDiscount");		
		assertValueInCollection("xavaPropertiesList", 28, 0, "sellerDiscount");
		assertValueInCollection("xavaPropertiesList", 29, 0, "total");
		assertValueInCollection("xavaPropertiesList", 30, 0, "vatPercentage");		
		
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
		assertLabelInList(8, "City");
		assertLabelInList(9, "Year discount");
				
	}
	
	private void doTestCustomizeList_storePreferences() throws Exception {
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
		assertLabelInList(8, "City");
		assertLabelInList(9, "Year discount");
				
		
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
		String date = getValueInList(0, 2);
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
		assertYearInList("02");

		String [] condition2004 = { " ", " ", "2004", "true" }; // We supussed that there are invoices in 2004
		setConditionValues(condition2004);
		execute("List.filter");
		assertYearInList("04");
		
		String [] monthComparators = { "=", "=", "month_comparator", ""};
		setConditionComparators(monthComparators);		
		String [] conditionMonth1 = { " ", " ", "1", "true" }; 
		setConditionValues(conditionMonth1);
		execute("List.filter");
		assertListRowCount(3); // We supussed that there are 3 invoices of month 1
		
		String [] yearMonthComparators = { "=", "=", "year_month_comparator", ""};
		setConditionComparators(yearMonthComparators);		
		String [] conditionYear2004Month1 = { " ", " ", "2004/1", "true" }; 
		setConditionValues(conditionYear2004Month1);
		execute("List.filter");
		assertListRowCount(2); // We supussed that there are 2 invoices of month 1 of year 2004				
	}
	
	public void testFilterByBoolean() throws Exception {
		int total = Invoice.findAll().size();
		int paidOnes = Invoice.findPaidOnes().size();
		int notPaidOnes = Invoice.findNotPaidOnes().size();
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
		assertValue("Customer", "type", isOX3()?"0":"1");
		execute("Reference.search", "keyProperty=xava.Customer.alternateSeller.number");
		assertNoErrors();
		execute("ReferenceSearch.cancel");
		assertAction("NewCreation.saveNew");
		assertAction("NewCreation.cancel");	
		assertValue("Customer", "type", isOX3()?"0":"1");		
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
		setLocale("es");
		execute("CRUD.new");
		setValue("year", String.valueOf(getInvoice().getYear()));
		setValue("number", String.valueOf(getInvoice().getNumber()));
		execute("CRUD.search");
		assertNoErrors();
		String originalDate = getValue("date"); // For restore at end
		
		setValue("date", "1/1/2004");
		execute("CRUD.save");
		assertNoErrors();
		setValue("year", String.valueOf(getInvoice().getYear()));
		setValue("number", String.valueOf(getInvoice().getNumber()));
		execute("CRUD.search");
		assertNoErrors();
		assertValue("date", "01/01/2004");
		
		setValue("date", "02012004");
		execute("CRUD.save");
		assertNoErrors();
		setValue("year", String.valueOf(getInvoice().getYear()));
		setValue("number", String.valueOf(getInvoice().getNumber()));
		execute("CRUD.search");
		assertNoErrors();
		assertValue("date", "02/01/2004");
		
		setValue("date", "3.1.2004");
		execute("CRUD.save");
		assertNoErrors();
		setValue("year", String.valueOf(getInvoice().getYear()));
		setValue("number", String.valueOf(getInvoice().getNumber()));
		execute("CRUD.search");
		assertNoErrors();
		assertValue("date", "03/01/2004");
		
		setValue("date", "4-1-2004");
		execute("CRUD.save");
		assertNoErrors();
		setValue("year", String.valueOf(getInvoice().getYear()));
		setValue("number", String.valueOf(getInvoice().getNumber()));
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
		createDelivery(); 
				
		execute("CRUD.new");
		setValue("year", String.valueOf(getInvoice().getYear()));
		setValue("number", String.valueOf(getInvoice().getNumber()));
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
		assertValue("amountsSum", "");
		setValue("vatPercentage", "23");
				
		execute("Sections.change", "activeSection=1");
		assertNotExists("customer.number");
		assertNotExists("vatPercentage");
		
		assertCollectionRowCount("details", 0);
		
		execute("Collection.new", "viewObject=xava_view_section1_details");
		setValue("details.serviceType", isOX3()?"":"0");
		setValue("details.quantity", "20");
		setValue("details.unitPrice", getProductUnitPrice());
		assertValue("details.amount", getProductUnitPriceMultiplyBy("20"));
		setValue("details.product.number", getProductNumber());
		assertValue("details.product.description", getProductDescription());
		setValue("details.deliveryDate", "18/03/2004"); // Testing multiple-mapping in aggregate
		setValue("details.soldBy.number", getProductNumber());
		execute("Collection.save", "viewObject=xava_view_section1_details");
		assertMessage("Invoice detail created successfully");
		assertNoErrors();		
		assertExists("details.serviceType"); // Testing does not hide detail on save
		assertCollectionRowCount("details", 1);

		// Next line tests IModelCalculator in an aggregate collection (only apply to XML components)
		assertValueInCollection("details", 0, "free", "0".equals(getProductUnitPrice())?"Yes":"No"); 

		assertNoEditable("year"); // Testing header is saved
		assertNoEditable("number");
		
		// Testing if recalculate dependent properties
		execute("Sections.change", "activeSection=2");
		assertValue("amountsSum", getProductUnitPriceMultiplyBy("20")); 
		setValue("vatPercentage", "23");		
		execute("Sections.change", "activeSection=1");
		// end of recalculate testing
				
		setValue("details.serviceType", isOX3()?"0":"1");
		setValue("details.quantity", "200");
		setValue("details.unitPrice", getProductUnitPrice());		
		assertValue("details.amount", getProductUnitPriceMultiplyBy("200"));
		setValue("details.product.number", getProductNumber());
		assertValue("details.product.description", getProductDescription());
		setValue("details.deliveryDate", "3/19/04"); // Testing multiple-mapping in aggregate
		setValue("details.soldBy.number", getProductNumber());
		execute("Collection.save", "viewObject=xava_view_section1_details");
		assertCollectionRowCount("details", 2);
		
		setValue("details.serviceType", isOX3()?"1":"2");
		setValue("details.quantity", "2");
		setValue("details.unitPrice", getProductUnitPrice());
		assertValue("details.amount", getProductUnitPriceMultiplyBy("2"));
		setValue("details.product.number", getProductNumber());
		assertValue("details.product.description", getProductDescription());
		setValue("details.deliveryDate", "3/20/04"); // Testing multiple-mapping in aggregate		
		execute("Collection.save", "viewObject=xava_view_section1_details");
		assertCollectionRowCount("details", 3);
				
		assertValueInCollection("details", 0, 0, "Urgent");
		assertValueInCollection("details", 0, 1, getProductDescription());
		assertValueInCollection("details", 0, 2, getProductUnitPriceInPesetas());
		assertValueInCollection("details", 0, 3, "2");
		assertValueInCollection("details", 0, 4, getProductUnitPrice());
		assertValueInCollection("details", 0, 5, getProductUnitPriceMultiplyBy("2"));

		assertValueInCollection("details", 1, 0, "Special");
		assertValueInCollection("details", 1, 1, getProductDescription());
		assertValueInCollection("details", 1, 2, getProductUnitPriceInPesetas());
		assertValueInCollection("details", 1, 3, "200");
		assertValueInCollection("details", 1, 4, getProductUnitPrice());
		assertValueInCollection("details", 1, 5, getProductUnitPriceMultiplyBy("200"));

		assertValueInCollection("details", 2, 0, "");
		assertValueInCollection("details", 2, 1, getProductDescription());
		assertValueInCollection("details", 2, 2, getProductUnitPriceInPesetas());
		assertValueInCollection("details", 2, 3, "20");
		assertValueInCollection("details", 2, 4, getProductUnitPrice());
		assertValueInCollection("details", 2, 5, getProductUnitPriceMultiplyBy("20"));
										
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
		assertValueInCollection("details", 0, 5, getProductUnitPriceMultiplyBy("2"));

		assertValueInCollection("details", 1, 0, "Special");
		assertValueInCollection("details", 1, 1, getProductDescription());
		assertValueInCollection("details", 1, 2, getProductUnitPriceInPesetas());
		assertValueInCollection("details", 1, 3, "200");
		assertValueInCollection("details", 1, 4, getProductUnitPrice());
		assertValueInCollection("details", 1, 5, getProductUnitPriceMultiplyBy("200"));

		assertValueInCollection("details", 2, 0, "");
		assertValueInCollection("details", 2, 1, getProductDescription());
		assertValueInCollection("details", 2, 2, getProductUnitPriceInPesetas());
		assertValueInCollection("details", 2, 3, "20");
		assertValueInCollection("details", 2, 4, getProductUnitPrice());
		assertValueInCollection("details", 2, 5, getProductUnitPriceMultiplyBy("20"));
				
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
		assertValue("details.deliveryDate", "3/19/04");
		setValue("details.quantity", "234");
		setValue("details.deliveryDate", "4/23/04");
		execute("Collection.save", "viewObject=xava_view_section1_details");
		assertNoErrors();
		assertMessage("Invoice detail modified successfully");
		assertValueInCollection("details", 1, 3, "234");		
		assertExists("details.product.description"); // Because on save detail is not hide
		assertExists("details.quantity");
		assertExists("details.deliveryDate");
		execute("Invoices.editDetail", "row=1,viewObject=xava_view_section1_details");
		assertValue("details.product.description", getProductDescription());
		assertValue("details.quantity", "234");
		assertValue("details.deliveryDate", "4/23/04");
		
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
		assertValueInCollection("details", 1, 5, getProductUnitPriceMultiplyBy("234"));
		assertNotExists("details.product.description");
		assertNotExists("details.quantity");
		assertNotExists("details.deliveryDate");
		execute("Invoices.editDetail", "row=1,viewObject=xava_view_section1_details");
		assertValue("details.product.description", getProductDescription());
		assertValue("details.quantity", "234");
		assertValue("details.deliveryDate", "4/23/04");
		
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
		assertValue("details.deliveryDate", "4/23/04");
		
		// Remove a row from collection
		assertCollectionRowCount("details", 3);
		execute("Collection.remove", "viewObject=xava_view_section1_details");
		assertMessage("Invoice detail deleted from database");
		assertCollectionRowCount("details", 2);
		assertValueInCollection("details", 0, 0, "Urgent");
		assertValueInCollection("details", 0, 1, getProductDescription());
		assertValueInCollection("details", 0, 2, getProductUnitPriceInPesetas());
		assertValueInCollection("details", 0, 3, "2");
		assertValueInCollection("details", 0, 4, getProductUnitPrice());
		assertValueInCollection("details", 0, 5, getProductUnitPriceMultiplyBy("2"));

		assertValueInCollection("details", 1, 0, "");
		assertValueInCollection("details", 1, 1, getProductDescription());
		assertValueInCollection("details", 1, 2, getProductUnitPriceInPesetas());
		assertValueInCollection("details", 1, 3, "20");
		assertValueInCollection("details", 1, 4, getProductUnitPrice());
		assertValueInCollection("details", 1, 5, getProductUnitPriceMultiplyBy("20"));
		
		
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
		assertValueInCollection("details", 0, 5, getProductUnitPriceMultiplyBy("2"));

		assertValueInCollection("details", 1, 0, "");
		assertValueInCollection("details", 1, 1, getProductDescription());
		assertValueInCollection("details", 1, 2, getProductUnitPriceInPesetas());
		assertValueInCollection("details", 1, 3, "20");
		assertValueInCollection("details", 1, 4, getProductUnitPrice());
		assertValueInCollection("details", 1, 5, getProductUnitPriceMultiplyBy("20"));
		
		assertValue("comment", "DETAIL DELETED"); // verifying postdelete-calculator in collection
		
		
		// Testing if recalculate dependent properties on remove using chechbox in collection
		execute("Sections.change", "activeSection=2");		
		assertValue("amountsSum", getSumOf2ProductsUnitPriceMultiplyBy("2", "20"));		
		execute("Sections.change", "activeSection=1");
		checkRowCollection("details", 1);
		execute("Collection.removeSelected", "viewObject=xava_view_section1_details");
		assertNoErrors();
		execute("Sections.change", "activeSection=2");		 		
		assertValue("amountsSum", getProductUnitPriceMultiplyBy("2"));
		// end of recalculate testing		
								
		// Delete		
		execute("CRUD.delete");
		assertMessage("Invoice deleted successfully");
	}
	
	public void testAggregateValidatorUsingReferencesToContainer() throws Exception { 
		
		// Create
		execute("CRUD.new");
		execute("Sections.change", "activeSection=0");				
						
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
		assertValue("details.amount", getProductUnitPriceMultiplyBy("20"));
		setValue("details.product.number", getProductNumber());
		assertValue("details.product.description", getProductDescription());
		setValue("details.deliveryDate", "18/03/2004");
		setValue("details.soldBy.number", getProductNumber());
		execute("Collection.save", "viewObject=xava_view_section1_details");
		assertError("It is not possible to add details, the invoice is paid");		
		
		// Delete invoice
		execute("CRUD.delete");		
		assertMessage("Invoice deleted successfully");
	}
	
	
	public void testValidationOnSaveAggregateAndModelValidatorReceivesReference() throws Exception {
		
		// Create
		execute("CRUD.new");
		execute("Sections.change", "activeSection=0");				
		assertExists("customer.number");
		assertNotExists("vatPercentage");
				
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
		assertValue("details.amount", "600.00");
		assertValue("details.product.number", "");
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
		assertValue("customerDiscount", "");
		assertValue("customerTypeDiscount", "");
		assertValue("customer.number", "");
		assertValue("customer.name", "");
		setValue("customer.number", "1");
		assertValue("customer.number", "1");
		assertValue("customer.name", "Javi");
		assertValue("customerDiscount", "11.50");
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
		assertValue("customerDiscount", "");
		setValue("paid", "true");
		assertValue("customerDiscount", "77.00");				
	}
		
	public void testEditableCollectionActions_i18nforMemberOfCollections() throws Exception {
		execute("CRUD.new");
		String [] initialActions = {
			"Navigation.previous",
			"Navigation.first",
			"Navigation.next",
			"CRUD.new",
			"CRUD.save",
			"CRUD.delete",
			"CRUD.search",
			"Invoices.print",
			"Invoices.removeViewDeliveryInInvoice",
			"Invoices.addViewDeliveryInInvoice",			
			"Sections.change",
			"Customers.changeNameLabel",
			"Customers.prefixStreet",
			"Reference.search",
			"Reference.createNew",			
			"Reference.modify",
			"Mode.list"						
		};		
		assertActions(initialActions);
				
		setValue("year", String.valueOf(getInvoice().getYear()));
		setValue("number", String.valueOf(getInvoice().getNumber()));
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
			"Invoices.print",
			"Invoices.removeViewDeliveryInInvoice",
			"Invoices.addViewDeliveryInInvoice",									
			"Mode.list",
			"Sections.change",
			"Invoices.editDetail", // because it is overwrite, otherwise 'Collection.edit'
			"Collection.new",
			"Collection.removeSelected",
			"Print.generatePdf", // In collection
			"Print.generateExcel", // In collection
			"List.filter", 
			"List.orderBy", 
			"List.customize", 
			"List.hideRows"
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
			"Invoices.print",
			"Invoices.removeViewDeliveryInInvoice",
			"Invoices.addViewDeliveryInInvoice",			
			"Mode.list",
			"Sections.change",			
			"Reference.createNew",
			"Reference.search",
			"Reference.modify",
			"Gallery.edit",
			"Invoices.editDetail", // because it is overwrite, otherwise 'Collection.edit'
			"Collection.save",
			"Collection.remove",
			"Collection.hideDetail",
			"List.filter", 
			"List.orderBy", 
			"List.customize", 
			"List.hideRows",			
			"Invoices.viewProduct"
		};		
		assertActions(aggregateDetailActions);
		
		assertEditable("details.serviceType");
		
		// i18n for member of collections
		// In resource file we have: Invoice.details.product.description=Product
		assertLabelInCollection("details", 1, "Product");
	}
	
	public void testDetailActionInCollection_overwriteEditAction_goAndReturnToAnotherXavaView() throws Exception {
		assertNoListTitle();
		execute("CRUD.new");							
		setValue("year", String.valueOf(getInvoice().getYear()));
		setValue("number", String.valueOf(getInvoice().getNumber()));
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
		assertValue("year", String.valueOf(getInvoice().getYear()));
		assertValue("number", String.valueOf(getInvoice().getNumber()));									
	}
	
	
	public void testViewCollectionElementWithKeyWithReference() throws Exception {
		deleteInvoiceDeliveries();
		createDelivery();
		
		execute("CRUD.new");
		setValue("year", String.valueOf(getInvoice().getYear()));
		setValue("number", String.valueOf(getInvoice().getNumber()));
		
		execute("CRUD.search");
		assertNoErrors();
		
		execute("Sections.change", "activeSection=3");
		assertCollectionRowCount("deliveries", 1);
		
		assertNotExists("deliveries.number");
		assertNotExists("deliveries.date");
		assertNotExists("deliveries.description");
		execute("Collection.view", "row=0,viewObject=xava_view_section3_deliveries");
		assertValue("deliveries.number", "666");		
		assertValue("deliveries.date", "2/22/04");		
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
		nf.setMinimumFractionDigits(2);
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
		// Also delete transport charge, because they can reference to some delivery
		XPersistence.getManager().createQuery("delete from TransportCharge").executeUpdate();
		Iterator it = getInvoice().getDeliveries().iterator();
		while (it.hasNext()) {
			Delivery delivery = (Delivery) PortableRemoteObject.narrow(it.next(), Delivery.class);
			XPersistence.getManager().remove(delivery);	
		}		
	}

	private void createDelivery() throws Exception {
		Delivery delivery = new Delivery();
		delivery.setInvoice(getInvoice());
		DeliveryType type = new DeliveryType();
		type.setNumber(1);
		delivery.setType(type); 		
		delivery.setNumber(666);
		delivery.setDate(Dates.create(22,2,2004));
		delivery.setDescription("Delivery JUNIT 666");
		delivery.setRemarks("FOUR\nLINES\nCUATRO\nLINEAS"); // It's used in DeliveriesRemarks2002Test 
				
		XPersistence.getManager().persist(delivery);
		XPersistence.commit();
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
		
	private String getProductUnitPriceInPesetas() throws Exception {
		if (productUnitPriceInPesetas == null) {			
			productUnitPriceInPesetas = DecimalFormat.getInstance().format(getProduct().getUnitPriceInPesetas());
		}
		return productUnitPriceInPesetas;
		
	}
	
	private String getProductUnitPrice() throws Exception {
		if (productUnitPrice == null) {			
			productUnitPrice = getMoneyFormat().format(getProductUnitPriceDB());
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
			productUnitPricePlus10 = getMoneyFormat().format(getProductUnitPriceDB().add(new BigDecimal("10")));
		}
		return productUnitPricePlus10;		
	}
	
	private String getProductUnitPriceMultiplyBy(String quantity) throws Exception {
		return getMoneyFormat().format(getProductUnitPriceDB().multiply(new BigDecimal(quantity)));
	}

	private NumberFormat getMoneyFormat() {
		NumberFormat f = NumberFormat.getNumberInstance();
		f.setMinimumFractionDigits(2);
		f.setMaximumFractionDigits(2);
		return f;
	}
	
	
	private String getSumOf2ProductsUnitPriceMultiplyBy(String quantity1, String quantity2) throws Exception { 
		BigDecimal sum = getProductUnitPriceDB().multiply(new BigDecimal(quantity1)).add(getProductUnitPriceDB().multiply(new BigDecimal(quantity2)));		
		return getMoneyFormat().format(sum);
	}
	
	
	private Product getProduct() throws Exception {
		if (product == null) {
			product = (Product) XPersistence.getManager().find(Product.class, new Long(2));
		}
		return product;
	}
		
	private String getCurrentDate() {
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
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
		return getMoneyFormat().format(bd);
	}

	private Invoice getInvoice() throws Exception {
		if (invoice == null) {	
 			Collection invoices = XPersistence.getManager().createQuery("from Invoice").getResultList(); 
			Iterator it = invoices.iterator();
			while (it.hasNext()) {			
				Invoice inv = (Invoice) it.next();
				if (inv.getDetailsCount() > 0) {
					invoice = inv;
					break;
				}			
			}
			if (invoice == null) {
				fail("It must to exists at least one invoice with details for run this test");
			}
		}
		return invoice;
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
	
	private void createOneDetail() throws Exception {
		execute("Sections.change", "activeSection=1");
		assertNotExists("customer.number");
		assertNotExists("vatPercentage");

		assertCollectionRowCount("details", 0);

		execute("Collection.new", "viewObject=xava_view_section1_details");
		setValue("details.serviceType", "0");
		setValue("details.quantity", "20");
		setValue("details.unitPrice", getProductUnitPrice());
		assertValue("details.amount", getProductUnitPriceMultiplyBy("20"));
		setValue("details.product.number", getProductNumber());
		assertValue("details.product.description", getProductDescription());
		setValue("details.deliveryDate", "09/05/2007");
		setValue("details.soldBy.number", getProductNumber());
		execute("Collection.save", "viewObject=xava_view_section1_details");
		assertNoErrors();
		assertMessage("Invoice detail created successfully");
	}
									
}
