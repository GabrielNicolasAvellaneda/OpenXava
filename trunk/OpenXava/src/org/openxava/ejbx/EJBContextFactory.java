package org.openxava.ejbx;

import javax.ejb.*;

import org.openxava.util.*;


/**
 * Crea <code>IEJBContext</code>s. <p>
 *
 * @author  Javier Paniza
 */


public class EJBContextFactory {



  /**
   * Crea un <code>IEJBContext</code> usando para su implentación el <code>EJBContext</code> enviado. <br>
   * <b>Postcondición:</b>
   * <ul>
   * <li> return != null
   * </ul>
   * Enviar <code>null</code> como argumento provocará que la invariante del objeto creado no
   * se cumpla, con las consecuencias que ello implica (lanzamiento de excepciones al intentar usarlo).<br>
 * @throws InitException
   */
  public static IEJBContext create(EJBContext ejbContext) throws InitException  {
		IEJBContextInit rs = new EJB11Context();
		rs.setEJBContext(ejbContext);
		return rs;
  }
}
