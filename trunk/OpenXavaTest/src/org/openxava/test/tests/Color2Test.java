package org.openxava.test.tests;

import org.openxava.tests.*;


/**
 * @author Javier Paniza
 */

public class Color2Test extends ModuleTestBase {
	
	public Color2Test(String testName) {
		super(testName, "Color2");		
	}
	
	public void testDescriptionsListInMyReport() throws Exception { 
		execute("ExtendedPrint.myReports");
		assertValueInCollection("columns", 4, 0, "Name of Used to");
		execute("MyReport.editColumn", "row=4,viewObject=xava_view_columns");
		assertNotExists("comparator");
		assertNotExists("value");
		assertNotExists("booleanValue");
		assertNotExists("validValuesValue"); 
		assertExists("descriptionsListValue"); 
		assertExists("order");
		
		setValue("name", "number");
		assertExists("comparator");
		assertExists("value");
		assertNotExists("booleanValue");
		assertNotExists("validValuesValue"); 
		assertNotExists("descriptionsListValue"); 
		assertExists("order");
		
		setValue("name", "usedTo.name");
		assertNotExists("comparator");
		assertNotExists("value");
		assertNotExists("booleanValue");
		assertNotExists("validValuesValue"); 
		assertExists("descriptionsListValue"); 
		assertExists("order");

		String [][] validValues = {
			{ "", "" },
			{ "1::CAR", "CAR" },
			{ "3::DOOR", "DOOR" },
			{ "0::HOUSE", "HOUSE" },
			{ "2::LAMPPOST", "LAMPPOST" }
		};
		
		assertValidValues("descriptionsListValue", validValues);
		assertValue("descriptionsListValue", "");
		setValue("descriptionsListValue", "1::CAR");
		execute("MyReport.saveColumn");
		assertValueInCollection("columns", 4, 2, "CAR");
		
		execute("MyReport.editColumn", "row=4,viewObject=xava_view_columns");
		assertValue("descriptionsListValue", "1::CAR");		
		closeDialog();
		
		execute("MyReport.generatePdf");		
		assertPopupPDFLinesCount(5);  
		assertPopupPDFLine(3, "0 ROJO FF0000 RED CAR 3 PLACES");
		
		execute("ExtendedPrint.myReports");
		assertValueInCollection("columns", 4, 0, "Name of Used to");
		assertValueInCollection("columns", 4, 2, "CAR");
		execute("MyReport.editColumn", "row=4,viewObject=xava_view_columns");
		assertValue("descriptionsListValue", "1::CAR");		
		closeDialog();
		
		execute("MyReport.remove", "xava.keyProperty=name");				
	}
	
	public void testFilterDescriptionsList_keyReferenceWithSameNameThatPropertyFather() throws Exception{ 
		assertLabelInList(4, "Name of Used to");
		assertValueInList(0, 4, "CAR");
		setConditionValues(new String[] { "", "", "", "1"} );
		// execute("List.filter"); // Not needed because filterOnChange=true
		assertListRowCount(1); 
	}
		
}