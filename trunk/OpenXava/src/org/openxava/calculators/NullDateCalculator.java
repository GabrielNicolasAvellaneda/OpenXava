package org.openxava.calculators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.util.*;


/**
 * @author Javier Paniza
 */
public class NullDateCalculator implements ICalculator {

	private final static java.util.Date NULL_DATE = Dates.create(1, 1, 1); 	
	private Log log = LogFactory.getLog(NullDateCalculator.class);

	/**
	 * @see org.openxava.calculators.ICalculator#calculate()
	 */
	public Object calculate() throws Exception {
		return NULL_DATE;
	}

}
