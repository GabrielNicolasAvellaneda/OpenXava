

// File generated by OpenXava: Wed Jul 12 11:13:28 CEST 2006
// Archivo generado por OpenXava: Wed Jul 12 11:13:28 CEST 2006

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Customer		Java interface for aggregate/Interfaz java para Agregado: DeliveryPlace

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IDeliveryPlace  extends org.openxava.model.IModel {	

	// Properties/Propiedades 	
	int getOid() throws RemoteException; 
	String getRemarks() throws RemoteException;
	void setRemarks(String remarks) throws RemoteException; 
	String getAddress() throws RemoteException;
	void setAddress(String address) throws RemoteException; 
	String getName() throws RemoteException;
	void setName(String name) throws RemoteException;	

	java.util.Collection getReceptionists() throws RemoteException;		

	// References/Referencias 

	// Customer : Reference/Referencia
	
	org.openxava.test.model.ICustomer getCustomer() throws RemoteException;
	void setCustomer(org.openxava.test.model.ICustomer newCustomer) throws RemoteException; 

	// PreferredWarehouse : Reference/Referencia
	
	org.openxava.test.model.IWarehouse getPreferredWarehouse() throws RemoteException;
	void setPreferredWarehouse(org.openxava.test.model.IWarehouse newPreferredWarehouse) throws RemoteException;

	// Methods 


}