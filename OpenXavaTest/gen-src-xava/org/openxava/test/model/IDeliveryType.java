

// File generated by OpenXava: Thu Oct 26 12:14:16 CEST 2006
// Archivo generado por OpenXava: Thu Oct 26 12:14:16 CEST 2006

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: DeliveryType		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IDeliveryType  extends org.openxava.model.IModel {	

	// Properties/Propiedades 
	String getDescription() throws RemoteException;
	void setDescription(String description) throws RemoteException; 	
	int getNumber() throws RemoteException;	

	java.util.Collection getDeliveries() throws RemoteException;		

	// References/Referencias

	// Methods 


}