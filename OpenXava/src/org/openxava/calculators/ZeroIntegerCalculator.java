package org.openxava.calculators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Javier Paniza
 */
public class ZeroIntegerCalculator implements ICalculator {
	
	private final static Integer ZERO = new Integer(0);
	private static Log log = LogFactory.getLog(ZeroIntegerCalculator.class);

	public Object calculate() throws Exception {
		return ZERO;
	}

}
