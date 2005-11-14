package org.openxava.test.tests;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;
import javax.naming.*;
import javax.rmi.*;

import org.hibernate.*;
import org.openxava.model.impl.*;
import org.openxava.test.model.*;
import org.openxava.tests.*;
import org.openxava.validators.*;



/**
 * @author Javier Paniza
 */

public class DescriptionTest extends ModuleTestBase {
	
	
	public DescriptionTest(String testName) {
		super(testName, "OpenXavaTest", "Description");		
	}
	
	public void testDocModule() throws Exception {		
		assertTrue(getHtml().indexOf("application is used to test all OpenXava features")>=0);
	}
	
}
