package org.openxava.ejbx;

import javax.naming.*;


/**
 * Provee un {@link IContext} que busca en el espacio JNDI. <p>
 *
 * No hace caché del contexto devuelto, lo crea cada vez.<br>
 * La búsqueda la hace a partir de <tt>new InitialContext()</tt> de
 * JNDI.<br>
 *
 * @version 00.10.26
 * @author  Javier Paniza
 */

public class JndiContextProvider implements IContextProvider {

  public JndiContextProvider() {
  }
  // Implementa IContextProvider
  public IContext getContext() throws NamingException {
  	return new JndiContext(new InitialContext());
  }
}
