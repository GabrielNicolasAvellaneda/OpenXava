package org.openxava.test.tests;

import org.openxava.tests.*;

/**
 * @author Javier Paniza
 */

public class Invoices2002Test extends ModuleTestBase {
	
	
	private String [] listActions = {
		"Print.generatePdf",
		"Print.generateExcel",
		"CRUD.new",
		"CRUD.deleteSelected",
		"Mode.detailAndFirst",
		"List.filter",
		"List.customize",
		"List.orderBy",
		"List.viewDetail",
		"Invoices2002.changeListTitle"
	};

	public Invoices2002Test(String testName) {
		super(testName, "OpenXavaTest", "Invoices2002");		
	}
	
	public void testGeneratePdfWithFilter() throws Exception {
		execute("Print.generatePdf");		
		assertContentTypeForPopup("application/pdf");		
	}
	
	public void testChangeTabTitle() throws Exception {
		assertListTitle("Invoices report of year 2,002");
		execute("Invoices2002.changeListTitle");
		assertListTitle("The little invoices of 2002");
	}
	
	public void testCustomizeListWithFilterAndBaseCondition() throws Exception {
		assertValueInList(0, 0, "2002");
		execute("List.customize");
		execute("List.moveColumnToRight", "columnIndex=0");
		assertValueInList(0, 1, "2002");		
		// Restoring
		execute("List.moveColumnToLeft", "columnIndex=1");
	}

	public void testFilterWithConverterAndFilter() throws Exception {
		assertNoErrors();
		String [] comparators = { "=", "=", "=", "=", "="};
		String [] condition = { "", "", "", "", "true"	};
		setConditionComparators(comparators);
		setConditionValues(condition);
		execute("List.filter");
		assertNoErrors();
	}	
	
	public void testOnInitAction_IRequestFilter_BaseContextFilter() throws Exception {
		assertActions(listActions);		
		assertListTitle("Invoices report of year 2,002");
		int cantidad = getListRowCount();
		for (int i = 0; i < cantidad; i++) {
			assertValueInList(i, "year", "2002");	
		}		
	}
	
	public void testCalculatedPropertiesInListMode() throws Exception {
		assertActions(listActions);
		int rowCount = getListRowCount();
		for (int i = 0; i < rowCount; i++) {
			String number = getValueInList(i, "number");
			if ("1".equals(number)) {
				assertValueInList(i, "amountsSum", "2,500");
				assertValueInList(i, "vat", "400");
				assertValueInList(i, "detailsCount", "2");
				assertValueInList(i, "importance", "NORMAL"); // UpperCaseFormatter is used
				return;
			}			
		}		
		fail("It must to exists invoice 2002/1 for run this test");
	}	
							
}
