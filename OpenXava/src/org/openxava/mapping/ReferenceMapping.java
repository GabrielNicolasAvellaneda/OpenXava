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

	public void addDetail(ReferenceMappingDetail detalle) {
		details.put(detalle.getReferencedModelProperty(), detalle);
		detalle.setContainer(this);
		columns.add(detalle.getColumn()); // para conservar el orden. 
	}
	
	/**
	 * Gets the nombreModeloReferenciado
	 * @return Returns a String
	 */
	String getReferencedModelName() throws XavaException {
		if (referencedModelName == null) {
			referencedModelName = getContainer().getMetaModel().getMetaReference(getReference()).getReferencedModelName();
		}
		return referencedModelName;
	}
	
	/**
	 */
	public String getReferencedTable() throws XavaException {
		return getReferencedMapping().getTable();
	}
	
	/**
	 * Columna cualificada. <p>
	 * 
	 * @param propiedad
	 * @return String
	 * @throws ElementNotFoundException
	 * @throws XavaException	 
	 */
	public String getColumnForReferencedModelProperty(String propiedad) throws XavaException {
		Object result = details.get(propiedad);
		if (result == null) {
			throw new ElementNotFoundException("reference_mapping_property_not_found", propiedad, referencedModelName, reference);
		}
		return ((ReferenceMappingDetail) result).getColumn();  
	}
	
	/**
	 * Columna no cualificada. <p>
	 * 
	 * @param propiedad	 
	 */
	public boolean hasColumnForReferencedModelProperty(String propiedad) {
		return details.containsKey(propiedad);
	}
	
	
	/**
	 * @return nunca nulo.
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


	/**
	 * Gets the contenedor
	 * @return Returns a MapeoEntidad
	 */
	public ModelMapping getContainer() {
		return container;
	}
	/**
	 * Sets the contenedor
	 * @param contenedor The contenedor to set
	 */
	public void setContainer(ModelMapping contenedor) {
		this.container = contenedor;
	}


	/**
	 * Gets the referenciaModelo
	 * @return Returns a String
	 */
	public String getReference() {
		return reference;
	}
	/**
	 * Sets the referenciaModelo
	 * @param referenciaModelo The referenciaModelo to set
	 */
	public void setReference(String referenciaModelo) {
		this.reference = referenciaModelo;
	}
	
	public Collection getColumns() {
		return columns;
	}
	
	public String getCMPAttribute(String nombrePropiedadModeloReferenciado) throws XavaException {
		if (getContainer().isReferenceOverlappingWithSomeProperty(getReference(), nombrePropiedadModeloReferenciado)) {
			return getContainer().getCMPAttributeForColumn(getColumnForReferencedModelProperty(nombrePropiedadModeloReferenciado));
		}
		return Strings.change(getReference() + "_" + nombrePropiedadModeloReferenciado, ".", "_");
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


