package org.openxava.tab.impl;

/**
 * @author Javier Paniza
 */
public class TabUserPreferences implements java.io.Serializable {
	
	private int oid = -1;
	private String user;
	private String tab; // The name of tab in this format: 'Component.tabName'
	private String propertiesNames;

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
}
