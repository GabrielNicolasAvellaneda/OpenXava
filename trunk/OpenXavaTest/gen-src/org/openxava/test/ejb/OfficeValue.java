/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.ejb;

/**
 * Value object for Office.
 *
 */
public class OfficeValue
   extends java.lang.Object
   implements java.io.Serializable 
{
   private java.lang.String name;
   private boolean nameHasBeenSet = false;
   private int number;
   private boolean numberHasBeenSet = false;
   private int zoneNumber;
   private boolean zoneNumberHasBeenSet = false;
   private int officeManager_zoneNumber;
   private boolean officeManager_zoneNumberHasBeenSet = false;
   private int officeManager_officeNumber;
   private boolean officeManager_officeNumberHasBeenSet = false;
   private int officeManager_number;
   private boolean officeManager_numberHasBeenSet = false;
   private java.lang.Integer defaultCarrier_number;
   private boolean defaultCarrier_numberHasBeenSet = false;
   private int mainWarehouse_zoneNumber;
   private boolean mainWarehouse_zoneNumberHasBeenSet = false;
   private java.lang.Integer mainWarehouse_number;
   private boolean mainWarehouse_numberHasBeenSet = false;

   // private org.openxava.test.ejb.OfficeKey pk;

   public OfficeValue()
   {
	 // pk = new org.openxava.test.ejb.OfficeKey();
   }

   /*
   public OfficeValue(org.openxava.test.ejb.OfficeKey pk)
   {
	  this.pk = pk;
   }
   */

   /*
   public OfficeValue( java.lang.String name,int number,int zoneNumber,int officeManager_zoneNumber,int officeManager_officeNumber,int officeManager_number,java.lang.Integer defaultCarrier_number,int mainWarehouse_zoneNumber,java.lang.Integer mainWarehouse_number )
   {
	  this.name = name;
	  nameHasBeenSet = true;
	  this.number = number;
	  numberHasBeenSet = true;
	  this.zoneNumber = zoneNumber;
	  zoneNumberHasBeenSet = true;
	  this.officeManager_zoneNumber = officeManager_zoneNumber;
	  officeManager_zoneNumberHasBeenSet = true;
	  this.officeManager_officeNumber = officeManager_officeNumber;
	  officeManager_officeNumberHasBeenSet = true;
	  this.officeManager_number = officeManager_number;
	  officeManager_numberHasBeenSet = true;
	  this.defaultCarrier_number = defaultCarrier_number;
	  defaultCarrier_numberHasBeenSet = true;
	  this.mainWarehouse_zoneNumber = mainWarehouse_zoneNumber;
	  mainWarehouse_zoneNumberHasBeenSet = true;
	  this.mainWarehouse_number = mainWarehouse_number;
	  mainWarehouse_numberHasBeenSet = true;
	  //pk = new org.openxava.test.ejb.OfficeKey(this.getNumber(),this.getZoneNumber());
   }
   */

   //TODO Cloneable is better than this !
   public OfficeValue( OfficeValue otherValue )
   {
	  this.name = otherValue.name;
	  nameHasBeenSet = true;
	  this.number = otherValue.number;
	  numberHasBeenSet = true;
	  this.zoneNumber = otherValue.zoneNumber;
	  zoneNumberHasBeenSet = true;
	  this.officeManager_zoneNumber = otherValue.officeManager_zoneNumber;
	  officeManager_zoneNumberHasBeenSet = true;
	  this.officeManager_officeNumber = otherValue.officeManager_officeNumber;
	  officeManager_officeNumberHasBeenSet = true;
	  this.officeManager_number = otherValue.officeManager_number;
	  officeManager_numberHasBeenSet = true;
	  this.defaultCarrier_number = otherValue.defaultCarrier_number;
	  defaultCarrier_numberHasBeenSet = true;
	  this.mainWarehouse_zoneNumber = otherValue.mainWarehouse_zoneNumber;
	  mainWarehouse_zoneNumberHasBeenSet = true;
	  this.mainWarehouse_number = otherValue.mainWarehouse_number;
	  mainWarehouse_numberHasBeenSet = true;

	  // pk = new org.openxava.test.ejb.OfficeKey(this.getNumber(),this.getZoneNumber());
   }

   /*
   public org.openxava.test.ejb.OfficeKey getPrimaryKey()
   {
	  return pk;
   }

   public void setPrimaryKey( org.openxava.test.ejb.OfficeKey pk )
   {
      // it's also nice to update PK object - just in case
      // somebody would ask for it later...
      this.pk = pk;
	  setNumber( pk.number );
	  setZoneNumber( pk.zoneNumber );
   }
   */

   public java.lang.String getName()
   {
	  return this.name;
   }

   public void setName( java.lang.String name )
   {
	  this.name = name;
	  nameHasBeenSet = true;

   }

   public boolean nameHasBeenSet(){
	  return nameHasBeenSet;
   }
   public int getNumber()
   {
	  return this.number;
   }

   public void setNumber( int number )
   {
	  this.number = number;
	  numberHasBeenSet = true;

		 //pk.setNumber(number);
   }

   public boolean numberHasBeenSet(){
	  return numberHasBeenSet;
   }
   public int getZoneNumber()
   {
	  return this.zoneNumber;
   }

   public void setZoneNumber( int zoneNumber )
   {
	  this.zoneNumber = zoneNumber;
	  zoneNumberHasBeenSet = true;

		 //pk.setZoneNumber(zoneNumber);
   }

   public boolean zoneNumberHasBeenSet(){
	  return zoneNumberHasBeenSet;
   }
   public int getOfficeManager_zoneNumber()
   {
	  return this.officeManager_zoneNumber;
   }

   public void setOfficeManager_zoneNumber( int officeManager_zoneNumber )
   {
	  this.officeManager_zoneNumber = officeManager_zoneNumber;
	  officeManager_zoneNumberHasBeenSet = true;

   }

   public boolean officeManager_zoneNumberHasBeenSet(){
	  return officeManager_zoneNumberHasBeenSet;
   }
   public int getOfficeManager_officeNumber()
   {
	  return this.officeManager_officeNumber;
   }

   public void setOfficeManager_officeNumber( int officeManager_officeNumber )
   {
	  this.officeManager_officeNumber = officeManager_officeNumber;
	  officeManager_officeNumberHasBeenSet = true;

   }

   public boolean officeManager_officeNumberHasBeenSet(){
	  return officeManager_officeNumberHasBeenSet;
   }
   public int getOfficeManager_number()
   {
	  return this.officeManager_number;
   }

   public void setOfficeManager_number( int officeManager_number )
   {
	  this.officeManager_number = officeManager_number;
	  officeManager_numberHasBeenSet = true;

   }

   public boolean officeManager_numberHasBeenSet(){
	  return officeManager_numberHasBeenSet;
   }
   public java.lang.Integer getDefaultCarrier_number()
   {
	  return this.defaultCarrier_number;
   }

   public void setDefaultCarrier_number( java.lang.Integer defaultCarrier_number )
   {
	  this.defaultCarrier_number = defaultCarrier_number;
	  defaultCarrier_numberHasBeenSet = true;

   }

   public boolean defaultCarrier_numberHasBeenSet(){
	  return defaultCarrier_numberHasBeenSet;
   }
   public int getMainWarehouse_zoneNumber()
   {
	  return this.mainWarehouse_zoneNumber;
   }

   public void setMainWarehouse_zoneNumber( int mainWarehouse_zoneNumber )
   {
	  this.mainWarehouse_zoneNumber = mainWarehouse_zoneNumber;
	  mainWarehouse_zoneNumberHasBeenSet = true;

   }

   public boolean mainWarehouse_zoneNumberHasBeenSet(){
	  return mainWarehouse_zoneNumberHasBeenSet;
   }
   public java.lang.Integer getMainWarehouse_number()
   {
	  return this.mainWarehouse_number;
   }

   public void setMainWarehouse_number( java.lang.Integer mainWarehouse_number )
   {
	  this.mainWarehouse_number = mainWarehouse_number;
	  mainWarehouse_numberHasBeenSet = true;

   }

   public boolean mainWarehouse_numberHasBeenSet(){
	  return mainWarehouse_numberHasBeenSet;
   }

   public String toString()
   {
	  StringBuffer str = new StringBuffer("{");

	  str.append("name=" + getName() + " " + "number=" + getNumber() + " " + "zoneNumber=" + getZoneNumber() + " " + "officeManager_zoneNumber=" + getOfficeManager_zoneNumber() + " " + "officeManager_officeNumber=" + getOfficeManager_officeNumber() + " " + "officeManager_number=" + getOfficeManager_number() + " " + "defaultCarrier_number=" + getDefaultCarrier_number() + " " + "mainWarehouse_zoneNumber=" + getMainWarehouse_zoneNumber() + " " + "mainWarehouse_number=" + getMainWarehouse_number());
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
	  ret = ret && zoneNumberHasBeenSet;
	  return ret;
   }

   public boolean equals(Object other)
   {
      if (this == other)
         return true;
	  if ( ! hasIdentity() ) return false;
	  if (other instanceof OfficeValue)
	  {
		 OfficeValue that = (OfficeValue) other;
		 if ( ! that.hasIdentity() ) return false;
		 boolean lEquals = true;
		 lEquals = lEquals && this.number == that.number;
		 lEquals = lEquals && this.zoneNumber == that.zoneNumber;

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
	  if (other instanceof OfficeValue)
	  {
		 OfficeValue that = (OfficeValue) other;
		 boolean lEquals = true;
		 if( this.name == null )
		 {
			lEquals = lEquals && ( that.name == null );
		 }
		 else
		 {
			lEquals = lEquals && this.name.equals( that.name );
		 }
		 lEquals = lEquals && this.officeManager_zoneNumber == that.officeManager_zoneNumber;
		 lEquals = lEquals && this.officeManager_officeNumber == that.officeManager_officeNumber;
		 lEquals = lEquals && this.officeManager_number == that.officeManager_number;
		 if( this.defaultCarrier_number == null )
		 {
			lEquals = lEquals && ( that.defaultCarrier_number == null );
		 }
		 else
		 {
			lEquals = lEquals && this.defaultCarrier_number.equals( that.defaultCarrier_number );
		 }
		 lEquals = lEquals && this.mainWarehouse_zoneNumber == that.mainWarehouse_zoneNumber;
		 if( this.mainWarehouse_number == null )
		 {
			lEquals = lEquals && ( that.mainWarehouse_number == null );
		 }
		 else
		 {
			lEquals = lEquals && this.mainWarehouse_number.equals( that.mainWarehouse_number );
		 }

		 return lEquals;
	  }
	  else
	  {
		 return false;
	  }
   }

   public int hashCode(){
	  int result = 17;
      result = 37*result + ((this.name != null) ? this.name.hashCode() : 0);

      result = 37*result + (int) number;

      result = 37*result + (int) zoneNumber;

      result = 37*result + (int) officeManager_zoneNumber;

      result = 37*result + (int) officeManager_officeNumber;

      result = 37*result + (int) officeManager_number;

      result = 37*result + ((this.defaultCarrier_number != null) ? this.defaultCarrier_number.hashCode() : 0);

      result = 37*result + (int) mainWarehouse_zoneNumber;

      result = 37*result + ((this.mainWarehouse_number != null) ? this.mainWarehouse_number.hashCode() : 0);

	  return result;
   }

}
