package org.openxava.util;

import java.sql.*;

/**
 * Provee conexiones JDBC. <p>
 *
 * @author  Javier Paniza
 */

public interface IConnectionProvider {

  /**
   * Devuelve una conexi�n JDBC por defecto. <br>
   * @exception SQLException  Si hay problemas al obtener la conexi�n.
   */
  Connection getConnection() throws SQLException;
  /**
   * Devuelve una conexi�n JDBC a partir de un nombre identificativo. <br>
   * @param dataSourceName  Nombre de la fuente de datos de donde obtener la conexi�n.<br>
   * @exception SQLException  Si hay problemas al obtener la conexi�n.
   */
  Connection getConnection(String dataSourceName) throws SQLException;
  
	/**
	 * Establece la clave usada para establecer la conexi�n. <p>
	 *
	 * Ha de llamarse tambi�n a {@link #setUsuario}. Aunque no
	 * es preceptivo establecer usuario/clave.<br>
	 */
	void setPassword(String password);
  /**
   * Establece el nombre de la fuente de datos usada
   * cuando se use {@link #getConnection}. <br>
   */
  void setDefaultDataSource(String dataSourceName);
	/**
	 * Establece el usuario usado para establecer la conexi�n. <p>
	 *
	 * No es obligado llamar a este m�todo.<br>
	 */
  void setUser(String user);
}
