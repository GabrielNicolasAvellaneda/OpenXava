package org.openxava.tab;

import java.sql.*;
import java.text.*;
import java.util.*;

import javax.servlet.http.*;

import org.hibernate.*;
import org.hibernate.cfg.*;

import org.openxava.component.*;
import org.openxava.converters.*;
import org.openxava.filters.*;
import org.openxava.mapping.*;
import org.openxava.model.meta.*;
import org.openxava.tab.impl.*;
import org.openxava.tab.meta.*;
import org.openxava.util.*;


/**
 * Session object to work with tabular data. <p> 
 * 
 * @author Javier Paniza
 */

public class Tab {

	private final static int DEFAULT_PAGE_ROW_COUNT = 10;	
	private final static String STARTS_COMPARATOR = "starts_comparator";
	private final static String CONTAINS_COMPARATOR = "contains_comparator";
	private final static String YEAR_COMPARATOR = "year_comparator";
	private final static String MONTH_COMPARATOR = "month_comparator";
	
	private int pageRowCount = DEFAULT_PAGE_ROW_COUNT;
	private TabUserPreferences userPreferences;
	private Object [] titleArguments;
	private List metaPropertiesNotCalculated;
	private ReferenceMapping referencesCollectionMapping;
	private Object[] baseConditionValuesForReference;
	private String baseCondition;
	private String baseConditionForReference;
	private MetaTab metaTab;
	private boolean descendingOrder = false;
	private String orderBy;	
	private String condition;
	private String[] conditionComparators;
	private String[] conditionValues;
	private List metaProperties;
	private int page = 1;
	private boolean notResetNextTime = false;
	private int initialIndex;	 			
	private IXTableModel tableModel;	
	private int [] selected;
	private String modelName;
	private String tabName;
	private HttpServletRequest request;
	private boolean metaTabCloned = false;
	private boolean titleVisible = false;
	private List metaPropertiesKey;
	private boolean customize;
	private String titleId = null;
	private static SessionFactory sessionFactory;
	private boolean notResetPageNextTime;
	private boolean sortRemainingProperties;
	private boolean rowsHidden;
	
	public List getMetaProperties() {
		if (metaProperties == null) {
			if (Is.emptyString(getModelName())) return Collections.EMPTY_LIST;
			try {				
				metaProperties = getMetaTab().getMetaProperties();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				System.err.println(XavaResources.getString("tab_metaproperties_warning"));
			}
		}		
		return metaProperties;
	}
	
	public Collection getRemainingPropertiesNames() throws XavaException {
		if (isSortRemainingProperties()) {
			List result = new ArrayList(getMetaTab().getRemainingPropertiesNames());
			Collections.sort(result);
			return result;
		}
		else {
			return getMetaTab().getRemainingPropertiesNames();
		}
	}
	
	public List getMetaPropertiesNotCalculated() throws XavaException {
		if (metaPropertiesNotCalculated == null) {
			metaPropertiesNotCalculated = new ArrayList();
			Iterator it = getMetaProperties().iterator();			
			while (it.hasNext()) {
				MetaProperty p = (MetaProperty) it.next();
				if (!p.isCalculated()) {
					metaPropertiesNotCalculated.add(p);
				}								
			}
		}
		return metaPropertiesNotCalculated;
	}
	
	
	public String getBaseCondition() throws XavaException {
		return baseCondition;
	}
	
	private String getSQLBaseCondition() throws XavaException { 		
		return getMetaTab().getMetaModel().getMapping().changePropertiesByColumns(getBaseCondition());
	}
	
	private String getSQLBaseConditionForReference() throws XavaException { 		
		return getMetaTab().getMetaModel().getMapping().changePropertiesByColumns(getBaseConditionForReference());
	}
	
	
	public void setBaseCondition(String condition) throws XavaException { 		
		if (Is.equal(this.baseCondition, condition)) return;
		this.baseCondition = condition;		
		this.condition = null;
	}
		
	/**
	 * This is an alternative to setModelName, and is used when this
	 * tab represent a collection of references.
	 */
	public void setReferencesCollection(String model, String collectionName) throws XavaException {
		MetaModel metaModel = MetaComponent.get(model).getMetaEntity(); 
		MetaReference ref = metaModel.getMetaCollection(collectionName).getMetaReference();
		setModelName(ref.getReferencedModelName());		
		referencesCollectionMapping = ref.getMetaModelReferenced().getMapping().getReferenceMapping(ref.getRole());
		createBaseConditionForReference();
		cloneMetaTab();
		getMetaTab().setPropertiesNames("*");
	}
	
	private void createBaseConditionForReference() throws XavaException {				
		Iterator it = referencesCollectionMapping.getDetails().iterator();
		StringBuffer condition = new StringBuffer();
		while (it.hasNext()) {
			ReferenceMappingDetail detail = (ReferenceMappingDetail) it.next();
			condition.append(detail.getColumn());
			condition.append(" = ?");
			if (it.hasNext()) condition.append(" and "); 
		}
		setBaseConditionForReference(condition.toString());
	}
	
	public void setBaseConditionValuesForReference(Map values) throws XavaException { 
		ReferenceMapping mapping = referencesCollectionMapping;
		Iterator it = mapping.getDetails().iterator();
		baseConditionValuesForReference = new Object[mapping.getDetails().size()];
		for (int i=0; i<baseConditionValuesForReference.length; i++) {
			ReferenceMappingDetail detalle = (ReferenceMappingDetail) it.next();
			baseConditionValuesForReference[i] = values.get(detalle.getReferencedModelProperty());
		}		
	}
		
	private void cloneMetaTab() throws XavaException { 
		if (metaTabCloned) return;		
		metaTab = getMetaTab().cloneMetaTab();		
		metaTabCloned = true;
	}
		
	private MetaTab getMetaTab() throws XavaException  {
		if (metaTab == null) {				
			metaTab = MetaComponent.get(getModelName()).getMetaTab(getTabName());			
		}
		return metaTab;
	}
	
	/**
	 * @return Comma separate list. <p>
	 */
	public String getPropertiesNamesAsString() {
		StringBuffer names = new StringBuffer();
		for (Iterator it=getMetaProperties().iterator(); it.hasNext();) {
			MetaProperty p = (MetaProperty) it.next();
			names.append(p.getQualifiedName());
			if (it.hasNext()) {
				names.append(",");
			}
		}		
		return names.toString();
	}
		
	public MetaProperty getMetaProperty(int i) {
		return (MetaProperty) getMetaProperties().get(i);
	}
	
	/**
	 * A table model with on-demand data reading. <p>
	 * 
	 * Suitable for UI.
	 */
	public IXTableModel getTableModel() throws Exception {		
		if (tableModel == null) {
			try {
				tableModel = createTableModel();			
			}
			catch (Exception ex) {
				restoreDefaultProperties(); // if fails because user customized list uses properties no longer existing 
				tableModel = createTableModel();
			}
		}
		return tableModel;
	}
	
	private IXTableModel createTableModel() throws Exception {
		IXTableModel tableModel = null;
		IEntityTab tab = EntityTabFactory.create(getMetaTab());
		tab.search(getCondition(), getKey());
		tableModel = tab.getTable();
		
		// To load data, thus it's possible go directly to other page than first				
		int limit = getPage() * getPageRowCount();
		for (int row=0; row < limit; row += getPageRowCount() ) {
			tableModel.getValueAt(row,0);
		}
		return tableModel;
	}
	
	/**
	 * A table model with load all data at once. <p>
	 * 
	 * Suitable for report generation (for example).
	 */
	public IXTableModel getAllDataTableModel() throws Exception {								
		IEntityTab tab = EntityTabFactory.createAllData(getMetaTab());				
		tab.search(getCondition(), getKey());
		return tab.getTable();					
	}
	
	
	public void setTableModel(IXTableModel tableModel) {
		this.tableModel = tableModel;
	}
	
	private String getCondition() {
		if (condition == null) {
			try {
				condition = createCondition();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				System.err.println(XavaResources.getString("tab_condition_warning"));
				condition = "";
				conditionValues = null;				
				conditionComparators = null;					
			}
		}		
		return condition;
	}
	
	private String createCondition() throws Exception {
		StringBuffer sb = new StringBuffer();
		boolean firstCondition = true;
		metaPropertiesKey = null;
		if (!Is.emptyString(getBaseConditionForReference())) {
			sb.append(getSQLBaseConditionForReference());
			firstCondition = false;						
		}
		else if (!Is.emptyString(getBaseCondition())) {
			sb.append(getSQLBaseCondition());
			firstCondition = false;			
		}		
		if (!(conditionValues == null || conditionValues.length == 0)) {
			MetaProperty pOrder = null;
								
			for (int i = 0; i < this.conditionValues.length; i++) {				
				MetaProperty p = (MetaProperty) getMetaPropertiesNotCalculated().get(i);				
				if (orderBy != null && p.getQualifiedName().equals(orderBy)) {
					pOrder = p;
				}				
				if (Is.emptyString(this.conditionComparators[i])) {
					this.conditionValues[i] = ""; 
				}
				if (!Is.emptyString(this.conditionValues[i])) {												
					if (firstCondition) firstCondition = false;
					else sb.append(" and ");
					ModelMapping mapping = getMetaTab().getMetaModel().getMapping();					
					String column = mapping.getQualifiedColumn(p.getQualifiedName());					
					sb.append(decorateColumn(p, column, i));
					sb.append(' ');
					sb.append(convertComparator(p, this.conditionComparators[i]));
					sb.append(" ? ");
					if (metaPropertiesKey == null) metaPropertiesKey = new ArrayList(); 
					metaPropertiesKey.add(p);					
				}
			}		
			if (pOrder != null) {				
				if (sb.length() == 0) sb.append(" 1=1 ");
				sb.append(" order by ");								
				sb.append(getMetaTab().getMetaModel().getMapping().getQualifiedColumn(pOrder.getQualifiedName()));
				if (descendingOrder) {
					sb.append(" desc");
				}
			}				
			else if (getMetaTab().hasDefaultOrder()) {
				if (sb.length() == 0) sb.append(" 1=1 ");
				sb.append(" order by ");			
				sb.append(getMetaTab().getSQLDefaultOrder());					
			}
		}
		else if (getMetaTab().hasDefaultOrder()) {
			if (sb.length() == 0) sb.append(" 1=1 ");
			sb.append(" order by ");						
			sb.append(getMetaTab().getSQLDefaultOrder());									
		}				
		return sb.toString();
	}

	private String decorateColumn(MetaProperty p, String column, int i) throws XavaException {
		if ("year_comparator".equals(this.conditionComparators[i])) {
			return "year(" + column + ")";
		}
		if ("month_comparator".equals(this.conditionComparators[i])) {
			return "month(" + column + ")";
		}		
		if (java.lang.String.class.equals(p.getType())) {
			return "upper(" + column + ")";
		}
		return column;
	}


	private Object convertComparator(MetaProperty p, String comparator) throws XavaException {
		if (STARTS_COMPARATOR.equals(comparator)) return "like";
		if (CONTAINS_COMPARATOR.equals(comparator)) return "like";
		if (YEAR_COMPARATOR.equals(comparator)) return "=";
		if (MONTH_COMPARATOR.equals(comparator)) return "=";		
		if ("eq".equals(comparator)) {
			if (p.getType().equals(Timestamp.class)) {
				return "between ? and ";
			}
			else {
				return "=";
			}
		}
		if ("ne".equals(comparator)) return "<>";
		if ("ge".equals(comparator)) return ">=";
		if ("le".equals(comparator)) return "<=";
		if ("gt".equals(comparator)) return ">";
		if ("lt".equals(comparator)) return "<";
		return comparator;
	}
	
	private Object [] getKey() throws XavaException {
		if (conditionValues == null || conditionValues.length == 0) { 
			return filterKey(null);
		}
		Collection key = new ArrayList();
		
		for (int i = 0; i < this.conditionValues.length; i++) {
			String value = this.conditionValues[i];
			if (!Is.emptyString(value)) {
								
				if (STARTS_COMPARATOR.equals(this.conditionComparators[i])) {
					value = value.trim().toUpperCase() + "%";
					key.add(value);
				}
				else if (CONTAINS_COMPARATOR.equals(this.conditionComparators[i])) {
					value = "%" + value.trim().toUpperCase() + "%";
					key.add(value);
				} 
				else if (YEAR_COMPARATOR.equals(this.conditionComparators[i]) || MONTH_COMPARATOR.equals(this.conditionComparators[i])) {
					value = value.trim().toUpperCase();
					try {					
						key.add(new Integer(value));
					}
					catch (Exception ex) {
						ex.printStackTrace();
						System.err.println(XavaResources.getString("tab_key_value_warning"));
						key.add(null);
					}										
				}
				else {
					value = value.trim().toUpperCase();
					MetaProperty p = (MetaProperty) getMetaPropertiesNotCalculated().get(i);
					try {					
						key.add(p.parse(value));
					}
					catch (Exception ex) {
						ex.printStackTrace();
						System.err.println(XavaResources.getString("tab_key_value_warning"));
						key.add(null);
					}					
				}
				
			}
		}		
		return filterKey(key.toArray());
	}
	
	private Object[] filterKey(Object[] key) throws XavaException {
		// first, for references
		if (baseConditionValuesForReference != null && baseConditionValuesForReference.length > 0) {
			if (key==null) key=new Object[0];
			Object [] newKey = new Object[baseConditionValuesForReference.length + key.length];
			int j = 0;
			for (int i = 0; i < baseConditionValuesForReference.length; i++) {
				newKey[j++] = baseConditionValuesForReference[i];
			}
			for (int i = 0; i < key.length; i++) {
				 newKey[j++] = key[i];
			}			
			key = newKey;
		}		
		
		// Filter of meta tabs
		int indexIncrement = 0;
		if (getMetaTab().hasFilter()) {
			IFilter filter = getMetaTab().getMetaFilter().getFilter();
			if (filter instanceof IRequestFilter) {
				((IRequestFilter) filter).setRequest(request);
			}
			int original = key == null?0:key.length;
			key = (Object[]) filter.filter(key);
			indexIncrement = key == null?0:key.length - original;
		}
		
		// To db format		
		if (key != null && metaPropertiesKey != null) {			
			for (int i = indexIncrement; i < key.length; i++) {
				MetaProperty p = (MetaProperty) metaPropertiesKey.get(i - indexIncrement);
				// If has a converter, apply
												
				if (p.getMapping().hasConverter()) {
					try {	
						key[i] = p.getMapping().getConverter().toDB(key[i]);
					}
					catch (ConversionException ex) {
						if (!java.util.Date.class.isAssignableFrom(p.getType())) {
							// because Dates are special, maybe a year or a month and this
							// is not convertible by a date converter 
							throw ex;
						}
					}
				}
			}									
		}
		
		// Split Timestamp arguments
		if (hasTimestamp(key)) {
			key = filterTimestampInKey(key);
		}
		
		return key;
	}

	private Object[] filterTimestampInKey(Object[] key) {
		Collection result = new ArrayList();
		for (int i=0; i<key.length; i++) {
			if (key[i] instanceof java.sql.Timestamp) {				
				result.add(Dates.cloneWithoutTime((java.sql.Timestamp) key[i]));
				result.add(Dates.cloneWith2359((java.sql.Timestamp) key[i]));
			}
			else {
				result.add(key[i]);
			}
		}
		
		return result.toArray();
	}

	private boolean hasTimestamp(Object[] key) {
		if (key == null) return false;
		for (int i=0; i<key.length; i++) {
			if (key[i] instanceof java.sql.Timestamp) {
				return true;
			}
		}
		return false;
	}

	public void reset() {
		if (notResetNextTime) {
			notResetNextTime = false;
			return;
		}		
		tableModel = null;
		if (!notResetPageNextTime) { 
			notResetPageNextTime = false;
			initialIndex = 0; 		
			page = 1;	
		}
	}
	
	public int [] getSelected() {		
		return selected;
	}
	
	public boolean hasSelected() {
		return selected != null && selected.length > 0;
	}
	
	/**
	 * Change all selected. <p>
	 *
	 * Postcondition: <tt>this.selected == values</tt>
	 */
	public void setAllSelected(int [] values) {
		this.selected = values;
	}
	
	public void deselectVisualizedRows() { 
		List r = new ArrayList(); 				
		if (selected != null) {	
			for (int i=0; i<selected.length; i++) {
				int num = selected[i];
				if (num< getInitialIndex() || num>getFinalIndex()-1) {					
					r.add(new Integer(num));
				}
			}
		}

		Collections.sort(r);		
		selected = new int[r.size()];		
		for (int i=0; i<selected.length; i++) {			
			selected[i] = ((Integer) r.get(i)).intValue();			
		}				
	}

	/**
	 * Change the selelected ones only within the current page range. <p>
	 * 
	 * Postcondition <tt>this.selected == values</tt> <b>is not fulfilled</b> 	 
	 */
	public void setSelected(int [] values) {				
		List r = new ArrayList();
		if (selected != null) {	
			for (int i=0; i<selected.length; i++) {
				int num = selected[i];
				if (num< getInitialIndex() || num>getFinalIndex()-1) {					
					r.add(new Integer(num));
				}
			}
		}				
		for (int i=0; i<values.length; i++) {			
			r.add(new Integer(values[i]));			
		}		
		Collections.sort(r);		
		selected = new int[r.size()];		
		for (int i=0; i<selected.length; i++) {			
			selected[i] = ((Integer) r.get(i)).intValue();			
		}		
	}
	
	public void deselectAll() {
		selected = new int[0];
	}
		
	public boolean isSelected(int row) {
		if (selected == null || selected.length == 0) return false;
		return Arrays.binarySearch(selected, row) >= 0;
	}
	
	public int getInitialIndex() {		
		return initialIndex;
	}
	
	public int getFinalIndex() {		
		return initialIndex + getPageRowCount();
	}
	
	public boolean isLastPage() {
		return getFinalIndex() >= tableModel.getRowCount();
	}
	
	public void pageForward() {		
		goPage(page+1);		
	}

	public boolean isNotResetNextTime() {
		return notResetNextTime;
	}

	public void setNotResetNextTime(boolean b) {
		notResetNextTime = b;
	}
	
	public int getLastPage() {		
		return (tableModel.getRowCount() - 1) / getPageRowCount() + 1;
	}

	public void pageBack() {
		if (page < 1) page = 1;		
		goPage(page-1);		
	}


	/**
	 * 1 is the first page
	 * @param page
	 */
	public void goPage(int page) {
		this.page = page;
		recalculateIndices();
		notResetPageNextTime = true; 
	}
	
	private void recalculateIndices() {
		initialIndex = (page - 1) * getPageRowCount();		
	}


	public int getPage() {
		return page;
	}
	
	public int getTotalSize() {
		try {
			return getTableModel().getTotalSize(); 			
		}
		catch (Throwable ex) {
			ex.printStackTrace();			
			System.err.println(XavaResources.getString("tab_size_warning"));
			return -1;
		}
	}
	
	public void setConditionValues(String [] values) throws XavaException {
	  if (Arrays.equals(this.conditionValues, values)) return;
	  if (getMetaPropertiesNotCalculated().size() != values.length) return; // to avoid problems on changing module	  
	  this.conditionValues = values;
	  goPage(1);
	  rowsHidden = false;
	  condition = null;	  
	}
	
	public String [] getConditionValues() {
		return conditionValues; 
	}
	
	public void setConditionComparators(String [] comparators) throws XavaException {
		if (Arrays.equals(this.conditionComparators, comparators)) return; 		
		if (getMetaPropertiesNotCalculated().size() != comparators.length) return;		
		this.conditionComparators = comparators;
		condition = null;						
	}
	
	public String [] getConditionComparators() {
		return conditionComparators;
	}
	
	public void orderBy(String property) {
		if (Is.equal(property, orderBy)) {
			descendingOrder = !descendingOrder;
		}
		else {
			descendingOrder = false;
			orderBy = property;
		}
		condition = null;		
	}
	
	public String getOrderBy() {
		return orderBy;
	}
	
	public boolean isOrderAscending(String name) {
		return !descendingOrder && Is.equal(name, orderBy);
	}
	
	public boolean isOrderDescending(String name) {
		return descendingOrder && Is.equal(name, orderBy);
	}
	
	
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String newModelName) {
		if (Is.equal(modelName, newModelName)) return;
		modelName = newModelName;		
		reinitState();
		loadUserPreferences();
	}

	private void reinitState() {		
		descendingOrder = false;
		orderBy = null;	
		condition = null;		
		conditionComparators = null;
		conditionValues = null;
		metaProperties = null;
		metaPropertiesNotCalculated = null;		
		notResetNextTime = false;		 	 			
		tableModel  = null;	
		selected  = null;
		metaTab = null;
		metaTabCloned = false;
	}
	
	public String getTabName() {
		return tabName;
	}

	public void setTabName(String newTabName) {		
		if (Is.equal(tabName, newTabName)) return;
		tabName = newTabName;
		reinitState();		
		loadUserPreferences();
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;		
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}
	
	public boolean isTitleVisible() {
		return titleVisible;
	}

	public void setTitleVisible(boolean b) {
		titleVisible = b;
	}
	
	public void setTitleArgument(Object v1) {
		this.titleArguments = new Object [] { v1 };
	}
	public void setTitleArguments(Object v1, Object v2) {
		this.titleArguments = new Object [] { v1, v2 };
	}
	public void setTitleArguments(Object v1, Object v2, Object v3) {
		this.titleArguments = new Object [] { v1, v2, v3 };
	}
	public void setTitleArguments(Object v1, Object v2, Object v3, Object v4) {
		this.titleArguments = new Object [] { v1, v2, v3, v4 };
	}
	public void setTitleArguments(Object [] valores) {
		this.titleArguments = valores;
	}
	
	public String getTitle() throws XavaException {
		String modelName = getModelName();
		String tabName = getTabName();
		Locale locale = XavaResources.getLocale(request);		
		String title = titleId==null?getTitleI18n(locale, modelName, tabName):Labels.get(titleId, locale);
		if (title != null) return putTitleArguments(locale, title);
		String modelLabel = MetaComponent.get(modelName).getMetaEntity().getLabel();
		return XavaResources.getString(request, "report_title", modelLabel);					
	}
	
	private String putTitleArguments(Locale locale, String title) {
		if (titleArguments == null || titleArguments.length == 0) return title;
		MessageFormat formateador = new MessageFormat("");
		formateador.setLocale(locale);
		formateador.applyPattern(title);
		return formateador.format(titleArguments);		
	}

	public static String getTitleI18n(Locale locale, String modelName, String tabName) throws XavaException {
		return MetaTab.getTitleI18n(locale, modelName, tabName);
	}
		
	public String getBaseConditionForReference() {
		return baseConditionForReference;
	}
	public void setBaseConditionForReference(
			String baseConditionForReference) {
		this.baseConditionForReference = baseConditionForReference;
	}

	public void addProperty(String propertyName) throws XavaException {
		cloneMetaTab();
		getMetaTab().addProperty(propertyName);
		resetAfterChangeProperties();
		saveUserPreferences();
	}
	
	public void addProperty(int index, String propertyName) throws XavaException {
		cloneMetaTab();
		getMetaTab().addProperty(index, propertyName);
		resetAfterChangeProperties();
		saveUserPreferences();
	}
	
	public void addProperties(Collection properties) throws XavaException {
		cloneMetaTab();
		for (Iterator it=properties.iterator(); it.hasNext();) {
			getMetaTab().addProperty((String)it.next());
		}		
		resetAfterChangeProperties();
		saveUserPreferences();
	}
	
	

	public void removeProperty(String propertyName) throws XavaException {
		cloneMetaTab();
		getMetaTab().removeProperty(propertyName);
		resetAfterChangeProperties();
		saveUserPreferences();
	}
	
	public void removeProperty(int index) throws XavaException {
		cloneMetaTab();
		getMetaTab().removeProperty(index);
		resetAfterChangeProperties();
		saveUserPreferences();
	}	
	
	public void movePropertyToRight(int index) throws XavaException {
		cloneMetaTab();
		getMetaTab().movePropertyToRight(index);		
		resetAfterChangeProperties();
		saveUserPreferences();
	}
	
	public void movePropertyToLeft(int index) throws XavaException {
		cloneMetaTab();
		getMetaTab().movePropertyToLeft(index);
		resetAfterChangeProperties();
		saveUserPreferences();
	}
		
	public void clearProperties() throws XavaException {	
		cloneMetaTab();
		getMetaTab().clearProperties();		
		resetAfterChangeProperties();
		saveUserPreferences();
	}
	
	public void restoreDefaultProperties() throws XavaException { 	
		cloneMetaTab();
		getMetaTab().restoreDefaultProperties();		
		resetAfterChangeProperties();
		removeUserPreferences();
	}
	
	
	private void resetAfterChangeProperties() {
		reset(); 
		metaProperties = null;
		metaPropertiesNotCalculated = null;		
		metaPropertiesKey = null;		
		conditionValues = null;		
		orderBy = null;			
		condition = null;
		conditionComparators = null;		
	}
	 	
	public boolean isCustomize() {
		return customize;
	}
	public void setCustomize(boolean customize) {
		this.customize = customize;
	}
	
	public String getTitleId() {
		return titleId;
	}
	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}
	
	private void loadUserPreferences() {
		try {			
			Session session = getSessionFactory().openSession();				
			Transaction tx = session.beginTransaction();
			
			Query query = session.createQuery("from TabUserPreferences p where p.user = :user and p.tab = :tab");
			query.setString("user", getUserName());
			query.setString("tab", getMetaTab().getMetaModel().getName() + "." + getMetaTab().getName());
			userPreferences = (TabUserPreferences) query.uniqueResult();
		
			if (userPreferences != null) {										
				cloneMetaTab();
				getMetaTab().setPropertiesNames(userPreferences.getPropertiesNames());		
				resetAfterChangeProperties();
				rowsHidden = userPreferences.isRowsHidden(); 
			}
			
			tx.commit();
			session.close();			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(XavaResources.getString("warning_load_preferences_tab"));
		}			
	}
	
	private String getUserName() { 
		String user = Users.getCurrent();
		if (user != null) return user;
		return "openxava";
	}


	private void saveUserPreferences() {
		try {
			Session session = getSessionFactory().openSession();				
			Transaction tx = session.beginTransaction();
			
			if (userPreferences == null) {
				userPreferences = new TabUserPreferences();
				userPreferences.setUser(getUserName());
				userPreferences.setTab(getMetaTab().getMetaModel().getName() + "." + getMetaTab().getName());
			}
			
			userPreferences.setPropertiesNames(getPropertiesNamesAsString());
			userPreferences.setRowsHidden(rowsHidden); 
			session.saveOrUpdate(userPreferences);
			
			tx.commit();
			session.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(XavaResources.getString("warning_save_preferences_tab"));
		}	
	}
	
	private void removeUserPreferences() {
		if (userPreferences == null) return;
		try {
			Session session = getSessionFactory().openSession();				
			Transaction tx = session.beginTransaction();
			
			session.delete(userPreferences);
			
			tx.commit();
			session.close();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(XavaResources.getString("warning_save_preferences_tab"));
		}				
	}
	
	
	private static SessionFactory getSessionFactory() throws HibernateException {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure("/openxava-hibernate.cfg.xml").buildSessionFactory();
		}
		return sessionFactory;		
	}
	
	public String getStyle(Locale locale, int row) {
		try {
			if (!getMetaTab().hasRowStyles()) return null;
			for (Iterator it = getMetaTab().getMetaRowStyles().iterator(); it.hasNext();) {
				MetaRowStyle rowStyle = (MetaRowStyle) it.next();
				String result = getStyle(locale, rowStyle, row);
				if (result != null) return result;
			}
			return null;
		}
		catch (Exception ex) {
			System.err.println("[Tab.getStyle] " + XavaResources.getString("warning_row_style"));
			return null;
		}
	}
	
	
	private String getStyle(Locale locale, MetaRowStyle rowStyle, int row) {
		try {
			int column = getMetaTab().getPropertiesNames().indexOf(rowStyle.getProperty());			
			if (column < 0) return null;			
			Object value = getTableModel().getValueAt(row, column);
			if (Is.equalAsStringIgnoreCase(value, rowStyle.getValue())) {
				return rowStyle.getStyle();
			}
			return null;
		}
		catch (Exception ex) {			
			System.err.println("[Tab.getStyle] " + XavaResources.getString("warning_row_style"));
			return null;
		}
	}
		
	public boolean isSortRemainingProperties() {
		return sortRemainingProperties;
	}
	public void setSortRemainingProperties(boolean sortRemainingProperties) {
		this.sortRemainingProperties = sortRemainingProperties;
	}
	
	public Map [] getSelectedKeys() {
		if (selected == null) return new Map[0];
		Map [] keys = new Map[selected.length];
		for (int i = 0; i < keys.length; i++) {
			try {
				keys[i] = (Map) tableModel.getObjectAt(selected[i]);
			}
			catch (Exception ex) {
				keys[i] = Collections.EMPTY_MAP;
				System.err.println(XavaResources.getString("tab_row_key_warning", new Integer(i)));
			}
		}
		return keys;
	}


	public void hideRows() {		
		this.rowsHidden = true;
		saveUserPreferences();
	}	
	
	public void showRows() {
		this.rowsHidden = false;
		saveUserPreferences();
	}
	
	public boolean isRowsHidden() {
		return rowsHidden;
	}
	
	/**
	 * If you like show or hide rows is better calling to {@link #showRows} and {@link #hideRows}. <p>	 
	 */
	public void setRowsHidden(boolean rowsHidden) {
		this.rowsHidden = rowsHidden;
	}

	public int getPageRowCount() {
		return pageRowCount;
	}

	public void setPageRowCount(int pageRowCount) {
		this.pageRowCount = pageRowCount;
	}
	
}
