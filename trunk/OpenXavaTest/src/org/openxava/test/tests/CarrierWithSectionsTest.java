package org.openxava.test.tests;

import org.openxava.tests.ModuleTestBase;
import org.openxava.util.Is;

/**
 * Create on 07/04/2008 (12:16:03)
 * @autor Ana Andrés
 */
public class CarrierWithSectionsTest extends ModuleTestBase {
	
	public CarrierWithSectionsTest(String testName) {
		super(testName, "CarrierWithSections");
	}
	
	public void testCustomReport() throws Exception { // tmp
		execute("ExtendedPrint.customReport");
		assertValue("reportName", "Carrier report");
		assertCollectionRowCount("columns", 3);
		assertValueInCollection("columns", 0, 0, "calculated");
		assertValueInCollection("columns", 1, 0, "number");
		assertValueInCollection("columns", 2, 0, "name");

		execute("CustomReport.columnUp", "row=2,viewObject=xava_view_columns");
		assertValueInCollection("columns", 0, 0, "calculated");
		assertValueInCollection("columns", 1, 0, "name");
		assertValueInCollection("columns", 2, 0, "number");
		
		execute("CustomReport.columnDown", "row=0,viewObject=xava_view_columns");
		assertValueInCollection("columns", 0, 0, "name");
		assertValueInCollection("columns", 1, 0, "calculated");
		assertValueInCollection("columns", 2, 0, "number");
		
		execute("CustomReport.removeColumn", "row=1,viewObject=xava_view_columns");
		assertValueInCollection("columns", 0, 0, "name");
		assertValueInCollection("columns", 1, 0, "number");
		
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
		assertValidValues("columnName", validColumnNames);
		assertValue("columnName", "");
		String [][] emptyComparators = {
			{ "", "" }
		};
		assertValidValues("comparator", emptyComparators);
		setValue("columnName", "name");
		String [][] stringComparators = {
			{ "", "" },	
			{ "starts_comparator", "starts" },
			{ "contains_comparator", "contains" },
			{ "not_contains_comparator", "not contains" },
			{ "eq", "=" },
			{ "ne", "<>" },  
			{ "ge", ">=" }, 
			{ "le", "<=" }, 
			{ "gt", ">" }, 
			{ "lt", "<" } 
		};
		assertValidValues("comparator", stringComparators);
		setValue("columnName", "warehouse.zoneNumber");
		String [][] numberComparators = {
			{ "", "" },	
			{ "eq", "=" },
			{ "ne", "<>" },  
			{ "ge", ">=" }, 
			{ "le", "<=" }, 
			{ "gt", ">" }, 
			{ "lt", "<" } 
		};
		assertValidValues("comparator", numberComparators);		
		setValue("comparator", "eq"); 
		setValue("value", "1");
		execute("CustomReport.saveColumn");
		assertCollectionRowCount("columns", 3);
		assertValueInCollection("columns", 0, 0, "name");
		assertValueInCollection("columns", 0, 1, "");
		assertValueInCollection("columns", 0, 2, "");
		assertValueInCollection("columns", 1, 0, "number");
		assertValueInCollection("columns", 1, 1, "");
		assertValueInCollection("columns", 1, 2, "");		
		assertValueInCollection("columns", 2, 0, "warehouse.zoneNumber");
		assertValueInCollection("columns", 2, 1, "=");
		assertValueInCollection("columns", 2, 2, "1");
		
		execute("CustomReport.editColumn", "row=0,viewObject=xava_view_columns");
		assertValue("columnName", "name");
		setValue("comparator", "starts_comparator"); 
		setValue("value", "C");
		execute("CustomReport.saveColumn");
		assertCollectionRowCount("columns", 3);
		assertValueInCollection("columns", 0, 0, "name");
		assertValueInCollection("columns", 0, 1, "starts with");
		assertValueInCollection("columns", 0, 2, "C");
		assertValueInCollection("columns", 1, 0, "number");
		assertValueInCollection("columns", 1, 1, "");
		assertValueInCollection("columns", 1, 2, "");		
		assertValueInCollection("columns", 2, 0, "warehouse.zoneNumber");
		assertValueInCollection("columns", 2, 1, "=");
		assertValueInCollection("columns", 2, 2, "1");
		
						
		setValue("reportName", "jUnit Carrier report");
		execute("CustomReport.generatePdf");		
		
		printPopupPDFAsText(); // tmp
		assertPopupPDFLinesCount(5); // Instead of 9, because of warehouse.zoneNumber = 1 and name like 'C%' condition 
		assertPopupPDFLine(1, "jUnit Carrier report");
		assertPopupPDFLine(2, "Name Number Zone");
		assertPopupPDFLine(3, "CUATRO 4 1");
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
			"ExtendedPrint.customReport",
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
			"ExtendedPrint.customReport",
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
