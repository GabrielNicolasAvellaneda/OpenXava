/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.ejb;

/**
 * Remote interface for FilterBySubfamily.
 */
public interface FilterBySubfamily
   extends org.openxava.ejbx.EJBReplicable, org.openxava.test.ejb.IFilterBySubfamily
{

   public java.lang.String getOid(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.ejb.Subfamily2 getSubfamilyTo(  )
      throws java.rmi.RemoteException;

   public void setSubfamilyTo( org.openxava.test.ejb.Subfamily2 newSubfamilyTo )
      throws java.rmi.RemoteException;

   public org.openxava.test.ejb.Subfamily2Key getSubfamilyToKey(  )
      throws java.rmi.RemoteException;

   public void setSubfamilyToKey( org.openxava.test.ejb.Subfamily2Key key )
      throws java.rmi.RemoteException;

   public int getSubfamilyTo_number(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.ejb.Subfamily2 getSubfamily(  )
      throws java.rmi.RemoteException;

   public void setSubfamily( org.openxava.test.ejb.Subfamily2 newSubfamily )
      throws java.rmi.RemoteException;

   public org.openxava.test.ejb.Subfamily2Key getSubfamilyKey(  )
      throws java.rmi.RemoteException;

   public void setSubfamilyKey( org.openxava.test.ejb.Subfamily2Key key )
      throws java.rmi.RemoteException;

   public int getSubfamily_number(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.ejb.FilterBySubfamilyData getData(  )
      throws java.rmi.RemoteException;

   public void setData( org.openxava.test.ejb.FilterBySubfamilyData data )
      throws java.rmi.RemoteException;

   public org.openxava.test.ejb.FilterBySubfamilyValue getFilterBySubfamilyValue(  )
      throws java.rmi.RemoteException;

   public void setFilterBySubfamilyValue( org.openxava.test.ejb.FilterBySubfamilyValue value )
      throws java.rmi.RemoteException;

}
