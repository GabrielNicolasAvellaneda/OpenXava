package org.openxava.model.meta;


import java.util.*;

import org.openxava.calculators.*;
import org.openxava.component.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;



/**
 * 
 * @author: Javier Paniza
 */

public class MetaReference extends MetaMember implements Cloneable {


	private MetaModel metaModelReferenced;
	private String referencedModelName;
	private String role;
	private boolean required;
	private boolean key;
	private MetaCalculator metaCalculatorDefaultValue;
	private ICalculator defaultValueCalculator;
	
	/**
	 * Comentario de constructor Referencia.
	 */
	public MetaReference() {
		super();
	}
	
	public MetaCollection getMetaCollectionFromReferencedModel() throws XavaException { 				
		Iterator it = getMetaModelReferenced().getMetaCollections().iterator();
		String nombreModelo = getMetaModel().getName();
		while (it.hasNext()) {
			MetaCollection metaColeccion = (MetaCollection) it.next();
			if (metaColeccion.getMetaReference().getRole().equals(getName()) &&
				nombreModelo.equals(metaColeccion.getMetaReference().getReferencedModelName())
			) {
				return metaColeccion;
			}  			
		}
		return null;		
	}
	
	public String getOrderFromReferencedModel() throws XavaException {
		MetaCollection metaColeccion = getMetaCollectionFromReferencedModel(); 				
		return metaColeccion==null?null:metaColeccion.getOrder();		
	}
	
	public String getSQLOrderFromReferencedModel() throws XavaException { 
		String orden = getOrderFromReferencedModel();		
		if (orden == null) return null;		
		return getMetaModel().getMapping().changePropertiesByColumns(orden);
	}
	
	public String getEJBQLOrderFromReferencedModel() throws XavaException { 
		String orden = getOrderFromReferencedModel();		
		if (orden == null) return "";		
		return "ORDER BY " + getMetaModel().getMapping().changePropertiesByCMPAttributes(orden);
	}
					
	/**
	 */
	public MetaModel getMetaModelReferenced() throws XavaException {
		if (metaModelReferenced == null) {
			ElementName nombreModelo = new ElementName(getReferencedModelName());			
			if (nombreModelo.isQualified()) { // calificado
				String nombreComponente = nombreModelo.getContainerName();
				String nombreAgregado = nombreModelo.getUnqualifiedName();
				return MetaComponent.get(nombreComponente).getMetaAggregate(nombreAgregado);
			}
			
			// No calificado
			
			try {				
				// Así buscamos el agregados local
				metaModelReferenced = getMetaModel().getMetaComponent().getMetaAggregate(getReferencedModelName());
			}
			catch (ElementNotFoundException ex) {
				// Buscamos la entidad (buscando el componente)
				metaModelReferenced = MetaComponent.get(getReferencedModelName()).getMetaEntity();
			}
		}
		return metaModelReferenced;
	}
	
	/**
	 */
	public boolean isAggregate() throws XavaException {
		return getMetaModelReferenced() instanceof MetaAggregate;
	}
	
		
	/**
	 * @see Elemento#getLabel()
	 */
	public String getLabel() {
		String e = super.getLabel();
		try {
			return Is.emptyString(e)? getMetaModelReferenced().getLabel() : e;
		} catch (XavaException ex) {
			return e;
		}
	}
	
	/**
	 * Gets the nombreModeloReferenciado
	 * @return Returns a String
	 */
	public String getReferencedModelName() {
		if (Is.emptyString(referencedModelName)) {
			referencedModelName = Strings.firstUpper(super.getName());
		}
		return referencedModelName;
	}
	
	/**
	 * Sets the nombreModeloReferenciado
	 * @param nombreModeloReferenciado The nombreModeloReferenciado to set
	 */
	public void setReferencedModelName(String nombreModeloReferenciado) {
		this.referencedModelName = nombreModeloReferenciado;
	}
	
	/**
	 * Gets the requerido
	 * @return Returns a boolean
	 */
	public boolean isRequired() {
		return required;
	}
	/**
	 * Sets the requerido
	 * @param requerido The requerido to set
	 */
	public void setRequired(boolean requerido) {
		this.required = requerido;
	}


	/**
	 * @see Elemento#getName()
	 */
	public String getName() {
		if (Is.emptyString(super.getName())) {
			ElementName n = new ElementName(getReferencedModelName());
			setName(firstLower(n.getUnqualifiedName()));		
		}
		return super.getName();
	}

	/**
	 * Returns the cometidoDestino.
	 * @return String
	 */
	public String getRole() {		
		return Is.emptyString(role)?Strings.firstLower(getMetaModel().getName()):role;
	}

	/**
	 * Sets the cometidoDestino.
	 * @param cometidoDestino The cometidoDestino to set
	 */
	public void setRole(String cometidoDestino) {
		this.role = cometidoDestino;
	}
	
		
	public boolean isKey() {
		return key;
	}

	public void setKey(boolean b) {
		key = b;
	}
	
	public MetaReference cloneMetaReference() throws XavaException {
		try {
			return (MetaReference) super.clone();			
		}
		catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("reference_implements_cloneable"));
		}
	}	
	
	public MetaCalculator getMetaCalculatorDefaultValue() {
		return metaCalculatorDefaultValue;
	}
	public void setMetaCalculatorDefaultValue(
			MetaCalculator metaCalculatorDefaultValue) throws XavaException {
		if (metaCalculatorDefaultValue != null && metaCalculatorDefaultValue.isOnCreate()) {
			throw new XavaException("reference_not_default_values_on_create", getName());
		}
		this.metaCalculatorDefaultValue = metaCalculatorDefaultValue;		
	}
	
	/**
	 * 
	 * @return null si no tiene calculador para valor inicial.
	 */
	public ICalculator getDefaultValueCalculator() throws XavaException {
		if (!hasDefaultValueCalculator()) return null;
		if (defaultValueCalculator == null) {
			defaultValueCalculator = metaCalculatorDefaultValue.createCalculator();
		}
		return defaultValueCalculator;
	}
	
	public boolean hasDefaultValueCalculator() {		
		return metaCalculatorDefaultValue != null;
	}
		
}