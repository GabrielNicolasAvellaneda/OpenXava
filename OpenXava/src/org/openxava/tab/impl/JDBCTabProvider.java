package org.openxava.tab.impl;

import java.rmi.*;
import java.sql.*;
import java.util.*;

import org.apache.commons.logging.*;
import org.openxava.util.*;

/**
 * An <code>ITabProvider</code> that obtain data via JDBC. <p>
 *
 * @author  Javier Paniza
 */

public class JDBCTabProvider extends TabProviderBase {
	
	private static Log log = LogFactory.getLog(JDBCTabProvider.class);

	protected String translateCondition(String condition) { 
		return getMetaModel().getMapping().changePropertiesByColumns(condition); // tmp ¿Mover changePropertiesByColumns() aquí?
	}
	
	public String toQueryField(String propertyName) {		
		return getMetaModel().getMapping().getQualifiedColumn(propertyName);
	}

	public String translateSelect(String select) {
		return getMetaModel().getMapping().changePropertiesByColumns(select); // tmp ¿Mover changePropertiesByColumns() aquí?	
	}
	
	public DataChunk nextChunk() throws RemoteException {		
		if (getSelect() == null || isEOF()) { // search not called yet
			return new DataChunk(Collections.EMPTY_LIST, true, getCurrent()); // Empty
		}
		ResultSet resultSet = null;
		Connection con = null;
		PreparedStatement ps = null;
		try {
			con = getConnectionProvider().getConnection();
			ps = createPreparedStatement(con);
			resultSet = nextBlock(ps);
			List data = new ArrayList();
			int f = 0;
			int nc = resultSet.getMetaData().getColumnCount();
			while (resultSet != null && resultSet.next()) {
				if (++f > getChunkSize()) {
					setCurrent(getCurrent() + getChunkSize());
					return new DataChunk(data, false, getCurrent());
				}
				Object[] row = new Object[nc];
				for (int i = 0; i < nc; i++) {					
					row[i] = resultSet.getObject(i + 1);
				}
				data.add(row);
			}
			// No more
			setEOF(true);
			return new DataChunk(data, true, getCurrent());
		}
		catch (Exception ex) {
			log.error(XavaResources.getString("select_error", getSelect()), ex);
			throw new RemoteException(XavaResources.getString("select_error", getSelect()));
		}
		finally {
			try {
				if (resultSet != null) resultSet.close();
				if (ps != null) {
					ps.close();
					ps = null;
				}
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
		
	/**
	 * Creates a <code>ResultSet</code> with the next block data. <p>
	 */
	private ResultSet nextBlock(PreparedStatement ps) throws SQLException {
		if (ps == null) return null;

		// Fill key values
		StringBuffer message =
			new StringBuffer("[JDBCTabProvider.nextBlock] ");
		message.append(XavaResources.getString("executing_select", getSelect()));		
		
		Object [] key = getKey();
		for (int i = 0; i < key.length; i++) {
			ps.setObject(i + 1, key[i]);
			message.append(key[i]);
			if (i < key.length - 1)
				message.append(", ");
		}
		log.debug(message);
		
		if ((getCurrent() + getChunkSize()) < Integer.MAX_VALUE) { 
			ps.setMaxRows(getCurrent() + getChunkSize() + 1); 
		}		
		ResultSet rs = ps.executeQuery();						
		position(rs);

		return rs;
	}

	private PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		/* Not in this way because TYPE_SCROLL_INTENSIVE has a very poor performance
		   in some databases
		  PreparedStatement ps =
			con.prepareStatement(
				select,
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		*/
		
		if (keyHasNulls()) return null; // Because some databases (like Informix) have problems setting nulls
				
		return con.prepareStatement(getSelect());
	}
	
	/**
	 * Position the <code>ResultSet</code> in the appropiate part. <p>
	 *
	 * @param rs  <tt>!= null</tt>
	 */
	private void position(ResultSet rs) throws SQLException { 
		//rs.absolute(current); // this only run with TYPE_SCROLL_INSENSITIVE, and this is very slow on executing query in some databases
		for (int i = 0; i < getCurrent(); i++) {
			if (!rs.next())
				return;
		}
	}


	protected Number executeNumberSelect(String select, String errorId) {
		if (select == null || keyHasNulls()) return 0;						
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			con = getConnectionProvider().getConnection();
			ps = con.prepareStatement(select);
			Object [] key = getKey();
			for (int i = 0; i < key.length; i++) {
				ps.setObject(i + 1, key[i]);				
			}			
			rs = ps.executeQuery();
			rs.next();
			return (Number) rs.getObject(1);
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new XavaException(errorId);
		}
		finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (Exception ex) {
				log.error(XavaResources.getString("close_resultset_warning"), ex);
			}			
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (Exception ex) {
				log.error(XavaResources.getString("close_statement_warning"), ex);
			}
			try {
				con.close();
			}
			catch (Exception ex) {
				log.error(XavaResources.getString("close_connection_warning"), ex);
			}
		}						
	}
	
	private IConnectionProvider getConnectionProvider() {
		return DataSourceConnectionProvider.getByComponent(getMetaModel().getMetaComponent().getName()); 
	}

}
