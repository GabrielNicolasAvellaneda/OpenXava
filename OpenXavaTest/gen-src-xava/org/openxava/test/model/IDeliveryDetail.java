

// File generated by OpenXava: Thu Nov 16 09:44:09 CET 2006
// Archivo generado por OpenXava: Thu Nov 16 09:44:09 CET 2006

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Delivery		Java interface for aggregate/Interfaz java para Agregado: DeliveryDetail

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IDeliveryDetail  extends org.openxava.model.IModel {	

	// Properties/Propiedades 
	String getDescription() throws RemoteException;
	void setDescription(String description) throws RemoteException; 	
	int getNumber() throws RemoteException;		

	// References/Referencias 

	// Delivery : Reference/Referencia
	
	org.openxava.test.model.IDelivery getDelivery() throws RemoteException;
	void setDelivery(org.openxava.test.model.IDelivery newDelivery) throws RemoteException;

	// Methods 


}