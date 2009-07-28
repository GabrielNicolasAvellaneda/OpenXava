package org.openxava.test.tests;

import java.math.*;

import org.openxava.tests.*;
import org.openxava.util.*;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

import junit.framework.*;

/**
 * 
 * @author Javier Paniza
 */

public class TestServletTest extends TestCase {
	
	public void testTestServlet() throws Exception {
		WebClient client = new WebClient();
		Page page = client.getPage("http://" + getHost() + ":" + getPort() + "/OpenXavaTest/test");
		String content = page.getWebResponse().getContentAsString();		
		assertTrue(content.indexOf("Hello, I'm a test servlet from OpenXava") >= 0);
	}
	
	private String getPort() { 
		return ModuleTestBase.getProperty("port", "8080");
	}
	
	private String getHost() { 
		return ModuleTestBase.getProperty("host", "localhost");
	}	
	

}
