/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

import java.util.*;

import org.openxava.util.*;

/**
 * Primary key for Office2.
 */
public class Office2Key
   extends java.lang.Object
   implements java.io.Serializable
{

   private static final long serialVersionUID = 1L;

   public int zoneNumber;
   public int number;
   public java.lang.Integer _MainWarehouse_number;

   public Office2Key()
   {
   }

   public Office2Key( int zoneNumber,int number,java.lang.Integer _MainWarehouse_number )
   {
      this.zoneNumber = zoneNumber;
      this.number = number;
      this._MainWarehouse_number = _MainWarehouse_number;
   }

   public int getZoneNumber()
   {
      return zoneNumber;
   }
   public int getNumber()
   {
      return number;
   }
   public java.lang.Integer get_MainWarehouse_number()
   {
      return _MainWarehouse_number;
   }

   public void setZoneNumber(int zoneNumber)
   {
      this.zoneNumber = zoneNumber;
   }
   public void setNumber(int number)
   {
      this.number = number;
   }
   public void set_MainWarehouse_number(java.lang.Integer _MainWarehouse_number)
   {
      this._MainWarehouse_number = _MainWarehouse_number;
   }

   public int hashCode()
   {
      int _hashCode = 0;
         _hashCode += (int)this.zoneNumber;
         _hashCode += (int)this.number;
         if (this._MainWarehouse_number != null) _hashCode += this._MainWarehouse_number.hashCode();

      return _hashCode;
   }

   public boolean equals(Object obj)
   {
      if( !(obj instanceof org.openxava.test.model.Office2Key) )
         return false;

      org.openxava.test.model.Office2Key pk = (org.openxava.test.model.Office2Key)obj;
      boolean eq = true;

      if( obj == null )
      {
         eq = false;
      }
      else
      {
         eq = eq && this.zoneNumber == pk.zoneNumber;
         eq = eq && this.number == pk.number;
         if( this._MainWarehouse_number != null )
         {
            eq = eq && this._MainWarehouse_number.equals( pk.get_MainWarehouse_number() );
         }
         else  // this._MainWarehouse_number == null
         {
            eq = eq && ( pk.get_MainWarehouse_number() == null );
         }
      }

      return eq;
   }

   /**
    * Create from a string with the format of toString() method
    */
   public static Office2Key createFromString(String string) throws IllegalArgumentException, IllegalAccessException {
      StringTokenizer st = new StringTokenizer(string, "[.]");
      Office2Key key = new Office2Key();
      java.lang.reflect.Field [] fields = key.getClass().getFields();
      Arrays.sort(fields, FieldComparator.getInstance());
      for (int i = 0; i < fields.length; i++) {
         String v = st.nextToken();
         Class type = fields[i].getType();
         Object value = null;
         if (!type.equals(String.class)) {
            value = Strings.toObject(type, v);
         }
         else {
            value = string;
         }
         fields[i].set(key, value);
      }
      return key;
   }

   /** @return String representation of this pk in the form of [.field1.field2.field3]. */
   public String toString()
   {
      StringBuffer toStringValue = new StringBuffer("[.");
      java.lang.reflect.Field [] fields = getClass().getFields();
      Arrays.sort(fields, FieldComparator.getInstance());
      for (int i=0; i < fields.length; i++) {
      	try {
      	 	toStringValue.append(fields[i].get(this)).append('.');
      	}
      	catch (IllegalAccessException ex) {
      	 	ex.printStackTrace();
      	 	toStringValue.append(" ").append('.');
      	}
      }
      toStringValue.append(']');
      return toStringValue.toString();
   }

}