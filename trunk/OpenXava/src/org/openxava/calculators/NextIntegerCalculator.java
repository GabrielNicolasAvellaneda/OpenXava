package org.openxava.calculators;

import java.sql.*;

import org.openxava.component.*;
import org.openxava.mapping.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;


/**
 * @author Javier Paniza
 */
public class NextIntegerCalculator implements IJDBCCalculator {
	

	
		
	private String packageName;
	private IConnectionProvider provider;
	private String model;
	private String property;

	private String select;


	public void setConnectionProvider(IConnectionProvider proveedor) {
		this.provider = proveedor;
	}

	public Object calculate() throws Exception {					
		return new Integer(calculateNextInteger());
	}

	public int calculateNextInteger()
		throws Exception {
		if (XSystem.onClient()) {
			Object r = Server.calculate(this, getPackageName());
			if (r instanceof Number) {
				return ((Number) r).intValue();
			}
			return 0;
		}
		Connection con = provider.getConnection();
		try {
			PreparedStatement ps = con.prepareStatement(getSelect());			
			ResultSet rs = ps.executeQuery();
			int nr = 0;
			if (rs.next()) {
				nr = rs.getInt(1);
			}
			ps.close();
			return nr+1;			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(XavaResources.getString("next_integer_calculator_warning"));
			return 1;
		}
		finally {
			con.close();
		}
	}
	
	private String getPackageName() throws XavaException {		
		if (packageName == null) {
			packageName = MetaComponent.get(getModel()).getPackageName();
		}
		return packageName;
	}

	private String getSelect() throws XavaException {
		if (select == null) {
			if (Is.emptyString(this.model, this.property)) {
				throw new XavaException("next_integer_calculator_required_properties");
			}
			ModelMapping mapping = null;
			try {
				mapping = MetaComponent.get(this.model).getEntityMapping();
			}
			catch (ElementNotFoundException ex) {
				mapping = MetaAggregate.get(this.model).getMapping(); 
			}
			String column = mapping.getColumn(this.property);
			String table = mapping.getTable();
			StringBuffer sb = new StringBuffer("select max(");
			sb.append(column);
			sb.append(") from ");
			sb.append(table);
			select = sb.toString();
		}
		return select;		
	}				


	/**
	 * Returns the modelo.
	 * @return String
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets the modelo.
	 * @param modelo The modelo to set
	 */
	public void setModel(String modelo) {
		this.model = modelo;
		this.select = null;		
	}

	/**
	 * Returns the propiedad.
	 * @return String
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * Sets the propiedad.
	 * @param propiedad The propiedad to set
	 */
	public void setProperty(String propiedad) {
		this.property = propiedad;
		this.select = null;
	}

}
