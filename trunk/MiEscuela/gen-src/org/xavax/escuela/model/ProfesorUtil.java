/*
 * Generated file - Do not edit!
 */
package org.xavax.escuela.model;

import org.xavax.ejbx.*;

/**
 * Utility class for Profesor.
 */
public class ProfesorUtil
{
   /** Cached remote home (EJBHome). Uses lazy loading to obtain its value (loaded by getHome() methods). */
   private static org.xavax.escuela.model.ProfesorHome cachedRemoteHome = null;

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
    * @return Home interface for Profesor. Lookup using JNDI_NAME
    */
   public static org.xavax.escuela.model.ProfesorHome getHome() throws javax.naming.NamingException
   {
      if (cachedRemoteHome == null) {
            cachedRemoteHome = (org.xavax.escuela.model.ProfesorHome) lookupHome(null, org.xavax.escuela.model.ProfesorHome.JNDI_NAME, org.xavax.escuela.model.ProfesorHome.class);
      }
      return cachedRemoteHome;
   }

   /**
    * Obtain remote home interface from parameterised initial context
    * @param environment Parameters to use for creating initial context
    * @return Home interface for Profesor. Lookup using JNDI_NAME
    */
   public static org.xavax.escuela.model.ProfesorHome getHome( java.util.Hashtable environment ) throws javax.naming.NamingException
   {
       return (org.xavax.escuela.model.ProfesorHome) lookupHome(environment, org.xavax.escuela.model.ProfesorHome.JNDI_NAME, org.xavax.escuela.model.ProfesorHome.class);
   }

}
