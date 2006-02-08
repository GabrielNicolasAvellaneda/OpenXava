package org.openxava.model.meta;


import java.beans.*;
import java.util.*;

import org.openxava.component.*;
import org.openxava.mapping.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;
import org.openxava.validators.meta.*;
import org.openxava.view.meta.*;

/**
 *
 * 
 * @author Javier Paniza
 */
abstract public class MetaModel extends MetaElement implements IMetaModel {

	private static boolean someModelHasDefaultCalculatorOnCreateInNotKey = false;
	private static boolean someModelHasPostCreateCalculator = false;
	private static boolean someModelHasPostModifyCalculator = false;
	private Class pojoClass;
	private Collection allKeyPropertiesNames;
	private List metaCalculatorsPostCreate;
	private List metaCalculatorsPostLoad;
	private List metaCalculatorsPostModify;
	private List metaCalculatorsPreRemove;
	private List propertiesNamesWithoutHidden;
	private String containerModelName;
	private MetaModel metaModelContainer;
	private Map mapMetaPropertiesView;
	private Collection metaPropertiesViewWithDefaultCalculator;
	private Collection metaValidators;
	private Collection metaValidatorsRemove;
	private MetaComponent metaComponent;
	private transient Map propertyDescriptors;
	private Map mapMetaProperties;
	private Map mapMetaReferences;
	private Map mapMetaColections;
	private Map mapMetaViews;
	private Map mapMetaMethods;
	private Collection membersNames = new ArrayList();
	private Collection calculatedPropertiesNames;
	private MetaView metaViewByDefault;
	private boolean generate;
	
	
	private Collection metaPropertiesWithDefaultValueCalculator;
	private List propertiesNames;
	private Collection metaPropertiesWithDefaultValueCalcultaorOnCreate;
	
	private Collection metaFinders;

	private Collection metaPropertiesPersistents;

	private Collection persistentPropertiesNames;
	private Collection interfaces;
	private Collection recursiveQualifiedPropertiesNames;
	private Collection metaReferencesWithDefaultValueCalculator;
	private String qualifiedName;
	private boolean hasDefaultCalculatorOnCreateInNotKey = false;
	
	/**
	 * All models (Entities and Aggregates) with a mapping associated.
	 * @return of type MetaModel
	 * @throws XavaException
	 */
	public static Collection getAllPersistent() throws XavaException {  
		Collection r = new HashSet();
		for (Iterator it = MetaComponent.getAllLoaded().iterator(); it.hasNext();) {
			MetaComponent comp = (MetaComponent) it.next();
			r.add(comp.getMetaEntity());						
			for (Iterator itAggregates = comp.getMetaAggregates().iterator(); itAggregates.hasNext();) {
				MetaAggregate ag = (MetaAggregate) itAggregates.next();
				if (ag instanceof MetaAggregateEjb) {
					r.add(ag);
				}
			}
		}		
		return r;
	}
	
	/**
	 * All models (Entities and Aggregates) where its java code is generated.
	 * @return of type MetaModel
	 * @throws XavaException
	 */
	public static Collection getAllGenerated() throws XavaException { 
		Collection r = new HashSet();
		for (Iterator it = MetaComponent.getAll().iterator(); it.hasNext();) {
			MetaComponent comp = (MetaComponent) it.next();
			MetaEntity en = comp.getMetaEntity();
			if (en.isGenerate()) { // At momment, pojo and hibernate will be treated
				r.add(en);
			}
									
			for (Iterator itAggregates = comp.getMetaAggregates().iterator(); itAggregates.hasNext();) {
				MetaAggregate ag = (MetaAggregate) itAggregates.next();
				if (ag instanceof MetaAggregateEjb) { // At momment, pojo and hibernate will be treated
					if (ag.isGenerate()) {
						r.add(ag);
					}
				}
			}
		}		
		return r;
	}
	
	public void addMetaFinder(MetaFinder metaFinder) {
		if (metaFinders == null) metaFinders = new ArrayList();
		metaFinders.add(metaFinder);		
		metaFinder.setMetaModel(this);
	}
	
	public void addMetaMethod(MetaMethod metaMethod) {		
		if (mapMetaMethods == null) mapMetaMethods = new HashMap();
		mapMetaMethods.put(metaMethod.getName(), metaMethod);
	}
	
	public void addMetaCalculatorPostCreate(MetaCalculator metaCalculator) {		
		if (metaCalculatorsPostCreate == null) metaCalculatorsPostCreate = new ArrayList();		
		metaCalculatorsPostCreate.add(metaCalculator);
		someModelHasPostCreateCalculator = true;
	}
	
	public void addMetaCalculatorPostLoad(MetaCalculator metaCalculator) {		
		if (metaCalculatorsPostLoad == null) metaCalculatorsPostLoad = new ArrayList();		
		metaCalculatorsPostLoad.add(metaCalculator);
	}
		
	public void addMetaCalculatorPostModify(MetaCalculator metaCalculator) {		
		if (metaCalculatorsPostModify == null) metaCalculatorsPostModify = new ArrayList();		
		metaCalculatorsPostModify.add(metaCalculator);
		someModelHasPostModifyCalculator = true;
	}
	
	public void addMetaCalculatorPreRemove(MetaCalculator metaCalculator) {		
		if (metaCalculatorsPreRemove == null) metaCalculatorsPreRemove = new ArrayList();		
		metaCalculatorsPreRemove.add(metaCalculator);
	}
				
	public void addMetaValidator(MetaValidator metaValidator) {
		if (metaValidators == null) metaValidators = new ArrayList();
		metaValidators.add(metaValidator);				
	}
	
	public void addMetaValidatorRemove(MetaValidator metaValidator) {
		if (metaValidatorsRemove == null) metaValidatorsRemove = new ArrayList();
		metaValidatorsRemove.add(metaValidator);				
	}	
	
	abstract public String getClassName() throws XavaException;
	
	/**
	 * If entity the name of component, if aggregate the name of component + the name of
	 * aggregate. <p>
	 */
	public String getQualifiedName() {
		return qualifiedName;
	}
	/**
	 * If entity the name of component, if aggregate the name of component + the name of
	 * aggregate. <p>
	 */	
	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}

		
	/**	 
	 * @return Collection of MetaFinder. Not null
	 */
	public Collection getMetaFinders() {
		return metaFinders==null?Collections.EMPTY_LIST:metaFinders;
	}
	
	/**
	 * @return Collection of MetaMethod. Not null
	 */
	public Collection getMetaMethods() {
		return mapMetaMethods==null?Collections.EMPTY_LIST:mapMetaMethods.values();
	}
	
	/**
	 * 
	 * @param newMetaProperty  not null
	 */
	public void addMetaProperty(MetaProperty newMetaProperty) {
		getMapMetaProperties().put(newMetaProperty.getName(), newMetaProperty);
		membersNames.add(newMetaProperty.getName());
		newMetaProperty.setMetaModel(this);
		propertiesNames = null;
		recursiveQualifiedPropertiesNames = null;
		if (newMetaProperty.hasCalculatorDefaultValueOnCreate() && !newMetaProperty.isKey()) {
			someModelHasDefaultCalculatorOnCreateInNotKey = true;
			hasDefaultCalculatorOnCreateInNotKey = true;
		}
	}
	
	public boolean hasDefaultCalculatorOnCreateInNotKey() {
		return hasDefaultCalculatorOnCreateInNotKey ;
	}
	
	/**
	 * 
	 * @param newMetaReference  not null
	 */
	public void addMetaReference(MetaReference newMetaReference) {
		getMapMetaReferences().put(newMetaReference.getName(), newMetaReference);
		membersNames.add(newMetaReference.getName());
		newMetaReference.setMetaModel(this);		
	}
	
	public void addMetaView(MetaView newMetaView) throws XavaException {		
		getMapMetaViews().put(newMetaView.getName(), newMetaView);		
		newMetaView.setModelName(this.getName());
		newMetaView.setMetaModel(this);		
		if (Is.emptyString(newMetaView.getName())) {
			if (this.metaViewByDefault != null) {
				throw new XavaException("no_more_1_default_view", getName());
			}
			this.metaViewByDefault = newMetaView;
		}						
	}	
	
	/**
	 * 
	 * @param newMetaCollection  not null
	 */	
	public void addMetaCollection(MetaCollection newMetaCollection) {
		getMapMetaColections().put(newMetaCollection.getName(), newMetaCollection);
		membersNames.add(newMetaCollection.getName());
		newMetaCollection.setMetaModel(this);
	}
	
	public boolean containsMetaProperty(String property) {
		return getMapMetaProperties().containsKey(property);
	}

	public boolean containsMetaPropertyView(String property) {		
		return getMapMetaPropertiesView().containsKey(property);
	}	
	
	public boolean containsMetaReference(String reference) {
		return getMapMetaReferences().containsKey(reference);
	}
	
	public boolean containsMetaCollection(String collection) {
		return getMapMetaColections().containsKey(collection);
	}
	
	/**
	 * Class that contains the properties defined in this model. <p>
	 * 
	 * @return Not null
	 */
	public Class getPropertiesClass() throws XavaException {
		try {
			return Class.forName(getInterfaceName());
		} 
		catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			throw new XavaException("no_class_for_model", getInterfaceName(), getName());
		}
	}
	
	public MetaMember getMetaMember(String name) throws ElementNotFoundException, XavaException {
		try {
			return getMetaProperty(name);
		} catch (ElementNotFoundException ex) {
			try {
				return getMetaReference(name);
			}
			catch (ElementNotFoundException ex2) {
				try {
					return getMetaCollection(name);
				}
				catch (ElementNotFoundException ex3) {
					throw new ElementNotFoundException("member_not_found", name, getName());
				}
			}			
		}
	}
	
	PropertyDescriptor getPropertyDescriptor(String propertyName)
		throws XavaException {
		PropertyDescriptor pd =
			(PropertyDescriptor) getPropertyDescriptors().get(propertyName);
		if (pd == null) {
			throw new ElementNotFoundException("property_not_found", propertyName, getPropertiesClass().getName());
		}
		return pd;
	}
					
	/**
	 * Of the properties.
	 */
	private Map getPropertyDescriptors() throws XavaException {
		if (propertyDescriptors == null) {
			try {
				BeanInfo info = Introspector.getBeanInfo(getPropertiesClass());
				PropertyDescriptor[] pds = info.getPropertyDescriptors();
				propertyDescriptors = new HashMap();
				for (int i = 0; i < pds.length; i++) {
					propertyDescriptors.put(pds[i].getName(), pds[i]);
				}
			} 
			catch (Exception ex) {
				ex.printStackTrace();
				throw new XavaException("property_descriptors_error", getPropertiesClass());
			}
		}
		return propertyDescriptors;
	}
	
	/**
	 * Support qualified for properties of references with dot (.). <p>
	 */
	public MetaProperty getMetaProperty(String name) throws ElementNotFoundException, XavaException {
		MetaProperty r = (MetaProperty) getMapMetaProperties().get(name);		
		if (r == null) {
			int idx = name.indexOf('.');
			if (idx >= 0) {				
				String referenceName = name.substring(0, idx);								
				String propertyName = name.substring(idx + 1);				
				return getMetaReference(referenceName).getMetaModelReferenced().getMetaProperty(propertyName);
			}
			throw new ElementNotFoundException("property_not_found", name, getName());
		}
		return r;
	}
	
	public MetaProperty getMetaPropertyView(String name) throws ElementNotFoundException, XavaException {
		if (!containsMetaPropertyView(name)) {
			throw new ElementNotFoundException("property_not_found_in_any_view", name, getName());
		}
		return (MetaProperty) getMapMetaPropertiesView().get(name);
	}
		
	private Map getMapMetaProperties() {
		if (mapMetaProperties == null) {
			mapMetaProperties = new HashMap();
		}
		return mapMetaProperties;
	}
	
	private Map getMapMetaViews() {
		if (mapMetaViews == null) {
			mapMetaViews = new HashMap();
		}
		return mapMetaViews;
	}
	
	public MetaReference getMetaReference(String name) throws ElementNotFoundException, XavaException {
		if (name == null) {
			throw new ElementNotFoundException("reference_not_found", name, getName());
		}
		MetaReference r = (MetaReference) getMapMetaReferences().get(name);
		if (r == null) {
			name = Strings.change(name, "_", ".");
			int idx = name.indexOf('.');
			if (idx >= 0) {
				String aggregate = name.substring(0, idx);			
				String nestedReference = name.substring(idx + 1);
				return getMetaReference(aggregate).getMetaModelReferenced().getMetaReference(nestedReference);
			}
			else {
				throw new ElementNotFoundException("reference_not_found", name, getName());
			}
		}
		return r;
	}
		
	public MetaMethod getMetaMethod(String name) throws ElementNotFoundException, XavaException {
		if (mapMetaMethods == null) {
			throw new ElementNotFoundException("method_not_found", name, getName());
		}
		MetaMethod m = (MetaMethod) mapMetaMethods.get(name);
		if (m == null) {
			throw new ElementNotFoundException("method_not_found", name, getName());
		}
		return m;
	}
	
	
	private Map getMapMetaReferences() {
		if (mapMetaReferences == null) {
			mapMetaReferences = new HashMap();
		}
		return mapMetaReferences;
	}
		
	public MetaCollection getMetaCollection(String name) throws XavaException {
		MetaCollection r = (MetaCollection) getMapMetaColections().get(name);
		if (r == null) {
			throw new ElementNotFoundException("collection_not_found", name, getName());
		}
		return r;
	}
	
	public MetaView getMetaView(String name) throws XavaException {		
		MetaView r = (MetaView) getMapMetaViews().get(name == null?"":name);
		if (r == null) {
			throw new ElementNotFoundException("view_not_found_in_model", name, getName());
		}
		return r;		

	}	
		
	private Map getMapMetaColections() {
		if (mapMetaColections == null) {
			mapMetaColections = new HashMap();
		}
		return mapMetaColections;
	}
	
	
	/**
	 * Order like xml file.
	 * @return Not null, read only and serializable
	 */
	public Collection getMembersNames() {
		return Collections.unmodifiableCollection(membersNames);
		// It is not obtained from map to keep order
	}

	/**
	 * Order like xml files.
	 * @return Not null, read only and serializable
	 */
	public List getPropertiesNames() {
		// We obtain it from memberNames to keep order 
		if (propertiesNames == null) {
			List result = new ArrayList();
			Iterator it = getMembersNames().iterator();
			while (it.hasNext()) {
				Object name = it.next();
				if (getMapMetaProperties().containsKey(name)) {
					result.add(name);
				}
			}		
			propertiesNames = Collections.unmodifiableList(result);
		}
		return propertiesNames;
	}

	/**
	 * @return Not null, read only and serializable
	 */
	public Collection getReferencesNames() {
		// We wrap it inside array for make it serializable		
		return Collections.unmodifiableCollection(new ArrayList(getMapMetaReferences().keySet()));
	}
	
	/**
	 * @return Not null, read only and serializable
	 */
	public Collection getColectionsNames() {
		// We wrap it inside array for make it serializable		
		return Collections.unmodifiableCollection(new ArrayList(getMapMetaColections().keySet()));
	}

	public Collection getEntityReferencesNames() throws XavaException {
		Iterator it = getMapMetaReferences().values().iterator();
		ArrayList result = new ArrayList();
		while (it.hasNext()) {
			MetaReference r = (MetaReference) it.next();
			if (!r.isAggregate()) {
				result.add(r.getName());
			}
		}
		return result;
	}
	
	public Collection getAggregateReferencesNames() throws XavaException {
		Iterator it = getMapMetaReferences().values().iterator();
		ArrayList result = new ArrayList();
		while (it.hasNext()) {
			MetaReference r = (MetaReference) it.next();
			if (r.isAggregate()) {
				result.add(r.getName());
			}
		}
		return result;
	}
	
	
	/**
	 * @return Collection of <tt>MetaCollection</tt>, not null and read only
	 */
	public Collection getMetaCollectionsAgregate() throws XavaException {
		Iterator it = getMapMetaColections().values().iterator();
		ArrayList result = new ArrayList();
		while (it.hasNext()) {
			MetaCollection c = (MetaCollection) it.next();
			if (c.getMetaReference().isAggregate()) {
				result.add(c);
			}
		}
		return Collections.unmodifiableCollection(result);
	}
	
	/**
	 * @return Collection of <tt>String</tt>, not null and read only
	 */
	public Collection getRequiredPropertiesNames() throws XavaException {
		Iterator it = getMetaProperties().iterator();
		ArrayList result = new ArrayList();
		while (it.hasNext()) {
			MetaProperty p = (MetaProperty) it.next();
			if (p.isRequired()) {
				result.add(p.getName());
			}
		}
		return Collections.unmodifiableCollection(result);
	}
	
	/**
	 * @return Collection of <tt>String</tt>, not null and read only
	 */
	public Collection getRequiredMemberNames() throws XavaException {
		Iterator it = getMembersNames().iterator();
		ArrayList result = new ArrayList();
		while (it.hasNext()) {
			String nombre = (String) it.next();
			if (containsMetaProperty(nombre)) {
				MetaProperty p = getMetaProperty(nombre);
				if (p.isRequired()) { 
					result.add(p.getName());
				}				
			}
			else if (containsMetaReference(nombre)){
				MetaReference ref = getMetaReference(nombre);
				if (ref.isRequired()) {
					result.add(ref.getName());
				}				
			}
		}
		return Collections.unmodifiableCollection(result);
	}
	
	
	/**
	 * @return Collection of <tt>String</tt>, not null and read only 
	 */
	public Collection getKeyPropertiesNames() throws XavaException {
		Iterator it = getMetaProperties().iterator();
		ArrayList result = new ArrayList();
		while (it.hasNext()) {
			MetaProperty p = (MetaProperty) it.next();
			if (p.isKey()) {
				result.add(p.getName());
			}
		}
		return Collections.unmodifiableCollection(result);
	}
	
	/**
	 * @return Collection of <tt>String</tt>, not null and read only 
	 */
	public Collection getKeyReferencesNames() throws XavaException {
		Iterator it = getMetaReferencesKey().iterator();
		ArrayList result = new ArrayList();
		while (it.hasNext()) {
			MetaReference ref = (MetaReference) it.next();
			result.add(ref.getName());
		}
		return Collections.unmodifiableCollection(result);
	}
	
	
	/**
	 * Includes qualified properties in case of key references. <p>
	 * 
	 * @return Collection of <tt>String</tt>, not null and read only 
	 */
	public Collection getAllKeyPropertiesNames() throws XavaException {  
		if (allKeyPropertiesNames==null) {
			ArrayList result = new ArrayList(getKeyPropertiesNames());
			Iterator itRef = getMetaReferencesKey().iterator(); 
			while (itRef.hasNext()) {
				MetaReference ref = (MetaReference) itRef.next();
				Iterator itProperties = ref.getMetaModelReferenced().getAllKeyPropertiesNames().iterator();
				while (itProperties.hasNext()) {
					result.add(ref.getName() + "." + itProperties.next());
				}
			}		
			allKeyPropertiesNames = Collections.unmodifiableCollection(result);
		}
		return allKeyPropertiesNames;
	}
	
	
	/**
	 * Order like xml file.
	 * 
	 * @return Collection of <tt>String</tt>, not null and read only
	 */
	public List getPropertiesNamesWithoutHidden() throws XavaException {
		// We get it from memberNames to keep order
		if (propertiesNamesWithoutHidden == null) {
			List result = new ArrayList();
			Iterator it = getMembersNames().iterator();
			while (it.hasNext()) {
				Object name = it.next();				
				MetaProperty p = (MetaProperty) getMapMetaProperties().get(name);
				if (p != null && !p.isHidden()) {
					result.add(name);  
				}									
			}		
			propertiesNamesWithoutHidden = Collections.unmodifiableList(result);
		}
		return propertiesNamesWithoutHidden;
	}
	
	
	/**
	 * @return Collection of <tt>MetaProperty</tt>, not null and read only
	 */
	public Collection getMetaPropertiesKey() throws XavaException {
		Iterator it = getMembersNames().iterator(); // memberNames to keep order		
		ArrayList result = new ArrayList();
		while (it.hasNext()) {
			String name = (String) it.next();
			if (!containsMetaProperty(name)) continue;			
			MetaProperty p = (MetaProperty) getMetaProperty(name);
			if (p.isKey()) {
				result.add(p);
			}
		}
		return Collections.unmodifiableCollection(result);
	}
	
	/**
	 * @return Collection of <tt>MetaMember</tt>, not null and read only
	 */
	public Collection getMetaMembersKey() throws XavaException {
		Iterator it = getMembersNames().iterator(); // memberNames to keep order		
		ArrayList result = new ArrayList();
		while (it.hasNext()) {
			String name = (String) it.next();
			if (containsMetaProperty(name)) { 			
				MetaProperty p = (MetaProperty) getMetaProperty(name);
				if (p.isKey()) {
					result.add(p);
				}
			}
			else if (containsMetaReference(name)) {
				MetaReference r = (MetaReference) getMetaReference(name);
				if (r.isKey()){
					result.add(r);
				}
			}
		}
		return Collections.unmodifiableCollection(result);
	}
	
	/**
	 * Include qualified properties in case of key references. <p<
	 * 
	 * @return Collection of <tt>MetaProperty</tt>, not null and read only
	 */
	public Collection getAllMetaPropertiesKey() throws XavaException {				
		ArrayList result = new ArrayList(getMetaPropertiesKey());
		Iterator itRef = getMetaReferencesKey().iterator();
		while (itRef.hasNext()) {
			MetaReference ref = (MetaReference) itRef.next();
			Iterator itProperties = ref.getMetaModelReferenced().getAllMetaPropertiesKey().iterator();
			while (itProperties.hasNext()) {
				MetaProperty original = (MetaProperty) itProperties.next();
				original.getMapping(); // Thus the clon will have the mapping
				MetaProperty p = original.cloneMetaProperty();
				p.setName(ref.getName() + "." + p.getName());
				result.add(p);
			}
		}
		return Collections.unmodifiableCollection(result);
	}
	
	
	/**
	 * @return Collection of <tt>MetaProperty</tt>, not null and read only
	 */
	public Collection getMetaPropertiesCalculated() throws XavaException {
		Iterator it = getMembersNames().iterator(); // memberNames to keep order
		ArrayList result = new ArrayList();
		while (it.hasNext()) {
			String name = (String) it.next();
			if (!containsMetaProperty(name)) continue;			
			MetaProperty p = (MetaProperty) getMetaProperty(name);
			if (p.isCalculated()) {
				result.add(p);
			}
		}
		return Collections.unmodifiableCollection(result);
	}
	
	
	
	
	/**
	 * @return Collection of <tt>String</tt>, not null and read only
	 */
	public Collection getOnlyReadPropertiesNames() throws XavaException {
		Iterator it = getMetaProperties().iterator();
		ArrayList result = new ArrayList();
		while (it.hasNext()) {
			MetaProperty p = (MetaProperty) it.next();
			if (p.isReadOnly()) {
				result.add(p.getName());
			}
		}
		return Collections.unmodifiableCollection(result);
	}
	
	
	/**
	 * @return Collection of <tt>String</tt>, not null and read only
	 */
	public Collection getCalculatedPropertiesNames() {
		if (calculatedPropertiesNames == null) {
			Iterator it = getMetaProperties().iterator();
			ArrayList result = new ArrayList();
			while (it.hasNext()) {
				MetaProperty p = (MetaProperty) it.next();
				if (p.isCalculated()) {
					result.add(p.getName());
				}
			}
			calculatedPropertiesNames = Collections.unmodifiableCollection(result);
		}
		return calculatedPropertiesNames;
	}
	
	public Collection getMetaPropertiesWithDefaultValueCalculator() {
		if (metaPropertiesWithDefaultValueCalculator == null) {
			Iterator it = getMetaProperties().iterator();
			ArrayList result = new ArrayList();
			while (it.hasNext()) {
				MetaProperty p = (MetaProperty) it.next();
				if (p.hasDefaultValueCalculator()) {
					result.add(p);
				}
			}
			metaPropertiesWithDefaultValueCalculator = Collections.unmodifiableCollection(result);
		}
		return metaPropertiesWithDefaultValueCalculator;
	}
	
	public Collection getMetaPropertiesViewWithDefaultCalculator() {
		if (metaPropertiesViewWithDefaultCalculator == null) {
			Iterator it = getMetaPropertiesView().iterator();
			ArrayList result = new ArrayList();
			while (it.hasNext()) {
				MetaProperty p = (MetaProperty) it.next();
				if (p.hasDefaultValueCalculator()) {
					result.add(p);
				}
			}
			metaPropertiesViewWithDefaultCalculator = Collections.unmodifiableCollection(result);
		}
		return metaPropertiesViewWithDefaultCalculator;
	}
	
	public Collection getMetaReferencesWithDefaultValueCalculator() {
		if (metaReferencesWithDefaultValueCalculator == null) {
			Iterator it = getMetaReferences().iterator();
			ArrayList result = new ArrayList();
			while (it.hasNext()) {
				MetaReference ref = (MetaReference) it.next();
				if (ref.hasDefaultValueCalculator()) {
					result.add(ref);
				}
			}
			metaReferencesWithDefaultValueCalculator = Collections.unmodifiableCollection(result);
		}
		return metaReferencesWithDefaultValueCalculator;
	}
	
	
	/**
	 * Order like xml files.
	 */
	public Collection getMetaPropertiesPersistents() throws XavaException {
		if (metaPropertiesPersistents == null) {
			Iterator it = getMembersNames().iterator(); // memberNames to keep order
			ArrayList result = new ArrayList();			
			while (it.hasNext()) {
				String name = (String) it.next();
				if (!containsMetaProperty(name)) continue;			
				MetaProperty p = (MetaProperty) getMetaProperty(name);							
				if (p.isPersistent()) {
					result.add(p);
				}
			}					
			metaPropertiesPersistents = Collections.unmodifiableCollection(result);
		}
		return metaPropertiesPersistents;
	}
	
	/**
	 * Order like xml files.
	 */
	public Collection getMetaPropertiesPersistentsFromReference(String referenceName) throws XavaException {
		Iterator it = getMembersNames().iterator(); // memberNames to keep order
		ArrayList result = new ArrayList();			
		while (it.hasNext()) {
			String name = (String) it.next();
			if (!containsMetaProperty(name)) continue;			
			MetaProperty p = (MetaProperty) getMetaProperty(name).cloneMetaProperty();
			p.setQualifiedName(referenceName + "." + p.getName());
			if (p.isPersistent()) {
				result.add(p);
			}
		}					
		return result;
	}
	
	
	/**
	 * Order like xml files
	 */
	public Collection getPersistentPropertiesNames() throws XavaException {
		if (persistentPropertiesNames == null) {
			Iterator it = getMembersNames().iterator(); // memberNames to keep order
			ArrayList result = new ArrayList();
			while (it.hasNext()) {
				String name = (String) it.next();
				if (!containsMetaProperty(name)) continue;			
				MetaProperty p = (MetaProperty) getMetaProperty(name);
				if (p.isPersistent()) {
					result.add(name);
				}
			}
			persistentPropertiesNames = result;							
		}		
		return persistentPropertiesNames;
	}
	
	
	
	/**
	 * @return Collection of <tt>String</tt>, not null and read only
	 */
	public Collection getMetaPropertiesWithDefaultValueOnCreate() {
		if (metaPropertiesWithDefaultValueCalcultaorOnCreate == null) {
			Iterator it = getMetaProperties().iterator();
			ArrayList result = new ArrayList();
			while (it.hasNext()) {
				MetaProperty p = (MetaProperty) it.next();
				if (p.hasCalculatorDefaultValueOnCreate()) {
					result.add(p);
				}
			}
			metaPropertiesWithDefaultValueCalcultaorOnCreate = Collections.unmodifiableCollection(result);
		}
		return metaPropertiesWithDefaultValueCalcultaorOnCreate;
	}	
	
	
	
	/**
	 * @return Collection of <tt>MetaProperty</tt>, not null and read only
	 */
	public Collection getMetaProperties() {
		return Collections.unmodifiableCollection(getMapMetaProperties().values());
	}
	
	/**
	 * @return Collection of <tt>MetaReference</tt>, not null and read only
	 */
	public Collection getMetaReferences() {
		return Collections.unmodifiableCollection(getMapMetaReferences().values());
	}
	
	/**
	 * @return Collection of <tt>MetaReference</tt>, not null and read only
	 */
	public Collection getMetaEntityReferences() throws XavaException {
		Collection result = new ArrayList();
		Iterator it = getMapMetaReferences().values().iterator();
		while (it.hasNext()) {
			MetaReference metaReference = (MetaReference) it.next();
			if (!metaReference.isAggregate()) {
				result.add(metaReference);
			}
		}
		return result;
	}
	
	/**
	 * @return Collection of <tt>MetaReference</tt>, not null and read only
	 */
	public Collection getMetaReferencesWithMapping() throws XavaException {
		Collection result = new ArrayList();
		Iterator it = getMapMetaReferences().values().iterator();
		while (it.hasNext()) {
			MetaReference metaReference = (MetaReference) it.next();			
			if (getMapping().hasReferenceMapping(metaReference)) {
				result.add(metaReference);
			}
		}
		return result;
	}
	
	
	/**
	 * @return Collection of <tt>MetaReference</tt>, not null and read only
	 */
	public Collection getMetaReferencesKey() throws XavaException {
		Collection result = new ArrayList();
		Iterator it = getMapMetaReferences().values().iterator();
		while (it.hasNext()) {
			MetaReference metaReference = (MetaReference) it.next();
			if (metaReference.isKey()) {
				result.add(metaReference);
			}
		}
		return result;
	}
	
	
	/**
	 * @return Collection of <tt>MetaReference</tt>, not null and read only
	 */
	public Collection getMetaAggregateReferences() throws XavaException {
		Collection result = new ArrayList();
		Iterator it = getMapMetaReferences().values().iterator();
		while (it.hasNext()) {
			MetaReference metaReference = (MetaReference) it.next();
			if (metaReference.isAggregate()) {
				result.add(metaReference);
			}
		}
		return result;
	}
	
	
	/**
	 * @return Collection of <tt>MetaCollection</tt>, not null and read only
	 */
	public Collection getMetaCollections() {
		return Collections.unmodifiableCollection(getMapMetaColections().values());
	}
	
	/**
	 * @return Not null. If no view is set, then it will generate a default one
	 */
	public MetaView getMetaViewByDefault() throws XavaException {
		if (metaViewByDefault == null) {
			metaViewByDefault = new MetaView();			
			metaViewByDefault.setModelName(this.getName());
			metaViewByDefault.setMetaModel(this);
			metaViewByDefault.setMembersNames("*");			
			metaViewByDefault.setLabel(getLabel());		
		}
		return metaViewByDefault;
	}
	
	
				
	/**
	 * Container component of model. <p>
	 *
	 * @return Not null
	 */
	public MetaComponent getMetaComponent() {
		return metaComponent;
	}
	
	/**
	 * Container component of model. <p>
	 * 
	 * @param metaComponent Not null
	 */
	public void setMetaComponent(MetaComponent metaComponent) {
		this.metaComponent = metaComponent;
	}
	
	public boolean isCalculated(String propertyName) throws XavaException {
		boolean r = getCalculatedPropertiesNames().contains(propertyName);
		if (r) return r;
		
		int idx = propertyName.indexOf('.');
		if (idx >= 0) {				
			String refName = propertyName.substring(0, idx);								
			String property = propertyName.substring(idx + 1);
			return getMetaReference(refName).getMetaModelReferenced().isCalculated(property);
		}
		
		return false;		
	}	

	public String toString() {
		return getName();
	}
		
	public boolean isGenerate() {
		return generate;
	}

	public void setGenerate(boolean generate) {		
		this.generate = generate;
	}
	
	
	abstract public ModelMapping getMapping() throws XavaException;
	
	public boolean containsMetaReferenceWithModel(String name) {
		Iterator it = getMetaReferences().iterator();
		while (it.hasNext()) {
			MetaReference r = (MetaReference) it.next();
			if (r.getReferencedModelName().equals(name)) return true;
		}
		return false;
	}
				
	/**
	 * A map with the values that are keys in the sent map. <p> 
	 * 
	 * @param values  Not null
	 * @return Not null
	 */
	public Map extractKeyValues(Map values)
		throws XavaException {
		Iterator it = values.keySet().iterator();
		Map result = new HashMap();
		while (it.hasNext()) {
			String name = (String) it.next();
			if (isKey(name)) {
				result.put(name, values.get(name));
			}
		}
		return result;
	}
	
	public boolean isKey(String name) throws XavaException {		
		try { 					
			return getMetaProperty(name).isKey();		
		}
		catch (ElementNotFoundException ex) {					
			try {
				return getMetaReference(name).isKey();
			}
			catch (ElementNotFoundException ex2) {
				return false; // If is Metacollection, does no exist or is of another type
			}			
		}				
	}
	
	public boolean isHiddenKey(String name) throws XavaException {		
		try { 					
			MetaProperty pr = getMetaProperty(name); 
			return pr.isKey() && pr.isHidden();		
		}
		catch (ElementNotFoundException ex) {					
			try {
				MetaReference ref = getMetaReference(name); 
				return ref.isKey() && ref.isHidden();
			}
			catch (ElementNotFoundException ex2) {
				return false; // If is Metacollection, does no exist or is of another type
			}			
		}				
	}
	
	public boolean containsValidadors() {
		return metaValidators != null && !metaValidators.isEmpty();
	}
	
	/**
	 * 
	 * @return Not null
	 */
	public List getMetaCalculatorsPostCreate() {
		return metaCalculatorsPostCreate == null?Collections.EMPTY_LIST:metaCalculatorsPostCreate;
	}
	
	public MetaCalculator getMetaCalculatorPostCreate(int idx) {
		if (metaCalculatorsPostCreate == null) {
			throw new IndexOutOfBoundsException(XavaResources.getString("calculator_out_of_bound", new Integer(idx)));
		}		
		return (MetaCalculator) metaCalculatorsPostCreate.get(idx);
	}
	
	/**
	 * 
	 * @return Not null
	 */
	public List getMetaCalculatorsPostLoad() {
		return metaCalculatorsPostLoad == null?Collections.EMPTY_LIST:metaCalculatorsPostLoad;
	}
	
	public MetaCalculator getMetaCalculatorPostLoad(int idx) {
		if (metaCalculatorsPostLoad == null) {
			throw new IndexOutOfBoundsException(XavaResources.getString("calculator_out_of_bound", new Integer(idx)));
		}		
		return (MetaCalculator) metaCalculatorsPostLoad.get(idx);
	}
	
	/**
	 * 
	 * @return Not null
	 */
	public List getMetaCalculatorsPreRemove() {
		return metaCalculatorsPreRemove == null?Collections.EMPTY_LIST:metaCalculatorsPreRemove;
	}
	
	public MetaCalculator getMetaCalculatorPreRemove(int idx) {
		if (metaCalculatorsPreRemove == null) {
			throw new IndexOutOfBoundsException(XavaResources.getString("calculator_out_of_bound", new Integer(idx)));
		}		
		return (MetaCalculator) metaCalculatorsPreRemove.get(idx);
	}
		
	/**
	 * 
	 * @return Not null
	 */
	public List getMetaCalculatorsPostModify() {
		return metaCalculatorsPostModify == null?Collections.EMPTY_LIST:metaCalculatorsPostModify;
	}
	
	public MetaCalculator getMetaCalculatorPostModify(int idx) {
		if (metaCalculatorsPostModify == null) {
			throw new IndexOutOfBoundsException(XavaResources.getString("calculator_out_of_bound", new Integer(idx)));
		}		
		return (MetaCalculator) metaCalculatorsPostModify.get(idx);
	}
				
	/**
	 * 
	 * @return Not null
	 */
	public Collection getMetaValidators() {
		return metaValidators == null?Collections.EMPTY_LIST:metaValidators;
	}
	
	public Collection getMetaValidatorsRemove() {
		return metaValidatorsRemove == null?Collections.EMPTY_LIST:metaValidatorsRemove;
	}
	
	private Map getMapMetaPropertiesView() {
		if (mapMetaPropertiesView == null) {
			mapMetaPropertiesView = new HashMap();		
			Iterator it = getMapMetaViews().values().iterator();
			while (it.hasNext()) {
				MetaView view = (MetaView) it.next();
				Iterator itProperties = view.getMetaViewProperties().iterator();
				while (itProperties.hasNext()) {
					MetaProperty pr = (MetaProperty) itProperties.next();
					pr.setMetaModel(this); 
					pr.setReadOnly(false);
					mapMetaPropertiesView.put(pr.getName(), pr);
				}												
			}			
		}
		return mapMetaPropertiesView;
	}

	public Collection getViewPropertiesNames() {
		return Collections.unmodifiableCollection(getMapMetaPropertiesView().keySet());
	}
	
	public Collection getMetaPropertiesView() {
		return Collections.unmodifiableCollection(getMapMetaPropertiesView().values());
	}
	
	/**
	 * If this is a aggregate the return the container, else the main entity.
	 * 
	 * @return Not null
	 */
	public MetaModel getMetaModelContainer() throws XavaException { 
		if (metaModelContainer == null) {
			if (Is.emptyString(this.containerModelName)) {
				metaModelContainer = getMetaComponent().getMetaEntity();	
			}
			else {
				try {
					metaModelContainer = getMetaComponent().getMetaAggregate(this.containerModelName); 					
				}
				catch (ElementNotFoundException ex) {
					metaModelContainer = getMetaComponent().getMetaEntity(); 
				}
			}			 			
		}
		return metaModelContainer;
	}
			
	public void setContainerModelName(String modelName) {
		this.containerModelName = modelName;
	}
	
	public Collection getMetaCollectionsWithConditionInOthersModels() throws XavaException {
		Collection result = new ArrayList();
		Iterator itComponents = MetaComponent.getAllLoaded().iterator();
		while (itComponents.hasNext()) {
			MetaComponent comp = (MetaComponent) itComponents.next();			
			result.addAll(comp.getMetaEntity().getMetaColectionsWithConditionReferenceTo(getName()));
			Iterator itAggregates = comp.getMetaAggregates().iterator();
			while (itAggregates.hasNext()) {
				MetaAggregate agr = (MetaAggregate) itAggregates.next();
				result.addAll(agr.getMetaColectionsWithConditionReferenceTo(getName()));
			}			
		}
		return result;
	}
	
	public Collection getMetaColectionsWithConditionReferenceTo(String modelName) {
		Collection result = new ArrayList();
		Iterator it = getMetaCollections().iterator();
		while (it.hasNext()) {
			MetaCollection col = (MetaCollection) it.next();
			if (!Is.emptyString(col.getCondition()) && 
				col.getMetaReference().getReferencedModelName().equals(modelName)) {
				result.add(col);		
			}
		}
		return result;
	}
	
	public void addInterfaceName(String name) {
		getInterfacesNames().add(name);
	}
	
	public Collection getInterfacesNames() {
		if (interfaces == null) {
			interfaces = new ArrayList();
			interfaces.add("org.openxava.model.IModel"); 
		}
		return interfaces;
	}
	
	/**
	 * String in java format: comma separate interfaces names 
	 */
	public String getImplements() {
		StringBuffer r = new StringBuffer();
		Iterator it = getInterfacesNames().iterator();
		while (it.hasNext()) {			
			r.append(it.next());
			if (it.hasNext()) r.append(", ");
		}
		return r.toString();
	}


	public Collection getRecursiveQualifiedPropertiesNames() throws XavaException {
		if (recursiveQualifiedPropertiesNames == null) {
			Collection parents = new HashSet();
			parents.add(getName());			
			recursiveQualifiedPropertiesNames = createQualifiedPropertiesNames(parents, "");
		}
		return recursiveQualifiedPropertiesNames;
	}
	
	private Collection createQualifiedPropertiesNames(Collection parents, String prefix) throws XavaException {
		List result = new ArrayList();		
		for (Iterator it = getMembersNames().iterator(); it.hasNext();) {
			Object name = it.next();
			if (getMapMetaProperties().containsKey(name)) {
				if (Is.emptyString(prefix)) result.add(name);
				else result.add(prefix + name);				
			}
			else if (getMapMetaReferences().containsKey(name)) {
				MetaReference ref = (MetaReference) getMapMetaReferences().get(name);
				if (!parents.contains(ref.getReferencedModelName())) {
					Collection newParents = new HashSet();
					newParents.addAll(parents);
					newParents.add(ref.getReferencedModelName());	
					result.addAll(ref.getMetaModelReferenced().createQualifiedPropertiesNames(newParents, prefix + ref.getName() + "."));
				}
			}
		} 
		return result;		
	}

	public static MetaModel get(String modelName) throws XavaException { 
		if (modelName.indexOf('.') < 0) {
			return MetaComponent.get(modelName).getMetaEntity();
		}
		else {
			return MetaAggregate.getAggregate(modelName);
		}
	}
	
	public String getInterfaceName()  throws XavaException {
		return getMetaComponent().getPackageName() + ".I" + getName();
	}
	
	public String getPOJOClassName()  throws XavaException {
		return getMetaComponent().getPackageName() + "." + getName();
	}
	
	public Class getPOJOClass() throws XavaException, ClassNotFoundException { 
		if (pojoClass==null){
			pojoClass =  Class.forName(getPOJOClassName());
		}
		return pojoClass;
			
	}

	public static boolean someModelHasDefaultCalculatorOnCreateInNotKey() {		
		return someModelHasDefaultCalculatorOnCreateInNotKey ;
	}
	
	public static boolean someModelHasPostCreateCalculator() {		
		return someModelHasPostCreateCalculator;
	}
	
	public static boolean someModelHasPostModifyCalculator() {		
		return someModelHasPostModifyCalculator;
	}	
	
	
}