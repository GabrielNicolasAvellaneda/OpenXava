/*
 * Generated by XDoclet - Do not edit!
 */
package org.openxava.test.ejb;

/**
 * Data object for Receptionist.
 */
public class ReceptionistData
   extends java.lang.Object
   implements java.io.Serializable
{
   private int oid;
   private java.lang.String _Name;
   private java.lang.String deliveryPlace_oid;

  /*
  // begin value object
   private org.openxava.test.ejb.ReceptionistValue ReceptionistValue = null;

   public org.openxava.test.ejb.ReceptionistValue getReceptionistValue()
   {
	  if( ReceptionistValue == null )
	  {
          ReceptionistValue = new org.openxava.test.ejb.ReceptionistValue();
	  }
      try
         {
            ReceptionistValue.setOid( getOid() );
            ReceptionistValue.setName( getName() );
            ReceptionistValue.setDeliveryPlace_oid( getDeliveryPlace_oid() );
                   }
         catch (Exception e)
         {
            throw new javax.ejb.EJBException(e);
         }

	  return ReceptionistValue;
   }

   public void setReceptionistValue( org.openxava.test.ejb.ReceptionistValue valueHolder )
   {

	  try
	  {
		 setName( valueHolder.getName() );
		 setDeliveryPlace_oid( valueHolder.getDeliveryPlace_oid() );
	  }
	  catch (Exception e)
	  {
		 throw new javax.ejb.EJBException(e);
	  }
   }

  // end value object

 */

   public ReceptionistData()
   {
   }

  /*
   public ReceptionistData( int oid,java.lang.String _Name,java.lang.String deliveryPlace_oid )
   {
      setOid(oid);
      set_Name(_Name);
      setDeliveryPlace_oid(deliveryPlace_oid);
   }
   */

   public ReceptionistData( ReceptionistData otherData )
   {
      setOid(otherData.getOid());
      set_Name(otherData.get_Name());
      setDeliveryPlace_oid(otherData.getDeliveryPlace_oid());

   }

   public org.openxava.test.ejb.ReceptionistKey getPrimaryKey() {
     org.openxava.test.ejb.ReceptionistKey pk = new org.openxava.test.ejb.ReceptionistKey(this.getOid());
     return pk;
   }

   public int getOid()
   {
      return this.oid;
   }
   public void setOid( int oid )
   {
      this.oid = oid;
   }

   public java.lang.String get_Name()
   {
      return this._Name;
   }
   public void set_Name( java.lang.String _Name )
   {
      this._Name = _Name;
   }

   public java.lang.String getDeliveryPlace_oid()
   {
      return this.deliveryPlace_oid;
   }
   public void setDeliveryPlace_oid( java.lang.String deliveryPlace_oid )
   {
      this.deliveryPlace_oid = deliveryPlace_oid;
   }

   public String toString()
   {
      StringBuffer str = new StringBuffer("{");

      str.append("oid=" + getOid() + " " + "_Name=" + get_Name() + " " + "deliveryPlace_oid=" + getDeliveryPlace_oid());
      str.append('}');

      return(str.toString());
   }

   public boolean equals( Object pOther )
   {
      if( pOther instanceof ReceptionistData )
      {
         ReceptionistData lTest = (ReceptionistData) pOther;
         boolean lEquals = true;

         lEquals = lEquals && this.oid == lTest.oid;
         if( this._Name == null )
         {
            lEquals = lEquals && ( lTest._Name == null );
         }
         else
         {
            lEquals = lEquals && this._Name.equals( lTest._Name );
         }
         if( this.deliveryPlace_oid == null )
         {
            lEquals = lEquals && ( lTest.deliveryPlace_oid == null );
         }
         else
         {
            lEquals = lEquals && this.deliveryPlace_oid.equals( lTest.deliveryPlace_oid );
         }

         return lEquals;
      }
      else
      {
         return false;
      }
   }

   public int hashCode()
   {
      int result = 17;

      result = 37*result + (int) oid;

      result = 37*result + ((this._Name != null) ? this._Name.hashCode() : 0);

      result = 37*result + ((this.deliveryPlace_oid != null) ? this.deliveryPlace_oid.hashCode() : 0);

      return result;
   }

}
