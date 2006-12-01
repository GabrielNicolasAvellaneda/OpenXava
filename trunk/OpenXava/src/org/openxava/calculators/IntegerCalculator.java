package org.openxava.calculators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Javier Paniza
 */
public class IntegerCalculator implements ICalculator {

	private int value;
	private Log log = LogFactory.getLog(IntegerCalculator.class);
	
	public Object calculate() throws Exception {		
		return new Integer(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int i) {
		value = i;
	}

}
