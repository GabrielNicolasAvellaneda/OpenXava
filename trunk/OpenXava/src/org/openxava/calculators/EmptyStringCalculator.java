package org.openxava.calculators;


/**
 * @author Javier Paniza
 */
public class EmptyStringCalculator implements ICalculator {


	/**
	 * @see org.openxava.calculators.ICalculator#calculate()
	 */
	public Object calculate() throws Exception {
		return "";
	}

}
