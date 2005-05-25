/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.ejb;

/**
 * Data object for DeliveryDetail.
 */
public class DeliveryDetailData
   extends java.lang.Object
   implements java.io.Serializable
{
   private java.lang.String _Description;
   private int number;
   private int _Delivery_number;
   private int _Delivery_type_number;
   private int _Delivery_invoice_year;
   private int _Delivery_invoice_number;

   public DeliveryDetailData()
   {
   }

   public DeliveryDetailData( DeliveryDetailData otherData )
   {
      set_Description(otherData.get_Description());
      setNumber(otherData.getNumber());
      set_Delivery_number(otherData.get_Delivery_number());
      set_Delivery_type_number(otherData.get_Delivery_type_number());
      set_Delivery_invoice_year(otherData.get_Delivery_invoice_year());
      set_Delivery_invoice_number(otherData.get_Delivery_invoice_number());

   }

   public org.openxava.test.ejb.DeliveryDetailKey getPrimaryKey() {
     org.openxava.test.ejb.DeliveryDetailKey pk = new org.openxava.test.ejb.DeliveryDetailKey(this.getNumber());
     return pk;
   }

   public java.lang.String get_Description()
   {
      return this._Description;
   }
   public void set_Description( java.lang.String _Description )
   {
      this._Description = _Description;
   }

   public int getNumber()
   {
      return this.number;
   }
   public void setNumber( int number )
   {
      this.number = number;
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

      str.append("_Description=" + get_Description() + " " + "number=" + getNumber() + " " + "_Delivery_number=" + get_Delivery_number() + " " + "_Delivery_type_number=" + get_Delivery_type_number() + " " + "_Delivery_invoice_year=" + get_Delivery_invoice_year() + " " + "_Delivery_invoice_number=" + get_Delivery_invoice_number());
      str.append('}');

      return(str.toString());
   }

   public boolean equals( Object pOther )
   {
      if( pOther instanceof DeliveryDetailData )
      {
         DeliveryDetailData lTest = (DeliveryDetailData) pOther;
         boolean lEquals = true;

         if( this._Description == null )
         {
            lEquals = lEquals && ( lTest._Description == null );
         }
         else
         {
            lEquals = lEquals && this._Description.equals( lTest._Description );
         }
         lEquals = lEquals && this.number == lTest.number;
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

      result = 37*result + ((this._Description != null) ? this._Description.hashCode() : 0);

      result = 37*result + (int) number;

      result = 37*result + (int) _Delivery_number;

      result = 37*result + (int) _Delivery_type_number;

      result = 37*result + (int) _Delivery_invoice_year;

      result = 37*result + (int) _Delivery_invoice_number;

      return result;
   }

}
