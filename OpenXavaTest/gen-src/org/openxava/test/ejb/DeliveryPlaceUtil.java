/*
 * Generated file - Do not edit!
 */
package org.openxava.test.ejb;

import org.openxava.ejbx.*;

/**
 * Utility class for DeliveryPlace.
 */
public class DeliveryPlaceUtil
{
   /** Cached remote home (EJBHome). Uses lazy loading to obtain its value (loaded by getHome() methods). */
   private static org.openxava.test.ejb.DeliveryPlaceHome cachedRemoteHome = null;

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
      } finally {
         initialContext.close();
      }
   }

   // Home interface lookup methods

   /**
    * Obtain remote home interface from default initial context
    * @return Home interface for DeliveryPlace. Lookup using JNDI_NAME
    */
   public static org.openxava.test.ejb.DeliveryPlaceHome getHome() throws javax.naming.NamingException
   {
      if (cachedRemoteHome == null) {
            cachedRemoteHome = (org.openxava.test.ejb.DeliveryPlaceHome) lookupHome(null, org.openxava.test.ejb.DeliveryPlaceHome.JNDI_NAME, org.openxava.test.ejb.DeliveryPlaceHome.class);
      }
      return cachedRemoteHome;
   }

   /**
    * Obtain remote home interface from parameterised initial context
    * @param environment Parameters to use for creating initial context
    * @return Home interface for DeliveryPlace. Lookup using JNDI_NAME
    */
   public static org.openxava.test.ejb.DeliveryPlaceHome getHome( java.util.Hashtable environment ) throws javax.naming.NamingException
   {
       return (org.openxava.test.ejb.DeliveryPlaceHome) lookupHome(environment, org.openxava.test.ejb.DeliveryPlaceHome.JNDI_NAME, org.openxava.test.ejb.DeliveryPlaceHome.class);
   }

}

