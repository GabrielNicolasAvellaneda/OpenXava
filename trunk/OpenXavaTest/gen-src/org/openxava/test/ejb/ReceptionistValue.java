/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.ejb;

/**
 * Value object for Receptionist.
 *
 */
public class ReceptionistValue
   extends java.lang.Object
   implements java.io.Serializable 
{
   private int oid;
   private boolean oidHasBeenSet = false;
   private java.lang.String name;
   private boolean nameHasBeenSet = false;
   private java.lang.String deliveryPlace_oid;
   private boolean deliveryPlace_oidHasBeenSet = false;

   public ReceptionistValue()
   {
   }

   //TODO Cloneable is better than this !
   public ReceptionistValue( ReceptionistValue otherValue )
   {
	  this.oid = otherValue.oid;
	  oidHasBeenSet = true;
	  this.name = otherValue.name;
	  nameHasBeenSet = true;
	  this.deliveryPlace_oid = otherValue.deliveryPlace_oid;
	  deliveryPlace_oidHasBeenSet = true;
   }

   public int getOid()
   {
	  return this.oid;
   }

   public void setOid( int oid )
   {
	  this.oid = oid;
	  oidHasBeenSet = true;
   }

   public boolean oidHasBeenSet(){
	  return oidHasBeenSet;
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
   public java.lang.String getDeliveryPlace_oid()
   {
	  return this.deliveryPlace_oid;
   }

   public void setDeliveryPlace_oid( java.lang.String deliveryPlace_oid )
   {
	  this.deliveryPlace_oid = deliveryPlace_oid;
	  deliveryPlace_oidHasBeenSet = true;
   }

   public boolean deliveryPlace_oidHasBeenSet(){
	  return deliveryPlace_oidHasBeenSet;
   }

   public String toString()
   {
	  StringBuffer str = new StringBuffer("{");

	  str.append("oid=" + getOid() + " " + "name=" + getName() + " " + "deliveryPlace_oid=" + getDeliveryPlace_oid());
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
	  ret = ret && oidHasBeenSet;
	  return ret;
   }

   public boolean equals(Object other)
   {
      if (this == other)
         return true;
	  if ( ! hasIdentity() ) return false;
	  if (other instanceof ReceptionistValue)
	  {
		 ReceptionistValue that = (ReceptionistValue) other;
		 if ( ! that.hasIdentity() ) return false;
		 boolean lEquals = true;
		 lEquals = lEquals && this.oid == that.oid;

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
	  if (other instanceof ReceptionistValue)
	  {
		 ReceptionistValue that = (ReceptionistValue) other;
		 boolean lEquals = true;
		 if( this.name == null )
		 {
			lEquals = lEquals && ( that.name == null );
		 }
		 else
		 {
			lEquals = lEquals && this.name.equals( that.name );
		 }
		 if( this.deliveryPlace_oid == null )
		 {
			lEquals = lEquals && ( that.deliveryPlace_oid == null );
		 }
		 else
		 {
			lEquals = lEquals && this.deliveryPlace_oid.equals( that.deliveryPlace_oid );
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
      result = 37*result + (int) oid;

      result = 37*result + ((this.name != null) ? this.name.hashCode() : 0);

      result = 37*result + ((this.deliveryPlace_oid != null) ? this.deliveryPlace_oid.hashCode() : 0);

	  return result;
   }

}
