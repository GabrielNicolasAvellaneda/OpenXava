

// File generated by OpenXava: Mon Mar 07 10:45:47 CET 2005
// Archivo generado por OpenXava: Mon Mar 07 10:45:47 CET 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Customer		Java interface for aggregate/Interfaz java para Agregado: DeliveryPlace

package org.openxava.test.ejb;

import java.math.*;
import java.rmi.RemoteException;


public interface IDeliveryPlace {	

	// Properties/Propiedades 	
	String getOid() throws RemoteException; 
	String getRemarks() throws RemoteException;
	void setRemarks(String Remarks) throws RemoteException; 
	String getAddress() throws RemoteException;
	void setAddress(String Address) throws RemoteException; 
	String getName() throws RemoteException;
	void setName(String Name) throws RemoteException;	

	java.util.Collection getReceptionists() throws RemoteException;		

	// References/Referencias 

	// Customer : Reference/Referencia
	
	org.openxava.test.ejb.Customer getCustomer() throws RemoteException;
	void setCustomer(org.openxava.test.ejb.Customer newCustomer) throws RemoteException;
	
	org.openxava.test.ejb.CustomerKey getCustomerKey() throws RemoteException;
	void setCustomerKey(org.openxava.test.ejb.CustomerKey key) throws RemoteException; 

	// PreferredWarehouse : Reference/Referencia
	
	org.openxava.test.ejb.Warehouse2 getPreferredWarehouse() throws RemoteException;
	void setPreferredWarehouse(org.openxava.test.ejb.Warehouse2 newPreferredWarehouse) throws RemoteException;
	
	org.openxava.test.ejb.Warehouse2Key getPreferredWarehouseKey() throws RemoteException;
	void setPreferredWarehouseKey(org.openxava.test.ejb.Warehouse2Key key) throws RemoteException;

	// Methods 


}