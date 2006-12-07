package org.openxava.calculators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Javier Paniza
 */
public class EmptyStringCalculator implements ICalculator {

	private static Log log = LogFactory.getLog(EmptyStringCalculator.class);
	
	public Object calculate() throws Exception {
		return "";
	}

}
