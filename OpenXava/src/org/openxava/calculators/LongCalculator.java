package org.openxava.calculators;


/**
 * @author Javier Paniza
 */
public class LongCalculator implements ICalculator {

	private long value;

	public Object calculate() throws Exception {		
		return new Long(value);
	}

	public long getValue() {
		return value;
	}

	public void setValue(long i) {
		value = i;
	}

}
