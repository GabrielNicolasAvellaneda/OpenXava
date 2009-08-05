/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Home interface for Course.
 */
public interface CourseHome
   extends javax.ejb.EJBHome
{
   public static final String COMP_NAME="java:comp/env/ejb/Course";
   public static final String JNDI_NAME="@subcontext@/ejb/org.openxava.test.model/Course";

   public org.openxava.test.model.CourseRemote create(java.util.Map values)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.CourseRemote create(org.openxava.test.model.CourseData data)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.CourseRemote create(org.openxava.test.model.CourseValue value)
      throws javax.ejb.CreateException,org.openxava.validators.ValidationException,java.rmi.RemoteException;

   public org.openxava.test.model.CourseRemote findByYearNumber(int year,int number)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

   public org.openxava.test.model.CourseRemote findByPrimaryKey(org.openxava.test.model.CourseKey pk)
      throws javax.ejb.FinderException,java.rmi.RemoteException;

}