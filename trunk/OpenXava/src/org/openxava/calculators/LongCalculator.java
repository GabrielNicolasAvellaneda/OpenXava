package org.openxava.calculators;


/**
 * @author Javier Paniza
 */
public class LongCalculator implements ICalculator {

	private int value;

	public Object calculate() throws Exception {
		return new Long(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int i) {
		value = i;
	}

}
