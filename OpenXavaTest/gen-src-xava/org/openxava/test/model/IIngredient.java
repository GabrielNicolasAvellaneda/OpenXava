

// File generated by OpenXava: Mon Apr 29 14:27:46 CEST 2013
// Archivo generado por OpenXava: Mon Apr 29 14:27:46 CEST 2013

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Ingredient		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IIngredient  extends org.openxava.model.IModel {	

	// Properties/Propiedades 	
	public static final String PROPERTY_oid = "oid"; 	
	String getOid() throws RemoteException; 	
	public static final String PROPERTY_name = "name"; 
	String getName() throws RemoteException;
	void setName(String name) throws RemoteException;		

	// References/Referencias 

	// FavouriteFormula : Reference/Referencia
	
	org.openxava.test.model.IFormula getFavouriteFormula() throws RemoteException;
	void setFavouriteFormula(org.openxava.test.model.IFormula newFavouriteFormula) throws RemoteException; 

	// PartOf : Reference/Referencia
	
	org.openxava.test.model.IIngredient getPartOf() throws RemoteException;
	void setPartOf(org.openxava.test.model.IIngredient newPartOf) throws RemoteException;

	// Methods 


}