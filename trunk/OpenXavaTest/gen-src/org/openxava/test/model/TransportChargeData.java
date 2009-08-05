/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Data object for TransportCharge.
 */
public class TransportChargeData
   extends java.lang.Object
   implements java.io.Serializable
{

   private static final long serialVersionUID = 1L;
   private java.math.BigDecimal _Amount;
   private int _Delivery_number;
   private int _Delivery_type_number;
   private int _Delivery_invoice_year;
   private int _Delivery_invoice_number;

   public TransportChargeData()
   {
   }

   public TransportChargeData( TransportChargeData otherData )
   {
      set_Amount(otherData.get_Amount());
      set_Delivery_number(otherData.get_Delivery_number());
      set_Delivery_type_number(otherData.get_Delivery_type_number());
      set_Delivery_invoice_year(otherData.get_Delivery_invoice_year());
      set_Delivery_invoice_number(otherData.get_Delivery_invoice_number());

   }

   public org.openxava.test.model.TransportChargeKey getPrimaryKey() {
     org.openxava.test.model.TransportChargeKey pk = new org.openxava.test.model.TransportChargeKey(this.get_Delivery_number(),this.get_Delivery_type_number(),this.get_Delivery_invoice_year(),this.get_Delivery_invoice_number());
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

   public int get_Delivery_invoice_year()
   {
      return this._Delivery_invoice_year;
   }
   public void set_Delivery_invoice_year( int _Delivery_invoice_year )
   {
      this._Delivery_invoice_year = _Delivery_invoice_year;
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

      str.append("_Amount=" + get_Amount() + " " + "_Delivery_number=" + get_Delivery_number() + " " + "_Delivery_type_number=" + get_Delivery_type_number() + " " + "_Delivery_invoice_year=" + get_Delivery_invoice_year() + " " + "_Delivery_invoice_number=" + get_Delivery_invoice_number());
      str.append('}');

      return(str.toString());
   }

   public boolean equals( Object pOther )
   {
      if( pOther instanceof TransportChargeData )
      {
         TransportChargeData lTest = (TransportChargeData) pOther;
         boolean lEquals = true;

         if( this._Amount == null )
         {
            lEquals = lEquals && ( lTest._Amount == null );
         }
         else
         {
            lEquals = lEquals && this._Amount.equals( lTest._Amount );
         }
         lEquals = lEquals && this._Delivery_number == lTest._Delivery_number;
         lEquals = lEquals && this._Delivery_type_number == lTest._Delivery_type_number;
         lEquals = lEquals && this._Delivery_invoice_year == lTest._Delivery_invoice_year;
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

      result = 37*result + (int) _Delivery_number;

      result = 37*result + (int) _Delivery_type_number;

      result = 37*result + (int) _Delivery_invoice_year;

      result = 37*result + (int) _Delivery_invoice_number;

      return result;
   }

}