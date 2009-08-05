/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Data object for CustomerState.
 */
public class CustomerStateData
   extends java.lang.Object
   implements java.io.Serializable
{

   private static final long serialVersionUID = 1L;
   private java.lang.String _State_id;
   private int _Customer_number;

   public CustomerStateData()
   {
   }

   public CustomerStateData( CustomerStateData otherData )
   {
      set_State_id(otherData.get_State_id());
      set_Customer_number(otherData.get_Customer_number());

   }

   public org.openxava.test.model.CustomerStateKey getPrimaryKey() {
     org.openxava.test.model.CustomerStateKey pk = new org.openxava.test.model.CustomerStateKey(this.get_State_id(),this.get_Customer_number());
     return pk;
   }

   public java.lang.String get_State_id()
   {
      return this._State_id;
   }
   public void set_State_id( java.lang.String _State_id )
   {
      this._State_id = _State_id;
   }

   public int get_Customer_number()
   {
      return this._Customer_number;
   }
   public void set_Customer_number( int _Customer_number )
   {
      this._Customer_number = _Customer_number;
   }

   public String toString()
   {
      StringBuffer str = new StringBuffer("{");

      str.append("_State_id=" + get_State_id() + " " + "_Customer_number=" + get_Customer_number());
      str.append('}');

      return(str.toString());
   }

   public boolean equals( Object pOther )
   {
      if( pOther instanceof CustomerStateData )
      {
         CustomerStateData lTest = (CustomerStateData) pOther;
         boolean lEquals = true;

         if( this._State_id == null )
         {
            lEquals = lEquals && ( lTest._State_id == null );
         }
         else
         {
            lEquals = lEquals && this._State_id.equals( lTest._State_id );
         }
         lEquals = lEquals && this._Customer_number == lTest._Customer_number;

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

      result = 37*result + ((this._State_id != null) ? this._State_id.hashCode() : 0);

      result = 37*result + (int) _Customer_number;

      return result;
   }

}