/*
 * Generated file - Do not edit!
 */
package org.openxava.escuela.modelo;

import org.openxava.ejbx.*;

/**
 * Utility class for Profesor.
 */
public class ProfesorUtil
{
   /** Cached remote home (EJBHome). Uses lazy loading to obtain its value (loaded by getHome() methods). */
   private static org.openxava.escuela.modelo.ProfesorHome cachedRemoteHome = null;

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
   public static org.openxava.escuela.modelo.ProfesorHome getHome() throws javax.naming.NamingException
   {
      if (cachedRemoteHome == null) {
            cachedRemoteHome = (org.openxava.escuela.modelo.ProfesorHome) lookupHome(null, org.openxava.escuela.modelo.ProfesorHome.JNDI_NAME, org.openxava.escuela.modelo.ProfesorHome.class);
      }
      return cachedRemoteHome;
   }

   /**
    * Obtain remote home interface from parameterised initial context
    * @param environment Parameters to use for creating initial context
    * @return Home interface for Profesor. Lookup using JNDI_NAME
    */
   public static org.openxava.escuela.modelo.ProfesorHome getHome( java.util.Hashtable environment ) throws javax.naming.NamingException
   {
       return (org.openxava.escuela.modelo.ProfesorHome) lookupHome(environment, org.openxava.escuela.modelo.ProfesorHome.JNDI_NAME, org.openxava.escuela.modelo.ProfesorHome.class);
   }

}
