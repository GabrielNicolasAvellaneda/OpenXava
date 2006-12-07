package org.openxava.calculators;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Javier Paniza
 */
public class CurrentYearCalculator implements ICalculator {

	private static Log log = LogFactory.getLog(CurrentYearCalculator.class);
	
	public Object calculate() throws Exception {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new java.util.Date());
		return new Integer(cal.get(Calendar.YEAR));
	}

}
