

// File generated by OpenXava: Fri Sep 15 12:44:20 CEST 2006
// Archivo generado por OpenXava: Fri Sep 15 12:44:20 CEST 2006

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Product2		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IProduct2  extends org.openxava.model.IModel {	

	// Properties/Propiedades 
	String getPhotos() throws RemoteException;
	void setPhotos(String photos) throws RemoteException; 
	java.math.BigDecimal getUnitPrice() throws RemoteException;
	void setUnitPrice(java.math.BigDecimal unitPrice) throws RemoteException; 
	String getDescription() throws RemoteException;
	void setDescription(String description) throws RemoteException; 	
	java.math.BigDecimal getUnitPriceInPesetas() throws RemoteException; 	
	long getNumber() throws RemoteException;		

	// References/Referencias 

	// Warehouse : Reference/Referencia
	
	org.openxava.test.model.IWarehouse getWarehouse() throws RemoteException;
	void setWarehouse(org.openxava.test.model.IWarehouse newWarehouse) throws RemoteException; 

	// Family : Reference/Referencia
	
	org.openxava.test.model.IFamily2 getFamily() throws RemoteException;
	void setFamily(org.openxava.test.model.IFamily2 newFamily) throws RemoteException; 

	// Subfamily : Reference/Referencia
	
	org.openxava.test.model.ISubfamily2 getSubfamily() throws RemoteException;
	void setSubfamily(org.openxava.test.model.ISubfamily2 newSubfamily) throws RemoteException; 

	// Formula : Reference/Referencia
	
	org.openxava.test.model.IFormula getFormula() throws RemoteException;
	void setFormula(org.openxava.test.model.IFormula newFormula) throws RemoteException;

	// Methods 


}