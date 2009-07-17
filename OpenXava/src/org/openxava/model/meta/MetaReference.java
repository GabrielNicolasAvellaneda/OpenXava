package org.openxava.model.meta;

import java.util.*;



import org.apache.commons.logging.*;
import org.openxava.calculators.*;
import org.openxava.component.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;

/**
 * 
 * @author: Javier Paniza
 */

public class MetaReference extends MetaMember implements Cloneable {

	private static Log log = LogFactory.getLog(MetaReference.class);
		
	private MetaModel metaModelReferenced;
	private String referencedModelName;
	private String role;
	private boolean required;
	private boolean key;
	private boolean searchKey;
	private MetaCalculator metaCalculatorDefaultValue;
	private ICalculator defaultValueCalculator;
	private boolean explicitAggregate; 
	private boolean aggregate; 
	
	public MetaCollection getMetaCollectionFromReferencedModel() throws XavaException { 				
		Iterator it = getMetaModelReferenced().getMetaCollections().iterator();
		String modelName = getMetaModel().getName();
		while (it.hasNext()) {
			MetaCollection metaCollection = (MetaCollection) it.next();
			if (metaCollection.getMetaReference().getRole().equals(getName()) &&
				modelName.equals(metaCollection.getMetaReference().getReferencedModelName())
			) {
				return metaCollection;
			}  			
		}
		return null;		
	}
	
	public String getOrderFromReferencedModel() throws XavaException {
		MetaCollection metaCollection = getMetaCollectionFromReferencedModel();
		return metaCollection==null?null:metaCollection.getOrder();
	}
	
	private boolean orderHasQualifiedProperties() throws XavaException { 
		MetaCollection metaCollection = getMetaCollectionFromReferencedModel();
		return metaCollection==null?false:metaCollection.orderHasQualifiedProperties();
	}
	
	public String getSQLOrderFromReferencedModel() throws XavaException { 
		String orden = getOrderFromReferencedModel();		
		if (orden == null) return null;		
		return getMetaModel().getMapping().changePropertiesByColumns(orden);
	}
	
	public String getEJBQLOrderFromReferencedModel() throws XavaException { 
		String order = orderHasQualifiedProperties()?null:getOrderFromReferencedModel(); 		
		if (order == null) return getEJBQLOrderByPrimaryKeyFromReferencedModel();
		return "ORDER BY " + getMetaModel().getMapping().changePropertiesByCMPAttributes(order);
	}
	
	private String getEJBQLOrderByPrimaryKeyFromReferencedModel() throws XavaException {
		// reference keys are not included because some problems with jbossql
		Collection cKeys = getMetaModel().getMetaPropertiesKey();
		StringBuffer order = new StringBuffer();
		for (Iterator it = cKeys.iterator(); it.hasNext();) {
			MetaProperty p = (MetaProperty) it.next();
			String type = p.getCMPTypeName();
			// orders with properties of long type does not work in WebSphere 5.1.x
			// at momment only exclude them
			if ("long".equals(type) || "java.lang.Long".equals(type)) continue;
			if (order.length() == 0) order.append("${"); 
			else order.append(", ${");
			order.append(p.getName());
			order.append('}');			
		}
		if (order.length() == 0) return "";
		return "ORDER BY " + getMetaModel().getMapping().changePropertiesByCMPAttributes(order.toString());				
	}
					
	public MetaModel getMetaModelReferenced() throws XavaException {
		if (metaModelReferenced == null) {
			ElementName modelName = new ElementName(getReferencedModelName());			
			if (modelName.isQualified()) { 
				String componentName = modelName.getContainerName();
				String aggregateName = modelName.getUnqualifiedName();
				return MetaComponent.get(componentName).getMetaAggregate(aggregateName);
			}
			
			// Not qualified
			
			try {				
				// look for local aggregate
				metaModelReferenced = getMetaModel().getMetaComponent().getMetaAggregate(getReferencedModelName());
			}
			catch (ElementNotFoundException ex) {
				// look for entity (looking for component)
				metaModelReferenced = MetaComponent.get(getReferencedModelName()).getMetaEntity();
			}
		}
		return metaModelReferenced;
	}
	
	public boolean isAggregate() throws XavaException {
		if (explicitAggregate) return aggregate;
		return getMetaModelReferenced() instanceof MetaAggregate;
	}
	public void setAggregate(boolean aggregate) {
		this.explicitAggregate = true;
		this.aggregate = aggregate;
	}
			
	public String getLabel() {
		String e = super.getLabel();
		try {
			return Is.emptyString(e)? getMetaModelReferenced().getLabel() : e;
		} catch (XavaException ex) {
			return e;
		}
	}
	
	public String getReferencedModelName() {
		if (Is.emptyString(referencedModelName)) {
			referencedModelName = Strings.firstUpper(super.getName());
		}
		return referencedModelName;
	}
	
	public void setReferencedModelName(String referencedModelName) {
		this.referencedModelName = referencedModelName;
	}
	
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getName() {
		if (Is.emptyString(super.getName())) {
			ElementName n = new ElementName(getReferencedModelName());	
			setName(Strings.firstLower(n.getUnqualifiedName()));
		}
		return super.getName();
	}

	public String getRole() {		
		return Is.emptyString(role)?Strings.firstLower(getMetaModel().getName()):role;
	}
	public void setRole(String role) {
		this.role = role;
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
			log.error(ex.getMessage(), ex);
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
	 * @return null if this does not have default value calculator
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
	
	public String toString() {		
		return "MetaReference:" + getId();
	}

	public boolean isSearchKey() {
		return searchKey;
	}

	public void setSearchKey(boolean searchKey) {
		this.searchKey = searchKey;
	}
		
}