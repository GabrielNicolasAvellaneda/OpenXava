package org.openxava.test.tests;

import java.util.*;

import org.openxava.model.*;
import org.openxava.util.*;
import org.openxava.validators.*;

import junit.framework.*;

/**
 * @author Javier Paniza
 */
public class MapFacadeTest extends TestCase {

	public MapFacadeTest(String name) {
		super(name);
		Locale.setDefault(Locale.ENGLISH);
	}
	
	public void testAccumulateExistsPropertiesAndRequiredPropertiesOnCreate() throws Exception {
		Map values = new HashMap();
		values.put("description", "");
		try {
			MapFacade.create("Shipment", values);
		}
		catch (ValidationException ex) {
			Collection errors = ex.getErrors().getStrings();			
			assertTrue("type", errors.contains("Value for Type in Shipment is required"));
			assertTrue("number", errors.contains("Value for Number in Shipment is required"));
			assertTrue("description", errors.contains("Value for Description in Shipment is required"));
		}		
	}
	
	public void testValidateOnly() throws Exception {
		Map values = new HashMap();
		values.put("description", "");		
		Messages errors = MapFacade.validate("Shipment", values);
		assertTrue("It has to have errors", errors.contains());
		Collection texts = errors.getStrings();			
		assertTrue("type", !texts.contains("Value for Type in Shipment is required"));
		assertTrue("number", !texts.contains("Value for Number in Shipment is required"));
		assertTrue("description", texts.contains("Value for Description in Shipment is required"));				
	}

}
