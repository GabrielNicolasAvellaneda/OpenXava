package org.openxava.test.tests;

import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class ColorTest extends ModuleTestBase {
	
	public ColorTest(String testName) {
		super(testName, "Color");		
	}
	
	public void testKeysWithZeroValue() throws Exception {
		assertValueInList(0, "number", "0");
		assertValueInList(0, "name", "ROJO");
		execute("Mode.detailAndFirst");
		assertNoErrors();
		assertValue("number", "0");
		assertValue("name", "ROJO");
		assertValue("sample", "RED");
	}		
	
	public void testMessageScapedWithQuotes() throws Exception{
		assertListNotEmpty();
		execute("List.viewDetail", "row=0");
		execute("Color.seeMessage");
		assertMessage("Message: A.B.C");
	}

	public void testIdentityCalculator() throws Exception {
		execute("List.orderBy", "property=number");
		execute("List.orderBy", "property=number");
		String last = getValueInList(0, "number");	
		execute("CRUD.new");
		assertNoErrors(); 
		setValue("number", "-1"); // needed in this case because 0 is an existing key
		setValue("name", "JUNIT COLOR " + (int) (Math.random() * 200));
		execute("TypicalNotResetOnSave.save");
		assertNoErrors();						
		String next = String.valueOf(Integer.parseInt(last) + 1);
		assertValue("number", next);
	}
	
	public void testOptimisticConcurrency() throws Exception {
		// Must be called 2 times in order to fix some problems on second time
		modifyColorFromFirstUser(1);
		modifyColorFromFirstUser(2);
	}

	public void testFilterByNumberZero() throws Exception {
		setConditionValues(new String[] { "0" });
		execute("List.filter");
		assertListRowCount(1);
	}
	
	public void testFilterDescriptionList_keyReferenceWithSameNameThatPropertyFather() throws Exception{
		assertLabelInList(4, "Name of Used to");
		assertValueInList(0, 4, "CAR");
		setConditionValues(new String[] { "", "", "", "1"} );
		execute("List.filter");
		assertListNotEmpty();
	}
	
	public void modifyColorFromFirstUser(int id) throws Exception {		
		// First user
		execute("List.viewDetail", "row=2");		
		assertNotExists("version");
		setValue("name", "COLOR A" + id);
		
		// Second user, it's faster, he wins
		ColorTest otherSession = new ColorTest("Color2");
		otherSession.modifyColorFromSecondUser(id);
		
		// The first user continues		
		execute("TypicalNotResetOnSave.save");
		assertError("Impossible to execute Save action: Another user has modified this record");
		execute("Mode.list");		
		assertValueInList(2, "name", "COLOR B" + id); // The second user won
	}
	
	private void modifyColorFromSecondUser(int id) throws Exception {
		setUp();
		execute("List.viewDetail", "row=2");
		setValue("name", "COLOR B" + id);
		execute("TypicalNotResetOnSave.save");
		assertNoErrors();		
		tearDown();
	}	
	
}
