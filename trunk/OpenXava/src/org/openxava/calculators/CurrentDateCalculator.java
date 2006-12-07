package org.openxava.calculators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * @author Javier Paniza
 */
public class CurrentDateCalculator implements ICalculator {

	private static Log log = LogFactory.getLog(CurrentDateCalculator.class);
	
	public Object calculate() throws Exception {
		return new java.util.Date();
	}

}
