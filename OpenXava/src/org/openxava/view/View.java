package org.openxava.view;

import java.lang.reflect.*;
import java.util.*;

import javax.ejb.*;
import javax.servlet.http.*;

import org.openxava.actions.*;
import org.openxava.calculators.*;
import org.openxava.component.*;
import org.openxava.controller.*;
import org.openxava.mapping.*;
import org.openxava.model.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;
import org.openxava.view.meta.*;
import org.openxava.web.*;

/**
 * Session object to manage a view based in maps,
 * hence suitable for OpenXava
 * 
 * @author Javier Paniza
 */

public class View implements java.io.Serializable {
	
	private static final long serialVersionUID = -7582669617830655121L;

	private final static int [] EMPTY_SELECTED = new int[0];
	
	private String editCollectionElementAction;
	private boolean focusForward;
	private String idFocusProperty;
	private Map membersNamesWithHidden;
	private Map groupsViews;
	private Collection membersNamesInGroup;
	private Map collectionMemberNames;
	private List collectionValues;
	private static int nextOid = 0;	
	private int collectionEditingRow = -1;
	private boolean searchingObject;
	private Collection memberNamesWithoutSeccions;
	private View parent;	
	private Collection metaPropertiesList;
	private boolean knowIfDisplayDetailInCollection;
	private boolean displayDetailInCollection;
	private String lastPropertyKeyName;
	private Map subviews;
	private Set hidden;		
	private int oid;
	private List metaProperties;
	private Collection metaPropertiesQualified;
	private Map calculatedPropertiesNames;
	private Map mapStereotypesProperties;
	private Map membersNames;
	private MetaModel metaModel;
	private Collection metaMembers;
	private Map values;  
	private MetaView metaView;
	private boolean keyEditable = true;
	private boolean editable = true;
	private boolean representsAggregate;
	private boolean representsEntityReference;
	private boolean representsCollection;
	private String modelName;
	private String viewName;
	private boolean subview;
	private boolean section;	
	private boolean group;
	private boolean collectionDetailVisible = false;
	private Messages messages; 
	private Messages errors;
	private Set noEditablesMemberNames;
	private transient HttpServletRequest request;
	private Collection depends;
	private boolean hasToSearchOnChangeIfSubview = true;
	private View [] sectionsViews;
	private int activeSection;
	private String memberName;
	private boolean collectionMembersEditables;
	private boolean collectionEditable;
	private Collection actionsNamesDetail;
	private Collection actionsNamesList;
	private int [] listSelected;
	private boolean readOnly; // Always not editable, marked from xml
	private boolean onlyThrowsOnChange; 

	private Collection metaPropertiesIncludingSections;
	private Collection metaPropertiesIncludingGroups;

	private Collection metaMembersIncludingHiddenKey;

	private String propertyPrefix;

	private Map labels;

	private Collection executedActions;	

	private boolean registeringExecutedActions = false;
		
	public View() {
		oid = nextOid++;
	}
	
	public Collection getMetaMembers() throws XavaException {
		if (metaMembers == null) {
			metaMembers = createMetaMembers(false); 
		}				
		return metaMembers;		
	}
	
	private Collection getMetaMembersIncludingHiddenKey() throws XavaException {
		if (metaMembersIncludingHiddenKey == null) {
			metaMembersIncludingHiddenKey = createMetaMembers(false);
			if (!isRepresentsAggregate()) { 
				for (Iterator it=getMetaModel().getMetaPropertiesKey().iterator(); it.hasNext(); ) {
					MetaProperty p = (MetaProperty) it.next();
					if (p.isHidden()) {
						metaMembersIncludingHiddenKey.add(p);
					}
				}
			}
		}
		return metaMembersIncludingHiddenKey;		
	}	
			
	private Collection createMetaMembers(boolean hiddenIncluded) throws XavaException {		 
		Collection metaMembers = new ArrayList(getMetaView().getMetaMembers());			
		if (isRepresentsAggregate()) {
			// This is for eluding recursive references				
			String parentName = getMetaModel().getMetaModelContainer().getName();
			Collection filtered = new ArrayList();
			Iterator it = metaMembers.iterator();
			while (it.hasNext()) {
				MetaMember m = (MetaMember) it.next();
				if (m instanceof MetaReference) {						
					MetaReference ref = (MetaReference) m;
					if (!parentName.equals(ref.getReferencedModelName())) {
						filtered.add(m);						
					}
				}
				else {
					filtered.add(m);
				}
			}
			metaMembers = filtered;
		}		
		if (!hiddenIncluded && hidden != null) removeHidden(metaMembers);
		removeOverlapedProperties(metaMembers);
		return metaMembers;	
	}
	
	private void removeHidden(Collection metaMembers) {
		Iterator it = metaMembers.iterator();		
		while (it.hasNext()) {
			MetaMember m = (MetaMember) it.next();
			if (hidden.contains(m.getName())) it.remove();			
		}
	}
	
	private void removeOverlapedProperties(Collection metaMembers) throws XavaException { 		
		if (!(representsEntityReference && !isRepresentsCollection())) return; 
		if (getParent().isRepresentsAggregate()) return; // At momment references to entity in aggregage can not be overlapped
		ModelMapping parentMapping = getParent().getMetaModel().getMapping();
		String referenceName = getMemberName();
		Iterator it = metaMembers.iterator();		
		while (it.hasNext()) {
			Object m = it.next();
			if (!(m instanceof MetaProperty)) continue;
			MetaProperty p = (MetaProperty) m;			
			if (!p.isKey()) continue; // In references only key is matter			
			if (parentMapping.isReferenceOverlappingWithSomeProperty(referenceName, p.getName())) {
				it.remove();
			}			
		}		
	}
	

	public void setMetaMembers(Collection metaMembers) {		
		if (Is.equal(this.metaMembers, metaMembers)) return;		
		this.metaMembers = metaMembers;
		this.membersNames = null;
		this.collectionMemberNames = null;
		this.metaProperties = null;
		this.calculatedPropertiesNames = null;
		this.metaPropertiesIncludingSections = null;
		this.metaPropertiesIncludingGroups = null;
		this.metaPropertiesQualified = null;
		this.mapStereotypesProperties = null;
		this.lastPropertyKeyName = null;
		this.values = null;
		this.subviews = null;
	}
	
	private MetaView getMetaView() throws XavaException {
		if (metaView == null) {			
			if (Is.emptyString(getViewName())) {
				metaView = getMetaModel().getMetaViewByDefault();				
			}
			else {
				metaView = getMetaModel().getMetaView(getViewName());				
			}
		}
		return metaView;
	}
	private void setMetaView(MetaView metaView) {
		this.metaView = metaView;
	}
	
	public MetaModel getMetaModel() throws XavaException {
		if (metaModel == null) {
			String modelName = getModelName();
			int idx = modelName.indexOf('.');
			if (idx < 0) {
				metaModel = MetaComponent.get(modelName).getMetaEntity();
			} 
			else {
				String componentName = modelName.substring(0, idx);
				idx = modelName.lastIndexOf('.'); // We get the last one in case we have MyComponent.MyAggregate.MyNestedAggregate, thus we search MyNestedAggregate within MyComponent 
				String aggregateName = modelName.substring(idx+1);
				metaModel = MetaComponent.get(componentName).getMetaAggregate(aggregateName);
			}
		}
		return metaModel;
	}
	
	public Map getValues() throws XavaException {		
		return getValues(false);
	}
	
	public Map getAllValues() throws XavaException {
		return getValues(true);
	}
		
	private Map getValues(boolean all) throws XavaException {		
		Map hiddenKey = null;
		if (values == null) { 
			values = new HashMap(); 			
		}
		else {
			hiddenKey = getHiddenKey(values);
		}		
		if (hasSubviews()) {
			Iterator it = getSubviews().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry en = (Map.Entry) it.next();
				View v = (View) en.getValue();				
				if (v.isRepresentsCollection()) continue;
				if (all || v.isRepresentsAggregate()) {
					values.put(en.getKey(), v.getValues(all));					
				}
				else {					
					values.put(en.getKey(), v.getKeyValues());
				}				
			}
		}
		
		if (hasGroups()) {
			Iterator it = getGroupsViews().values().iterator();
			while (it.hasNext()) {
				View v = (View) it.next();
				values.putAll(v.getValues(all));
			}			
		}
		
		if (hasSections()) {
			int quantity = getSections().size();
			for (int i=0; i<quantity; i++) {												
				values.putAll(getSectionView(i).getValues(all));
			}
		}									
		
		if (hiddenKey != null) {
			values.putAll(hiddenKey);
		}
		
		return values;
	}
	
	private Map getHiddenKey(Map keyValues) throws XavaException {
		Map result = null;
		for (Iterator it=keyValues.keySet().iterator(); it.hasNext(); ) {
			String property = (String) it.next();
			if (getMetaModel().isHiddenKey(property)) {
				if (result == null) result = new HashMap();
				result.put(property, keyValues.get(property));
			}
		}
		return result;
	}

	private boolean hasGroups() {		
		return groupsViews != null && !groupsViews.isEmpty();
	}

	public void addValues(Map map) throws XavaException {
		map = map==null?Collections.EMPTY_MAP:map; 		
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry en = (Map.Entry) it.next(); 
			String key = (String) en.getKey();
			Object value = en.getValue();
			int idx = key.indexOf('.');
			if (idx < 0) {
				setValue(key, value);
			}
			else {
				String subviewName = key.substring(0, idx);
				String member = key.substring(idx+1);				
				getSubview(subviewName).setValue(member, value); 					
			}
		}							
						
		if (isSubview() && !isRepresentsAggregate()) { // to throwing code change on search in subview						
			try {				
				searchingObject = true; // to avoid the searching
				propertyChanged(getLastPropertyKeyName());				
			}
			finally {				
				searchingObject = false;
			}
		}					
	}
	
	public void setValues(Map map) throws XavaException {		
		if (values == null) values = new HashMap();
		else values.clear();				
		addValues(map);								
	}
	

	/**
	 * Set the values and execute the on-change actions associated to
	 * the assigned properties. <p>
	 */
	public void setValuesExecutingOnChangeActions(Map values) throws XavaException {
		if (getParent() == null) {
			getRoot().registeringExecutedActions = true;
		}		
		setOnlyThrowsOnChange(true);
		try {
			setValuesNotifying(values);
		}
		finally {
			setOnlyThrowsOnChange(false);
			if (getParent() == null) {
				getRoot().registeringExecutedActions = false;		
				resetExecutedActions();
			}			
		}
	}
	
	/**
	 * Set the values and throws are events associated to the changed values. 
	 */
	public void setValuesNotifying(Map values) throws XavaException {		
		setValues(values); 
		Iterator it = values.keySet().iterator();
		String key = null;
		String qualifier = null;
		if (isSubview()) {
			qualifier = getMemberName() + ".";
		}
		while (it.hasNext()) {
			String property = (String) it.next();
			if (property.equals(getLastPropertyKeyName())) {
				key = property;
			}
			else {
				if (qualifier == null) {
					propertyChanged(property);	
				}
				else {
					getParent().propertyChanged(qualifier + property);
				}				
			}							
		}
		if (key != null) {				
			if (qualifier == null) {
				propertyChanged(key);	
			}
			else {
				getParent().propertyChanged(qualifier + key);
			}							
		}		
	}
	
	/**
	 * @param recalculatingValues If true reobtain values from views, groups and sections.
	 */
	private Object getValue(String name, boolean recalculatingValues) throws XavaException {
		int idx = name.indexOf('.');		
		if (idx < 0) { 						
			if (!getMemberNamesWithoutSeccions().contains(name) && (hidden == null || !hidden.contains(name)) && !getMetaModel().getKeyPropertiesNames().contains(name)) {
				return getValueInSections(name, recalculatingValues);
			}
			else {				
				if (hasSubview(name)) { 															
					View subview = getSubview(name);
					if (!subview.isRepresentsCollection()) {						
						return subview.getValues();
					}
					else {						
						return subview.getCollectionValues();
					}
				}
				else {															
					if (values == null && !recalculatingValues) return null;
					return recalculatingValues?getValues().get(name):values.get(name);
				}				 							 								
			} 			
		} 
		else {						
			String subview = name.substring(0, idx);			
			String member = name.substring(idx+1);			
			return getSubview(subview).getValue(member, recalculatingValues);
		}		
	}
	
	public Object getValue(String name) throws XavaException {
		return getValue(name, true);
	}
	
	public int getValueInt(String name) throws XavaException {
		Number v = (Number) getValue(name);			
		return v==null?0:v.intValue();
	}
	
	public String getValueString(String name) throws XavaException {
		Object v = getValue(name);
		return v == null?"":v.toString();						
	}
	
	public View getSubview(String name) throws XavaException {		
		View subview = (View) getSubviews().get(name);		
		if (subview == null) {
			subview = findSubviewInSection(name);
			if (subview == null) {
				subview = findSubviewInGroup(name);
				if (subview == null) {					
					throw new ElementNotFoundException("subview_not_found", name, getModelName());
				}							
			}			
		}				
		return subview;
	}
	
	public View getGroupView(String name) throws XavaException {
		View subview = (View) getGroupsViews().get(name);
		if (subview == null) {			
			groupsViews = null; // to force reload the group views
			subviews = null;
			subview = (View) getGroupsViews().get(name);
			if (subview == null) {
				throw new ElementNotFoundException("subview_group_no_found", name, getModelName());
			}
		}				
		return subview;
	}
		
	private View findSubviewInSection(String name) throws XavaException {
		if (!hasSections()) return null;
		int quantity = getSections().size();
		for (int i=0; i<quantity; i++) {
			View sectionView = getSectionView(i); 
			View subview = (View) sectionView.getSubviews().get(name);
			if (subview == null) {
				subview = sectionView.findSubviewInGroup(name);
				if (subview != null)	return subview;
			} 
			else {
				return subview;
			}		
		}
		return null;
	}
	
	private View findSubviewInGroup(String name) throws XavaException {
		if (!hasGroups()) return null;
		Iterator it = getGroupsViews().values().iterator();
		while (it.hasNext()) {
			View groupView = (View) it.next();			
			View subview = (View) groupView.getSubviews().get(name);
			if (subview != null) return subview;
			if (groupView.hasGroups()) {
				subview = groupView.findSubviewInGroup(name);
				if (subview != null) return subview;
			}
		}
		return null;
	}
	

	private void createAndAddSubview(MetaMember member) throws XavaException {
		if (!(member instanceof MetaReference || member instanceof MetaCollection || member instanceof MetaGroup)) return;				
		View newView = new View();
		newView.setSubview(true);
		newView.setParent(this);
		
		MetaReference ref = null;
		if (member instanceof MetaReference) {
			ref = (MetaReference) member;
		}
		else if (member instanceof MetaCollection) {
			ref = ((MetaCollection) member).getMetaReference();
			newView.setRepresentsCollection(true);						
		}
		else { // MetaGroup			
			newView.setModelName(getModelName());			 
			MetaView metaView = ((MetaGroup) member).getMetaView();
			newView.setMetaView(metaView);
			newView.setGroup(true);
			getGroupsViews().put(member.getName(), newView);						
			return;			
		}		
		if (ref.isAggregate()) {
			newView.setModelName(getModelName() + "." + ref.getReferencedModelName());
			newView.setRepresentsAggregate(true);
			newView.setEditable(isEditable()); 
		}
		else {
			newView.setModelName(ref.getReferencedModelName());
			newView.setRepresentsEntityReference(true);
			newView.setKeyEditable(isEditable());
			newView.setEditable(false);	
		}
		newView.setMetaView(getMetaView().getMetaView(ref));
		if (newView.isRepresentsCollection()) {
			MetaCollectionView metaCollectionView = getMetaView().getMetaCollectionView(member.getName());
			if (metaCollectionView != null) {
				Collection propertiesListNames = metaCollectionView.getPropertiesListNames();
				if (!propertiesListNames.isEmpty()) {
					newView.setMetaPropertiesList(namesToMetaProperties(newView, propertiesListNames));
				}
				Collection actionsDetailNames = metaCollectionView.getActionsDetailNames();
				if (!actionsDetailNames.isEmpty()) {
					newView.setActionsNamesDetail(actionsDetailNames);
				}
				Collection actionsListNames = metaCollectionView.getActionsListNames();
				if (!actionsListNames.isEmpty()) {
					newView.setActionsNamesList(actionsListNames);
				}
				newView.setEditCollectionElementAction(metaCollectionView.getEditActionName());
				newView.setKeyEditable(!metaCollectionView.isReadOnly());
				newView.setEditable(!metaCollectionView.isReadOnly());				
				newView.setCollectionEditable(!metaCollectionView.isReadOnly() && !metaCollectionView.isEditOnly());
				newView.setCollectionMembersEditables(metaCollectionView.isEditOnly());
				
				newView.setViewName(metaCollectionView.getViewName()); 								
			}
			else {
				newView.setEditable(true);
				newView.setCollectionEditable(true);
				newView.setCollectionMembersEditables(true);
			}
		}
		else {
			MetaReferenceView metaReferenceView = getMetaView().getMetaReferenceView(ref);
			if (metaReferenceView != null) {
				newView.setReadOnly(metaReferenceView.isReadOnly());
			}
		}
		newView.setMemberName(member.getName());		
		subviews.put(member.getName(), newView);
	} 
	 
		
	private Map getGroupsViews() throws XavaException {
		if (groupsViews == null) {
			groupsViews = new HashMap();
			getSubviews(); // in order to start the process that create subviews and groups
		}
		return groupsViews;
	}

	private Collection namesToMetaProperties(View view, Collection names) throws XavaException {
		Collection metas = new ArrayList();
		Iterator it = names.iterator();
		while (it.hasNext()) {
			String name = (String) it.next();
			MetaProperty metaProperty = view.getMetaModel().getMetaProperty(name);			
			if (name.indexOf('.') >= 0) { 
				metaProperty = metaProperty.cloneMetaProperty();
				metaProperty.setName(name);				
			}				
			metas.add(metaProperty);		
		}
		return metas;
	}

	private boolean hasSubviews() {		
		return subviews != null && !subviews.isEmpty();		
	}
	
	private boolean hasSubview(String name) throws XavaException {
		if (!hasSubviews()) return false;		
		return getSubviews().containsKey(name); 
	}
	
	private Map getSubviews() throws XavaException {
		if (getModelName() == null) return Collections.EMPTY_MAP;
		if (subviews == null) {  
			subviews = new HashMap();					
			Iterator it = getMetaMembers().iterator();
			while (it.hasNext()) {
				MetaMember member = (MetaMember) it.next();								
				createAndAddSubview(member);
			}								
		}
		return subviews;
	}
	
	/**
	 * Set the value and notifies the property change, recalculating all dependent properties. 
	 * 
	 * @param name Can be qualified	 
	 */
	public void setValueNotifying(String name, Object value) throws ElementNotFoundException, XavaException {
		setValue(name, value);			
		propertyChanged(name);		
	}
	
	/**
	 * 
	 * @param name Can be qualified	 
	 */
	public void setValue(String name, Object value) throws XavaException {
		trySetValue(name, value);		
	}	
				
	/**
	 * 
	 * @param name Can be qualified	 
	 */
	public boolean trySetValue(String name, Object value) throws XavaException {
		int idx = name.indexOf('.');		
		if (idx < 0) {
			if (getMembersNamesInGroup().contains(name)) {
				trySetValueInGroups(name, value);				
			}																	
			else if (!getMemberNamesWithoutSeccions().contains(name) && !getMetaModel().getKeyPropertiesNames().contains(name) && !getMetaModel().getKeyReferencesNames().contains(name)) {				
				if (!setValueInSections(name, value)) { 
					return false;
				}
			}
			else {								
				if (hasSubview(name)) {					
					View subview = getSubview(name);
					if (!subview.isRepresentsCollection()) {
						subview.setValues((Map)value);										
					}
					else {						
						throw new XavaException("no_set_collection_value_error", name);
					}					
				}
				else { 										
					if (values == null) values = new HashMap();
					values.put(name, value);					
				}				 							 								
			} 
		} 
		else {			
			String subview = name.substring(0, idx);
			String member = name.substring(idx+1);
			getSubview(subview).setValue(member, value);		
		}		
		return true;
	}
	
	private Collection getMembersNamesInGroup() throws XavaException { 
		if (membersNamesInGroup == null) {
			membersNamesInGroup = new ArrayList();		
			Iterator it = getGroupsViews().values().iterator();		
			while (it.hasNext()) {
				View subview = (View) it.next();																
				membersNamesInGroup.addAll(subview.getMembersNamesWithHidden().keySet());
			}		
		}
		return membersNamesInGroup;
	}

	private void trySetValueInGroups(String name, Object value) throws XavaException {		
		Iterator it = getGroupsViews().values().iterator();
		while (it.hasNext()) {
			View subview = (View) it.next();			
			boolean attempt = subview.trySetValue(name, value);			
		}				
	}
	
	private boolean setValueInSections(String name, Object value) throws XavaException {
		if (!hasSections()) return false;
		int quantity = getSections().size();		
		for (int i = 0; i < quantity; i++) {			
			if (getSectionView(i).trySetValue(name, value))	return true;			 				
		}		
		return false;
	}
	
	public void setSectionEditable(String sectionName, boolean editable) throws XavaException {
		getSection(sectionName).setEditable(editable);
	}
	public boolean isSectionEditable(String sectionName) throws XavaException {
		return getSection(sectionName).isEditable();
	} 
	
	private View getSection(String sectionName) throws XavaException {
		if (!hasSections()) {
			throw new ElementNotFoundException("no_sections_error");
		}
		int quantity = getSections().size();
		for (int i = 0; i < quantity; i++) {							
			if (getSectionView(i).getMetaView().getName().equals(sectionName)) {
				return getSectionView(i);				
			} 				
		}		
		throw new ElementNotFoundException("section_not_found", sectionName);
	}
		
	private Object getValueInSections(String name, boolean recalculatingValues) throws XavaException { 
		if (!hasSections()) return null;
		int quantity = getSections().size();
		for (int i = 0; i < quantity; i++) {			
			Object value = getSectionView(i).getValue(name, recalculatingValues);
			if (value != null) return value;
		}
		return null;
	}
	
	/**
	 * Excludes those values that are null, zero or empty string.
	 */
	public Map getKeyValuesWithValue() throws XavaException {
		Iterator it = getValues().keySet().iterator();
		Map result = new HashMap();
		while (it.hasNext()) {
			String name = (String) it.next();			
			if (getMetaModel().isKey(name)) {
				Object value = getValues().get(name);
				if (isEmptyValue(value)) continue;
				result.put(name, value);
			}			
		}				
		return result;
	}
	
	private boolean isEmptyValue(Object value) {
		if (value == null) return true;
		if (value instanceof Number && ((Number) value).intValue() == 0) return true;
		if (value instanceof String && Is.emptyString((String) value)) return true;
		return false;		
	}
	
	public Map getKeyValues() throws XavaException {
		Map values = getValues();				
		Iterator it = values.keySet().iterator();
		Map result = new HashMap();
		while (it.hasNext()) {
			String name = (String) it.next();		
			if (getMetaModel().isKey(name)) {
				result.put(name, values.get(name));
			}			
		}		

		if (getParent() != null && !getParent().isRepresentsAggregate()) {			
			// At momment reference to entity within aggregate can not be part of key
			if (isRepresentsEntityReference() && !isRepresentsCollection()) {				
				ModelMapping mapping = getParent().getMetaModel().getMapping();
				if (mapping.isReferenceOverlappingWithSomeProperty(getMemberName())) {					
					Iterator itProperties = mapping.getOverlappingPropertiesOfReference(getMemberName()).iterator();					
					while (itProperties.hasNext()) {
						String property = (String) itProperties.next();						
						String overlappedProperty = mapping.getOverlappingPropertyForReference(getMemberName(), property);						
						result.put(property, getParent().getValue(overlappedProperty, false));						
					}
					
				}								
			}		
		}

		return result;
	}
	
	public Map getMembersNamesWithHidden() throws XavaException {
		if (membersNamesWithHidden == null) {
			membersNamesWithHidden = createMembersNames(true);
		}
		return membersNamesWithHidden;		
	}
		
	public Map getMembersNames() throws XavaException {
		if (membersNames == null) {
			membersNames = createMembersNames(false);
		}
		return membersNames;		
	}
	
	public Map getCalculatedPropertiesNames() throws XavaException {
		if (calculatedPropertiesNames == null) { 
			calculatedPropertiesNames = createCalculatedPropertiesNames();
		}
		return calculatedPropertiesNames;
	}
	
	private Map createCalculatedPropertiesNames() throws XavaException {
		Map memberNames = new HashMap();
		Iterator it = createMetaMembers(false).iterator();
		while (it.hasNext()) {
			MetaMember m = (MetaMember) it.next();								
			if (m instanceof MetaProperty && !m.equals(PropertiesSeparator.INSTANCE)) {
				if (((MetaProperty)m).isCalculated()) {
					memberNames.put(m.getName(), null);
				}
			}
			else if (m instanceof MetaReference) {
				Map names = getSubview(m.getName()).createCalculatedPropertiesNames();
				if (!names.isEmpty()) memberNames.put(m.getName(), names);				
			}
			else if (m instanceof MetaGroup) {
				Map names = getGroupView(m.getName()).createCalculatedPropertiesNames();
				if (!names.isEmpty()) memberNames.putAll(names);
			}
		}			
		if (hasSections()) {
			int quantity = getSections().size();
			for (int i = 0; i < quantity; i++) {
				Map names = getSectionView(i).createCalculatedPropertiesNames();
				if (!names.isEmpty()) memberNames.putAll(names);
			}
		}			
		return memberNames;	
	}
	
			
	private Map createMembersNames(boolean hiddenIncluded) throws XavaException {
		Map membersNames = new HashMap();
		Iterator it = createMetaMembers(hiddenIncluded).iterator();
		while (it.hasNext()) {
			MetaMember m = (MetaMember) it.next();								
			if (m instanceof MetaProperty && !m.equals(PropertiesSeparator.INSTANCE)) {										
				membersNames.put(m.getName(), null);
			}
			else if (m instanceof MetaReference) {  
				membersNames.put(m.getName(), getSubview(m.getName()).createMembersNames(hiddenIncluded));									
			}
			else if (m instanceof MetaCollection) { 					
				// The collections are obtained from the collection view, this allows to load collections on demmand.
			}				
			else if (m instanceof MetaGroup) { 
				membersNames.putAll(getGroupView(m.getName()).createMembersNames(hiddenIncluded));
			}
		}			
		if (hasSections()) {
			int quaintity = getSections().size();
			for (int i = 0; i < quaintity; i++) {
				membersNames.putAll(getSectionView(i).createMembersNames(hiddenIncluded));
			}
		}			
		return membersNames;	
	}
	
	public List getCollectionValues() throws XavaException { 
		if (!isRepresentsCollection()) return Collections.EMPTY_LIST;
		if (collectionValues == null) {
			Map membersNames = new HashMap();
			membersNames.put(getMemberName(), getCollectionMemberNames());
			View root = getRoot();			
			try {							
				Map values = MapFacade.getValues(getParent().getModelName(), getParent().getKeyValues(), membersNames);
				collectionValues = (List) values.get(getMemberName());				
			}
			catch (ObjectNotFoundException ex) { // New one is creating				
				collectionValues = Collections.EMPTY_LIST; 								
			}
			catch (Exception ex) {
				ex.printStackTrace();
				getErrors().add("collection_error", getMemberName());
				return Collections.EMPTY_LIST;
			}
		}
		return collectionValues;
	}
	
	public void resetCollectionValues() {
		collectionValues = null;
	}
	
	public boolean isDetailMemberInCollection() throws XavaException {
		return getCollectionMemberNames().keySet().containsAll(getMembersNames().keySet());
	}
	
	private Map getCollectionMemberNames() throws XavaException {
		if (collectionMemberNames == null) {   		
			collectionMemberNames = new HashMap();
			Iterator it = getMetaPropertiesList().iterator();
			while (it.hasNext()) {
				MetaProperty pr = (MetaProperty) it.next();
				String propertyName = pr.getName();
				addQualifiedMemberToMap(propertyName, collectionMemberNames);
			}		
		}	
		return collectionMemberNames;
	}
	
	private void addQualifiedMemberToMap(String propertyName, Map collectionMemberNames) { 
		int idx = propertyName.indexOf('.');
		if (idx < 0) {
			collectionMemberNames.put(propertyName, null);				
		}
		else {
			String referenceName = propertyName.substring(0, idx);
			String referencePropertyName = propertyName.substring(idx+1);			
			Map ref = (Map) collectionMemberNames.get(referenceName);
			if (ref == null) {
				ref = new HashMap();
				collectionMemberNames.put(referenceName, ref);
			}
			if (referencePropertyName.indexOf('.') < 0) {
				ref.put(referencePropertyName, null);
			}
			else {
				addQualifiedMemberToMap(referencePropertyName, ref);
			}												
		}			
	}

	private Collection getMemberNamesWithoutSeccions() throws XavaException { 
		if (memberNamesWithoutSeccions==null) { 	
			Iterator it = createMetaMembers(true).iterator();						
			memberNamesWithoutSeccions = new ArrayList();
			while (it.hasNext()) {
				MetaMember m = (MetaMember) it.next();
				if (m instanceof MetaProperty && !m.equals(PropertiesSeparator.INSTANCE)) {					
					memberNamesWithoutSeccions.add(m.getName());
				}
				else if (m instanceof MetaReference) {										
					memberNamesWithoutSeccions.add(m.getName());					
				}
				else if (m instanceof MetaCollection) {					
					memberNamesWithoutSeccions.add(m.getName());
				}				
				else if (m instanceof MetaGroup && !isHidden(m.getName())) {  
					memberNamesWithoutSeccions.addAll(getGroupView(((MetaGroup) m).getName()).getMemberNamesWithoutSeccions());					
				}
			}					
		}
		return memberNamesWithoutSeccions;
	}

	/**
	 * Clear all data and set the default values.
	 */
	public void reset() throws XavaException {					
		clear();		
		calculateDefaultValues();
	}
	
	/**
	 * Clear all displayed data. 
	 */
	public void clear() throws XavaException {		
		setIdFocusProperty(null);
		setCollectionDetailVisible(false);
		if (values == null) return;		
		Iterator it = values.entrySet().iterator();		
		Map result = new HashMap();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			values.put(e.getKey(), null);			
		}					
		if (hasSubviews()) {
			Iterator itSubviews = getSubviews().values().iterator();
			while (itSubviews.hasNext()) {
				View subview = (View) itSubviews.next();
				subview.clear();
			}
		}
		if (hasGroups()) {
			Iterator itSubviews = getGroupsViews().values().iterator();
			while (itSubviews.hasNext()) {
				View subview = (View) itSubviews.next();
				subview.clear();
			}
		}						
		if (hasSections()) {
			int quantity = getSections().size();
			for (int i = 0; i < quantity; i++) {
				getSectionView(i).clear();
			}	
		}		
	}
	
	/**
	 * Set the defaul values in the empty fields.  
	 */
	private void calculateDefaultValues() throws XavaException {
		// Properties
		if (getParent() == null) {
			getRoot().registeringExecutedActions = true;
		}		
		try {					
			Collection properties = new ArrayList(getMetaModel().getMetaPropertiesWithDefaultValueCalculator());			
			properties.addAll(getMetaModel().getMetaPropertiesViewWithDefaultCalculator());			
			if (!properties.isEmpty()) {		
				Map membersNames = getMembersNames();				
				Iterator it = properties.iterator();
				Collection alreadyPut = new ArrayList();				
				while (it.hasNext()) {
					MetaProperty p = (MetaProperty) it.next();					
					if (membersNames.containsKey(p.getName())) {				
						try {
							if (!p.getMetaCalculatorDefaultValue().containsMetaSetsWithoutValue()) { // This way to avoid calculate the dependend ones
								setValue(p.getName(), p.getDefaultValueCalculator().calculate());
								alreadyPut.add(p.getName());
							}					
						}
						catch (Exception ex) {
							ex.printStackTrace();
							getErrors().add("calculate_default_value_error", p.getName());
						}				 
					}
				}				
				if (!alreadyPut.isEmpty()) {
					Iterator itAlreadyPut = alreadyPut.iterator();					
					boolean hasNext = itAlreadyPut.hasNext(); 
					while (hasNext) {												 
						String propertyName = (String) itAlreadyPut.next();						 
						try {
							hasToSearchOnChangeIfSubview = false;							
							propertyChanged(propertyName);							
						}
						finally {
							hasToSearchOnChangeIfSubview = true;						
						}						
						hasNext = itAlreadyPut.hasNext(); // Loop in this way to bypass a bug in Websphere 5.0.2.9 JDK						
					}					
				}
			}
								
			// On change events					
			Iterator itOnChangeProperties = getMetaView().getPropertiesNamesThrowOnChange().iterator();			
			while (itOnChangeProperties.hasNext()) {
				String propertyName = (String) itOnChangeProperties.next();
				propertyChanged(propertyName);
			}			
					
			// Subviews		
			Iterator itSubviews = getSubviews().values().iterator();			
			while (itSubviews.hasNext()) {
				View subview = (View) itSubviews.next();
				if (subview.isRepresentsAggregate()) { 
					subview.calculateDefaultValues();
				}
				else { // Reference to entity
					subview.clear();
				}
			}			
					
			// Groups		
			Iterator itGroups = getGroupsViews().values().iterator();			
			while (itGroups.hasNext()) {
				View group = (View) itGroups.next(); 
				group.calculateDefaultValues();
			}			
					
			// Sections		
			if (hasSections()) {
				int quantity = getSections().size();
				for (int i = 0; i < quantity; i++) {
					getSectionView(i).calculateDefaultValues();
				}	
			}			
			
			// References
			Collection references = getMetaModel().getMetaReferencesWithDefaultValueCalculator();					
			if (!references.isEmpty()) {		
				Map membersNames = getMembersNames();		
				Iterator it = references.iterator();
				Collection alreadyPut = new ArrayList();						
				while (it.hasNext()) {
					MetaReference ref = (MetaReference) it.next();
					if (membersNames.containsKey(ref.getName())) {
						try {
							if (!ref.getMetaCalculatorDefaultValue().containsMetaSetsWithoutValue()) { // This way to avoid calculated dependend ones
								Object value = ref.getDefaultValueCalculator().calculate();
								IMetaEjb referencedModel = (IMetaEjb) ref.getMetaModelReferenced();	
								if (referencedModel.getPrimaryKeyClass().isInstance(value)) {
									Map values = referencedModel.obtainMapFromPrimaryKey(value);
									setValue(ref.getName(), values);
									alreadyPut.addAll(referencedModel.getAllKeyPropertiesNames());
								}		
								else {
									Collection keys = referencedModel.getAllKeyPropertiesNames();
									if (keys.size() != 1) {
										throw new XavaException("reference_calculator_with_multiple_key_requires_key_class", ref.getName(), referencedModel.getPrimaryKey());
									}
									String propertyKeyName = ref.getName() + "." + (String) keys.iterator().next();
									setValue(propertyKeyName, value);
									alreadyPut.add(propertyKeyName);
								}
							}					
						}
						catch (Exception ex) {
							ex.printStackTrace();
							getErrors().add("calculate_default_value_error", ref.getName());
						}				 
					}
				}				
				if (!alreadyPut.isEmpty()) { 
					Iterator itAlreadyPut = alreadyPut.iterator();
					boolean hasNext = itAlreadyPut.hasNext(); 
					while (hasNext) {
						String propertyName = (String) itAlreadyPut.next();										
						try {
							hasToSearchOnChangeIfSubview = false;
							propertyChanged(propertyName);
						}
						finally {
							hasToSearchOnChangeIfSubview = true;						
						}
						hasNext = itAlreadyPut.hasNext(); // Loop in this way to bypass a bug in Websphere 5.0.2.9 JDK
					}
				}
			}
		}
		finally {						
			if (getParent() == null) {
				getRoot().registeringExecutedActions = false;		
				resetExecutedActions();
			}			
		}				
	}

	private void resetExecutedActions() {		
		if (getRoot().executedActions != null) getRoot().executedActions.clear();		
	}
	
	private void registerExecutedAction(String name, Object action) {
		if (!getRoot().registeringExecutedActions) return;		
		if (getRoot().executedActions == null) getRoot().executedActions = new HashSet();
		getRoot().executedActions.add(name + "::" + action.getClass());
	}
	
	private boolean actionRegisteredAsExecuted(String name, Object action) {
		if (!getRoot().registeringExecutedActions) return false;		
		if (getRoot().executedActions == null) return false;
		return getRoot().executedActions.contains(name + "::" + action.getClass());
	}

	private boolean tieneValor(Object v) {		
		return !(v == null || "".equals(v) || "0".equals(v));
	}

	public boolean isKeyEditable() {
		return !isReadOnly() && keyEditable;
	}

	public void setKeyEditable(boolean b) throws XavaException {
		keyEditable = b;		
		Iterator it = getMetaModel().getMetaReferencesKey().iterator();
		while (it.hasNext()) {
			MetaReference ref = (MetaReference) it.next();			
			if (hasSubview(ref.getName())) {				
				getSubview(ref.getName()).setKeyEditable(b);
				getSubview(ref.getName()).setEditable(false);
			}
		}

		if (hasGroups()) { 
			it = getGroupsViews().values().iterator();
			while (it.hasNext()) {				
				View subvista = (View) it.next();
				subvista.setKeyEditable(b);
			}
		}
		
		
		if (hasSections()) {
			int cantidad = getSections().size();
			for (int i = 0; i < cantidad; i++) {
				getSectionView(i).setKeyEditable(b);
			}	
		}	
						
	}
	
	/**
	 * Si en estos momento se puede editar.
	 */
	public boolean isEditable(MetaProperty metaProperty) {
		try {							
			if (metaProperty.isReadOnly()) return false;
			if (metaProperty.isKey()) return isKeyEditable();
			if (!isEditable()) return false;			
			return isMarkAsEditable(metaProperty.getName());
		}
		catch (Exception ex) {
			System.err.println(XavaResources.getString("readonly_not_know_warning", metaProperty));
			return false;
		}		
	}
	
	/**
	 * Si en estos momento se puede editar.
	 */
	public boolean isEditable(MetaReference metaReference) {
		try {
			MetaReferenceView metaReferenceView = getMetaView().getMetaReferenceView(metaReference);
			if (metaReferenceView != null && metaReferenceView.isReadOnly()) return false;
			if (metaReference.isKey()) return isKeyEditable();
			if (!isEditable()) return false;			
			return isMarkAsEditable(metaReference.getName());
		}
		catch (Exception ex) {
			System.err.println(XavaResources.getString("readonly_not_know_warning", metaReference));
			return false;
		}		
	}

	public boolean isEditable(String miembro) throws XavaException {				
		return isEditable(getMetaView().getMetaProperty(miembro));
	}
	
	private boolean isMarkAsEditable(String nombre) {
		if (noEditablesMemberNames == null) return true;
		return !getNoEditablesMembersNames().contains(nombre);
	}
	
	public void setEditable(String nombre, boolean editable) throws XavaException {
		if (editable) getNoEditablesMembersNames().remove(nombre);
		else getNoEditablesMembersNames().add(nombre);
		if (hasGroups()) {
			Iterator it = getGroupsViews().values().iterator();
			while (it.hasNext()) {
				View v = (View) it.next();
				v.setEditable(nombre, editable);
			}
		}
		if (hasSections()) {
			int cantidad = getSections().size();
			for (int i = 0; i < cantidad; i++) {
				getSectionView(i).setEditable(nombre, editable);
			}	
		}						
	}

	public boolean isEditable() {
		return !isReadOnly() && editable;
	}

	public void setEditable(boolean b) throws XavaException {		
		editable = b;
		
		if (hasSubviews()) { 
			Iterator it = getSubviews().values().iterator();
			while (it.hasNext()) {				
				View subvista = (View) it.next();
				if (subvista.isRepresentsCollection()) continue; 
				if (subvista.isRepresentsEntityReference()) {
					subvista.setKeyEditable(b);
				}
				else {
					subvista.setEditable(b);
				}						
			}
		}
		
		if (hasGroups()) { 
			Iterator it = getGroupsViews().values().iterator();
			while (it.hasNext()) {				
				View subvista = (View) it.next();
				if (subvista.isRepresentsCollection()) continue; 
				if (subvista.isRepresentsEntityReference()) {
					subvista.setKeyEditable(b);
				}
				else {
					subvista.setEditable(b);
				}						
			}
		}
		
		
		if (hasSections()) {
			int cantidad = getSections().size();
			for (int i = 0; i < cantidad; i++) {
				getSectionView(i).setEditable(b);
			}	
		}				
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String nuevo) { 				
		if (Is.equal(modelName, nuevo)) return;		
		modelName = nuevo;
		keyEditable = true;
		editable = true;		
		resetMembers();		
	}
	
	private void resetMembers() { 
		propertyPrefix = null;
		viewName = null;
		membersNames = null;
		collectionMemberNames = null;
		memberNamesWithoutSeccions = null;
		membersNamesWithHidden = null;
		membersNamesInGroup = null;
		metaModel = null;
		metaMembers = null;
		metaMembersIncludingHiddenKey = null;
		values = null;		
		metaView = null;
		mapStereotypesProperties = null;
		metaProperties = null;
		metaPropertiesIncludingSections = null;
		metaPropertiesIncludingGroups = null;
		metaPropertiesQualified = null;
		calculatedPropertiesNames = null;
		lastPropertyKeyName = null;		
		depends = null;
		subviews = null;
		sectionsViews = null;
		groupsViews = null; 
	}
	
	public void assignValuesToWebView() {
		assignValuesToWebView(getModelName());
	}
		
	public void assignValuesToWebView(String qualifier) {
		try {												
			focusForward = "true".equalsIgnoreCase(getRequest().getParameter("focus_forward"));
			setIdFocusProperty(getRequest().getParameter("focus_property"));			
			Iterator it = isSubview()?getMetaMembersIncludingHiddenKey().iterator():getMetaMembers().iterator();			
			if (isRepresentsCollection()) fillListSelected(qualifier);
			
			while (it.hasNext()) {
				Object m = it.next();				
				if (m instanceof MetaProperty) {						
					MetaProperty p = (MetaProperty) m;										
					if (!PropertiesSeparator.INSTANCE.equals(m)) { 
						String clavePropiedad= "xava." + qualifier + "." + p.getName();						
						String claveValor = clavePropiedad + ".value";						
						String resultado = getRequest().getParameter(clavePropiedad);						
						Object valor = WebEditors.parse(getRequest(), p, resultado, getErrors());
						boolean isHiddenKeyWithoutValue = p.isHidden() && Is.emptyString(resultado); // for not reset hidden values
						if (!isHiddenKeyWithoutValue && WebEditors.mustToFormat(p)) { 
							getRequest().setAttribute(claveValor, valor);
							setValue(p.getName(), getRequest().getAttribute(claveValor));																					
						}						
					}
				}
				else if (m instanceof MetaReference) {					
					MetaReference ref = (MetaReference) m;					
					String key = "xava." + qualifier + "." + ref.getName() + ".KEY";					
					String value = getRequest().getParameter(key);
					if (value == null) {
						View subview = getSubview(ref.getName());
						subview.assignValuesToWebView(qualifier + "." + ref.getName());					 																				
					}
					else { // References as combo (descriptions-list) and composite key
						assignReferenceValue(qualifier, ref, value);
					}
				}
				else if (m instanceof MetaCollection) {
					MetaCollection collec = (MetaCollection) m;	
					View subview = getSubview(collec.getName());
					subview.assignValuesToWebView(qualifier + "." + collec.getName()); 
				}
				else if (m instanceof MetaGroup) {					
					MetaGroup grupo = (MetaGroup) m;					
					View subvista = getGroupView(grupo.getName());
					subvista.assignValuesToWebView(qualifier);					 																									
				}
			}
						
			if (hasSections()) {				
				View seccion = getSectionView(getActiveSection());
				seccion.assignValuesToWebView(qualifier);
			}			
						
			if (!isSubview() && !isSection()) {			
			  String changedProperty = getRequest().getParameter("changed_property");			  
				if (!Is.emptyString(changedProperty)) {
					if (getParent() == null) {
						getRoot().registeringExecutedActions = true;
					}
					try {						
						propertyChanged(changedProperty);
					}
					finally {
						if (getParent() == null) {
							getRoot().registeringExecutedActions = false;		
							resetExecutedActions();
						}						
					}						
				}			
			}						
		}
		catch (Exception ex) {
			ex.printStackTrace();
			getErrors().add("system_error");
		}						
	}
	
	private void fillListSelected(String calificador) {
		String id = "xava." + calificador + ".__SELECTED__";
		String [] sel = getRequest().getParameterValues(id);
		if (sel == null || sel.length == 0) {
			listSelected = null;
			return;
		}
		listSelected = new int[sel.length];
		for (int i=0; i<sel.length; i++) {
			listSelected[i] = Integer.parseInt(sel[i]);
		}				
	}
	
	/**
	 * Indices de las filas seleccionadas en el formato lista de una colección 
	 */
	public int [] getListSelected() {
		return listSelected==null?EMPTY_SELECTED:listSelected;
	}

	private void assignReferenceValue(String qualifier, MetaReference ref, String value) throws XavaException {		
		IMetaEjb metaModel = (IMetaEjb) ref.getMetaModelReferenced(); 
		Class keyClass = metaModel.getPrimaryKeyClass();
		Field [] fields = keyClass.getFields();
		Arrays.sort(fields, FieldComparator.getInstance());
		if (!value.startsWith("[")) value = "";
		StringTokenizer st = new StringTokenizer(Strings.change(value, "..", ". ."), "[.]");		
		for (int i = 0; i < fields.length; i++) {
			String propertyName = fields[i].getName();			
			boolean hasConverter = propertyName.startsWith("_"); 
			if (hasConverter) propertyName = Strings.firstLower(propertyName.substring(1));						
			MetaProperty p = metaModel.getMetaProperty(propertyName);
			Object propertyValue = null;
			if (st.hasMoreTokens()) { // if not then null is assumed. This is a case of empty vale
				String stringPropertyValue = st.nextToken();
				if (hasConverter) { 
					try {
						Object parsedValue = Strings.toObject(p.getMapping().getCmpType(), stringPropertyValue);
						propertyValue = p.getMapping().getConverter().toJava(parsedValue);
					}
					catch (Exception ex) {
						ex.printStackTrace();
						throw new XavaException("html_to_java_reference_conversion_error");
					}
				}
				else {
					propertyValue = WebEditors.parse(getRequest(), p, stringPropertyValue, getErrors());	
				}								
			}			
			String valueKey = qualifier + "." + ref.getName() + "." + propertyName + ".value";			 
			if (WebEditors.mustToFormat(p)) {
				getRequest().setAttribute(valueKey, propertyValue);
				setValue(ref.getName() + "." + p.getName(), propertyValue);
			}									
		}				
	}
		
	public boolean throwsPropertyChanged(MetaProperty p) {		
		try {						
			if (hasDependentsProperties(p)) return true;			
			if (getMetaView().hasOnChangeAction(p.getName())) return true;			
			if (!isSubview()) return false;				
			return isRepresentsEntityReference() && getLastPropertyKeyName().equals(p.getName());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(XavaResources.getString("property_changed_not_know_warning", p.getName()));
			return false;
		}		 		 				
	}
	
	public boolean isLastKeyProperty(MetaProperty p) throws XavaException {
		return p.isKey() && p.getName().equals(getLastPropertyKeyName());
	}
	
	public boolean isFirstPropertyAndViewHasNoKeys(MetaProperty pr) throws XavaException {
		if (isGroup()) return getParent().isFirstPropertyAndViewHasNoKeys(pr); 
		if (!pr.hasMetaModel()) return false; // maybe a view property
		if (getMetaProperties().isEmpty()) return false;
		MetaProperty first = (MetaProperty) getMetaProperties().get(0);
		return first.equals(pr) && !hasKeyProperties(); 
	}
	
	private boolean hasKeyProperties() throws XavaException {
		for (Iterator it=getMetaProperties().iterator(); it.hasNext();) {
			MetaProperty pr = (MetaProperty) it.next();
			if (pr.isKey()) {
				return true;
			}
		}
		return false;
	}
		
	private String getLastPropertyKeyName() throws XavaException {
		if (lastPropertyKeyName == null) {
			Iterator it = getMetaProperties().iterator();
			lastPropertyKeyName = "";
			while (it.hasNext()) {
				MetaProperty p = (MetaProperty) it.next();
				if (p.isKey()) {
					lastPropertyKeyName = p.getName();
				}
			}
		}
		return lastPropertyKeyName;		
	}

	private void propertyChanged(String propertyId) {		
		try {														
			String nombre = removeNamePrefix(propertyId);
			if (nombre.endsWith(".KEY")) {
				String nombreRef = nombre.substring(0, nombre.length() - 4);
				MetaModel referencedModel = null;
				try {
					referencedModel = getMetaModel().getMetaReference(nombreRef).getMetaModelReferenced();
				}
				catch (ElementNotFoundException ex) {
					// try if is a collection
					int idx = nombreRef.indexOf('.');
					String collectionName = nombreRef.substring(0, idx);
					String refName = nombreRef.substring(idx+1);
					referencedModel = getMetaModel().getMetaCollection(collectionName).getMetaReference().getMetaModelReferenced().getMetaReference(refName).getMetaModelReferenced();
				}
				
				Iterator itPropiedadesClave = referencedModel.getKeyPropertiesNames().iterator();
				while (itPropiedadesClave.hasNext()) {
					propertyChanged(nombreRef + "." + itPropiedadesClave.next());								
				}
				return;								
			}
			int idxPunto = nombre.indexOf('.');			
			if (idxPunto >= 0) { // es calificada				
				String nombreSubvista = nombre.substring(0, idxPunto);	
				String propertyName = nombre.substring(idxPunto + 1);
				View subview = getSubview(nombreSubvista);				
				subview.propertyChanged(propertyName);
				try {
					MetaProperty changed = subview.getMetaView().getMetaProperty(propertyName);
					propertyChanged(changed, nombre);
				}
				catch (ElementNotFoundException ex) {
					// try if is hidden
					MetaProperty changed = subview.getMetaModel().getMetaProperty(propertyName);					
					if (changed.isHidden()) {						
						propertyChanged(changed, nombre);
					}
				}
			}
			else {	
				MetaProperty cambiada = null;
				try {					
					cambiada = getMetaView().getMetaProperty(nombre);
				}				
				catch (ElementNotFoundException ex) {
					// try to obtain from model in case it is an hidden key 
					cambiada = getMetaModel().getMetaProperty(nombre);					
					if (!(cambiada.isKey() && cambiada.isHidden())) throw ex;
				}	
				propertyChanged(cambiada, nombre);
				if (getParent() != null) {
					String qualifiedName = Is.emptyString(getMemberName())?nombre:(getMemberName() + "." + nombre);
					getParent().propertyChanged(cambiada, qualifiedName);
				}
			}		
		}
		catch (ElementNotFoundException ex) {			
			// Para que secciones que no tienen todas las propiedades no lancen excepciones
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(XavaResources.getString("property_changed_warning", propertyId));
			getErrors().add("change_property_error");
		}				 		 		
	}
	
	private void propertyChanged(MetaProperty cambiada, String nombreCualificadoCambiada) {
		try {			
			tryPropertyChanged(cambiada, nombreCualificadoCambiada);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(XavaResources.getString("property_changed_warning", cambiada));
			getErrors().add("change_property_error");			
		}		 		 		
	}
	
	private void tryPropertyChanged(MetaProperty cambiada, String nombreCualificadoCambiada) throws Exception {
		if (!isOnlyThrowsOnChange()) {					
			Iterator it = getMetaPropertiesIncludingGroups().iterator();			
			while (it.hasNext()) {
				MetaProperty pr = (MetaProperty) it.next();				
				if (dependeDe(pr, cambiada, nombreCualificadoCambiada)) {
					if (pr.hasCalculator()) {
						calcularValor(pr, pr.getMetaCalculator(), pr.getCalculator(), errors, messages);					
					}
					if (pr.hasDefaultValueCalculator() && isEmptyValue(getValue(pr.getName()))) {					
						calcularValor(pr, pr.getMetaCalculatorDefaultValue(), pr.getDefaultValueCalculator(), errors, messages);					
					}					
				}
			}				
			if (hasToSearchOnChangeIfSubview && isSubview() && !isGroup() && 
					( 
					(getLastPropertyKeyName().equals(cambiada.getName()) && metaPropiedadesContiene(cambiada)) || // visible keys
					(!hasKeyProperties() && cambiada.isKey() && cambiada.isHidden()) // hidden keys
					)
				) {	
				if (!searchingObject) { // Para evitar bucles recursivos infinitos				
					try {
						searchingObject = true;					
						findObject();						
					}
					finally {
						searchingObject = false;				 
					}				
				}			
			}
		} // of if (!isOnlyThrowsOnChange())		
		if (!isSection() && getMetaView().hasOnChangeAction(nombreCualificadoCambiada)) {
			IOnChangePropertyAction accion = getMetaView().createOnChangeAction(nombreCualificadoCambiada);
			if (!actionRegisteredAsExecuted(nombreCualificadoCambiada, accion)) {
				View viewOfAction = this;
				while (viewOfAction.isGroup()) viewOfAction = viewOfAction.getParent();
				accion.setView(viewOfAction);
				accion.setChangedProperty(nombreCualificadoCambiada); 
				accion.setNewValue(getValue(nombreCualificadoCambiada));								
				getModuleManager(getRequest()).executeAction(accion, getErrors(), getMessages(), getRequest());
				registerExecutedAction(nombreCualificadoCambiada, accion);
			}
		} 		
		if (hasGroups()) {
			Iterator itGrupos = getGroupsViews().values().iterator();
			while (itGrupos.hasNext()) {
				View v = (View) itGrupos.next();
				try {
					v.tryPropertyChanged(cambiada, nombreCualificadoCambiada);					
				}
				catch (ElementNotFoundException ex) {
					// The common case of a qualified property  whose
					// her subview is not in this group (maybe in another group
					// or main view)
				}
			}		
		}			
		if (hasSections()) {			
			int cantidad = getSections().size();
			for (int i = 0; i < cantidad; i++) {
				getSectionView(i).propertyChanged(cambiada, nombreCualificadoCambiada);				
			}			
		}
		
	}	
	
	
	private boolean dependeDe(MetaProperty pr, MetaProperty cambiada, String nombreCualificadoCambiada) throws XavaException {		
		if (pr.depends(cambiada)) return true;
		if (pr.getPropertyNamesThatIDepend().contains(nombreCualificadoCambiada) ) {
			return true;
		}		 					
		return false;
	}

	private boolean metaPropiedadesContiene(MetaProperty cambiada) throws XavaException {
		Iterator it = getMetaProperties().iterator();
		while (it.hasNext()) {
			MetaProperty p = (MetaProperty) it.next();
			if (p.equals(cambiada)) return true;
		}
		return false;
	}

	private ModuleManager getModuleManager(HttpServletRequest request) throws XavaException {		
		ModuleContext context = (ModuleContext) request.getSession().getAttribute("context");		
		return (ModuleManager) context.get(request, "manager");		
	}

	public void findObject() throws Exception {
		if (getKeyValuesWithValue().isEmpty()) {			
			clear(); 			
		}
		else {					
			try {				
				setValues(MapFacade.getValues(getModelName(), getKeyValues(), getMembersNamesWithHidden()));
				
			}
			catch (ObjectNotFoundException ex) {						
				getErrors().add("object_with_key_not_found", getModelName(), getKeyValues());
				clear(); 								
			}
		}							
	}

	private String removeNamePrefix(String name) {
		if (Is.emptyString(name)) return "";		
		StringTokenizer st = new StringTokenizer(name, ".");
		if (!st.hasMoreTokens()) return name;
		String xava = st.nextToken();
		if (!"xava".equals(xava)) return name;
		if (!st.hasMoreTokens()) return name;
		String compo = st.nextToken();		
		return name.substring(compo.length() + "xava".length() + 2);
	}

	private void calcularValor(MetaProperty metaPropiedad, MetaCalculator metaCalculador, ICalculator calculador, Messages errores, Messages mensajes) {		
		try {					
			PropertiesManager mp = new PropertiesManager(calculador);
			Iterator it = metaCalculador.getMetaSets().iterator();
			while (it.hasNext()) {
				MetaSet poner = (MetaSet) it.next();				
				Object valor = null;
				if (poner.hasValue()) {					
					valor = poner.getValue();
				}
				else {
					valor = getValue(poner.getPropertyNameFrom());					
				}
				mp.executeSet(poner.getPropertyName(), valor);				
			}			
			Object nuevoValor = calculador.calculate();			
			if (calculador instanceof IOptionalCalculator) {
				if (!((IOptionalCalculator) calculador).isCalculate()) {
					return;
				}
			}			
			Object viejo = getValue(metaPropiedad.getName());
			if (!Is.equal(viejo, nuevoValor)) {				
				setValueNotifying(metaPropiedad.getName(), nuevoValor);
			}			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(XavaResources.getString("value_calculate_warning", metaPropiedad));
		}		
	}

	private boolean hasDependentsProperties(MetaProperty p) {
		try {			
			// In this view						
			for (Iterator it = getMetaPropertiesQualified().iterator(); it.hasNext();) {
				Object element = (Object) it.next();				
				if (element instanceof MetaProperty && !PropertiesSeparator.INSTANCE.equals(element)) {
					MetaProperty pro = (MetaProperty) element;					
					if (WebEditors.depends(pro, p)) {				
						return true;
					}
				}
			}			
			
			if (isRepresentsAggregate() || isRepresentsEntityReference()) {
				p = p.cloneMetaProperty();
				p.setName(getMemberName() + "." + p.getName());
			}
						
			// From the root
			for (Iterator it = getRoot().getMetaPropertiesQualified().iterator(); it.hasNext();) {
				Object element = (Object) it.next();
				if (element instanceof MetaProperty && !PropertiesSeparator.INSTANCE.equals(element)) {
					MetaProperty pro = (MetaProperty) element;					
					if (WebEditors.depends(pro, p)) {				
						return true;
					}
				}
			}
			
			// In descriptionList of reference			
			for (Iterator it=getMetaView().getMetaDescriptionsLists().iterator(); it.hasNext();) {
				MetaDescriptionsList descriptionList = (MetaDescriptionsList) it.next();				
				if (descriptionList.dependsOn(p)) {
					return true;
				}
			}				
			return false;
		}	
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(XavaResources.getString("dependents_properties_warning", p));
			return false;
		}	
	}
	
	private Collection getMetaPropertiesQualified() throws XavaException {		
		if (metaPropertiesQualified == null) {
			metaPropertiesQualified = new ArrayList();
			llenarMetaPropiedadesCalificadas(this, metaPropertiesQualified, null); 
			if (hasSections()) {
				int cantidad = getSections().size();
				for (int i=0; i<cantidad; i++) {				
					llenarMetaPropiedadesCalificadas(getSectionView(i), metaPropertiesQualified, null);								
				}			
			}			
		}
		return metaPropertiesQualified;
	}
	
	private void llenarMetaPropiedadesCalificadas(View vista, Collection propiedades, String prefijo) throws XavaException {		
		Iterator it = vista.getMetaMembers().iterator(); 		
		while (it.hasNext()) {
			Object element = (Object) it.next();
			if (element instanceof MetaProperty && !PropertiesSeparator.INSTANCE.equals(element)) {
				MetaProperty pro = (MetaProperty) element;
				if (prefijo == null) propiedades.add(pro);
				else {
					MetaProperty p = pro.cloneMetaProperty();
					p.setName(prefijo + p.getName());
					propiedades.add(p);
				}
			}
			else if (element instanceof MetaReference) {
				MetaReference ref = (MetaReference) element;
				View subvista = vista.getSubview(ref.getName());
				llenarMetaPropiedadesCalificadas(subvista, propiedades, ref.getName() + ".");
			}
			else if (element instanceof MetaGroup) {				
				MetaGroup group = (MetaGroup) element;
				View subvista = vista.getGroupView(group.getName());
				llenarMetaPropiedadesCalificadas(subvista, propiedades, prefijo);				
			}
		}		
	}

	
	/**	  
	 * @param stereotypesList Comma separate
	 */
	public Collection getPropertiesNamesFromStereotypesList(String stereotypesList) throws XavaException {
		if (Is.emptyString(stereotypesList)) return Collections.EMPTY_LIST;
		StringTokenizer st = new StringTokenizer(stereotypesList, ", ");
		Collection r = new ArrayList();
		while (st.hasMoreTokens()) {
			String estereotipo = st.nextToken().trim();
			String propiedad = getNombrePropiedadDesdeEstereotipo(estereotipo);
			if (propiedad != null) {
				r.add(propiedad);
			}
			else {
				System.err.println(XavaResources.getString("property_for_stereotype_warning", estereotipo));
				r.add(null);
			}
		} 				
		return r;
				
	}
	
	private String getNombrePropiedadDesdeEstereotipo(String stereotype) {		
		if (getParent() != null) return getParent().getNombrePropiedadDesdeEstereotipo(stereotype);
		String propiedad = (String) getMapStereotypesProperties().get(stereotype);
		if (propiedad != null) return propiedad;
		try {			
			Iterator it = getMetaPropertiesIncludingSections().iterator();
			while (it.hasNext()) {
				MetaProperty p = (MetaProperty) it.next();				
				if (stereotype.equals(p.getStereotype())) {
					getMapStereotypesProperties().put(stereotype, p.getName());
					return p.getName();					
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(XavaResources.getString("property_for_stereotype_warning", stereotype)); 
		}
		return null;
	}
	
	

	public List getMetaProperties() throws XavaException {
		if (metaProperties == null) {
			Iterator it = getMetaMembers().iterator();
			metaProperties = new ArrayList();
			while (it.hasNext()) {
				Object element = (Object) it.next();
				if (element instanceof MetaProperty && !PropertiesSeparator.INSTANCE.equals(element)) {
					metaProperties.add(element);					
				}
			}			
		}
		return metaProperties;		
	}
	
	private Collection getMetaPropertiesIncludingSections() throws XavaException {
		if (!hasSections()) return getMetaProperties();
		if (metaPropertiesIncludingSections == null) { 
			metaPropertiesIncludingSections = new ArrayList(getMetaProperties());
			int cantidad = getSections().size();
			for (int i = 0; i < cantidad; i++) {
				metaPropertiesIncludingSections.addAll(getSectionView(i).getMetaProperties());
			}	
		}
		return metaPropertiesIncludingSections;
	}
	
	private Collection getMetaPropertiesIncludingGroups() throws XavaException {
		if (!hasGroups()) return getMetaProperties();
		if (metaPropertiesIncludingGroups == null) {
			metaPropertiesIncludingGroups = new ArrayList(getMetaProperties());	
			for (Iterator it = getGroupsViews().values().iterator(); it.hasNext();) {
				View group = (View) it.next();
				metaPropertiesIncludingGroups.addAll(group.getMetaProperties());
			}			
		}
		return metaPropertiesIncludingGroups;
	}
		
	public Collection getMetaPropertiesList() throws XavaException {
		if (metaPropertiesList == null) {
			metaPropertiesList = new ArrayList();
			Iterator it = getMetaModel().getPropertiesNames().iterator();
			while (it.hasNext()) {
				MetaProperty pr= getMetaModel().getMetaProperty((String) it.next());
				if (!pr.isHidden()) {
					MetaProperty prList = pr.cloneMetaProperty();					
					metaPropertiesList.add(pr);
				}
			}
			setLabelsIdForMetaPropertiesList();
		} 
		return metaPropertiesList;
	}
	
	
	private void setLabelsIdForMetaPropertiesList() throws XavaException {
		if (getMemberName() == null || metaPropertiesList == null) return;
		Collection newList = new ArrayList();
		Iterator it = metaPropertiesList.iterator();
		while (it.hasNext()) {
			MetaProperty p = ((MetaProperty) it.next()).cloneMetaProperty();
			p.setLabelId(getMetaModel().getMetaComponent().getName() + "." + getMemberName() + "." + p.getName());
			newList.add(p);
		}
		metaPropertiesList = newList;
	}
	
		
	private void setMetaPropertiesList(Collection metaPropiedades) throws XavaException {  
		this.metaPropertiesList = metaPropiedades;
		setLabelsIdForMetaPropertiesList();
	}

	private Map getMapStereotypesProperties() {
		if (mapStereotypesProperties == null) {
			mapStereotypesProperties = new HashMap();
		}
		return mapStereotypesProperties;		
	}

	/**	  
	 * @param propertiesList Properties names comma separate
	 */
	public Collection getPropertiesNamesFromPropertiesList(String propertiesList) throws XavaException {
		if (Is.emptyString(propertiesList)) return Collections.EMPTY_LIST;
		StringTokenizer st = new StringTokenizer(propertiesList, ", ");
		Collection r = new ArrayList();
		while (st.hasMoreTokens()) {
			String propiedad = st.nextToken().trim();
			r.add(propiedad);
		}
		return r;
	}
	
	private String getPropertyPrefix() throws XavaException { 
		if (propertyPrefix == null) {
			if (Is.emptyString(getModelName())) {
				propertyPrefix = "xava." + getModelName() + ".";
			}
			else { 
				propertyPrefix = "xava." + getMetaModel().getMetaComponent().getName() + ".";
			}				
		}
		return propertyPrefix;
	}
			
	public String getViewName() {
		return viewName;
	}

	public void setViewName(String newView) { 
		if (Is.equal(viewName, newView)) return;
		resetMembers();
		viewName = newView;
	}
	
	public String toString() {
		return "View:" + oid;
	}


	public boolean isSubview() {
		return subview;
	}

	public void setSubview(boolean b) {
		subview = b;
	}

	public boolean isRepresentsAggregate() {
		if (isGroup()) return getParent().isRepresentsAggregate();
		return representsAggregate;
	}

	public void setRepresentsAggregate(boolean b) {
		representsAggregate = b;
	}
			
	public String getSearchAction() throws XavaException {		
		if (getMetaView().hasMetaSearchAction()) {
			String accion = getMetaView().getMetaSearchAction().getActionName();
			if (!Is.emptyString(accion)) return accion; 
		}		
		return "Reference.search";					
	}

	public boolean isRepresentsCollection() {
		return representsCollection;
	}

	public void setRepresentsCollection(boolean b) {
		representsCollection = b;
	}
	
	public boolean displayDetailInCollection(String nombreColeccion) throws XavaException {
		if (knowIfDisplayDetailInCollection) return displayDetailInCollection;
		Iterator it = getMetaMembers().iterator();
		while (it.hasNext()) {
			Object m = it.next();
			if (m instanceof MetaReference || m instanceof MetaCollection) {
				displayDetailInCollection = true;
				knowIfDisplayDetailInCollection = true;
				return displayDetailInCollection;
			}
		}
		displayDetailInCollection = false;
		knowIfDisplayDetailInCollection = true;
		return displayDetailInCollection;				
	}
	
	public boolean isCollectionDetailVisible() {
		return collectionDetailVisible;
	}

	public void setCollectionDetailVisible(boolean b) {
		collectionDetailVisible = b;
	}

	public Messages getErrors() {
		if (getParent() != null) return getParent().getErrors();
		return errors;
	}

	public Messages getMessages() {
		if (getParent() != null) return getParent().getMessages();
		return messages;
	}

	public void setErrors(Messages mensajes) {	
		errors = mensajes;
	}

	public void setMessages(Messages mensajes) throws XavaException {
		this.messages = mensajes;		
	}
	
	private Set getNoEditablesMembersNames() {
		if (noEditablesMemberNames == null) {
			noEditablesMemberNames = new HashSet();
		}
		return noEditablesMemberNames;
	}
	
	public HttpServletRequest getRequest() {
		if (request == null) return getParent().getRequest();
		return request;
	}

	public void setRequest(HttpServletRequest request) throws XavaException {		
		this.request = request;								
	}
	
	public boolean displayAsDescriptionsList(MetaReference ref) throws XavaException {
		return getMetaView().getMetaDescriptionList(ref) != null;				
	}
	
	public String getDescriptionPropertyInDescriptionsList(MetaReference ref) throws XavaException {		
		return getMetaView().getMetaDescriptionList(ref).getDescriptionPropertyName();
	}
	
	public String getDescriptionPropertiesInDescriptionsList(MetaReference ref) throws XavaException {		
		return getMetaView().getMetaDescriptionList(ref).getDescriptionPropertiesNames();
	}	
	
	public boolean throwsReferenceChanged(MetaReference ref) throws XavaException {		
		if (getDepends().contains(ref.getName())) return true;
		Iterator itClaves = ref.getMetaModelReferenced().getKeyPropertiesNames().iterator();
		while (itClaves.hasNext()) {			
			String propertyKey = (String) itClaves.next();
			String nombreRef = ref.getName() + "." + propertyKey;
			if (getMetaView().hasOnChangeAction(nombreRef)) {
				return true;
			}
			
			Iterator itProperties = getRoot().getMetaPropertiesQualified().iterator();
			while (itProperties.hasNext()) {
				MetaProperty p = (MetaProperty) itProperties.next();
				if (p.getPropertyNamesThatIDepend().contains(nombreRef)) return true;				
			}
			
			MetaProperty p = ref.getMetaModelReferenced().getMetaProperty(propertyKey).cloneMetaProperty();
			p.setName(nombreRef);
			if (hasDependentsProperties(p)) return true;			
		}
		return false;					
	}
		
	private Collection getDepends() throws XavaException {
		if (depends == null) {
			depends = new ArrayList();
			Iterator it = getMetaView().getMetaDescriptionsLists().iterator();
			while (it.hasNext()) {
				MetaDescriptionsList metaListaDescripciones = (MetaDescriptionsList) it.next();					
				StringTokenizer st = new StringTokenizer(metaListaDescripciones.getDepends(), ",");
				while (st.hasMoreTokens()) {
					String token = st.nextToken().trim();
					if (token.startsWith("this.")) {
						depends.add(token.substring(5));
					}
					else {
						depends.add(token);
					}
				}					
			}
		}		
		return depends;
	}
	
	public String getParameterValuesPropertiesInDescriptionsList(MetaReference ref) throws XavaException {		
		MetaDescriptionsList descriptionsList = getMetaView().getMetaDescriptionList(ref);		
		if (descriptionsList == null) return "";
		String depends = descriptionsList.getDepends();		
		if (Is.emptyString(depends)) return "";
		StringTokenizer st = new StringTokenizer(depends, ",");
		StringBuffer result = new StringBuffer();
		while (st.hasMoreTokens()) {
			String member = st.nextToken().trim();
			try {
				String reference = member.startsWith("this.")?member.substring(5):member; 
				MetaModel fromIDepends = getMetaModel().getMetaReference(reference).getMetaModelReferenced();
				for (Iterator it=fromIDepends.getKeyPropertiesNames().iterator(); it.hasNext();) {
					String key = (String) it.next();
					if (result.length() > 0) result.append(',');
					result.append(member);
					result.append('.');
					result.append(key);
				}
			}
			catch (ElementNotFoundException ex) {
				// not reference, it is simple property
				if (result.length() > 0) result.append(',');
				result.append(member);			
			}			
		}		
		return result.toString();
	}	

	public String getConditionInDescriptionsList(MetaReference ref) throws XavaException {
		MetaDescriptionsList listaDescripciones = getMetaView().getMetaDescriptionList(ref);
		if (listaDescripciones == null) return "";
		return listaDescripciones.getCondition();
	}	
		
	public boolean isOrderByKeyInDescriptionsList(MetaReference ref) throws XavaException {
		MetaDescriptionsList listaDescripciones = getMetaView().getMetaDescriptionList(ref);
		if (listaDescripciones == null) return false;
		return listaDescripciones.isOrderByKey();
	}

	public boolean isCreateNewForReference(MetaReference ref) throws XavaException {
		MetaReferenceView viewRef = getMetaView().getMetaReferenceView(ref);		
		if (viewRef == null) return true;
		return viewRef.isCreate();
	}
	
	public boolean isSearchForReference(MetaReference ref) throws XavaException {
		MetaReferenceView viewRef = getMetaView().getMetaReferenceView(ref);		
		if (viewRef == null) return true;
		return viewRef.isSearch();
	}
		
	public boolean isCreateNew() throws XavaException {
		try {			
			MetaReference ref = getParent().getMetaModel().getMetaReference(getMemberName());			
			return getParent().isCreateNewForReference(ref);
		}
		catch (ElementNotFoundException ex) {
			return getParent().getMetaModel().containsMetaCollection(getMemberName());			
		}
	}
	
	public boolean isSearch() throws XavaException {
		if (isGroup()) return getParent().isSearch(); 
		try {			
			MetaReference ref = getParent().getMetaModel().getMetaReference(getMemberName());			
			return getParent().isSearchForReference(ref);
		}
		catch (ElementNotFoundException ex) {
			return getParent().getMetaModel().containsMetaCollection(getMemberName());
		}
	}
	
	public boolean isHidden(String nombre) {		
		return hidden != null && hidden.contains(nombre);
	}

	public void setHidden(String nombre, boolean oculto) throws XavaException {				
		if (hidden == null) {
			if (!oculto) return;		
			hidden = new HashSet();
		}
		if (oculto) hidden.add(nombre);
		else hidden.remove(nombre); 		
		metaMembers = null;
		metaMembersIncludingHiddenKey = null;
		membersNames = null;		
		memberNamesWithoutSeccions = null;
		membersNamesWithHidden = null;
		membersNamesInGroup = null;
		
		// Los ocultos se pasan todos a todas las secciones y grupos,
		// así si una propiedad aparece en la cabecera y en varias
		// secciones, es ocultada/mostrada en todos sitios.
		if (hasGroups()) {
			Iterator it = getGroupsViews().values().iterator();
			while (it.hasNext()) {
				View v = (View) it.next();
				v.setHidden(nombre, oculto);
			}
		}
		
		if (hasSections()) {
			int cantidad = getSections().size();
			for (int i = 0; i < cantidad; i++) {				
				getSectionView(i).setHidden(nombre, oculto);				
			}	
		}
		
	}
		
	public View getParent() { 
		if (parent != null && parent.isSection()) return parent.getParent(); 
		return parent;
	}
	
	public View getRoot() { 
		View parent = getParent();
		if (parent == null) return this;
		return parent.getRoot();
	}

	private void setParent(View vista) {
		parent = vista;
	}
	
	public boolean hasSections() throws XavaException {		
		if (getModelName() == null) return false; 
		return getMetaView().hasSections();
	}
	
	public List getSections() throws XavaException {
		return getMetaView().getSections();
	}
	
	public View getSectionView(int indice) throws XavaException {		
		if (sectionsViews == null) {
			sectionsViews = new View[getSections().size()];			
		}
		if (sectionsViews[indice] == null) {
			View v = new View();
			v.setSection(true);
			v.setParent(this);			
			v.setModelName(getModelName()); 
			v.setMetaView((MetaView) getSections().get(indice));
			sectionsViews[indice] = v;
		}		
		return sectionsViews[indice];
	}
			
	public boolean isSection() {
		return section;
	}

	public void setSection(boolean b) {
		section = b;
	}

	public int getActiveSection() throws XavaException {
		if (activeSection >= getSections().size()) {
			activeSection = 0;
		}
		return activeSection;
	}

	public void setActiveSection(int i) {
		activeSection = i;
	}

	public int getCollectionEditingRow() {
		return collectionEditingRow;
	}

	public void setCollectionEditingRow(int i) { 
		collectionEditingRow = i;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String string) throws XavaException {
		memberName = string;		
		setLabelsIdForMetaPropertiesList();
	}

	public boolean isCollectionMembersEditables() {
		return collectionMembersEditables;
	}

	public void setCollectionMembersEditables(boolean b) {
		collectionMembersEditables = b;
	}

	public boolean isCollectionEditable() {
		return collectionEditable;
	}

	public void setCollectionEditable(boolean b) {
		collectionEditable = b;
	}

	public boolean isRepresentsEntityReference() {
		if (isGroup()) return getParent().isRepresentsEntityReference(); 
		return representsEntityReference;
	}

	public void setRepresentsEntityReference(boolean b) {
		representsEntityReference = b;
	}

	/**
	 * Tiene sentido si la subvista representa a una colección, aunque funciona siempre.	 
	 */
	public Collection getActionsNamesDetail() {
		return actionsNamesDetail==null?Collections.EMPTY_LIST:actionsNamesDetail;
	}

	/**
	 * Tiene sentido si la subvista representa a una colección, aunque funciona siempre.	 
	 */
	public void setActionsNamesDetail(Collection collection) {
		actionsNamesDetail = collection;
	}
	
	public Collection getActionsNamesList() {
		return actionsNamesList==null?Collections.EMPTY_LIST:actionsNamesList;
	}
	public boolean hasListActions() {		
		return !getActionsNamesList().isEmpty();
	}

	public void setActionsNamesList(Collection collection) {
		actionsNamesList = collection;
	}	
	
	public Collection getActionsNamesForProperty(MetaProperty p) throws XavaException {
		return getMetaView().getActionsNamesForProperty(p);
	}
	
	public int getLabelFormatForProperty(MetaProperty p) throws XavaException {
		return getMetaView().getLabelFormatForProperty(p);
	}
	
	public boolean isFrame() throws XavaException {
		return getMetaView().isFrame();
	}
		
	public String getFocusPropertyId() { 
		try {
			if (!Is.emptyString(idFocusProperty) && !focusForward) return idFocusProperty;
			return calcularIdPropiedadFoco();
		}
		catch (Exception ex) { 
			ex.printStackTrace();
			System.err.println(XavaResources.getString("focus_warning"));
			return "";			
		}
	}
	
	private String calcularIdPropiedadFoco() throws XavaException {		
		String prefijo = Is.emptyString(getMemberName())?
			"xava." + getModelName() + ".":
			"xava." + getModelName() + "." + getMemberName() + ".";
			
		if (Is.emptyString(idFocusProperty)) {
			return getIdPrimeraPropiedadEditable(prefijo);
		}
		else {			
			String nombrePropiedadFoco = idFocusProperty.substring(prefijo.length());			
			int idx = nombrePropiedadFoco.indexOf('.'); 
			if (idx < 0) {			
				String nombre = getNombreSiguientePropiedadFoco(nombrePropiedadFoco);
				return nombre==null?getIdPrimeraPropiedadEditable(prefijo):prefijo + nombre;
			}
			else {
				String nombreSubvista = nombrePropiedadFoco.substring(0, idx);
				String miembro = nombrePropiedadFoco.substring(idx + 1);
				View subvista = getSubview(nombreSubvista);				
				String nombre = subvista.getNombreSiguientePropiedadFoco(miembro);				
				if (nombre != null) return prefijo + nombre;				
				nombre = getNombreSiguientePropiedadFoco(nombreSubvista);				
				return nombre==null?getIdPrimeraPropiedadEditable(prefijo):prefijo + nombre;				  
			}
		}
	}
	
	private String getIdPrimeraPropiedadEditable(String prefijo) throws XavaException {
		Iterator it = getMetaProperties().iterator();
		while (it.hasNext()) {
			MetaProperty pr = (MetaProperty) it.next();
			if (isEditable(pr)) {
				return prefijo + pr.getName();
			}
		}
		if (hasSections()) {
			return getSectionView(getActiveSection()).getIdPrimeraPropiedadEditable(prefijo);
		}
		return null;
	}

	private String getNombreSiguientePropiedadFoco(String nombreMiembro) throws XavaException {		
		Iterator it = getMetaMembers().iterator();
		boolean encontrado = false;		
		while (it.hasNext()) {
			MetaMember m = (MetaMember) it.next();			
			if (m instanceof MetaGroup) {
				String nombre = getGroupView(m.getName()).getNombreSiguientePropiedadFoco(nombreMiembro);				
				if (nombre != null) return nombre;
			}			
			if (m.getName().equals(nombreMiembro)) {				
				encontrado = true;
				continue;
			}
			if (!encontrado) continue;			
			if (m instanceof MetaProperty &&
				!(m instanceof PropertiesSeparator) && 
				isEditable((MetaProperty) m)) {	
				return m.getName();
			}
			if (m instanceof MetaReference &&				 
				isEditable((MetaReference) m)) {
				MetaReference ref = (MetaReference) m;
				Collection keys = ref.getMetaModelReferenced().getKeyPropertiesNames();
				if (keys.size() == 1) {
					String key = (String) keys.iterator().next();
					return m.getName() + "." + key;
				}
				else {
					return m.getName();
				}
			}			
		}
		if (hasSections()) {
			return getSectionView(getActiveSection()).getNombreSiguientePropiedadFoco(nombreMiembro);
		}		
		return null;
	}

	private void setIdFocusProperty(String string) {
		idFocusProperty = string;
	}

	public String getEditCollectionElementAction() {
		return Is.emptyString(editCollectionElementAction)?"Collection.edit":editCollectionElementAction;
	}
	
	public void setEditCollectionElementAction(
			String editCollectionElementAction) {
		this.editCollectionElementAction = editCollectionElementAction;
	}

	public void recalculateProperties() {
		try {												
			Map names = getCalculatedPropertiesNames();
			if (!names.isEmpty()) {
				addValues(MapFacade.getValues(getModelName(), getKeyValues(), names));
			}
		}
		catch (Exception ex) {						
			getErrors().add("recalculate_view_error", getModelName());	 								
		}		
	}
	
	private boolean isReadOnly() {
		return readOnly;
	}
	private void setReadOnly(boolean onlyRead) {
		this.readOnly = onlyRead;
	}
	
	public String getLabelForProperty(MetaProperty p) throws XavaException {
		if (getLabels() != null) {
			String idLabel = (String) getLabels().get(p.getName());
			if (idLabel != null) {
				try {
					return Labels.get(idLabel, getRequest().getLocale());
				}
				catch (Exception ex) {
					return idLabel;
				}
			}
		}		
		if (!Is.emptyString(getMemberName())) {
			int idx = p.getId().lastIndexOf('.');
			String id = p.getId().substring(0, idx) + "." + getMemberName() + p.getId().substring(idx);
			if (Labels.existsExact(id, getRequest().getLocale())) {								
				return Labels.get(id, getRequest().getLocale());
			}
			id = getMemberName() + p.getId().substring(idx);
			if (Labels.existsExact(id, getRequest().getLocale())) {				
				return Labels.get(id, getRequest().getLocale());
			}			
		}
		return p.getLabel(getRequest());
	}
	
	public void setLabelId(String propertyName, String id) {
		if (getLabels() == null) setLabels(new HashMap());
		labels.put(propertyName, id);
	}
	
	private Map getLabels() {
		View root = getRoot();
		if (this == root) return labels;
		return root.getLabels();
	}
	
	private void setLabels(Map labels) {
		View root = getRoot();
		if (this == root) {
			this.labels = labels;
		}
		else {
			root.setLabels(labels);
		}
	}
	
	private boolean isGroup() {
		return group;
	}
	private void setGroup(boolean group) {
		this.group = group;
	}
	public boolean isOnlyThrowsOnChange() {
		if (getParent() == null) return onlyThrowsOnChange;
		return getParent().isOnlyThrowsOnChange();
	}
	public void setOnlyThrowsOnChange(boolean onlyThrowsOnChange) {
		if (getParent() == null) this.onlyThrowsOnChange = onlyThrowsOnChange;
		else getParent().setOnlyThrowsOnChange(onlyThrowsOnChange);
	}
	
}
