package org.openxava.ejbx;

import java.io.*;
import java.security.*;
import java.sql.*;
import java.util.*;

import javax.ejb.*;
import javax.naming.*;

import org.openxava.util.*;


/**
 * Clase base de cualquier EJB. <p>
 *
 * Por supuesto, el uso de esta clase es opcional, pero
 * proporciona una serie de ventajas importantes.<br>
 * Basicamente contiene métodos que encapsulan el
 * acceso al <code>IEJBContext</code>, creandolo y envolviendo
 * las excepciones en formato de <code>EJBException</code>, claro
 * esta, se puede optar por la creación directa de un
 * <code>IEJBContext</code>.<br>
 * <b>Importante:</b> Es necesario llamar a {@link #setEJBContext} con
 * un valor no nulo antes de poder usar cualquiera de los métodos
 * de esta clase.<br>
 *
 * @see org.openxava.ejbx.IEJBContext
 * @version 00.10.26
 * @author  Javier Paniza
 */

public class EJBBase implements Serializable {
	
  static {
	  XSystem._setOnServer();
  }


  private final static String NO_CONTEXT =
	"El contexto (IEJBContext) usado por el bean no ha sido creado correctamente";

  private EJBContext ejbContext; // el de ejb
  private IEJBContext portableContext; // el nuestro (portable)
  private transient IContext context; // Para buscar otros EJBs
	private Map variablesBooleanas;  

  public EJBBase() {
	// System.out.println("(c) Gestión cuatrocientos, S.L.: Bean de Negocio creado");
  }
  /**
   * Como <code>EJBContext.getCallerPrincipal</code> en EJB 1.1. <br>
   *
   * @exception EJBException  Si hay algún problema, normalmente no se puede
   *                              crear el contexto portable.
   */
  public Principal getCallerPrincipal() {
	return getPortableContext().getCallerPrincipal();
  }
  /**
   * Devuelve una conexión JDBC por defecto. <br>
   *
   * @exception EJBException  Si hay problemas al obtener la conexión.
   */
  public Connection getConnection() {
	try {
	  return getPortableContext().getConnection();
	}
	catch (SQLException ex) {
	  throw new EJBException(ex.getLocalizedMessage());
	}
  }
  /**
   * Devuelve una conexión JDBC a partir de un nombre identificativo. <br>
   *
   * @param nombreDataSource  Nombre de la fuente de datos de donde obtener la conexión.<br>
   * @exception EJBException  Si hay problemas al obtener la conexión.
   */
  public Connection getConnection(String nombreDataSource) {
	try {
	  return getPortableContext().getConnection(nombreDataSource);
	}
	catch (SQLException ex) {
	  throw new EJBException(ex.getLocalizedMessage());
	}
  }
  /**
   * Devuelve un contexto, usado para buscar <i>homes</i> de EJBs. <p>
   *
   * La búsqueda se hace directamente sin especificar subcontextos estándars tales
   * como <tt>java:comp/env/ejb</tt>. Por supuesto, se pueden usar subcontextos
   * a modo clasificatorio.
   *
   * @exception javax.ejb.EJBException  Si encuentra algún problema al conseguir el contexto.
   */
  public IContext getContext() {
	if (context == null) {
	  try {
		context = BeansContext.get();
	  }
	  catch (NamingException ex) {
		ex.printStackTrace();
		throw new EJBException(ex.getMessage());
	  }
	}
	return context;
  }
  /**
   * El contexto usado para implementar todos los métodos. <br>
   *
   * @exception EJBException Si no es posible crear el contexto, algo
   *                 que se toma como un problema de sistema.
   */
  protected IEJBContext getPortableContext() {
	if (portableContext == null) {
	  if (ejbContext == null)
		throw new IllegalStateException(XavaResources.getString("ejbcontext_precondition"));
	  try {
		portableContext = EJBContextFactory.create(ejbContext);
	  }
	  catch (Exception ex) {
		ex.printStackTrace();
		throw new EJBException(XavaResources.getString("create_context_error"));
	  }
	}
	return portableContext;
  }
  /**
   * Valor de propiedad a partir del nombre. <br>
   * Devuelve <code>null</code> si la propiedad no
   * existe o no puede conseguirla por alguna cosa.<br>
   * @exception EJBException  Si hay algún problema
   */
  public String getProperty(String nombre)  {
	return getPortableContext().getProperty(nombre);
  }
/**
 * 
 * @return java.util.Map
 */
private Map getVariablesBooleanas() {
	if (variablesBooleanas == null) {
		variablesBooleanas = new HashMap();
	}
	return variablesBooleanas;
}
  /**
   * Como <code>EJBContext.isCallerInRole</code> en EJB 1.1. <br>
   *
   * @exception EJBException  Si hay algún problema, normalmente no se puede
   *                              crear el contexto portable.
   */
  public boolean isCallerInRole(String roleName) {
	return getPortableContext().isCallerInRole(roleName);
  }
/**
 * Examina una variable de entorno que puede contener true o false 
 * (no importan mayúsculas o minúsculas).
 *
 * @param variable  Nombre de la variable de entorno a examinar.
 * @param bean  Nombre del bean actual, usado para mensajes de error.
 * @return boolean <tt>true</tt> o <tt>false</tt> según valga la variable.
 * @exception EJBException  Si la variable no está definida.
 */
protected boolean isTrue(String variable, String bean) {
	Boolean result = (Boolean) getVariablesBooleanas().get(variable);
	if (result == null) {
		String valor = getProperty(variable);
		if (valor == null) {
			throw new EJBException(XavaResources.getString("var_in_ejb_required", variable, bean));
		}
		if (valor.trim().equalsIgnoreCase("true")) {
			result = new Boolean(true);
		}
		else if (valor.trim().equalsIgnoreCase("false")) {
			result = new Boolean(false);
		}
		else {
			throw new EJBException(XavaResources.getString("var_in_ejb_invalid_boolean_value", valor, variable, bean));			
		}
		getVariablesBooleanas().put(variable, result);
	}
	return result.booleanValue();
}
  /**
   * Establece el nombre de la fuente de datos usada
   * cuando se use {@link #getConnection()}. <br>
   *
   * @exception EJBException  Si hay algún problema
   */
  public void setDefaultDataSource(String nombreDataSource) {
	getPortableContext().setDefaultDataSource(nombreDataSource);
  }
  /**
   * Establece el contexto que será usado para implementar los métodos. <br>
   * Es obligado llamar a este método enviando algo diferente de nulo
   * antes de llamar a cualquier otro de esta clase.<br>
   * Se puede enviar nulo, en cuyo caso se desactiva el contexto interno,
   * es aconsable llamar <code>setEJBContext(null)</code> cuando el EJB Server
   * llame a <code>unsetEntityContext()</code>. <br>
   */
  protected void setEJBContext(EJBContext ejbContext) {
	this.ejbContext = ejbContext;
	if (ejbContext == null) portableContext = null;
  }
}
