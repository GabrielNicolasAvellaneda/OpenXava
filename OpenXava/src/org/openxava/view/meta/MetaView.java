package org.openxava.view.meta;


import java.util.*;

import org.openxava.actions.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;

/**
 * 
 * 
 * @author Javier Paniza
 */
public class MetaView extends MetaElement implements Cloneable {
	
	// OJO: Si se añaden propiedades ver si hay que hacer clon de ellas
	
	private final static String NAME_SEPARATOR = "\n";
	
	private boolean section = false;
	private MetaView parent; // in section case
	private String parentName = null; // at momment for use in section
	private Collection allMetaMembers;
	private Map metaGroups;
	private Map metaProperties;
	private Collection propertiesNamesThrowOnChange;	
	private List sections = null;
	private Collection metaMembers; // De MetaMiembro
	private Collection _membersNames = new ArrayList(); // De String
	private Map metaViewsReferences;
	private Map metaViewsProperties;
	private Map metaViewsCollections;
	private Map metaViewProperties;
	private MetaSearchAction metaSearchAction;
		
	private MetaModel metaModel;
	private java.lang.String modelName;
	private boolean allMembers;
	private boolean membersNamesByDefaultCreated = false;
	private boolean frame = true;
	
	
	private String mediatorClassName;
	
	/**
	 * Comentario de constructor Vista.
	 */
	public MetaView() {
		super();
	}
		
	/**
	 * 
	 * @param nombrePropiedad java.lang.String
	 */
	private void addMemberName(String nombreMiembro) {
		_membersNames.add(nombreMiembro);
	}

	public void addMetaViewProperty(MetaProperty metaPropiedad) throws XavaException {
		if (metaViewProperties == null) metaViewProperties = new HashMap();
		metaViewProperties.put(metaPropiedad.getName(), metaPropiedad);
	}
	
	public void addMetaViewReference(MetaReferenceView metaVistaReferencia) throws XavaException {
		if (metaViewsReferences == null) metaViewsReferences = new HashMap();
		else {
			if (metaViewsReferences.containsKey(metaVistaReferencia.getReferenceName())) {
				throw new XavaException("reference_view_already_defined", metaVistaReferencia.getReferenceName(), getName(), getModelName());
			}
		}
		metaViewsReferences.put(metaVistaReferencia.getReferenceName(), metaVistaReferencia);		
	}
	
	public void addMetaViewProperty(MetaPropertyView metaVistaPropiedad) throws XavaException {
		if (metaViewsProperties == null) metaViewsProperties = new HashMap();
		else {
			if (metaViewsProperties.containsKey(metaVistaPropiedad.getPropertyName())) {
				throw new XavaException("property_view_already_defined", metaVistaPropiedad.getPropertyName(), getName(), getModelName());
			}
		}
		metaViewsProperties.put(metaVistaPropiedad.getPropertyName(), metaVistaPropiedad);
	}	
	
	public void addMetaViewCollection(MetaCollectionView metaVistaColeccion) throws XavaException {
		if (metaViewsCollections == null) metaViewsCollections = new HashMap();
		else {
			if (metaViewsCollections.containsKey(metaVistaColeccion.getCollectionName())) {
				throw new XavaException("collection_view_already_defined", metaVistaColeccion.getCollectionName(), getName(), getModelName());
			}
		}		
		metaViewsCollections.put(metaVistaColeccion.getCollectionName(), metaVistaColeccion);		
	}	
	
	/**
	 * Incluso si está dentro de una sección  
	 */
	public MetaProperty getMetaProperty(String nombre) throws XavaException {
		return getMetaProperty(nombre, true);
	}
	
	private MetaProperty getMetaProperty(String nombre, boolean searchInGroups) throws XavaException {
		try {
			return getMetaPropiedadVista(nombre);			
		}
		catch (ElementNotFoundException ex) {
			if (metaProperties == null) {
				metaProperties = new HashMap();
				Iterator it = getAllMetaMembers().iterator(); 
				while (it.hasNext()) {
					Object m = it.next();
					if (m instanceof MetaProperty) {
						metaProperties.put(((MetaProperty) m).getName(), m);
					}
				}
			}
			MetaProperty p = (MetaProperty) metaProperties.get(nombre);
			if (searchInGroups && p == null) {
				p = getMetaPropertyInGroup(nombre);
			}
			if (p == null) {
				throw new ElementNotFoundException("property_not_found_in_view", nombre, getName(), getModelName());
			}
			return p;
		}
	}
	
	
	private MetaProperty getMetaPropertyInGroup(String name) throws XavaException { 
		if (metaGroups == null) return null;
		for (Iterator it = metaGroups.values().iterator(); it.hasNext(); ) {
			MetaGroup metaGroup = (MetaGroup) it.next();
			try {
				return metaGroup.getMetaView().getMetaProperty(name, false);
			}
			catch (ElementNotFoundException ex) {				
			}
		}
		return null;
	}

	// incluye los miembros de las secciones
	private Collection getAllMetaMembers() throws XavaException { 
		if (!hasSections()) return getMetaMembers();
		if (allMetaMembers == null) {		
			allMetaMembers = new ArrayList();
			allMetaMembers.addAll(getMetaMembers());
			Iterator it = getSections().iterator();
			while (it.hasNext()) {
				MetaView seccion = (MetaView) it.next();
				allMetaMembers.addAll(seccion.getMetaMembers());
			}
		}
		return allMetaMembers;
	}
	
	/**
	 * De la propia vista, que no están en el modelo.	 
	 */
	private MetaProperty getMetaPropiedadVista(String nombre) throws XavaException {
		if (metaViewProperties == null) 
			throw new ElementNotFoundException("view_property_not_found", nombre, getName(), getModelName());
		MetaProperty p = (MetaProperty) metaViewProperties.get(nombre);
		if (p == null)
			throw new ElementNotFoundException("view_property_not_found", nombre, getName(), getModelName());
		return p;
	}
	
	/**
	 * 
	 * @return Nunca nulo, de tipo <tt>MetaMiembro</tt> y solo lectura
	 */
	public Collection getMetaMembers() throws XavaException {
		if (metaMembers == null) {
			metaMembers = new ArrayList();
			Iterator it = getMembersNames().iterator();			
			while (it.hasNext()) {
				String nombre = (String) it.next();
				if (nombre.startsWith("__GROUP__")) {
					String nombreGrupo = nombre.substring("__GROUP__".length());					
					metaMembers.add(getMetaGroup(nombreGrupo));					
				}
				else if (nombre.equals(NAME_SEPARATOR)) {
					metaMembers.add(PropertiesSeparator.INSTANCE);
				}				
				else {
					MetaMember miembro = null;
					try {
						miembro = getMetaModel().getMetaMember(nombre);
					}
					catch (ElementNotFoundException ex) {
						miembro = getMetaPropiedadVista(nombre);
					}
					if (!miembro.isHidden()) {
						miembro = modify(miembro);
						metaMembers.add(miembro);
					}					
				}
			}
			metaMembers = Collections.unmodifiableCollection(metaMembers);						
		}
		return metaMembers;
	}

	private MetaMember modify(MetaMember miembro) throws XavaException {
		if (miembro instanceof MetaProperty) {
			MetaProperty propiedad = (MetaProperty) miembro;
			MetaProperty nueva = propiedad.cloneMetaProperty();
			miembro = nueva;
			MetaPropertyView vistaPropiedad = getMetaVistaPropiedadPara(propiedad.getName());						
			if (vistaPropiedad != null) {				
				String etiqueta = vistaPropiedad.getLabel();				
				if (!Is.emptyString(etiqueta)) {					
					nueva.setLabel(etiqueta);					
				}
				if (!nueva.isCalculated()) {
					nueva.setReadOnly(vistaPropiedad.isReadOnly());
				}
			}
		}
		String idEtiqueta = isSection()?
				getParent().getId() + "." + miembro.getName():
				getId() + "." + miembro.getName();		
		miembro.setLabelId(idEtiqueta);
		return miembro;
	}
	
	/**
	 * 
	 * @return Nunca nulo.
	 */
	public MetaModel getMetaModel() throws XavaException {
		return metaModel;
	}
	
	public void setMetaModel(MetaModel metaModelo) throws XavaException {
		this.metaModel = metaModelo;
		if (hasSections()) {
			Iterator it = getSections().iterator();
			while (it.hasNext()) {
				MetaView seccion = (MetaView) it.next();
				seccion.setMetaModel(metaModelo);
			}
		}
	}
	
	/**
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getModelName() throws XavaException {
		return modelName;
	}
	
	public void setModelName(String nombreModelo) throws XavaException {
		this.modelName = nombreModelo;
		Iterator it = getSections().iterator();
		while (it.hasNext()) {
			((MetaView) it.next()).setModelName(this.modelName);
		}
	}
	/**
	 * 
	 * @return boolean
	 */
	public boolean isAllMembers() {
		return allMembers;
	}
	
	/**
	 * Si no tiene miembros a visualizar.
	 */
	public boolean isEmpty() throws XavaException {
		return getMetaMembers().size() == 0;
	}
	
	/**
	 * 
	 * @return Nunca nulo, de tipo <tt>String</tt> y de solo lectura.
	 */
	public Collection getMembersNames() throws XavaException {
		if (isAllMembers() && !membersNamesByDefaultCreated && _membersNames.isEmpty()) {						
			crearNombresMiembrosPorDefecto();			
		}
		return Collections.unmodifiableCollection(_membersNames);				
	}
	
	/**
	 * 
	 * @param propiedades Nombres separados por comas o espacios.
	 */
	public void setMembersNames(String nombresMiembros) throws XavaException {			
		iniciar();				
		if (nombresMiembros.trim().equals("*")) {
			setAllMembers(true);			
		} else {
			setAllMembers(false);			
			StringTokenizer lineas = new StringTokenizer(nombresMiembros, ";");
			while (lineas.hasMoreTokens()) {
				String linea = lineas.nextToken();
				StringTokenizer nombres = new StringTokenizer(linea, ",");
				while (nombres.hasMoreTokens()) {	
					String nombreMiembro = nombres.nextToken().trim();
					if (!nombreMiembro.equals("")) {
						addMemberName(nombreMiembro);					
					}
				}
				if (lineas.hasMoreTokens()) {
					addMemberName(NAME_SEPARATOR);
				}				
			}
		}
	}
	
	private void iniciar() {
		this._membersNames.clear();
		this.allMetaMembers = null;
		metaProperties = null;
		propertiesNamesThrowOnChange = null;	
		sections = null;
		metaMembers = null;		
	}

	private void crearNombresMiembrosPorDefecto() throws XavaException {
		MetaModel metaModelo = getMetaModel();
		if (metaModelo != null) {
			Iterator it = metaModelo.getMembersNames().iterator();			
			while (it.hasNext()) {
				String nombreMiembro = (String) it.next();								
				addMemberName(nombreMiembro);	
				addMemberName(NAME_SEPARATOR);			
			}
		}
		membersNamesByDefaultCreated = true;
	}
			
	
	/**
	 * 
	 * @param newTodasPropiedades boolean
	 */
	public void setAllMembers(boolean newTodosMiembros) {
		allMembers = newTodosMiembros;
	}
	
	public boolean hasOnChangeAction(String nombreCualificadoPropiedad) { 
		MetaPropertyView metaVistaPropiedad = getMetaVistaPropiedadPara(nombreCualificadoPropiedad);		
		if (metaVistaPropiedad == null) return false;
		return metaVistaPropiedad.hasOnChangeAction(); 
	}
		
	public IOnChangePropertyAction createOnChangeAction(String nombreCualificadoPropiedad) throws XavaException {  
		MetaPropertyView metaVistaPropiedad = getMetaVistaPropiedadPara(nombreCualificadoPropiedad);		
		if (metaVistaPropiedad == null) {
			throw new XavaException("on_change_action_not_found", nombreCualificadoPropiedad);
		}
		return metaVistaPropiedad.createOnChangeAction();
	}	
		
	public boolean hasMetaSearchAction() {
		return this.metaSearchAction != null;
	}
	
	/**
	 * Para buscar objetos del tipo representado por esta
	 * vista. NO PARA LAS REFERENCIAS.
	 */
	public MetaSearchAction getMetaSearchAction() {
		if (!hasMetaSearchAction()) return null;
		return metaSearchAction;
	}
	
	public void setMetaSearchAction(MetaSearchAction metaAccionBuscar) {
		this.metaSearchAction = metaAccionBuscar;
	}
	
	/**
	 * @return Nulo si no tiene
	 */
	public MetaDescriptionsList getMetaDescriptionList(MetaReference r) throws XavaException {
		if (!tieneMetaVistaReferenciaPara(r)) return null;
		MetaReferenceView metaVistaReferencia = getMetaReferenceView(r);
		MetaDescriptionsList metaListaDescripciones = metaVistaReferencia.getMetaDescriptionsList();
		if (metaListaDescripciones==null) return null;
		if (Is.emptyString(metaListaDescripciones.getDescriptionPropertyName()) &&
				Is.emptyString(metaListaDescripciones.getDescriptionPropertiesNames())
				) {			
			Collection propiedades = r.getMetaModelReferenced().getPropertiesNames();
			if (propiedades.contains("descripcion")) metaListaDescripciones.setDescriptionPropertyName("descripcion");
			else if (propiedades.contains("description")) metaListaDescripciones.setDescriptionPropertyName("description");
			else if (propiedades.contains("nombre")) metaListaDescripciones.setDescriptionPropertyName("nombre");
			else if (propiedades.contains("name")) metaListaDescripciones.setDescriptionPropertyName("name");
			else throw new XavaException("description_property_required");  
		}
		return metaListaDescripciones;
	}
	
	/**
	 * @return del tipo <tt>ListaDescripciones</tt>
	 */
	public Collection getMetaDescriptionsLists() {
		Collection metaListasDescripciones = new ArrayList();
		if (metaViewsReferences == null) return metaListasDescripciones;
		Iterator it = metaViewsReferences.values().iterator();
		while (it.hasNext()) {
			MetaReferenceView vistaReferencia = (MetaReferenceView) it.next();
			MetaDescriptionsList listaDescripciones = vistaReferencia.getMetaDescriptionsList(); 
			if (listaDescripciones != null) {
				metaListasDescripciones.add(listaDescripciones);
			}
		}
		return metaListasDescripciones;  
	}
	
	public MetaView getMetaView(MetaReference r) throws XavaException {
		MetaView result = null;		
		if (tieneMetaVistaReferenciaPara(r)) {			
			MetaReferenceView metaVistaReferencia = getMetaReferenceView(r);
			String nombreVista = metaVistaReferencia.getViewName();
			if (Is.emptyString(nombreVista)) {
				result = r.getMetaModelReferenced().getMetaViewByDefault();				
			}
			else {
				result = r.getMetaModelReferenced().getMetaView(nombreVista);				 
			}
			try {
				result = (MetaView) result.clone();
			}
			catch (CloneNotSupportedException e) {
				throw new XavaException("meta_view_reference_error_no_clone");				
			}
			result.setMetaSearchAction(metaVistaReferencia.getMetaSearchAction());	
			result.setFrame(metaVistaReferencia.isFrame());
			MetaDescriptionsList metaListaDescripciones = getMetaDescriptionList(r);			
			if (metaListaDescripciones != null) {
				result.borrarMiembros();
				Iterator itClaves = r.getMetaModelReferenced().getKeyPropertiesNames().iterator();
				while (itClaves.hasNext()) {
					result.addMemberName((String)itClaves.next());
				}
			}
		}
		else {
			result = r.getMetaModelReferenced().getMetaViewByDefault();			
		}
		result.setLabel(r.getLabel());
		return result;
	}
	
	private void borrarMiembros() {
		if (_membersNames != null) _membersNames.clear();
		allMetaMembers = null;
		metaMembers = null;
		metaProperties = null;				
	}

	private boolean tieneMetaVistaReferenciaPara(MetaReference r) {				
		if (metaViewsReferences == null) {
			return false;
		}		
		return metaViewsReferences.containsKey(r.getName());
	}
	
	/**
	 * @return Nulo si no existe
	 */
	public MetaReferenceView getMetaReferenceView(MetaReference r) {
		if (metaViewsReferences == null) {
			return null;
		}
		return (MetaReferenceView) metaViewsReferences.get(r.getName());
	}
	
	private MetaPropertyView getMetaVistaPropiedadPara(String nombreCualificadoPropiedad) { 		
		if (metaViewsProperties == null) return null;
		return (MetaPropertyView) metaViewsProperties.get(nombreCualificadoPropiedad);
	}
	
	
	public List getSections() throws XavaException {
		return sections == null?Collections.EMPTY_LIST:sections;		
	}
	
	public void addSection(String name, String label, String members) throws XavaException {
		if (sections == null) sections = new ArrayList();		
		MetaView section = new MetaView();
		section.setSection(true);
		section.setParent(this);
		section.setName(name);
		section.parentName = getName();
		section.setLabel(label);
		section.setMembersNames(members);
		promocionar(section);		
		sections.add(section);
	}
	
	public void addMetaGroup(String name, String label, String members) throws XavaException {
		if (metaGroups == null) metaGroups = new HashMap();
		MetaGroup metaGroup = new MetaGroup(this);
		metaGroup.setName(name); 		
		metaGroup.setLabel(label);
		metaGroup.setMembersNames(members);				
		metaGroups.put(name, metaGroup);		
	}
		
	private MetaGroup getMetaGroup(String nombre) throws XavaException {
		if (metaGroups == null) {
			throw new ElementNotFoundException("group_not_found_no_groups", nombre);
		}
		Object result = metaGroups.get(nombre);
		if (result == null) { 
			throw new ElementNotFoundException("group_not_found", nombre);
		}
		return (MetaGroup) result;
	}
		
	private void promocionar(MetaView vista)
		throws XavaException {
		vista.setMetaSearchAction(this.getMetaSearchAction());
		vista.setMetaModel(this.getMetaModel());
				
		vista.setModelName(this.getModelName());
		
		if (this.metaViewsProperties == null) {
			this.metaViewsProperties = new HashMap();
		}
		vista.metaViewsProperties = this.metaViewsProperties;
		if (this.metaViewsReferences == null) {
			this.metaViewsReferences = new HashMap();
		}
		vista.metaViewsReferences = this.metaViewsReferences;
		if (this.metaViewsCollections == null) {
			this.metaViewsCollections = new HashMap();
		}
		vista.metaViewsCollections = this.metaViewsCollections;
		vista.metaViewProperties = this.metaViewProperties;
		vista.metaGroups = this.metaGroups; 
	}
	
	public boolean hasSections() {
		return sections != null;
	}
		
	public void clearSections() {
		sections =  null;
	}			
	
	
	/**
	 * Returns the nombreClaseMediador.
	 * @return String
	 */
	public String getMediatorClassName() throws XavaException {		
		return mediatorClassName;
	}

	/**
	 * Sets the nombreClaseMediador.
	 * @param nombreClaseMediador The nombreClaseMediador to set
	 */
	public void setMediatorClassName(String nombreClaseMediador) {
		this.mediatorClassName = nombreClaseMediador;
	}
	/**
	 * Method getMetaVistaColeccion.
	 * @param string
	 * @return Nulo si no existe.
	 */
	public MetaCollectionView getMetaCollectionView(String nombreColeccion) {		
		if (metaViewsCollections == null) return null;
		return (MetaCollectionView) metaViewsCollections.get(nombreColeccion);				
	}
	
	
	/**
	 * Returns the sacarMarco.
	 * @return boolean
	 */
	public boolean isFrame() {
		return frame;
	}

	/**
	 * Sets the sacarMarco.
	 * @param sacarMarco The sacarMarco to set
	 */
	public void setFrame(boolean sacarMarco) {
		this.frame = sacarMarco;
	}
		
	public String getId() {
		if (!Is.emptyString(parentName)) return modelName + ".views." + parentName; // in section case		
		return getIdIfNotSection();
	}
	
	private String getIdIfNotSection() {
		if (hasName()) return modelName + ".views." + getName();
		else return modelName + ".view";				
	}

	protected String getLabel(Locale locale, String id) {
		if (!Is.emptyString(parentName)) { // If is a section
			id = getIdIfNotSection();
		} 
		return super.getLabel(locale, id);
	}

	public Collection getPropertiesNamesThrowOnChange() {
		if (propertiesNamesThrowOnChange == null) {
			if (metaViewsProperties == null) propertiesNamesThrowOnChange = Collections.EMPTY_LIST;
			else {
				propertiesNamesThrowOnChange = new ArrayList();
				Iterator it = metaViewsProperties.values().iterator();
				while (it.hasNext()) {
					MetaPropertyView vistaPropiedad = (MetaPropertyView) it.next();
					if (vistaPropiedad.hasOnChangeAction()) {
						propertiesNamesThrowOnChange.add(vistaPropiedad.getPropertyName());
					}
				}
				propertiesNamesThrowOnChange = Collections.unmodifiableCollection(propertiesNamesThrowOnChange);
			} 			 
		}
		return propertiesNamesThrowOnChange;
	}

	public Collection getViewPropertiesNames() {
		if (metaViewProperties == null) return Collections.EMPTY_LIST;
		return metaViewProperties.keySet();
	}
	
	public Collection getMetaViewProperties() { 
		if (metaViewProperties == null) return Collections.EMPTY_LIST;
		return metaViewProperties.values();
	}
	
	
	protected Object clone() throws CloneNotSupportedException { 
		MetaView clon =  (MetaView) super.clone();
				
		if (propertiesNamesThrowOnChange != null) clon.propertiesNamesThrowOnChange = new ArrayList(propertiesNamesThrowOnChange);
		if (sections != null) clon.sections = new ArrayList(sections);
		if (metaGroups != null) clon.metaGroups = new HashMap(metaGroups);
		if (_membersNames != null) clon._membersNames = new ArrayList(_membersNames);
		if (metaViewsReferences != null) clon.metaViewsReferences = new HashMap(metaViewsReferences);
		if (metaViewsProperties != null) clon.metaViewsProperties = new HashMap(metaViewsProperties);
		if (metaViewsCollections != null) clon.metaViewsCollections = new HashMap(metaViewsCollections);
		if (metaViewProperties != null) clon.metaViewProperties = new HashMap(metaViewProperties);
		if (metaSearchAction != null) clon.metaSearchAction = (MetaSearchAction) metaSearchAction.clone(); 
		
		return clon;
	}

	public Collection getActionsNamesForProperty(MetaProperty p) {
		MetaPropertyView metaVistaPropiedad = getMetaVistaPropiedadPara(p.getName());
		if (metaVistaPropiedad == null) return Collections.EMPTY_LIST;
		return metaVistaPropiedad.getActionsNames();
	}

	public int getLabelFormatForProperty(MetaProperty p) {
		MetaPropertyView metaVistaPropiedad = getMetaVistaPropiedadPara(p.getName());
		if (metaVistaPropiedad == null) return MetaPropertyView.NORMAL_LABEL;
		return metaVistaPropiedad.getLabelFormat();
	}

	private boolean isSection() {
		return section;
	}
	private void setSection(boolean section) {
		this.section = section;
	}
	private MetaView getParent() {
		return parent;
	}
	private void setParent(MetaView parent) {
		this.parent = parent;
	}
}