

// File generated by OpenXava: Tue Aug 12 19:44:03 CEST 2008
// Archivo generado por OpenXava: Tue Aug 12 19:44:03 CEST 2008

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Vehicle		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IVehicle  extends org.openxava.model.IModel {	

	// Properties/Propiedades 	
	public static final String PROPERTY_model = "model"; 
	String getModel() throws RemoteException;
	void setModel(String model) throws RemoteException; 	
	public static final String PROPERTY_oid = "oid"; 	
	String getOid() throws RemoteException; 	
	public static final String PROPERTY_code = "code"; 
	String getCode() throws RemoteException;
	void setCode(String code) throws RemoteException; 	
	public static final String PROPERTY_make = "make"; 
	String getMake() throws RemoteException;
	void setMake(String make) throws RemoteException;		

	// References/Referencias

	// Methods 


}