/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

import java.util.*;

import org.openxava.util.*;

/**
 * Primary key for Product3.
 */
public class Product3Key
   extends java.lang.Object
   implements java.io.Serializable
{

   private static final long serialVersionUID = 1L;

   public long number;

   public Product3Key()
   {
   }

   public Product3Key( long number )
   {
      this.number = number;
   }

   public long getNumber()
   {
      return number;
   }

   public void setNumber(long number)
   {
      this.number = number;
   }

   public int hashCode()
   {
      int _hashCode = 0;
         _hashCode += (int)this.number;

      return _hashCode;
   }

   public boolean equals(Object obj)
   {
      if( !(obj instanceof org.openxava.test.model.Product3Key) )
         return false;

      org.openxava.test.model.Product3Key pk = (org.openxava.test.model.Product3Key)obj;
      boolean eq = true;

      if( obj == null )
      {
         eq = false;
      }
      else
      {
         eq = eq && this.number == pk.number;
      }

      return eq;
   }

   /**
    * Create from a string with the format of toString() method
    */
   public static Product3Key createFromString(String string) throws IllegalArgumentException, IllegalAccessException {
      StringTokenizer st = new StringTokenizer(string, "[.]");
      Product3Key key = new Product3Key();
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