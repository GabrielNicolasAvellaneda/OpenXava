package org.openxava.ejbx;

import javax.naming.*;

import org.openxava.util.*;

/**
 * It provides the default context to look up <i>Enterprise JavaBeans</i>. <p>
 *
 * It's required to configure <tt>BeansContext.properties</tt>. See the
 * doc in {@link BeansContextProviderFactory}.<br>
 *
 * @author  Javier Paniza
 */


public class BeansContext {

  private static IContextProvider provider;

  /**
   * Context to lookup EJBs. <p>
   *
   * @return Not null.
   */
  public static IContext get() throws NamingException {	  	
		IContext ctx = getProvider().getContext();
		if (getSubcontext() == null) return ctx;
		return new JndiContext((Context) ctx.lookup(getSubcontext()));
  }
  
  private final static IContextProvider getProvider() throws NamingException {
  	if (provider == null) {
  		try {
  			provider = BeansContextProviderFactory.create();
  		}
  		catch (InitException ex) {
  			ex.printStackTrace();
  			throw new NamingException(XavaResources.getString("create_error", IContextProvider.class.getName()));
  		}
  	}
  	return provider;
  }
  
  private static String getSubcontext() throws NamingException {
  	return BeansContextProviderFactory.getSubcontext();
  }
  
}
