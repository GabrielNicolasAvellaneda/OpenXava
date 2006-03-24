package org.openxava.test.tests;

/**
 * @author Javier Paniza
 */

public class CustomersWithSectionTest extends CustomersTest {

	private String [] listActions = {
			"Print.generatePdf",
			"Print.generateExcel",
			"CRUD.new",
			"CRUD.deleteSelected",
			"Mode.detailAndFirst",
			"List.filter",
			"List.orderBy",
			"List.viewDetail",
			"List.customize",
			"List.hideRows",
			"Customers.hideSellerInList",
			"Customers.showSellerInList"
		};

	private String [] listCustomizeActions = {
			"Print.generatePdf",
			"Print.generateExcel",
			"CRUD.new",
			"CRUD.deleteSelected",
			"Mode.detailAndFirst",
			"List.filter",
			"List.orderBy",
			"List.viewDetail",
			"List.customize",
			"List.addColumns",
			"List.moveColumnToLeft",
			"List.moveColumnToRight",
			"List.removeColumn",
			"List.hideRows",
			"Customers.hideSellerInList",
			"Customers.showSellerInList"
		};
	

	public CustomersWithSectionTest(String testName) {
		super(testName, "CustomersWithSection", true);		
	}
	
	public void testChangeReferenceLabel() throws Exception {
		execute("CRUD.new");
		assertLabel("alternateSeller", "Alternate seller");
		execute("Customers.changeAlternateSellerLabel");
		assertLabel("alternateSeller", "Secondary seller");
	}
	
	public void testCustomizeList() throws Exception { 
		doTestCustomizeList_moveAndRemove();
		tearDown();	setUp();
		doTestCustomizeList_generatePDF();
		tearDown();	setUp();
		doTestRestoreColumns_addRemoveTabColumnsDynamically();
	}
	
	private void doTestCustomizeList_moveAndRemove() throws Exception {
		assertActions(listActions);
		execute("List.customize");		
		assertActions(listCustomizeActions);

		assertListColumnCount(6);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "Seller");
		assertLabelInList(3, "City");
		assertLabelInList(4, "Seller level");
		assertLabelInList(5, "State");
		assertTrue("It is needed customers for execute this test", getListRowCount() > 1);
		String name = getValueInList(0, 0);
		String type = getValueInList(0, 1);
		String seller = getValueInList(0, 2);
		String city = getValueInList(0, 3);
		String sellerLevel = getValueInList(0, 4);
		String state = getValueInList(0, 5); 

		// move 2 to 3
		execute("List.moveColumnToRight", "columnIndex=2");
		assertNoErrors();
		assertListColumnCount(6);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "City");
		assertLabelInList(3, "Seller");
		assertLabelInList(4, "Seller level");
		assertLabelInList(5, "State"); 
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, city);
		assertValueInList(0, 3, seller);
		assertValueInList(0, 4, sellerLevel);
		assertValueInList(0, 5, state); 
		
		// try to move 5, it is the last, do nothing
		execute("List.moveColumnToRight", "columnIndex=5");
		assertNoErrors();
		assertListColumnCount(6);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "City");
		assertLabelInList(3, "Seller");
		assertLabelInList(4, "Seller level");
		assertLabelInList(5, "State"); 
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, city);
		assertValueInList(0, 3, seller);
		assertValueInList(0, 4, sellerLevel);
		assertValueInList(0, 5, state); 
		
		// move 3 to 2
		execute("List.moveColumnToLeft", "columnIndex=3");
		assertNoErrors();
		assertListColumnCount(6);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "Seller");
		assertLabelInList(3, "City");
		assertLabelInList(4, "Seller level");
		assertLabelInList(5, "State"); 
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, seller);
		assertValueInList(0, 3, city);
		assertValueInList(0, 4, sellerLevel);
		assertValueInList(0, 5, state); 
		
		// try to move 0 to left, do nothing
		execute("List.moveColumnToLeft", "columnIndex=0");
		assertNoErrors();
		assertListColumnCount(6);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "Seller");
		assertLabelInList(3, "City");
		assertLabelInList(4, "Seller level");
		assertLabelInList(5, "State"); 
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, seller);
		assertValueInList(0, 3, city);
		assertValueInList(0, 4, sellerLevel);
		assertValueInList(0, 5, state); 

		// remove column 3
		execute("List.removeColumn", "columnIndex=3");
		assertNoErrors();
		assertListColumnCount(5);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");		
		assertLabelInList(2, "Seller");
		assertLabelInList(3, "Seller level");
		assertLabelInList(4, "State"); 
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, seller);
		assertValueInList(0, 3, sellerLevel);
		assertValueInList(0, 4, state); 
						
		execute("List.customize");
		assertActions(listActions);
	}
	
	private void doTestCustomizeList_generatePDF() throws Exception {
		// Trusts in that testCustomizeList_moveAndRemove is executed before
		execute("List.customize");
		assertListColumnCount(5);
		execute("List.removeColumn", "columnIndex=3");
		assertNoErrors();
		assertListColumnCount(4);		
		execute("Print.generatePdf"); 
		assertContentTypeForPopup("application/pdf");
		
	}
		
	private void doTestRestoreColumns_addRemoveTabColumnsDynamically() throws Exception { 
		// Restoring initial tab setup
		execute("List.customize");
		execute("List.addColumns");							
		execute("AddColumns.restoreDefault");		
		// End restoring
		
		assertListColumnCount(6);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "Seller");
		assertLabelInList(3, "City");
		assertLabelInList(4, "Seller level");
		assertLabelInList(5, "State"); 
		assertTrue("Must to have customers for run this test", getListRowCount() > 1);
		String name = getValueInList(0, 0);
		String type = getValueInList(0, 1);
		String seller = getValueInList(0, 2);
		String city = getValueInList(0, 3);
		String sellerLevel = getValueInList(0, 4);
		String state = getValueInList(0, 5); 
		
		execute("Customers.hideSellerInList");
		assertNoErrors();
		assertListColumnCount(5);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "City");
		assertLabelInList(3, "Seller level");
		assertLabelInList(4, "State"); 
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, city);
		assertValueInList(0, 3, sellerLevel);
		assertValueInList(0, 4, state); 
		
		execute("Customers.showSellerInList");
		assertNoErrors();
		assertListColumnCount(6);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "Seller");		
		assertLabelInList(3, "City");
		assertLabelInList(4, "Seller level");
		assertLabelInList(5, "State"); 
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, seller);
		assertValueInList(0, 3, city);
		assertValueInList(0, 4, sellerLevel);
		assertValueInList(0, 5, state); 
	}
	
	public void testCustomizeList_addAndResetModule() throws Exception { 
		assertListColumnCount(6);
		String value = getValueInList(0, 0);
		execute("List.customize");
		execute("List.addColumns");		
		checkRow("selectedProperties", "number"); 		
		execute("AddColumns.addColumns");
		assertListColumnCount(7);
		assertValueInList(0, 0, value);
				
		resetModule();
		assertListColumnCount(7);
		assertValueInList(0, 0, value);
		
		execute("List.customize");
		execute("List.removeColumn", "columnIndex=6");
		assertListColumnCount(6);
	}
	
	public void testRowStyle() throws Exception {
		int c = getListRowCount();
		boolean found = false;
		for (int i=0; i<c; i++) {
			String type = getValueInList(i, "type");
			if ("Steady".equals(type)) {				
				assertRowStyleInList(i, "highlight");
				found = true;
			}
			else {
				assertNoRowStyleInList(i);
			}						
		}
		if (!found) {
			fail("It is required at least one Customer of 'Steady' type for run this test");
		}
	}
	
}
