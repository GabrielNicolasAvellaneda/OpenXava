package org.openxava.calculators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Miguel Embuena
 */
public class LongCalculator implements ICalculator {

	private long value;
	private static Log log = LogFactory.getLog(LongCalculator.class);

	public Object calculate() throws Exception {
		return new Long(value);
	}

	public long getValue() {
		return value;
	}

	public void setValue(long l) {
		value = l;
	}

}
