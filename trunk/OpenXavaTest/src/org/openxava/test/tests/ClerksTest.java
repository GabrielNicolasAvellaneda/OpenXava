package org.openxava.test.tests;

import java.text.*;
import java.util.*;

import org.openxava.tests.*;



/**
 * @author Javier Paniza
 */

public class ClerksTest extends ModuleTestBase {
	
	public ClerksTest(String testName) {
		super(testName, "Clerks");		
	}
	
	public void testTimeStereotypeAndSqlTimeAndStringAsByteArrayInDB() throws Exception {
		assertListNotEmpty();
		execute("Mode.detailAndFirst");
		String time = getCurrentTime();
		setValue("arrivalTime", time);
		setValue("endingTime", time);
		setValue("comments", "Created at " + time);
		execute("CRUD.save");
		assertNoErrors();
		execute("Mode.list");
		assertValueInList(0, "arrivalTime", time + ":00");
		assertValueInList(0, "endingTime", time + ":00");
		assertValueInList(0, "comments", "Created at " + time);
		
		setConditionValues(new String [] { "", "", "", "", time });
		execute("List.filter");
		assertListRowCount(1);
		assertValueInList(0, "arrivalTime", time + ":00");
		assertValueInList(0, "endingTime", time + ":00");
		
		// Asserting that java.sql.Time works in JasperReport
		execute("Print.generatePdf"); 
		assertContentTypeForPopup("application/pdf");				
	}

	private String getCurrentTime() {
		return new SimpleDateFormat("hh:mm").format(new Date());
	}
}
