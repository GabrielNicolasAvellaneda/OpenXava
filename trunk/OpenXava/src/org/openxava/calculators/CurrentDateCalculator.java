package org.openxava.calculators;



/**
 * @author Javier Paniza
 */
public class CurrentDateCalculator implements ICalculator {


	/**
	 * @see org.openxava.calculators.ICalculator#calculate()
	 */
	public Object calculate() throws Exception {
		return new java.util.Date();
	}

}
