package org.openxava.test.tests;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.tests.ModuleTestBase;

/**
 * @author Javier Paniza
 */

public class SubfamilyTest extends ModuleTestBase {
	private static Log log = LogFactory.getLog(SubfamilyTest.class);
	
	public SubfamilyTest(String testName) {
		super(testName, "Subfamily");		
	}
	
	public void testMemoFormatterInList() throws Exception {
		assertLabelInList(3, "Remarks");
		assertValueInList(0, 3, "DESARROLLO DE SOFTWARE");
		assertValueInList(1, 3, "GESTION INTEGRAL DE RA...");	// list-formatter with set
		execute("List.viewDetail", "row=1");
		assertValue("remarks", "GESTION INTEGRAL DE RAYOS DE LUNAS SOBRE LOS MARES QUE PASAN POR EL ABISMO DE MIS MISERIAS. ME ALIMENTARA Y FORTALECIERA. DISOLVIENDO EL DINERAL COMO UN VIRUS QUE SE EXTIENDE Y SE CONTAGIA.");
	}
	
	public void testSaveHiddenKeyWithSections() throws Exception {
		assertTrue("For this test is required al least 2 families", getListColumnCount() >= 2);
		execute("Mode.detailAndFirst");
		assertNoErrors();
		execute("CRUD.save");
		assertNoErrors();
	}	
	
	public void testFilledWithZeros() throws Exception {
		String formattedNumber = "002";		
		String[] condition = {"2"};
		setConditionValues(condition);
		execute("List.filter");
		assertListRowCount(1);
		assertNoErrors();
		execute("List.viewDetail", "row=0");
		assertValue("number", formattedNumber);
		assertNoErrors();
	}
	
	public void testNavigateHiddenKeyWithSections() throws Exception {
		assertTrue("For this test is required al least 2 families", getListColumnCount() >= 2);
		execute("Mode.detailAndFirst");
		assertNoErrors();
		execute("Navigation.next");
		assertNoErrors();
	}
	
	public void testPropertiesTabByDefault() throws Exception {
		assertLabelInList(0, "Number");
		assertLabelInList(1, "Family");
		assertLabelInList(2, "Description");		
	}
	
							
}
