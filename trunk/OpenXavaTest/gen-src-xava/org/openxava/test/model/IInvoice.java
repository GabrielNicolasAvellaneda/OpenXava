

// File generated by OpenXava: Fri Mar 17 12:54:34 CET 2006
// Archivo generado por OpenXava: Fri Mar 17 12:54:34 CET 2006

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Invoice		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IInvoice  extends org.openxava.model.IModel {	

	// Properties/Propiedades 
	String getComment() throws RemoteException;
	void setComment(String comment) throws RemoteException; 
	java.util.Date getDate() throws RemoteException;
	void setDate(java.util.Date date) throws RemoteException; 
	boolean isPaid() throws RemoteException;
	void setPaid(boolean paid) throws RemoteException; 	
	java.math.BigDecimal getYearDiscount() throws RemoteException; 	
	int getDetailsCount() throws RemoteException; 	
	boolean isConsiderable() throws RemoteException; 	
	java.math.BigDecimal getSellerDiscount() throws RemoteException; 	
	java.math.BigDecimal getAmountsSum() throws RemoteException; 	
	String getImportance() throws RemoteException; 	
	int getYear() throws RemoteException; 	
	java.math.BigDecimal getCustomerDiscount() throws RemoteException; 
	java.math.BigDecimal getVatPercentage() throws RemoteException;
	void setVatPercentage(java.math.BigDecimal vatPercentage) throws RemoteException; 	
	java.math.BigDecimal getCustomerTypeDiscount() throws RemoteException; 	
	java.math.BigDecimal getVat() throws RemoteException; 	
	int getNumber() throws RemoteException;	

	java.util.Collection getDetails() throws RemoteException;	

	java.util.Collection getDeliveries() throws RemoteException;		

	// References/Referencias 

	// Customer : Reference/Referencia
	
	org.openxava.test.model.ICustomer getCustomer() throws RemoteException;
	void setCustomer(org.openxava.test.model.ICustomer newCustomer) throws RemoteException;

	// Methods 


}