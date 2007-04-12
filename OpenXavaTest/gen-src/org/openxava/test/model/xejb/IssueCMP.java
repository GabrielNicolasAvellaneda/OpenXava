/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model.xejb;

/**
 * CMP layer for Issue.
 */
public abstract class IssueCMP
   extends org.openxava.test.model.xejb.IssueBean
   implements javax.ejb.EntityBean
{

   public org.openxava.test.model.IssueData getData()
   {
      org.openxava.test.model.IssueData dataHolder = null;
      try
      {
         dataHolder = new org.openxava.test.model.IssueData();

         dataHolder.set_Description( get_Description() );
         dataHolder.setId( getId() );

      }
      catch (RuntimeException e)
      {
         throw new javax.ejb.EJBException(e);
      }

      return dataHolder;
   }

   public void setData( org.openxava.test.model.IssueData dataHolder )
   {
      try
      {
         set_Description( dataHolder.get_Description() );

      }
      catch (Exception e)
      {
         throw new javax.ejb.EJBException(e);
      }
   }

   public void ejbLoad() 
   {
      super.ejbLoad();
   }

   public void ejbStore() 
   {
         super.ejbStore();
   }

   public void ejbActivate() 
   {
   }

   public void ejbPassivate() 
   {

      IssueValue = null;
   }

   public void setEntityContext(javax.ejb.EntityContext ctx) 
   {
      super.setEntityContext(ctx);
   }

   public void unsetEntityContext() 
   {
      super.unsetEntityContext();
   }

   public void ejbRemove() throws javax.ejb.RemoveException
   {
      super.ejbRemove();

   }

 /* Value Objects BEGIN */

   private org.openxava.test.model.IssueValue IssueValue = null;

   public org.openxava.test.model.IssueValue getIssueValue()
   {
      IssueValue = new org.openxava.test.model.IssueValue();
      try
         {
            IssueValue.setDescription( getDescription() );
            IssueValue.setId( getId() );

         }
         catch (Exception e)
         {
            throw new javax.ejb.EJBException(e);
         }

	  return IssueValue;
   }

   public void setIssueValue( org.openxava.test.model.IssueValue valueHolder )
   {

	  try
	  {
		 setDescription( valueHolder.getDescription() );

	  }
	  catch (Exception e)
	  {
		 throw new javax.ejb.EJBException(e);
	  }
   }

/* Value Objects END */

   public abstract java.lang.String get_Description() ;

   public abstract void set_Description( java.lang.String _Description ) ;

   public abstract java.lang.String getId() ;

   public abstract void setId( java.lang.String id ) ;

}
