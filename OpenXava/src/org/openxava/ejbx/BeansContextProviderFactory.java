package org.openxava.ejbx;

import javax.naming.*;

import org.openxava.util.*;

import java.util.*;

/**
 * Clase que se encarga de crear <code>IContextProvider</code>s para
 * acceder a EJBs. <p>
 *
 * Esta clase provee la implementación de {@link BeansContext}, de
 * esta forma es posible configurar su comportamiento declarativament.
 * Es necesario tener un archivo de propiedades llamado
 * <code>BeansContext.properties</code> situado en la ruta del
 * CLASSPATH. Este archivo ha de ser de la forma <code>nombre=clase</code>.
 * Como sigue:
 * <pre>
 * _defecto=javacomp
 * _subcontexto=
 * javacomp=puntocom.negocio.context.JavaCompBeansContextProvider
 * jndi=puntocom.negocio.context.JndiBeansContextProvider
 * </pre>
 * O en inglés:
 * <pre>
 * _default=javacomp
 * _subcontext=
 * javacomp=puntocom.negocio.context.JavaCompBeansContextProvider
 * jndi=puntocom.negocio.context.JndiBeansContextProvider
 * </pre> 
 * La entrada <code>_default/_defecto</code> indica el objeto usado por defecto. El
 * objeto por defecto se crea llamando a {@link #create()} sin argumentos.
 * Las clases indicadas han de implementar {@link IContextProvider}.<br>
 * La entrada <code>_subcontext/_subcontexto</code> indica el subcontexto de busqueda usado,
 * puede dejarse en blanco o incluso no poner la entrada, entonces buscará
 * a partir del subcontexto raiz.<br>
 *
 * @author  Javier Paniza
 */

public class BeansContextProviderFactory {

  // Si cambian las variables final cambiar doc de cabecera y de create()
  private final static String PROPERTIES_FILE = "BeansContext.properties";
  private final static String SUBCONTEXT_PROPERTY_ES = "_subcontexto";
  private final static String SUBCONTEXT_PROPERTY_EN = "_subcontext";
  
  private static boolean subcontextReaded = false;
  private static String subcontext;  
  
  private static Factory impl = new Factory( // En todo el CLASSPATH
	BeansContextProviderFactory.class.getClassLoader().getResource(PROPERTIES_FILE));
//  private static Factory impl = new Factory( // Donde está la clase
//    BeansContextProviderFactory.class.getResource(ARCHIVO_PROPIEDADES));


  /**
   * Crea un proveedor de contexto para buscar EJBs por defecto. <p>
   *
   * <b>Postcondición:</b>
   * <ul>
   * <li> return != null
   * </ul>
   * Lo hace según la entrada indicada en la entrada <code>_default/_defecto</code>
   * de <code>BeansContext.properties</code>.<br>
   * @exception InitException  Si hay algún problema el iniciar.
   */
  public static IContextProvider create() throws InitException {
	return (IContextProvider) impl.create();
  }
  /**
   * Crea el proveedor de contexto para buscar EJBs indicado. <p>
   *
   * <b>Postcondición:</b>
   * <ul>
   * <li> return != null
   * </ul>
   * El argumento es el nombre identificativo del proveedor de contexto, el cual está
   * registrado en el archivo <code>BeansContext.properties</code>.<br>
   * @exception InitException  Si hay algún problema el iniciar el contexto.
   */
  public static IContextProvider create(String nombre) throws InitException {
	return (IContextProvider) impl.create(nombre);
  }
/**
 * @return Puede ser nulo
 */
static String getSubcontext() throws NamingException {
	if (!subcontextReaded) { 
		try {
			Properties pro = new Properties();			
			pro.load(BeansContextProviderFactory.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
			subcontext = pro.getProperty(SUBCONTEXT_PROPERTY_EN);
			if (Is.emptyString(subcontext)) subcontext = pro.getProperty(SUBCONTEXT_PROPERTY_ES);
			if (Is.emptyString(subcontext)) subcontext = null;
			subcontextReaded = true;
			System.out.println("[BeansContextProviderFactory] subcontexto=" + subcontext);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new NamingException(XavaResources.getString("subcontext_error", ex.getLocalizedMessage()));
		}
	}
	return subcontext;
}
}
