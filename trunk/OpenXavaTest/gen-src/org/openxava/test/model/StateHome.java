/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Home interface for State.
 */
public interface StateHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/State";
   public static final String JNDI_NAME="@subcontext@/ejb/org.openxava.test.model/State";

   public org.openxava.test.model.StateRemote create(java.util.Map values)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.StateRemote create(org.openxava.test.model.StateData data)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.StateRemote create(org.openxava.test.model.StateValue value)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.StateRemote findById(java.lang.String id)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public org.openxava.test.model.StateRemote findByPrimaryKey(org.openxava.test.model.StateKey pk)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

}