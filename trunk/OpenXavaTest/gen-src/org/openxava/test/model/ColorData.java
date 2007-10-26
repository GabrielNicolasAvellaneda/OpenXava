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
   private java.lang.Integer number;
   private java.lang.Integer _Version;

   public ColorData()
   {
   }

   public ColorData( ColorData otherData )
   {
      set_Name(otherData.get_Name());
      setNumber(otherData.getNumber());
      set_Version(otherData.get_Version());

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

   public java.lang.Integer getNumber()
   {
      return this.number;
   }
   public void setNumber( java.lang.Integer number )
   {
      this.number = number;
   }

   public java.lang.Integer get_Version()
   {
      return this._Version;
   }
   public void set_Version( java.lang.Integer _Version )
   {
      this._Version = _Version;
   }

   public String toString()
   {
      StringBuffer str = new StringBuffer("{");

      str.append("_Name=" + get_Name() + " " + "number=" + getNumber() + " " + "_Version=" + get_Version());
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
         if( this.number == null )
         {
            lEquals = lEquals && ( lTest.number == null );
         }
         else
         {
            lEquals = lEquals && this.number.equals( lTest.number );
         }
         if( this._Version == null )
         {
            lEquals = lEquals && ( lTest._Version == null );
         }
         else
         {
            lEquals = lEquals && this._Version.equals( lTest._Version );
         }

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

      result = 37*result + ((this.number != null) ? this.number.hashCode() : 0);

      result = 37*result + ((this._Version != null) ? this._Version.hashCode() : 0);

      return result;
   }

}
