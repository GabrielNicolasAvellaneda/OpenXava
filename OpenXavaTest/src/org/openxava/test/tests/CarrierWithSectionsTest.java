package org.openxava.test.tests;

import java.util.*;

import org.openxava.tests.ModuleTestBase;
import org.openxava.util.Is;

/**
 * Create on 07/04/2008 (12:16:03)
 * 
 * @author Ana Andrés
 * @author Javier Paniza
 */
public class CarrierWithSectionsTest extends ModuleTestBase {
	
	public CarrierWithSectionsTest(String testName) {
		super(testName, "CarrierWithSections");
	}
	
	public void testCustomReport() throws Exception { 
		execute("ExtendedPrint.myReports");
		assertDialogTitle("My reports"); 
		assertValue("name", "Carrier report");
		assertCollectionRowCount("columns", 3);
		assertValueInCollection("columns", 0, 0, "Calculated");
		assertValueInCollection("columns", 1, 0, "Number");
		assertValueInCollection("columns", 2, 0, "Name");

		execute("CustomReport.columnUp", "row=2,viewObject=xava_view_columns");
		assertValueInCollection("columns", 0, 0, "Calculated");
		assertValueInCollection("columns", 1, 0, "Name");
		assertValueInCollection("columns", 2, 0, "Number");
		 
		reload();
		assertDialogTitle("My reports");  
		
		execute("CustomReport.columnDown", "row=0,viewObject=xava_view_columns");
		assertValueInCollection("columns", 0, 0, "Name");
		assertValueInCollection("columns", 1, 0, "Calculated");
		assertValueInCollection("columns", 2, 0, "Number");
		
		execute("CustomReport.editColumn", "row=1,viewObject=xava_view_columns");
		assertValue("name", "calculated");
		assertValue("label", "Calculated"); 
		assertNotExists("comparator");
		assertNotExists("value");
		assertNotExists("order");
		assertNotExists("descriptionsListValue");  
		assertNotExists("booleanValue"); 
		assertNotExists("validValuesValue"); 
		
		setValue("name", "number");
		assertValue("label", "Number"); 
		assertExists("comparator");
		assertExists("value");
		assertExists("order");
		assertNotExists("descriptionsListValue"); 
		assertNotExists("booleanValue"); 
		assertNotExists("validValuesValue"); 
		
		setValue("name", "calculated");
		assertValue("label", "Calculated"); 
		assertNotExists("comparator");
		assertNotExists("value");
		assertNotExists("order");
		assertNotExists("descriptionsListValue"); 
		assertNotExists("booleanValue"); 
		assertNotExists("validValuesValue"); 
		
		execute("Collection.hideDetail");
		
		execute("CustomReport.removeColumn", "row=1,viewObject=xava_view_columns");
		assertValueInCollection("columns", 0, 0, "Name");
		assertValueInCollection("columns", 1, 0, "Number");
		
		execute("Collection.new", "viewObject=xava_view_columns");
		String [][] validColumnNames = {
			{ "", "" },	
			{ "number", "number" },
			{ "name", "name" },
			{ "drivingLicence.type", "drivingLicence.type" },	
			{ "drivingLicence.level", "drivingLicence.level" },	
			{ "drivingLicence.description", "drivingLicence.description" },	
			{ "warehouse.zoneNumber", "warehouse.zoneNumber" },	
			{ "warehouse.number", "warehouse.number" },	
			{ "warehouse.name", "warehouse.name" },	
			{ "remarks", "remarks" },	
			{ "calculated", "calculated" }
		};
		assertValidValues("name", validColumnNames);
		assertValue("name", "");		
		String [][] emptyComparators = {
		};
		assertValidValues("comparator", emptyComparators);
		setValue("name", "warehouse.zoneNumber");
		assertValue("label", "Zone of Warehouse");
		assertValue("comparator", "eq_comparator"); 
		String [][] numberComparators = {
			{ "eq_comparator", "=" },
			{ "ne_comparator", "<>" },  
			{ "ge_comparator", ">=" }, 
			{ "le_comparator", "<=" }, 
			{ "gt_comparator", ">" }, 
			{ "lt_comparator", "<" } 
		};
		assertValidValues("comparator", numberComparators);
		
		setValue("name", "name");
		assertValue("comparator", "starts_comparator"); 
		String [][] stringComparators = {
			{ "starts_comparator", "starts" },
			{ "ends_comparator", "ends" }, 
			{ "contains_comparator", "contains" },
			{ "not_contains_comparator", "not contains" },
			{ "eq_comparator", "=" },
			{ "ne_comparator", "<>" },  
			{ "ge_comparator", ">=" }, 
			{ "le_comparator", "<=" }, 
			{ "gt_comparator", ">" }, 
			{ "lt_comparator", "<" } 
		};
		assertValidValues("comparator", stringComparators);

		setValue("name", "warehouse.zoneNumber"); 
		assertValue("label", "Zone of Warehouse"); 
		assertValue("comparator", "eq_comparator"); 
		assertValidValues("comparator", numberComparators); 
		
		setValue("value", "1");
		execute("CustomReport.saveColumn");
		assertCollectionRowCount("columns", 3);
		assertValueInCollection("columns", 0, 0, "Name");
		assertValueInCollection("columns", 0, 1, "");
		assertValueInCollection("columns", 0, 2, "");
		assertValueInCollection("columns", 1, 0, "Number");
		assertValueInCollection("columns", 1, 1, "");
		assertValueInCollection("columns", 1, 2, "");		
		assertValueInCollection("columns", 2, 0, "Zone of Warehouse");
		assertValueInCollection("columns", 2, 1, "=");
		assertValueInCollection("columns", 2, 2, "1");
		
		execute("Collection.new", "viewObject=xava_view_columns");		
		setValue("name", "name");
		setValue("comparator", "eq_comparator"); 
		setValue("value", "UNO");
		execute("CustomReport.saveColumn");
		assertError("Column name not added to report. Already exists");
		assertCollectionRowCount("columns", 3);
		assertValueInCollection("columns", 0, 0, "Name");
		assertValueInCollection("columns", 0, 1, "");
		assertValueInCollection("columns", 0, 2, "");
		assertValueInCollection("columns", 1, 0, "Number");
		assertValueInCollection("columns", 1, 1, "");
		assertValueInCollection("columns", 1, 2, "");		
		assertValueInCollection("columns", 2, 0, "Zone of Warehouse");
		assertValueInCollection("columns", 2, 1, "=");
		assertValueInCollection("columns", 2, 2, "1");
				
		execute("CustomReport.editColumn", "row=0,viewObject=xava_view_columns");
		assertValue("name", "name");
		setValue("comparator", "starts_comparator"); 
		setValue("value", "c"); // In lowercase to verify that it's case insensitive
		execute("CustomReport.saveColumn");
		assertCollectionRowCount("columns", 3);
		assertValueInCollection("columns", 0, 0, "Name");
		assertValueInCollection("columns", 0, 1, "starts"); 
		assertValueInCollection("columns", 0, 2, "c");
		assertValueInCollection("columns", 1, 0, "Number");
		assertValueInCollection("columns", 1, 1, "");
		assertValueInCollection("columns", 1, 2, "");		
		assertValueInCollection("columns", 2, 0, "Zone of Warehouse");
		assertValueInCollection("columns", 2, 1, "=");
		assertValueInCollection("columns", 2, 2, "1");
		
						
		setValue("name", "jUnit Carrier report");
		execute("CustomReport.generatePdf");		
		
		assertPopupPDFLinesCount(5); // Instead of 9, because of warehouse.zoneNumber = 1 and name like 'c%' condition 
		assertPopupPDFLine(1, "jUnit Carrier report");
		assertPopupPDFLine(2, "Name Number Zone of Warehouse");  
		assertPopupPDFLine(3, "CUATRO 4 1");
		
		execute("ExtendedPrint.myReports");
		execute("CustomReport.remove", "xava.keyProperty=name");
	}
	
	public void testCustomReportWithHiddenProperties() throws Exception { 
		execute("ExtendedPrint.myReports");
		setValue("name", "Carriers of zone 2");
		assertCollectionRowCount("columns", 3);
		assertValueInCollection("columns", 0, 0, "Calculated");
		assertValueInCollection("columns", 1, 0, "Number");
		assertValueInCollection("columns", 2, 0, "Name");
		
		execute("Collection.new", "viewObject=xava_view_columns");
		setValue("name", "warehouse.zoneNumber");
		setValue("value", "2");
		setValue("hidden", "true");
		execute("CustomReport.saveColumn");

		assertCollectionRowCount("columns", 4);
		assertValueInCollection("columns", 0, 0, "Calculated");
		assertValueInCollection("columns", 1, 0, "Number");
		assertValueInCollection("columns", 2, 0, "Name");
		assertValueInCollection("columns", 3, 0, "Zone of Warehouse");
		assertValueInCollection("columns", 3, 1, "=");
		assertValueInCollection("columns", 3, 2, "2");
		assertValueInCollection("columns", 3, 5, "Yes");
		
		execute("CustomReport.columnUp", "row=3,viewObject=xava_view_columns");
		assertValueInCollection("columns", 0, 0, "Calculated");
		assertValueInCollection("columns", 1, 0, "Number");
		assertValueInCollection("columns", 2, 0, "Zone of Warehouse");
		assertValueInCollection("columns", 2, 1, "=");
		assertValueInCollection("columns", 2, 2, "2");
		assertValueInCollection("columns", 2, 5, "Yes");
		assertValueInCollection("columns", 3, 0, "Name");

		execute("CustomReport.generatePdf");

		assertPopupPDFLinesCount(5); // Instead of 9, because of warehouse.zoneNumber = 1 and name like 'c%' condition 
		assertPopupPDFLine(1, "Carriers of zone 2");
		assertPopupPDFLine(2, "Calculated Number Name");  
		assertPopupPDFLine(3, "TR 5 Cinco");
		
		execute("ExtendedPrint.myReports");
		assertCollectionRowCount("columns", 4);
		assertValueInCollection("columns", 0, 0, "Calculated");
		assertValueInCollection("columns", 1, 0, "Number");
		assertValueInCollection("columns", 2, 0, "Zone of Warehouse");
		assertValueInCollection("columns", 2, 1, "=");
		assertValueInCollection("columns", 2, 2, "2");
		assertValueInCollection("columns", 2, 5, "Yes");
		assertValueInCollection("columns", 3, 0, "Name");
		
		execute("CustomReport.editColumn", "row=2,viewObject=xava_view_columns");
		assertValue("hidden", "true");
		closeDialog();
		
		execute("CustomReport.generateExcel");
		assertContentTypeForPopup("text/x-csv");		
		StringTokenizer excel = new StringTokenizer(getPopupText(), "\n\r");
		String header = excel.nextToken();
		assertEquals("header", "Calculated;Number;Name", header);		
		String line1 = excel.nextToken();
		assertEquals("line1", "\"TR\";5;\"Cinco\"", line1);
		assertTrue("Only one line must be generated", !excel.hasMoreTokens());		

		execute("ExtendedPrint.myReports");
		execute("CustomReport.remove", "xava.keyProperty=name");		
	}
	
	public void testCustomReportFilteringByExactStringAndOrdering() throws Exception {
		execute("ExtendedPrint.myReports");
		assertValueInCollection("columns", 2, 0, "Name");
		execute("CustomReport.editColumn", "row=2,viewObject=xava_view_columns");
		setValue("comparator", "eq_comparator"); 
		setValue("value", "UNO");
		execute("CustomReport.saveColumn");
		assertValueInCollection("columns", 2, 0, "Name");
		assertValueInCollection("columns", 2, 1, "=");
		assertValueInCollection("columns", 2, 2, "UNO");
		execute("CustomReport.editColumn", "row=2,viewObject=xava_view_columns");
		assertValue("comparator", "eq_comparator"); 
		assertValue("value", "UNO");
		execute("CustomReport.saveColumn");
		assertValueInCollection("columns", 2, 0, "Name");
		assertValueInCollection("columns", 2, 1, "=");
		assertValueInCollection("columns", 2, 2, "UNO");		
		execute("CustomReport.generatePdf");		
		
		assertPopupPDFLinesCount(5); // Instead of 9, because of name = 'UNO' 
		assertPopupPDFLine(1, "Carrier report");
		assertPopupPDFLine(2, "Calculated Number Name");
		assertPopupPDFLine(3, "TR 1 UNO");
		
		execute("ExtendedPrint.myReports");
		execute("CustomReport.remove", "xava.keyProperty=name"); 		
		assertValueInCollection("columns", 1, 0, "Number");
		execute("CustomReport.editColumn", "row=1,viewObject=xava_view_columns");
		setValue("order", "1"); // DESCENDING
		execute("CustomReport.saveColumn");
		assertValueInCollection("columns", 1, 0, "Number");
		assertValueInCollection("columns", 1, 1, ""); 
		assertValueInCollection("columns", 1, 2, "");
		assertValueInCollection("columns", 1, 3, "Descending");
		execute("CustomReport.generatePdf");		
		
		assertPopupPDFLinesCount(9);  
		assertPopupPDFLine(1, "Carrier report");
		assertPopupPDFLine(2, "Calculated Number Name");
		assertPopupPDFLine(3, "TR 5 Cinco");
		assertPopupPDFLine(7, "TR 1 UNO");
		
		execute("ExtendedPrint.myReports");
		execute("CustomReport.generateExcel");
		assertContentTypeForPopup("text/x-csv");		
		StringTokenizer excel = new StringTokenizer(getPopupText(), "\n\r");
		String header = excel.nextToken();
		assertEquals("header", "Calculated;Number;Name", header);		
		String line1 = excel.nextToken();
		assertEquals("line1", "\"TR\";5;\"Cinco\"", line1);
		excel.nextToken(); excel.nextToken(); excel.nextToken();// Lines 2, 3, 4
		String line5 = excel.nextToken();
		assertEquals("line5", "\"TR\";1;\"UNO\"", line5);
		assertTrue("Only five lines must be generated", !excel.hasMoreTokens());		
		
		execute("ExtendedPrint.myReports"); 
		execute("CustomReport.remove", "xava.keyProperty=name"); 				
	}
	
	
	public void testStoringCustomReports() throws Exception { 
		execute("ExtendedPrint.myReports");
		assertValue("name", "Carrier report");
		assertCollectionRowCount("columns", 3);
		assertValueInCollection("columns", 0, 0, "Calculated");
		assertValueInCollection("columns", 1, 0, "Number");
		assertValueInCollection("columns", 2, 0, "Name");
		assertNoAction("CustomReport.createNew");
		assertAction("CustomReport.remove");
		setValue("name", "Carrier report NUMBER first");
		execute("CustomReport.columnUp", "row=1,viewObject=xava_view_columns");
		assertValueInCollection("columns", 0, 0, "Number");
		execute("CustomReport.generatePdf");
		
		execute("ExtendedPrint.myReports");
		assertValue("name", "Carrier report NUMBER first");
		String [][] customReports1 = {
			{ "Carrier report NUMBER first", "Carrier report NUMBER first" }			
		};
		assertValidValues("name", customReports1);
		assertCollectionRowCount("columns", 3);
		assertValueInCollection("columns", 0, 0, "Number");
		assertValueInCollection("columns", 1, 0, "Calculated");
		assertValueInCollection("columns", 2, 0, "Name");
		assertAction("CustomReport.createNew");
		assertAction("CustomReport.remove");
		execute("CustomReport.createNew", "xava.keyProperty=name");
		setValue("name", "Carrier report NAME first");
		execute("CustomReport.columnUp", "row=2,viewObject=xava_view_columns");
		execute("CustomReport.columnUp", "row=1,viewObject=xava_view_columns");
		assertValueInCollection("columns", 0, 0, "Name");
		execute("CustomReport.generatePdf");
		
		execute("ExtendedPrint.myReports");
		assertValue("name", "Carrier report NAME first");
		String [][] customReports2 = {
			{ "Carrier report NAME first", "Carrier report NAME first" },	
			{ "Carrier report NUMBER first", "Carrier report NUMBER first" }			
		};
		assertValidValues("name", customReports2);
		assertCollectionRowCount("columns", 3);
		assertValueInCollection("columns", 0, 0, "Name");
		assertValueInCollection("columns", 1, 0, "Calculated");
		assertValueInCollection("columns", 2, 0, "Number");
		assertAction("CustomReport.createNew");
		assertAction("CustomReport.remove");
		execute("CustomReport.createNew", "xava.keyProperty=name");
		setValue("name", "Carrier report With no CALCULATED");
		execute("CustomReport.removeColumn", "row=0,viewObject=xava_view_columns");
		execute("CustomReport.editColumn", "row=0,viewObject=xava_view_columns");
		setValue("comparator", "lt_comparator"); 
		setValue("value", "5");		
		execute("CustomReport.saveColumn");
		execute("CustomReport.editColumn", "row=1,viewObject=xava_view_columns");
		setValue("order", "1"); // DESCENDING
		execute("CustomReport.saveColumn");
		assertValueInCollection("columns", 0, 0, "Number");
		assertValueInCollection("columns", 0, 1, "<");
		assertValueInCollection("columns", 0, 2, "5");		
		assertValueInCollection("columns", 0, 3, "");
		assertValueInCollection("columns", 1, 0, "Name");
		assertValueInCollection("columns", 1, 1, "");
		assertValueInCollection("columns", 1, 2, "");
		assertValueInCollection("columns", 1, 3, "Descending");
		execute("CustomReport.generatePdf");		
		assertPopupPDFLinesCount(8);  
		assertPopupPDFLine(1, "Carrier report With no CALCULATED");		
		assertPopupPDFLine(2, "Number Name");
		assertPopupPDFLine(3, "1 UNO");
		assertPopupPDFLine(4, "3 TRES");
		assertPopupPDFLine(5, "2 DOS");
		assertPopupPDFLine(6, "4 CUATRO");				
		
		execute("ExtendedPrint.myReports");
		assertValue("name", "Carrier report With no CALCULATED"); 
		execute("CustomReport.generatePdf");
		assertPopupPDFLinesCount(8);  
		assertPopupPDFLine(1, "Carrier report With no CALCULATED");		
		assertPopupPDFLine(2, "Number Name");
		assertPopupPDFLine(3, "1 UNO");
		assertPopupPDFLine(4, "3 TRES");
		assertPopupPDFLine(5, "2 DOS");
		assertPopupPDFLine(6, "4 CUATRO");				
		execute("ExtendedPrint.myReports");
		assertValue("name", "Carrier report With no CALCULATED"); 
		String [][] customReports3 = {
			{ "Carrier report NAME first", "Carrier report NAME first" },	
			{ "Carrier report NUMBER first", "Carrier report NUMBER first" },
			{ "Carrier report With no CALCULATED", "Carrier report With no CALCULATED" }
		};
		assertValidValues("name", customReports3);
		assertCollectionRowCount("columns", 2);
		assertValueInCollection("columns", 0, 0, "Number");
		assertValueInCollection("columns", 0, 1, "<");
		assertValueInCollection("columns", 0, 2, "5");
		assertValueInCollection("columns", 0, 3, "");
		assertValueInCollection("columns", 1, 0, "Name");
		assertValueInCollection("columns", 1, 1, "");
		assertValueInCollection("columns", 1, 2, "");
		assertValueInCollection("columns", 1, 3, "Descending");
		
		setValue("name", "Carrier report NAME first");		
		assertCollectionRowCount("columns", 3);
		assertValueInCollection("columns", 0, 0, "Name");
		assertValueInCollection("columns", 1, 0, "Calculated");
		assertValueInCollection("columns", 2, 0, "Number");

		setValue("name", "Carrier report With no CALCULATED");
		assertValue("name", "Carrier report With no CALCULATED");
		assertCollectionRowCount("columns", 2);
		assertValueInCollection("columns", 0, 0, "Number");
		assertValueInCollection("columns", 0, 1, "<");
		assertValueInCollection("columns", 0, 2, "5");		
		assertValueInCollection("columns", 0, 3, "");
		assertValueInCollection("columns", 1, 0, "Name");
		assertValueInCollection("columns", 1, 1, "");
		assertValueInCollection("columns", 1, 2, "");
		assertValueInCollection("columns", 1, 3, "Descending");
		
		execute("CustomReport.remove", "xava.keyProperty=name"); 
		assertMessage("Report 'Carrier report With no CALCULATED' removed");		
		assertValidValuesCount("name", 2);
		assertValidValues("name", customReports2);
		assertValue("name", "Carrier report NAME first");
		assertCollectionRowCount("columns", 3);
		assertValueInCollection("columns", 0, 0, "Name");
		assertValueInCollection("columns", 1, 0, "Calculated");
		assertValueInCollection("columns", 2, 0, "Number");
		
		execute("CustomReport.remove", "xava.keyProperty=name");
		assertValidValuesCount("name", 1);
		assertValidValues("name", customReports1);
		assertValue("name", "Carrier report NUMBER first");
		assertValueInCollection("columns", 0, 0, "Number");
		assertAction("CustomReport.createNew");
		assertAction("CustomReport.remove");
				
		execute("CustomReport.remove", "xava.keyProperty=name");		
		assertCollectionRowCount("columns", 3);
		assertValueInCollection("columns", 0, 0, "Calculated");
		assertValueInCollection("columns", 1, 0, "Number");
		assertValueInCollection("columns", 2, 0, "Name");
		assertNoAction("CustomReport.createNew");
		assertAction("CustomReport.remove");
	}
	
	public void testRemoveReportInCustomReport() throws Exception { 
		execute("ExtendedPrint.myReports");
		setValue("name", "Carrier report 1");
		execute("CustomReport.generatePdf");		
		
		execute("ExtendedPrint.myReports");
		String [][] customReports1 = {
			{ "Carrier report 1", "Carrier report 1" },	
		};
		assertValidValues("name", customReports1);
		
		assertValue("name", "Carrier report 1");
		execute("CustomReport.createNew", "xava.keyProperty=name");
		setValue("name", "Carrier report 7");
		execute("CustomReport.generatePdf");
		execute("ExtendedPrint.myReports");
		String [][] customReports2 = {
			{ "Carrier report 1", "Carrier report 1" },	
			{ "Carrier report 7", "Carrier report 7" }
		};
		assertValidValues("name", customReports2);
		assertValue("name", "Carrier report 7");
		execute("CustomReport.createNew", "xava.keyProperty=name");
		setValue("name", "Carrier report 2");
		execute("CustomReport.remove", "xava.keyProperty=name");
		assertValue("name", "Carrier report 7"); // The last report generated
		execute("CustomReport.remove", "xava.keyProperty=name");
		assertValidValues("name", customReports1);
		assertValue("name", "Carrier report 1");
				
		execute("CustomReport.createNew", "xava.keyProperty=name");
		setValue("name", "Carrier report 2");
		execute("CustomReport.remove", "xava.keyProperty=name");
		assertMessage("Report 'Carrier report 2' removed");  
		assertValue("name", "Carrier report 1");
		assertValidValues("name", customReports1);
		
		execute("CustomReport.remove", "xava.keyProperty=name");
		assertValue("name", "Carrier report");
		setValue("name", "Carrier report NUEVO");
		assertValueInCollection("columns", 0, 0, "Calculated");
		execute("CustomReport.columnUp", "row=1,viewObject=xava_view_columns");
		assertValueInCollection("columns", 0, 0, "Number");		
		
		execute("CustomReport.remove", "xava.keyProperty=name");	
		assertMessage("Report 'Carrier report NUEVO' removed"); 
		assertValue("name", "Carrier report");
		assertValueInCollection("columns", 0, 0, "Calculated");
		
		setValue("name", "Carrier report 1");
		execute("CustomReport.generatePdf");
		assertNoErrors();
		
		execute("ExtendedPrint.myReports");
		assertValue("name", "Carrier report 1");
		assertValueInCollection("columns", 0, 0, "Calculated");
		execute("CustomReport.columnUp", "row=1,viewObject=xava_view_columns");
		assertValueInCollection("columns", 0, 0, "Number");
		execute("CustomReport.remove", "xava.keyProperty=name");
		assertMessage("Report 'Carrier report 1' removed"); 		
	}
	
	public void testRemoveColumnsInCustomReport() throws Exception  { 
		execute("ExtendedPrint.myReports"); 
		assertCollectionRowCount("columns", 3);
		assertValueInCollection("columns", 0, 0, "Calculated");
		assertValueInCollection("columns", 1, 0, "Number");
		assertValueInCollection("columns", 2, 0, "Name");
		checkRowCollection("columns", 1);
		execute("CustomReport.removeColumn", "viewObject=xava_view_columns");
		assertNoErrors();
		assertCollectionRowCount("columns", 2);
		assertValueInCollection("columns", 0, 0, "Calculated");
		assertValueInCollection("columns", 1, 0, "Name");
		
		execute("CustomReport.generatePdf");
		execute("ExtendedPrint.myReports");
		assertCollectionRowCount("columns", 2);
		assertValueInCollection("columns", 0, 0, "Calculated");
		assertValueInCollection("columns", 1, 0, "Name");		
				
		execute("CustomReport.removeColumn", "row=0,viewObject=xava_view_columns");
		assertNoErrors();
		assertCollectionRowCount("columns", 1);
		assertValueInCollection("columns", 0, 0, "Name");
		
		execute("CustomReport.generatePdf");
		execute("ExtendedPrint.myReports");
		assertCollectionRowCount("columns", 1);
		assertValueInCollection("columns", 0, 0, "Name");
		execute("CustomReport.remove", "xava.keyProperty=name");
	}

		
	public void testCarrierSelected() throws Exception{
		assertListNotEmpty();
		execute("List.viewDetail", "row=0");
		execute("CarrierWithSections.fellowCarriersSelected");
		assertTrue(Is.empty(getValue("fellowCarriersSelected")));
		execute("Sections.change", "activeSection=1");
		checkRowCollection("fellowCarriersCalculated", 0);
		checkRowCollection("fellowCarriersCalculated", 1);
		execute("Sections.change", "activeSection=0");
		execute("CarrierWithSections.fellowCarriersSelected");
		assertTrue(getValue("fellowCarriersSelected").equalsIgnoreCase("DOS TRES")); 
		execute("Mode.list");
		execute("List.viewDetail", "row=0");
		execute("CarrierWithSections.fellowCarriersSelected");
		assertTrue(Is.empty(getValue("fellowCarriersSelected")));
	}
	
	public void testSetControllers() throws Exception {
		String [] defaultActions = {
			"List.hideRows",
			"List.filter",
			"List.customize",
			"List.orderBy",
			"List.viewDetail",
			"List.sumColumn",
			"Mode.detailAndFirst",			
			"Mode.split",
			"Print.generatePdf",
			"Print.generateExcel",
			"ExtendedPrint.myReports",
			"CRUD.new",
			"CRUD.deleteSelected",
			"CRUD.deleteRow", 
			"Carrier.translateAll",
			"Carrier.deleteAll",
			"CarrierWithSections.setTypicalController",
			"CarrierWithSections.setPrintController",
			"CarrierWithSections.setDefaultControllers",
			"CarrierWithSections.returnToPreviousControllers"
		};
		String [] printActions = {
			"List.hideRows",
			"List.filter",
			"List.customize",
			"List.orderBy",
			"List.viewDetail",
			"List.sumColumn",
			"Mode.detailAndFirst",							
			"Mode.split",
			"Print.generatePdf",
			"Print.generateExcel",			
			"CarrierWithSections.setTypicalController",
			"CarrierWithSections.setPrintController",
			"CarrierWithSections.setDefaultControllers",
			"CarrierWithSections.returnToPreviousControllers"			
		};		
		String [] typicalActions = {
			"List.hideRows",
			"List.filter",
			"List.customize",
			"List.orderBy",
			"List.viewDetail",
			"List.sumColumn", 
			"Mode.detailAndFirst",							
			"Mode.split",
			"Print.generatePdf",
			"Print.generateExcel",
			"ExtendedPrint.myReports",
			"CRUD.new",
			"CRUD.deleteSelected",
			"CRUD.deleteRow", 
			"CarrierWithSections.setTypicalController",
			"CarrierWithSections.setPrintController",
			"CarrierWithSections.setDefaultControllers",
			"CarrierWithSections.returnToPreviousControllers"			
		};		
		
		// Returning with returnToPreviousController
		assertActions(defaultActions); 
		execute("CarrierWithSections.setTypicalController");
		assertActions(typicalActions);
		execute("CarrierWithSections.setPrintController");
		assertActions(printActions);
		execute("CarrierWithSections.returnToPreviousControllers");
		assertActions(typicalActions);
		execute("CarrierWithSections.returnToPreviousControllers");
		assertActions(defaultActions);
		
		// Returning with setDefaultControllers()
		assertActions(defaultActions);
		execute("CarrierWithSections.setTypicalController");
		assertActions(typicalActions);
		execute("CarrierWithSections.setPrintController");
		assertActions(printActions);
		execute("CarrierWithSections.setDefaultControllers");
		assertActions(defaultActions);
		execute("CarrierWithSections.returnToPreviousControllers");
		assertActions(defaultActions); // Verifies that setDefaultControllers empties the stacks
		
	}
	
}
