

// File generated by OpenXava: Tue Dec 13 12:49:16 CET 2011
// Archivo generado por OpenXava: Tue Dec 13 12:49:16 CET 2011

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Vehicle		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IVehicle  extends org.openxava.model.IModel {	

	// Properties/Propiedades 	
	public static final String PROPERTY_code = "code"; 
	String getCode() throws RemoteException;
	void setCode(String code) throws RemoteException; 	
	public static final String PROPERTY_oid = "oid"; 	
	String getOid() throws RemoteException; 	
	public static final String PROPERTY_make = "make"; 
	String getMake() throws RemoteException;
	void setMake(String make) throws RemoteException; 	
	public static final String PROPERTY_model = "model"; 
	String getModel() throws RemoteException;
	void setModel(String model) throws RemoteException;		

	// References/Referencias

	// Methods 


}