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
			"Customers.hideSellerInList",
			"Customers.showSellerInList"
		};
	

	public CustomersWithSectionTest(String testName) {
		super(testName, "CustomersWithSection", true);		
	}
	
	public void testCustomizeList_moveAndRemove() throws Exception {
		assertActions(listActions);
		execute("List.customize");		
		assertActions(listCustomizeActions);

		assertListColumnCount(5);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "Seller");
		assertLabelInList(3, "City");
		assertLabelInList(4, "Seller level");
		assertTrue("It is needed customers for execute this test", getListRowCount() > 1);
		String name = getValueInList(0, 0);
		String type = getValueInList(0, 1);
		String seller = getValueInList(0, 2);
		String city = getValueInList(0, 3);
		String sellerLevel = getValueInList(0, 4);

		// move 2 to 3
		execute("List.moveColumnToRight", "columnIndex=2");
		assertNoErrors();
		assertListColumnCount(5);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "City");
		assertLabelInList(3, "Seller");
		assertLabelInList(4, "Seller level");
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, city);
		assertValueInList(0, 3, seller);
		assertValueInList(0, 4, sellerLevel);
		
		// try to move 4, it is the last, do nothing
		execute("List.moveColumnToRight", "columnIndex=4");
		assertNoErrors();
		assertListColumnCount(5);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "City");
		assertLabelInList(3, "Seller");
		assertLabelInList(4, "Seller level");
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, city);
		assertValueInList(0, 3, seller);
		assertValueInList(0, 4, sellerLevel);
		
		// move 3 to 2
		execute("List.moveColumnToLeft", "columnIndex=3");
		assertNoErrors();
		assertListColumnCount(5);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "Seller");
		assertLabelInList(3, "City");
		assertLabelInList(4, "Seller level");
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, seller);
		assertValueInList(0, 3, city);
		assertValueInList(0, 4, sellerLevel);
		
		// try to move 0 to left, do nothing
		execute("List.moveColumnToLeft", "columnIndex=0");
		assertNoErrors();
		assertListColumnCount(5);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "Seller");
		assertLabelInList(3, "City");
		assertLabelInList(4, "Seller level");
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, seller);
		assertValueInList(0, 3, city);
		assertValueInList(0, 4, sellerLevel);

		// remove column 3
		execute("List.removeColumn", "columnIndex=3");
		assertNoErrors();
		assertListColumnCount(4);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");		
		assertLabelInList(2, "Seller");
		assertLabelInList(3, "Seller level");
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, seller);
		assertValueInList(0, 3, sellerLevel);
						
		execute("List.customize");
		assertActions(listActions);
	}
	
	public void testCustomizeList_generatePDF() throws Exception {
		// Trust in that testCustomizeList_moveAndRemove is executed before
		execute("List.customize");
		assertListColumnCount(4);
		execute("List.removeColumn", "columnIndex=3");
		assertNoErrors();
		assertListColumnCount(3);		
		execute("Print.generatePdf"); 
		assertContentTypeForPopup("application/pdf");				
	}
		
	public void testAddRemoveTabColumnsDynamically() throws Exception {	
		// Restoring initial tab setup
		execute("List.customize");
		execute("List.addColumns");		
		checkRow("selectedProperties", "address.city");				
		execute("AddColumns.addColumns");
		execute("List.addColumns");
		checkRow("selectedProperties", "seller.level.description");
		execute("AddColumns.addColumns");
		// End restoring
		
		assertListColumnCount(5);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "Seller");
		assertLabelInList(3, "City");
		assertLabelInList(4, "Seller level");
		assertTrue("Must to have customers for run this test", getListRowCount() > 1);
		String name = getValueInList(0, 0);
		String type = getValueInList(0, 1);
		String seller = getValueInList(0, 2);
		String city = getValueInList(0, 3);
		String sellerLevel = getValueInList(0, 4);
		
		execute("Customers.hideSellerInList");
		assertNoErrors();
		assertListColumnCount(4);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "City");
		assertLabelInList(3, "Seller level");
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, city);
		assertValueInList(0, 3, sellerLevel);
		
		execute("Customers.showSellerInList");
		assertNoErrors();
		assertListColumnCount(5);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Type");
		assertLabelInList(2, "Seller");		
		assertLabelInList(3, "City");
		assertLabelInList(4, "Seller level");
		assertValueInList(0, 0, name);
		assertValueInList(0, 1, type);
		assertValueInList(0, 2, seller);
		assertValueInList(0, 3, city);
		assertValueInList(0, 4, sellerLevel);
	}
	
	public void testCustomizeList_addAndResetModule() throws Exception {
		assertListColumnCount(5);
		String value = getValueInList(0, 0);
		execute("List.customize");
		execute("List.addColumns");		
		checkRow("selectedProperties", "number"); 		
		execute("AddColumns.addColumns");
		assertListColumnCount(6);
		assertValueInList(0, 0, value);
				
		resetModule();
		assertListColumnCount(6);
		assertValueInList(0, 0, value);
		
		execute("List.customize");
		execute("List.removeColumn", "columnIndex=5");
		assertListColumnCount(5);
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
