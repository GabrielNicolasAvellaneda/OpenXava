package org.openxava.test.calculators;

import org.openxava.calculators.*;
import org.openxava.test.model.*;

/**
 * @author Javier Paniza
 */
public class DefaultWarehouseCalculator implements ICalculator {

	public Object calculate() throws Exception {		
		return new WarehouseKey(new Integer(4), 4);
	}

}
