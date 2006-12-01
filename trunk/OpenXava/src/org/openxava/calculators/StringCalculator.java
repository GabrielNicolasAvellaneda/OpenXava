package org.openxava.calculators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Javier Paniza
 */
public class StringCalculator implements ICalculator {
	
	private String string;
	private Log log = LogFactory.getLog(StringCalculator.class);

	public Object calculate() throws Exception {
		return string;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

}
