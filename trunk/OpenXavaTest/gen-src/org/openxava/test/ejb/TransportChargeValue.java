/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.ejb;

/**
 * Value object for TransportCharge.
 *
 */
public class TransportChargeValue
   extends java.lang.Object
   implements java.io.Serializable 
{
   private java.math.BigDecimal amount;
   private boolean amountHasBeenSet = false;
   private int delivery_number;
   private boolean delivery_numberHasBeenSet = false;
   private int delivery_type_number;
   private boolean delivery_type_numberHasBeenSet = false;
   private int delivery_invoice_year;
   private boolean delivery_invoice_yearHasBeenSet = false;
   private int delivery_invoice_number;
   private boolean delivery_invoice_numberHasBeenSet = false;

   // private org.openxava.test.ejb.TransportChargeKey pk;

   public TransportChargeValue()
   {
	 // pk = new org.openxava.test.ejb.TransportChargeKey();
   }

   /*
   public TransportChargeValue(org.openxava.test.ejb.TransportChargeKey pk)
   {
	  this.pk = pk;
   }
   */

   /*
   public TransportChargeValue( java.math.BigDecimal amount,int delivery_number,int delivery_type_number,int delivery_invoice_year,int delivery_invoice_number )
   {
	  this.amount = amount;
	  amountHasBeenSet = true;
	  this.delivery_number = delivery_number;
	  delivery_numberHasBeenSet = true;
	  this.delivery_type_number = delivery_type_number;
	  delivery_type_numberHasBeenSet = true;
	  this.delivery_invoice_year = delivery_invoice_year;
	  delivery_invoice_yearHasBeenSet = true;
	  this.delivery_invoice_number = delivery_invoice_number;
	  delivery_invoice_numberHasBeenSet = true;
	  //pk = new org.openxava.test.ejb.TransportChargeKey(this.getDelivery_number(),this.getDelivery_type_number(),this.getDelivery_invoice_year(),this.getDelivery_invoice_number());
   }
   */

   //TODO Cloneable is better than this !
   public TransportChargeValue( TransportChargeValue otherValue )
   {
	  this.amount = otherValue.amount;
	  amountHasBeenSet = true;
	  this.delivery_number = otherValue.delivery_number;
	  delivery_numberHasBeenSet = true;
	  this.delivery_type_number = otherValue.delivery_type_number;
	  delivery_type_numberHasBeenSet = true;
	  this.delivery_invoice_year = otherValue.delivery_invoice_year;
	  delivery_invoice_yearHasBeenSet = true;
	  this.delivery_invoice_number = otherValue.delivery_invoice_number;
	  delivery_invoice_numberHasBeenSet = true;

	  // pk = new org.openxava.test.ejb.TransportChargeKey(this.getDelivery_number(),this.getDelivery_type_number(),this.getDelivery_invoice_year(),this.getDelivery_invoice_number());
   }

   /*
   public org.openxava.test.ejb.TransportChargeKey getPrimaryKey()
   {
	  return pk;
   }

   public void setPrimaryKey( org.openxava.test.ejb.TransportChargeKey pk )
   {
      // it's also nice to update PK object - just in case
      // somebody would ask for it later...
      this.pk = pk;
	  setDelivery_number( pk.delivery_number );
	  setDelivery_type_number( pk.delivery_type_number );
	  setDelivery_invoice_year( pk.delivery_invoice_year );
	  setDelivery_invoice_number( pk.delivery_invoice_number );
   }
   */

   public java.math.BigDecimal getAmount()
   {
	  return this.amount;
   }

   public void setAmount( java.math.BigDecimal amount )
   {
	  this.amount = amount;
	  amountHasBeenSet = true;

   }

   public boolean amountHasBeenSet(){
	  return amountHasBeenSet;
   }
   public int getDelivery_number()
   {
	  return this.delivery_number;
   }

   public void setDelivery_number( int delivery_number )
   {
	  this.delivery_number = delivery_number;
	  delivery_numberHasBeenSet = true;

		 //pk.setDelivery_number(delivery_number);
   }

   public boolean delivery_numberHasBeenSet(){
	  return delivery_numberHasBeenSet;
   }
   public int getDelivery_type_number()
   {
	  return this.delivery_type_number;
   }

   public void setDelivery_type_number( int delivery_type_number )
   {
	  this.delivery_type_number = delivery_type_number;
	  delivery_type_numberHasBeenSet = true;

		 //pk.setDelivery_type_number(delivery_type_number);
   }

   public boolean delivery_type_numberHasBeenSet(){
	  return delivery_type_numberHasBeenSet;
   }
   public int getDelivery_invoice_year()
   {
	  return this.delivery_invoice_year;
   }

   public void setDelivery_invoice_year( int delivery_invoice_year )
   {
	  this.delivery_invoice_year = delivery_invoice_year;
	  delivery_invoice_yearHasBeenSet = true;

		 //pk.setDelivery_invoice_year(delivery_invoice_year);
   }

   public boolean delivery_invoice_yearHasBeenSet(){
	  return delivery_invoice_yearHasBeenSet;
   }
   public int getDelivery_invoice_number()
   {
	  return this.delivery_invoice_number;
   }

   public void setDelivery_invoice_number( int delivery_invoice_number )
   {
	  this.delivery_invoice_number = delivery_invoice_number;
	  delivery_invoice_numberHasBeenSet = true;

		 //pk.setDelivery_invoice_number(delivery_invoice_number);
   }

   public boolean delivery_invoice_numberHasBeenSet(){
	  return delivery_invoice_numberHasBeenSet;
   }

   public String toString()
   {
	  StringBuffer str = new StringBuffer("{");

	  str.append("amount=" + getAmount() + " " + "delivery_number=" + getDelivery_number() + " " + "delivery_type_number=" + getDelivery_type_number() + " " + "delivery_invoice_year=" + getDelivery_invoice_year() + " " + "delivery_invoice_number=" + getDelivery_invoice_number());
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
	  ret = ret && delivery_numberHasBeenSet;
	  ret = ret && delivery_type_numberHasBeenSet;
	  ret = ret && delivery_invoice_yearHasBeenSet;
	  ret = ret && delivery_invoice_numberHasBeenSet;
	  return ret;
   }

   public boolean equals(Object other)
   {
      if (this == other)
         return true;
	  if ( ! hasIdentity() ) return false;
	  if (other instanceof TransportChargeValue)
	  {
		 TransportChargeValue that = (TransportChargeValue) other;
		 if ( ! that.hasIdentity() ) return false;
		 boolean lEquals = true;
		 lEquals = lEquals && this.delivery_number == that.delivery_number;
		 lEquals = lEquals && this.delivery_type_number == that.delivery_type_number;
		 lEquals = lEquals && this.delivery_invoice_year == that.delivery_invoice_year;
		 lEquals = lEquals && this.delivery_invoice_number == that.delivery_invoice_number;

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
	  if (other instanceof TransportChargeValue)
	  {
		 TransportChargeValue that = (TransportChargeValue) other;
		 boolean lEquals = true;
		 if( this.amount == null )
		 {
			lEquals = lEquals && ( that.amount == null );
		 }
		 else
		 {
			lEquals = lEquals && this.amount.equals( that.amount );
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
      result = 37*result + ((this.amount != null) ? this.amount.hashCode() : 0);

      result = 37*result + (int) delivery_number;

      result = 37*result + (int) delivery_type_number;

      result = 37*result + (int) delivery_invoice_year;

      result = 37*result + (int) delivery_invoice_number;

	  return result;
   }

}
