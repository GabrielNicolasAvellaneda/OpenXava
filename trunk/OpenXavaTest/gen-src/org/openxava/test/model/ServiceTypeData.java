/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Data object for ServiceType.
 */
public class ServiceTypeData
   extends java.lang.Object
   implements java.io.Serializable
{

   private static final long serialVersionUID = 1L;
   private java.lang.Integer _Family;
   private java.lang.String _Description;
   private java.lang.Integer _Subfamily;
   private int number;

   public ServiceTypeData()
   {
   }

   public ServiceTypeData( ServiceTypeData otherData )
   {
      set_Family(otherData.get_Family());
      set_Description(otherData.get_Description());
      set_Subfamily(otherData.get_Subfamily());
      setNumber(otherData.getNumber());

   }

   public org.openxava.test.model.ServiceTypeKey getPrimaryKey() {
     org.openxava.test.model.ServiceTypeKey pk = new org.openxava.test.model.ServiceTypeKey(this.getNumber());
     return pk;
   }

   public java.lang.Integer get_Family()
   {
      return this._Family;
   }
   public void set_Family( java.lang.Integer _Family )
   {
      this._Family = _Family;
   }

   public java.lang.String get_Description()
   {
      return this._Description;
   }
   public void set_Description( java.lang.String _Description )
   {
      this._Description = _Description;
   }

   public java.lang.Integer get_Subfamily()
   {
      return this._Subfamily;
   }
   public void set_Subfamily( java.lang.Integer _Subfamily )
   {
      this._Subfamily = _Subfamily;
   }

   public int getNumber()
   {
      return this.number;
   }
   public void setNumber( int number )
   {
      this.number = number;
   }

   public String toString()
   {
      StringBuffer str = new StringBuffer("{");

      str.append("_Family=" + get_Family() + " " + "_Description=" + get_Description() + " " + "_Subfamily=" + get_Subfamily() + " " + "number=" + getNumber());
      str.append('}');

      return(str.toString());
   }

   public boolean equals( Object pOther )
   {
      if( pOther instanceof ServiceTypeData )
      {
         ServiceTypeData lTest = (ServiceTypeData) pOther;
         boolean lEquals = true;

         if( this._Family == null )
         {
            lEquals = lEquals && ( lTest._Family == null );
         }
         else
         {
            lEquals = lEquals && this._Family.equals( lTest._Family );
         }
         if( this._Description == null )
         {
            lEquals = lEquals && ( lTest._Description == null );
         }
         else
         {
            lEquals = lEquals && this._Description.equals( lTest._Description );
         }
         if( this._Subfamily == null )
         {
            lEquals = lEquals && ( lTest._Subfamily == null );
         }
         else
         {
            lEquals = lEquals && this._Subfamily.equals( lTest._Subfamily );
         }
         lEquals = lEquals && this.number == lTest.number;

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

      result = 37*result + ((this._Family != null) ? this._Family.hashCode() : 0);

      result = 37*result + ((this._Description != null) ? this._Description.hashCode() : 0);

      result = 37*result + ((this._Subfamily != null) ? this._Subfamily.hashCode() : 0);

      result = 37*result + (int) number;

      return result;
   }

}