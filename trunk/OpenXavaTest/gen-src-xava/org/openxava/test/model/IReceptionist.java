

// File generated by OpenXava: Fri Sep 08 13:37:41 CEST 2006
// Archivo generado por OpenXava: Fri Sep 08 13:37:41 CEST 2006

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Customer		Java interface for aggregate/Interfaz java para Agregado: Receptionist

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IReceptionist  extends org.openxava.model.IModel {	

	// Properties/Propiedades 	
	int getOid() throws RemoteException; 
	String getName() throws RemoteException;
	void setName(String name) throws RemoteException;		

	// References/Referencias 

	// DeliveryPlace : Reference/Referencia
	
	org.openxava.test.model.IDeliveryPlace getDeliveryPlace() throws RemoteException;
	void setDeliveryPlace(org.openxava.test.model.IDeliveryPlace newDeliveryPlace) throws RemoteException;

	// Methods 


}