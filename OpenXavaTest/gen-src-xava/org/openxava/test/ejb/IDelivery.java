

// File generated by OpenXava: Tue Jun 07 18:44:37 CEST 2005
// Archivo generado por OpenXava: Tue Jun 07 18:44:37 CEST 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: Delivery		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.ejb;

import java.math.*;
import java.rmi.RemoteException;


public interface IDelivery  {	

	// Properties/Propiedades 	
	java.util.Date getDateAsLabel() throws RemoteException; 
	int getDistance() throws RemoteException;
	void setDistance(int distance) throws RemoteException; 
	java.lang.String getIncidents() throws RemoteException;
	void setIncidents(java.lang.String incidents) throws RemoteException; 
	String getDriverType() throws RemoteException;
	void setDriverType(String driverType) throws RemoteException; 
	java.lang.String getRemarks() throws RemoteException;
	void setRemarks(java.lang.String remarks) throws RemoteException; 
	String getEmployee() throws RemoteException;
	void setEmployee(String employee) throws RemoteException; 
	String getDescription() throws RemoteException;
	void setDescription(String description) throws RemoteException; 
	String getVehicle() throws RemoteException;
	void setVehicle(String vehicle) throws RemoteException; 	
	String getTransportMode() throws RemoteException; 
	java.util.Date getDate() throws RemoteException;
	void setDate(java.util.Date date) throws RemoteException; 	
	int getNumber() throws RemoteException;	

	java.util.Collection getDetails() throws RemoteException;		

	// References/Referencias 

	// Type : Reference/Referencia
	
	org.openxava.test.ejb.DeliveryType getType() throws RemoteException;
	void setType(org.openxava.test.ejb.DeliveryType newType) throws RemoteException;
	
	org.openxava.test.ejb.DeliveryTypeKey getTypeKey() throws RemoteException;
	void setTypeKey(org.openxava.test.ejb.DeliveryTypeKey key) throws RemoteException; 

	// Shipment : Reference/Referencia
	
	org.openxava.test.ejb.Shipment getShipment() throws RemoteException;
	void setShipment(org.openxava.test.ejb.Shipment newShipment) throws RemoteException;
	
	org.openxava.test.ejb.ShipmentKey getShipmentKey() throws RemoteException;
	void setShipmentKey(org.openxava.test.ejb.ShipmentKey key) throws RemoteException; 

	// Carrier : Reference/Referencia
	
	org.openxava.test.ejb.Carrier getCarrier() throws RemoteException;
	void setCarrier(org.openxava.test.ejb.Carrier newCarrier) throws RemoteException;
	
	org.openxava.test.ejb.CarrierKey getCarrierKey() throws RemoteException;
	void setCarrierKey(org.openxava.test.ejb.CarrierKey key) throws RemoteException; 

	// Invoice : Reference/Referencia
	
	org.openxava.test.ejb.Invoice getInvoice() throws RemoteException;
	void setInvoice(org.openxava.test.ejb.Invoice newInvoice) throws RemoteException;
	
	org.openxava.test.ejb.InvoiceKey getInvoiceKey() throws RemoteException;
	void setInvoiceKey(org.openxava.test.ejb.InvoiceKey key) throws RemoteException;

	// Methods 


}