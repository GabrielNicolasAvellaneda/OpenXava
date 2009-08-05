/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Data object for State.
 */
public class StateData
   extends java.lang.Object
   implements java.io.Serializable
{

   private static final long serialVersionUID = 1L;
   private java.lang.String _Name;
   private java.lang.String id;

   public StateData()
   {
   }

   public StateData( StateData otherData )
   {
      set_Name(otherData.get_Name());
      setId(otherData.getId());

   }

   public org.openxava.test.model.StateKey getPrimaryKey() {
     org.openxava.test.model.StateKey pk = new org.openxava.test.model.StateKey(this.getId());
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

   public java.lang.String getId()
   {
      return this.id;
   }
   public void setId( java.lang.String id )
   {
      this.id = id;
   }

   public String toString()
   {
      StringBuffer str = new StringBuffer("{");

      str.append("_Name=" + get_Name() + " " + "id=" + getId());
      str.append('}');

      return(str.toString());
   }

   public boolean equals( Object pOther )
   {
      if( pOther instanceof StateData )
      {
         StateData lTest = (StateData) pOther;
         boolean lEquals = true;

         if( this._Name == null )
         {
            lEquals = lEquals && ( lTest._Name == null );
         }
         else
         {
            lEquals = lEquals && this._Name.equals( lTest._Name );
         }
         if( this.id == null )
         {
            lEquals = lEquals && ( lTest.id == null );
         }
         else
         {
            lEquals = lEquals && this.id.equals( lTest.id );
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

      result = 37*result + ((this.id != null) ? this.id.hashCode() : 0);

      return result;
   }

}