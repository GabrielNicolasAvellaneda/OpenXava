

// File generated by OpenXava: Tue Mar 01 17:31:54 CET 2005
// Archivo generado por OpenXava: Tue Mar 01 17:31:54 CET 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Office		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.ejb;

import java.math.*;
import java.rmi.RemoteException;


public interface IOffice {	

	// Properties/Propiedades 
	String getName() throws RemoteException;
	void setName(String Name) throws RemoteException; 	
	int getNumber() throws RemoteException; 	
	int getZoneNumber() throws RemoteException;		

	// References/Referencias 

	// OfficeManager : Reference/Referencia
	
	org.openxava.test.ejb.Clerk getOfficeManager() throws RemoteException;
	void setOfficeManager(org.openxava.test.ejb.Clerk newOfficeManager) throws RemoteException;
	
	org.openxava.test.ejb.ClerkKey getOfficeManagerKey() throws RemoteException;
	void setOfficeManagerKey(org.openxava.test.ejb.ClerkKey key) throws RemoteException; 

	// DefaultCarrier : Reference/Referencia
	
	org.openxava.test.ejb.Carrier getDefaultCarrier() throws RemoteException;
	void setDefaultCarrier(org.openxava.test.ejb.Carrier newDefaultCarrier) throws RemoteException;
	
	org.openxava.test.ejb.CarrierKey getDefaultCarrierKey() throws RemoteException;
	void setDefaultCarrierKey(org.openxava.test.ejb.CarrierKey key) throws RemoteException; 

	// MainWarehouse : Reference/Referencia
	
	org.openxava.test.ejb.Warehouse2 getMainWarehouse() throws RemoteException;
	void setMainWarehouse(org.openxava.test.ejb.Warehouse2 newMainWarehouse) throws RemoteException;
	
	org.openxava.test.ejb.Warehouse2Key getMainWarehouseKey() throws RemoteException;
	void setMainWarehouseKey(org.openxava.test.ejb.Warehouse2Key key) throws RemoteException;

	// Methods 


}