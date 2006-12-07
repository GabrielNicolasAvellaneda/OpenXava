package org.openxava.view.meta;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.model.meta.*;

/**
 * It's used in a property collection for
 * indicate a separation. <p>
 *
 * Extends from <tt>MetaProperty</tt> thus it can be processed
 * by any method that receive a <tt>MetaProperty</tt>.<br> 
 * 
 * @author Javier Paniza
 */

public class PropertiesSeparator extends MetaProperty {
	
	public static final PropertiesSeparator INSTANCE = new PropertiesSeparator(); 
	
	private static Log log = LogFactory.getLog(PropertiesSeparator.class);
	
	private PropertiesSeparator() {
	}
	
	public String getName() {
		return "[SEPARATOR]";
	}
	
	public String getLabel() {
		return "";
	}

}
