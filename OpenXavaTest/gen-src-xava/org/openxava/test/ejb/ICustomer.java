

// File generated by OpenXava: Tue May 17 13:11:45 CEST 2005
// Archivo generado por OpenXava: Tue May 17 13:11:45 CEST 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Customer		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.ejb;

import java.math.*;
import java.rmi.RemoteException;


public interface ICustomer  extends org.openxava.test.ejb.IWithName {	

	// Properties/Propiedades 
	int getType() throws RemoteException;
	void setType(int type) throws RemoteException; 
	java.lang.String getRemarks() throws RemoteException;
	void setRemarks(java.lang.String remarks) throws RemoteException; 
	String getRelationWithSeller() throws RemoteException;
	void setRelationWithSeller(String relationWithSeller) throws RemoteException; 
	byte[] getPhoto() throws RemoteException;
	void setPhoto(byte[] photo) throws RemoteException; 
	String getName() throws RemoteException;
	void setName(String name) throws RemoteException; 	
	int getNumber() throws RemoteException;	

	java.util.Collection getDeliveryPlaces() throws RemoteException;		

	// References/Referencias 

	// Seller : Reference/Referencia
	
	org.openxava.test.ejb.Seller getSeller() throws RemoteException;
	void setSeller(org.openxava.test.ejb.Seller newSeller) throws RemoteException;
	
	org.openxava.test.ejb.SellerKey getSellerKey() throws RemoteException;
	void setSellerKey(org.openxava.test.ejb.SellerKey key) throws RemoteException; 

	// AlternateSeller : Reference/Referencia
	
	org.openxava.test.ejb.Seller getAlternateSeller() throws RemoteException;
	void setAlternateSeller(org.openxava.test.ejb.Seller newAlternateSeller) throws RemoteException;
	
	org.openxava.test.ejb.SellerKey getAlternateSellerKey() throws RemoteException;
	void setAlternateSellerKey(org.openxava.test.ejb.SellerKey key) throws RemoteException;  	
	// Address : Aggregate 
	
	org.openxava.test.ejb.Address getAddress() throws RemoteException;
	void setAddress(org.openxava.test.ejb.Address newAddress) throws RemoteException;

	// Methods 


}