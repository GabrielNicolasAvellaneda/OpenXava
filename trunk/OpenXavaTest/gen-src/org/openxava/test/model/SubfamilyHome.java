/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Home interface for Subfamily.
 */
public interface SubfamilyHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/Subfamily";
   public static final String JNDI_NAME="@subcontext@/ejb/org.openxava.test.model/Subfamily";

   public org.openxava.test.model.SubfamilyRemote create(java.util.Map values)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.SubfamilyRemote create(org.openxava.test.model.SubfamilyData data)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.SubfamilyRemote create(org.openxava.test.model.SubfamilyValue value)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.SubfamilyRemote findByOid(java.lang.String oid)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public org.openxava.test.model.SubfamilyRemote findByPrimaryKey(org.openxava.test.model.SubfamilyKey pk)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

}
