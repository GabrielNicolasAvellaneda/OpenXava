package org.openxava.test.calculators;

import org.openxava.calculators.*;
import org.openxava.test.ejb.*;

/**
 * @author Javier Paniza
 */
public class DefaultWarehouse2Calculator implements ICalculator {

	public Object calculate() throws Exception {		
		return new Warehouse2Key(new Integer(4), 4);
	}

}
