package org.openxava.test.ejb;

import java.rmi.RemoteException;

import org.openxava.ejbx.*;


/**
 * Remote interface for Enterprise Bean: Storehouse
 */
public interface Warehouse extends javax.ejb.EJBObject, EJBReplicable {
	
	public int getZoneNumber() throws RemoteException;
	public String getName() throws RemoteException;
	public int getNumber() throws RemoteException;
	public void setName(String nombre) throws RemoteException;

}

