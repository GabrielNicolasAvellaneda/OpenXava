

// File generated by OpenXava: Fri Oct 28 13:33:20 CEST 2005
// Archivo generado por OpenXava: Fri Oct 28 13:33:20 CEST 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Office2		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface IOffice2  {	

	// Properties/Propiedades 
	int getReceptionist() throws RemoteException;
	void setReceptionist(int receptionist) throws RemoteException; 
	String getName() throws RemoteException;
	void setName(String name) throws RemoteException; 	
	int getZoneNumber() throws RemoteException; 	
	int getNumber() throws RemoteException;		

	// References/Referencias 

	// OfficeManager : Reference/Referencia
	
	org.openxava.test.model.IClerk getOfficeManager() throws RemoteException;
	void setOfficeManager(org.openxava.test.model.IClerk newOfficeManager) throws RemoteException; 

	// DefaultCarrier : Reference/Referencia
	
	org.openxava.test.model.ICarrier getDefaultCarrier() throws RemoteException;
	void setDefaultCarrier(org.openxava.test.model.ICarrier newDefaultCarrier) throws RemoteException; 

	// MainWarehouse : Reference/Referencia
	
	org.openxava.test.model.IWarehouse getMainWarehouse() throws RemoteException;
	void setMainWarehouse(org.openxava.test.model.IWarehouse newMainWarehouse) throws RemoteException;

	// Methods 


}