package org.openxava.calculators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Javier Paniza
 */
public class ZeroLongCalculator implements ICalculator {
	
	private final static Long ZERO = new Long(0);
	private Log log = LogFactory.getLog(ZeroLongCalculator.class);

	public Object calculate() throws Exception {
		return ZERO;
	}

}
