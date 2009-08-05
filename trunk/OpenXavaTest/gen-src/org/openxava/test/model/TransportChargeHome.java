/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Home interface for TransportCharge.
 */
public interface TransportChargeHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/TransportCharge";
   public static final String JNDI_NAME="@subcontext@/ejb/org.openxava.test.model/TransportCharge";

   public org.openxava.test.model.TransportChargeRemote create(java.util.Map values)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.TransportChargeRemote create(org.openxava.test.model.TransportChargeData data)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.TransportChargeRemote create(org.openxava.test.model.TransportChargeValue value)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public java.util.Collection findByDelivery(int invoice_year, int invoice_number, int type_number, int number)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public java.util.Collection findAll()
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public org.openxava.test.model.TransportChargeRemote findByPrimaryKey(org.openxava.test.model.TransportChargeKey pk)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

}