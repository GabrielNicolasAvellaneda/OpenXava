package org.openxava.calculators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Javier Paniza
 */
public class CounterByPassOidCalculator implements IAggregateOidCalculator {

	private int counter;
	private Log log = LogFactory.getLog(CounterByPassOidCalculator.class);

	public void setContainer(Object contenedorKey) {
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public Object calculate() throws Exception { 
		return new Integer(counter);		
	}

}
