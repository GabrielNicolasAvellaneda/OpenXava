

// File generated by OpenXava: Fri May 13 17:14:43 CEST 2005
// Archivo generado por OpenXava: Fri May 13 17:14:43 CEST 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Subfamily		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.ejb;

import java.math.*;
import java.rmi.RemoteException;


public interface ISubfamily  {	

	// Properties/Propiedades 	
	String getFamily() throws RemoteException; 
	int getFamilyNumber() throws RemoteException;
	void setFamilyNumber(int familyNumber) throws RemoteException; 	
	String getOid() throws RemoteException; 
	java.lang.String getRemarks() throws RemoteException;
	void setRemarks(java.lang.String remarks) throws RemoteException; 
	String getDescription() throws RemoteException;
	void setDescription(String description) throws RemoteException; 	
	String getRemarksDB() throws RemoteException; 
	int getNumber() throws RemoteException;
	void setNumber(int number) throws RemoteException;		

	// References/Referencias

	// Methods 


}