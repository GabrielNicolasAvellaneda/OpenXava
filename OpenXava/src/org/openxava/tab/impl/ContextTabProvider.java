package org.openxava.tab.impl;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import org.openxava.ejbx.*;
import org.openxava.util.*;


/**
 * Un <code>ITabProvider</code> que se configura a
 * partir de un {@link org.openxava.ejbx.IEJBContext}. <p>
 *
 * Está implementado delegando en un {@link JDBCTabProvider}. <br>
 * El nombre de las propiedades es totalmente configurable.<br>
 *
 * Es necesario llamar a {@link #setContext} antes de usar el objeto,
 * después de eso conveniente llamar al método {@link #invariante}, para
 * comprobar que está bien configurado.<br>
 *
 * @author  Javier Paniza
 */


public class ContextTabProvider implements ITabProvider, java.io.Serializable {

	private String propiedadTabla = "TABLA";
	private String propiedadCampos = "CAMPOS";
	private String propiedadCamposPK = "CAMPOS_PK";
	private String[] propiedadesCondiciones = { "CONDICION_TODOS" };
	private String[] cabecerasDefecto = { "Todos" };
	private int[] indicesPK = null;
	private String errorSetCamposPK = null;
	// Controla si ha habido error al llamar a setCampos
	private String erroresCondiciones = null;
	// Controla si ha habido error en las propiedades de la condiciones
	private boolean setContextLlamado = false;

	private JDBCTabProvider tabProvider = new JDBCTabProvider();

	public ContextTabProvider() {
	}
	// Implementa ITabProvider
	public void search(int indice, Object clave)
		throws FinderException, RemoteException {
		tabProvider.search(indice, clave);
	}
	/**
	 * Indices de los campos que forma la Primary Key del objeto. <br>
	 */
	public int[] getIndexesPK() {
		return indicesPK;
	}
	/**
	 * Nombre de la propiedad de donde se obtendrá el nombre de los campos. <br>
	 * Cuando se rellene el valor de la propiedad hay
	 * que poner los campos separados por comas, tal como
	 * en un SELECT de SQL. <br>
	 */
	public String getFieldsProperty() {
		return propiedadCampos;
	}
	/**
	 * Nombre de la propiedad de donde se obtiene la lista de campos que serán la
	 * clave primaria. <br>
	 * La clave primería es usada para construir un objeto
	 * de negocio (EntityBean por le general) a partir de una
	 * tupla.<br>
	 * Cuando se rellene el valor de la propiedad hay
	 * que poner los campos separados por comas, tal como
	 * en un SELECT de SQL. <br>
	 */
	public String getPKFieldsProperty() {
		return propiedadCamposPK;
	}
	/**
	 * Nombre de las propiedades que contienen las condiciones para las consultas. <p>
	 *
	 * La sintaxis de las condiciones es SQL. Para ver una explicación detallada
	 * de la sintaxis de las condiciones ver {@link JDBCTabProvider#getCondiciones}.<br>
	 */
	public String[] getConditionsProperties() {
		return propiedadesCondiciones;
	}
	// Propiedades

	/** Nombre de la propiedad de donde se obtendrá el nombre de la tabla de la DB. */
	public String getTableProperty() {
		return propiedadTabla;
	}
	/**
	 * Comprueba la invariante del objeto. <br>
	 * La invariante se cumple si se rellenan correctamente
	 * las propiedades indicadas en
	 * <code>propiedadTabla, propiedadCampos, propiedadCamposPK, propiedadesCondiciones</code> y
	 * <code>propiedadDataSource</code>.<br>
	 * Es, pues, necesario llamar primero a {@link #setContext}.
	 * @exception IllegalStateException Si no se cumple la invariante.
	 */
	public void invariant() throws IllegalStateException {
		if (!setContextLlamado) {
			throw new IllegalStateException(XavaResources.getString("tabprovider_context_required"));
		}
		tabProvider.invariant();
		if (errorSetCamposPK != null) {
			throw new IllegalStateException(errorSetCamposPK);
		}
		if (indicesPK == null || indicesPK.length == 0) {
			throw new IllegalStateException(XavaResources.getString("tabprovider_campos_pk_required"));
		}
		if (erroresCondiciones != null) {
			throw new IllegalStateException(erroresCondiciones);
		}
	}
	// Convierte un String con una lista separadas por comas en un String []
	// Si lista == null return null
	private String[] listaToArray(String lista) {
		if (lista == null)
			return null;
		StringTokenizer st = new StringTokenizer(lista, ",");
		int nt = st.countTokens();
		String[] rs = new String[nt];
		for (int i = 0; i < nt; i++) {
			rs[i] = st.nextToken().trim();
		}
		return rs;
	}
	/** Configura indicesPK a partir de los camposPK enviados. */
	private void setCamposPK(String[] camposPK) {
		errorSetCamposPK = null;
		if (camposPK.length == 0) {
			errorSetCamposPK =
				"Es obligado que haya al menos un campo en CAMPOS_PK";
			return;
		}
		Vector campos = toVector(tabProvider.getFields());
		int[] idx = new int[camposPK.length];
		for (int i = 0; i < camposPK.length; i++) {
			idx[i] = campos.indexOf(camposPK[i]);
			if (idx[i] == -1) {
				errorSetCamposPK =
					"Es obligado que los CAMPOS_PK estén contenidos en CAMPOS";
				return;
			}
		}
		setIndexesPK(idx);
	}
	/**
	 * Configura el bean según el <code>IEJBContext</code> enviado. <br>
	 * Lo hace según las propiedades del <code>IEJBContext</code>, indicadas en
	 * <code>propiedadTabla, propiedadCampos, propiedadCamposPK, propiedadesCondiciones</code>. <br>
	 * Es necesario llamar a este método antes de usar el objeto.<br>
	 */
	public void setContext(IEJBContext ctx) {
		if (ctx == null)
			return;
		setContextLlamado = true;
		if (propiedadTabla != null) {
			String tabla = ctx.getProperty(propiedadTabla);
			if (tabla != null)
				tabProvider.setTable(tabla);
		}

		if (propiedadCampos != null) {
			String[] campos = listaToArray(ctx.getProperty(propiedadCampos));
			if (campos != null)
				tabProvider.setFields(campos);
		}

		if (propiedadCamposPK != null) {
			String[] camposPK =
				listaToArray(ctx.getProperty(propiedadCamposPK));
			if (camposPK != null)
				setCamposPK(camposPK);
		}
		StringBuffer errores = new StringBuffer();
		erroresCondiciones = null;
		if (propiedadesCondiciones != null) {
			String[] condiciones = new String[propiedadesCondiciones.length];
			for (int i = 0; i < propiedadesCondiciones.length; i++) {
				String condicion = ctx.getProperty(propiedadesCondiciones[i]);
				if (condicion == null) {
					errores.append("No está definida la variable de entorno ");
					errores.append(propiedadesCondiciones[i]);
					errores.append('\n');
				}
				condiciones[i] = condicion;
			}
			tabProvider.setConditions(condiciones);
			if (errores.length() > 0) {
				erroresCondiciones = errores.toString();
			}
		}

		// El ConnectionProvider
		tabProvider.setConnectionProvider(ctx);
	}
	/**
	 * Indices de los campos que forma la Primary Key del objeto. <br>
	 */
	private void setIndexesPK(int[] indicesPK) {
		this.indicesPK = indicesPK;
	}
	/**
	 * Nombre de la propiedad de donde se obtendrá el nombre de los campos. <br>
	 * Cuando se rellene el valor de la propiedad hay
	 * que poner los campos separados por comas, tal como
	 * en un SELECT de SQL. <br>
	 */
	public void setFieldsProperty(String propiedadCampos) {
		this.propiedadCampos = propiedadCampos;
	}
	/**
	 * Nombre de la propiedad de donde se obtiene la lista de campos que serán la
	 * clave primaria. <br>
	 * La clave primería es usada para construir un objeto
	 * de negocio (EntityBean por le general) a partir de una
	 * tupla.<br>
	 * Cuando se rellene el valor de la propiedad hay
	 * que poner los campos separados por comas, tal como
	 * en un SELECT de SQL. <br>
	 */
	public void setPKFieldsProperty(String propiedadCamposPK) {
		this.propiedadCamposPK = propiedadCamposPK;
	}
	/**
	 * Nombre de las propiedades que contienen las condiciones para las consultas. <p>
	 *
	 * La sintaxis de las condiciones es SQL. Para ver una explicación detallada
	 * de la sintaxis de las condiciones ver {@link JDBCTabProvider#getCondiciones}.<br>
	 */
	public void setConditionsProperties(String[] propiedadesCondiciones) {
		this.propiedadesCondiciones = propiedadesCondiciones;
	}
	/** Nombre de la propiedad de donde se obtendrá el nombre de la tabla de la DB. */
	public void setTableProperty(String propiedadTabla) {
		this.propiedadTabla = propiedadTabla;
	}
	// Implementa ITabProvider
	public DataChunk nextChunk() throws RemoteException {
		return tabProvider.nextChunk();
	}
	/** Convierte un array en un vector. */
	private Vector toVector(Object[] array) {
		if (array == null)
			return null;
		int l = array.length;
		Vector rs = new Vector(l);
		for (int i = 0; i < l; i++) {
			rs.addElement(array[i]);
		}
		return rs;
	}
	public void search(String condicion, Object clave) throws FinderException, RemoteException {
		tabProvider.search(condicion, clave);
		
	}
	public int getResultSize() throws RemoteException {			
		return tabProvider.getResultSize();
	}
	
	public void reset() throws RemoteException {
		tabProvider.reset();
	}

}
