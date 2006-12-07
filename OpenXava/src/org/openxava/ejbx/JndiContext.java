package org.openxava.ejbx;

import javax.naming.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.util.*;


/**
 * Adapter from a JNDI context to a {@link IContext}. <b>
 * 
 * If the JNDI name starts with @subcontext@ ignores this word.
 *
 * @author  Javier Paniza
 */

public class JndiContext implements IContext {

  private Context ctx;
  private static Log log = LogFactory.getLog(JndiContext.class);

  /**
   * It's created from JNDI context that it wants wrap.
   *
   * <b>Preconditions:</b>
   * <ul>
   * <li> ctx != null
   * </ul>
   */
  public JndiContext(Context ctx) {
  	Assert.arg(ctx);
  	this.ctx = ctx;
  }
  
  public Object lookup(String name) throws NamingException {
  	if (name.startsWith("@subcontext@")) name = name.substring("@subcontext@/".length()); // sync with heading doc  	
		return ctx.lookup(name);
  }
  
  public void close() throws NamingException {
  	ctx.close();
  }
  
}
