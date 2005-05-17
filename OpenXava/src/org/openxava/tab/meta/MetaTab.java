package org.openxava.tab.meta;

import java.util.*;

import org.openxava.component.*;
import org.openxava.filters.*;
import org.openxava.filters.meta.*;
import org.openxava.mapping.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;

/**
 * 
 * @author Javier Paniza
 */

public class MetaTab implements java.io.Serializable, Cloneable {

	private String defaultOrder;
	private String sQLDefaultOrder;
	private String sQLBaseCondition;
	private String selectSQL;
	private Collection metaPropertiesHiddenCalculated;
	private Collection metaPropertiesHidden;
	private String name;
	private MetaComponent metaComponent;
	private List metaConsults = new ArrayList();
	private List propertiesNames = null;
	private List metaProperties = null;
	private List metaPropertiesCalculated = null;
	private String properties; // separated by commas, like in xml file
	private String select;
	private Collection entityReferencesMappings;	
	private Collection tableColumns;
	private String modelName; 
	private IMetaModel metaModel;
	private boolean excludeAll = false;
	private boolean excludeByKey = false;
	private MetaFilter metaFilter;
	private IFilter filter;
	private List hiddenPropertiesNames;
	private Collection hiddenTableColumns;
	private String baseCondition;
	private Map metaPropertiesTab;
	private Collection rowStyles;
	private String defaultPropertiesNames;
	
	public static String getTitleI18n(Locale locale, String modelName, String tabName) throws XavaException {
		String id = null;
		if (Is.emptyString(tabName)) {
			id = modelName + ".tab.title"; 
		}
		else {
			id = modelName + ".tabs." + tabName + ".title";
		}
		if (Labels.exists(id)) {
			return Labels.get(id, locale);
		}		
		else {
			return null;
		}
	}
	
	public IMetaModel getMetaModel() throws XavaException {
		return metaModel;
	}
	public void setMetaModel(IMetaModel metaModel) {
		this.metaModel = metaModel;
		this.metaComponent = metaModel.getMetaComponent();
		this.modelName = metaModel.getName();
	}

	public void addMetaConsult(MetaConsult consult) {
		metaConsults.add(consult);
		consult.setMetaTab(this);
	}

	/**
	 * @return Not null, read only and of type <tt>MetaConsult</tt>.
	 */
	public Collection getMetaConsults() {
		return Collections.unmodifiableCollection(metaConsults);
	}

	public static MetaTab createDefault(MetaComponent component)
			throws XavaException {
		MetaTab tab = new MetaTab();
		tab.setMetaComponent(component);
		tab.addDefaultMetaConsults();
		return tab;
	}

	/**
	 * @return Not null, read only and of type <tt>MetaProperty</tt>.
	 */
	public List getMetaProperties() throws XavaException {
		if (metaProperties == null) {
			metaProperties = namesToMetaProperties(getPropertiesNames());
		}
		return metaProperties;
	}

	public Collection getMetaPropertiesHidden() throws XavaException {
		if (metaPropertiesHidden == null) {
			metaPropertiesHidden = namesToMetaProperties(getHiddenPropertiesNames());
		}
		return metaPropertiesHidden;
	}

	public List namesToMetaProperties(Collection names) throws XavaException {
		List metaProperties = new ArrayList();
		Iterator it = names.iterator();
		int i = -1;
		while (it.hasNext()) {
			i++;
			String name = (String) it.next();
			MetaProperty metaPropertyTab = null;
			try {
				MetaProperty metaProperty = getMetaEntity().getMetaProperty(
						name).cloneMetaProperty();
				metaProperty.setQualifiedName(name);
				String idEtiqueta = getId() + ".properties." + name;
				if (Labels.exists(idEtiqueta)) {
					metaProperty.setLabelId(idEtiqueta);
				} else if (metaPropertiesTab != null) {
					// By now only the label overwrited from the property of tab 
					metaPropertyTab = (MetaProperty) metaPropertiesTab
							.get(name);
					if (metaPropertyTab != null) {
						metaProperty = metaProperty.cloneMetaProperty();
						metaProperty.setLabel(metaPropertyTab.getLabel());
					}
				}
				metaProperties.add(metaProperty);
			} catch (ElementNotFoundException ex) {
				MetaProperty notInEntity = new MetaProperty();
				notInEntity.setName(name);
				notInEntity.setTypeName("java.lang.Object");
				if (metaPropertyTab != null) {
					notInEntity.setLabel(metaPropertyTab.getLabel());
				}
				metaProperties.add(notInEntity);
			}
		}
		return metaProperties;
	}

	/**
	 * Hidden ones are not included
	 * 
	 * @return Not null, read only and of type <tt>MetaProperty</tt>.
	 */
	public Collection getMetaPropertiesCalculated() throws XavaException {
		if (metaPropertiesCalculated == null) {
			metaPropertiesCalculated = new ArrayList();
			Iterator it = getMetaProperties().iterator();
			while (it.hasNext()) {
				MetaProperty metaProperty = (MetaProperty) it.next();
				if (metaProperty.isCalculated()) {					
					metaPropertiesCalculated.add(metaProperty);
				}
			}
		}
		return metaPropertiesCalculated;
	}

	/**
	 * 
	 * @return Not null, read only and of type <tt>MetaProperty</tt>.
	 */
	public Collection getMetaPropertiesHiddenCalculated() throws XavaException {
		if (metaPropertiesHiddenCalculated == null) {
			metaPropertiesHiddenCalculated = new ArrayList();
			Iterator it = getMetaPropertiesHidden().iterator();
			while (it.hasNext()) {
				MetaProperty metaProperty = (MetaProperty) it.next();
				if (metaProperty.isCalculated()) {
					metaPropertiesHiddenCalculated.add(metaProperty);
				}
			}
		}
		return metaPropertiesHiddenCalculated;
	}

	public boolean hasCalculatedProperties() throws XavaException {
		return !getMetaPropertiesCalculated().isEmpty();
	}

	/**
	 * @return Not null, read only and of type <tt>String</tt>.
	 */
	public Collection getTableColumns() throws XavaException {
		if (tableColumns == null) {
			tableColumns = getTableColumns(getPropertiesNames());
		}
		return tableColumns;
	}

	/**
	 * @return Not null, read only and of type <tt>String</tt>.
	 */
	public Collection getHiddenTableColumns() throws XavaException {
		if (hiddenTableColumns == null) {
			hiddenTableColumns = getTableColumns(getHiddenPropertiesNames());
			hiddenTableColumns
					.addAll(getCmpFieldsColumnsInMultipleProperties());
		}
		return hiddenTableColumns;
	}

	private Collection getTableColumns(Collection propertyNames)
			throws XavaException {
		Collection tableColumns = new ArrayList();
		Iterator it = propertyNames.iterator();
		while (it.hasNext()) {
			String name = (String) it.next();
			try {
				tableColumns
						.add(getMapping().getQualifiedColumn(name));
			} 
			catch (ElementNotFoundException ex) {
				tableColumns.add("0"); // It will be replaced
			}
		}
		return tableColumns;
	}

	/**
	 * @return Not null, read only of type <tt>String</tt>.
	 */
	public List getPropertiesNames() throws XavaException {
		if (propertiesNames == null) {
			if (!areAllProperties()) {
				propertiesNames = createPropertiesNames();
			} 
			else {
				propertiesNames = createAllPropertiesNames();
			}
		}
		return propertiesNames;
	}

	/**
	 * Names of properties that must to exist but is not needed show they
	 * to users.
	 * <p>
	 * Usually are properties used to calcualte others. <br>
	 * The keys are excluded. <br>
	 * 
	 * @return Not null, read only and of type <tt>String</tt>.
	 */
	public List getHiddenPropertiesNames() throws XavaException {
		if (hiddenPropertiesNames == null) {
			hiddenPropertiesNames = obtainPropertiesNamesUsedToCalculate();
			hiddenPropertiesNames
					.addAll(obtainPropertiesNamesUsedInOrderBy());
		}
		return hiddenPropertiesNames;
	}

	private Collection obtainPropertiesNamesUsedInOrderBy()
			throws XavaException {
		List result = new ArrayList();
		Iterator itConsults = getMetaConsults().iterator();
		while (itConsults.hasNext()) {
			MetaConsult consult = (MetaConsult) itConsults.next();
			if (consult.useOrderBy()) {
				Iterator itProperties = consult.getOrderByPropertiesNames()
						.iterator();
				while (itProperties.hasNext()) {
					String property = (String) itProperties.next();
					if (!getPropertiesNames().contains(property)
							&& !hiddenPropertiesNames.contains(property)
							&& !result.contains(property)
							&& !getMetaModel().isKey(property)) {
						result.add(property);
					}
				}
			}
		}
		return result;
	}

	private List obtainPropertiesNamesUsedToCalculate()
			throws XavaException {
		Set result = new HashSet();
		Iterator itProperties = getMetaPropertiesCalculated().iterator();
		while (itProperties.hasNext()) {
			MetaProperty metaProperty = (MetaProperty) itProperties.next();
			if (!metaProperty.hasCalculator())
				continue;
			MetaSetsContainer metaCalculator = metaProperty
					.getMetaCalculator();
			if (!metaCalculator.containsMetaSets())
				continue;
			Iterator itSets = metaCalculator.getMetaSets().iterator();
			while (itSets.hasNext()) {
				MetaSet set = (MetaSet) itSets.next();
				String propertyNameFrom = set.getPropertyNameFrom();
				if (!Is.emptyString(propertyNameFrom)
						&& !getPropertiesNames().contains(propertyNameFrom)) {
					String qualifiedName = metaProperty.getQualifiedName();
					int idx = qualifiedName.indexOf('.');
					if (idx < 0) {
						result.add(propertyNameFrom);
					}
					else {
						String ref = qualifiedName.substring(0, idx + 1);
						result.add(ref + propertyNameFrom);
					}
				}
			}
		}
		return new ArrayList(result);
	}

	private boolean areAllProperties() {
		return properties == null || properties.trim().equals("*");
	}

	// assert(!areAllProperties());
	private List createPropertiesNames() {
		StringTokenizer st = new StringTokenizer(properties, ",");
		List result = new ArrayList();
		while (st.hasMoreTokens()) {
			result.add(st.nextToken().trim());
		}
		return result;
	}

	private List createAllPropertiesNames() throws XavaException {
		return getMetaEntity().getPropertiesNamesWithoutHidden();
	}
	
	public void setDefaultPropertiesNames(String properties) {
		this.defaultPropertiesNames = properties;
		setPropertiesNames(properties);
	}

	/**
	 * Comma separated.
	 */
	public void setPropertiesNames(String properties) {
		this.properties = properties;

		this.propertiesNames = null;
		this.metaProperties = null;
		this.metaPropertiesHiddenCalculated = null;
		this.metaPropertiesHidden = null;
		this.metaPropertiesCalculated = null;
		this.entityReferencesMappings = null;
		this.entityReferencesMappings = null;
		this.tableColumns = null;
		this.hiddenPropertiesNames = null;
		this.hiddenTableColumns = null;
		this.metaPropertiesTab = null;
		
		this.select = null; 
		this.selectSQL = null; 
	}

	ModelMapping getMapping() throws XavaException {
		return getMetaModel().getMapping();
	}

	public String getSelect() throws XavaException {
		if (select == null) {
			select = createSelect();
		}
		return select;
	}

	public String getSelectSQL() throws XavaException {
		if (selectSQL == null) {			
			selectSQL = getMapping().changePropertiesByColumns(
					getSelect());			
		}		
		return selectSQL;
	}

	private String createSelect() throws XavaException {
		if (hasBaseCondition()) {
			String baseCondition = getBaseCondition();
			if (baseCondition.trim().toUpperCase().startsWith("SELECT ")) {
				return baseCondition;
			}
		}
		// baseic select
		StringBuffer select = new StringBuffer("select ");
		Iterator itProperties = getPropertiesNames().iterator();
		while (itProperties.hasNext()) {
			select.append("${");
			select.append(itProperties.next());
			select.append('}');
			if (itProperties.hasNext())
				select.append(", ");
		}
		Iterator itHiddenProperties = getHiddenPropertiesNames().iterator();
		while (itHiddenProperties.hasNext()) {
			select.append(", ");
			select.append("${");
			select.append(itHiddenProperties.next());
			select.append('}');
		}
		Iterator itCmpFieldsColumnsInMultipleProperties = getCmpFieldsColumnsInMultipleProperties()
				.iterator();
		while (itCmpFieldsColumnsInMultipleProperties.hasNext()) {
			select.append(", ");
			select.append(itCmpFieldsColumnsInMultipleProperties.next());
		}
		select.append(" from ");
		select.append(getMapping().getTable());
		// for the references		
		if (hasReferences()) {
			// the tables

			Iterator itReferencesMappings = getEntityReferencesMappings()
					.iterator();
			while (itReferencesMappings.hasNext()) {
				ReferenceMapping referenceMapping = (ReferenceMapping) itReferencesMappings.next();				
				select.append(" left join ");				
				select.append(referenceMapping.getReferencedTable());
				// where of join
				select.append(" on ");
				Iterator itDetails = referenceMapping.getDetails().iterator();
				while (itDetails.hasNext()) {
					ReferenceMappingDetail detail = (ReferenceMappingDetail) itDetails
							.next();
					select.append(detail.getQualifiedColumn());
					select.append(" = ");
					select.append(detail
							.getQualifiedColumnOfReferencedTable());
					if (itDetails.hasNext()) {
						select.append(" and ");
					}
				}
			}
		}

		select.append(' ');
		if (hasBaseCondition()) {
			select.append(" where ");
			select.append(getBaseCondition());
		}		
		return select.toString();
	}

	private Collection getCmpFieldsColumnsInMultipleProperties()
			throws XavaException {
		Collection cmpFieldsColumnsInMultipleProperties = new ArrayList();
		Iterator it = getMetaProperties().iterator();
		String table = getMapping().getTable();
		while (it.hasNext()) {
			MetaProperty p = (MetaProperty) it.next();
			PropertyMapping mapping = p.getMapping();
			if (mapping != null) {
				if (mapping.hasMultipleConverter()) {
					Iterator itFields = mapping.getCmpFields().iterator();
					while (itFields.hasNext()) {
						CmpField field = (CmpField) itFields.next();
						cmpFieldsColumnsInMultipleProperties.add(table + "."
								+ field.getColumn());
					}
				}
			}
		}
		return cmpFieldsColumnsInMultipleProperties;
	}

	public boolean hasReferences() throws XavaException {
		return !getEntityReferencesMappings().isEmpty();
	}
	
	private Collection getEntityReferencesMappings() throws XavaException {	
		if (entityReferencesMappings == null) {
			entityReferencesMappings = new ArrayList(); 
			for (Iterator itProperties = getPropertiesNames().iterator(); itProperties.hasNext();) {
				String property = (String) itProperties.next();
				fillEntityReferencesMappings(entityReferencesMappings, property, getMetaComponent().getMetaEntity());
			}
			for (Iterator itProperties = getHiddenPropertiesNames().iterator(); itProperties.hasNext();) {
				String property = (String) itProperties.next();
				fillEntityReferencesMappings(entityReferencesMappings, property, getMetaComponent().getMetaEntity());
			}						
		}
		return entityReferencesMappings;
	}
	
	private void fillEntityReferencesMappings(Collection result, String property, MetaModel metaModel) throws XavaException {		
		int idx = property.indexOf('.');				
		if (idx >= 0) {
			String referenceName = property.substring(0, idx);					
			MetaReference ref = metaModel.getMetaReference(referenceName);
			String memberName = property.substring(idx + 1);
			boolean hasMoreLevels = memberName.indexOf('.') >= 0;
			if (!ref.isAggregate()) {												
				if (hasMoreLevels || !ref.getMetaModelReferenced().isKey(memberName)) {
					ReferenceMapping rm = metaModel.getMapping().getReferenceMapping(referenceName);
					if (!result.contains(rm))	result.add(rm);
				}
			}			
			 
			if (hasMoreLevels) {
				fillEntityReferencesMappings(result, memberName, MetaComponent.get(ref.getReferencedModelName()).getMetaEntity());
			}
		}		
	}
		
	public void addDefaultMetaConsults() throws XavaException {
		if (!isExcludeByKey())
			addPrimaryKeyMetaConsult();
		if (!isExcludeAll())
			addAllMetaConsult();
	}

	private void addAllMetaConsult() {
		MetaConsult all = new MetaConsult();
		all.setName("todos"); // in spanish because this feature is used only in spanish/swing version
		all.setCondition("");
		addMetaConsult(all);
	}

	private void addPrimaryKeyMetaConsult() throws XavaException {
		Collection properties = getMetaModel().getMetaPropertiesKey();
		if (properties.isEmpty())
			return;
		Iterator it = properties.iterator();
		MetaConsult byKey = new MetaConsult();
		byKey.setMetaTab(this);
		while (it.hasNext()) {
			MetaProperty property = (MetaProperty) it.next();
			MetaParameter parameter = new MetaParameter();
			parameter.setPropertyName(property.getName());
			byKey.addMetaParameter(parameter);
		}
		metaConsults.add(0, byKey);
	}

	private MetaEntity getMetaEntity() throws XavaException {
		return getMetaComponent().getMetaEntity();
	}

	public MetaComponent getMetaComponent() {
		return metaComponent;
	}

	public void setMetaComponent(MetaComponent component) {
		this.metaComponent = component;
		this.metaModel = this.metaComponent.getMetaEntity();
		this.modelName = this.metaComponent.getName();
	}

	public boolean isExcludeByKey() {
		return excludeByKey;
	}

	public boolean isExcludeAll() {
		return excludeAll;
	}

	public void setExcludeByKey(boolean excludeByKey) {
		this.excludeByKey = excludeByKey;
	}

	public void setExcludeAll(boolean excludeAll) {
		this.excludeAll = excludeAll;
	}

	public String getName() {
		return name == null ? "" : name;
	}

	private boolean hasName() {
		return name != null && !name.trim().equals("");
	}

	public void setName(String name) {
		this.name = name;
	}

	public MetaFilter getMetaFilter() {
		return metaFilter;
	}
	public void setMetaFilter(MetaFilter metaFilter) {
		this.metaFilter = metaFilter;
	}

	public boolean hasFilter() {
		return this.metaFilter != null;
	}

	/**
	 * Apply the filter associated to this tab if there is is.
	 */
	Object filterParameters(Object o) throws XavaException {
		if (getMetaFilter() == null)
			return o;
		return getFilter().filter(o);
	}

	private IFilter getFilter() throws XavaException {
		if (filter == null) {
			filter = getMetaFilter().createFilter();
		}
		return filter;
	}

	public void addMetaProperty(MetaProperty metaProperty) {
		if (metaPropertiesTab == null) {
			metaPropertiesTab = new HashMap();
		}
		metaPropertiesTab.put(metaProperty.getName(), metaProperty);
	}

	/**
	 * For dynamically add properties to this tab
	 */
	public void addProperty(String propertyName) {
		if (propertiesNames == null)
			return;
		propertiesNames.add(propertyName);
		resetAfterAddRemoveProperty();
	}

	/**
	 * For dynamically add properties to this tab
	 */
	public void addProperty(int index, String propertyName) {
		if (propertiesNames == null)
			return;
		propertiesNames.add(index, propertyName);
		resetAfterAddRemoveProperty();
	}

	/**
	 * For dynamically remove properties to this tab
	 */
	public void removeProperty(String propertyName) {
		if (propertiesNames == null)
			return;
		propertiesNames.remove(propertyName);
		resetAfterAddRemoveProperty();
	}

	/**
	 * For dynamically remove properties to this tab
	 */
	public void removeProperty(int index) {
		if (propertiesNames == null)
			return;
		propertiesNames.remove(index);
		resetAfterAddRemoveProperty();
	}

	public void movePropertyToRight(int index) {
		if (propertiesNames == null)
			return;
		if (index >= propertiesNames.size() - 1)
			return;
		Object aux = propertiesNames.get(index);
		propertiesNames.set(index, propertiesNames.get(index + 1));
		propertiesNames.set(index + 1, aux);
		resetAfterAddRemoveProperty();
	}

	public void movePropertyToLeft(int index) {
		if (propertiesNames == null)
			return;
		if (index <= 0)
			return;
		Object aux = propertiesNames.get(index);
		propertiesNames.set(index, propertiesNames.get(index - 1));
		propertiesNames.set(index - 1, aux);
		resetAfterAddRemoveProperty();
	}

	/**
	 * For dynamically remove all properties to this tab
	 */
	public void clearProperties() { 
		if (propertiesNames == null)
			return;
		propertiesNames.clear();
		resetAfterAddRemoveProperty();
	}
	
	public void restoreDefaultProperties() { 
		setPropertiesNames(defaultPropertiesNames); 
		resetAfterAddRemoveProperty();
	}
	
	private void resetAfterAddRemoveProperty() {
		selectSQL = null;
		metaProperties = null;
		metaPropertiesCalculated = null;
		select = null;
		entityReferencesMappings = null;
		entityReferencesMappings = null;
		tableColumns = null;
		hiddenPropertiesNames = null;
		hiddenTableColumns = null;
	}

	public String getBaseCondition() {
		return baseCondition == null ? "" : baseCondition;
	}

	public void setBaseCondition(String string) {
		baseCondition = string;
		sQLBaseCondition = null;
	}

	public String getSQLBaseCondition() throws XavaException {
		if (sQLBaseCondition == null) {
			sQLBaseCondition = getMapping().changePropertiesByColumns(
					getBaseCondition());
		}
		return sQLBaseCondition;
	}

	public boolean hasBaseCondition() {
		return !Is.emptyString(this.baseCondition);
	}

	/**
	 * Apply the tab filter to sent objects.
	 * <p>
	 * It's used to filter arguments. <br>
	 */
	public Object filter(Object[] objects) throws FilterException,
			XavaException {
		if (getMetaFilter() == null)
			return objects;
		return getMetaFilter().filter(objects);
	}

	public String getId() {
		if (!hasName())
			return getMetaComponent().getName() + ".tab";
		return getMetaComponent().getName() + ".tabs." + getName();
	}

	public String getDefaultOrder() {
		return defaultOrder;
	}

	public void setDefaultOrder(String defaultOrder) {
		this.defaultOrder = defaultOrder;
		this.sQLDefaultOrder = null;
	}

	public String getSQLDefaultOrder() throws XavaException {
		if (sQLDefaultOrder == null) {
			sQLDefaultOrder = getMapping().changePropertiesByColumns(
					getDefaultOrder());
		}
		return sQLDefaultOrder;
	}

	public boolean hasDefaultOrder() {
		return !Is.emptyString(this.defaultOrder);
	}

	public MetaTab cloneMetaTab() {
		try {
			MetaTab r = (MetaTab) clone();
			if (r.metaPropertiesHiddenCalculated != null) {
				r.metaPropertiesHiddenCalculated = new ArrayList(
						metaPropertiesHiddenCalculated);
			}
			if (r.metaPropertiesHidden != null) {
				r.metaPropertiesHidden = new ArrayList(metaPropertiesHidden);
			}
			if (r.metaConsults != null) {
				r.metaConsults = new ArrayList(metaConsults);
			}
			if (r.propertiesNames != null) {
				r.propertiesNames = new ArrayList(propertiesNames);
			}
			if (r.metaProperties != null) {
				r.metaProperties = new ArrayList(metaProperties);
			}
			if (r.metaPropertiesCalculated != null) {
				r.metaPropertiesCalculated = new ArrayList(
						metaPropertiesCalculated);
			}
			if (r.entityReferencesMappings != null) {
				r.entityReferencesMappings = new ArrayList(entityReferencesMappings);
			}
			if (r.entityReferencesMappings != null) {
				r.entityReferencesMappings = new ArrayList(
						entityReferencesMappings);
			}
			if (r.tableColumns != null) {
				r.tableColumns = new ArrayList(tableColumns);
			}
			if (r.hiddenPropertiesNames != null) {
				r.hiddenPropertiesNames = new ArrayList(hiddenPropertiesNames);
			}
			if (r.hiddenTableColumns != null) {
				r.hiddenTableColumns = new ArrayList(hiddenTableColumns);
			}
			if (r.metaPropertiesTab != null) {
				r.metaPropertiesTab = new HashMap(metaPropertiesTab);
			}
			return r;
		} catch (CloneNotSupportedException ex) {
			throw new RuntimeException(XavaResources.getString("clone_error",
					getClass()));
		}
	}

	public Collection getRemainingPropertiesNames() throws XavaException {
		Collection result = new ArrayList(getMetaModel().getRecursiveQualifiedPropertiesNames());
		result.removeAll(getPropertiesNames());
		return result;
	}

	public void addMetaRowStyle(MetaRowStyle style) {
		if (rowStyles == null) rowStyles = new ArrayList();
		rowStyles.add(style);
	}
	
	public boolean hasRowStyles() {
		return rowStyles != null;
	}
	
	public Collection getMetaRowStyles() {
		return rowStyles==null?Collections.EMPTY_LIST:rowStyles;
	}

	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) throws XavaException {
		this.modelName = modelName;		
		this.metaModel = MetaModel.get(modelName);
		this.metaComponent = this.metaModel.getMetaComponent();
	}
}

