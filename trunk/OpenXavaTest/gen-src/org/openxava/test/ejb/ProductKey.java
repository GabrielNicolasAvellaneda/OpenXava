/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.ejb;

/**
 * Primary key for Product.
 */
public class ProductKey
   extends java.lang.Object
   implements java.io.Serializable
{

   public long number;

   public ProductKey()
   {
   }

   public ProductKey( long number )
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
      if( !(obj instanceof org.openxava.test.ejb.ProductKey) )
         return false;

      org.openxava.test.ejb.ProductKey pk = (org.openxava.test.ejb.ProductKey)obj;
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

   /** @return String representation of this pk in the form of [.field1.field2.field3]. */
   public String toString()
   {
      StringBuffer toStringValue = new StringBuffer("[.");
      java.lang.reflect.Field [] fields = getClass().getFields();
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
