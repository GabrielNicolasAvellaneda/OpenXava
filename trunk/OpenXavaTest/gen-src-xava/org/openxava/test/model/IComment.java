

// File generated by OpenXava: Tue Aug 07 17:50:17 CEST 2007
// Archivo generado por OpenXava: Tue Aug 07 17:50:17 CEST 2007

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Issue		Java interface for aggregate/Interfaz java para Agregado: Comment

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IComment  extends org.openxava.model.IModel {	

	// Properties/Propiedades 	
	Integer getId() throws RemoteException; 
	java.lang.String getComment() throws RemoteException;
	void setComment(java.lang.String comment) throws RemoteException; 
	java.util.Date getDate() throws RemoteException;
	void setDate(java.util.Date date) throws RemoteException;		

	// References/Referencias 

	// Issue : Reference/Referencia
	
	org.openxava.test.model.IIssue getIssue() throws RemoteException;
	void setIssue(org.openxava.test.model.IIssue newIssue) throws RemoteException;

	// Methods 


}