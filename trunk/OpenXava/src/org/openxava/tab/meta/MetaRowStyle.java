package org.openxava.tab.meta;

import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Javier Paniza
 */
public class MetaRowStyle implements Serializable{
	
	private String style;
	private String property;
	private String value;
	
	private Log log = LogFactory.getLog(MetaRowStyle.class);

	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
