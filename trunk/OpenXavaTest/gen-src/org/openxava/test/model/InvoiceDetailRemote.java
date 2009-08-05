/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Remote interface for InvoiceDetail.
 */
public interface InvoiceDetailRemote
   extends org.openxava.ejbx.EJBReplicable, org.openxava.test.model.IInvoiceDetail
{

   public boolean isFree(  )
      throws java.rmi.RemoteException;

   public java.math.BigDecimal getAmount(  )
      throws java.rmi.RemoteException;

   public java.math.BigDecimal getUnitPrice(  )
      throws java.rmi.RemoteException;

   public void setUnitPrice( java.math.BigDecimal newUnitPrice )
      throws java.rmi.RemoteException;

   public java.lang.String getOid(  )
      throws java.rmi.RemoteException;

   public java.lang.String getRemarks(  )
      throws java.rmi.RemoteException;

   public void setRemarks( java.lang.String newRemarks )
      throws java.rmi.RemoteException;

   public java.util.Date getDeliveryDate(  )
      throws java.rmi.RemoteException;

   public void setDeliveryDate( java.util.Date newDeliveryDate )
      throws java.rmi.RemoteException;

   public int getQuantity(  )
      throws java.rmi.RemoteException;

   public void setQuantity( int newQuantity )
      throws java.rmi.RemoteException;

   public int getServiceType(  )
      throws java.rmi.RemoteException;

   public void setServiceType( int newServiceType )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.IProduct getProduct(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.ProductRemote getProductRemote(  )
      throws java.rmi.RemoteException;

   public void setProduct( org.openxava.test.model.IProduct newProduct )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.ProductKey getProductKey(  )
      throws java.rmi.RemoteException;

   public void setProductKey( org.openxava.test.model.ProductKey key )
      throws java.rmi.RemoteException;

   public long getProduct_number(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.ISeller getSoldBy(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.SellerRemote getSoldByRemote(  )
      throws java.rmi.RemoteException;

   public void setSoldBy( org.openxava.test.model.ISeller newSoldBy )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.SellerKey getSoldByKey(  )
      throws java.rmi.RemoteException;

   public void setSoldByKey( org.openxava.test.model.SellerKey key )
      throws java.rmi.RemoteException;

   public int getSoldBy_number(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.IInvoice getInvoice(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.InvoiceRemote getInvoiceRemote(  )
      throws java.rmi.RemoteException;

   public void setInvoice( org.openxava.test.model.IInvoice newInvoice )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.InvoiceKey getInvoiceKey(  )
      throws java.rmi.RemoteException;

   public void setInvoiceKey( org.openxava.test.model.InvoiceKey key )
      throws java.rmi.RemoteException;

   public int getInvoice_year(  )
      throws java.rmi.RemoteException;

   public int getInvoice_number(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.InvoiceDetailData getData(  )
      throws java.rmi.RemoteException;

   public void setData( org.openxava.test.model.InvoiceDetailData data )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.InvoiceDetailValue getInvoiceDetailValue(  )
      throws java.rmi.RemoteException;

   public void setInvoiceDetailValue( org.openxava.test.model.InvoiceDetailValue value )
      throws java.rmi.RemoteException;

}