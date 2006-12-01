package org.openxava.calculators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Javier Paniza
 */
public class ZeroBigDecimalCalculator implements ICalculator {
	
	private static final java.math.BigDecimal ZERO = new java.math.BigDecimal("0");
	private Log log = LogFactory.getLog(ZeroBigDecimalCalculator.class);

	public Object calculate() throws Exception {
		return ZERO;
	}
	
}
