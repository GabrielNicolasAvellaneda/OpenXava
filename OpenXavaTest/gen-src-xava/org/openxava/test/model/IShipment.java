

// File generated by OpenXava: Tue Nov 09 12:31:07 CET 2010
// Archivo generado por OpenXava: Tue Nov 09 12:31:07 CET 2010

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Shipment		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IShipment  extends org.openxava.model.IModel {	

	// Properties/Propiedades 	
	public static final String PROPERTY_type = "type"; 	
	int getType() throws RemoteException; 	
	public static final String PROPERTY_time = "time"; 
	java.sql.Timestamp getTime() throws RemoteException;
	void setTime(java.sql.Timestamp time) throws RemoteException; 	
	public static final String PROPERTY_description = "description"; 
	String getDescription() throws RemoteException;
	void setDescription(String description) throws RemoteException; 	
	public static final String PROPERTY_mode = "mode"; 	
	int getMode() throws RemoteException; 	
	public static final String PROPERTY_number = "number"; 	
	int getNumber() throws RemoteException;		

	// References/Referencias 

	// CustomerContactPerson : Reference/Referencia
	
	org.openxava.test.model.ICustomerContactPerson getCustomerContactPerson() throws RemoteException;
	void setCustomerContactPerson(org.openxava.test.model.ICustomerContactPerson newCustomerContactPerson) throws RemoteException;

	// Methods 


}