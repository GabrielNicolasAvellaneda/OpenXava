

// File generated by OpenXava: Tue Nov 28 11:10:09 CET 2006
// Archivo generado por OpenXava: Tue Nov 28 11:10:09 CET 2006

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: CustomerContactPerson		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface ICustomerContactPerson  extends org.openxava.model.IModel {	

	// Properties/Propiedades 
	String getName() throws RemoteException;
	void setName(String name) throws RemoteException;		

	// References/Referencias 

	// Customer : Reference/Referencia
	
	org.openxava.test.model.ICustomer getCustomer() throws RemoteException;
	void setCustomer(org.openxava.test.model.ICustomer newCustomer) throws RemoteException;

	// Methods 


}