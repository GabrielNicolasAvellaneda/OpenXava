package org.openxava.util;

import java.sql.*;

/**
 * Provee conexiones JDBC. <p>
 *
 * @author  Javier Paniza
 */

public interface IConnectionProvider {

  /**
   * Devuelve una conexión JDBC por defecto. <br>
   * @exception SQLException  Si hay problemas al obtener la conexión.
   */
  Connection getConnection() throws SQLException;
  /**
   * Devuelve una conexión JDBC a partir de un nombre identificativo. <br>
   * @param dataSourceName  Nombre de la fuente de datos de donde obtener la conexión.<br>
   * @exception SQLException  Si hay problemas al obtener la conexión.
   */
  Connection getConnection(String dataSourceName) throws SQLException;
  
	/**
	 * Establece la clave usada para establecer la conexión. <p>
	 *
	 * Ha de llamarse también a {@link #setUsuario}. Aunque no
	 * es preceptivo establecer usuario/clave.<br>
	 */
	void setPassword(String password);
  /**
   * Establece el nombre de la fuente de datos usada
   * cuando se use {@link #getConnection}. <br>
   */
  void setDefaultDataSource(String dataSourceName);
	/**
	 * Establece el usuario usado para establecer la conexión. <p>
	 *
	 * No es obligado llamar a este método.<br>
	 */
  void setUser(String user);
}
