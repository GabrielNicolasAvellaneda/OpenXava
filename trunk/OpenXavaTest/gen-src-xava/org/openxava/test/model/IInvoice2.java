

// File generated by OpenXava: Mon Apr 30 18:14:54 CEST 2007
// Archivo generado por OpenXava: Mon Apr 30 18:14:54 CEST 2007

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Invoice2		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IInvoice2  extends org.openxava.model.IModel {	

	// Properties/Propiedades 	
	int getYear() throws RemoteException; 
	java.math.BigDecimal getVatPercentage() throws RemoteException;
	void setVatPercentage(java.math.BigDecimal vatPercentage) throws RemoteException; 
	java.util.Date getDate() throws RemoteException;
	void setDate(java.util.Date date) throws RemoteException; 	
	int getNumber() throws RemoteException;	

	java.util.Collection getDetails() throws RemoteException;		

	// References/Referencias 

	// Customer : Reference/Referencia
	
	org.openxava.test.model.ICustomer getCustomer() throws RemoteException;
	void setCustomer(org.openxava.test.model.ICustomer newCustomer) throws RemoteException;

	// Methods 


}