

// File generated by OpenXava: Tue May 27 10:58:49 CEST 2008
// Archivo generado por OpenXava: Tue May 27 10:58:49 CEST 2008

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: CustomerContactPerson		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface ICustomerContactPerson  extends org.openxava.model.IModel {	

	// Properties/Propiedades 	
	public static final String PROPERTY_name = "name"; 
	String getName() throws RemoteException;
	void setName(String name) throws RemoteException;		

	// References/Referencias 

	// Customer : Reference/Referencia
	
	org.openxava.test.model.ICustomer getCustomer() throws RemoteException;
	void setCustomer(org.openxava.test.model.ICustomer newCustomer) throws RemoteException;

	// Methods 


}