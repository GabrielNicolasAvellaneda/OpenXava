package org.openxava.util.impl;

import javax.ejb.*;

import org.openxava.actions.*;
import org.openxava.calculators.*;
import org.openxava.ejbx.*;
import org.openxava.util.*;


public class ServerBean extends SessionBase {
	
	public void ejbCreate() throws CreateException {
	}
	
	
	public Object calculate(ICalculator calculator) throws Exception {		
		XSystem._setOnServer(); // to secure it
		if (calculator instanceof IJDBCCalculator) {			
			((IJDBCCalculator) calculator).setConnectionProvider(getPortableContext());
		}
		try {
			return calculator.calculate();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}	
	
	public Object calculateWithoutTransaction(ICalculator calculator) throws Exception {
		XSystem._setOnServer(); // to secure it	
		return calculate(calculator);
	}
	
	public IRemoteAction execute(IRemoteAction action) throws Exception {
		XSystem._setOnServer(); // to secure it
		try {
			action.execute();
			return action;
		}
		catch (Exception ex) {
			ex.printStackTrace(); 
			getSessionContext().setRollbackOnly();									
			throw ex;
		}
	}	
			
}