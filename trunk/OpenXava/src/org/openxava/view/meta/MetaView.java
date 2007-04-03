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
	
	// WARNING!: If you add properties you must see if is needed to make a clon of they
	
	private final static String NAME_SEPARATOR = "\n";
		
	private boolean section = false;
	private MetaView parent; // in section case
	private String parentName = null; // at momment for use in section
	private Collection allMetaMembers;
	private Map metaGroups;
	private Map metaProperties;
	private Collection propertiesNamesThrowOnChange;	
	private List sections = null;
	private Collection metaMembers; // Of MetaMember
	private Collection _membersNames = new ArrayList(); // Of String
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
	private boolean alignedByColumns = false; 
		
	private String mediatorClassName;
	
	
	
	private void addMemberName(String memberName) {
		_membersNames.add(memberName);
	}

	public void addMetaViewProperty(MetaProperty metaProperty) throws XavaException {
		if (metaViewProperties == null) metaViewProperties = new HashMap();
		metaViewProperties.put(metaProperty.getName(), metaProperty);
	}
	
	public void addMetaViewReference(MetaReferenceView metaReferenceView) throws XavaException {
		if (metaViewsReferences == null) metaViewsReferences = new HashMap();
		else {
			if (metaViewsReferences.containsKey(metaReferenceView.getReferenceName())) {
				throw new XavaException("reference_view_already_defined", metaReferenceView.getReferenceName(), getName(), getModelName());
			}
		}
		metaViewsReferences.put(metaReferenceView.getReferenceName(), metaReferenceView);		
	}
	
	public void addMetaViewProperty(MetaPropertyView metaPropertyView) throws XavaException {
		if (metaViewsProperties == null) metaViewsProperties = new HashMap();
		else {
			if (metaViewsProperties.containsKey(metaPropertyView.getPropertyName())) {
				throw new XavaException("property_view_already_defined", metaPropertyView.getPropertyName(), getName(), getModelName());
			}
		}
		metaViewsProperties.put(metaPropertyView.getPropertyName(), metaPropertyView);
	}	
	
	public void addMetaViewCollection(MetaCollectionView metaCollectionView) throws XavaException {
		if (metaViewsCollections == null) metaViewsCollections = new HashMap();
		else {
			if (metaViewsCollections.containsKey(metaCollectionView.getCollectionName())) {
				throw new XavaException("collection_view_already_defined", metaCollectionView.getCollectionName(), getName(), getModelName());
			}
		}		
		metaViewsCollections.put(metaCollectionView.getCollectionName(), metaCollectionView);		
	}	
	
	/**
	 * Including if property is inside a section  
	 */
	public MetaProperty getMetaProperty(String name) throws XavaException {
		return getMetaProperty(name, true);
	}
	
	private MetaProperty getMetaProperty(String name, boolean searchInGroups) throws XavaException {
		try {
			return getMetaViewProperty(name);			
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
			MetaProperty p = (MetaProperty) metaProperties.get(name);
			if (searchInGroups && p == null) {
				p = getMetaPropertyInGroup(name);
			}
			if (p == null) {
				throw new ElementNotFoundException("property_not_found_in_view", name, getName(), getModelName());
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

	// Including members inside sections
	private Collection getAllMetaMembers() throws XavaException { 
		if (!hasSections()) return getMetaMembers();
		if (allMetaMembers == null) {		
			allMetaMembers = new ArrayList();
			allMetaMembers.addAll(getMetaMembers());
			Iterator it = getSections().iterator();
			while (it.hasNext()) {
				MetaView section = (MetaView) it.next();
				allMetaMembers.addAll(section.getAllMetaMembers());
			}			
		}
		return allMetaMembers;
	}
	
	/**
	 * Property only of the view, not in model	 
	 */
	private MetaProperty getMetaViewProperty(String name) throws XavaException {
		if (metaViewProperties == null) 
			throw new ElementNotFoundException("view_property_not_found", name, getName(), getModelName());
		MetaProperty p = (MetaProperty) metaViewProperties.get(name);
		if (p == null)
			throw new ElementNotFoundException("view_property_not_found", name, getName(), getModelName());
		return p;
	}
	
	/**
	 * 
	 * @return Not null, of type <tt>MetaMember</tt> and read only
	 */
	public Collection getMetaMembers() throws XavaException {
		if (metaMembers == null) {
			metaMembers = new ArrayList();
			Iterator it = getMembersNames().iterator();			
			while (it.hasNext()) {
				String name = (String) it.next();
				if (name.startsWith("__GROUP__")) {
					String groupName = name.substring("__GROUP__".length());					
					metaMembers.add(getMetaGroup(groupName));					
				}
				else if (name.startsWith("__ACTION__")) {
					boolean alwaysEnabled = name.startsWith("__ACTION__AE__");					
					String actionName = name.substring((alwaysEnabled?"__ACTION__AE__":"__ACTION__").length());					
					MetaViewAction action = new MetaViewAction(actionName);
					action.setAlwaysEnabled(alwaysEnabled);
					metaMembers.add(action);					
					MetaPropertyView vp = new MetaPropertyView();					
					vp.setPropertyName(action.getName());
					vp.setLabelFormat(MetaPropertyView.NO_LABEL);
					addMetaViewProperty(vp);
				}
				else if (name.equals(NAME_SEPARATOR)) {					
					metaMembers.add(PropertiesSeparator.INSTANCE);
				}				
				else {
					MetaMember member = null;
					try {
						member = getMetaModel().getMetaMember(name);
					}
					catch (ElementNotFoundException ex) {
						member = getMetaViewProperty(name);
					}
					member = modify(member);
					metaMembers.add(member);					
				}
			}
			metaMembers = Collections.unmodifiableCollection(metaMembers);						
		}
		return metaMembers;
	}

	private MetaMember modify(MetaMember member) throws XavaException {
		if (member instanceof MetaProperty) {
			MetaProperty property = (MetaProperty) member;
			MetaProperty newProperty = property.cloneMetaProperty();
			member = newProperty;
			MetaPropertyView propertyView = getMetaPropertyViewFor(property.getName());						
			if (propertyView != null) {				
				String label = propertyView.getLabel();				
				if (!Is.emptyString(label)) {					
					newProperty.setLabel(label);					
				}
				if (!newProperty.isCalculated()) {
					newProperty.setReadOnly(propertyView.isReadOnly());
				}
			}
		}
		String idLabel = isSection()?
				getParent().getId() + "." + member.getName():
				getId() + "." + member.getName();		
		member.setLabelId(idLabel);
		return member;
	}
	
	/**
	 * 
	 * @return Not null
	 */
	public MetaModel getMetaModel() throws XavaException {
		return metaModel;
	}
	
	public void setMetaModel(MetaModel metaModel) throws XavaException {
		this.metaModel = metaModel;
		if (hasSections()) {
			Iterator it = getSections().iterator();
			while (it.hasNext()) {
				MetaView section = (MetaView) it.next();
				section.setMetaModel(metaModel);
			}
		}
	}
	
	public java.lang.String getModelName() throws XavaException {
		return modelName;
	}
	
	public void setModelName(String modelName) throws XavaException {
		this.modelName = modelName;
		Iterator it = getSections().iterator();
		while (it.hasNext()) {
			((MetaView) it.next()).setModelName(this.modelName);
		}
	}
	
	public boolean isAllMembers() {
		return allMembers;
	}
	
	/**
	 * If does not have members to visualize.
	 */
	public boolean isEmpty() throws XavaException {
		return getMetaMembers().size() == 0;
	}
	
	/**
	 * 
	 * @return Not nul, of type <tt>String</tt> and read only.
	 */
	public Collection getMembersNames() throws XavaException {
		if (isAllMembers() && !membersNamesByDefaultCreated && _membersNames.isEmpty()) {						
			crearMembersNamesByDefault();			
		}
		return Collections.unmodifiableCollection(_membersNames);				
	}
	
	/**
	 *
	 * @param membersNames  Names separated by commas or spaces.
	 */
	public void setMembersNames(String membersNames) throws XavaException {
		setMembersNames(membersNames, true);
	}

	/**
	 *
	 * @param membersNames  Names separated by commas or spaces.
	 */
	public void setMembersNamesNotResetSections(String membersNames) throws XavaException {
		setMembersNames(membersNames, false);
	}

	private void setMembersNames(String membersNames, boolean resetSection) throws XavaException {			
		init(resetSection);				
		if (membersNames == null) membersNames = "";
		if (membersNames.trim().equals("*")) {
			setAllMembers(true);
		} else {
			setAllMembers(false);
			StringTokenizer lines = new StringTokenizer(membersNames, ";");
			while (lines.hasMoreTokens()) {
				String line = lines.nextToken();
				StringTokenizer names = new StringTokenizer(line, ",");
				while (names.hasMoreTokens()) {
					String memberName = names.nextToken().trim();
					if (!memberName.equals("")) {
						addMemberName(memberName);
					}
				}
				if (lines.hasMoreTokens()) {
					addMemberName(NAME_SEPARATOR);
				}				
			}
		}
	}
	
	private void init(boolean resetSections ) {
		this._membersNames.clear();
		this.allMetaMembers = null;
		metaProperties = null;
		propertiesNamesThrowOnChange = null;	
		if (resetSections) sections = null;  
		metaMembers = null;		
	}

	private void crearMembersNamesByDefault() throws XavaException {
		MetaModel metaModel = getMetaModel();
		if (metaModel != null) {
			Iterator it = metaModel.getMembersNames().iterator();			
			while (it.hasNext()) {
				String memberName = (String) it.next();
				if (!metaModel.getMetaMember(memberName).isHidden()) {
					addMemberName(memberName);	
					addMemberName(NAME_SEPARATOR);			
				}
			}
		}
		membersNamesByDefaultCreated = true;
	}
			
	public void setAllMembers(boolean newAllMembers) {
		allMembers = newAllMembers;
	}
	
	public boolean hasOnChangeAction(String qualifiedPropertyName) { 
		MetaPropertyView metaVistaPropiedad = getMetaPropertyViewFor(qualifiedPropertyName);		
		if (metaVistaPropiedad == null) return false;
		return metaVistaPropiedad.hasOnChangeAction(); 
	}
		
	public IOnChangePropertyAction createOnChangeAction(String qualifiedPropertyName) throws XavaException {  
		MetaPropertyView metaPropertyView = getMetaPropertyViewFor(qualifiedPropertyName);		
		if (metaPropertyView == null) {
			throw new XavaException("on_change_action_not_found", qualifiedPropertyName);
		}
		return metaPropertyView.createOnChangeAction();
	}	
		
	public boolean hasMetaSearchAction() {
		return this.metaSearchAction != null;
	}
	
	/**
	 * To search the objects of type thas is represents by this view.
	 * <b>Not for the references</b>
	 */
	public MetaSearchAction getMetaSearchAction() {
		if (!hasMetaSearchAction()) return null;
		return metaSearchAction;
	}
	
	public void setMetaSearchAction(MetaSearchAction metaSearchAction) {
		this.metaSearchAction = metaSearchAction;
	}
	
	/**
	 * @return Null if not found
	 */
	public MetaDescriptionsList getMetaDescriptionList(MetaReference r) throws XavaException {
		if (!hasMetaReferenceViewFor(r)) return null;
		MetaReferenceView metaReferenceView = getMetaReferenceView(r);
		MetaDescriptionsList metaDescriptionsList = metaReferenceView.getMetaDescriptionsList();
		if (metaDescriptionsList==null) return null;
		if (Is.emptyString(metaDescriptionsList.getDescriptionPropertyName()) &&
				Is.emptyString(metaDescriptionsList.getDescriptionPropertiesNames())
				) {			
			calculateDefaultValuesForDescriptionsList(r, metaDescriptionsList);  
		}
		return metaDescriptionsList;
	}

	private void calculateDefaultValuesForDescriptionsList(MetaReference r, MetaDescriptionsList metaDescriptionsList) throws XavaException {
		Collection properties = r.getMetaModelReferenced().getPropertiesNames();
		if (properties.contains("descripcion")) metaDescriptionsList.setDescriptionPropertyName("descripcion");
		else if (properties.contains("description")) metaDescriptionsList.setDescriptionPropertyName("description");
		else if (properties.contains("nombre")) metaDescriptionsList.setDescriptionPropertyName("nombre");
		else if (properties.contains("name")) metaDescriptionsList.setDescriptionPropertyName("name");
		else throw new XavaException("description_property_required");
	}
	
	public MetaDescriptionsList createMetaDescriptionList(MetaReference r) throws XavaException {
		MetaDescriptionsList metaDescriptionsList = new MetaDescriptionsList();
		calculateDefaultValuesForDescriptionsList(r, metaDescriptionsList);  
		return metaDescriptionsList;
	}
		
	/**
	 * @return of type <tt>MetaDescriptionsList</tt>
	 */
	public Collection getMetaDescriptionsLists() {
		Collection metaDescriptionsLists = new ArrayList();
		if (metaViewsReferences == null) return metaDescriptionsLists;
		Iterator it = metaViewsReferences.values().iterator();
		while (it.hasNext()) {
			MetaReferenceView referenceView = (MetaReferenceView) it.next();
			MetaDescriptionsList descriptionsList = referenceView.getMetaDescriptionsList(); 
			if (descriptionsList != null) {
				metaDescriptionsLists.add(descriptionsList);
			}
		}
		return metaDescriptionsLists;  
	}
	
	public MetaView getMetaView(MetaReference r) throws XavaException {
		MetaView result = null;		
		if (hasMetaReferenceViewFor(r)) {			
			MetaReferenceView metaReferenceView = getMetaReferenceView(r);
			String viewName = metaReferenceView.getViewName();
			if (Is.emptyString(viewName)) {
				result = r.getMetaModelReferenced().getMetaViewByDefault();				
			}
			else {
				result = r.getMetaModelReferenced().getMetaView(viewName);				 
			}
			try {
				result = (MetaView) result.clone();
			}
			catch (CloneNotSupportedException e) {
				throw new XavaException("meta_view_reference_error_no_clone");				
			}
			result.setMetaSearchAction(metaReferenceView.getMetaSearchAction());	
			result.setFrame(metaReferenceView.isFrame());
			MetaDescriptionsList metaDescriptionsList = getMetaDescriptionList(r);			
			if (metaDescriptionsList != null) {
				result.removeMembers();
				Iterator itKeys = r.getMetaModelReferenced().getKeyPropertiesNames().iterator();
				while (itKeys.hasNext()) {
					result.addMemberName((String)itKeys.next());
				}
			}
		}
		else {
			result = r.getMetaModelReferenced().getMetaViewByDefault();			
		}
		result.setLabel(r.getLabel());
		return result;
	}
	
	private void removeMembers() {
		if (_membersNames != null) _membersNames.clear();
		allMetaMembers = null;
		metaMembers = null;
		metaProperties = null;				
	}

	private boolean hasMetaReferenceViewFor(MetaReference r) {				
		if (metaViewsReferences == null) return false;		
		return metaViewsReferences.containsKey(r.getName());
	}
	
	/**
	 * @return Null if not found
	 */
	public MetaReferenceView getMetaReferenceView(MetaReference r) {
		return getMetaReferenceViewFor(r.getName());
	}
	
	private MetaPropertyView getMetaPropertyViewFor(String qualifiedPropertyName) { 		
		if (metaViewsProperties == null) return null;
		return (MetaPropertyView) metaViewsProperties.get(qualifiedPropertyName);
	}
	
	public MetaReferenceView getMetaReferenceViewFor(String ref) {
		if (metaViewsReferences == null) {
			return null;
		}
		return (MetaReferenceView) metaViewsReferences.get(ref);
	}
	
	
	
	
	public List getSections() throws XavaException {
		return sections == null?Collections.EMPTY_LIST:sections;		
	}
	
	/**
	 * 
	 * @return The added section
	 */
	public MetaView addSection(String name, String label, String members, boolean alignedByColumns) throws XavaException {
		if (sections == null) sections = new ArrayList();		
		MetaView section = new MetaView();
		section.setSection(true);
		section.setParent(this);
		section.setName(name);
		section.parentName = getName();
		section.setLabel(label);
		section.setMembersNames(members);
		section.setAlignedByColumns(alignedByColumns);
		promote(section);		
		sections.add(section);		
		return section; 
	}
	
	public void addMetaGroup(String name, String label, String members, boolean alignedByColumns) throws XavaException {
		if (metaGroups == null) metaGroups = new HashMap();
		MetaGroup metaGroup = new MetaGroup(this);
		metaGroup.setName(name); 		
		metaGroup.setLabel(label);
		metaGroup.setMembersNames(members);
		metaGroup.setAlignedByColumns(alignedByColumns);
		metaGroups.put(name, metaGroup);		
	}
		
	private MetaGroup getMetaGroup(String name) throws XavaException {
		if (metaGroups == null) {
			throw new ElementNotFoundException("group_not_found_no_groups", name);
		}
		Object result = metaGroups.get(name);
		if (result == null) { 
			throw new ElementNotFoundException("group_not_found", name);
		}
		return (MetaGroup) result;
	}
	
	private void promote(MetaView view)
		throws XavaException {
		view.setMetaSearchAction(this.getMetaSearchAction());
		view.setMetaModel(this.getMetaModel());
				
		view.setModelName(this.getModelName());
		
		if (this.metaViewsProperties == null) {
			this.metaViewsProperties = new HashMap();
		}
		view.metaViewsProperties = this.metaViewsProperties;
		if (this.metaViewsReferences == null) {
			this.metaViewsReferences = new HashMap();
		}
		view.metaViewsReferences = this.metaViewsReferences;
		if (this.metaViewsCollections == null) {
			this.metaViewsCollections = new HashMap();
		}
		view.metaViewsCollections = this.metaViewsCollections;
		view.metaViewProperties = this.metaViewProperties;
		if (metaGroups == null) metaGroups = new HashMap(); 
		view.metaGroups = this.metaGroups; 
	}
	
	public boolean hasSections() {		
		return sections != null;		
	}
		
	public void clearSections() {
		sections =  null;
	}			
		
	public String getMediatorClassName() throws XavaException {		
		return mediatorClassName;
	}

	public void setMediatorClassName(String mediatorClassName) {
		this.mediatorClassName = mediatorClassName;
	}
	
	/**
	 * @return Null if not found
	 */
	public MetaCollectionView getMetaCollectionView(String collectionName) {		
		if (metaViewsCollections == null) return null;
		return (MetaCollectionView) metaViewsCollections.get(collectionName);				
	}
	
	public boolean isFrame() {
		return frame;
	}

	public void setFrame(boolean frame) {
		this.frame = frame;
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
					MetaPropertyView propertyView = (MetaPropertyView) it.next();
					if (propertyView.hasOnChangeAction()) {
						propertiesNamesThrowOnChange.add(propertyView.getPropertyName());
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

	public Collection getActionsNamesForProperty(MetaProperty p, boolean editable) {
		MetaPropertyView metaPropertyView = getMetaPropertyViewFor(p.getName());
		if (metaPropertyView == null) return Collections.EMPTY_LIST;
		return editable?metaPropertyView.getActionsNames():metaPropertyView.getAlwaysEnabledActionsNames();
	}
			
	public Collection getActionsNamesForReference(MetaReference ref, boolean editable) {
		MetaReferenceView metaReferenceView = getMetaReferenceViewFor(ref.getName());
		if (metaReferenceView == null) return Collections.EMPTY_LIST;
		return editable?metaReferenceView.getActionsNames():metaReferenceView.getAlwaysEnabledActionsNames();
	}
	

	public int getLabelFormatForProperty(MetaProperty p) {
		MetaPropertyView metaPropertyView = getMetaPropertyViewFor(p.getName());
		if (metaPropertyView == null) return MetaPropertyView.NORMAL_LABEL;
		return metaPropertyView.getLabelFormat();
	}
	
	public int getLabelFormatForReference(MetaReference ref) {
		MetaReferenceView metaReferenceView = getMetaReferenceViewFor(ref.getName());
		if (metaReferenceView == null) return MetaPropertyView.NORMAL_LABEL;
		MetaDescriptionsList descriptionsList = metaReferenceView.getMetaDescriptionsList(); 
		if (descriptionsList == null) return MetaPropertyView.NORMAL_LABEL;
		return descriptionsList.getLabelFormat();		
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

	public boolean isAlignedByColumns() {
		return alignedByColumns;
	}

	public void setAlignedByColumns(boolean alignedByColumns) {
		this.alignedByColumns = alignedByColumns;
	}
}