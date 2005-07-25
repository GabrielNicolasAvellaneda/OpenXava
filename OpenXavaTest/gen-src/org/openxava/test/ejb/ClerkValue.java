/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.ejb;

/**
 * Value object for Clerk.
 *
 */
public class ClerkValue
   extends java.lang.Object
   implements java.io.Serializable 
{

   private static final long serialVersionUID = 1L;

   private java.lang.String name;
   private boolean nameHasBeenSet = false;
   private int officeNumber;
   private boolean officeNumberHasBeenSet = false;
   private int number;
   private boolean numberHasBeenSet = false;
   private int zoneNumber;
   private boolean zoneNumberHasBeenSet = false;

   public ClerkValue()
   {
   }

   //TODO Cloneable is better than this !
   public ClerkValue( ClerkValue otherValue )
   {
	  this.name = otherValue.name;
	  nameHasBeenSet = true;
	  this.officeNumber = otherValue.officeNumber;
	  officeNumberHasBeenSet = true;
	  this.number = otherValue.number;
	  numberHasBeenSet = true;
	  this.zoneNumber = otherValue.zoneNumber;
	  zoneNumberHasBeenSet = true;
   }

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
   public int getOfficeNumber()
   {
	  return this.officeNumber;
   }

   public void setOfficeNumber( int officeNumber )
   {
	  this.officeNumber = officeNumber;
	  officeNumberHasBeenSet = true;
   }

   public boolean officeNumberHasBeenSet(){
	  return officeNumberHasBeenSet;
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
   public int getZoneNumber()
   {
	  return this.zoneNumber;
   }

   public void setZoneNumber( int zoneNumber )
   {
	  this.zoneNumber = zoneNumber;
	  zoneNumberHasBeenSet = true;
   }

   public boolean zoneNumberHasBeenSet(){
	  return zoneNumberHasBeenSet;
   }

   public String toString()
   {
	  StringBuffer str = new StringBuffer("{");

	  str.append("name=" + getName() + " " + "officeNumber=" + getOfficeNumber() + " " + "number=" + getNumber() + " " + "zoneNumber=" + getZoneNumber());
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
	  ret = ret && officeNumberHasBeenSet;
	  ret = ret && numberHasBeenSet;
	  ret = ret && zoneNumberHasBeenSet;
	  return ret;
   }

   public boolean equals(Object other)
   {
      if (this == other)
         return true;
	  if ( ! hasIdentity() ) return false;
	  if (other instanceof ClerkValue)
	  {
		 ClerkValue that = (ClerkValue) other;
		 if ( ! that.hasIdentity() ) return false;
		 boolean lEquals = true;
		 lEquals = lEquals && this.officeNumber == that.officeNumber;
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
	  if (other instanceof ClerkValue)
	  {
		 ClerkValue that = (ClerkValue) other;
		 boolean lEquals = true;
		 if( this.name == null )
		 {
			lEquals = lEquals && ( that.name == null );
		 }
		 else
		 {
			lEquals = lEquals && this.name.equals( that.name );
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

      result = 37*result + (int) officeNumber;

      result = 37*result + (int) number;

      result = 37*result + (int) zoneNumber;

	  return result;
   }

}
