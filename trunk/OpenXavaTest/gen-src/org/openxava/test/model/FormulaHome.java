/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Home interface for Formula.
 */
public interface FormulaHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/Formula";
   public static final String JNDI_NAME="@subcontext@/ejb/org.openxava.test.model/Formula";

   public org.openxava.test.model.FormulaRemote create(java.util.Map values)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.FormulaRemote create(org.openxava.test.model.FormulaData data)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.FormulaRemote create(org.openxava.test.model.FormulaValue value)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.FormulaRemote findByName(java.lang.String name)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public org.openxava.test.model.FormulaRemote findByOid(java.lang.String oid)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public org.openxava.test.model.FormulaRemote findByPrimaryKey(org.openxava.test.model.FormulaKey pk)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

}
