/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model;

/**
 * Value object for Comment.
 *
 */
public class CommentValue
   extends java.lang.Object
   implements java.io.Serializable 
{

   private static final long serialVersionUID = 1L;

   private java.lang.Integer id;
   private boolean idHasBeenSet = false;
   private java.lang.String comment;
   private boolean commentHasBeenSet = false;
   private java.util.Date date;
   private boolean dateHasBeenSet = false;
   private java.lang.String issue_id;
   private boolean issue_idHasBeenSet = false;

   public CommentValue()
   {
   }

   //TODO Cloneable is better than this !
   public CommentValue( CommentValue otherValue )
   {
	  this.id = otherValue.id;
	  idHasBeenSet = true;
	  this.comment = otherValue.comment;
	  commentHasBeenSet = true;
	  this.date = otherValue.date;
	  dateHasBeenSet = true;
	  this.issue_id = otherValue.issue_id;
	  issue_idHasBeenSet = true;
   }

   public java.lang.Integer getId()
   {
	  return this.id;
   }

   public void setId( java.lang.Integer id )
   {
	  this.id = id;
	  idHasBeenSet = true;
   }

   public boolean idHasBeenSet(){
	  return idHasBeenSet;
   }
   public java.lang.String getComment()
   {
	  return this.comment;
   }

   public void setComment( java.lang.String comment )
   {
	  this.comment = comment;
	  commentHasBeenSet = true;
   }

   public boolean commentHasBeenSet(){
	  return commentHasBeenSet;
   }
   public java.util.Date getDate()
   {
	  return this.date;
   }

   public void setDate( java.util.Date date )
   {
	  this.date = date;
	  dateHasBeenSet = true;
   }

   public boolean dateHasBeenSet(){
	  return dateHasBeenSet;
   }
   public java.lang.String getIssue_id()
   {
	  return this.issue_id;
   }

   public void setIssue_id( java.lang.String issue_id )
   {
	  this.issue_id = issue_id;
	  issue_idHasBeenSet = true;
   }

   public boolean issue_idHasBeenSet(){
	  return issue_idHasBeenSet;
   }

   public String toString()
   {
	  StringBuffer str = new StringBuffer("{");

	  str.append("id=" + getId() + " " + "comment=" + getComment() + " " + "date=" + getDate() + " " + "issue_id=" + getIssue_id());
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
	  if (other instanceof CommentValue)
	  {
		 CommentValue that = (CommentValue) other;
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
	  if (other instanceof CommentValue)
	  {
		 CommentValue that = (CommentValue) other;
		 boolean lEquals = true;
		 if( this.comment == null )
		 {
			lEquals = lEquals && ( that.comment == null );
		 }
		 else
		 {
			lEquals = lEquals && this.comment.equals( that.comment );
		 }
		 if( this.date == null )
		 {
			lEquals = lEquals && ( that.date == null );
		 }
		 else
		 {
			lEquals = lEquals && this.date.equals( that.date );
		 }
		 if( this.issue_id == null )
		 {
			lEquals = lEquals && ( that.issue_id == null );
		 }
		 else
		 {
			lEquals = lEquals && this.issue_id.equals( that.issue_id );
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
      result = 37*result + ((this.id != null) ? this.id.hashCode() : 0);

      result = 37*result + ((this.comment != null) ? this.comment.hashCode() : 0);

      result = 37*result + ((this.date != null) ? this.date.hashCode() : 0);

      result = 37*result + ((this.issue_id != null) ? this.issue_id.hashCode() : 0);

	  return result;
   }

}
