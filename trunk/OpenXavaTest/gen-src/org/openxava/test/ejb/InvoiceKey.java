/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.ejb;

/**
 * Primary key for Invoice.
 */
public class InvoiceKey
   extends java.lang.Object
   implements java.io.Serializable
{

   public int year;
   public int number;

   public InvoiceKey()
   {
   }

   public InvoiceKey( int year,int number )
   {
      this.year = year;
      this.number = number;
   }

   public int getYear()
   {
      return year;
   }
   public int getNumber()
   {
      return number;
   }

   public void setYear(int year)
   {
      this.year = year;
   }
   public void setNumber(int number)
   {
      this.number = number;
   }

   public int hashCode()
   {
      int _hashCode = 0;
         _hashCode += (int)this.year;
         _hashCode += (int)this.number;

      return _hashCode;
   }

   public boolean equals(Object obj)
   {
      if( !(obj instanceof org.openxava.test.ejb.InvoiceKey) )
         return false;

      org.openxava.test.ejb.InvoiceKey pk = (org.openxava.test.ejb.InvoiceKey)obj;
      boolean eq = true;

      if( obj == null )
      {
         eq = false;
      }
      else
      {
         eq = eq && this.year == pk.year;
         eq = eq && this.number == pk.number;
      }

      return eq;
   }

   /** @return String representation of this pk in the form of [.field1.field2.field3]. */
   public String toString()
   {
      StringBuffer toStringValue = new StringBuffer("[.");
         toStringValue.append(this.year).append('.');
         toStringValue.append(this.number).append('.');
      toStringValue.append(']');
      return toStringValue.toString();
   }

}
