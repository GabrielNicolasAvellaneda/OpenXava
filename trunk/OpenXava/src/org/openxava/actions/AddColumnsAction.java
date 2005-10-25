package org.openxava.actions;

import java.util.*;

import javax.servlet.http.*;

import org.openxava.tab.*;

/**
 * @author Javier Paniza
 */
public class AddColumnsAction extends BaseAction implements IRequestAction, INavigationAction, IChangeModeAction {
	
	private HttpServletRequest request;
	private Tab tab;

	public void execute() throws Exception {
		String [] values = request.getParameterValues("selectedProperties");		
		getTab().addProperties(Arrays.asList(values));
		
	}
	
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public Tab getTab() {
		return tab;
	}
	public void setTab(Tab tab) {
		this.tab = tab;
	}

	public String[] getNextControllers() throws Exception {
		return DEFAULT_CONTROLLERS;
	}

	public String getCustomView() throws Exception {
		return DEFAULT_VIEW;
	}

	public String getNextMode() {
		return LIST;
	}
}
