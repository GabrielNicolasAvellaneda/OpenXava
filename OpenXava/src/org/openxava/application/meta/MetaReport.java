package org.openxava.application.meta;

import java.io.*;

/**
 * @author Javier Paniza
 */
public class MetaReport implements Serializable {
	
	private String modelName;
	private String tabName;
	

	/**
	 * Returns the nombreModelo.
	 * @return String
	 */
	public String getModelName() {
		return modelName;
	}

	/**
	 * Sets the nombreModelo.
	 * @param nombreModelo The nombreModelo to set
	 */
	public void setModelName(String nombreModelo) {
		this.modelName = nombreModelo;
	}

	/**
	 * Returns the nombreTab.
	 * @return String
	 */
	public String getTabName() {
		return tabName;
	}

	/**
	 * Sets the nombreTab.
	 * @param nombreTab The nombreTab to set
	 */
	public void setTabName(String nombreTab) {
		this.tabName = nombreTab;
	}

}
