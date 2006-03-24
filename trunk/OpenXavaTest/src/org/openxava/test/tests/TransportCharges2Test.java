package org.openxava.test.tests;

import org.openxava.hibernate.*;
import org.openxava.tests.*;
import org.openxava.util.*;


/**
 * @author Javier Paniza
 */

public class TransportCharges2Test extends ModuleTestBase {
			
	public TransportCharges2Test(String testName) {
		super(testName, "OpenXavaTest", "TransportCharges2");		
	}
	
	public void testKeyNestedOverlappedReferences() throws Exception {
		deleteAll();
		
		execute("CRUD.new");
		setValue("year", "2002");
		execute("Reference.search", "keyProperty=xava.TransportCharge2.delivery.number");
		String year = getValueInList(0, 0);
		String number = getValueInList(0, 1);
		assertTrue("It is required that year in invoice has value", !Is.emptyString(year));
		assertTrue("It is required that number in invoice has value", !Is.emptyString(number));
		
		execute("ReferenceSearch.choose", "row=0");
		assertNoErrors();
		assertValue("delivery.invoice.year", year);
		assertValue("delivery.invoice.number", number);
		
		setValue("amount", "666");
		execute("CRUD.save");
		assertNoErrors();
				
		assertValue("year", "");
		assertValue("delivery.invoice.year", "");
		assertValue("delivery.invoice.number", "");
		assertValue("amount", "");
		
		execute("Mode.list");
		execute("Mode.detailAndFirst");
		assertNoErrors();
		assertValue("year", "2002");
		assertValue("delivery.invoice.year", year);
		assertValue("delivery.invoice.number", number);
		assertValue("amount", "666");
		
		setValue("amount", "777");
		execute("CRUD.save");
		assertNoErrors();

		assertValue("year", "");
		assertValue("delivery.invoice.year", "");
		assertValue("delivery.invoice.number", "");
		assertValue("amount", "");
		
		execute("Mode.list");
		execute("Mode.detailAndFirst");
		assertNoErrors();
		assertValue("year", "2002");
		assertValue("delivery.invoice.year", year);
		assertValue("delivery.invoice.number", number);
		assertValue("amount", "777");
						
		execute("CRUD.delete");										
		assertMessage("TransportCharge2 deleted successfully");
	}
	
	private void deleteAll() throws Exception {
		XHibernate.getSession().createQuery("delete from TransportCharge2").executeUpdate();
		XHibernate.commit(); 
		
	}	
	
}
