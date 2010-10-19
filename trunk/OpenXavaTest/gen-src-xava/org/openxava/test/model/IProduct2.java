

// File generated by OpenXava: Mon Oct 18 09:51:06 CEST 2010
// Archivo generado por OpenXava: Mon Oct 18 09:51:06 CEST 2010

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Product2		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IProduct2  extends org.openxava.model.IModel {	

	// Properties/Propiedades 	
	public static final String PROPERTY_photos = "photos"; 
	String getPhotos() throws RemoteException;
	void setPhotos(String photos) throws RemoteException; 	
	public static final String PROPERTY_unitPrice = "unitPrice"; 
	java.math.BigDecimal getUnitPrice() throws RemoteException;
	void setUnitPrice(java.math.BigDecimal unitPrice) throws RemoteException; 	
	public static final String PROPERTY_description = "description"; 
	String getDescription() throws RemoteException;
	void setDescription(String description) throws RemoteException; 	
	public static final String PROPERTY_unitPriceInPesetas = "unitPriceInPesetas"; 	
	java.math.BigDecimal getUnitPriceInPesetas() throws RemoteException; 	
	public static final String PROPERTY_unitPriceWithTax = "unitPriceWithTax"; 
	java.math.BigDecimal getUnitPriceWithTax() throws RemoteException;
	void setUnitPriceWithTax(java.math.BigDecimal unitPriceWithTax) throws RemoteException; 	
	public static final String PROPERTY_number = "number"; 	
	long getNumber() throws RemoteException;		

	// References/Referencias 

	// Warehouse : Reference/Referencia
	
	org.openxava.test.model.IWarehouse getWarehouse() throws RemoteException;
	void setWarehouse(org.openxava.test.model.IWarehouse newWarehouse) throws RemoteException; 

	// Color : Reference/Referencia
	
	org.openxava.test.model.IColor getColor() throws RemoteException;
	void setColor(org.openxava.test.model.IColor newColor) throws RemoteException; 

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