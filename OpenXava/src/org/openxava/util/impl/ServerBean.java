package org.openxava.util.impl;

import javax.ejb.*;

import org.openxava.actions.*;
import org.openxava.calculators.*;
import org.openxava.ejbx.*;
import org.openxava.util.*;


public class ServerBean extends SessionBase {
	
	public void ejbCreate() throws CreateException {
	}
	
	
	public Object calculate(ICalculator calculador) throws Exception {		
		XSystem._setOnServer(); // para asegurarse
		if (calculador instanceof IJDBCCalculator) {			
			((IJDBCCalculator) calculador).setConnectionProvider(getPortableContext());
		}
		try {
			return calculador.calculate();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}	
	
	public Object calculateWithoutTransaction(ICalculator calculador) throws Exception {
		XSystem._setOnServer(); // para asegurarse	
		return calculate(calculador);
	}
	
	public IRemoteAction execute(IRemoteAction accion) throws Exception {
		XSystem._setOnServer(); // para asegurarse
		try {
			accion.execute();
			return accion;
		}
		catch (Exception ex) {
			ex.printStackTrace(); 
			getSessionContext().setRollbackOnly();									
			throw ex;
		}
	}	
			
}