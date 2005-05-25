

// File generated by OpenXava: Wed May 25 12:25:33 CEST 2005
// Archivo generado por OpenXava: Wed May 25 12:25:33 CEST 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Seller		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.ejb;

import java.math.*;
import java.rmi.RemoteException;


public interface ISeller  {	

	// Properties/Propiedades 
	String getName() throws RemoteException;
	void setName(String name) throws RemoteException; 	
	int getNumber() throws RemoteException; 
	void addToCustomers(org.openxava.test.ejb.Customer newElement) throws RemoteException;
	void removeFromCustomers(org.openxava.test.ejb.Customer toRemove) throws RemoteException;	

	java.util.Collection getCustomers() throws RemoteException;		

	// References/Referencias 

	// Level : Reference/Referencia
	
	org.openxava.test.ejb.SellerLevel getLevel() throws RemoteException;
	void setLevel(org.openxava.test.ejb.SellerLevel newLevel) throws RemoteException;
	
	org.openxava.test.ejb.SellerLevelKey getLevelKey() throws RemoteException;
	void setLevelKey(org.openxava.test.ejb.SellerLevelKey key) throws RemoteException;

	// Methods 


}