

// File generated by OpenXava: Thu Nov 17 18:42:03 CET 2005
// Archivo generado por OpenXava: Thu Nov 17 18:42:03 CET 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Subfamily2		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface ISubfamily2  {	

	// Properties/Propiedades 
	java.lang.String getRemarks() throws RemoteException;
	void setRemarks(java.lang.String remarks) throws RemoteException; 
	String getDescription() throws RemoteException;
	void setDescription(String description) throws RemoteException; 	
	int getNumber() throws RemoteException;		

	// References/Referencias 

	// Family : Reference/Referencia
	
	org.openxava.test.model.IFamily2 getFamily() throws RemoteException;
	void setFamily(org.openxava.test.model.IFamily2 newFamily) throws RemoteException;

	// Methods 
	java.util.Collection getProductsValues() throws RemoteException; 


}