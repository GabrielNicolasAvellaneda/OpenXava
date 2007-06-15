package org.openxava.test.tests;

import org.openxava.tests.*;

import com.meterware.httpunit.*;

import junit.framework.*;

public class PublicJSPTest extends TestCase {
	
	public void testPublicJSP() throws Exception {
		WebConversation wc = new WebConversation();
	    WebResponse   resp = wc.getResponse("http://" + getHost() + ":" + getPort() + "/" + getApplication() + "/public/myPublicJSP.jsp" ); 
	    assertTrue(resp.getText().startsWith("The uri of this JSP is"));
	}
	
	private static String getPort() {		
		return ModuleTestBase.getProperty("port", "8080");				
	}
	
	private static String getHost() {
		return ModuleTestBase.getProperty("host", "localhost");
	}	
	
	private static String getApplication() {
		return ModuleTestBase.getProperty("application", "OpenXavaTest");
	}


}
