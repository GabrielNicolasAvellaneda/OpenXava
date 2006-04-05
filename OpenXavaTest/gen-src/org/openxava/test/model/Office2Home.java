/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Home interface for Office2.
 */
public interface Office2Home
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/Office2";
   public static final String JNDI_NAME="@subcontext@/ejb/org.openxava.test.model/Office2";

   public org.openxava.test.model.Office2Remote create(java.util.Map values)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.Office2Remote create(org.openxava.test.model.Office2Data data)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.Office2Remote create(org.openxava.test.model.Office2Value value)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public java.util.Collection findByOfficeManager(int zoneNumber, int officeNumber, int number)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public java.util.Collection findByDefaultCarrier(java.lang.Integer number)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public java.util.Collection findByMainWarehouse(int zoneNumber, java.lang.Integer number)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public org.openxava.test.model.Office2Remote findByPrimaryKey(org.openxava.test.model.Office2Key pk)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

}
