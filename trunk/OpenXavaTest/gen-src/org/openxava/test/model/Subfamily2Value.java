/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Value object for Subfamily2.
 *
 */
public class Subfamily2Value
   extends java.lang.Object
   implements java.io.Serializable 
{

   private static final long serialVersionUID = 1L;

   private java.lang.String remarks;
   private boolean remarksHasBeenSet = false;
   private java.lang.String description;
   private boolean descriptionHasBeenSet = false;
   private int number;
   private boolean numberHasBeenSet = false;
   private int family_number;
   private boolean family_numberHasBeenSet = false;

   public Subfamily2Value()
   {
   }

   //TODO Cloneable is better than this !
   public Subfamily2Value( Subfamily2Value otherValue )
   {
	  this.remarks = otherValue.remarks;
	  remarksHasBeenSet = true;
	  this.description = otherValue.description;
	  descriptionHasBeenSet = true;
	  this.number = otherValue.number;
	  numberHasBeenSet = true;
	  this.family_number = otherValue.family_number;
	  family_numberHasBeenSet = true;
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
   public int getFamily_number()
   {
	  return this.family_number;
   }

   public void setFamily_number( int family_number )
   {
	  this.family_number = family_number;
	  family_numberHasBeenSet = true;
   }

   public boolean family_numberHasBeenSet(){
	  return family_numberHasBeenSet;
   }

   public String toString()
   {
	  StringBuffer str = new StringBuffer("{");

	  str.append("remarks=" + getRemarks() + " " + "description=" + getDescription() + " " + "number=" + getNumber() + " " + "family_number=" + getFamily_number());
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
	  if (other instanceof Subfamily2Value)
	  {
		 Subfamily2Value that = (Subfamily2Value) other;
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
	  if (other instanceof Subfamily2Value)
	  {
		 Subfamily2Value that = (Subfamily2Value) other;
		 boolean lEquals = true;
		 if( this.remarks == null )
		 {
			lEquals = lEquals && ( that.remarks == null );
		 }
		 else
		 {
			lEquals = lEquals && this.remarks.equals( that.remarks );
		 }
		 if( this.description == null )
		 {
			lEquals = lEquals && ( that.description == null );
		 }
		 else
		 {
			lEquals = lEquals && this.description.equals( that.description );
		 }
		 lEquals = lEquals && this.family_number == that.family_number;

		 return lEquals;
	  }
	  else
	  {
		 return false;
	  }
   }

   public int hashCode(){
	  int result = 17;
      result = 37*result + ((this.remarks != null) ? this.remarks.hashCode() : 0);

      result = 37*result + ((this.description != null) ? this.description.hashCode() : 0);

      result = 37*result + (int) number;

      result = 37*result + (int) family_number;

	  return result;
   }

}