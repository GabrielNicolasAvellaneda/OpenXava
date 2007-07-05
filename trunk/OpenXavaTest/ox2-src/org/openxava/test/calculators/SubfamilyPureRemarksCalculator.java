package org.openxava.test.calculators;

import java.rmi.*;

import org.openxava.calculators.*;

/**
 * @author Javier Paniza
 */
public class SubfamilyPureRemarksCalculator implements IModelCalculator {
	
	// tmp private SubfamilyBean subfamily;

	public void setModel(Object entity) throws RemoteException {
		// tmp this.subfamily = (SubfamilyBean) entity;		
	}

	public Object calculate() throws Exception {		
		return ""; // tmp subfamily.get_Remarks();
	}

}
