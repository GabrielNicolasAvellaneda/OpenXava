

// File generated by OpenXava: Mon May 24 17:50:54 CEST 2010
// Archivo generado por OpenXava: Mon May 24 17:50:54 CEST 2010

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: State		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IState  extends org.openxava.model.IModel {	

	// Properties/Propiedades 	
	public static final String PROPERTY_fullName = "fullName"; 	
	String getFullName() throws RemoteException; 	
	public static final String PROPERTY_name = "name"; 
	String getName() throws RemoteException;
	void setName(String name) throws RemoteException; 	
	public static final String PROPERTY_id = "id"; 	
	String getId() throws RemoteException;		

	// References/Referencias

	// Methods 


}