

// File generated by OpenXava: Mon Apr 30 18:14:56 CEST 2007
// Archivo generado por OpenXava: Mon Apr 30 18:14:56 CEST 2007

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Formula		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IFormula  extends org.openxava.model.IModel {	

	// Properties/Propiedades 	
	String getOid() throws RemoteException; 
	java.lang.String getRecipe() throws RemoteException;
	void setRecipe(java.lang.String recipe) throws RemoteException; 
	String getName() throws RemoteException;
	void setName(String name) throws RemoteException;	

	java.util.Collection getIngredients() throws RemoteException;		

	// References/Referencias

	// Methods 


}