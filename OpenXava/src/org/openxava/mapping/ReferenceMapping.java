package org.openxava.mapping;


import java.util.*;

import org.openxava.component.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;


public class ReferenceMapping implements java.io.Serializable {
	
	private ModelMapping container;
	private String reference;
	private ModelMapping referencedMapping;
	private Map details = new HashMap();
	private String referencedModelName;
	private Collection columns = new ArrayList();

	public void addDetail(ReferenceMappingDetail detail) {
		details.put(detail.getReferencedModelProperty(), detail);
		detail.setContainer(this);
		columns.add(detail.getColumn()); // To keep order 
	}
	
	String getReferencedModelName() throws XavaException {
		if (referencedModelName == null) {
			referencedModelName = getContainer().getMetaModel().getMetaReference(getReference()).getReferencedModelName();
		}
		return referencedModelName;
	}
	
	public String getReferencedTable() throws XavaException {
		return getReferencedMapping().getTable();
	}
	
	/**
	 * Qualified column. <p>
	 */
	public String getColumnForReferencedModelProperty(String property) throws ElementNotFoundException, XavaException {
		Object result = details.get(property);
		if (result == null) {
			throw new ElementNotFoundException("reference_mapping_property_not_found", property, referencedModelName, reference);
		}
		return ((ReferenceMappingDetail) result).getColumn();  
	}
	
	/**
	 * Column not qualified. <p>	 
	 */
	public boolean hasColumnForReferencedModelProperty(String property) {
		return details.containsKey(property);
	}
	
	
	/**
	 * @return Not null.
	 */
	public Collection getDetails() {
		return details.values();
	}
	
	
	ModelMapping getReferencedMapping() throws XavaException {
		if (referencedMapping == null) {
			referencedMapping = MetaComponent.get(getReferencedModelName()).getEntityMapping();
		}
		return referencedMapping;
	}

	public ModelMapping getContainer() {
		return container;
	}
	public void setContainer(ModelMapping container) {
		this.container = container;
	}

	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public Collection getColumns() {
		return columns;
	}
	
	public String getCMPAttribute(String propertyNameOfReferencedModel) throws XavaException {
		if (getContainer().isReferenceOverlappingWithSomeProperty(getReference(), propertyNameOfReferencedModel)) {
			return getContainer().getCMPAttributeForColumn(getColumnForReferencedModelProperty(propertyNameOfReferencedModel));
		}
		return Strings.change(getReference() + "_" + propertyNameOfReferencedModel, ".", "_");
	}

	public Collection getCmpFields() throws XavaException {
		Collection fields = new ArrayList();  
		for (Iterator it=getDetails().iterator(); it.hasNext();) {
			ReferenceMappingDetail d = (ReferenceMappingDetail) it.next();
			CmpField field = new CmpField();
			field.setCmpPropertyName(
					getReference() + "_" + 
					Strings.change(d.getReferencedModelProperty(), ".", "_"));
			String propertyName = 
				Strings.change(getReference(), "_", ".") + "." +
				Strings.change(d.getReferencedModelProperty(), "_", ".");
			MetaProperty property = getContainer().getMetaModel().getMetaProperty(propertyName);
			field.setCmpTypeName(property.getMapping().toCmpField().getCmpTypeName());			
			field.setColumn(d.getColumn());
			fields.add(field);
		}
		return fields;
	}
	
}


