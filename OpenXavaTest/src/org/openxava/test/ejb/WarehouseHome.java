package org.openxava.test.ejb;

import java.util.Map;

import org.openxava.validators.*;


/**
 * Home interface for Enterprise Bean: Storehouse
 */
public interface WarehouseHome extends javax.ejb.EJBHome {
	
	Warehouse create(int zoneNumber, int number, String name) throws javax.ejb.CreateException, ValidationException, java.rmi.RemoteException;
	Warehouse create(Map initialValues) throws javax.ejb.CreateException, ValidationException, java.rmi.RemoteException;	
	Warehouse findByPrimaryKey(WarehouseKey primaryKey) throws javax.ejb.FinderException, java.rmi.RemoteException;
}
