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

	private List metaCalculatorsPostCreate;
	private List metaCalculatorsPostModify;
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
	private boolean generateXDocLet;
	
	
	private Collection metaPropertiesWithDefaultValueCalculator;
	private List propertiesNames;
	private Collection metaPropertiesWithDefaultValueCalcultaorOnCreate;
	
	private Collection metaFinders;

	private Collection metaPropertiesPersistents;

	private Collection persistentPropertiesNames;
	private Collection interfaces;
	private Collection recursiveQualifiedPropertiesNames;
	private Collection metaReferencesWithDefaultValueCalculator;
	
	public MetaModel() {
		super();
	}
	
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
		for (Iterator it = MetaComponent.getAllLoaded().iterator(); it.hasNext();) {
			MetaComponent comp = (MetaComponent) it.next();
			MetaEntity en = comp.getMetaEntity();
			if (en.isGenerateXDocLet()) { // At momment, pojo and hibernate will be treated
				r.add(en);
			}
									
			for (Iterator itAggregates = comp.getMetaAggregates().iterator(); itAggregates.hasNext();) {
				MetaAggregate ag = (MetaAggregate) itAggregates.next();
				if (ag instanceof MetaAggregateEjb) { // At momment, pojo and hibernate will be treated
					if (ag.isGenerateXDocLet()) {
						r.add(ag);
					}
				}
			}
		}		
		return r;
	}
	
	public void addMetaFinder(MetaFinder metaBuscador) {
		if (metaFinders == null) metaFinders = new ArrayList();
		metaFinders.add(metaBuscador);		
		metaBuscador.setMetaModel(this);
	}
	
	public void addMetaMethod(MetaMethod metaMetodo) {		
		if (mapMetaMethods == null) mapMetaMethods = new HashMap();
		mapMetaMethods.put(metaMetodo.getName(), metaMetodo);
	}
	
	public void addMetaCalculatorPostCreate(MetaCalculator metaCalculador) {		
		if (metaCalculatorsPostCreate == null) metaCalculatorsPostCreate = new ArrayList();		
		metaCalculatorsPostCreate.add(metaCalculador);
	}
	
	public void addMetaCalculatorPostModify(MetaCalculator metaCalculador) {		
		if (metaCalculatorsPostModify == null) metaCalculatorsPostModify = new ArrayList();		
		metaCalculatorsPostModify.add(metaCalculador);
	}
			
	public void addMetaValidator(MetaValidator metaValidador) {
		if (metaValidators == null) metaValidators = new ArrayList();
		metaValidators.add(metaValidador);				
	}
	
	public void addMetaValidatorRemove(MetaValidator metaValidador) {
		if (metaValidatorsRemove == null) metaValidatorsRemove = new ArrayList();
		metaValidatorsRemove.add(metaValidador);				
	}	
	
	abstract public String getClassName() throws XavaException;
		

	/**	 
	 * @return Collection de MetaBuscador. Nunca nulo.
	 */
	public Collection getMetaFinders() {
		return metaFinders==null?Collections.EMPTY_LIST:metaFinders;
	}
	
	/**
	 * @return Collection de MetaMetodo. Nunca nulo.
	 */
	public Collection getMetaMethods() {
		return mapMetaMethods==null?Collections.EMPTY_LIST:mapMetaMethods.values();
	}
	
	/**
	 * 
	 * @param Nunca nulo.
	 */
	public void addMetaProperty(MetaProperty nueva) {
		getMapMetaProperties().put(nueva.getName(), nueva);
		membersNames.add(nueva.getName());
		nueva.setMetaModel(this);
		propertiesNames = null;
		recursiveQualifiedPropertiesNames = null;
	}
	
	/**
	 * 
	 * @param Nunca nulo.
	 */
	public void addMetaReference(MetaReference nueva) {
		getMapMetaReferences().put(nueva.getName(), nueva);
		membersNames.add(nueva.getName());
		nueva.setMetaModel(this);		
	}
	
	public void addMetaView(MetaView nueva) throws XavaException {		
		getMapMetaViews().put(nueva.getName(), nueva);		
		nueva.setModelName(this.getName());
		nueva.setMetaModel(this);		
		if (Is.emptyString(nueva.getName())) {
			if (this.metaViewByDefault != null) {
				throw new XavaException("no_more_1_default_view", getName());
			}
			this.metaViewByDefault = nueva;
		}						
	}	
	
	/**
	 * 
	 * @param Nunca nulo.
	 */	
	public void addMetaCollection(MetaCollection nueva) {
		getMapMetaColections().put(nueva.getName(), nueva);
		membersNames.add(nueva.getName());
		nueva.setMetaModel(this);
	}
	
	public boolean containsMetaProperty(String propiedad) {
		return getMapMetaProperties().containsKey(propiedad);
	}

	public boolean containsMetaPropertyView(String propiedad) {		
		return getMapMetaPropertiesView().containsKey(propiedad);
	}	
	
	public boolean containsMetaReference(String referencia) {
		return getMapMetaReferences().containsKey(referencia);
	}
	
	public boolean containsMetaCollection(String coleccion) {
		return getMapMetaColections().containsKey(coleccion);
	}
	
	/**
	 * Clase java que contiene las propiedades definidas en
	 * este modelo. <p>
	 * 
	 * @return Nunca nulo.
	 */
	public abstract Class getPropertiesClass() throws XavaException;
	
	/**
	 * 
	 * @exception ElementNotFoundException  Si el miembro solicitado no existe o algún otro problema.
	 * @exception XavaException Si la propiedad solicitada no existe o algún otro problema.
	 */
	public MetaMember getMetaMember(String nombre) throws XavaException {
		try {
			return getMetaProperty(nombre);
		} catch (ElementNotFoundException ex) {
			try {
				return getMetaReference(nombre);
			}
			catch (ElementNotFoundException ex2) {
				try {
					return getMetaCollection(nombre);
				}
				catch (ElementNotFoundException ex3) {
					throw new ElementNotFoundException("member_not_found", nombre, getName());
				}
			}			
		}
	}
	
	/**
	 */
	PropertyDescriptor getPropertyDescriptor(String nombrePropiedad)
		throws XavaException {
		PropertyDescriptor pd =
			(PropertyDescriptor) getPropertyDescriptors().get(nombrePropiedad);
		if (pd == null) {
			throw new ElementNotFoundException(
				"No existe la propiedad "
					+ nombrePropiedad
					+ " en "
					+ getPropertiesClass().getName());
		}
		return pd;
	}
					
	/**
	 * De las propiedades.
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
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new XavaException(
					"Imposible obtener las propiedades de " + getPropertiesClass());
			}
		}
		return propertyDescriptors;
	}
	
	/**
	 * Soporta calificación de propiedades de referencias con punto (.). <p>
	 * 
	 * @return Nunca nulo
	 * @param nombre java.lang.String
	 * @exception ElementNotFoundException  Si la propiedad solicitada no existe o algún otro problema.
	 * @exception XavaException  Algún problema.	  
	 */
	public MetaProperty getMetaProperty(String nombre) throws XavaException {
		MetaProperty r = (MetaProperty) getMapMetaProperties().get(nombre);		
		if (r == null) {
			int idx = nombre.indexOf('.');
			if (idx >= 0) {				
				String nombreReferencia = nombre.substring(0, idx);								
				String nombrePropiedad = nombre.substring(idx + 1);				
				return getMetaReference(nombreReferencia).getMetaModelReferenced().getMetaProperty(nombrePropiedad);
			}
			throw new ElementNotFoundException(
				"No encontrada propiedad " + nombre + " en modelo " + getName());
		}
		return r;
	}
	
	/**
	 * @return Nunca nulo
	 * @param nombre java.lang.String
	 * @exception ElementNotFoundException  Si la propiedad solicitada no existe o algún otro problema.
	 * @exception XavaException  Algún problema.	  
	 */
	public MetaProperty getMetaPropertyView(String nombre) throws XavaException {
		if (!containsMetaPropertyView(nombre)) {
			throw new ElementNotFoundException("property_not_found_in_any_view", nombre, getName());
		}
		return (MetaProperty) getMapMetaPropertiesView().get(nombre);
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
	
	/**	  
	 * @return Nunca nulo
	 * @param nombre 
	 * @exception ElementNotFoundException  Si la propiedad solicitada no existe o algún otro problema.
	 * @exception XavaException  Algún problema.	  
	 */
	public MetaReference getMetaReference(String nombre) throws XavaException {
		MetaReference r = (MetaReference) getMapMetaReferences().get(nombre);
		if (r == null) {
			throw new ElementNotFoundException(
				"No encontrada referencia " + nombre + " en modelo " + getName());
		}
		return r;
	}
	
	/**	  
	 * @return Nunca nulo
	 * @param nombre 
	 * @exception ElementNotFoundException  Si el método solicitado no existe.
	 * @exception XavaException  Algún problema.	  
	 */
	public MetaMethod getMetaMethod(String nombre) throws ElementNotFoundException, XavaException {
		if (mapMetaMethods == null) {
			throw new ElementNotFoundException(
				"No encontrado método " + nombre + " en modelo " + getName());			
		}
		MetaMethod m = (MetaMethod) mapMetaMethods.get(nombre);
		if (m == null) {
			throw new ElementNotFoundException(
				"No encontrado método " + nombre + " en modelo " + getName());
		}
		return m;
	}
	
	
	private Map getMapMetaReferences() {
		if (mapMetaReferences == null) {
			mapMetaReferences = new HashMap();
		}
		return mapMetaReferences;
	}
		
	/**
	 * 
	 * @return Nunca nulo
	 * @param nombre 
	 * @exception ElementNotFoundException  Si la colección solicitada no existe.
	 * @exception XavaException  Algún problema.	  
	 */
	public MetaCollection getMetaCollection(String nombre) throws XavaException {
		MetaCollection r = (MetaCollection) getMapMetaColections().get(nombre);
		if (r == null) {
			throw new ElementNotFoundException(
				"No encontrada colección " + nombre + " en modelo " + getName());
		}
		return r;
	}
	
	/**
	 * 
	 * @return Nunca nulo
	 * @param nombre 
	 * @exception ElementNotFoundException  Si la vista solicitada no existe.
	 * @exception XavaException  Algún problema.	  
	 */
	public MetaView getMetaView(String nombre) throws XavaException {		
		MetaView r = (MetaView) getMapMetaViews().get(nombre == null?"":nombre);
		if (r == null) {
			throw new ElementNotFoundException(
				"No encontrada vista " + nombre + " en modelo " + getName());
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
	 * Ordenado segun archivo xml.
	 * @return Nunca nulo, de solo lectura y serializable.
	 */
	public Collection getMembersNames() {
		return Collections.unmodifiableCollection(membersNames);
		// No se obtiene del map para conservar el orden
	}

	/**
	 * Ordenado segun archivo xml
	 * 
	 * @return Nunca nulo, de solo lectura y serializable.
	 */
	public List getPropertiesNames() {
		// Lo cogemos de nombresMiembros para que salga ordenado.
		if (propertiesNames == null) {
			List result = new ArrayList();
			Iterator it = getMembersNames().iterator();
			while (it.hasNext()) {
				Object nombre = it.next();
				if (getMapMetaProperties().containsKey(nombre)) {
					result.add(nombre);
				}
			}		
			propertiesNames = Collections.unmodifiableList(result);
		}
		return propertiesNames;
	}

	/**
	 * @return Nunca nulo, de solo lectura y serializable.
	 */
	public Collection getReferencesNames() {
		// Lo envolvemos en un array para que sea serializable		
		return Collections.unmodifiableCollection(new ArrayList(getMapMetaReferences().keySet()));
	}
	
	/**
	 * @return Nunca nulo, de solo lectura y serializable.
	 */
	public Collection getColectionsNames() {
		// Lo envolvemos en un array para que sea serializable		
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
	 * @return Colección de <tt>MetaColeccion</tt>, nunca nula y de solo lectura
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
	 * Los campos clave también se consideran requeridos. <p>
	 *
	 * @return Colección de cadenas, nunca nula y de solo lectura
	 */
	public Collection getRequiredPropertiesNames() throws XavaException {
		Iterator it = getMetaProperties().iterator();
		ArrayList result = new ArrayList();
		while (it.hasNext()) {
			MetaProperty p = (MetaProperty) it.next();
			if (p.isKey() && !p.hasCalcultaroDefaultValueOnCreate() || 
				p.isRequired()) 
			{
				result.add(p.getName());
			}
		}
		return Collections.unmodifiableCollection(result);
	}
	
	/**
	 * Los campos clave también se consideran requeridos. <p>
	 *
	 * @return Colección de cadenas, nunca nula y de solo lectura
	 */
	public Collection getRequiredMemberNames() throws XavaException {
		Iterator it = getMembersNames().iterator();
		ArrayList result = new ArrayList();
		while (it.hasNext()) {
			String nombre = (String) it.next();
			if (containsMetaProperty(nombre)) {
				MetaProperty p = getMetaProperty(nombre);
				if (p.isKey() && !p.hasCalcultaroDefaultValueOnCreate() || 
					p.isRequired())	{
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
	 * @return Collection of String, no null and only read
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
	 * @return Collection of String, no null and only read
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
	 * Incluye propiedades calificadas en caso de haber referencias clave.
	 * 
	 * @return Colección de cadenas, nunca nula y de solo lectura
	 */
	public Collection getAllKeyPropertiesNames() throws XavaException {
		ArrayList result = new ArrayList(getKeyPropertiesNames());
		Iterator itRef = getMetaReferencesKey().iterator(); 
		while (itRef.hasNext()) {
			MetaReference ref = (MetaReference) itRef.next();
			Iterator itPropiedades = ref.getMetaModelReferenced().getAllKeyPropertiesNames().iterator();
			while (itPropiedades.hasNext()) {
				result.add(ref.getName() + "." + itPropiedades.next());
			}
		}		
		return Collections.unmodifiableCollection(result);
	}
	
	
	/**
	 * Ordenado según el archivo xml
	 * 
	 * @return Colección de cadenas, nunca nula y de solo lectura
	 */
	public List getPropertiesNamesWithoutHidden() throws XavaException {
		// Lo cogemos de nombresMiembros para que salga ordenado.
		if (propertiesNamesWithoutHidden == null) {
			List result = new ArrayList();
			Iterator it = getMembersNames().iterator();
			while (it.hasNext()) {
				Object nombre = it.next();				
				MetaProperty p = (MetaProperty) getMapMetaProperties().get(nombre);
				if (p != null && !p.isHidden()) {
					result.add(nombre);  
				}									
			}		
			propertiesNamesWithoutHidden = Collections.unmodifiableList(result);
		}
		return propertiesNamesWithoutHidden;
	}
	
	
	/**
	 * @return Colección de MetaPropiedad, nunca nula y de solo lectura
	 */
	public Collection getMetaPropertiesKey() throws XavaException {
		Iterator it = getMembersNames().iterator(); // nombres miembros para que esté ordenado		
		ArrayList result = new ArrayList();
		while (it.hasNext()) {
			String nombre = (String) it.next();
			if (!containsMetaProperty(nombre)) continue;			
			MetaProperty p = (MetaProperty) getMetaProperty(nombre);
			if (p.isKey()) {
				result.add(p);
			}
		}
		return Collections.unmodifiableCollection(result);
	}
	
	/**
	 * Incluye propiedades calificadas en caso de haber referencias clave.
	 * 
	 * @return Colección de MetaPropiedad, nunca nula y de solo lectura
	 */
	public Collection getAllMetaPropertiesKey() throws XavaException {				
		ArrayList result = new ArrayList(getMetaPropertiesKey());
		Iterator itRef = getMetaReferencesKey().iterator();
		while (itRef.hasNext()) {
			MetaReference ref = (MetaReference) itRef.next();
			Iterator itPropiedades = ref.getMetaModelReferenced().getAllMetaPropertiesKey().iterator();
			while (itPropiedades.hasNext()) {
				MetaProperty original = (MetaProperty) itPropiedades.next();
				original.getMapping(); // para que el clon tenga el mapeo ya obtenido
				MetaProperty p = original.cloneMetaProperty();
				p.setName(ref.getName() + "." + p.getName());
				result.add(p);
			}
		}
		return Collections.unmodifiableCollection(result);
	}
	
	
	/**
	 * @return Colección de MetaPropiedad, nunca nula y de solo lectura
	 */
	public Collection getMetaPropertiesCalculated() throws XavaException {
		Iterator it = getMembersNames().iterator(); // nombres miembros para que esté ordenado
		ArrayList result = new ArrayList();
		while (it.hasNext()) {
			String nombre = (String) it.next();
			if (!containsMetaProperty(nombre)) continue;			
			MetaProperty p = (MetaProperty) getMetaProperty(nombre);
			if (p.isCalculated()) {
				result.add(p);
			}
		}
		return Collections.unmodifiableCollection(result);
	}
	
	
	
	
	/**
	 * Los campos clave no se consideran de solo lectura. <p>
	 *
	 * @return Colección de cadenas, nunca nula y de solo lectura.
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
	 * @return Colección de cadenas, nunca nula y de solo lectura.
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
	 * Ordenadas según en archivo xml.
	 */
	public Collection getMetaPropertiesPersistents() throws XavaException {
		if (metaPropertiesPersistents == null) {
			Iterator it = getMembersNames().iterator(); // nombres miembros para que esté ordenado
			ArrayList result = new ArrayList();
			while (it.hasNext()) {
				String nombre = (String) it.next();
				if (!containsMetaProperty(nombre)) continue;			
				MetaProperty p = (MetaProperty) getMetaProperty(nombre);
				if (p.isPersistent()) {
					result.add(p);
				}
			}					
			metaPropertiesPersistents = Collections.unmodifiableCollection(result);
		}
		return metaPropertiesPersistents;
	}
	
	/**
	 * Ordenadas según en archivo xml.
	 */
	public Collection getPersistentPropertiesNames() throws XavaException {
		if (persistentPropertiesNames == null) {
			Iterator it = getMembersNames().iterator(); // nombres miembros para que esté ordenado
			ArrayList result = new ArrayList();
			while (it.hasNext()) {
				String nombre = (String) it.next();
				if (!containsMetaProperty(nombre)) continue;			
				MetaProperty p = (MetaProperty) getMetaProperty(nombre);
				if (p.isPersistent()) {
					result.add(nombre);
				}
			}
			persistentPropertiesNames = result;							
		}
		System.out.println("[MetaModelo.getNombresPropiedadesPersistentes] " + getName() + "=" + persistentPropertiesNames);
		return persistentPropertiesNames;
	}
	
	
	
	/**
	 * @return Colección de cadenas, nunca nula y de solo lectura.
	 */
	public Collection getMetaPropertiesWithDefaultValueOnCreate() {
		if (metaPropertiesWithDefaultValueCalcultaorOnCreate == null) {
			Iterator it = getMetaProperties().iterator();
			ArrayList result = new ArrayList();
			while (it.hasNext()) {
				MetaProperty p = (MetaProperty) it.next();
				if (p.hasCalcultaroDefaultValueOnCreate()) {
					result.add(p);
				}
			}
			metaPropertiesWithDefaultValueCalcultaorOnCreate = Collections.unmodifiableCollection(result);
		}
		return metaPropertiesWithDefaultValueCalcultaorOnCreate;
	}	
	
	
	
	/**
	 * @return de <tt>MetaPropiedad</tt>. Nunca nulo y de solo lectura.
	 */
	public Collection getMetaProperties() {
		return Collections.unmodifiableCollection(getMapMetaProperties().values());
	}
	
	/**
	 * @return de <tt>MetaReferencia</tt>. Nunca nulo y de solo lectura.
	 */
	public Collection getMetaReferences() {
		return Collections.unmodifiableCollection(getMapMetaReferences().values());
	}
	
	/**
	 * @return de <tt>MetaReferencia</tt>. Nunca nulo y de solo lectura.
	 */
	public Collection getMetaEntityReferences() throws XavaException {
		Collection result = new ArrayList();
		Iterator it = getMapMetaReferences().values().iterator();
		while (it.hasNext()) {
			MetaReference metaReferencia = (MetaReference) it.next();
			if (!metaReferencia.isAggregate()) {
				result.add(metaReferencia);
			}
		}
		return result;
	}
	
	/**
	 * @return de <tt>MetaReferencia</tt>. Nunca nulo y de solo lectura.
	 */
	public Collection getMetaReferencesWithMapping() throws XavaException {
		Collection result = new ArrayList();
		Iterator it = getMapMetaReferences().values().iterator();
		while (it.hasNext()) {
			MetaReference metaReferencia = (MetaReference) it.next();			
			if (getMapping().hasReferenceMapping(metaReferencia)) {
				result.add(metaReferencia);
			}
		}
		return result;
	}
	
	
	/**
	 * @return de <tt>MetaReferencia</tt>. Nunca nulo y de solo lectura.
	 */
	public Collection getMetaReferencesKey() throws XavaException {
		Collection result = new ArrayList();
		Iterator it = getMapMetaReferences().values().iterator();
		while (it.hasNext()) {
			MetaReference metaReferencia = (MetaReference) it.next();
			if (metaReferencia.isKey()) {
				result.add(metaReferencia);
			}
		}
		return result;
	}
	
	
	/**
	 * @return de <tt>MetaReferencia</tt>. Nunca nulo y de solo lectura.
	 */
	public Collection getMetaAggregateReferences() throws XavaException {
		Collection result = new ArrayList();
		Iterator it = getMapMetaReferences().values().iterator();
		while (it.hasNext()) {
			MetaReference metaReferencia = (MetaReference) it.next();
			if (metaReferencia.isAggregate()) {
				result.add(metaReferencia);
			}
		}
		return result;
	}
	
	
	/**
	 * @return de <tt>MetaColeccion</tt>. Nunca nulo y de solo lectura.
	 */
	public Collection getMetaCollections() {
		return Collections.unmodifiableCollection(getMapMetaColections().values());
	}
	
	/**
	 * @return Nunca nulo, si no se ha establecido ninguna vista o
	 *         se ha establecido a nulo, se generará una por defecto.     
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
	 * Componente conenedor del modelo.
	 * @return Nunca nulo.
	 */
	public MetaComponent getMetaComponent() {
		return metaComponent;
	}
	
	/**
	 * Componente conenedor del modelo.
	 * @param componente Nunca nulo
	 */
	public void setMetaComponent(MetaComponent componente) {
		this.metaComponent = componente;
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

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName();
	}

		

	/**
	 * Returns the generaXDocLet.
	 * @return boolean
	 */
	public boolean isGenerateXDocLet() {
		return generateXDocLet;
	}

	/**
	 * Sets the generaXDocLet.
	 * @param generaXDocLet The generaXDocLet to set
	 */
	public void setGenerateXDocLet(boolean generaXDocLet) {		
		this.generateXDocLet = generaXDocLet;
	}
	
	
	abstract public ModelMapping getMapping() throws XavaException;
	
	/**
	 * Method containsMetaReferenciaConModelo.
	 * @param nombre
	 * @return boolean
	 */
	public boolean containsMetaReferenceWithModel(String nombre) {
		Iterator it = getMetaReferences().iterator();
		while (it.hasNext()) {
			MetaReference r = (MetaReference) it.next();
			if (r.getReferencedModelName().equals(nombre)) return true;
		}
		return false;
	}
				
	/**
	 * Un mapa con los valores que sean clave en el mapa enviado. <p>
	 * 
	 * @param valores No puede ser nulo.
	 * @return Nuncan nulo.
	 */
	public Map extractKeyValues(Map valores)
		throws XavaException {
		Iterator it = valores.keySet().iterator();
		Map result = new HashMap();
		while (it.hasNext()) {
			String nombre = (String) it.next();
			if (isKey(nombre)) {
				result.put(nombre, valores.get(nombre));
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
				return false; // Si es MetaColeccion, no existe o es otro tipo
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
				return false; // Si es MetaColeccion, no existe o es otro tipo
			}			
		}				
	}
	

	public boolean containsValidadors() {
		return metaValidators != null && !metaValidators.isEmpty();
	}
	
	/**
	 * 
	 * @return Nuncan nulo.
	 */
	public Collection getMetaCalculatorsPostCreate() {
		return metaCalculatorsPostCreate == null?Collections.EMPTY_LIST:metaCalculatorsPostCreate;
	}
	
	public MetaCalculator getMetaCalculatorPostCreate(int idx) {
		if (metaCalculatorsPostCreate == null) {
			throw new IndexOutOfBoundsException(XavaResources.getString("calculator_out_of_bound", new Integer(idx)));
		}		
		return (MetaCalculator) metaCalculatorsPostCreate.get(idx);
	}
	
	public int getMetaCalculatorsPostCreateCount() {
		if (metaCalculatorsPostCreate == null) return 0;
		return metaCalculatorsPostCreate.size(); 
	}
	
	/**
	 * 
	 * @return Nuncan nulo.
	 */
	public Collection getMetaCalculatorsPostModify() {
		return metaCalculatorsPostModify == null?Collections.EMPTY_LIST:metaCalculatorsPostModify;
	}
	
	public MetaCalculator getMetaCalculatorPostModify(int idx) {
		if (metaCalculatorsPostModify == null) {
			throw new IndexOutOfBoundsException(XavaResources.getString("calculator_out_of_bound", new Integer(idx)));
		}		
		return (MetaCalculator) metaCalculatorsPostModify.get(idx);
	}
	
	public int getMetaCalculatorsPostModifyCount() {
		if (metaCalculatorsPostModify == null) return 0;
		return metaCalculatorsPostModify.size(); 
	}	
			
	/**
	 * 
	 * @return Nuncan nulo.
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
				MetaView vista = (MetaView) it.next();
				Iterator itPropiedades = vista.getMetaViewProperties().iterator();
				while (itPropiedades.hasNext()) {
					MetaProperty pr = (MetaProperty) itPropiedades.next();
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
	 * Si es un agregado el contenedor, y si no la entidad principal
	 * 
	 * @return Nuncan nulo
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
			
	public void setContainerModelName(String nombreModelo) {
		this.containerModelName = nombreModelo;
	}
	
	public Collection getMetaCollectionsWithConditionInOthersModels() throws XavaException {
		Collection result = new ArrayList();
		Iterator itComponentes = MetaComponent.getAllLoaded().iterator();
		while (itComponentes.hasNext()) {
			MetaComponent comp = (MetaComponent) itComponentes.next();			
			result.addAll(comp.getMetaEntity().getMetaColectionsWithConditionReferenceTo(getName()));
			Iterator itAgregados = comp.getMetaAggregates().iterator();
			while (itAgregados.hasNext()) {
				MetaAggregate agr = (MetaAggregate) itAgregados.next();
				result.addAll(agr.getMetaColectionsWithConditionReferenceTo(getName()));
			}			
		}
		return result;
	}
	
	public Collection getMetaColectionsWithConditionReferenceTo(String nombreModelo) {
		Collection result = new ArrayList();
		Iterator it = getMetaCollections().iterator();
		while (it.hasNext()) {
			MetaCollection col = (MetaCollection) it.next();
			if (!Is.emptyString(col.getCondition()) && 
				col.getMetaReference().getReferencedModelName().equals(nombreModelo)) {
				result.add(col);		
			}
		}
		return result;
	}
	
	public void addInterfaceName(String name) {
		if (interfaces == null) interfaces = new ArrayList();
		interfaces.add(name);
	}
	
	public Collection getInterfacesNames() {
		if (interfaces == null) return Collections.EMPTY_LIST;
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
			recursiveQualifiedPropertiesNames = createQualifiedPropertiesNames("", parents);
		}
		return recursiveQualifiedPropertiesNames;
	}
	
	private Collection createQualifiedPropertiesNames(String prefix, Collection parents) throws XavaException {
		List result = new ArrayList();		
		parents.add(getName());
		for (Iterator it = getMembersNames().iterator(); it.hasNext();) {
			Object name = it.next();
			if (getMapMetaProperties().containsKey(name)) {
				if (Is.emptyString(prefix)) result.add(name);
				else result.add(prefix + name);				
			}
		}
		for (Iterator it=getMetaReferences().iterator(); it.hasNext();) {
			MetaReference ref = (MetaReference) it.next();
			if (parents.contains(ref.getReferencedModelName())) continue;			 
			result.addAll(ref.getMetaModelReferenced().createQualifiedPropertiesNames(prefix + ref.getName() + ".", parents));
		}			
		return result;		
	}
	
}