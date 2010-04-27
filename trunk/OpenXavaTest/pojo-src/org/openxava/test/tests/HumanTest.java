package org.openxava.test.tests;

import org.openxava.tests.*;
import org.openxava.util.*;

/**
 * 
 * @author Javier Paniza
 */

public class HumanTest extends ModuleTestBase {
	
	public HumanTest(String testName) {
		super(testName, "Human");		
	}
	
	public void testMappedSuperclassCRUD() throws Exception { 
		execute("CRUD.new");
		setValue("name", "JUNIT HUMAN");
		setValue("sex", "0");
		execute("CRUD.save");
		assertNoErrors();
		assertValue("name", "");
		assertValue("sex", "");

		setValue("name", "JUNIT HUMAN");
		execute("CRUD.refresh");

		assertValue("name", "JUNIT HUMAN");
		assertValue("sex", "0");
		execute("CRUD.delete");
		assertMessage("Human deleted successfully");
	}
	
	public void testEnumeratedString() throws Exception {
		assertLabelInList(1, "Sex");
		int blanks = 0;
		int males = 0;
		int females = 0;
		int rowCount = getListRowCount();
		for (int row=0; row<rowCount; row++) {
			String sex = getValueInList(row, "sex");
			if (Is.emptyString(sex)) blanks++;
			else if ("Male".equals(sex)) males++;
			else if ("Female".equals(sex)) females++;
			else fail("Only '','Male' or 'Female' are allowed");
		}
		if (males == 0) fail("Male humans are required");
		if (females == 0) fail("Female humans are required");

		String [] malesCondition = { " ", "0" };		
		setConditionValues(malesCondition);
		execute("List.filter");
		assertListRowCount(males);
		for (int row=0; row<males; row++) {
			assertValueInList(row, "sex", "Male");
		}		
		
		String [] femalesCondition = { " ", "1" };		
		setConditionValues(femalesCondition);
		execute("List.filter");
		assertListRowCount(females);		
		for (int row=0; row<females; row++) {
			assertValueInList(row, "sex", "Female");
		}		
		
	}
	
	public void testBaseEntityWithChildrenList_polymorphicView() throws Exception {
		assertListColumnCount(2);
		assertLabelInList(0, "Name");
		assertLabelInList(1, "Sex");
		
		assertListRowCount(6);
		assertValueInList(0, 0, "PEPE");
		assertValueInList(1, 0, "JUANA");
		assertValueInList(2, 0, "JAVI");
		assertValueInList(3, 0, "JUANJO");		
		assertValueInList(4, 0, "DANI");
		assertValueInList(5, 0, "HOUSE");
				
		execute("Mode.detailAndFirst");
		assertValue("name", "PEPE");
		assertHumanView();
		
		execute("Navigation.next");
		assertValue("name", "JUANA");
		assertHumanView();
		
		execute("Navigation.next");
		assertJavaProgrammerView();
		assertValue("name", "JAVI");
		
		execute("Navigation.next");
		assertProgrammerView();
		assertValue("name", "JUANJO");
				
		execute("Navigation.next");
		assertJavaProgrammerView();
		assertValue("name", "DANI");
				
		execute("Navigation.next");
		assertDoctorView();
		assertValue("name", "HOUSE");				
	}
	
	public void testPolymorphicSearchByAnyProperty() throws Exception {
		execute("CRUD.new");
		assertHumanView();
		setValue("name", "HOUSE");
		execute("CRUD.refresh");
		assertDoctorView();		
		assertValue("name", "HOUSE");
		assertValue("sex", "0");
		assertValue("currentHospital", "LA FE");
		
		execute("CRUD.new");		
		assertHumanView();
		setValue("name", "JAVI");
		execute("CRUD.refresh");
		assertJavaProgrammerView();		
		assertValue("name", "JAVI");
		assertValue("sex", "0");
		assertValue("mainLanguage", "JAVA");
		assertValue("favouriteFramework", "OPENXAVA");
	}

	private void assertHumanView() throws Exception {
		setModel("Human");
		assertExists("name");
		assertExists("sex");		
		assertNotExists("currentHospital");
		assertNotExists("mainLanguage");
		assertNotExists("favouriteFramework");		
	}
	
	private void assertProgrammerView() throws Exception {
		setModel("Programmer");
		assertExists("name");
		assertExists("sex");		
		assertNotExists("currentHospital");
		assertExists("mainLanguage");
		assertNotExists("favouriteFramework");		
	}
	
	private void assertJavaProgrammerView() throws Exception {
		setModel("JavaProgrammer");
		assertExists("name");
		assertExists("sex");		
		assertNotExists("currentHospital");
		assertExists("mainLanguage");
		assertExists("favouriteFramework");		
	}
	
	private void assertDoctorView() throws Exception {
		setModel("Doctor");
		assertExists("name");
		assertExists("sex");		
		assertExists("currentHospital");
		assertNotExists("mainLanguage");
		assertNotExists("favouriteFramework");		
	}
			
}
