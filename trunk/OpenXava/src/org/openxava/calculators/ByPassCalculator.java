package org.openxava.calculators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Javier Paniza
 */
public class ByPassCalculator implements ICalculator {
	
	private Object source;
	private Log log = LogFactory.getLog(ByPassCalculator.class);

	public Object calculate() throws Exception {
		return source;
	}

	public Object getSource() {
		return source;
	}
	public void setSource(Object source) {
		this.source = source;
	}
	
}
