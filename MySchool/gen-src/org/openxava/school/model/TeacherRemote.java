/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.school.model;

/**
 * Remote interface for Teacher.
 */
public interface TeacherRemote
   extends org.openxava.ejbx.EJBReplicable, org.openxava.school.model.ITeacher
{

   public java.lang.String getName(  )
      throws java.rmi.RemoteException;

   public void setName( java.lang.String newName )
      throws java.rmi.RemoteException;

   public java.lang.String getId(  )
      throws java.rmi.RemoteException;

   public org.openxava.school.model.TeacherData getData(  )
      throws java.rmi.RemoteException;

   public void setData( org.openxava.school.model.TeacherData data )
      throws java.rmi.RemoteException;

   public org.openxava.school.model.TeacherValue getTeacherValue(  )
      throws java.rmi.RemoteException;

   public void setTeacherValue( org.openxava.school.model.TeacherValue value )
      throws java.rmi.RemoteException;

}
