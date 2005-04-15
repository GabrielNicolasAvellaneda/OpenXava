package org.openxava.test.calculators;

import java.rmi.*;

import org.openxava.calculators.*;
import org.openxava.test.ejb.xejb.*;

/**
 * @author Javier Paniza
 */
public class SubfamilyPureRemarksCalculator implements IEntityCalculator {
	
	private SubfamilyBean subfamily;

	public void setEntity(Object entity) throws RemoteException {
		this.subfamily = (SubfamilyBean) entity;		
	}

	public Object calculate() throws Exception {		
		return subfamily.get_Remarks();
	}

}
