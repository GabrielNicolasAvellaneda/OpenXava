package org.openxava.view;

import java.lang.reflect.*;
import java.rmi.*;
import java.util.*;

import javax.ejb.*;
import javax.servlet.http.*;

import org.apache.commons.logging.*;
import org.openxava.actions.*;
import org.openxava.calculators.*;
import org.openxava.component.*;
import org.openxava.controller.*;
import org.openxava.controller.meta.*;
import org.openxava.filters.*;
import org.openxava.mapping.*;
import org.openxava.model.*;
import org.openxava.model.meta.*;
import org.openxava.tab.*;
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
		
	private static Log log = LogFactory.getLog(View.class);
	private static final long serialVersionUID = -7582669617830655121L;
	private static Collection defaultListActionsForCollections;
	
	private Map objects = null; 	
	private String editCollectionElementAction;
	private String viewCollectionElementAction;
	private String newCollectionElementAction;
	private String saveCollectionElementAction;
	private String hideCollectionElementAction;
	private String removeCollectionElementAction;
	private String removeSelectedCollectionElementsAction;
	
	private boolean focusForward;
	private String focusPropertyId;
	private Map membersNamesWithHidden;
	private Map groupsViews;
	private Collection membersNamesInGroup;
	private Map collectionMemberNames;
	private static int nextOid = 0;	
	private int collectionEditingRow = -1;
	private boolean searchingObject;
	private Collection memberNamesWithoutSeccions;
	private View parent;	
	private Collection metaPropertiesList;
	private boolean knowIfDisplayDetailInCollection;
	private boolean displayDetailInCollection;
	private String lastPropertyKeyName;
	private String lastPropertySearchKeyName;
	private Map subviews;
	private Set hiddenMembers;		
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
	private boolean collectionEditableFixed;
	private Collection actionsNamesDetail;
	private Collection actionsNamesList;
	private int [] listSelected;
	private boolean readOnly; // Always not editable, marked from xml
	private boolean onlyThrowsOnChange; 
	private Collection metaPropertiesIncludingSections;
	private Collection metaPropertiesIncludingGroups;
	private Collection metaMembersIncludingHiddenKey;
	private Map labels;
	private Collection executedActions;	
	private boolean registeringExecutedActions = false;
	private Tab collectionTab; 	
	private String propertiesListNames;
	private Collection rowStyles; // Of type MetaRowStyle 	
		
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
			String parentName = Strings.firstLower(getMetaModel().getMetaModelContainer().getName());
			Collection filtered = new ArrayList();
			Iterator it = metaMembers.iterator();
			while (it.hasNext()) {
				MetaMember m = (MetaMember) it.next();
				if (m instanceof MetaReference) {						
					MetaReference ref = (MetaReference) m;				
					if (!parentName.equals(ref.getName())) {
						filtered.add(m);						
					}
				}
				else {
					filtered.add(m);
				}
			}
			metaMembers = filtered;
		}		
		if (!hiddenIncluded && hiddenMembers != null) {
			removeHidden(metaMembers);
			removeFirstAndLastSeparator(metaMembers);
		}
		removeOverlapedProperties(metaMembers);
		return metaMembers;	
	}
	
	private void removeFirstAndLastSeparator(Collection metaMembers) {
		Iterator it = metaMembers.iterator();
		Object member = null;
		if (it.hasNext()) member = it.next();
		if (PropertiesSeparator.INSTANCE.equals(member)) it.remove();
		while (it.hasNext()) member = it.next();
		if (PropertiesSeparator.INSTANCE.equals(member)) it.remove();
	}

	private void removeHidden(Collection metaMembers) {
		Iterator it = metaMembers.iterator();		
		while (it.hasNext()) {			
			MetaMember member = (MetaMember) it.next();
			if (hiddenMembers.contains(member.getName())) it.remove();			
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
		if (this.metaView == metaView) return;
		resetMembers();
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
	
	/**
	 * Copy of the values showed in view. <p>
	 * 
	 * It's a copy, if you change it the displayed data
	 * is not changed. If you wish change displayed data
	 * you have to use <code>setValues</code> or <code>setValue</code>.<br>
	 */
	public Map getValues() throws XavaException {			
		return new HashMap(getValues(false));
	}

	/**
	 * Copy of all values showed in view. <p>
	 * 
	 * It's a copy, if you change it the displayed data
	 * is not changed. If you wish change displayed data
	 * you have to use <code>setValues</code> or <code>setValue</code>.<br>
	 */	
	public Map getAllValues() throws XavaException {		
		return new HashMap(getValues(true));
	}

	private Map getValues(boolean all) throws XavaException {  
		return getValues(all, false); 
	} 
		 
	private Map getValues(boolean all, boolean onlyKeyFromSubviews) throws XavaException {		
		Map hiddenKeyAndVersion = null;  
		if (values == null) {  
			values = new HashMap();  
		} 
		else { 
			hiddenKeyAndVersion = getHiddenKeyAndVersion(values); 
		}  
		if (hasSubviews()) { 
			Iterator it = getSubviews().entrySet().iterator(); 
			while (it.hasNext()) { 
				Map.Entry en = (Map.Entry) it.next(); 
				View v = (View) en.getValue();  
				if (v.isRepresentsCollection()) continue; 
				if (!onlyKeyFromSubviews && (all || v.isRepresentsAggregate())) { 
					values.put(en.getKey(), v.getValues(all, onlyKeyFromSubviews));					
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
				values.putAll(v.getValues(all, onlyKeyFromSubviews)); 
			}  
		} 
		 
		if (hasSections()) { 
			int count = getSections().size(); 
			for (int i=0; i<count; i++) {  
				values.putAll(getSectionView(i).getValues(all, onlyKeyFromSubviews));  
			} 
		}  
		
		if (hiddenKeyAndVersion != null) { 
			values.putAll(hiddenKeyAndVersion);  
		} 

		return values; 
	} 	
	
	private Map getHiddenKeyAndVersion(Map keyValues) throws XavaException { 
		Map result = null;
		for (Iterator it=keyValues.keySet().iterator(); it.hasNext(); ) {
			String property = (String) it.next();
			if (getMetaModel().isHiddenKey(property) || getMetaModel().isVersion(property)) { 
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
				trySetValue(key, value);
			}
			else {
				String subviewName = key.substring(0, idx);
				String member = key.substring(idx+1);								 				
				getSubview(subviewName).trySetValue(member, value);
			}
		}											
	}
	
	public void setValues(Map map) throws XavaException {
		if (values == null) values = new HashMap();
		else values.clear();
		closeChildCollectionDetails();
		addValues(map);	
	}
	

	private void closeChildCollectionDetails() throws XavaException {		
		if (hasSubviews()) { 
			Iterator it = getSubviews().values().iterator();
			
			while (it.hasNext()) {				
				View subview = (View) it.next();
				subview.setCollectionDetailVisible(false);
				subview.setCollectionEditingRow(-1);
				subview.closeChildCollectionDetails();										
			}
		}
				
		if (hasGroups()) { 
			Iterator it = getGroupsViews().values().iterator();
			while (it.hasNext()) {				
				View subview = (View) it.next();
				subview.setCollectionDetailVisible(false);
				subview.setCollectionEditingRow(-1);				
				subview.closeChildCollectionDetails();
			}
		}
				
		if (hasSections()) {
			int count = getSections().size();
			for (int i = 0; i < count; i++) {
				View subview = getSectionView(i); 
				subview.setCollectionDetailVisible(false);
				subview.setCollectionEditingRow(-1);
				subview.closeChildCollectionDetails();
			}	
		}						
	}

	/**
	 * Set the values and execute the on-change actions associated to
	 * the assigned properties. <p>
	 */
	public void setValuesExecutingOnChangeActions(Map values) throws XavaException {		
		setOnlyThrowsOnChange(true);
		try {
			setValuesNotifying(values);
		}
		finally {
			setOnlyThrowsOnChange(false);
		}		
	}
	
	/**
	 * Set the values and throws are events associated to the changed values. 
	 */
	public void setValuesNotifying(Map values) throws XavaException {		
		getRoot().registeringExecutedActions = true;			
		try {
			setValues(values); 
			notifying(values);
		}
		finally {
			getRoot().registeringExecutedActions = false;		
			resetExecutedActions();			
		}				
	}

	private void notifying(Map values) throws XavaException {
		Iterator it = Maps.treeToPlain(values).keySet().iterator(); 
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
			if (!getMemberNamesWithoutSeccions().contains(name) && (hiddenMembers == null || !hiddenMembers.contains(name)) && !getMetaModel().getKeyPropertiesNames().contains(name)) {
				return getValueInSections(name, recalculatingValues);
			}
			else {				
				if (hasSubview(name)) { 															
					View subview = getSubview(name);
					if (!subview.isRepresentsCollection()) {						
						return subview.getValues(false);
					}
					else {						
						return subview.getCollectionValues();
					}
				}
				else {															
					if (values == null && !recalculatingValues) return null;
					return recalculatingValues?getValues(false).get(name):values.get(name);
				}				 							 								
			} 			
		} 
		else {						
			String subview = name.substring(0, idx);			
			String member = name.substring(idx+1);
			return getSubview(subview).getValue(member, recalculatingValues);
		}		
	}
	

	/**
	 * 
	 * @param name  Qualified properties are allowed
	 */	
	public Object getValue(String name) throws XavaException {
		return getValue(name, true);
	}

	/**
	 * 
	 * @param name  Qualified properties are allowed
	 */		
	public int getValueInt(String name) throws XavaException {
		Number v = (Number) getValue(name);			
		return v==null?0:v.intValue();
	}
	
	/**
	 * 
	 * @param name  Qualified properties are allowed
	 */		
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
		int count = getSections().size();
		for (int i=0; i<count; i++) {
			View sectionView = getSectionView(i); 
			View subview = (View) sectionView.getSubviews().get(name);
			if (subview == null) {
				subview = sectionView.findSubviewInSection(name);
				if (subview != null) return subview;
			} 
			if (subview == null) {
				subview = sectionView.findSubviewInGroup(name);
				if (subview != null) return subview;
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
			newView.setMemberName(getMemberName()); 
			newView.setGroup(true);
			newView.setEditable(isEditable()); 
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
		newView.setMemberName(member.getName()); 
		if (newView.isRepresentsCollection()) {					
			MetaCollectionView metaCollectionView = getMetaView().getMetaCollectionView(member.getName());
			if (metaCollectionView != null) {
				if (metaCollectionView.isAsAggregate()) {
					newView.setRepresentsAggregate(true); 
				}
				Collection propertiesListNames = metaCollectionView.getPropertiesListNames();
				if (!propertiesListNames.isEmpty()) {
					newView.setPropertiesListNames(metaCollectionView.getPropertiesListNamesAsString());
					newView.setMetaPropertiesList(namesToMetaProperties(newView, propertiesListNames));
				}				
				if (metaCollectionView.hasRowStyles()) { 
					newView.setRowStyles(metaCollectionView.getMetaRowStyles());					
				}				
				Collection actionsDetailNames = metaCollectionView.getActionsDetailNames();
				if (!actionsDetailNames.isEmpty()) {
					newView.setActionsNamesDetail(new ArrayList(actionsDetailNames));
				}
				newView.setEditCollectionElementAction(metaCollectionView.getEditActionName());
				newView.setViewCollectionElementAction(metaCollectionView.getViewActionName());
				newView.setNewCollectionElementAction(metaCollectionView.getNewActionName());
				newView.setSaveCollectionElementAction(metaCollectionView.getSaveActionName());
				newView.setHideCollectionElementAction(metaCollectionView.getHideActionName());
				newView.setRemoveCollectionElementAction(metaCollectionView.getRemoveActionName());
				newView.setRemoveSelectedCollectionElementsAction(metaCollectionView.getRemoveSelectedActionName());
				boolean editable = false;
				boolean keyEditable = false;
				if (!metaCollectionView.isReadOnly()) {
					keyEditable = isEditable();
					if (newView.isRepresentsAggregate()) editable = isEditable();															
				}				
				newView.setCollectionEditable(!metaCollectionView.isReadOnly() && !metaCollectionView.isEditOnly());
				if (!newView.isCollectionEditable()) {
					newView.setCollectionEditableFixed(true);
				}
				else {
					newView.setCollectionEditable(isEditable());
				}
				newView.setCollectionMembersEditables(keyEditable || metaCollectionView.isEditOnly());								
				newView.setEditable(editable);				
				newView.setKeyEditable(keyEditable);				
				newView.setViewName(metaCollectionView.getViewName());
				Collection actionsListNames = metaCollectionView.getActionsListNames();
				if (!actionsListNames.isEmpty()) {					
					Collection actions = new ArrayList(actionsListNames);
					actions.addAll(newView.getDefaultListActionsForCollections());
					newView.setActionsNamesList(actions);
				}
			}
			else {
				newView.setKeyEditable(isEditable()); 
				newView.setEditable(newView.isRepresentsAggregate()?isEditable():false);
				newView.setCollectionEditable(isEditable()); 				
				newView.setCollectionMembersEditables(true);
			}
		}
		else {
			MetaReferenceView metaReferenceView = getMetaView().getMetaReferenceView(ref);
			if (metaReferenceView != null) {
				newView.setReadOnly(metaReferenceView.isReadOnly());
				if (newView.isRepresentsEntityReference()) {
					newView.setRepresentsAggregate(metaReferenceView.isAsAggregate());					
					if (newView.isRepresentsAggregate()) {
						newView.setEditable(isEditable());
					}
				}
			}			
		}
		subviews.put(member.getName(), newView);
	} 
	 
		
	private Collection getDefaultListActionsForCollections() {
		try {
			if (isCollectionCalculated()) return Collections.EMPTY_LIST;
			if (defaultListActionsForCollections == null) {			
					MetaController controller = MetaControllers.getMetaController("DefaultListActionsForCollections"); // Si no existe: ┐Advertencia?
					Collection result = new ArrayList();
					for (Iterator it = controller.getAllMetaActions().iterator(); it.hasNext();) {
						MetaAction action = (MetaAction) it.next();
						if (!action.isHidden()) {
							result.add(action.getQualifiedName());
						}
					}				
					defaultListActionsForCollections = Collections.unmodifiableCollection(result); 
			}
			return defaultListActionsForCollections;
		}
		catch (XavaException ex) {
			log.warn(XavaResources.getString("default_list_action_controllers_warning"), ex);
			return Collections.EMPTY_LIST;
		}
	}

	private void setPropertiesListNames(String propertiesListNames) {
		this.propertiesListNames = propertiesListNames; 		
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
	 * Set the value to the indicated member. <p>
	 * 
	 * If member is not of this view an exception is thrown.
	 * 
	 * @param name Can be qualified	 
	 * @exception XavaException  If name is not a displayed member of this view.
	 */
	public void setValue(String name, Object value) throws XavaException {		
		if (!trySetValue(name, value)) {
			String viewName = getViewName() == null?"":"'" + getViewName() + "'";
			throw new XavaException("member_not_found_in_view", "'" + name + "'", viewName, "'" + getModelName() + "'");
		}		
	}	
				
	/**
	 * Try to set the value to the indicated member. <p>
	 * 
	 * If member does not exist in view, returns false, but it does not throw exception.<br>
	 * 
	 * @param name Can be qualified
	 * @return <code>true</code> if member exists and it's updated, <code>false</code> otherwise.	 
	 */
	public boolean trySetValue(String name, Object value) throws XavaException {		
		int idx = name.indexOf('.');		
		if (idx < 0) {
			if (getMembersNamesInGroup().contains(name)) {
				trySetValueInGroups(name, value);				
			}																							
			else if (!getMemberNamesWithoutSeccions().contains(name) && !getMetaModel().getKeyPropertiesNames().contains(name) && !getMetaModel().getKeyReferencesNames().contains(name) && !getMetaModel().isVersion(name)) {
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
					value = Strings.removeXSS(value); 
					values.put(name, value);					
				}				 							 								
			} 
		} 
		else {			
			String subview = name.substring(0, idx);
			String member = name.substring(idx+1);
			getSubview(subview).trySetValue(member, value);
		}		
		return true;
	}
	
	private Collection getMembersNamesInGroup() throws XavaException { 
		if (membersNamesInGroup == null) {
			membersNamesInGroup = new ArrayList();		
			Iterator it = getGroupsViews().values().iterator();		
			while (it.hasNext()) {
				View subview = (View) it.next();																
				membersNamesInGroup.addAll(subview.getMembersNamesWithHiddenImpl().keySet());
			}		
		}
		return membersNamesInGroup;
	}

	private void trySetValueInGroups(String name, Object value) throws XavaException {		
		Iterator it = getGroupsViews().values().iterator();
		while (it.hasNext()) {
			View subview = (View) it.next();			
			subview.trySetValue(name, value);			
		}				
	}
	
	private boolean setValueInSections(String name, Object value) throws XavaException {
		if (!hasSections()) return false;
		int count = getSections().size();		
		for (int i = 0; i < count; i++) {			
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
		int count = getSections().size();
		for (int i = 0; i < count; i++) {							
			if (getSectionView(i).getMetaView().getName().equals(sectionName)) {
				return getSectionView(i);				
			} 				
		}		
		throw new ElementNotFoundException("section_not_found", sectionName);
	}
		
	private Object getValueInSections(String name, boolean recalculatingValues) throws XavaException { 
		if (!hasSections()) return null;
		int count = getSections().size();
		for (int i = 0; i < count; i++) {			
			Object value = getSectionView(i).getValue(name, recalculatingValues);
			if (value != null) return value;
		}
		return null;
	}
	
	/**
	 * Excludes those values that are null, zero or empty string.
	 */
	public Map getKeyValuesWithValue() throws XavaException {		
		Map values = getValues(false, true); 
		Iterator it = values.keySet().iterator();
		Map result = new HashMap();
		while (it.hasNext()) {
			String name = (String) it.next();			
			if (getMetaModel().isKey(name)) {
				Object value = values.get(name);
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
		Map values = getValues(false, true); 		
		Iterator it = values.keySet().iterator();
		Map result = new HashMap();
		while (it.hasNext()) {
			String name = (String) it.next();			
			if (getMetaModel().isKey(name)) {
				result.put(name, values.get(name));
			}			
		}		

		if (getParent() != null && !getParent().isRepresentsAggregate()) {			
			// At the moment reference to entity within aggregate can not be part of key
			if (isRepresentsEntityReference() && !isRepresentsCollection()) {				
				ModelMapping mapping = getParent().getMetaModel().getMapping();
				if (mapping.isReferenceOverlappingWithSomeProperty(getMemberName())) {					
					Iterator itProperties = mapping.getOverlappingPropertiesOfReference(getMemberName()).iterator();					
					while (itProperties.hasNext()) {
						String property = (String) itProperties.next();						
						String overlappedProperty = mapping.getOverlappingPropertyForReference(getMemberName(), property);
						Maps.putValueFromQualifiedName(result, property, getParent().getValue(overlappedProperty, false));
					}
					
				}								
			}		
		}		
		return result; 
	}
	
	public Map getMembersNamesWithHidden() throws XavaException {		
		// the public version create a new Map always
		return createMembersNames(true);
	}
		
	public Map getMembersNames() throws XavaException {		
		// the public version create a new Map always
		return createMembersNames(false);
	}	
	
	private Map getMembersNamesWithHiddenImpl() throws XavaException { 
		// the private version make cache
		if (membersNamesWithHidden == null) {
			membersNamesWithHidden = createMembersNames(true);
		}
		return membersNamesWithHidden;		
	}
		
	private Map getMembersNamesImpl() throws XavaException { 
		// the private version make cache
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
			if (isMetaProperty(m)) {
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
			int count = getSections().size();
			for (int i = 0; i < count; i++) {
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
			if (isMetaProperty(m)) {										
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

	/**
	 * <code>Tab</code> used for manage the data of this collection. <p>
	 *
	 * This view must represents a collection in order to call this method.<br>
	 */
	public Tab getCollectionTab() throws XavaException {		
		assertRepresentsCollection("getCollectionTab()");
		if (collectionTab == null) {			
			collectionTab = new Tab();				
			collectionTab.setModelName(getModelName());
			collectionTab.setTabName(Tab.COLLECTION_PREFIX + getMemberName());
			collectionTab.setCollectionView(this);			
			collectionTab.setMetaRowStyles(rowStyles);
			collectionTab.setFilterVisible(XavaPreferences.getInstance().isShowFilterByDefaultInCollections());
			if (propertiesListNames != null) {
				collectionTab.setPropertiesNames(propertiesListNames);
			}
			MetaCollection metaCollection = getMetaCollection();
			if (metaCollection.hasCondition()) {
				collectionTab.setBaseCondition(metaCollection.getSQLCondition());
				CollectionWithConditionInViewFilter filter = new CollectionWithConditionInViewFilter(); 
				filter.setView(getParent());
				filter.setConditionArgumentsPropertyNames(metaCollection.getConditionArgumentsPropertyNames());
				collectionTab.setFilter(filter);
			}
			else {
				collectionTab.setBaseCondition(createBaseConditionForCollectionTab());							
				CollectionInViewFilter filter = new CollectionInViewFilter();
				filter.setView(getParent());
				collectionTab.setFilter(filter);
			}
			collectionTab.setDefaultOrder(getMetaCollection().getOrder());
		}
		return collectionTab;
	}
	
	private void assertRepresentsCollection(String method) {
		if (!isRepresentsCollection()) {
			throw new IllegalStateException(
				XavaResources.getString("represents_collections_required_in_view", method));
		}
	}
	
	private String createBaseConditionForCollectionTab() throws XavaException {
		String referenceToParent = getMetaCollection().getMetaReference().getRole();
		Collection keyNames = getParent().getMetaModel().getAllKeyPropertiesNames();
		StringBuffer condition = new StringBuffer();
		for (Iterator it = keyNames.iterator(); it.hasNext();) {
			condition.append("${");
			condition.append(referenceToParent);
			condition.append('.');
			condition.append(it.next());
			condition.append("} = ?");
			if (it.hasNext()) {
				condition.append(" and ");
			}			
		}				
		return condition.toString();
	}
	
	private MetaCollection getMetaCollection() throws XavaException {
		return getParent().getMetaModel().getMetaCollection(getMemberName());
	}


	/**
	 * A list of all collection element when each element is a map 
	 * with the values of the collection element.<p>
	 * 
	 * In order to call this method <b>this</b> view must represents a collection</b>.<p>
	 * 
	 * The values only include the displayed data in the row.<br>
	 * @return  Of type <tt>Map</tt>. Never null.
	 */	
	public List getCollectionValues() throws XavaException {		
		assertRepresentsCollection("getCollectionValues()");				
		if (isCollectionCalculated()) {
			// If calculated we obtain the data directly from the model object
			Map membersNames = new HashMap();
			membersNames.put(getMemberName(), new HashMap(getCollectionMemberNames()));		
			try {							
				Map values = MapFacade.getValues(getParent().getModelName(), getParent().getKeyValues(), membersNames);				
				return (List) values.get(getMemberName());				
			}
			catch (ObjectNotFoundException ex) { // New one is creating
				return Collections.EMPTY_LIST; 								
			}
			catch (Exception ex) {
				log.error(ex.getMessage(), ex);
				getErrors().add("collection_error", getMemberName()); 
				throw new XavaException("collection_error", getMemberName()); 								
			}			
		}
		else { 
			// If not calculated we obtain the data from the Tab
			return getCollectionValues(getCollectionTab().getAllKeys());
		}
		
	}
		
	/**
	 * A list of selected collection element when each element is a map 
	 * with the values of the collection element.<p>
	 * 
	 * In order to call this method <b>this view must represents a collection</b>.<p>
	 * 
	 * The values only include the displayed data in the row.<br>
	 * @return  Of type <tt>Map</tt>. Never null.
	 */
	public List getCollectionSelectedValues() throws XavaException {  		
		assertRepresentsCollection("getCollectionSelectedValues()");				
		if (isCollectionCalculated()) {
			// If calculated we obtain the data directly from the model object
			if (listSelected == null) return Collections.EMPTY_LIST;
			List result = new ArrayList();
			List all = getCollectionValues();
			for (int i=0; i<listSelected.length; i++) {
				result.add(all.get(listSelected[i]));
			}
			return result;
		}
		else { 
			// If not calculated we obtain the data from the Tab
			return getCollectionValues(getCollectionTab().getSelectedKeys());
		}
	}

	private List getCollectionValues(Map [] keys) throws XavaException { 
		List result = new ArrayList();
		Map memberNames = new HashMap(getCollectionMemberNames());
		for (int i = 0; i < keys.length; i++) {			
			try {
				Map values = MapFacade.getValues(getModelName(), keys[i], memberNames);
				result.add(values);				
			}
			catch (Exception ex) {
				log.error(ex.getMessage(), ex);
				getErrors().add("collection_error", getMemberName());
				throw new XavaException("collection_error", getMemberName());	
			}				
		}
		return result;
	}
	
	/**
	 * A list of all objects (POJOs or EntityBeans) in the collection.<p>
	 * 
	 * In order to call this method <b>this view must represents a collection</b>.<p>
	 * 
	 * Generally the objects are POJOs, although if you use EJBPersistenceProvider
	 * the they will be EntityBeans (of EJB2).<br> 
	 *  
	 * @return  Never null.
	 */		
	public List getCollectionObjects() throws XavaException {  		
		assertRepresentsCollection("getCollectionObjects()");		
		Map [] keys = null;
		if (isCollectionCalculated()) {
			Collection values = getCollectionValues();
			keys = new Map[values.size()];
			values.toArray(keys);
		}
		else {				
			keys = getCollectionTab().getAllKeys(); 
		}
		return getCollectionObjects(keys);						
	}
	
	/**
	 * A list of selected objects (POJOs or EntityBeans) in the collection.<p>
	 * 
	 * In order to call this method <b>this view must represents a collection</b>.<p>
	 * 
	 * Generally the objects are POJOs, although if you use EJBPersistenceProvider
	 * the they will be EntityBeans (of EJB2).<br> 
	 *  
	 * @return  Never null.
	 */
	public List getCollectionSelectedObjects() throws XavaException {  
		assertRepresentsCollection("getCollectionSelectedObjects()");		
		Map [] selectedKeys = null;
		if (isCollectionCalculated()) {
			Collection selectedValues = getCollectionSelectedValues();
			selectedKeys = new Map[selectedValues.size()];
			selectedValues.toArray(selectedKeys);
		}
		else {				
			selectedKeys = getCollectionTab().getSelectedKeys();
		}
		return getCollectionObjects(selectedKeys);						
	}

	private List getCollectionObjects(Map[] keys) throws XavaException {
		List result = new ArrayList();		
		for (int i = 0; i < keys.length; i++) {			
			try {
				Object object = MapFacade.findEntity(getModelName(), keys[i]);
				result.add(object);				
			}
			catch (Exception ex) {
				log.error(ex.getMessage(), ex);
				getErrors().add("collection_error", getMemberName()); 
				throw new XavaException("collection_error", getMemberName()); 									
			}			
		}
		return result;
	}
	
	/**
	 * If the collection represents by this view is calculated. <p>
	 * 
	 * In order to call this method <b>this view must represents a collection</b>.<p>
	 */
	public boolean isCollectionCalculated() throws XavaException { 
		assertRepresentsCollection("isCollectionCalculated()");
		return getMetaCollection().hasCalculator();
	}
	
	public boolean isDetailMemberInCollection() throws XavaException {		
		return getCollectionMemberNames().keySet().containsAll(getMembersNamesImpl().keySet());
	}
	
	private Map getCollectionMemberNames() throws XavaException {
		if (collectionMemberNames == null) {   		
			Map result = new HashMap();			
			Iterator it = getMetaPropertiesList().iterator();
			while (it.hasNext()) {
				MetaProperty pr = (MetaProperty) it.next();
				String propertyName = pr.getName();				
				addQualifiedMemberToMap(propertyName, result);
			}	
			collectionMemberNames = Collections.unmodifiableMap(result); 
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
				if (isMetaProperty(m)) {					
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
			int count = getSections().size();
			for (int i = 0; i < count; i++) {
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
				Map membersNames = getMembersNamesImpl();				
				Iterator it = properties.iterator();
				Collection alreadyPut = new ArrayList();				
				while (it.hasNext()) {
					MetaProperty p = (MetaProperty) it.next();
					if (membersNames.containsKey(p.getName())) {				
						try {
							if (!p.getMetaCalculatorDefaultValue().containsMetaSetsWithoutValue()) { // This way to avoid calculate the dependend ones
								ICalculator calculator = p.createDefaultValueCalculator();
								if (calculator instanceof IJDBCCalculator) {
									((IJDBCCalculator) calculator).setConnectionProvider(DataSourceConnectionProvider.getByComponent(getModelName()));
								}								
								trySetValue(p.getName(), calculator.calculate()); 
								alreadyPut.add(p.getName());
							}					
						}
						catch (Exception ex) {
							log.error(ex.getMessage(), ex);
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
				int count = getSections().size();
				for (int i = 0; i < count; i++) {
					getSectionView(i).calculateDefaultValues();
				}	
			}			
			
			// References			
			Collection references = getMetaModel().getMetaReferencesWithDefaultValueCalculator();					
			if (!references.isEmpty()) {		
				Map membersNames = getMembersNamesImpl();		
				Iterator it = references.iterator();
				Collection alreadyPut = new ArrayList();						
				while (it.hasNext()) {
					MetaReference ref = (MetaReference) it.next();
					if (membersNames.containsKey(ref.getName())) {
						try {							
							if (!ref.getMetaCalculatorDefaultValue().containsMetaSetsWithoutValue()) { // This way to avoid calculated dependend ones
								Object value = ref.getDefaultValueCalculator().calculate();
								MetaModel referencedModel = ref.getMetaModelReferenced();								
								if (referencedModel.getPOJOClass().isInstance(value)) { 																								
									Map values = referencedModel.toMap(value);
									trySetValue(ref.getName(), values); 
									alreadyPut.addAll(referencedModel.getAllKeyPropertiesNames());									
								}
								else if (referencedModel.getMetaEJB() != null && referencedModel.getMetaEJB().isPrimaryKeyClassAvailable() && referencedModel.getMetaEJB().getPrimaryKeyClass().isInstance(value)) { 
									Map values = referencedModel.getMetaEJB().obtainMapFromPrimaryKey(value);
									trySetValue(ref.getName(), values); 
									alreadyPut.addAll(referencedModel.getAllKeyPropertiesNames());
								}		
								else {
									Collection keys = referencedModel.getAllKeyPropertiesNames();
									if (keys.size() != 1) {
										throw new XavaException("reference_calculator_with_multiple_key_requires_key_class", ref.getName(), referencedModel.getPOJOClass());
									}
									String propertyKeyName = ref.getName() + "." + (String) keys.iterator().next();
									trySetValue(propertyKeyName, value); 
									alreadyPut.add(propertyKeyName);
								}
							}					
						}
						catch (Exception ex) {
							log.error(ex.getMessage(), ex);
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
		getRoot().executedActions.add(getModelName() + "::" + name + "::" + action.getClass()); 		
	}
	
	private boolean actionRegisteredAsExecuted(String name, Object action) {
		if (!getRoot().registeringExecutedActions) return false;				
		if (getRoot().executedActions == null) return false;		
		return getRoot().executedActions.contains(getModelName() + "::" + name + "::" + action.getClass()); 
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
				View subview = (View) it.next();
				subview.setKeyEditable(b);
			}
		}
		
		
		if (hasSections()) {
			int count = getSections().size();
			for (int i = 0; i < count; i++) {
				getSectionView(i).setKeyEditable(b);
			}	
		}	
						
	}
	
	/**
	 * If at this moment is editable.
	 */
	public boolean isEditable(MetaProperty metaProperty) {		
		try {							
			if (metaProperty.isReadOnly()) return false;
			if (metaProperty.isKey()) return isKeyEditable();
			if (!isEditable()) return false;			
			return isMarkedAsEditable(metaProperty.getName());
		}
		catch (Exception ex) {
			log.warn(XavaResources.getString("readonly_not_know_warning", metaProperty),ex);
			return false;
		}		
	}
	
	/**
	 * If at this moment is editable.
	 */
	public boolean isEditable(MetaReference metaReference) {		
		try {
			MetaReferenceView metaReferenceView = getMetaView().getMetaReferenceView(metaReference);
			if (metaReferenceView != null && metaReferenceView.isReadOnly()) return false;
			if (metaReference.isKey()) return isKeyEditable();
			if (!isEditable()) return false;			
			return isMarkedAsEditable(metaReference.getName());
		}
		catch (Exception ex) {
			log.warn(XavaResources.getString("readonly_not_know_warning", metaReference),ex);
			return false;
		}		
	}

	public boolean isEditable(String member) throws XavaException {		
		int idx = member.indexOf('.'); 
		if (idx >= 0) {
			String reference = member.substring(0, idx);
			String submember = member.substring(idx + 1);
			try {
				return getSubview(reference).isEditable(submember);
			}
			catch (ElementNotFoundException ex) {
				// Maybe a custom JSP view wants access to a property not showed in default view
				MetaModel referencedModel = getMetaModel().getMetaReference(reference).getMetaModelReferenced(); 				
				return (referencedModel instanceof MetaAggregate) ||
					referencedModel.isKey(submember);
			}			
		}	
		return isEditable(getMetaView().getMetaProperty(member));
	}
	
	private boolean isMarkedAsEditable(String name) {
		if (noEditablesMemberNames == null) return true;
		return !getNoEditablesMembersNames().contains(name);
	}
	
	public void setEditable(String name, boolean editable) throws XavaException {		
		if (editable) getNoEditablesMembersNames().remove(name);
		else getNoEditablesMembersNames().add(name);
		if (hasGroups()) {
			Iterator it = getGroupsViews().values().iterator();
			while (it.hasNext()) {
				View v = (View) it.next();
				v.setEditable(name, editable);
			}
		}
		if (hasSections()) {
			int count = getSections().size();
			for (int i = 0; i < count; i++) {
				getSectionView(i).setEditable(name, editable);
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
				View subview = (View) it.next();
				if (subview.isRepresentsCollection()) {
					subview.setCollectionEditable(b);
					if (!subview.isCollectionMembersEditables()) {
						subview.setKeyEditable(false); 
						subview.setEditable(false); 
						continue;
					}					
				}
				if (subview.isRepresentsEntityReference()) {
					subview.setEditable(false);
					subview.setKeyEditable(b); 					
				}
				else {
					subview.setEditable(b);
				}						
			}
		}
				
		if (hasGroups()) { 
			Iterator it = getGroupsViews().values().iterator();
			while (it.hasNext()) {				
				View subview = (View) it.next();
				if (subview.isRepresentsCollection()) {
					subview.setCollectionEditable(b);
					if (!subview.isCollectionMembersEditables()) {
						subview.setKeyEditable(false); 
						subview.setEditable(false); 
						continue;
					}					
				}
				if (subview.isRepresentsEntityReference()) {				
					subview.setKeyEditable(b); 
					subview.setEditable(false); 					
				}
				else {
					subview.setEditable(b);
				}			
			}
		}
		
		
		if (hasSections()) {
			int count = getSections().size();
			for (int i = 0; i < count; i++) {
				getSectionView(i).setEditable(b);
			}	
		}				
	}

	public String getModelName() {		
		return modelName;
	}

	public void setModelName(String newModel) { 				
		if (Is.equal(modelName, newModel)) return;		
		modelName = newModel;
		keyEditable = true;
		editable = true;		
		resetMembers();		
	}
	
	private void resetMembers() { 
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
			if (isRepresentsCollection()) fillCollectionInfo(qualifier);
			
			while (it.hasNext()) {
				Object m = it.next();							
				if (isMetaProperty(m)) {
					MetaProperty p = (MetaProperty) m;
					String propertyKey= "xava." + qualifier + "." + p.getName();					
					String valueKey = propertyKey + ".value";
					String [] results = getRequest().getParameterValues(propertyKey);
					Object value = WebEditors.parse(getRequest(), p, results, getErrors());
					boolean isHiddenKeyWithoutValue = p.isHidden() && (results == null); // for not reset hidden values					
					if (!isHiddenKeyWithoutValue && WebEditors.mustToFormat(p)) { 
						getRequest().setAttribute(valueKey, value);																				
						trySetValue(p.getName(), getRequest().getAttribute(valueKey));
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
				View section = getSectionView(getActiveSection());
				section.assignValuesToWebView(qualifier);
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
			log.error(ex.getMessage(), ex);
			getErrors().add("system_error");
		}						
	}
	
	private String getQualifiedCollectionName() { 
		if (!isRepresentsCollection() && !isRepresentsEntityReference() && !isRepresentsAggregate()) return ""; 
		return getParent().getQualifiedCollectionName() + getMemberName() + "_"; 
	}
	
	private void fillCollectionInfo(String qualifier) throws XavaException { 
		String tabPrefix = Tab.COLLECTION_PREFIX + getQualifiedCollectionName();
		getCollectionTab().setSelected(getRequest().getParameterValues(tabPrefix + "selected"));
		String [] conditionComparators = getRequest().getParameterValues(tabPrefix + "conditionComparators");		
		if (conditionComparators != null) {				
			getCollectionTab().setConditionComparators(conditionComparators);
			getCollectionTab().setConditionValues(getRequest().getParameterValues(tabPrefix + "conditionValues"));
		}
		
		// Fill selected
		String id = "xava." + qualifier + ".__SELECTED__";
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
	
	private void assignReferenceValue(String qualifier, MetaReference ref, String value) throws XavaException {		
		MetaModel metaModel = ref.getMetaModelReferenced(); 
		Class keyClass = metaModel.getPOJOClass(); 
		Field [] fields = keyClass.getDeclaredFields();
		Arrays.sort(fields, FieldComparator.getInstance());
		if (!value.startsWith("[")) value = "";
		StringTokenizer st = new StringTokenizer(Strings.change(value, "..", ". ."), "[.]");		
		for (int i = 0; i < fields.length; i++) {
			String propertyName = fields[i].getName();
			if (!metaModel.isKey(propertyName)) continue;			
			MetaProperty p = metaModel.getMetaProperty(propertyName);			 													
			Object propertyValue = null;
			if (st.hasMoreTokens()) { // if not then null is assumed. This is a case of empty value
				String stringPropertyValue = st.nextToken();
				propertyValue = WebEditors.parse(getRequest(), p, stringPropertyValue, getErrors());									
			}			
			String valueKey = qualifier + "." + ref.getName() + "." + propertyName + ".value";			 
			if (WebEditors.mustToFormat(p)) {				
				getRequest().setAttribute(valueKey, propertyValue);
				trySetValue(ref.getName() + "." + p.getName(), propertyValue);
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
			log.warn(XavaResources.getString("property_changed_not_know_warning", p.getName()), ex);
			return false;
		}		 		 				
	}
	
	public boolean throwsPropertyChanged(String propertyName) throws XavaException {		
		int idx = propertyName.indexOf('.'); 
		if (idx >= 0) {
			String reference = propertyName.substring(0, idx);
			String member = propertyName.substring(idx + 1); 
			try {
				return getSubview(reference).throwsPropertyChanged(member);
			}
			catch (ElementNotFoundException ex) {
				// Maybe a custom JSP view wants access to a property not showed in default view
				MetaModel referencedModel = getMetaModel().getMetaReference(reference).getMetaModelReferenced(); 				
				return (referencedModel instanceof MetaEntity) &&
					referencedModel.isKey(member);
			}			
		}				
		return throwsPropertyChanged(getMetaView().getMetaProperty(propertyName));
	}	
	
	private boolean isLastKeyProperty(MetaProperty p) throws XavaException {		
		return p.isKey() && p.getName().equals(getLastPropertyKeyName());
	}
	
	private boolean isLastSearchKeyProperty(MetaProperty p) throws XavaException {		
		return p.isSearchKey() && p.getName().equals(getLastPropertySearchKeyName());
	}	
	
	public boolean isFirstPropertyAndViewHasNoKeys(MetaProperty pr) throws XavaException {		
		if (pr == null) return false; 
		if (isGroup()) return getParent().isFirstPropertyAndViewHasNoKeys(pr); 
		if (!pr.hasMetaModel()) return false; // maybe a view property
		if (getMetaProperties().isEmpty()) return false;
		MetaProperty first = (MetaProperty) getMetaProperties().get(0);
		return first.equals(pr) && !hasKeyProperties(); 
	}
	
	public boolean hasKeyProperties() throws XavaException {		
		for (Iterator it=getMetaProperties().iterator(); it.hasNext();) {
			MetaProperty pr = (MetaProperty) it.next();
			if (pr.isKey()) {
				return true;
			}
		}

		if (hasGroups()) {
			Iterator it = getGroupsViews().values().iterator();
			while (it.hasNext()) {
				View v = (View) it.next();
				if (v.hasKeyProperties()) return true;				
			}			
		}

		return false;
	}
		
	private String getLastPropertyKeyName() throws XavaException {
		if (lastPropertyKeyName == null) {
			Iterator it = getMetaPropertiesIncludingGroups().iterator(); 
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
	
	private String getLastPropertySearchKeyName() throws XavaException { 
		if (lastPropertySearchKeyName == null) { 
			Iterator it = getMetaPropertiesIncludingGroups().iterator(); 
			lastPropertySearchKeyName = "";
			while (it.hasNext()) {
				MetaProperty p = (MetaProperty) it.next();		
				if (p.isSearchKey()) {
					lastPropertySearchKeyName = p.getName();
				}
			}
		}		
		return lastPropertySearchKeyName;		
	}
	
	private boolean hasSearchPropertyKeys() throws XavaException { 
		return !Is.emptyString(getLastPropertySearchKeyName());
	}
	

	private void propertyChanged(String propertyId) {		
		try {														
			String name = removeNamePrefix(propertyId);
			if (name.endsWith(".KEY")) {
				String refName = name.substring(0, name.length() - 4);
				MetaModel referencedModel = null;
				try {
					referencedModel = getMetaModel().getMetaReference(refName).getMetaModelReferenced();
				}
				catch (ElementNotFoundException ex) {
					// try if is a collection
					int idx = refName.indexOf('.');
					String collectionName = refName.substring(0, idx);
					String refName2 = refName.substring(idx+1);
					referencedModel = getMetaModel().getMetaCollection(collectionName).getMetaReference().getMetaModelReferenced().getMetaReference(refName2).getMetaModelReferenced();
				}
				
				Iterator itKeyProperties = referencedModel.getKeyPropertiesNames().iterator();
				while (itKeyProperties.hasNext()) {
					propertyChanged(refName + "." + itKeyProperties.next());								
				}
				return;								
			}
			int idxDot = name.indexOf('.');			
			if (idxDot >= 0) { // it's qualified				
				String subviewName = name.substring(0, idxDot);	
				String propertyName = name.substring(idxDot + 1);
				View subview = getSubview(subviewName);				
				subview.propertyChanged(propertyName);
				try {
					MetaProperty changedProperty = subview.getMetaView().getMetaProperty(propertyName); 
					propertyChanged(changedProperty, name);
				}
				catch (ElementNotFoundException ex) {
				}
			}
			else {	
				MetaProperty changedProperty = null;
				try {					
					changedProperty = getMetaView().getMetaProperty(name);
				}				
				catch (ElementNotFoundException ex) {
					// try to obtain from model in case it is an hidden key 
					changedProperty = getMetaModel().getMetaProperty(name);					
					if (!(changedProperty.isKey() && changedProperty.isHidden())) throw ex;
				}	
				propertyChanged(changedProperty, name);				
				if (getParent() != null) {					
					String qualifiedName = Is.emptyString(getMemberName())?name:(getMemberName() + "." + name);
					getParent().propertyChanged(changedProperty, qualifiedName);
				}
			}		
		}
		catch (ElementNotFoundException ex) {
			// So that sections that do not have all the properties do not throw exceptions
		}
		catch (Exception ex) {
			log.warn(XavaResources.getString("property_changed_warning", propertyId),ex);
			getErrors().add("change_property_error");
		}				 		 		
	}
	
	private void propertyChanged(MetaProperty changedProperty, String changedPropertyQualifiedName) {
		try {			
			tryPropertyChanged(changedProperty, changedPropertyQualifiedName);
		}
		catch (Exception ex) {
			log.warn(XavaResources.getString("property_changed_warning", changedProperty),ex);
			getErrors().add("change_property_error");			
		}		 		 		
	}
	
	private void tryPropertyChanged(MetaProperty changedProperty, String changedPropertyQualifiedName) throws Exception {		
		if (!isOnlyThrowsOnChange()) {					
			Iterator it = getMetaPropertiesIncludingGroups().iterator();			
			while (it.hasNext()) {
				MetaProperty pr = (MetaProperty) it.next();				
				if (dependsOn(pr, changedProperty, changedPropertyQualifiedName)) {
					if (pr.hasCalculator()) {						
						calculateValue(pr, pr.getMetaCalculator(), pr.getCalculator(), errors, messages);											
					}
					if (pr.hasDefaultValueCalculator() && isEmptyValue(getValue(pr.getName()))) {					
						calculateValue(pr, pr.getMetaCalculatorDefaultValue(), pr.createDefaultValueCalculator(), errors, messages);					
					}					
				}
			}				
			
			if (hasToSearchOnChangeIfSubview && isSubview() && !isGroup() &&   
					( 	
					(getLastPropertyKeyName().equals(changedProperty.getName()) && getMetaPropertiesIncludingGroups().contains(changedProperty)) || // Visible keys
					(!hasKeyProperties() && changedProperty.isKey() && changedProperty.isHidden() && changedProperty.getMetaModel() == getMetaModel()) || // hidden keys
					(isRepresentsEntityReference() && isFirstPropertyAndViewHasNoKeys(changedProperty) && isKeyEditable()) || // A searching value that is not key
					(isRepresentsEntityReference() && hasSearchPropertyKeys() && isLastSearchKeyProperty(changedProperty)) // Explicit search key 
					)
				) {				
				if (!searchingObject) { // To avoid recursive infinites loops				
					try {
						searchingObject = true;								
						IOnChangePropertyAction action = getParent().getMetaView().createOnChangeSearchAction(getMemberName());
						executeOnChangeAction(changedPropertyQualifiedName, action);						
					}
					finally {
						searchingObject = false;				 
					}				
				}			
			}
		} // of if (!isOnlyThrowsOnChange())		
		if (!isSection() && getMetaView().hasOnChangeAction(changedPropertyQualifiedName)) {
			IOnChangePropertyAction action = getMetaView().createOnChangeAction(changedPropertyQualifiedName);			
			executeOnChangeAction(changedPropertyQualifiedName, action);
		} 		
		if (hasGroups()) {
			Iterator itGroups = getGroupsViews().values().iterator();
			while (itGroups.hasNext()) {
				View v = (View) itGroups.next();
				try {
					v.tryPropertyChanged(changedProperty, changedPropertyQualifiedName);					
				}
				catch (ElementNotFoundException ex) {
					// The common case of a qualified property  whose
					// her subview is not in this group (maybe in another group
					// or main view)
				}
			}		
		}			
		if (hasSections()) {			
			int count = getSections().size();
			for (int i = 0; i < count; i++) {
				getSectionView(i).propertyChanged(changedProperty, changedPropertyQualifiedName);				
			}			
		}		
	}

	private void executeOnChangeAction(String changedPropertyQualifiedName,	IOnChangePropertyAction action) 
		throws XavaException 
	{
		if (!actionRegisteredAsExecuted(changedPropertyQualifiedName, action)) {
			View viewOfAction = this;
			while (viewOfAction.isGroup()) viewOfAction = viewOfAction.getParent();
			action.setView(viewOfAction);
			action.setChangedProperty(changedPropertyQualifiedName); 
			action.setNewValue(getValue(changedPropertyQualifiedName));							
			getModuleManager(getRequest()).executeAction(action, getErrors(), getMessages(), getRequest());				
			registerExecutedAction(changedPropertyQualifiedName, action);
		}
	}	
	
	
	private boolean dependsOn(MetaProperty pr, MetaProperty changedProperty, String changedPropertyQualifiedName) throws XavaException {		
		if (pr.depends(changedProperty)) return true;
		if (pr.getPropertyNamesThatIDepend().contains(changedPropertyQualifiedName) ) {
			return true;
		}		 					
		return false;
	}

	private ModuleManager getModuleManager(HttpServletRequest request) throws XavaException {		
		ModuleContext context = (ModuleContext) request.getSession().getAttribute("context");		
		return (ModuleManager) context.get(request, "manager");		
	}

	/**
	 * Using the key values loaded in the view search the rest of the data
	 * from persistent storage and fill the view. 
	 */
	public void findObject() throws Exception {		
		findObject(null);
	}

	/**
	 * Using the key values loaded in the view search the rest of the data
	 * from persistent storage and fill the view.
	 * 
	 * @param changedProperty  Property which change produces the search. 
	 */	
	public void findObject(MetaProperty changedProperty) throws Exception { 		
		Map key = getKeyValues();		
		try {			
			if (isRepresentsEntityReference() && isFirstPropertyAndViewHasNoKeys(changedProperty) && isKeyEditable()) {				
				// Searching by the first visible property: Useful for searching from a reference with hidden key		
				Map alternateKey = new HashMap();
				alternateKey.put(changedProperty.getName(), getValue(changedProperty.getName()));
				if (Maps.isEmptyOrZero(alternateKey)) clear();
				else setValues(MapFacade.getValuesByAnyProperty(getModelName(), alternateKey, getMembersNamesWithHidden()));
			}
			else if (isRepresentsEntityReference() && hasSearchPropertyKeys()) {				
				Map alternateKey = new HashMap();
				for (Iterator it = getMetaProperties().iterator(); it.hasNext();) {
					MetaProperty p = (MetaProperty) it.next();
					if (p.isSearchKey()) {
						alternateKey.put(p.getName(), getValue(p.getName()));
					}
				}
				if (Maps.isEmptyOrZero(alternateKey)) clear();
				else {
					setValues(MapFacade.getValuesByAnyProperty(getModelName(), alternateKey, getMembersNamesWithHidden()));				
				}
			}
			else {			
				// Searching by key, the normal case				
				if (Maps.isEmptyOrZero(key)) clear();				
				else setValues(MapFacade.getValues(getModelName(), key, getMembersNamesWithHidden()));				
			}
		}
		catch (ObjectNotFoundException ex) {						
			getErrors().add("object_with_key_not_found", getModelName(), key);
			clear(); 								
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
	
	private View getParentIfSectionOrGroup() { 
		if (isSection() || isGroup()) return getParent().getParentIfSectionOrGroup();
		return this;
	}

	private void calculateValue(MetaProperty metaProperty, MetaCalculator metaCalculator, ICalculator calculator, Messages errors, Messages messages) {		
		try {					
			PropertiesManager mp = new PropertiesManager(calculator);
			Iterator it = metaCalculator.getMetaSets().iterator();
			while (it.hasNext()) {
				MetaSet set = (MetaSet) it.next();				
				if (!set.hasValue()) {
					Object value = getParentIfSectionOrGroup().getValue(set.getPropertyNameFrom());					
					mp.executeSet(set.getPropertyName(), value); 
				}											
			}		
						
			Object pojo = null; 
			if (calculator instanceof IModelCalculator) { 				
				pojo = getPOJO();
				((IModelCalculator) calculator).setModel(pojo);
			}
			if (calculator instanceof IEntityCalculator) {
				if (pojo == null) pojo = getPOJO();
				((IEntityCalculator) calculator).setEntity(pojo); 
			}
			
			Object newValue = calculator.calculate();
			PersistenceFacade.refreshIfManaged(pojo); 
			
			if (calculator instanceof IOptionalCalculator) {
				if (!((IOptionalCalculator) calculator).isCalculate()) {
					return;
				}
			}			
			Object old = getValue(metaProperty.getName());
			if (!Is.equal(old, newValue)) {				
				setValueNotifying(metaProperty.getName(), newValue);
			}			
		}
		catch (Exception ex) {
			log.warn(XavaResources.getString("value_calculate_warning", metaProperty),ex);
		}		
	}

	/**
	 * POJO associated to the current view. <p>
	 */
	private Object getPOJO() throws XavaException, ObjectNotFoundException, RemoteException, FinderException {
		if (isKeyEditable()) {		
			return getMetaModel().toPOJO(getParentIfSectionOrGroup().getAllValues());		
		}
		else {
			Object pojo = MapFacade.findEntity(getModelName(), getParentIfSectionOrGroup().getKeyValues());
			getMetaModel().fillPOJO(pojo, getParentIfSectionOrGroup().getAllValues());			
			return pojo;			
		}
	}

	private boolean hasDependentsProperties(MetaProperty p) {		
		try {			
			// In this view								
			for (Iterator it = getMetaPropertiesQualified().iterator(); it.hasNext();) {
				Object element = (Object) it.next();				
				if (isMetaProperty(element)) {
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
				if (isMetaProperty(element)) {
					MetaProperty pro = (MetaProperty) element;					
					if (pro.getPropertyNamesThatIDepend().contains(p.getName())) {
						return true;
					}										
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
			log.warn(XavaResources.getString("dependents_properties_warning", p),ex);
			return false;
		}	
	}
	
	private Collection getMetaPropertiesQualified() throws XavaException {		
		if (metaPropertiesQualified == null) {
			metaPropertiesQualified = new ArrayList();
			fillMetaPropertiesQualified(this, metaPropertiesQualified, null); 
			if (hasSections()) {
				int count = getSections().size();
				for (int i=0; i<count; i++) {				
					fillMetaPropertiesQualified(getSectionView(i), metaPropertiesQualified, null);								
				}			
			}			
		}
		return metaPropertiesQualified;
	}
	
	private void fillMetaPropertiesQualified(View view, Collection properties, String prefix) throws XavaException {		
		Iterator it = view.getMetaMembers().iterator(); 		
		while (it.hasNext()) {
			Object element = (Object) it.next();
			if (isMetaProperty(element)) {
				MetaProperty pro = (MetaProperty) element;
				if (prefix == null) properties.add(pro);
				else {
					MetaProperty p = pro.cloneMetaProperty();
					p.setName(prefix + p.getName());
					properties.add(p);
				}
			}
			else if (element instanceof MetaReference) {
				MetaReference ref = (MetaReference) element;
				View subview = view.getSubview(ref.getName());
				fillMetaPropertiesQualified(subview, properties, ref.getName() + ".");
			}
			else if (element instanceof MetaGroup) {				
				MetaGroup group = (MetaGroup) element;
				View subview = view.getGroupView(group.getName());
				fillMetaPropertiesQualified(subview, properties, prefix);				
			}
		}		
	}

	
	/**	  
	 * @param stereotypesList Comma separated
	 */
	public Collection getPropertiesNamesFromStereotypesList(String stereotypesList) throws XavaException {		
		if (Is.emptyString(stereotypesList)) return Collections.EMPTY_LIST;
		StringTokenizer st = new StringTokenizer(stereotypesList, ", ");
		Collection r = new ArrayList();
		while (st.hasMoreTokens()) {
			String stereotype = st.nextToken().trim();
			String property = getPropertyNameFromStereotype(stereotype);
			if (property != null) {
				r.add(property);
			}
			else {
				log.warn(XavaResources.getString("property_for_stereotype_warning", stereotype));
				r.add(null);
			}
		} 				
		return r;				
	}
	
	private String getPropertyNameFromStereotype(String stereotype) {		
		if (getParent() != null) return getParent().getPropertyNameFromStereotype(stereotype);
		String property = (String) getMapStereotypesProperties().get(stereotype);
		if (property != null) return property;
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
			log.warn(XavaResources.getString("property_for_stereotype_warning", stereotype),ex); 
		}
		return null;
	}
	
	

	public List getMetaProperties() throws XavaException {		
		if (metaProperties == null) {
			Iterator it = getMetaMembers().iterator();
			metaProperties = new ArrayList();
			while (it.hasNext()) {
				Object element = (Object) it.next();
				if (isMetaProperty(element)) {
					metaProperties.add(element);					
				}
			}			
		}
		return metaProperties;		
	}
	
	public MetaProperty getMetaProperty(String name) throws XavaException {			
		int idx = name.indexOf('.');
		if (idx >= 0) {
			String reference = name.substring(0, idx);
			String member = name.substring(idx + 1);
			try {
				return getSubview(reference).getMetaProperty(member);
			}
			catch (ElementNotFoundException ex) {
				// Maybe a custom JSP view wants access to a property not showed in default view
				return getMetaModel().getMetaReference(reference).getMetaModelReferenced().getMetaProperty(member); 
			}
		}
		try {
			return getMetaView().getMetaProperty(name);			
		}
		catch (ElementNotFoundException ex) { 
			// try if is hidden			
			MetaProperty changedProperty = getMetaModel().getMetaProperty(name);					
			if (changedProperty.isHidden()) {						
				return changedProperty;
			}
			else throw ex;
		}
	}
	
	public MetaReference getMetaReference(String name) throws XavaException { 			
		int idx = name.indexOf('.');
		if (idx >= 0) {
			String reference = name.substring(0, idx);
			String member = name.substring(idx + 1);
			return getSubview(reference).getMetaReference(member);
		}				
		return getMetaModel().getMetaReference(name);
	}
		
	private Collection getMetaPropertiesIncludingSections() throws XavaException {
		if (!hasSections()) return getMetaProperties();
		if (metaPropertiesIncludingSections == null) { 
			metaPropertiesIncludingSections = new ArrayList(getMetaProperties());
			int count = getSections().size();
			for (int i = 0; i < count; i++) {
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
					metaPropertiesList.add(prList);
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
	
		
	private void setMetaPropertiesList(Collection metaProperties) throws XavaException {  
		this.metaPropertiesList = metaProperties;
		setLabelsIdForMetaPropertiesList();
	}

	private Map getMapStereotypesProperties() {
		if (mapStereotypesProperties == null) {
			mapStereotypesProperties = new HashMap();
		}
		return mapStereotypesProperties;		
	}

	/**	  
	 * @param propertiesList Properties names comma separated
	 */
	public Collection getPropertiesNamesFromPropertiesList(String propertiesList) throws XavaException {		
		if (Is.emptyString(propertiesList)) return Collections.EMPTY_LIST;
		StringTokenizer st = new StringTokenizer(propertiesList, ", ");
		Collection r = new ArrayList();
		while (st.hasMoreTokens()) {
			String property = st.nextToken().trim();
			r.add(property);
		}
		return r;
	}
	
	public String getViewName() {		
		if (isGroup() || isSection()) return getParent().getViewName();
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
		representsEntityReference = !representsAggregate;
	}
			
	public String getSearchAction() throws XavaException {			
		if (getMetaView().hasMetaSearchAction()) {
			String action = getMetaView().getMetaSearchAction().getActionName();
			if (!Is.emptyString(action)) return action; 
		}		
		return "Reference.search";					
	}

	public boolean isRepresentsCollection() {		
		return representsCollection;
	}

	public void setRepresentsCollection(boolean b) {		
		representsCollection = b;
	}
	
	public boolean displayDetailInCollection(String collectionName) throws XavaException {
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

	public void setErrors(Messages messages) {				
		errors = messages;
	}

	public void setMessages(Messages messages) throws XavaException {		
		this.messages = messages;		
	}
	
	private Set getNoEditablesMembersNames() {
		if (noEditablesMemberNames == null) {
			noEditablesMemberNames = new HashSet();
		}
		return noEditablesMemberNames;
	}
	
	public HttpServletRequest getRequest() {		
		if (request == null) return parent.getRequest();
		return request;
	}

	public void setRequest(HttpServletRequest request) throws XavaException {			
		this.request = request;								
	}
	
	public boolean displayAsDescriptionsList(MetaReference ref) throws XavaException {		
		return getMetaView().getMetaDescriptionList(ref) != null;				
	}
	
	public String getDescriptionPropertyInDescriptionsList(MetaReference ref) throws XavaException {		
		MetaDescriptionsList metaDescriptionList = getMetaView().getMetaDescriptionList(ref);
		if (metaDescriptionList != null) return metaDescriptionList.getDescriptionPropertyName();
		return getMetaView().createMetaDescriptionList(ref).getDescriptionPropertyName(); 		
	}
	
	public String getDescriptionPropertiesInDescriptionsList(MetaReference ref) throws XavaException {		
		MetaDescriptionsList metaDescriptionList = getMetaView().getMetaDescriptionList(ref);
		if (metaDescriptionList != null) return metaDescriptionList.getDescriptionPropertiesNames();
		return getMetaView().createMetaDescriptionList(ref).getDescriptionPropertiesNames();			
	}	
	
	public boolean throwsReferenceChanged(MetaReference ref) throws XavaException {		
		if (getDepends().contains(ref.getName())) return true;
		Iterator itKeys = ref.getMetaModelReferenced().getKeyPropertiesNames().iterator();
		while (itKeys.hasNext()) {			
			String propertyKey = (String) itKeys.next();
			String refName = ref.getName() + "." + propertyKey;
			if (getMetaView().hasOnChangeAction(refName)) {
				return true;
			}
			
			Iterator itProperties = getRoot().getMetaPropertiesQualified().iterator();
			while (itProperties.hasNext()) {
				MetaProperty p = (MetaProperty) itProperties.next();
				if (p.getPropertyNamesThatIDepend().contains(refName)) return true;				
			}
			
			MetaProperty p = ref.getMetaModelReferenced().getMetaProperty(propertyKey).cloneMetaProperty();
			p.setName(refName);
			if (hasDependentsProperties(p)) return true;			
		}
		return false;					
	}
		
	private Collection getDepends() throws XavaException {
		if (depends == null) {
			depends = new ArrayList();
			Iterator it = getMetaView().getMetaDescriptionsLists().iterator();
			while (it.hasNext()) {
				MetaDescriptionsList metaDescriptionsList = (MetaDescriptionsList) it.next();					
				StringTokenizer st = new StringTokenizer(metaDescriptionsList.getDepends(), ",");
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
		MetaDescriptionsList descriptionsList = getMetaView().getMetaDescriptionList(ref);
		if (descriptionsList == null) return "";
		return descriptionsList.getCondition();
	}
	
	public String getOrderInDescriptionsList(MetaReference ref) throws XavaException {		
		MetaDescriptionsList descriptionsList = getMetaView().getMetaDescriptionList(ref);
		if (descriptionsList == null) return "";
		return descriptionsList.getOrder();				
	}		
		
	public boolean isOrderByKeyInDescriptionsList(MetaReference ref) throws XavaException {
		MetaDescriptionsList descriptionsList = getMetaView().getMetaDescriptionList(ref);
		if (descriptionsList == null) return false;
		return descriptionsList.isOrderByKey();
	}

	public boolean isCreateNewForReference(MetaReference ref) throws XavaException {		
		MetaReferenceView viewRef = getMetaView().getMetaReferenceView(ref);		
		if (viewRef == null) return true;
		return viewRef.isCreate();
	}
	
	public boolean isModifyForReference(MetaReference ref) throws XavaException {
		MetaReferenceView viewRef = getMetaView().getMetaReferenceView(ref);		
		if (viewRef == null) return true;
		return viewRef.isModify();
	}
	
	public boolean isSearchForReference(MetaReference ref) throws XavaException {
		MetaReferenceView viewRef = getMetaView().getMetaReferenceView(ref);		
		if (viewRef == null) return true;
		return viewRef.isSearch();
	}
		
	public boolean isCreateNew() throws XavaException {		
		if (isGroup()) return getParent().isCreateNew();
		try {			
			MetaReference ref = getParent().getMetaModel().getMetaReference(getMemberName());			
			return getParent().isCreateNewForReference(ref);
		}
		catch (ElementNotFoundException ex) {			
			if (!getParent().getMetaModel().containsMetaCollection(getMemberName())) return false;
			MetaCollectionView collectionView = getParent().getMetaView().getMetaCollectionView(getMemberName());
			if (collectionView == null) return true;
			return collectionView.isCreateReference(); 
		}
	}
	
	public boolean isModify() throws XavaException {
		if (isGroup()) return getParent().isModify();
		try {			
			MetaReference ref = getParent().getMetaModel().getMetaReference(getMemberName());			
			return getParent().isModifyForReference(ref);
		}
		catch (ElementNotFoundException ex) {			
			if (!getParent().getMetaModel().containsMetaCollection(getMemberName())) return false;
			MetaCollectionView collectionView = getParent().getMetaView().getMetaCollectionView(getMemberName());
			if (collectionView == null) return true;
			return collectionView.isModifyReference(); 
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
	
	public boolean isLastSearchKey(MetaProperty p) throws XavaException { 
		if (!isRepresentsEntityReference()) return false;
		if (hasSearchPropertyKeys()) return isLastSearchKeyProperty(p); // explicit search key
		return 
			(isEditable(p) && isLastKeyProperty(p)) || // with key visible
			(isFirstPropertyAndViewHasNoKeys(p) && isKeyEditable()); // with key hidden 		
	}
		
	public boolean isHidden(String name) {				
		return hiddenMembers != null && hiddenMembers.contains(name);
	}

	public void setHidden(String name, boolean hidden) throws XavaException {		
		if (hiddenMembers == null) {
			if (!hidden) return;		
			hiddenMembers = new HashSet();
		}
		// getSubview() is for starting the process that create subviews and groups
		// before to hide any member
		getSubviews(); 
		if (hidden) hiddenMembers.add(name);
		else hiddenMembers.remove(name); 		
		metaMembers = null;
		metaMembersIncludingHiddenKey = null;
		membersNames = null;		
		memberNamesWithoutSeccions = null;
		membersNamesWithHidden = null;
		membersNamesInGroup = null;
		
		// The hidden ones are sent to all sections and groups,
		// thus if a property is shown in heading and in some
		// sections, is hidden/show in all places.
		if (hasGroups()) {
			Iterator it = getGroupsViews().values().iterator();
			while (it.hasNext()) {
				View v = (View) it.next();
				v.setHidden(name, hidden);
			}
		}
		
		if (hasSections()) {
			int count = getSections().size();
			for (int i = 0; i < count; i++) {				
				getSectionView(i).setHidden(name, hidden);				
			}	
		}
		
	}
		
	public View getParent() { 		
		if (parent != null && (parent.isSection() || parent.isGroup())) { 
			View result = parent.getParent();
			parent = result;
		}
		return parent;
	}
	
	public View getRoot() { 		
		View parent = getParent();
		if (parent == null) return this;
		return parent.getRoot();
	}

	private void setParent(View view) {
		parent = view;
	}
	
	public boolean hasSections() throws XavaException {			
		if (getModelName() == null) return false;
		if (displayAsDescriptionsList()) return false; 						
		return getMetaView().hasSections();
	}
	
	private boolean displayAsDescriptionsList() {
		if (getMemberName() == null) return false;
		if (!isRepresentsEntityReference()) return false;
		try {
			MetaReference ref = getParent().getMetaModel().getMetaReference(getMemberName());
			return getParent().displayAsDescriptionsList(ref);
		}
		catch (XavaException ex) {
			// log.warn(XavaResources.getString("display_as_description_warning", getMemberName()));  
			return false;
		}				
	}

	public List getSections() throws XavaException {		
		return getMetaView().getSections();
	}
	
	public View getSectionView(int index) throws XavaException {			
		if (sectionsViews == null) {
			sectionsViews = new View[getSections().size()];			
		}
		if (sectionsViews[index] == null) {
			View v = new View();
			v.setSection(true);
			v.setParent(this);			
			v.setModelName(getModelName());			
			v.setMetaView((MetaView) getSections().get(index));
			sectionsViews[index] = v;
		}		
		return sectionsViews[index];
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
		if (isCollectionEditableFixed()) return false;
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
		representsAggregate = !representsEntityReference;
	}

	/**
	 * Has sense if the subview represents a collection, although always works.	 
	 */
	public Collection getActionsNamesDetail() {		
		return actionsNamesDetail==null?Collections.EMPTY_LIST:actionsNamesDetail;
	}

	/**
	 * Has sense if the subview represents a collection, although always works.	 
	 */
	public void setActionsNamesDetail(Collection collection) {		
		actionsNamesDetail = collection;
	}
	
	/**
	 * Has sense if the subview represents a collection, although always works.
	 * 
	 * @param qualifiedActionName  Qualified name (controller.action) as in controllers.xml 	 
	 */	
	public void addDetailAction(String qualifiedActionName) {		
		if (actionsNamesDetail == null) actionsNamesDetail = new ArrayList();
		actionsNamesDetail.add(qualifiedActionName);
	}

	/**
	 * Has sense if the subview represents a collection, although always works.
	 * 
	 * @param qualifiedActionName  Qualified name (controller.action) as in controllers.xml 	 
	 */	
	public void removeDetailAction(String qualifiedActionName) {		
		if (actionsNamesDetail == null) return;
		actionsNamesDetail.remove(qualifiedActionName);		
	}
	
	/**
	 * Has sense if the subview represents a collection, although always works.
	 * 
	 * @param qualifiedActionName  Qualified name (controller.action) as in controllers.xml 	 
	 */	
	public void addListAction(String qualifiedActionName) {		
		if (actionsNamesList == null) actionsNamesList = new ArrayList(getDefaultListActionsForCollections()); 
		actionsNamesList.add(qualifiedActionName);
	}

	/**
	 * Has sense if the subview represents a collection, although always works.
	 * 
	 * @param qualifiedActionName  Qualified name (controller.action) as in controllers.xml 	 
	 */	
	public void removeListAction(String qualifiedActionName) {		
		if (actionsNamesList == null) actionsNamesList = new ArrayList(getDefaultListActionsForCollections()); 
		actionsNamesList.remove(qualifiedActionName);		
	}
		
	public Collection getActionsNamesList() {		
		return actionsNamesList==null?getDefaultListActionsForCollections():actionsNamesList;
	}
	public boolean hasListActions() {				
		// return !getActionsNamesList().isEmpty();
		return true;	// because RemoveSelectedInCollectionAction is always present (at the moment)
	}
	
	public void setActionsNamesList(Collection collection) {		
		actionsNamesList = collection;
	}	
	
	public Collection getActionsNamesForProperty(MetaProperty p, boolean editable) throws XavaException {		
		return getMetaView().getActionsNamesForProperty(p, editable);
	}
	
	public Collection getActionsNamesForReference(MetaReference ref, boolean editable) throws XavaException {		
		return getMetaView().getActionsNamesForReference(ref, editable);
	}
	
	/**
	 * If this is a view that represents a reference
	 */
	public Collection getActionsNamesForReference(boolean editable) throws XavaException {		
		if (!isRepresentsEntityReference()) return Collections.EMPTY_LIST;
		if (isGroup()) return getParent().getActionsNamesForReference(editable); 
		View parent = getParent();
		if (parent == null) return Collections.EMPTY_LIST; // Impossible?
		MetaReference ref = null;
		try {
			ref = parent.getMetaModel().getMetaReference(getMemberName());
		}
		catch (ElementNotFoundException ex) {
			ref = parent.getMetaModel().getMetaCollection(getMemberName()).getMetaReference();
		}
		return parent.getActionsNamesForReference(ref, editable);
	}	
	
	
	public int getLabelFormatForProperty(MetaProperty p) throws XavaException {		
		return getMetaView().getLabelFormatForProperty(p);
	}
	
	public int getLabelFormatForReference(MetaReference ref) throws XavaException {		
		return getMetaView().getLabelFormatForReference(ref);
	}
	
	// @Trifon
	public int getDisplaySizeForProperty(MetaProperty p) throws XavaException {		
		return getMetaView().getDisplaySizeForProperty(p);
	}
	
	
	public boolean isFrame() throws XavaException {		
		return getMetaView().isFrame();
	}
		
	public String getFocusPropertyId() {		
		try {
			if (!Is.emptyString(focusPropertyId) && !focusForward) return focusPropertyId;
			return calculateFocusPropertyId();
		}
		catch (Exception ex) { 
			log.warn(XavaResources.getString("focus_warning"),ex);
			return "";			
		}
	}
	
	private String calculateFocusPropertyId() throws XavaException { 
		String prefix = Is.emptyString(getMemberName())?
			"xava." + getModelName() + ".":
			"xava." + getModelName() + "." + getMemberName() + ".";
			
		if (Is.emptyString(focusPropertyId)) {
			return getFirsEditablePropertyId(prefix);
		}
		else {			
			String focusPropertyName = focusPropertyId.substring(prefix.length());			
			int idx = focusPropertyName.indexOf('.'); 
			if (idx < 0) {			
				String name = getNextFocusPropertyName(focusPropertyName);				
				return name==null?getFirsEditablePropertyId(prefix):prefix + name;
			}
			else {
				String subviewName = focusPropertyName.substring(0, idx);
				String member = focusPropertyName.substring(idx + 1);
				View subview = getSubview(subviewName);				
				String name = subview.getNextFocusPropertyName(member);				
				if (name != null) return prefix + subviewName + "." + name;				
				name = getNextFocusPropertyName(subviewName);				
				return name==null?getFirsEditablePropertyId(prefix):prefix + name;				  
			}
		}
	}
	
	private String getFirsEditablePropertyId(String prefix) throws XavaException {
		Iterator it = getMetaMembers().iterator();
		while (it.hasNext()) {
			MetaMember m = (MetaMember) it.next();
			if (m instanceof MetaProperty) {
				if (PropertiesSeparator.INSTANCE.equals(m)) continue; 
				if (isEditable((MetaProperty) m)) {
					return prefix + m.getName();
				}
			}
			else if (m instanceof MetaGroup) {
				String result = getGroupView(m.getName()).getFirsEditablePropertyId(prefix);
				if (result != null) return result;
			}
			else if (m instanceof MetaReference) {
				String result = getSubview(m.getName()).getFirsEditablePropertyId(prefix + m.getName() + ".");
				if (result != null) return result;
			}
			
		}		
		if (hasSections()) {
			return getSectionView(getActiveSection()).getFirsEditablePropertyId(prefix);
		}
		return null;
	}

	private String getNextFocusPropertyName(String memberName) throws XavaException {		
		Iterator it = getMetaMembers().iterator();
		boolean found = false;		
		while (it.hasNext()) {
			MetaMember m = (MetaMember) it.next();			
			if (m instanceof MetaGroup) {												
				String name = getGroupView(m.getName()).getNextFocusPropertyName(memberName);
				if ("__FOUND__".equals(name)) {
					found = true; 
					continue;
				}
				else if (name != null) return name;
			}
						
			if (m.getName().equals(memberName)) {				
				found = true;
				continue;
			}
			if (!found) continue;
			if (m instanceof MetaGroup) {								
				String name = getGroupView(m.getName()).getFirsEditablePropertyId("");				
				if (name != null) return name;
			}			
			if (m instanceof MetaProperty &&
				!(m instanceof PropertiesSeparator) && 
				isEditable((MetaProperty) m)) {	
				return m.getName();
			}
			if (m instanceof MetaReference &&				 
				isEditable((MetaReference) m)) {
				if (displayAsDescriptionsList((MetaReference) m)) {
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
				else {				
					return getSubview(m.getName()).getFirsEditablePropertyId(m.getName() + ".");
				}
			}			
		}
		if (hasSections()) {
			return getSectionView(getActiveSection()).getNextFocusPropertyName(memberName);
		}	
		if (isGroup() && found) return "__FOUND__";		
		return null;
	}

	private void setIdFocusProperty(String string) {
		focusPropertyId = string;
	}

    /**
     * Sets the focus in the provided property
     */
    public void setFocus(String newFocusProperty) {    	
        focusPropertyId = "xava." + getModelName() + "." + newFocusProperty;
    }

	public String getEditCollectionElementAction() {		
		return editCollectionElementAction == null?"Collection.edit":editCollectionElementAction;
	}
	
	public String getViewCollectionElementAction() {		
		return viewCollectionElementAction == null?"Collection.view":viewCollectionElementAction;
	}
		
	public void setEditCollectionElementAction(String editCollectionElementAction) {		
		this.editCollectionElementAction = editCollectionElementAction;
	}
	
	public void setViewCollectionElementAction(String viewCollectionElementAction) {		
		this.viewCollectionElementAction = viewCollectionElementAction;
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
	
	public String getLabelFor(MetaMember p) throws XavaException {		
		if (getLabels() != null) {
			String idLabel = (String) getLabels().get(p.getName());
			if (idLabel != null) { 
				return Labels.get(idLabel, Locales.getCurrent());
			}
		}		
		if (!Is.emptyString(getMemberName())) {
			int idx = p.getId().lastIndexOf('.');
			if (idx < 0) idx = 0; 
			String id = p.getId().substring(0, idx) + "." + getMemberName() + p.getId().substring(idx);
			if (Labels.existsExact(id, Locales.getCurrent())) {								
				return Labels.get(id, Locales.getCurrent());
			}
			id = getMemberName() + p.getId().substring(idx);
			if (Labels.existsExact(id, Locales.getCurrent())) {				
				return Labels.get(id, Locales.getCurrent());
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
	
	private boolean isMetaProperty(Object member) { 
		return member instanceof MetaProperty && !member.equals(PropertiesSeparator.INSTANCE) && !(member instanceof MetaViewAction);
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

	private boolean isCollectionEditableFixed() {
		return collectionEditableFixed;
	}

	private void setCollectionEditableFixed(boolean collectionEditableFixed) {
		this.collectionEditableFixed = collectionEditableFixed;
	}

	public String getHideCollectionElementAction() { 		
		return hideCollectionElementAction == null?"Collection.hideDetail":hideCollectionElementAction;
	}

	public void setHideCollectionElementAction(String hideCollectionElementAction) {		
		this.hideCollectionElementAction = hideCollectionElementAction;
	}

	public String getNewCollectionElementAction() {  		
		if (!Is.emptyString(newCollectionElementAction)) return newCollectionElementAction;
		if (newCollectionElementAction != null) return "";
		return isRepresentsEntityReference()?"Collection.add":"Collection.new";
	}

	public void setNewCollectionElementAction(String newCollectionElementAction) {		
		this.newCollectionElementAction = newCollectionElementAction;
	}

	public String getRemoveCollectionElementAction() {
		return removeCollectionElementAction == null?"Collection.remove":removeCollectionElementAction; 
	}

	public void setRemoveCollectionElementAction(
			String removeCollectionElementAction) {		
		this.removeCollectionElementAction = removeCollectionElementAction;
	}
	
	public String getRemoveSelectedCollectionElementsAction() {		
		return removeSelectedCollectionElementsAction == null?"Collection.removeSelected":removeSelectedCollectionElementsAction; 
	}

	public void setRemoveSelectedCollectionElementsAction(
			String removeSelectedCollectionElementAction) {		
		this.removeSelectedCollectionElementsAction = removeSelectedCollectionElementAction;
	}
	
	public String getSaveCollectionElementAction() {		
		return saveCollectionElementAction == null?"Collection.save":saveCollectionElementAction;
	}

	public void setSaveCollectionElementAction(String saveCollectionElementAction) {
		this.saveCollectionElementAction = saveCollectionElementAction;
	}
	
	public boolean isAlignedByColumns() throws XavaException {		
		return getMetaView().isAlignedByColumns();
	}
	
	/**
	 * Associates an arbitrary object to this view. <p>
	 * 
	 * This object is not used by the view for any purpose,
	 * this is only a convenience method for developer.<br>
	 * 
	 * @param name The id to retrieve the object later.
	 * @param object The object to associate.
	 */
	public void putObject(String name, Object object) { 
		if (objects == null) objects = new HashMap();
		objects.put(name, object);
	}
	
	/**
	 * Retrieve an object associated to this view. <p>
	 * 
	 * The object have to be associated to this view using @link #putObject(String, Object)
	 * 
	 * @param name Name of the object to retrieve.
	 * @return The object, or null if it is not found.
	 */
	public Object getObject(String name) { 
		if (objects == null) return null;
		return objects.get(name);
	}
	
	/**
	 * Remove from this view an object previously associated with @link #putObject(String, Object)
	 * @param name Name of the object to remove.
	 */
	public void removeObject(String name) {
		if (objects == null) return;
		objects.remove(name);
	}

	private Collection getRowStyles() {
		return rowStyles;
	}

	private void setRowStyles(Collection rowStyles) {
		this.rowStyles = rowStyles;
	}

}
