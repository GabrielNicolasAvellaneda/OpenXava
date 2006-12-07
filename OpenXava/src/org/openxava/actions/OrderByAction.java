package org.openxava.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.tab.*;

/**
 * @author Javier Paniza
 */

public class OrderByAction extends BaseAction {
	
	private Tab tab;
	private String property;
	private static Log log = LogFactory.getLog(OrderByAction.class);
	
	public void execute() throws Exception {
		tab.orderBy(property);
	}

	public Tab getTab() {
		return tab;
	}

	public void setTab(Tab web) {
		tab = web;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String string) {
		property = string;
	}

}
