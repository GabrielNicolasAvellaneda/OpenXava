/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Data object for Color.
 */
public class ColorData
   extends java.lang.Object
   implements java.io.Serializable
{

   private static final long serialVersionUID = 1L;
   private java.lang.String _Name;
   private int number;

   public ColorData()
   {
   }

   public ColorData( ColorData otherData )
   {
      set_Name(otherData.get_Name());
      setNumber(otherData.getNumber());

   }

   public org.openxava.test.model.ColorKey getPrimaryKey() {
     org.openxava.test.model.ColorKey pk = new org.openxava.test.model.ColorKey(this.getNumber());
     return pk;
   }

   public java.lang.String get_Name()
   {
      return this._Name;
   }
   public void set_Name( java.lang.String _Name )
   {
      this._Name = _Name;
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

      str.append("_Name=" + get_Name() + " " + "number=" + getNumber());
      str.append('}');

      return(str.toString());
   }

   public boolean equals( Object pOther )
   {
      if( pOther instanceof ColorData )
      {
         ColorData lTest = (ColorData) pOther;
         boolean lEquals = true;

         if( this._Name == null )
         {
            lEquals = lEquals && ( lTest._Name == null );
         }
         else
         {
            lEquals = lEquals && this._Name.equals( lTest._Name );
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

      result = 37*result + ((this._Name != null) ? this._Name.hashCode() : 0);

      result = 37*result + (int) number;

      return result;
   }

}
