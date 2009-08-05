/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Data object for AdditionalDetail.
 */
public class AdditionalDetailData
   extends java.lang.Object
   implements java.io.Serializable
{

   private static final long serialVersionUID = 1L;
   private int counter;
   private java.lang.Integer _Subfamily;
   private int _Service_number;
   private int _Type_number;

   public AdditionalDetailData()
   {
   }

   public AdditionalDetailData( AdditionalDetailData otherData )
   {
      setCounter(otherData.getCounter());
      set_Subfamily(otherData.get_Subfamily());
      set_Service_number(otherData.get_Service_number());
      set_Type_number(otherData.get_Type_number());

   }

   public org.openxava.test.model.AdditionalDetailKey getPrimaryKey() {
     org.openxava.test.model.AdditionalDetailKey pk = new org.openxava.test.model.AdditionalDetailKey(this.getCounter(),this.get_Service_number());
     return pk;
   }

   public int getCounter()
   {
      return this.counter;
   }
   public void setCounter( int counter )
   {
      this.counter = counter;
   }

   public java.lang.Integer get_Subfamily()
   {
      return this._Subfamily;
   }
   public void set_Subfamily( java.lang.Integer _Subfamily )
   {
      this._Subfamily = _Subfamily;
   }

   public int get_Service_number()
   {
      return this._Service_number;
   }
   public void set_Service_number( int _Service_number )
   {
      this._Service_number = _Service_number;
   }

   public int get_Type_number()
   {
      return this._Type_number;
   }
   public void set_Type_number( int _Type_number )
   {
      this._Type_number = _Type_number;
   }

   public String toString()
   {
      StringBuffer str = new StringBuffer("{");

      str.append("counter=" + getCounter() + " " + "_Subfamily=" + get_Subfamily() + " " + "_Service_number=" + get_Service_number() + " " + "_Type_number=" + get_Type_number());
      str.append('}');

      return(str.toString());
   }

   public boolean equals( Object pOther )
   {
      if( pOther instanceof AdditionalDetailData )
      {
         AdditionalDetailData lTest = (AdditionalDetailData) pOther;
         boolean lEquals = true;

         lEquals = lEquals && this.counter == lTest.counter;
         if( this._Subfamily == null )
         {
            lEquals = lEquals && ( lTest._Subfamily == null );
         }
         else
         {
            lEquals = lEquals && this._Subfamily.equals( lTest._Subfamily );
         }
         lEquals = lEquals && this._Service_number == lTest._Service_number;
         lEquals = lEquals && this._Type_number == lTest._Type_number;

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

      result = 37*result + (int) counter;

      result = 37*result + ((this._Subfamily != null) ? this._Subfamily.hashCode() : 0);

      result = 37*result + (int) _Service_number;

      result = 37*result + (int) _Type_number;

      return result;
   }

}