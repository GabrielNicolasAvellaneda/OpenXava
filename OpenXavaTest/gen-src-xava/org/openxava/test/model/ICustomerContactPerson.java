

// File generated by OpenXava: Fri Jan 26 13:32:30 CET 2007
// Archivo generado por OpenXava: Fri Jan 26 13:32:30 CET 2007

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