package org.openxava.mapping;


import java.util.*;

import org.openxava.component.*;
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
		columns.add(detalle.getColumn()); // para conservar el orden. A lo mejor con JDO2 ya no hace falta
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
		/* A lo mejor con jdo2 se peude volver a hacer así.
		Collection columnas = new ArrayList();
		Iterator it = getDetalles().iterator();
		while (it.hasNext()) {
			DetalleMapeoReferencia d = (DetalleMapeoReferencia) it.next();
			columnas.add(d.getColumnaTabla());
		}
		return columnas;
		*/
		return columns;
	}
	
	public String getCMPAttribute(String nombrePropiedadModeloReferenciado) throws XavaException {
		if (getContainer().isReferenceOverlappingWithSomeProperty(getReference(), nombrePropiedadModeloReferenciado)) {
			return getContainer().getCMPAttributeForColumn(getColumnForReferencedModelProperty(nombrePropiedadModeloReferenciado));
		}
		return Strings.change(getReference() + "_" + nombrePropiedadModeloReferenciado, ".", "_");
	}
	
}


