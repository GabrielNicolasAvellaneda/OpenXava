/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Value object for State.
 *
 */
public class StateValue
   extends java.lang.Object
   implements java.io.Serializable 
{

   private static final long serialVersionUID = 1L;

   private java.lang.String fullName;
   private boolean fullNameHasBeenSet = false;
   private java.lang.String name;
   private boolean nameHasBeenSet = false;
   private java.lang.String id;
   private boolean idHasBeenSet = false;

   public StateValue()
   {
   }

   //TODO Cloneable is better than this !
   public StateValue( StateValue otherValue )
   {
	  this.fullName = otherValue.fullName;
	  fullNameHasBeenSet = true;
	  this.name = otherValue.name;
	  nameHasBeenSet = true;
	  this.id = otherValue.id;
	  idHasBeenSet = true;
   }

   public java.lang.String getFullName()
   {
	  return this.fullName;
   }

   public void setFullName( java.lang.String fullName )
   {
	  this.fullName = fullName;
	  fullNameHasBeenSet = true;
   }

   public boolean fullNameHasBeenSet(){
	  return fullNameHasBeenSet;
   }
   public java.lang.String getName()
   {
	  return this.name;
   }

   public void setName( java.lang.String name )
   {
	  this.name = name;
	  nameHasBeenSet = true;
   }

   public boolean nameHasBeenSet(){
	  return nameHasBeenSet;
   }
   public java.lang.String getId()
   {
	  return this.id;
   }

   public void setId( java.lang.String id )
   {
	  this.id = id;
	  idHasBeenSet = true;
   }

   public boolean idHasBeenSet(){
	  return idHasBeenSet;
   }

   public String toString()
   {
	  StringBuffer str = new StringBuffer("{");

	  str.append("fullName=" + getFullName() + " " + "name=" + getName() + " " + "id=" + getId());
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
	  ret = ret && idHasBeenSet;
	  return ret;
   }

   public boolean equals(Object other)
   {
      if (this == other)
         return true;
	  if ( ! hasIdentity() ) return false;
	  if (other instanceof StateValue)
	  {
		 StateValue that = (StateValue) other;
		 if ( ! that.hasIdentity() ) return false;
		 boolean lEquals = true;
		 if( this.id == null )
		 {
			lEquals = lEquals && ( that.id == null );
		 }
		 else
		 {
			lEquals = lEquals && this.id.equals( that.id );
		 }

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
	  if (other instanceof StateValue)
	  {
		 StateValue that = (StateValue) other;
		 boolean lEquals = true;
		 if( this.fullName == null )
		 {
			lEquals = lEquals && ( that.fullName == null );
		 }
		 else
		 {
			lEquals = lEquals && this.fullName.equals( that.fullName );
		 }
		 if( this.name == null )
		 {
			lEquals = lEquals && ( that.name == null );
		 }
		 else
		 {
			lEquals = lEquals && this.name.equals( that.name );
		 }

		 return lEquals;
	  }
	  else
	  {
		 return false;
	  }
   }

   public int hashCode(){
	  int result = 17;
      result = 37*result + ((this.fullName != null) ? this.fullName.hashCode() : 0);

      result = 37*result + ((this.name != null) ? this.name.hashCode() : 0);

      result = 37*result + ((this.id != null) ? this.id.hashCode() : 0);

	  return result;
   }

}