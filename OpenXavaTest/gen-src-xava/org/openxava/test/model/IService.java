

// File generated by OpenXava: Wed Feb 22 16:39:39 CET 2006
// Archivo generado por OpenXava: Wed Feb 22 16:39:39 CET 2006

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Service		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IService  extends org.openxava.model.IModel {	

	// Properties/Propiedades 
	int getFamily() throws RemoteException;
	void setFamily(int family) throws RemoteException; 
	String getDescription() throws RemoteException;
	void setDescription(String description) throws RemoteException; 	
	int getNumber() throws RemoteException;	

	java.util.Collection getAdditionalDetails() throws RemoteException;		

	// References/Referencias  	
	// Detail : Aggregate 
	
	org.openxava.test.model.Detail getDetail() throws RemoteException;
	void setDetail(org.openxava.test.model.Detail newDetail) throws RemoteException;

	// Methods 


}