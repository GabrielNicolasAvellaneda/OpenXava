

// File generated by OpenXava: Thu Mar 25 13:18:41 CET 2010
// Archivo generado por OpenXava: Thu Mar 25 13:18:41 CET 2010

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Customer		Java interface for aggregate/Interfaz java para Agregado: Receptionist

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IReceptionist  extends org.openxava.model.IModel {	

	// Properties/Propiedades 	
	public static final String PROPERTY_oid = "oid"; 	
	int getOid() throws RemoteException; 	
	public static final String PROPERTY_name = "name"; 
	String getName() throws RemoteException;
	void setName(String name) throws RemoteException;		

	// References/Referencias 

	// DeliveryPlace : Reference/Referencia
	
	org.openxava.test.model.IDeliveryPlace getDeliveryPlace() throws RemoteException;
	void setDeliveryPlace(org.openxava.test.model.IDeliveryPlace newDeliveryPlace) throws RemoteException;

	// Methods 


}