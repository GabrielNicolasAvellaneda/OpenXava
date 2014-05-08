package com.openxava.naviox.tests;

import org.openxava.tests.*;
import org.openxava.util.*;

/**
 * Base class for creating a jUnit test that runs against a NaviOX module. <p>
 * 
 * It works in the same way that ModuleTestBase but use the correct URL to
 * access NaviOX modules and login() and logout() works against the NaviOX
 * application.
 * 
 * @author Javier Paniza
 */

abstract public class NaviOXTestBase extends ModuleTestBase {
	
	public NaviOXTestBase(String nameTest, String module) {
		super(nameTest, module);
	}

	protected void login(String user, String password) throws Exception {
		String originalModule = getModule();
		selectModuleInPage("SignIn");
		setValue("user", user);
		setValue("password", password);
		execute("SignIn.signIn");
		assertNoErrors();
		assertTrue(getHtml().contains("Sign out (" + user + ")"));
		selectModuleInPage(originalModule);		
	}

	protected void logout() throws Exception {
		getHtmlPage().getAnchorByHref("/NaviOX/naviox/signOut.jsp").click();
		reload();
	}
	
	
	private String getModule() {
		return Strings.lastToken(getModuleURL(), "/");
	}

	
	protected String getModuleURL() throws XavaException {
		return Strings.noLastTokenWithoutLastDelim(super.getModuleURL(), "?").replace("/modules/", "/m/");
	}
	
	
}