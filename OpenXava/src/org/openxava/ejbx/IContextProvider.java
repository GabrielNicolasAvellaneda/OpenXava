package org.openxava.ejbx;

import javax.naming.*;

import org.openxava.ejbx.*;

/**
 * Provee un {@link IContext}. <p>
 *
 * Seg�n la implementaci�n puede hacer o no cach� del
 * contexto que provee.<br>
 *
 * @version 00.10.26
 * @author  Javier Paniza
 */

public interface IContextProvider {

  IContext getContext() throws NamingException;
}
