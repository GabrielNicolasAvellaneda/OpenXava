package org.openxava.ejbx;

import javax.naming.*;

import org.openxava.util.*;


/**
 * Adaptador de un contexto JNDI a un {@link IContext}. <b>
 * 
 * Si el nombre jndi empieza por @subcontext@ ignora esta palabra.
 *
 * @version 00.03.22
 * @author  Javier Paniza
 */

public class JndiContext implements IContext {

  private Context ctx;

  /**
   * Se crea a partir del contexto JNDI que se quiere envolver. <br>
   * <b>Precondiciones:</b>
   * <ul>
   * <li> ctx != null
   * </ul>
   */
  public JndiContext(Context ctx) {
  	Assert.arg(ctx);
  	this.ctx = ctx;
  }
  
  public Object lookup(String nombre) throws NamingException {
  	if (nombre.startsWith("@subcontext@")) nombre = nombre.substring("@subcontext@/".length()); // sinc con  doc de cabecera  	
		return ctx.lookup(nombre);
  }
  
  public void close() throws NamingException {
  	ctx.close();
  }
  
}
