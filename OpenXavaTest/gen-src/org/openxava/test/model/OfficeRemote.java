/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Remote interface for Office.
 */
public interface OfficeRemote
   extends org.openxava.ejbx.EJBReplicable, org.openxava.test.model.IOffice
{

   public int getReceptionist(  )
      throws java.rmi.RemoteException;

   public void setReceptionist( int newReceptionist )
      throws java.rmi.RemoteException;

   public java.lang.String getName(  )
      throws java.rmi.RemoteException;

   public void setName( java.lang.String newName )
      throws java.rmi.RemoteException;

   public int getZoneNumber(  )
      throws java.rmi.RemoteException;

   public void setZoneNumber( int newZoneNumber )
      throws java.rmi.RemoteException;

   public int getNumber(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.IClerk getOfficeManager(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.ClerkRemote getOfficeManagerRemote(  )
      throws java.rmi.RemoteException;

   public void setOfficeManager( org.openxava.test.model.IClerk newOfficeManager )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.ClerkKey getOfficeManagerKey(  )
      throws java.rmi.RemoteException;

   public void setOfficeManagerKey( org.openxava.test.model.ClerkKey key )
      throws java.rmi.RemoteException;

   public int getOfficeManager_zoneNumber(  )
      throws java.rmi.RemoteException;

   public int getOfficeManager_officeNumber(  )
      throws java.rmi.RemoteException;

   public int getOfficeManager_number(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.ICarrier getDefaultCarrier(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.CarrierRemote getDefaultCarrierRemote(  )
      throws java.rmi.RemoteException;

   public void setDefaultCarrier( org.openxava.test.model.ICarrier newDefaultCarrier )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.CarrierKey getDefaultCarrierKey(  )
      throws java.rmi.RemoteException;

   public void setDefaultCarrierKey( org.openxava.test.model.CarrierKey key )
      throws java.rmi.RemoteException;

   public java.lang.Integer getDefaultCarrier_number(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.IWarehouse getMainWarehouse(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.WarehouseRemote getMainWarehouseRemote(  )
      throws java.rmi.RemoteException;

   public void setMainWarehouse( org.openxava.test.model.IWarehouse newMainWarehouse )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.WarehouseKey getMainWarehouseKey(  )
      throws java.rmi.RemoteException;

   public void setMainWarehouseKey( org.openxava.test.model.WarehouseKey key )
      throws java.rmi.RemoteException;

   public int getMainWarehouse_zoneNumber(  )
      throws java.rmi.RemoteException;

   public java.lang.Integer getMainWarehouse_number(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.OfficeData getData(  )
      throws java.rmi.RemoteException;

   public void setData( org.openxava.test.model.OfficeData data )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.OfficeValue getOfficeValue(  )
      throws java.rmi.RemoteException;

   public void setOfficeValue( org.openxava.test.model.OfficeValue value )
      throws java.rmi.RemoteException;

}
