package org.openxava.ejbx;

import javax.naming.*;

import org.openxava.ejbx.*;

/**
 * Provee un {@link IContext}. <p>
 *
 * Según la implementación puede hacer o no caché del
 * contexto que provee.<br>
 *
 * @version 00.10.26
 * @author  Javier Paniza
 */

public interface IContextProvider {

  IContext getContext() throws NamingException;
}
