

// File generated by OpenXava: Thu Jun 16 14:24:54 CEST 2011
// Archivo generado por OpenXava: Thu Jun 16 14:24:54 CEST 2011

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: CharacteristicThing		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface ICharacteristicThing  extends org.openxava.model.IModel {	

	// Properties/Propiedades 	
	public static final String PROPERTY_description = "description"; 
	String getDescription() throws RemoteException;
	void setDescription(String description) throws RemoteException; 	
	public static final String PROPERTY_number = "number"; 	
	int getNumber() throws RemoteException;		

	// References/Referencias 

	// Thing : Reference/Referencia
	
	org.openxava.test.model.IThing getThing() throws RemoteException;
	void setThing(org.openxava.test.model.IThing newThing) throws RemoteException;

	// Methods 


}