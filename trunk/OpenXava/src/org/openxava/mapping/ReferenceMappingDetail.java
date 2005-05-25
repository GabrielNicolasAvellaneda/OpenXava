package org.openxava.mapping;

import org.openxava.component.*;
import org.openxava.util.*;


public class ReferenceMappingDetail implements java.io.Serializable {
	
	private String column;
	private String referencedModelProperty;
	private ReferenceMapping container;
	private String referencedTableColumn;
	private String converterClassName;
	
	
	public String getColumn() {
		return column;
	}
	public void setColumn(String tableColumn) {
		this.column = tableColumn;
	}

	public String getReferencedModelProperty() {
		return referencedModelProperty;
	}
	public void setReferencedModelProperty(String referencedModelProperty) {
		this.referencedModelProperty = referencedModelProperty;
	}
	
	public String getQualifiedColumnOfReferencedTable() throws XavaException {
		return getContainer().getReferencedTable() + "." + getReferencedTableColumn(); 
	}
	
	public String getReferencedTableColumn() throws XavaException {
		if (referencedTableColumn == null) {
			ReferenceMapping referenceMapping = getContainer();			
			EntityMapping referencedMapping = MetaComponent.get(referenceMapping.getReferencedModelName()).getEntityMapping();
			referencedTableColumn = referencedMapping.getColumn(getReferencedModelProperty());
		}
		return referencedTableColumn;
	}
	
	public String getQualifiedColumn() throws XavaException {
		return getContainer().getContainer().getTable() + "." +  getColumn();  
	}
	
	public ReferenceMapping getContainer() {
		return container;
	}
	public void setContainer(ReferenceMapping contenedor) {
		this.container = contenedor;
	}


	public String getConverterClassName() {
		return converterClassName;
	}
	public void setConverterClassName(String converterClassName) {
		this.converterClassName = converterClassName;
	}
	public boolean hasConverter() {
		return !Is.emptyString(converterClassName);
	}
	
}

