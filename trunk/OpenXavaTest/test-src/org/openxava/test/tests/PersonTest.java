package org.openxava.test.tests;

import org.openxava.test.model.*;
import junit.framework.TestCase;

/**
 * For testing PropertyAspect. <p>
 * 
 * You need to compile your code with AspectJ, or to have installed the
 * AJDT plugin in your Eclipse in order to execute this test.<br>
 * If you install AJDT, you have to 'Convert to AspectJ Project',
 * OpenXava and OpenXavaTest projects.<br>
 * 
 * @author Javier Paniza
 */

public class PersonTest extends TestCase {
	
	public void testCalculatedProperty() throws Exception {
		Person p = new Person();
		p.name = "Mª Carmen";
		p.surnames = "Gimeno Alabau";		
		assertEquals("Mª Carmen Gimeno Alabau", p.fullName);
	}
	
	public void testRefinedPropertyValue() throws Exception{
		Person p = new Person();
		p.address = "C/. Almudaina";		
		assertEquals("C/. ALMUDAINA", p.address);		
	}
	
	public void testPropertiesInsideProperties() throws Exception{
		Person p = new Person();
		p.name = "Mª Carmen";
		p.surnames = "Gimeno Alabau";				
		p.address = "C/. Almudaina";		
		assertEquals("Mª Carmen Gimeno Alabau C/. ALMUDAINA", p.fullNameAndAddress);		
	}
	
	public void testRefinedSetter() throws Exception {
		Person p = new Person();
		p.age = 33;
		assertEquals(p.age, 33);
		try {
			p.age = 250;
			fail("It must throw IllegalArgumentException");
		}
		catch (IllegalArgumentException ex) {
		}
	}
	
}
