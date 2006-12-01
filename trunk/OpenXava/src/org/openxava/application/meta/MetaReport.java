package org.openxava.application.meta;

import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Javier Paniza
 */
public class MetaReport implements Serializable {
	
	private String modelName;
	private String tabName;
	private Log log = LogFactory.getLog(MetaReport.class);
	
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	
	public String getTabName() {
		return tabName;
	}
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

}
