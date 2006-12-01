package org.openxava.calculators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Ana Andrés
 */
public class TrueCalculator implements ICalculator{

	private Log log = LogFactory.getLog(TrueCalculator.class);
	
	public Object calculate() throws Exception {		
		return Boolean.TRUE;
	}

}
