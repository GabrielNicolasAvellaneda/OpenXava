/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.ejb;

/**
 * Value object for AdditionalDetail.
 *
 */
public class AdditionalDetailValue
   extends java.lang.Object
   implements java.io.Serializable 
{

   private static final long serialVersionUID = 1L;

   private int counter;
   private boolean counterHasBeenSet = false;
   private int subfamily;
   private boolean subfamilyHasBeenSet = false;
   private int service_number;
   private boolean service_numberHasBeenSet = false;
   private int type_number;
   private boolean type_numberHasBeenSet = false;

   public AdditionalDetailValue()
   {
   }

   //TODO Cloneable is better than this !
   public AdditionalDetailValue( AdditionalDetailValue otherValue )
   {
	  this.counter = otherValue.counter;
	  counterHasBeenSet = true;
	  this.subfamily = otherValue.subfamily;
	  subfamilyHasBeenSet = true;
	  this.service_number = otherValue.service_number;
	  service_numberHasBeenSet = true;
	  this.type_number = otherValue.type_number;
	  type_numberHasBeenSet = true;
   }

   public int getCounter()
   {
	  return this.counter;
   }

   public void setCounter( int counter )
   {
	  this.counter = counter;
	  counterHasBeenSet = true;
   }

   public boolean counterHasBeenSet(){
	  return counterHasBeenSet;
   }
   public int getSubfamily()
   {
	  return this.subfamily;
   }

   public void setSubfamily( int subfamily )
   {
	  this.subfamily = subfamily;
	  subfamilyHasBeenSet = true;
   }

   public boolean subfamilyHasBeenSet(){
	  return subfamilyHasBeenSet;
   }
   public int getService_number()
   {
	  return this.service_number;
   }

   public void setService_number( int service_number )
   {
	  this.service_number = service_number;
	  service_numberHasBeenSet = true;
   }

   public boolean service_numberHasBeenSet(){
	  return service_numberHasBeenSet;
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

   public String toString()
   {
	  StringBuffer str = new StringBuffer("{");

	  str.append("counter=" + getCounter() + " " + "subfamily=" + getSubfamily() + " " + "service_number=" + getService_number() + " " + "type_number=" + getType_number());
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
	  ret = ret && counterHasBeenSet;
	  return ret;
   }

   public boolean equals(Object other)
   {
      if (this == other)
         return true;
	  if ( ! hasIdentity() ) return false;
	  if (other instanceof AdditionalDetailValue)
	  {
		 AdditionalDetailValue that = (AdditionalDetailValue) other;
		 if ( ! that.hasIdentity() ) return false;
		 boolean lEquals = true;
		 lEquals = lEquals && this.counter == that.counter;

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
	  if (other instanceof AdditionalDetailValue)
	  {
		 AdditionalDetailValue that = (AdditionalDetailValue) other;
		 boolean lEquals = true;
		 lEquals = lEquals && this.subfamily == that.subfamily;
		 lEquals = lEquals && this.service_number == that.service_number;
		 lEquals = lEquals && this.type_number == that.type_number;

		 return lEquals;
	  }
	  else
	  {
		 return false;
	  }
   }

   public int hashCode(){
	  int result = 17;
      result = 37*result + (int) counter;

      result = 37*result + (int) subfamily;

      result = 37*result + (int) service_number;

      result = 37*result + (int) type_number;

	  return result;
   }

}
