package org.openxava.test.actions;

import org.openxava.actions.*;

/**
 * @author Javier Paniza
 */

public class MySearchAction extends BaseAction implements INavigationAction {
		
	public void execute() throws Exception {				
	}

	public String[] getNextControllers() {		
		return new String [] { "MyReference" } ;
	}

	public String getCustomView() {		
		return "doYouWishSearch.jsp";
	}
	
	public void setKeyProperty(String s) {
	}

}
