package org.openxava.ejbx;

import javax.naming.*;

import org.openxava.util.*;

/**
 * Proporciona el contexto por defecto para buscar <i>Enterprise JavaBeans</i>. <p>
 *
 * Es necesario configurar <tt>BeansContext.properties</tt>, para ver c�mo
 * ir a la documentaci�n de {@link BeansContextProviderFactory}.<br>
 *
 *
 * @author  Javier Paniza
 */


public class BeansContext {

  private static IContextProvider provider;

  /**
   * Contexto para buscar EJBs. <p>
   *
   * @return Nunca ser� nulo.
   */
  public static IContext get() throws NamingException {	  	
		IContext ctx = getProvider().getContext();
		if (getSubcontexto() == null) return ctx;
		return new JndiContext((Context) ctx.lookup(getSubcontexto()));
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
  
/**
 * Inserte aqu� la descripci�n del m�todo.
 * Fecha de creaci�n: (02/08/2001 13:57:43)
 * @return java.lang.String
 */
private static String getSubcontexto() throws NamingException {
	return BeansContextProviderFactory.getSubcontext();
}
}
