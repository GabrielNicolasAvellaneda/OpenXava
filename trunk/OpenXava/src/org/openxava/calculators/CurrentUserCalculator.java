package org.openxava.calculators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.util.*;

public class CurrentUserCalculator implements ICalculator {

	private static Log log = LogFactory.getLog(CurrentUserCalculator.class);
	
	public Object calculate() throws Exception {		
		return Users.getCurrent();
	}

}
