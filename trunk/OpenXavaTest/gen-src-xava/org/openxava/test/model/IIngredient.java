

// File generated by OpenXava: Mon May 24 17:50:54 CEST 2010
// Archivo generado por OpenXava: Mon May 24 17:50:54 CEST 2010

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