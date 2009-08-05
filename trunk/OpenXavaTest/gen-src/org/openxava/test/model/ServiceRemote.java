/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Remote interface for Service.
 */
public interface ServiceRemote
   extends org.openxava.ejbx.EJBReplicable, org.openxava.test.model.IService
{

   public int getFamily(  )
      throws java.rmi.RemoteException;

   public void setFamily( int newFamily )
      throws java.rmi.RemoteException;

   public java.lang.String getDescription(  )
      throws java.rmi.RemoteException;

   public void setDescription( java.lang.String newDescription )
      throws java.rmi.RemoteException;

   public int getNumber(  )
      throws java.rmi.RemoteException;

   public java.util.Collection getAdditionalDetails(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.Detail getDetail(  )
      throws java.rmi.RemoteException;

   public void setDetail( org.openxava.test.model.Detail newDetail )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.IServiceType getDetail_typeRef(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.ServiceTypeRemote getDetail_typeRefRemote(  )
      throws java.rmi.RemoteException;

   public void setDetail_typeRef( org.openxava.test.model.IServiceType newDetail_typeRef )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.ServiceTypeKey getDetail_typeRefKey(  )
      throws java.rmi.RemoteException;

   public void setDetail_typeRefKey( org.openxava.test.model.ServiceTypeKey key )
      throws java.rmi.RemoteException;

   public int getDetail_typeRef_number(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.IServiceInvoice getInvoice(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.ServiceInvoiceRemote getInvoiceRemote(  )
      throws java.rmi.RemoteException;

   public void setInvoice( org.openxava.test.model.IServiceInvoice newInvoice )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.ServiceInvoiceKey getInvoiceKey(  )
      throws java.rmi.RemoteException;

   public void setInvoiceKey( org.openxava.test.model.ServiceInvoiceKey key )
      throws java.rmi.RemoteException;

   public java.lang.String getInvoice_oid(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.ServiceData getData(  )
      throws java.rmi.RemoteException;

   public void setData( org.openxava.test.model.ServiceData data )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.ServiceValue getServiceValue(  )
      throws java.rmi.RemoteException;

   public void setServiceValue( org.openxava.test.model.ServiceValue value )
      throws java.rmi.RemoteException;

}