package org.openxava.calculators;


/**
 * @author Javier Paniza
 */
public class StringCalculator implements ICalculator {
	
	private String string;

	/**
	 * @see org.openxava.calculators.ICalculator#calculate()
	 */
	public Object calculate() throws Exception {
		return string;
	}

	/**
	 * @return
	 */
	public String getString() {
		return string;
	}

	/**
	 * @param string
	 */
	public void setString(String string) {
		this.string = string;
	}

}
