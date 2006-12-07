package org.openxava.ejbx;

import javax.naming.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Provides a {@link IContext} that look up in JNDI space. <p>
 *
 * It don't make caché of returned context, it create one new each time. <br>
 * 
 * The look up is made using <tt>new InitialContext()</tt> of JNDI.
 *
 * @author  Javier Paniza
 */

public class JndiContextProvider implements IContextProvider {

  private static Log log = LogFactory.getLog(JndiContextProvider.class);
  
  public IContext getContext() throws NamingException {
  	return new JndiContext(new InitialContext());
  }
  
}
