package org.openxava.model.meta;

import java.io.*;
import java.util.*;

import org.openxava.util.*;

/**
 * @author Javier Paniza
 */
public class MetaFinder implements Serializable {
	
	private static Map argumentosJBoss11AEJBQL;
	 	
	private String name;
	private String arguments;	
	private boolean collection;
	private String condition;
	private String order;
	private MetaModel metaModel;
	
	/**
	 * Returns the argumentos.
	 * @return String
	 */
	public String getArguments() {
		arguments = Strings.change(arguments, "String", "java.lang.String");
		arguments = Strings.change(arguments, "java.lang.java.lang.String", "java.lang.String");
		return arguments;
	}

	/**
	 * Returns the coleccion.
	 * @return boolean
	 */
	public boolean isCollection() {
		return collection;
	}

	/**
	 * Returns the condicion.
	 * @return String
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * Returns the nombre.
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the argumentos.
	 * @param argumentos The argumentos to set
	 */
	public void setArguments(String argumentos) {
		this.arguments = argumentos;
	}

	/**
	 * Sets the coleccion.
	 * @param coleccion The coleccion to set
	 */
	public void setCollection(boolean coleccion) {
		this.collection = coleccion;
	}

	/**
	 * Sets the condicion.
	 * @param condicion The condicion to set
	 */
	public void setCondition(String condicion) {
		this.condition = condicion;
	}

	/**
	 * Sets the nombre.
	 * @param nombre The nombre to set
	 */
	public void setName(String nombre) {
		this.name = nombre;
	}
	
	public String getSQLCondition() throws XavaException {		
		if (Is.emptyString(this.condition)) return "1=1";
		return getMetaModel().getMapping().changePropertiesByColumns(this.condition);
	}
	
	public String getEJBQLCondition() throws XavaException {
		StringBuffer sb = new StringBuffer("SELECT OBJECT(o) FROM ");
		sb.append(getMetaModel().getName());
		sb.append(" o");
		if (!Is.emptyString(this.condition)) {
			sb.append(" WHERE ");			
			String condicionAtributos = getMetaModel().getMapping().changePropertiesByCMPAttributes(this.condition);			 
			sb.append(Strings.change(condicionAtributos, getArgumentosJBoss11AEJBQL()));
		}
		return sb.toString();
	}
	
	
	public String getJBossQLCondition() throws XavaException {
		if (Is.emptyString(this.order)) return getEJBQLCondition();
		StringBuffer sb = new StringBuffer(getEJBQLCondition());
		sb.append(" ORDER BY ");
		sb.append(getMetaModel().getMapping().changePropertiesByCMPAttributes(this.order));
		return sb.toString();
	}

	/**
	 * Returns the metaEntidad.
	 * @return MetaEntidad
	 */
	public MetaModel getMetaModel() {
		return metaModel;
	}

	/**
	 * Sets the metaEntidad.
	 * @param metaEntidad The metaEntidad to set
	 */
	public void setMetaModel(MetaModel metaEntidad) {
		this.metaModel = metaEntidad;
	}

	/**
	 * Returns the orden.
	 * @return String
	 */
	public String getOrder() {
		return order;
	}
	
	public String getSQLOrder() throws XavaException {
		if (Is.emptyString(this.order)) return "";
		return getMetaModel().getMapping().changePropertiesByColumns(this.order);
	}

	/**
	 * Sets the orden.
	 * @param orden The orden to set
	 */
	public void setOrder(String orden) {
		this.order = orden;
	}
	
	private static Map getArgumentosJBoss11AEJBQL() {
		if (argumentosJBoss11AEJBQL == null) {
			argumentosJBoss11AEJBQL = new HashMap();
			for (int i=0; i<30; i++) {
				argumentosJBoss11AEJBQL.put("{" + i+ "}", "?" + (i+1));
			}			
		}
		return argumentosJBoss11AEJBQL;
	}

}
