/*
 * Generated file - Do not edit!
 */
package org.xavax.school.model;

import org.xavax.ejbx.*;

/**
 * Utility class for Teacher.
 */
public class TeacherUtil
{
   /** Cached remote home (EJBHome). Uses lazy loading to obtain its value (loaded by getHome() methods). */
   private static org.xavax.school.model.TeacherHome cachedRemoteHome = null;

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
    * @return Home interface for Teacher. Lookup using JNDI_NAME
    */
   public static org.xavax.school.model.TeacherHome getHome() throws javax.naming.NamingException
   {
      if (cachedRemoteHome == null) {
            cachedRemoteHome = (org.xavax.school.model.TeacherHome) lookupHome(null, org.xavax.school.model.TeacherHome.JNDI_NAME, org.xavax.school.model.TeacherHome.class);
      }
      return cachedRemoteHome;
   }

   /**
    * Obtain remote home interface from parameterised initial context
    * @param environment Parameters to use for creating initial context
    * @return Home interface for Teacher. Lookup using JNDI_NAME
    */
   public static org.xavax.school.model.TeacherHome getHome( java.util.Hashtable environment ) throws javax.naming.NamingException
   {
       return (org.xavax.school.model.TeacherHome) lookupHome(environment, org.xavax.school.model.TeacherHome.JNDI_NAME, org.xavax.school.model.TeacherHome.class);
   }

}
