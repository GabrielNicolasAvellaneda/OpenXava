/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model.xejb;

/**
 * CMP layer for DeliveryType.
 */
public abstract class DeliveryTypeCMP
   extends org.openxava.test.model.xejb.DeliveryTypeBean
   implements javax.ejb.EntityBean
{

   public org.openxava.test.model.DeliveryTypeData getData()
   {
      org.openxava.test.model.DeliveryTypeData dataHolder = null;
      try
      {
         dataHolder = new org.openxava.test.model.DeliveryTypeData();

         dataHolder.set_Description( get_Description() );
         dataHolder.setNumber( getNumber() );

      }
      catch (RuntimeException e)
      {
         throw new javax.ejb.EJBException(e);
      }

      return dataHolder;
   }

   public void setData( org.openxava.test.model.DeliveryTypeData dataHolder )
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

      DeliveryTypeValue = null;
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

   private org.openxava.test.model.DeliveryTypeValue DeliveryTypeValue = null;

   public org.openxava.test.model.DeliveryTypeValue getDeliveryTypeValue()
   {
      DeliveryTypeValue = new org.openxava.test.model.DeliveryTypeValue();
      try
         {
            DeliveryTypeValue.setDescription( getDescription() );
            DeliveryTypeValue.setNumber( getNumber() );

         }
         catch (Exception e)
         {
            throw new javax.ejb.EJBException(e);
         }

	  return DeliveryTypeValue;
   }

   public void setDeliveryTypeValue( org.openxava.test.model.DeliveryTypeValue valueHolder )
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