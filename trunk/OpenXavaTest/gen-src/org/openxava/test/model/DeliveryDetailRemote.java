/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Remote interface for DeliveryDetail.
 */
public interface DeliveryDetailRemote
   extends org.openxava.ejbx.EJBReplicable, org.openxava.test.model.IDeliveryDetail
{

   public java.lang.String getDescription(  )
      throws java.rmi.RemoteException;

   public void setDescription( java.lang.String newDescription )
      throws java.rmi.RemoteException;

   public int getNumber(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.IDelivery getDelivery(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.DeliveryRemote getDeliveryRemote(  )
      throws java.rmi.RemoteException;

   public void setDelivery( org.openxava.test.model.IDelivery newDelivery )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.DeliveryKey getDeliveryKey(  )
      throws java.rmi.RemoteException;

   public void setDeliveryKey( org.openxava.test.model.DeliveryKey key )
      throws java.rmi.RemoteException;

   public int getDelivery_number(  )
      throws java.rmi.RemoteException;

   public int getDelivery_type_number(  )
      throws java.rmi.RemoteException;

   public int getDelivery_invoice_year(  )
      throws java.rmi.RemoteException;

   public int getDelivery_invoice_number(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.DeliveryDetailData getData(  )
      throws java.rmi.RemoteException;

   public void setData( org.openxava.test.model.DeliveryDetailData data )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.DeliveryDetailValue getDeliveryDetailValue(  )
      throws java.rmi.RemoteException;

   public void setDeliveryDetailValue( org.openxava.test.model.DeliveryDetailValue value )
      throws java.rmi.RemoteException;

}