package org.openxava.calculators;


/**
 * @author Javier Paniza
 */
public class ZeroLongCalculator implements ICalculator {
	
	private final static Long ZERO = new Long(0);


	/**
	 * @see org.openxava.calculators.ICalculator#calculate()
	 */
	public Object calculate() throws Exception {
		return ZERO;
	}

}
