/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.ejb.xejb;

/**
 * CMP layer for DeliveryDetail.
 */
public abstract class DeliveryDetailCMP
   extends org.openxava.test.ejb.xejb.DeliveryDetailBean
   implements javax.ejb.EntityBean
{

   public org.openxava.test.ejb.DeliveryDetailData getData()
   {
      org.openxava.test.ejb.DeliveryDetailData dataHolder = null;
      try
      {
         dataHolder = new org.openxava.test.ejb.DeliveryDetailData();

         dataHolder.setOid( getOid() );
         dataHolder.set_Description( get_Description() );
         dataHolder.setDelivery_number( getDelivery_number() );
         dataHolder.setDelivery_type_number( getDelivery_type_number() );
         dataHolder.setDelivery_invoice_year( getDelivery_invoice_year() );
         dataHolder.setDelivery_invoice_number( getDelivery_invoice_number() );

      }
      catch (RuntimeException e)
      {
         throw new javax.ejb.EJBException(e);
      }

      return dataHolder;
   }

   public void setData( org.openxava.test.ejb.DeliveryDetailData dataHolder )
   {
      try
      {
         set_Description( dataHolder.get_Description() );
         setDelivery_number( dataHolder.getDelivery_number() );
         setDelivery_type_number( dataHolder.getDelivery_type_number() );
         setDelivery_invoice_year( dataHolder.getDelivery_invoice_year() );
         setDelivery_invoice_number( dataHolder.getDelivery_invoice_number() );

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

      DeliveryDetailValue = null;
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

   }

 /* Value Objects BEGIN */

   private org.openxava.test.ejb.DeliveryDetailValue DeliveryDetailValue = null;

   public org.openxava.test.ejb.DeliveryDetailValue getDeliveryDetailValue()
   {
      DeliveryDetailValue = new org.openxava.test.ejb.DeliveryDetailValue();
      try
         {
            DeliveryDetailValue.setOid( getOid() );
            DeliveryDetailValue.setDescription( getDescription() );
            DeliveryDetailValue.setDelivery_number( getDelivery_number() );
            DeliveryDetailValue.setDelivery_type_number( getDelivery_type_number() );
            DeliveryDetailValue.setDelivery_invoice_year( getDelivery_invoice_year() );
            DeliveryDetailValue.setDelivery_invoice_number( getDelivery_invoice_number() );

         }
         catch (Exception e)
         {
            throw new javax.ejb.EJBException(e);
         }

	  return DeliveryDetailValue;
   }

   public void setDeliveryDetailValue( org.openxava.test.ejb.DeliveryDetailValue valueHolder )
   {

	  try
	  {
		 setDescription( valueHolder.getDescription() );
		 setDelivery_number( valueHolder.getDelivery_number() );
		 setDelivery_type_number( valueHolder.getDelivery_type_number() );
		 setDelivery_invoice_year( valueHolder.getDelivery_invoice_year() );
		 setDelivery_invoice_number( valueHolder.getDelivery_invoice_number() );

	  }
	  catch (Exception e)
	  {
		 throw new javax.ejb.EJBException(e);
	  }
   }

/* Value Objects END */

   public abstract long getOid() ;

   public abstract void setOid( long oid ) ;

   public abstract java.lang.String get_Description() ;

   public abstract void set_Description( java.lang.String _Description ) ;

   public abstract int getDelivery_number() ;

   public abstract void setDelivery_number( int delivery_number ) ;

   public abstract int getDelivery_type_number() ;

   public abstract void setDelivery_type_number( int delivery_type_number ) ;

   public abstract int getDelivery_invoice_year() ;

   public abstract void setDelivery_invoice_year( int delivery_invoice_year ) ;

   public abstract int getDelivery_invoice_number() ;

   public abstract void setDelivery_invoice_number( int delivery_invoice_number ) ;

}
