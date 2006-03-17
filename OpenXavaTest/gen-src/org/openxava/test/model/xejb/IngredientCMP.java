/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model.xejb;

/**
 * CMP layer for Ingredient.
 */
public abstract class IngredientCMP
   extends org.openxava.test.model.xejb.IngredientBean
   implements javax.ejb.EntityBean
{

   public org.openxava.test.model.IngredientData getData()
   {
      org.openxava.test.model.IngredientData dataHolder = null;
      try
      {
         dataHolder = new org.openxava.test.model.IngredientData();

         dataHolder.setOid( getOid() );
         dataHolder.set_Name( get_Name() );
         dataHolder.set_PartOf_oid( get_PartOf_oid() );

      }
      catch (RuntimeException e)
      {
         throw new javax.ejb.EJBException(e);
      }

      return dataHolder;
   }

   public void setData( org.openxava.test.model.IngredientData dataHolder )
   {
      try
      {
         set_Name( dataHolder.get_Name() );
         set_PartOf_oid( dataHolder.get_PartOf_oid() );

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

      IngredientValue = null;
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

   private org.openxava.test.model.IngredientValue IngredientValue = null;

   public org.openxava.test.model.IngredientValue getIngredientValue()
   {
      IngredientValue = new org.openxava.test.model.IngredientValue();
      try
         {
            IngredientValue.setOid( getOid() );
            IngredientValue.setName( getName() );
            IngredientValue.setPartOf_oid( getPartOf_oid() );

         }
         catch (Exception e)
         {
            throw new javax.ejb.EJBException(e);
         }

	  return IngredientValue;
   }

   public void setIngredientValue( org.openxava.test.model.IngredientValue valueHolder )
   {

	  try
	  {
		 setName( valueHolder.getName() );
		 setPartOf_oid( valueHolder.getPartOf_oid() );

	  }
	  catch (Exception e)
	  {
		 throw new javax.ejb.EJBException(e);
	  }
   }

/* Value Objects END */

   public abstract java.lang.String getOid() ;

   public abstract void setOid( java.lang.String oid ) ;

   public abstract java.lang.String get_Name() ;

   public abstract void set_Name( java.lang.String _Name ) ;

   public abstract java.lang.String get_PartOf_oid() ;

   public abstract void set_PartOf_oid( java.lang.String _PartOf_oid ) ;

}
