/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.model.xejb;

/**
 * CMP layer for Color.
 */
public abstract class ColorCMP
   extends org.openxava.test.model.xejb.ColorBean
   implements javax.ejb.EntityBean
{

   public org.openxava.test.model.ColorData getData()
   {
      org.openxava.test.model.ColorData dataHolder = null;
      try
      {
         dataHolder = new org.openxava.test.model.ColorData();

         dataHolder.set_Name( get_Name() );
         dataHolder.setNumber( getNumber() );

      }
      catch (RuntimeException e)
      {
         throw new javax.ejb.EJBException(e);
      }

      return dataHolder;
   }

   public void setData( org.openxava.test.model.ColorData dataHolder )
   {
      try
      {
         set_Name( dataHolder.get_Name() );

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

      ColorValue = null;
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

   private org.openxava.test.model.ColorValue ColorValue = null;

   public org.openxava.test.model.ColorValue getColorValue()
   {
      ColorValue = new org.openxava.test.model.ColorValue();
      try
         {
            ColorValue.setName( getName() );
            ColorValue.setNumber( getNumber() );

         }
         catch (Exception e)
         {
            throw new javax.ejb.EJBException(e);
         }

	  return ColorValue;
   }

   public void setColorValue( org.openxava.test.model.ColorValue valueHolder )
   {

	  try
	  {
		 setName( valueHolder.getName() );

	  }
	  catch (Exception e)
	  {
		 throw new javax.ejb.EJBException(e);
	  }
   }

/* Value Objects END */

   public abstract java.lang.String get_Name() ;

   public abstract void set_Name( java.lang.String _Name ) ;

   public abstract int getNumber() ;

   public abstract void setNumber( int number ) ;

}
