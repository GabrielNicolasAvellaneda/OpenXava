/*
 * Generated file - Do not edit!
 */
package org.openxava.test.model;

import org.openxava.ejbx.*;

/**
 * Utility class for DeliveryDetail.
 */
public class DeliveryDetailUtil
{
   /** Cached remote home (EJBHome). Uses lazy loading to obtain its value (loaded by getHome() methods). */
   private static org.openxava.test.model.DeliveryDetailHome cachedRemoteHome = null;

   private static Object lookupHome(java.util.Hashtable environment, String jndiName, Class narrowTo) throws javax.naming.NamingException {
      // Obtain initial context
      IContext initialContext = BeansContext.get();
      try {
         Object objRef = initialContext.lookup(jndiName);
         // only narrow if necessary
         // if (narrowTo.isInstance(java.rmi.Remote.class)) // this does not like much to websphere 6
            return javax.rmi.PortableRemoteObject.narrow(objRef, narrowTo);
         /*else
            return objRef;*/
      } finally {
         initialContext.close();
      }
   }

   // Home interface lookup methods

   /**
    * Obtain remote home interface from default initial context
    * @return Home interface for DeliveryDetail. Lookup using JNDI_NAME
    */
   public static org.openxava.test.model.DeliveryDetailHome getHome() throws javax.naming.NamingException
   {
      if (cachedRemoteHome == null) {
            cachedRemoteHome = (org.openxava.test.model.DeliveryDetailHome) lookupHome(null, org.openxava.test.model.DeliveryDetailHome.JNDI_NAME, org.openxava.test.model.DeliveryDetailHome.class);
      }
      return cachedRemoteHome;
   }

   /**
    * Obtain remote home interface from parameterised initial context
    * @param environment Parameters to use for creating initial context
    * @return Home interface for DeliveryDetail. Lookup using JNDI_NAME
    */
   public static org.openxava.test.model.DeliveryDetailHome getHome( java.util.Hashtable environment ) throws javax.naming.NamingException
   {
       return (org.openxava.test.model.DeliveryDetailHome) lookupHome(environment, org.openxava.test.model.DeliveryDetailHome.JNDI_NAME, org.openxava.test.model.DeliveryDetailHome.class);
   }

}

