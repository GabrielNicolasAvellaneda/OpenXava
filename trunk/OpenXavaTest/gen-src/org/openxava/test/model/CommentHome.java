/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Home interface for Comment.
 */
public interface CommentHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/Comment";
   public static final String JNDI_NAME="@subcontext@/ejb/org.openxava.test.model/Comment";

   public org.openxava.test.model.CommentRemote create(org.openxava.test.model.IssueRemote container , int counter , java.util.Map values)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.CommentRemote create(org.openxava.test.model.IssueKey containerKey , int counter , java.util.Map values)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.CommentRemote create(org.openxava.test.model.IssueRemote container , int counter , org.openxava.test.model.CommentData data)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.CommentRemote create(org.openxava.test.model.IssueRemote container , int counter , org.openxava.test.model.CommentValue value)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.CommentRemote create(org.openxava.test.model.IssueKey containerKey , int counter , org.openxava.test.model.CommentValue value)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public java.util.Collection findByIssue(java.lang.String id)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public org.openxava.test.model.CommentRemote findByPrimaryKey(org.openxava.test.model.CommentKey pk)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

}
