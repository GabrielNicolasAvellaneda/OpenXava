package org.openxava.test.calculators;

import java.rmi.*;

import org.openxava.calculators.*;
import org.openxava.test.model.xejb.*;

/**
 * @author Javier Paniza
 */
public class SubfamilyPureRemarksCalculator implements IModelCalculator {
	
	private SubfamilyBean subfamily;

	public void setModel(Object entity) throws RemoteException {
		this.subfamily = (SubfamilyBean) entity;		
	}

	public Object calculate() throws Exception {		
		return subfamily.get_Remarks();
	}

}
