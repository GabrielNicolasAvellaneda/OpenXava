package org.openxava.ejbx;

import java.io.*;
import java.security.*;
import java.sql.*;
import java.util.*;

import javax.ejb.*;
import javax.naming.*;
import javax.sql.*;

import org.openxava.util.*;


/**
 * Implantación de un <code>IEJBContext</code> para un servidor EJB 1.1. <p>
 *
 * Dado que EJB 1.1 normaliza bastantes cosas solo es necesaria una versión,
 * y no hacen falta archivos de propiedades ni nada parecido.<br>
 *
 * Si se quiere usar {@link #getConnection() } sin argumentos, hay que definir en el bean
 * una propiedad llamada DATA_SOURCE que indique la fuente de datos por defecto.<p>
 * En la propiedad DATA_SOURCE también se indicar usuario y clave por defecto, 
 * así <tt>datasource;usuario;clave</tt>.<br>  
 *
 * Se recomienda (aunque no es obligado) que las fuentes de datos (<i>DataSource</i>)
 * se incluyan dentro de <tt>jdbc</tt>, por ejemplo: <tt>jdbc/Contabilidad, jdbc/Personal</tt>.
 * La busqueda de un <i>DataSource</i> se hace a partir de <tt>java:comp/env/</tt>. <p>
 *
 * @author  Javier Paniza
 */

public class EJB11Context implements IEJBContextInit, Serializable {

	// Si se cambia cambiar doc de cabecera, de getConnection() y de IEJBContext
	private final static String PROPIEDAD_DATA_SOURCE_DEFECTO = "DATA_SOURCE";

	private final static String PRE_DS = "java:comp/env/";
	// si se cambia, cambiar doc de cabecera
	private final static String PRE_PRO = "java:comp/env/";
	// si se cambia, cambiar doc de cabecera
	private EJBContext ejbContext;
	private transient Context jndiContext;
	private String dataSourceDefecto;
	private String usuario;
	private String clave;

	/** Constructor por defecto. */
	public EJB11Context() {
	}
	private void assertEJBContext() throws IllegalStateException {
		if (ejbContext == null) {
			throw new IllegalStateException(XavaResources.getString("ejb11context_invariant"));
		}
	}
	/**
	 * Establece dataSourceDefecto, y si el usuario no está establecido
	 * lo establece. <p>
	 *
	 * Confía en un valor de propieda de esta forma: <tt>datasource;usuario;clave</tt>,
	 * aunque el separador puede ser también coma y dos puntos.<br> 
	 */
	private void establecerDataSourceDefecto() {
		dataSourceDefecto = getProperty(PROPIEDAD_DATA_SOURCE_DEFECTO);
		if (usuario == null) {
			StringTokenizer st = new StringTokenizer(dataSourceDefecto, ";,:");
			if (st.hasMoreTokens())
				dataSourceDefecto = st.nextToken();
			if (st.hasMoreTokens())
				usuario = st.nextToken();
			if (st.hasMoreTokens())
				clave = st.nextToken();
		}
	}
	// Implementa IEJBContext
	public Principal getCallerPrincipal() {
		assertEJBContext();
		return ejbContext.getCallerPrincipal();
	}
	// Implementa IEJBContext
	/**
	 * La conexión se obtiene del dataSource definido en la propidad DATA_SOURCE del bean. <br>
	 * También se puede especificar mediante código la fuente de datos por defecto,
	 * llamando a {#setDefaultDataSource}, esto está por delante de lo definido en DATA_SOURCE.
	 */
	public Connection getConnection() throws SQLException {
		if (dataSourceDefecto == null) {
			establecerDataSourceDefecto();
			if (Is.emptyString(dataSourceDefecto)) {
				throw new SQLException(XavaResources.getString("ejb_datasource_required"));
			}
		}
		return getConnection(dataSourceDefecto);
	}
	// Implementa IEJBContext
	public Connection getConnection(String nombre) throws SQLException {
		try {
			DataSource ds = (DataSource) getJndiContext().lookup(PRE_DS + nombre);
			if (usuario == null) {
				return ds.getConnection();
			} else {
				return ds.getConnection(usuario, clave);
			}
		} catch (NamingException ex) {
			throw new SQLException(XavaResources.getString("datasource_not_found", nombre));
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(XavaResources.getString("datasource_not_found", nombre));									
		}

	}
	private Context getJndiContext() {
		if (jndiContext == null) {
			try {
				jndiContext = new InitialContext();
			} catch (NamingException ex) {
				throw new EJBException(XavaResources.getString("create_error", "InitialContext"));
			}
		}
		return jndiContext;
	}
	// Implementa IEJBContext
	public String getProperty(String nombre) {
		Object rs = null;
		try {
			rs = getJndiContext().lookup(PRE_PRO + nombre);
		} catch (Exception ex) {
			ex.printStackTrace();
			// se de vuelve null
		}
		if (rs == null)
			return null;
		return rs.toString();
	}
	
	// Implementa IEJBContext
	public boolean isCallerInRole(String roleName) {
		assertEJBContext();
		return ejbContext.isCallerInRole(roleName);
	}
	/**
	 * @see org.openxava.util.IConnectionProvider
	 */
	public void setPassword(java.lang.String clave) {
		this.clave = clave;
	}
	// Implementa IEJBContext
	public void setDefaultDataSource(String nombreDataSource) {
		dataSourceDefecto = nombreDataSource;
	}
	// Implementa IEJBContextInit
	public void setEJBContext(EJBContext ejbContext) {
		this.ejbContext = ejbContext;
	}
	/**
	 * @see org.openxava.util.IConnectionProvider
	 */
	public void setUser(java.lang.String usuario) {
		this.usuario = usuario;
	}
}