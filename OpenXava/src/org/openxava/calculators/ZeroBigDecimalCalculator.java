package org.openxava.calculators;


/**
 * @author Javier Paniza
 */
public class ZeroBigDecimalCalculator implements ICalculator {
	
	private static final java.math.BigDecimal ZERO = new java.math.BigDecimal("0");

	/**
	 * @see org.openxava.calculators.ICalculator#calculate()
	 */
	public Object calculate() throws Exception {
		return ZERO;
	}
	
}
