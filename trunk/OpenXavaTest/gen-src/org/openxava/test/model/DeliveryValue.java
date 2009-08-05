/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Value object for Delivery.
 *
 */
public class DeliveryValue
   extends java.lang.Object
   implements java.io.Serializable 
{

   private static final long serialVersionUID = 1L;

   private java.util.Date dateAsLabel;
   private boolean dateAsLabelHasBeenSet = false;
   private int distance;
   private boolean distanceHasBeenSet = false;
   private java.lang.String incidents;
   private boolean incidentsHasBeenSet = false;
   private java.lang.String driverType;
   private boolean driverTypeHasBeenSet = false;
   private java.lang.String remarks;
   private boolean remarksHasBeenSet = false;
   private java.lang.String employee;
   private boolean employeeHasBeenSet = false;
   private java.lang.String description;
   private boolean descriptionHasBeenSet = false;
   private java.lang.String vehicle;
   private boolean vehicleHasBeenSet = false;
   private java.lang.String transportMode;
   private boolean transportModeHasBeenSet = false;
   private java.util.Date date;
   private boolean dateHasBeenSet = false;
   private int number;
   private boolean numberHasBeenSet = false;
   private int type_number;
   private boolean type_numberHasBeenSet = false;
   private java.lang.String shipment_type;
   private boolean shipment_typeHasBeenSet = false;
   private int shipment_mode;
   private boolean shipment_modeHasBeenSet = false;
   private int shipment_number;
   private boolean shipment_numberHasBeenSet = false;
   private java.lang.Integer carrier_number;
   private boolean carrier_numberHasBeenSet = false;
   private int invoice_year;
   private boolean invoice_yearHasBeenSet = false;
   private int invoice_number;
   private boolean invoice_numberHasBeenSet = false;

   public DeliveryValue()
   {
   }

   //TODO Cloneable is better than this !
   public DeliveryValue( DeliveryValue otherValue )
   {
	  this.dateAsLabel = otherValue.dateAsLabel;
	  dateAsLabelHasBeenSet = true;
	  this.distance = otherValue.distance;
	  distanceHasBeenSet = true;
	  this.incidents = otherValue.incidents;
	  incidentsHasBeenSet = true;
	  this.driverType = otherValue.driverType;
	  driverTypeHasBeenSet = true;
	  this.remarks = otherValue.remarks;
	  remarksHasBeenSet = true;
	  this.employee = otherValue.employee;
	  employeeHasBeenSet = true;
	  this.description = otherValue.description;
	  descriptionHasBeenSet = true;
	  this.vehicle = otherValue.vehicle;
	  vehicleHasBeenSet = true;
	  this.transportMode = otherValue.transportMode;
	  transportModeHasBeenSet = true;
	  this.date = otherValue.date;
	  dateHasBeenSet = true;
	  this.number = otherValue.number;
	  numberHasBeenSet = true;
	  this.type_number = otherValue.type_number;
	  type_numberHasBeenSet = true;
	  this.shipment_type = otherValue.shipment_type;
	  shipment_typeHasBeenSet = true;
	  this.shipment_mode = otherValue.shipment_mode;
	  shipment_modeHasBeenSet = true;
	  this.shipment_number = otherValue.shipment_number;
	  shipment_numberHasBeenSet = true;
	  this.carrier_number = otherValue.carrier_number;
	  carrier_numberHasBeenSet = true;
	  this.invoice_year = otherValue.invoice_year;
	  invoice_yearHasBeenSet = true;
	  this.invoice_number = otherValue.invoice_number;
	  invoice_numberHasBeenSet = true;
   }

   public java.util.Date getDateAsLabel()
   {
	  return this.dateAsLabel;
   }

   public void setDateAsLabel( java.util.Date dateAsLabel )
   {
	  this.dateAsLabel = dateAsLabel;
	  dateAsLabelHasBeenSet = true;
   }

   public boolean dateAsLabelHasBeenSet(){
	  return dateAsLabelHasBeenSet;
   }
   public int getDistance()
   {
	  return this.distance;
   }

   public void setDistance( int distance )
   {
	  this.distance = distance;
	  distanceHasBeenSet = true;
   }

   public boolean distanceHasBeenSet(){
	  return distanceHasBeenSet;
   }
   public java.lang.String getIncidents()
   {
	  return this.incidents;
   }

   public void setIncidents( java.lang.String incidents )
   {
	  this.incidents = incidents;
	  incidentsHasBeenSet = true;
   }

   public boolean incidentsHasBeenSet(){
	  return incidentsHasBeenSet;
   }
   public java.lang.String getDriverType()
   {
	  return this.driverType;
   }

   public void setDriverType( java.lang.String driverType )
   {
	  this.driverType = driverType;
	  driverTypeHasBeenSet = true;
   }

   public boolean driverTypeHasBeenSet(){
	  return driverTypeHasBeenSet;
   }
   public java.lang.String getRemarks()
   {
	  return this.remarks;
   }

   public void setRemarks( java.lang.String remarks )
   {
	  this.remarks = remarks;
	  remarksHasBeenSet = true;
   }

   public boolean remarksHasBeenSet(){
	  return remarksHasBeenSet;
   }
   public java.lang.String getEmployee()
   {
	  return this.employee;
   }

   public void setEmployee( java.lang.String employee )
   {
	  this.employee = employee;
	  employeeHasBeenSet = true;
   }

   public boolean employeeHasBeenSet(){
	  return employeeHasBeenSet;
   }
   public java.lang.String getDescription()
   {
	  return this.description;
   }

   public void setDescription( java.lang.String description )
   {
	  this.description = description;
	  descriptionHasBeenSet = true;
   }

   public boolean descriptionHasBeenSet(){
	  return descriptionHasBeenSet;
   }
   public java.lang.String getVehicle()
   {
	  return this.vehicle;
   }

   public void setVehicle( java.lang.String vehicle )
   {
	  this.vehicle = vehicle;
	  vehicleHasBeenSet = true;
   }

   public boolean vehicleHasBeenSet(){
	  return vehicleHasBeenSet;
   }
   public java.lang.String getTransportMode()
   {
	  return this.transportMode;
   }

   public void setTransportMode( java.lang.String transportMode )
   {
	  this.transportMode = transportMode;
	  transportModeHasBeenSet = true;
   }

   public boolean transportModeHasBeenSet(){
	  return transportModeHasBeenSet;
   }
   public java.util.Date getDate()
   {
	  return this.date;
   }

   public void setDate( java.util.Date date )
   {
	  this.date = date;
	  dateHasBeenSet = true;
   }

   public boolean dateHasBeenSet(){
	  return dateHasBeenSet;
   }
   public int getNumber()
   {
	  return this.number;
   }

   public void setNumber( int number )
   {
	  this.number = number;
	  numberHasBeenSet = true;
   }

   public boolean numberHasBeenSet(){
	  return numberHasBeenSet;
   }
   public int getType_number()
   {
	  return this.type_number;
   }

   public void setType_number( int type_number )
   {
	  this.type_number = type_number;
	  type_numberHasBeenSet = true;
   }

   public boolean type_numberHasBeenSet(){
	  return type_numberHasBeenSet;
   }
   public java.lang.String getShipment_type()
   {
	  return this.shipment_type;
   }

   public void setShipment_type( java.lang.String shipment_type )
   {
	  this.shipment_type = shipment_type;
	  shipment_typeHasBeenSet = true;
   }

   public boolean shipment_typeHasBeenSet(){
	  return shipment_typeHasBeenSet;
   }
   public int getShipment_mode()
   {
	  return this.shipment_mode;
   }

   public void setShipment_mode( int shipment_mode )
   {
	  this.shipment_mode = shipment_mode;
	  shipment_modeHasBeenSet = true;
   }

   public boolean shipment_modeHasBeenSet(){
	  return shipment_modeHasBeenSet;
   }
   public int getShipment_number()
   {
	  return this.shipment_number;
   }

   public void setShipment_number( int shipment_number )
   {
	  this.shipment_number = shipment_number;
	  shipment_numberHasBeenSet = true;
   }

   public boolean shipment_numberHasBeenSet(){
	  return shipment_numberHasBeenSet;
   }
   public java.lang.Integer getCarrier_number()
   {
	  return this.carrier_number;
   }

   public void setCarrier_number( java.lang.Integer carrier_number )
   {
	  this.carrier_number = carrier_number;
	  carrier_numberHasBeenSet = true;
   }

   public boolean carrier_numberHasBeenSet(){
	  return carrier_numberHasBeenSet;
   }
   public int getInvoice_year()
   {
	  return this.invoice_year;
   }

   public void setInvoice_year( int invoice_year )
   {
	  this.invoice_year = invoice_year;
	  invoice_yearHasBeenSet = true;
   }

   public boolean invoice_yearHasBeenSet(){
	  return invoice_yearHasBeenSet;
   }
   public int getInvoice_number()
   {
	  return this.invoice_number;
   }

   public void setInvoice_number( int invoice_number )
   {
	  this.invoice_number = invoice_number;
	  invoice_numberHasBeenSet = true;
   }

   public boolean invoice_numberHasBeenSet(){
	  return invoice_numberHasBeenSet;
   }

   public String toString()
   {
	  StringBuffer str = new StringBuffer("{");

	  str.append("dateAsLabel=" + getDateAsLabel() + " " + "distance=" + getDistance() + " " + "incidents=" + getIncidents() + " " + "driverType=" + getDriverType() + " " + "remarks=" + getRemarks() + " " + "employee=" + getEmployee() + " " + "description=" + getDescription() + " " + "vehicle=" + getVehicle() + " " + "transportMode=" + getTransportMode() + " " + "date=" + getDate() + " " + "number=" + getNumber() + " " + "type_number=" + getType_number() + " " + "shipment_type=" + getShipment_type() + " " + "shipment_mode=" + getShipment_mode() + " " + "shipment_number=" + getShipment_number() + " " + "carrier_number=" + getCarrier_number() + " " + "invoice_year=" + getInvoice_year() + " " + "invoice_number=" + getInvoice_number());
	  str.append('}');

	  return(str.toString());
   }

   /**
    * A Value Object has an identity if the attributes making its Primary Key have all been set. An object without identity is never equal to any other object.
    *
    * @return true if this instance has an identity.
    */
   protected boolean hasIdentity()
   {
	  boolean ret = true;
	  ret = ret && numberHasBeenSet;
	  return ret;
   }

   public boolean equals(Object other)
   {
      if (this == other)
         return true;
	  if ( ! hasIdentity() ) return false;
	  if (other instanceof DeliveryValue)
	  {
		 DeliveryValue that = (DeliveryValue) other;
		 if ( ! that.hasIdentity() ) return false;
		 boolean lEquals = true;
		 lEquals = lEquals && this.number == that.number;

		 lEquals = lEquals && isIdentical(that);

		 return lEquals;
	  }
	  else
	  {
		 return false;
	  }
   }

   public boolean isIdentical(Object other)
   {
	  if (other instanceof DeliveryValue)
	  {
		 DeliveryValue that = (DeliveryValue) other;
		 boolean lEquals = true;
		 if( this.dateAsLabel == null )
		 {
			lEquals = lEquals && ( that.dateAsLabel == null );
		 }
		 else
		 {
			lEquals = lEquals && this.dateAsLabel.equals( that.dateAsLabel );
		 }
		 lEquals = lEquals && this.distance == that.distance;
		 if( this.incidents == null )
		 {
			lEquals = lEquals && ( that.incidents == null );
		 }
		 else
		 {
			lEquals = lEquals && this.incidents.equals( that.incidents );
		 }
		 if( this.driverType == null )
		 {
			lEquals = lEquals && ( that.driverType == null );
		 }
		 else
		 {
			lEquals = lEquals && this.driverType.equals( that.driverType );
		 }
		 if( this.remarks == null )
		 {
			lEquals = lEquals && ( that.remarks == null );
		 }
		 else
		 {
			lEquals = lEquals && this.remarks.equals( that.remarks );
		 }
		 if( this.employee == null )
		 {
			lEquals = lEquals && ( that.employee == null );
		 }
		 else
		 {
			lEquals = lEquals && this.employee.equals( that.employee );
		 }
		 if( this.description == null )
		 {
			lEquals = lEquals && ( that.description == null );
		 }
		 else
		 {
			lEquals = lEquals && this.description.equals( that.description );
		 }
		 if( this.vehicle == null )
		 {
			lEquals = lEquals && ( that.vehicle == null );
		 }
		 else
		 {
			lEquals = lEquals && this.vehicle.equals( that.vehicle );
		 }
		 if( this.transportMode == null )
		 {
			lEquals = lEquals && ( that.transportMode == null );
		 }
		 else
		 {
			lEquals = lEquals && this.transportMode.equals( that.transportMode );
		 }
		 if( this.date == null )
		 {
			lEquals = lEquals && ( that.date == null );
		 }
		 else
		 {
			lEquals = lEquals && this.date.equals( that.date );
		 }
		 lEquals = lEquals && this.type_number == that.type_number;
		 if( this.shipment_type == null )
		 {
			lEquals = lEquals && ( that.shipment_type == null );
		 }
		 else
		 {
			lEquals = lEquals && this.shipment_type.equals( that.shipment_type );
		 }
		 lEquals = lEquals && this.shipment_mode == that.shipment_mode;
		 lEquals = lEquals && this.shipment_number == that.shipment_number;
		 if( this.carrier_number == null )
		 {
			lEquals = lEquals && ( that.carrier_number == null );
		 }
		 else
		 {
			lEquals = lEquals && this.carrier_number.equals( that.carrier_number );
		 }
		 lEquals = lEquals && this.invoice_year == that.invoice_year;
		 lEquals = lEquals && this.invoice_number == that.invoice_number;

		 return lEquals;
	  }
	  else
	  {
		 return false;
	  }
   }

   public int hashCode(){
	  int result = 17;
      result = 37*result + ((this.dateAsLabel != null) ? this.dateAsLabel.hashCode() : 0);

      result = 37*result + (int) distance;

      result = 37*result + ((this.incidents != null) ? this.incidents.hashCode() : 0);

      result = 37*result + ((this.driverType != null) ? this.driverType.hashCode() : 0);

      result = 37*result + ((this.remarks != null) ? this.remarks.hashCode() : 0);

      result = 37*result + ((this.employee != null) ? this.employee.hashCode() : 0);

      result = 37*result + ((this.description != null) ? this.description.hashCode() : 0);

      result = 37*result + ((this.vehicle != null) ? this.vehicle.hashCode() : 0);

      result = 37*result + ((this.transportMode != null) ? this.transportMode.hashCode() : 0);

      result = 37*result + ((this.date != null) ? this.date.hashCode() : 0);

      result = 37*result + (int) number;

      result = 37*result + (int) type_number;

      result = 37*result + ((this.shipment_type != null) ? this.shipment_type.hashCode() : 0);

      result = 37*result + (int) shipment_mode;

      result = 37*result + (int) shipment_number;

      result = 37*result + ((this.carrier_number != null) ? this.carrier_number.hashCode() : 0);

      result = 37*result + (int) invoice_year;

      result = 37*result + (int) invoice_number;

	  return result;
   }

}