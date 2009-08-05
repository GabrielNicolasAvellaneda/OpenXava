/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Remote interface for Product3.
 */
public interface Product3Remote
   extends org.openxava.ejbx.EJBReplicable, org.openxava.test.model.IProduct3
{

   public java.lang.String getComments(  )
      throws java.rmi.RemoteException;

   public void setComments( java.lang.String newComments )
      throws java.rmi.RemoteException;

   public java.lang.String getDescription(  )
      throws java.rmi.RemoteException;

   public void setDescription( java.lang.String newDescription )
      throws java.rmi.RemoteException;

   public long getNumber(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.SubfamilyInfo getSubfamily1(  )
      throws java.rmi.RemoteException;

   public void setSubfamily1( org.openxava.test.model.SubfamilyInfo newSubfamily1 )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.IFamily2 getSubfamily1_family(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.Family2Remote getSubfamily1_familyRemote(  )
      throws java.rmi.RemoteException;

   public void setSubfamily1_family( org.openxava.test.model.IFamily2 newSubfamily1_family )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.Family2Key getSubfamily1_familyKey(  )
      throws java.rmi.RemoteException;

   public void setSubfamily1_familyKey( org.openxava.test.model.Family2Key key )
      throws java.rmi.RemoteException;

   public int getSubfamily1_family_number(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.ISubfamily2 getSubfamily1_subfamily(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.Subfamily2Remote getSubfamily1_subfamilyRemote(  )
      throws java.rmi.RemoteException;

   public void setSubfamily1_subfamily( org.openxava.test.model.ISubfamily2 newSubfamily1_subfamily )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.Subfamily2Key getSubfamily1_subfamilyKey(  )
      throws java.rmi.RemoteException;

   public void setSubfamily1_subfamilyKey( org.openxava.test.model.Subfamily2Key key )
      throws java.rmi.RemoteException;

   public int getSubfamily1_subfamily_number(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.IFamily getFamily(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.FamilyRemote getFamilyRemote(  )
      throws java.rmi.RemoteException;

   public void setFamily( org.openxava.test.model.IFamily newFamily )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.FamilyKey getFamilyKey(  )
      throws java.rmi.RemoteException;

   public void setFamilyKey( org.openxava.test.model.FamilyKey key )
      throws java.rmi.RemoteException;

   public java.lang.String getFamily_oid(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.SubfamilyInfo getSubfamily2(  )
      throws java.rmi.RemoteException;

   public void setSubfamily2( org.openxava.test.model.SubfamilyInfo newSubfamily2 )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.IFamily2 getSubfamily2_family(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.Family2Remote getSubfamily2_familyRemote(  )
      throws java.rmi.RemoteException;

   public void setSubfamily2_family( org.openxava.test.model.IFamily2 newSubfamily2_family )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.Family2Key getSubfamily2_familyKey(  )
      throws java.rmi.RemoteException;

   public void setSubfamily2_familyKey( org.openxava.test.model.Family2Key key )
      throws java.rmi.RemoteException;

   public int getSubfamily2_family_number(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.ISubfamily2 getSubfamily2_subfamily(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.Subfamily2Remote getSubfamily2_subfamilyRemote(  )
      throws java.rmi.RemoteException;

   public void setSubfamily2_subfamily( org.openxava.test.model.ISubfamily2 newSubfamily2_subfamily )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.Subfamily2Key getSubfamily2_subfamilyKey(  )
      throws java.rmi.RemoteException;

   public void setSubfamily2_subfamilyKey( org.openxava.test.model.Subfamily2Key key )
      throws java.rmi.RemoteException;

   public int getSubfamily2_subfamily_number(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.Product3Data getData(  )
      throws java.rmi.RemoteException;

   public void setData( org.openxava.test.model.Product3Data data )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.Product3Value getProduct3Value(  )
      throws java.rmi.RemoteException;

   public void setProduct3Value( org.openxava.test.model.Product3Value value )
      throws java.rmi.RemoteException;

}