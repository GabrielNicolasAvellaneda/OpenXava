/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.ejb;

/**
 * Home interface for Delivery.
 */
public interface DeliveryHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/Delivery";
   public static final String JNDI_NAME="@subcontext@/ejb/org.openxava.test.ejb/Delivery";

   public org.openxava.test.ejb.Delivery create(java.util.Map values)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.ejb.Delivery create(org.openxava.test.ejb.DeliveryData data)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.ejb.Delivery create(org.openxava.test.ejb.DeliveryValue value)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public java.util.Collection findByType(int number)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public java.util.Collection findByShipment(java.lang.String type, int mode, int number)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public java.util.Collection findByCarrier(java.lang.Integer number)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public java.util.Collection findByInvoice(int year, int number)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public java.util.Collection findAll()
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public org.openxava.test.ejb.Delivery findByPrimaryKey(org.openxava.test.ejb.DeliveryKey pk)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

}
