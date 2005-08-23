

// File generated by OpenXava: Tue Aug 23 18:53:25 CEST 2005
// Archivo generado por OpenXava: Tue Aug 23 18:53:25 CEST 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Office		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IOffice  {	

	// Properties/Propiedades 
	int getReceptionist() throws RemoteException;
	void setReceptionist(int receptionist) throws RemoteException; 
	String getName() throws RemoteException;
	void setName(String name) throws RemoteException; 
	int getZoneNumber() throws RemoteException;
	void setZoneNumber(int zoneNumber) throws RemoteException; 	
	int getNumber() throws RemoteException;		

	// References/Referencias 

	// OfficeManager : Reference/Referencia
	
	org.openxava.test.model.IClerk getOfficeManager() throws RemoteException;
	void setOfficeManager(org.openxava.test.model.IClerk newOfficeManager) throws RemoteException;
	
	org.openxava.test.model.ClerkKey getOfficeManagerKey() throws RemoteException;
	void setOfficeManagerKey(org.openxava.test.model.ClerkKey key) throws RemoteException; 

	// DefaultCarrier : Reference/Referencia
	
	org.openxava.test.model.ICarrier getDefaultCarrier() throws RemoteException;
	void setDefaultCarrier(org.openxava.test.model.ICarrier newDefaultCarrier) throws RemoteException;
	
	org.openxava.test.model.CarrierKey getDefaultCarrierKey() throws RemoteException;
	void setDefaultCarrierKey(org.openxava.test.model.CarrierKey key) throws RemoteException; 

	// MainWarehouse : Reference/Referencia
	
	org.openxava.test.model.IWarehouse getMainWarehouse() throws RemoteException;
	void setMainWarehouse(org.openxava.test.model.IWarehouse newMainWarehouse) throws RemoteException;
	
	org.openxava.test.model.WarehouseKey getMainWarehouseKey() throws RemoteException;
	void setMainWarehouseKey(org.openxava.test.model.WarehouseKey key) throws RemoteException;

	// Methods 


}