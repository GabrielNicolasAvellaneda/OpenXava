package org.openxava.test.tests;

import java.util.*;

import javax.persistence.*;

import org.openxava.tests.*;
import org.openxava.util.*;
import static org.openxava.jpa.XPersistence.*;


/**
 * @author Javier Paniza
 */

public class OrderTest extends ModuleTestBase {
	
	public OrderTest(String testName) {
		super(testName, "Order");		
	}
	
	public void testCalculatedPropertiesFromCollection_generatedValueOnPersistRefreshedInView() throws Exception {
		String nextNumber = getNextNumber();
		execute("CRUD.new");
		assertValue("number", "");
		setValue("customer.number", "1");
		assertValue("customer.name", "Javi");
		assertCollectionRowCount("details", 0);
		execute("Collection.new", "viewObject=xava_view_details");
		setValue("details.product.number", "1");
		assertValue("details.product.description", "MULTAS DE TRAFICO");
		assertValue("details.product.unitPrice", "11.00");
		setValue("details.quantity", "10");
		assertValue("details.amount", "110.00");
		execute("Collection.save", "viewObject=xava_view_details");
		assertNoErrors();
		assertCollectionRowCount("details", 1);
		assertValue("amount", "110.00");
		assertValue("number", nextNumber);
	}
	
	private String getNextNumber() throws Exception {
		Query query = getManager().
			createQuery(
				"select max(o.number) from Order o where o.year = :year"); 
		query.setParameter("year", Dates.getYear(new Date()));
		Integer lastNumber = (Integer) query.getSingleResult();
		if (lastNumber == null) lastNumber = 0;
		return Integer.toString(lastNumber + 1);
	}

								
}
