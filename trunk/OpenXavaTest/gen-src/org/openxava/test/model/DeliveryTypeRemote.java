/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Remote interface for DeliveryType.
 */
public interface DeliveryTypeRemote
   extends org.openxava.ejbx.EJBReplicable, org.openxava.test.model.IDeliveryType
{

   public java.lang.String getDescription(  )
      throws java.rmi.RemoteException;

   public void setDescription( java.lang.String newDescription )
      throws java.rmi.RemoteException;

   public int getNumber(  )
      throws java.rmi.RemoteException;

   public java.util.Collection getDeliveries(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.DeliveryTypeData getData(  )
      throws java.rmi.RemoteException;

   public void setData( org.openxava.test.model.DeliveryTypeData data )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.DeliveryTypeValue getDeliveryTypeValue(  )
      throws java.rmi.RemoteException;

   public void setDeliveryTypeValue( org.openxava.test.model.DeliveryTypeValue value )
      throws java.rmi.RemoteException;

}
