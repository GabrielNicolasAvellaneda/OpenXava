package org.openxava.util.impl;

import java.rmi.*;

import org.openxava.actions.*;
import org.openxava.calculators.*;


public interface ServerRemote extends javax.ejb.EJBObject {
	
	Object calculate(ICalculator calculador) throws Exception, RemoteException;
	
	Object calculateWithoutTransaction(ICalculator calculador) throws Exception, RemoteException;	

	IRemoteAction execute(IRemoteAction accion) throws Exception, RemoteException;
}