

// File generated by OpenXava: Mon Oct 18 09:51:06 CEST 2010
// Archivo generado por OpenXava: Mon Oct 18 09:51:06 CEST 2010

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Journey		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IJourney  extends org.openxava.model.IModel {	

	// Properties/Propiedades 	
	public static final String PROPERTY_oid = "oid"; 	
	String getOid() throws RemoteException; 	
	public static final String PROPERTY_description = "description"; 
	String getDescription() throws RemoteException;
	void setDescription(String description) throws RemoteException; 	
	public static final String PROPERTY_name = "name"; 
	String getName() throws RemoteException;
	void setName(String name) throws RemoteException;		

	// References/Referencias 

	// AverageSpeed : Reference/Referencia
	
	org.openxava.test.model.IAverageSpeed getAverageSpeed() throws RemoteException;
	void setAverageSpeed(org.openxava.test.model.IAverageSpeed newAverageSpeed) throws RemoteException;

	// Methods 


}