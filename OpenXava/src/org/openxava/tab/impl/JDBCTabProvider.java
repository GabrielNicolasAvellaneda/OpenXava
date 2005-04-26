package org.openxava.tab.impl;

import java.rmi.*;
import java.sql.*;
import java.util.*;

import javax.ejb.*;

import org.openxava.util.*;


/**
 * Un <code>ITabProvider</code> que obtiene los datos vía JDBC. <p>
 *
 * Es un JavaBean que permite establecer propiedades como el
 * nombre de la tabla, los campos, las condiciones de búsqueda, etc. <br>
 *
 * Antes de usar el objeto es conveniente llamar al método {@link #invariante}.<p>
 *
 * @author  Javier Paniza
 */


public class JDBCTabProvider implements ITabProvider, java.io.Serializable {

	private static final int DEFAULT_CHUNK_SIZE = 50;

	private String select; // Select ... from ...
	private String selectSize;
	private String table;
	private String[] fields;
	private String[] conditions;
	private Object[] key;
	private IConnectionProvider connectionProvider;
	private int chunkSize = DEFAULT_CHUNK_SIZE;
	private int current;  
	private boolean eof = true;
	
	/** Constructor por defecto. */
	public JDBCTabProvider() {
	}
	// Implementa IBuscar
	public void search(int indice, Object clave)
		throws FinderException, RemoteException {
		try {
			search(conditions[indice], clave);
		}
		catch (IndexOutOfBoundsException ex) {
			throw new IndexOutOfBoundsException(
				"La busqueda " + indice + " no está definida");
		}
	}

	// Implementa IBuscar	
	public void search(String condicion, Object clave) throws FinderException, RemoteException {				
		current = 0;
		eof = false;
		this.key = toArray(clave);			
		condicion = condicion == null ? "" : condicion.trim().toUpperCase();		
		if (condicion.equals(""))
			select = generarSelect(); // para todos
		else if (condicion.startsWith("SELECT"))
			select = condicion;
		else
			select = generarSelect() + " WHERE " + condicion;					
		selectSize = crearSelectTamaño(select);
	}
	
	// Crea y devuelve el select a partir de las propiedades tabla y los campos
	private String generarSelect() {
		if (table == null
			|| table.trim().equals("")
			|| fields == null
			|| fields.length == 0) {
			return null;
		}

		StringBuffer nuevo = new StringBuffer("SELECT ");
		int i;
		for (i = 0; i < fields.length - 1; i++) {
			nuevo.append(fields[i]);
			nuevo.append(", ");
		}
		nuevo.append(fields[i]); // El último sin coma
		nuevo.append(" FROM ");
		nuevo.append(table);
		return nuevo.toString();
	}
	/** Campos del tabla a incluir. */
	public String[] getFields() {
		return fields;
	}
	/**
	 * Lista de condiciones en formato SQL. <p>
	 *
	 * Las condiciones pueden ser:
	 * <ul>
	 * <li> Un sentencia SELECT de SQL íntegra.
	 * <li> La sentencia SELECT a partir del WHERE (sin incluir éste). En este
	 *      caso la sentencia SQL completa se forma a partir de los valores de
	 *      las propiedades {@link #getCampos campos} y {@link #getTabla tabla}.
	 * <li> Nada. En cuyo caso hace un SELECT de todos.
	 * </ul>
	 *
	 * Aunque se use en todos los casos un SELECT íntegro es necesario especificar
	 * valor correcto para {@link #getCampos campos} y {@link #getTabla tabla}. Cuando
	 * se usen SELECT íntegros, la tabla y los campos a usar en el SELECT debe
	 * de coincidir con la de las propiedades <tt>campos</tt> y <tt>tabla</tt>.<br>
	 */
	// Si se cambia esta doc, cambiar la de EJBTabBase#getPropiedadesCondiciones
	// y las posibles plantillas para crear xxxTabs.
	public String[] getConditions() {
		return conditions;
	}
	/** Objeto del que se obtiene la conexión. */
	public IConnectionProvider getConnectionProvider() {
		return connectionProvider;
	}
	/** Nombre de la tabla en la DB. */
	public String getTable() {
		return table;
	}
	/** Tamaño de los bloques que se envían cuando se llama a {@link #siguienteTrozo}. */
	public int getChunkSize() {
		return chunkSize;
	}
	/**
	 * Comprueba la invariante del objeto. <p>
	 * <b>Invariante:</b>
	 * <ul>
	 * <li> tabla != null
	 * <li> campos != null && campos.length > 0
	 * <li> condiciones != null && condiciones.length > 0
	 * <li> connectionProvider != null
	 * </ul>
	 *
	 * @exception IllegalStateException  Si no se cumple la invariante.
	 */
	public void invariant() throws IllegalStateException {
		if (table == null)
			throw new IllegalStateException(XavaResources.getString("tabprovider_table_required"));
		if (fields == null)
			throw new IllegalStateException(XavaResources.getString("tabprovider_field_required"));
		if (conditions == null || conditions.length == 0)
			throw new IllegalStateException(XavaResources.getString("tabprovider_condition_required"));
		if (connectionProvider == null) {
			throw new IllegalStateException(XavaResources.getString("tabprovider_connection_provider_required"));
		}
	}
	/**
	 * Nos posicionamos en el <code>ResultSet</code> en la parte que toque.
	 *
	 * @param rs  <tt>!= null</tt>
	 */
	private void posicionar(ResultSet rs) throws SQLException {
		try {
			// Lo intentamos a estilo JDBC 2
			rs.absolute(current);
		}
		catch (Throwable ex) { // No hay que fiarse de los controladores JDBC
			// ... y ahora a estilo JDBC 1
			for (int i = 0; i < current; i++) {
				if (!rs.next())
					return;
			}
		}
	}
	/** Campos del tabla a incluir. */
	public void setFields(String[] campos) {
		this.fields = campos;
	}
	
	/**
	 * Lista de condiciones en formato SQL. <p>
	 *
	 * Las condiciones pueden ser:
	 * <ul>
	 * <li> Un sentencia SELECT de SQL íntegra.
	 * <li> La sentencia SELECT a partir del WHERE (sin incluir éste). En este
	 *      caso la sentencia SQL completa se forma a partir de los valores de
	 *      las propiedades {@link #getCampos campos} y {@link #getTabla tabla}.
	 * <li> Nada. En cuyo caso hace un SELECT de todos.
	 * </ul>
	 *
	 * Aunque se use en todos los casos un SELECT íntegro es necesario especificar
	 * valor correcto para {@link #getCampos campos} y {@link #getTabla tabla}. Cuando
	 * se usen SELECT íntegros, la tabla y los campos a usar en el SELECT debe
	 * de coincidir con la de las propiedades <tt>campos</tt> y <tt>tabla</tt>.<br>
	 */
	// Si se cambia esta doc, cambiar la de EJBTabBase#getPropiedadesCondiciones
	// y las posibles plantillas para crear xxxTabs.
	public void setConditions(String[] condiciones) {
		this.conditions = condiciones;
	}
	/** Objeto del que se obtiene la conexión. */
	public void setConnectionProvider(IConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}
	/** Nombre de la tabla en la DB. */
	public void setTable(String tabla) {
		this.table = tabla;
	}
	/** Tamaño de los bloques que se envían cuando se llama a {@link #siguienteTrozo}. */
	public void setChunkSize(int tamañoBloque) {
		this.chunkSize = tamañoBloque;
	}
	/**
	 * Crea un <code>ResultSet</code> con los datos del siguiente bloque. <p>
	 *
	 * @param  con  <tt>!= null</tt>
	 */
	private ResultSet nextBlock(Connection con) throws SQLException {
		
		// assert(con)
		
		/* Not in this way because TYPE_SCROLL_INTENSIVE has a very poor performance
		   in some databases
		  PreparedStatement ps =
			con.prepareStatement(
				select,
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		*/
				
		PreparedStatement ps = con.prepareStatement(select); 
		// Llena lo valores de clave
		StringBuffer mensaje =
			new StringBuffer("[JDBCTabProvider.siguienteBloque] ejecutando ");
		mensaje.append(select);
		mensaje.append(" con argumentos ");

		for (int i = 0; i < key.length; i++) {
			ps.setObject(i + 1, key[i]);
			mensaje.append(key[i]);
			if (i < key.length - 1)
				mensaje.append(", ");
		}
		System.out.println(mensaje);
		
		ResultSet rs = ps.executeQuery();
		posicionar(rs);

		return rs;
	}

	// Implementa ITabProvider
	public DataChunk nextChunk() throws RemoteException {		
		if (select == null || eof) { // todavía no se ha llamado a buscar
			return new DataChunk(new Vector(), true, current); // Vacío
		}
		ResultSet resultSet = null;
		Connection con = null;
		try {
			con = connectionProvider.getConnection();
			resultSet = nextBlock(con);
			List data = new ArrayList();
			int f = 0;
			int nc = fields.length;
			while (resultSet.next()) {
				if (++f > chunkSize) {
					current += chunkSize;
					return new DataChunk(data, false, current);
				}
				Object[] fila = new Object[nc];
				for (int i = 0; i < nc; i++) {					
					fila[i] = resultSet.getObject(i + 1);
				}
				data.add(fila);
			}
			// Ya no hay más
			eof = true;
			return new DataChunk(data, true, current);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("JDBCTabProvider: Fallo al ejecutar " + select);
			throw new RemoteException(
				"JDBCTabProvider: Fallo al ejecutar "
					+ select
					+ ", Causa: "
					+ ex.getLocalizedMessage());
		}
		finally {
			try {
				resultSet.close();
			}
			catch (Exception ex) {
			}
			try {
				con.close();
			}
			catch (Exception ex) {
			}
		}
	}
	// Devuelve un array a partir del objeto enviado.
	// Si obj == null return Object[0]
	private Object[] toArray(Object obj) {
		if (obj == null)
			return new Object[0];
		if (obj instanceof Object[]) {
			return (Object[]) obj;
		}
		else {
			Object[] rs = { obj };
			return rs;
		}
	}
	public int getCurrent() {
		return current;
	}

	public void setCurrent(int i) {
		current = i;
	}
	public int getResultSize() throws RemoteException {				
		if (this.selectSize == null) return 0;						
		Connection con = null;
		try {
			con = connectionProvider.getConnection();
			PreparedStatement ps = con.prepareStatement(this.selectSize);			
			for (int i = 0; i < key.length; i++) {
				ps.setObject(i + 1, key[i]);				
			}			
			ResultSet rs = ps.executeQuery();
			rs.next();
			int size = rs.getInt(1);
			rs.close();
			ps.close();
			return size;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException(XavaResources.getString("tab_result_size_error"));
		}
		finally {
			try {
				con.close();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				System.err.println("ADVETENCIA: Ha sido imposible cerrar una conexión");
			}
		}						
	}
	
	private String crearSelectTamaño(String select) {
		if (select == null) return null;
		select = select.toUpperCase();
		int iniCampos = select.indexOf("SELECT") + 6;
		int iniFrom = select.indexOf("FROM");
		int fin = select.indexOf("ORDER BY");
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) ");
		if (fin < 0) sb.append(select.substring(iniFrom));
		else sb.append(select.substring(iniFrom, fin - 1));
		return sb.toString();
	}
	
	public void reset() throws RemoteException {
		current = 0;
		eof = false;
	}


}
