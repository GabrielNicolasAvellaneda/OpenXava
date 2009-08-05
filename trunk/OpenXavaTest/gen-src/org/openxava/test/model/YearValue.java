/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Value object for Year.
 *
 */
public class YearValue
   extends java.lang.Object
   implements java.io.Serializable 
{

   private static final long serialVersionUID = 1L;

   private int year;
   private boolean yearHasBeenSet = false;

   public YearValue()
   {
   }

   //TODO Cloneable is better than this !
   public YearValue( YearValue otherValue )
   {
	  this.year = otherValue.year;
	  yearHasBeenSet = true;
   }

   public int getYear()
   {
	  return this.year;
   }

   public void setYear( int year )
   {
	  this.year = year;
	  yearHasBeenSet = true;
   }

   public boolean yearHasBeenSet(){
	  return yearHasBeenSet;
   }

   public String toString()
   {
	  StringBuffer str = new StringBuffer("{");

	  str.append("year=" + getYear());
	  str.append('}');

	  return(str.toString());
   }

   /**
    * A Value Object has an identity if the attributes making its Primary Key have all been set. An object without identity is never equal to any other object.
    *
    * @return true if this instance has an identity.
    */
   protected boolean hasIdentity()
   {
	  boolean ret = true;
	  return ret;
   }

   public boolean equals(Object other)
   {
      if (this == other)
         return true;
	  if ( ! hasIdentity() ) return false;
	  if (other instanceof YearValue)
	  {
		 YearValue that = (YearValue) other;
		 if ( ! that.hasIdentity() ) return false;
		 boolean lEquals = true;

		 lEquals = lEquals && isIdentical(that);

		 return lEquals;
	  }
	  else
	  {
		 return false;
	  }
   }

   public boolean isIdentical(Object other)
   {
	  if (other instanceof YearValue)
	  {
		 YearValue that = (YearValue) other;
		 boolean lEquals = true;
		 lEquals = lEquals && this.year == that.year;

		 return lEquals;
	  }
	  else
	  {
		 return false;
	  }
   }

   public int hashCode(){
	  int result = 17;
      result = 37*result + (int) year;

	  return result;
   }

}