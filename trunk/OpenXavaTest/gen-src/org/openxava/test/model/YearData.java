/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Data object for Year.
 */
public class YearData
   extends java.lang.Object
   implements java.io.Serializable
{

   private static final long serialVersionUID = 1L;
   private java.lang.Integer _Year;

   public YearData()
   {
   }

   public YearData( YearData otherData )
   {
      set_Year(otherData.get_Year());

   }

   public org.openxava.test.model.YearKey getPrimaryKey() {
     org.openxava.test.model.YearKey pk = new org.openxava.test.model.YearKey();
     return pk;
   }

   public java.lang.Integer get_Year()
   {
      return this._Year;
   }
   public void set_Year( java.lang.Integer _Year )
   {
      this._Year = _Year;
   }

   public String toString()
   {
      StringBuffer str = new StringBuffer("{");

      str.append("_Year=" + get_Year());
      str.append('}');

      return(str.toString());
   }

   public boolean equals( Object pOther )
   {
      if( pOther instanceof YearData )
      {
         YearData lTest = (YearData) pOther;
         boolean lEquals = true;

         if( this._Year == null )
         {
            lEquals = lEquals && ( lTest._Year == null );
         }
         else
         {
            lEquals = lEquals && this._Year.equals( lTest._Year );
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

      result = 37*result + ((this._Year != null) ? this._Year.hashCode() : 0);

      return result;
   }

}