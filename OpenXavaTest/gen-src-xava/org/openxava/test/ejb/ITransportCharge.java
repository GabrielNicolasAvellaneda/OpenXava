

// File generated by OpenXava: Fri Aug 19 09:35:28 CEST 2005
// Archivo generado por OpenXava: Fri Aug 19 09:35:28 CEST 2005

// WARNING: NO EDIT
// OJO: NO EDITAR
// Component: TransportCharge		Java interface for entity/Interfaz java para Entidad

package org.openxava.test.ejb;

import java.math.*;
import java.rmi.RemoteException;


public interface ITransportCharge  {	

	// Properties/Propiedades 
	java.math.BigDecimal getAmount() throws RemoteException;
	void setAmount(java.math.BigDecimal amount) throws RemoteException;		

	// References/Referencias 

	// Delivery : Reference/Referencia
	
	org.openxava.test.ejb.Delivery getDelivery() throws RemoteException;
	void setDelivery(org.openxava.test.ejb.Delivery newDelivery) throws RemoteException;
	
	org.openxava.test.ejb.DeliveryKey getDeliveryKey() throws RemoteException;
	void setDeliveryKey(org.openxava.test.ejb.DeliveryKey key) throws RemoteException;

	// Methods 


}