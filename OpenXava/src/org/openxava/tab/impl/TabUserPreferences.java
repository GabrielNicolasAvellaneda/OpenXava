package org.openxava.tab.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Javier Paniza
 */
public class TabUserPreferences implements java.io.Serializable {
	
	private int oid = -1;
	private String user;
	private String tab; // The name of tab in this format: 'Component.tabName'
	private String propertiesNames;
	private boolean rowsHidden;
	
	private Log log = LogFactory.getLog(TabUserPreferences.class);

	public String getPropertiesNames() {
		return propertiesNames;
	}
	public void setPropertiesNames(String propertiesNames) {
		this.propertiesNames = propertiesNames;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getTab() {
		return tab;
	}
	public void setTab(String tab) {
		this.tab = tab;
	}
	public boolean isRowsHidden() {
		return rowsHidden;
	}
	public void setRowsHidden(boolean rowsHidden) {
		this.rowsHidden = rowsHidden;
	}
}
