/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Data object for TransportCharge2.
 */
public class TransportCharge2Data
   extends java.lang.Object
   implements java.io.Serializable
{

   private static final long serialVersionUID = 1L;
   private java.math.BigDecimal _Amount;
   private int year;
   private int _Delivery_number;
   private int _Delivery_type_number;
   private int _Delivery_invoice_number;

   public TransportCharge2Data()
   {
   }

   public TransportCharge2Data( TransportCharge2Data otherData )
   {
      set_Amount(otherData.get_Amount());
      setYear(otherData.getYear());
      set_Delivery_number(otherData.get_Delivery_number());
      set_Delivery_type_number(otherData.get_Delivery_type_number());
      set_Delivery_invoice_number(otherData.get_Delivery_invoice_number());

   }

   public org.openxava.test.model.TransportCharge2Key getPrimaryKey() {
     org.openxava.test.model.TransportCharge2Key pk = new org.openxava.test.model.TransportCharge2Key(this.getYear(),this.get_Delivery_number(),this.get_Delivery_type_number(),this.get_Delivery_invoice_number());
     return pk;
   }

   public java.math.BigDecimal get_Amount()
   {
      return this._Amount;
   }
   public void set_Amount( java.math.BigDecimal _Amount )
   {
      this._Amount = _Amount;
   }

   public int getYear()
   {
      return this.year;
   }
   public void setYear( int year )
   {
      this.year = year;
   }

   public int get_Delivery_number()
   {
      return this._Delivery_number;
   }
   public void set_Delivery_number( int _Delivery_number )
   {
      this._Delivery_number = _Delivery_number;
   }

   public int get_Delivery_type_number()
   {
      return this._Delivery_type_number;
   }
   public void set_Delivery_type_number( int _Delivery_type_number )
   {
      this._Delivery_type_number = _Delivery_type_number;
   }

   public int get_Delivery_invoice_number()
   {
      return this._Delivery_invoice_number;
   }
   public void set_Delivery_invoice_number( int _Delivery_invoice_number )
   {
      this._Delivery_invoice_number = _Delivery_invoice_number;
   }

   public String toString()
   {
      StringBuffer str = new StringBuffer("{");

      str.append("_Amount=" + get_Amount() + " " + "year=" + getYear() + " " + "_Delivery_number=" + get_Delivery_number() + " " + "_Delivery_type_number=" + get_Delivery_type_number() + " " + "_Delivery_invoice_number=" + get_Delivery_invoice_number());
      str.append('}');

      return(str.toString());
   }

   public boolean equals( Object pOther )
   {
      if( pOther instanceof TransportCharge2Data )
      {
         TransportCharge2Data lTest = (TransportCharge2Data) pOther;
         boolean lEquals = true;

         if( this._Amount == null )
         {
            lEquals = lEquals && ( lTest._Amount == null );
         }
         else
         {
            lEquals = lEquals && this._Amount.equals( lTest._Amount );
         }
         lEquals = lEquals && this.year == lTest.year;
         lEquals = lEquals && this._Delivery_number == lTest._Delivery_number;
         lEquals = lEquals && this._Delivery_type_number == lTest._Delivery_type_number;
         lEquals = lEquals && this._Delivery_invoice_number == lTest._Delivery_invoice_number;

         return lEquals;
      }
      else
      {
         return false;
      }
   }

   public int hashCode()
   {
      int result = 17;

      result = 37*result + ((this._Amount != null) ? this._Amount.hashCode() : 0);

      result = 37*result + (int) year;

      result = 37*result + (int) _Delivery_number;

      result = 37*result + (int) _Delivery_type_number;

      result = 37*result + (int) _Delivery_invoice_number;

      return result;
   }

}