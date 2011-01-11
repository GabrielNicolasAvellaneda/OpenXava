

// File generated by OpenXava: Tue Jan 11 13:59:30 CET 2011
// Archivo generado por OpenXava: Tue Jan 11 13:59:30 CET 2011

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Invoice2		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IInvoice2  extends org.openxava.model.IModel {	

	// Properties/Propiedades 	
	public static final String PROPERTY_amountsSum = "amountsSum"; 
	java.math.BigDecimal getAmountsSum() throws RemoteException;
	void setAmountsSum(java.math.BigDecimal amountsSum) throws RemoteException; 	
	public static final String PROPERTY_year = "year"; 	
	int getYear() throws RemoteException; 	
	public static final String PROPERTY_vatPercentage = "vatPercentage"; 
	java.math.BigDecimal getVatPercentage() throws RemoteException;
	void setVatPercentage(java.math.BigDecimal vatPercentage) throws RemoteException; 	
	public static final String PROPERTY_date = "date"; 
	java.util.Date getDate() throws RemoteException;
	void setDate(java.util.Date date) throws RemoteException; 	
	public static final String PROPERTY_number = "number"; 	
	int getNumber() throws RemoteException;	

	java.util.Collection getDetails() throws RemoteException;		

	// References/Referencias 

	// Customer : Reference/Referencia
	
	org.openxava.test.model.ICustomer getCustomer() throws RemoteException;
	void setCustomer(org.openxava.test.model.ICustomer newCustomer) throws RemoteException;

	// Methods 


}