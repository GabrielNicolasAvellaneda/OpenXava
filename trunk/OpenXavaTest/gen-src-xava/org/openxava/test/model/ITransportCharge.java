

// File generated by OpenXava: Tue Apr 15 17:29:07 CEST 2008
// Archivo generado por OpenXava: Tue Apr 15 17:29:07 CEST 2008

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: TransportCharge		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.model;

import java.math.*;
import java.rmi.RemoteException;


public interface ITransportCharge  extends org.openxava.model.IModel {	

	// Properties/Propiedades 	
	public static final String PROPERTY_amount = "amount"; 
	java.math.BigDecimal getAmount() throws RemoteException;
	void setAmount(java.math.BigDecimal amount) throws RemoteException;		

	// References/Referencias 

	// Delivery : Reference/Referencia
	
	org.openxava.test.model.IDelivery getDelivery() throws RemoteException;
	void setDelivery(org.openxava.test.model.IDelivery newDelivery) throws RemoteException;

	// Methods 


}