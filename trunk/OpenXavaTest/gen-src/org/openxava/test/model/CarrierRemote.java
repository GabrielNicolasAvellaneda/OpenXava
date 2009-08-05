/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Remote interface for Carrier.
 */
public interface CarrierRemote
   extends org.openxava.ejbx.EJBReplicable, org.openxava.test.model.ICarrier
{

   public java.lang.String getRemarks(  )
      throws java.rmi.RemoteException;

   public void setRemarks( java.lang.String newRemarks )
      throws java.rmi.RemoteException;

   public java.lang.String getCalculated(  )
      throws java.rmi.RemoteException;

   public java.lang.String getName(  )
      throws java.rmi.RemoteException;

   public void setName( java.lang.String newName )
      throws java.rmi.RemoteException;

   public int getNumber(  )
      throws java.rmi.RemoteException;

   public java.util.Collection getFellowCarriersCalculated(  )
      throws java.rmi.RemoteException;

   public java.util.Collection getFellowCarriers(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.IWarehouse getWarehouse(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.WarehouseRemote getWarehouseRemote(  )
      throws java.rmi.RemoteException;

   public void setWarehouse( org.openxava.test.model.IWarehouse newWarehouse )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.WarehouseKey getWarehouseKey(  )
      throws java.rmi.RemoteException;

   public void setWarehouseKey( org.openxava.test.model.WarehouseKey key )
      throws java.rmi.RemoteException;

   public int getWarehouse_zoneNumber(  )
      throws java.rmi.RemoteException;

   public java.lang.Integer getWarehouse_number(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.IDrivingLicence getDrivingLicence(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.DrivingLicenceRemote getDrivingLicenceRemote(  )
      throws java.rmi.RemoteException;

   public void setDrivingLicence( org.openxava.test.model.IDrivingLicence newDrivingLicence )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.DrivingLicenceKey getDrivingLicenceKey(  )
      throws java.rmi.RemoteException;

   public void setDrivingLicenceKey( org.openxava.test.model.DrivingLicenceKey key )
      throws java.rmi.RemoteException;

   public java.lang.String getDrivingLicence_type(  )
      throws java.rmi.RemoteException;

   public int getDrivingLicence_level(  )
      throws java.rmi.RemoteException;

   public void translateToEnglish(  )
      throws java.rmi.RemoteException;

   public void translate(  )
      throws java.rmi.RemoteException;

   public void translateToSpanish(  )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.CarrierData getData(  )
      throws java.rmi.RemoteException;

   public void setData( org.openxava.test.model.CarrierData data )
      throws java.rmi.RemoteException;

   public org.openxava.test.model.CarrierValue getCarrierValue(  )
      throws java.rmi.RemoteException;

   public void setCarrierValue( org.openxava.test.model.CarrierValue value )
      throws java.rmi.RemoteException;

}