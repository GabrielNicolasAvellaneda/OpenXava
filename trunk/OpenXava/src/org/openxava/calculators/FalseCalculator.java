package org.openxava.calculators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Javier Paniza
 */
public class FalseCalculator implements ICalculator {

	private Log log = LogFactory.getLog(FalseCalculator.class);
	
	public Object calculate() throws Exception {
		return Boolean.FALSE;
	}

}
