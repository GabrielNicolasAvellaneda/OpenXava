/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Home interface for DeliveryType.
 */
public interface DeliveryTypeHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/DeliveryType";
   public static final String JNDI_NAME="@subcontext@/ejb/org.openxava.test.model/DeliveryType";

   public org.openxava.test.model.DeliveryTypeRemote create(java.util.Map values)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.DeliveryTypeRemote create(org.openxava.test.model.DeliveryTypeData data)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.DeliveryTypeRemote create(org.openxava.test.model.DeliveryTypeValue value)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.DeliveryTypeRemote findByNumber(int number)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public org.openxava.test.model.DeliveryTypeRemote findByPrimaryKey(org.openxava.test.model.DeliveryTypeKey pk)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

}
