package org.openxava.mapping;

import org.openxava.component.*;
import org.openxava.util.*;


public class ReferenceMappingDetail implements java.io.Serializable {
	
	private String column;
	private String referencedModelProperty;
	private ReferenceMapping container;
	private String referencedTableColumn;
	
	/**
	 * Gets the columnaTabla
	 * @return Returns a String
	 */
	public String getColumn() {
		return column;
	}
	/**
	 * Sets the columnaTabla
	 * @param columnaTabla The columnaTabla to set
	 */
	public void setColumn(String columnaTabla) {
		this.column = columnaTabla;
	}


	/**
	 * Gets the propiedadModeloReferenciado
	 * @return Returns a String
	 */
	public String getReferencedModelProperty() {
		return referencedModelProperty;
	}
	/**
	 * Sets the propiedadModeloReferenciado
	 * @param propiedadModeloReferenciado The propiedadModeloReferenciado to set
	 */
	public void setReferencedModelProperty(String propiedadModeloReferenciado) {
		this.referencedModelProperty = propiedadModeloReferenciado;
	}
	
	public String getColumnaTablaReferenciadaCualificada() throws XavaException {
		return getContainer().getReferencedTable() + "." + getReferencedTableColumn(); 
	}
	
	public String getReferencedTableColumn() throws XavaException {
		if (referencedTableColumn == null) {
			ReferenceMapping mapeoReferencia = getContainer();			
			EntityMapping mapeoReferenciado = MetaComponent.get(mapeoReferencia.getReferencedModelName()).getEntityMapping();
			referencedTableColumn = mapeoReferenciado.getColumn(getReferencedModelProperty());
		}
		return referencedTableColumn;
	}
	
	public String getQualifiedColumn() throws XavaException {
		return getContainer().getContainer().getTable() + "." +  getColumn();  
	}
	
	/**
	 * Gets the contenedor
	 * @return Returns a MapeoReferencia
	 */
	public ReferenceMapping getContainer() {
		return container;
	}
	/**
	 * Sets the contenedor
	 * @param contenedor The contenedor to set
	 */
	public void setContainer(ReferenceMapping contenedor) {
		this.container = contenedor;
	}


}

