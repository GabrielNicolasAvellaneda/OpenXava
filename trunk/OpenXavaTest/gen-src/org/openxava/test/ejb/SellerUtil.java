/*
 * Generated file - Do not edit!
 */
package org.openxava.test.ejb;

import org.openxava.ejbx.*;

/**
 * Utility class for Seller.
 */
public class SellerUtil
{
   /** Cached remote home (EJBHome). Uses lazy loading to obtain its value (loaded by getHome() methods). */
   private static org.openxava.test.ejb.SellerHome cachedRemoteHome = null;

   private static Object lookupHome(java.util.Hashtable environment, String jndiName, Class narrowTo) throws javax.naming.NamingException {
      // Obtain initial context
      IContext initialContext = BeansContext.get();
      try {
         Object objRef = initialContext.lookup(jndiName);
         // only narrow if necessary
         if (narrowTo.isInstance(java.rmi.Remote.class))
            return javax.rmi.PortableRemoteObject.narrow(objRef, narrowTo);
         else
            return objRef;
      } catch (Exception ex) {
      	 throw new javax.naming.NamingException(ex.getMessage());
      } finally {
         initialContext.close();
      }
   }

   // Home interface lookup methods

   /**
    * Obtain remote home interface from default initial context
    * @return Home interface for Seller. Lookup using JNDI_NAME
    */
   public static org.openxava.test.ejb.SellerHome getHome() throws javax.naming.NamingException
   {
      if (cachedRemoteHome == null) {
            cachedRemoteHome = (org.openxava.test.ejb.SellerHome) lookupHome(null, org.openxava.test.ejb.SellerHome.JNDI_NAME, org.openxava.test.ejb.SellerHome.class);
      }
      return cachedRemoteHome;
   }

   /**
    * Obtain remote home interface from parameterised initial context
    * @param environment Parameters to use for creating initial context
    * @return Home interface for Seller. Lookup using JNDI_NAME
    */
   public static org.openxava.test.ejb.SellerHome getHome( java.util.Hashtable environment ) throws javax.naming.NamingException
   {
       return (org.openxava.test.ejb.SellerHome) lookupHome(environment, org.openxava.test.ejb.SellerHome.JNDI_NAME, org.openxava.test.ejb.SellerHome.class);
   }

}
