/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model.xejb;

/**
 * CMP layer for Family2.
 */
public abstract class Family2CMP
   extends org.openxava.test.model.xejb.Family2Bean
   implements javax.ejb.EntityBean
{

   public org.openxava.test.model.Family2Data getData()
   {
      org.openxava.test.model.Family2Data dataHolder = null;
      try
      {
         dataHolder = new org.openxava.test.model.Family2Data();

         dataHolder.set_Description( get_Description() );
         dataHolder.setNumber( getNumber() );

      }
      catch (RuntimeException e)
      {
         throw new javax.ejb.EJBException(e);
      }

      return dataHolder;
   }

   public void setData( org.openxava.test.model.Family2Data dataHolder )
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

      Family2Value = null;
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

   private org.openxava.test.model.Family2Value Family2Value = null;

   public org.openxava.test.model.Family2Value getFamily2Value()
   {
      Family2Value = new org.openxava.test.model.Family2Value();
      try
         {
            Family2Value.setDescription( getDescription() );
            Family2Value.setNumber( getNumber() );

         }
         catch (Exception e)
         {
            throw new javax.ejb.EJBException(e);
         }

	  return Family2Value;
   }

   public void setFamily2Value( org.openxava.test.model.Family2Value valueHolder )
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

   public abstract int getNumber() ;

   public abstract void setNumber( int number ) ;

}