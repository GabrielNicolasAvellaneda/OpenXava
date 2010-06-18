package org.openxava.tab;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.component.MetaComponent;
import org.openxava.converters.ConversionException;
import org.openxava.converters.IConverter;
import org.openxava.filters.IFilter;
import org.openxava.filters.IRequestFilter;
import org.openxava.mapping.CmpField;
import org.openxava.mapping.ModelMapping;
import org.openxava.mapping.ReferenceMapping;
import org.openxava.mapping.ReferenceMappingDetail;
import org.openxava.model.meta.MetaModel;
import org.openxava.model.meta.MetaProperty;
import org.openxava.model.meta.MetaReference;
import org.openxava.tab.impl.EntityTabFactory;
import org.openxava.tab.impl.IEntityTab;
import org.openxava.tab.impl.IXTableModel;
import org.openxava.tab.meta.MetaRowStyle;
import org.openxava.tab.meta.MetaTab;
import org.openxava.util.CMPFieldComparator;
import org.openxava.util.Dates;
import org.openxava.util.ElementNotFoundException;
import org.openxava.util.Is;
import org.openxava.util.Labels;
import org.openxava.util.Users;
import org.openxava.util.XavaException;
import org.openxava.util.XavaPreferences;
import org.openxava.util.XavaResources;
import org.openxava.view.View;
import org.openxava.web.Ids;
import org.openxava.web.WebEditors;


/**
 * Session object to work with tabular data. <p> 
 * 
 * @author Javier Paniza
 */

public class Tab implements java.io.Serializable {
	
	private static final long serialVersionUID = 1724100598886966704L;

	/**
	 * Prefix used for naming (in session) to the tab objects used for collections.
	 */
	public final static String COLLECTION_PREFIX = "xava_collectionTab_";
	private final static String PROPERTIES_NAMES = "propertiesNames";
	private final static String ROWS_HIDDEN = "rowsHidden";
	private final static String FILTER_VISIBLE = "filterVisible";
	private final static String COLUMN_WIDTH = "columnWidth."; 
	
	
	private static Log log = LogFactory.getLog(Tab.class);
	
	private final static String STARTS_COMPARATOR = "starts_comparator";
	private final static String CONTAINS_COMPARATOR = "contains_comparator";
	private final static String YEAR_COMPARATOR = "year_comparator";
	private final static String MONTH_COMPARATOR = "month_comparator";
	private final static String YEAR_MONTH_COMPARATOR = "year_month_comparator"; 
	
	private int pageRowCount = XavaPreferences.getInstance().getPageRowCount();
	private int addColumnsPageRowCount = XavaPreferences.getInstance().getAddColumnsPageRowCount();
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
	private String[] conditionComparatorsToWhere;
	private Object[] conditionValuesToWhere;
	private List metaProperties;
	private int page = 1;
	private int addColumnsPage = 1; 
	private int addColumnsLastPage; 
	private boolean notResetNextTime = false;
	private int initialIndex;	 			
	private transient IXTableModel tableModel;	
	private int [] selected;
	private String modelName;
	private String tabName;
	private transient HttpServletRequest request; 
	private boolean metaTabCloned = false;
	private boolean titleVisible = false;
	private List metaPropertiesKey;
	private boolean customize;
	private String titleId = null;	
	private boolean notResetPageNextTime;
	private boolean sortRemainingProperties;
	private boolean rowsHidden;
	private IFilter filter; 
	private Map styles;
	private View collectionView;
	private boolean filterVisible=XavaPreferences.getInstance().isShowFilterByDefaultInList();
	private Map<String, Integer> columnWidths;
	
	private static int nextOid = 0; 
	public int oid = nextOid++; 
	
	public List<MetaProperty> getMetaProperties() {
		if (metaProperties == null) {
			if (Is.emptyString(getModelName())) return Collections.EMPTY_LIST;
			try {				
				metaProperties = getMetaTab().getMetaProperties();
			}
			catch (Exception ex) {
				log.error(XavaResources.getString("tab_metaproperties_warning"), ex);
			}
		}				
		return metaProperties;
	}
	
	private List getRemainingPropertiesNames() throws XavaException {
		if (isSortRemainingProperties()) {
			List result = new ArrayList(getMetaTab().getRemainingPropertiesNames());
			Collections.sort(result);
			return result;
		}
		else {
			return getMetaTab().getRemainingPropertiesNames();
		}
	}
	
	public Collection getColumnsToAdd() throws XavaException { 
		List remainingPropertiesNames = getRemainingPropertiesNames();
		int begin = (getAddColumnsPage() - 1) * getAddColumnsPageRowCount();
		addColumnsLastPage = (remainingPropertiesNames.size() - 1) / getAddColumnsPageRowCount() + 1;
		int end = begin + getAddColumnsPageRowCount();
		if (end > remainingPropertiesNames.size()) end = remainingPropertiesNames.size();
		return remainingPropertiesNames.subList(begin, end);
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
			try {				
				metaTab = MetaComponent.get(getModelName()).getMetaTab(getTabName());
			}
			catch (ElementNotFoundException ex) {
				if (getModelName().indexOf('.') >= 0 || // It's an aggregate
					getTabName().startsWith(COLLECTION_PREFIX) // Used for collection 
				) { 
					metaTab = MetaTab.createDefault(MetaModel.get(getModelName()));
					metaTab.setName(getTabName());
				}				
				else throw ex;
			}
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
	
	public int getColumnWidth(int columnIndex) { 
		if (columnWidths == null) return -1;
		Integer result = columnWidths.get(getMetaProperty(columnIndex).getQualifiedName());		
		return result==null?-1:result;
	}
	
	public void setColumnWidth(int columnIndex, int width) { 
		if (columnWidths == null) columnWidths = new HashMap<String, Integer>(); 
		columnWidths.put(getMetaProperty(columnIndex).getQualifiedName(), width);
		saveUserPreferences();
	}
	
		
	public MetaProperty getMetaProperty(int i) {
		return (MetaProperty) getMetaProperties().get(i);
	}
	
	public void setMetaRowStyles(Collection styles) throws XavaException {
		// WARNING! This will change the row style for all tab with this MetaTab
		getMetaTab().setMetaRowStyles(styles);
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
				log.error(ex.getMessage(), ex);
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
		if (tableModel.getColumnCount() > 0) { // Maybe we have a table model without columns, rare but possible			
			int limit = getPage() * getPageRowCount();
			for (int row=0; row < limit; row += getPageRowCount() ) {
				tableModel.getValueAt(row,0);
			}
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
		try {
			if (condition == null || getMetaTab().isDefaultSchemaChanged()) { 			
				condition = createCondition();
			}		
			return condition;
		}
		catch (Exception ex) {
			log.error(XavaResources.getString("tab_condition_warning"),ex);
			condition = "";
			conditionValues = null;				
			conditionComparators = null;
			return condition;
		}

	}
	
	private String createCondition() throws Exception {
		StringBuffer sb = new StringBuffer();
		boolean firstCondition = true;
		metaPropertiesKey = null;

		Collection<Object> valuesToWhere = new ArrayList<Object>();
		Collection<String> comparatorsToWhere = new ArrayList<String>();
		
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
					valuesToWhere.add("");
					comparatorsToWhere.add(this.conditionComparators[i]);
				}
				else if (!Is.empty(WebEditors.getEditorURLDescriptionsList(getTabName(), getModelName(), Ids.decorate(request, p.getQualifiedName()), i, getCollectionPrefix(), p.getQualifiedName(), p.getName()))){
					if (Is.empty(this.conditionValues[i])){
						comparatorsToWhere.add(this.conditionComparators[i]);
						valuesToWhere.add(this.conditionValues[i]);
						continue;
					}
					// by possible multiple key
					String reference = p.getQualifiedName().replace("." + p.getName(), "");
					List<CmpField> fields = (List) metaTab.getMetaModel().getMapping().getReferenceMapping(reference).getCmpFields();
					Collections.sort(fields, CMPFieldComparator.getInstance());
					
					ModelMapping mapping = getMetaTab().getMetaModel().getMapping();
					String alias = mapping.getTableToQualifyColumn() + ".";
					String keyValues = this.conditionValues[i].replaceAll("[\\[\\]]", "");
					StringTokenizer st = new StringTokenizer(keyValues, ".");
					for (CmpField field : fields) {
						String property = field.getCmpPropertyName().substring(field.getCmpPropertyName().lastIndexOf('_') + 1);
						String value = st.nextToken();
						
						valuesToWhere.add(getValueConverter(p, getMetaTab(), property, reference, value, field));
						comparatorsToWhere.add(this.conditionComparators[i]);
						
						if (firstCondition) firstCondition = false;
						else sb.append(" and ");
						sb.append(alias + field.getColumn());
						sb.append(' ');
						sb.append(convertComparator(p, this.conditionComparators[i]));
						sb.append(" ? ");
						
						if (metaPropertiesKey == null) metaPropertiesKey = new ArrayList();
						metaPropertiesKey.add(p);
					}
				}
				else if (!Is.emptyString(this.conditionValues[i])) {
					if (firstCondition) firstCondition = false;
					else sb.append(" and ");
					ModelMapping mapping = getMetaTab().getMetaModel().getMapping();
					String column = mapping.getQualifiedColumn(p.getQualifiedName());					
					sb.append(decorateColumn(p, column, i));
					sb.append(' ');
					sb.append(convertComparator(p, this.conditionComparators[i]));
					sb.append(" ? ");
					if (metaPropertiesKey == null) metaPropertiesKey = new ArrayList();
					if (YEAR_MONTH_COMPARATOR.equals(this.conditionComparators[i]) ||
						Timestamp.class.equals(p.getType()) && "eq".equals(this.conditionComparators[i])) {  
						metaPropertiesKey.add(null);
						metaPropertiesKey.add(null);
					}
					else {
						metaPropertiesKey.add(p);
					}
					
					String value = convertStringArgument(this.conditionValues[i].toString());
					try {				
						if (YEAR_COMPARATOR.equals(this.conditionComparators[i]) ||
							MONTH_COMPARATOR.equals(this.conditionComparators[i]) ||
							YEAR_MONTH_COMPARATOR.equals(this.conditionComparators[i])){
							valuesToWhere.add(value);
							comparatorsToWhere.add(this.conditionComparators[i]);
							continue;
						}
						Object v = p.parse(value.toString(), getLocale());
						if (v instanceof Timestamp && "eq".equals(this.conditionComparators[i])) {
							valuesToWhere.add(Dates.cloneWithoutTime((Timestamp) v));
							valuesToWhere.add(Dates.cloneWith2359((Timestamp) v));
							comparatorsToWhere.add(this.conditionComparators[i]);
							comparatorsToWhere.add(this.conditionComparators[i]);
						}
						else {						
							valuesToWhere.add(v);
							comparatorsToWhere.add(this.conditionComparators[i]);
						}
					}
					catch (Exception ex) {
						log.warn(XavaResources.getString("tab_key_value_warning"),ex);
					}	
					
				}
				else{
					comparatorsToWhere.add(this.conditionComparators[i]);
					valuesToWhere.add("");
				}
			}	// end for	
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
		
		// 
		if (valuesToWhere != null && valuesToWhere.size() > 0){
			this.conditionValuesToWhere = new Object[valuesToWhere.size()];
			this.conditionComparatorsToWhere = new String[comparatorsToWhere.size()];
			Iterator<Object> itValues = valuesToWhere.iterator();
			Iterator<String> itComparators = comparatorsToWhere.iterator();
			for (int i = 0; i < valuesToWhere.size(); i++){
				this.conditionComparatorsToWhere[i] = itComparators.next();
				this.conditionValuesToWhere[i] = itValues.next();
			}
		}
		
		return sb.toString();
	}
	
	private Object getValueConverter(MetaProperty p, MetaTab tab, String property, String reference, String value, CmpField field){
		IConverter converter = p.getMetaModel().getMapping().getConverter(property);
		try{
			if (Class.forName(field.getCmpTypeName()).isEnum()){
				Enum enumeration = Enum.valueOf((Class<Enum>) Class.forName(field.getCmpTypeName()), value);
				return String.valueOf(converter == null ? enumeration.ordinal() : converter.toDB(enumeration));
			}
			else if (converter != null) {
				MetaProperty propertyFromReference = tab.getMetaModel().getMetaReference(reference).getMetaModelReferenced().getMetaProperty(property);
				Object o = WebEditors.parse(getRequest(), propertyFromReference, value, null, "");
				if (o == null) o = value;
				return String.valueOf(converter.toDB(o));
			}
			else return value;
		}
		catch(ClassNotFoundException ex){	// primitive type
			return value;
		}
	}

	private String decorateColumn(MetaProperty p, String column, int i) throws XavaException {
		if ("year_comparator".equals(this.conditionComparators[i])) {
			return p.getMetaModel().getMapping().yearSQLFunction(column);
		}
		if ("month_comparator".equals(this.conditionComparators[i])) {
			return p.getMetaModel().getMapping().monthSQLFunction(column);
		}
		if ("year_month_comparator".equals(this.conditionComparators[i])) {
			ModelMapping mapping = p.getMetaModel().getMapping(); 
			return mapping.yearSQLFunction(column) + " = ? and " + mapping.monthSQLFunction(column);
		}						
		if (java.lang.String.class.equals(p.getType()) && XavaPreferences.getInstance().isToUpperForStringArgumentsInConditions()) { 
			return "upper(" + column + ")"; 
		}
		return column;
	}


	private Object convertComparator(MetaProperty p, String comparator) throws XavaException {
		if (STARTS_COMPARATOR.equals(comparator)) return "like";
		if (CONTAINS_COMPARATOR.equals(comparator)) return "like";
		if (YEAR_COMPARATOR.equals(comparator)) return "=";
		if (MONTH_COMPARATOR.equals(comparator)) return "=";
		if (YEAR_MONTH_COMPARATOR.equals(comparator)) return "="; 
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
	
	private boolean isEmpty(Object value){
		if (value instanceof Number) return false;
		if (value instanceof BigDecimal) return false;
		return Is.empty(value);
	}

	private Object [] getKey() throws XavaException {
		if (conditionValuesToWhere == null || conditionValuesToWhere.length == 0) { 
			return filterKey(null);
		}
		Collection key = new ArrayList();
		
		for (int i = 0; i < this.conditionValuesToWhere.length; i++) {
			Object value = this.conditionValuesToWhere[i];
			if (!isEmpty(value)) {
								
				if (STARTS_COMPARATOR.equals(this.conditionComparatorsToWhere[i])) { 
					value = convertStringArgument(value.toString()) + "%";
					key.add(value);
				}
				else if (CONTAINS_COMPARATOR.equals(this.conditionComparatorsToWhere[i])) {
					value = "%" + convertStringArgument(value.toString()) + "%";
					key.add(value);
				} 
				else if (YEAR_COMPARATOR.equals(this.conditionComparatorsToWhere[i]) || MONTH_COMPARATOR.equals(this.conditionComparatorsToWhere[i])) {
					value = convertStringArgument(value.toString());
					try {					
						key.add(new Integer(value.toString()));
					}
					catch (Exception ex) {
						log.warn(XavaResources.getString("tab_key_value_warning"),ex);
						key.add(null);
					}										
				}
				else if (YEAR_MONTH_COMPARATOR.equals(this.conditionComparatorsToWhere[i])) { 
					try {				
						StringTokenizer st = new StringTokenizer(value.toString(), "/ ,:;");
						if (st.hasMoreTokens()) key.add(new Integer(st.nextToken()));
						else key.add(null);
						if (st.hasMoreTokens()) key.add(new Integer(st.nextToken()));
						else key.add(null);
					}
					catch (Exception ex) {
						log.warn(XavaResources.getString("tab_key_value_warning"),ex);
						key.add(null);
						key.add(null);
					}															
				}
				else key.add(value);
			}
		}		
		return filterKey(key.toArray());
	}
	
	private String convertStringArgument(String value) {  
		if (XavaPreferences.getInstance().isToUpperForStringArgumentsInConditions()) {
			return value.trim().toUpperCase(); 
		}
		else {
			return value.trim();
		}
	}

	private Locale getLocale() {
		return XavaResources.getLocale(request);
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

		IFilter filter = getFilter();
		if (filter != null) {			
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
												
				if (p != null && p.getMapping().hasConverter()) { 
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
		
		return key;
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
	 * Change the selected ones only within the current page range. <p>
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
	
	/**
	 * Same that {@link #setSelectec(int [] values)} but from String []. <p>
	 */
	public void setSelected(String [] values) {
		if (values == null) {
			if (selected != null && selected.length > 0) deselectedInCurrentPage();
			return;
		}
		int [] intValues = new int[values.length];
		for (int i=0; i<values.length; i++) {
			intValues[i] = Integer.parseInt(values[i]);
		}
		setSelected(intValues);
	}
	
	private void deselectedInCurrentPage() { 
		List rest = new ArrayList();
		for (int i = 0; i < selected.length; i++){
			int value = selected[i];
			if (value >= getInitialIndex() && value <= getFinalIndex()) continue;
			rest.add(value);
		}
		
		Collections.sort(rest);		
		selected = new int[rest.size()];		
		for (int i=0; i<selected.length; i++) {			
			selected[i] = ((Integer) rest.get(i)).intValue();
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
	
	public int getAddColumnsLastPage() {
		return addColumnsLastPage; 		
	}
	
	public int getAddColumnsPage() { 
		return addColumnsPage;
	}
	
	public void goAddColumnsPage(int page) {
		if (page < 1) addColumnsPage = 1;
		else if (page > getAddColumnsLastPage()) addColumnsPage = getAddColumnsLastPage();
		else addColumnsPage = page;
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
			log.warn(XavaResources.getString("tab_size_warning"),ex);
			return -1;
		}
	}
	
	private void setConditionValuesImpl(String [] values) throws XavaException {
		if (Arrays.equals(this.conditionValues, values)) return;		
		if (getMetaPropertiesNotCalculated().size() != values.length) return; // to avoid problems on changing module
		this.conditionValues = values;				
		condition = null;
	}
		
	public String [] getConditionValues() {
		return conditionValues; 
	}
	
	private void setConditionComparatorsImpl(String [] comparators) throws XavaException {  
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
		tabName = null; 
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

	public synchronized void setRequest(HttpServletRequest request) {		
		this.request = request;
		String collectionPrefix = getCollectionPrefix();
		setConditionComparators(collectionPrefix);
		setConditionValues(collectionPrefix);
	}

	private String getCollectionPrefix() {
		String tabObject = request.getParameter("tabObject");
		return tabObject == null?"":tabObject + "_";
	}

	private void setConditionComparators(String collectionPrefix) { 		
		setConditionComparatorsImpl(
			getConditionFilterParameters(collectionPrefix + "conditionComparator.")
		);
	}
	
	private void setConditionValues(String collectionPrefix) { 		
		setConditionValuesImpl(
			getConditionFilterParameters(collectionPrefix + "conditionValue.")
		);
	}
	
	private String[] getConditionFilterParameters(String prefix) {
		String conditionComparator = request.getParameter(prefix + "0");		
		Collection conditionComparators = new ArrayList();
		for (int i=1; conditionComparator != null; i++) {
			conditionComparators.add(conditionComparator);			
			conditionComparator = request.getParameter(prefix + i);			
		}
		String [] result = new String[conditionComparators.size()];
		conditionComparators.toArray(result);
		return result;
	}
	
	
	private Preferences getPreferences() throws BackingStoreException {		
		return Users.getCurrentPreferences().node("tab." + getMetaTab().getMetaModel().getName() + "." + getTabName() + ".");
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
		if (getCollectionView() != null) return getCollectionTitle(); 
		String modelName = getModelName();
		String tabName = getTabName();
		Locale locale = XavaResources.getLocale(request);
		String title = titleId==null?getTitleI18n(locale, modelName, tabName):Labels.get(titleId, locale);
		if (title != null) return putTitleArguments(locale, title);		
		String modelLabel = MetaModel.get(modelName).getLabel(locale); 
		return XavaResources.getString(request, "report_title", modelLabel);					
	}
	
	private String getCollectionTitle() throws XavaException {		
		Locale locale = XavaResources.getLocale(request);
		View parentView = getCollectionView().getParent();
		MetaModel metaModel = parentView.getMetaModel(); 
		String modelLabel = metaModel.getLabel(locale);		
		String collectionLabel = metaModel.getMetaCollection(getCollectionView().getMemberName()).getLabel(locale);
		Map membersNames = parentView.getMembersNames();
		StringBuffer id = new StringBuffer();		
		if (membersNames.containsKey("id")) {
			id.append(parentView.getValue("id"));
		}
		else if (membersNames.containsKey("number")) {
			id.append(parentView.getValue("number"));
		} 
		else if (membersNames.containsKey("codigo")) {
			id.append(parentView.getValue("codigo"));
		}
		else if (membersNames.containsKey("numero")) {
			id.append(parentView.getValue("numero"));
		}
		
		if (id.length() > 0) {
			id.append(" - ");
		}

		if (membersNames.containsKey("name")) {
			id.append(parentView.getValue("name"));
		}
		else if (membersNames.containsKey("description")) {
			id.append(parentView.getValue("description"));
		}		
		else if (membersNames.containsKey("nombre")) {
			id.append(parentView.getValue("nombre"));
		}
		else if (membersNames.containsKey("descripcion")) {
			id.append(parentView.getValue("descripcion"));
		}
		
		if (id.length() > 0) {
			Map key = parentView.getKeyValuesWithValue();
			for (Iterator it = key.values().iterator(); it.hasNext(); ) {
				id.append(it.next());
				if (it.hasNext()) id.append(" - ");
			}
		}
		
		return XavaResources.getString(locale, "collection_report_title", collectionLabel, modelLabel, id.toString());
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
	public void setBaseConditionForReference(String baseConditionForReference) {
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
			Preferences preferences = getPreferences();			

			String propertiesNames = preferences.get(PROPERTIES_NAMES, null);
			if (propertiesNames != null) {
				setPropertiesNames(propertiesNames);
			}
			rowsHidden = preferences.getBoolean(ROWS_HIDDEN, rowsHidden);			
			filterVisible = preferences.getBoolean(FILTER_VISIBLE, filterVisible);
			if (columnWidths != null) columnWidths.clear();
			for (MetaProperty property: getMetaProperties()) {
				int value = preferences.getInt(COLUMN_WIDTH + property.getQualifiedName(), -1);
				if (value >= 0) {
					if (columnWidths == null) columnWidths = new HashMap<String, Integer>();
					columnWidths.put(property.getQualifiedName(), value);
				}
			}
		}
		catch (Exception ex) {
			log.warn(XavaResources.getString("warning_load_preferences_tab"),ex);
		}
	}

	private void saveUserPreferences() { 		
		try { 
			Preferences preferences = getPreferences();			

			preferences.put(PROPERTIES_NAMES, getPropertiesNamesAsString());
			preferences.putBoolean(ROWS_HIDDEN, rowsHidden);
			preferences.putBoolean(FILTER_VISIBLE, filterVisible);
			if (columnWidths != null) {
				for (Map.Entry<String, Integer> columnWidth: columnWidths.entrySet()) {
					preferences.putInt(
						COLUMN_WIDTH + columnWidth.getKey(),
						columnWidth.getValue()
					);
				}
			}
			preferences.flush();
		}
		catch (Exception ex) {
			log.warn(XavaResources.getString("warning_save_preferences_tab"),ex);
		}	
	}
	
	private void removeUserPreferences() { 		
		try { 
			Preferences preferences = getPreferences();			
			preferences.clear();
			preferences.flush();
		}
		catch (Exception ex) {
			log.warn(XavaResources.getString("warning_save_preferences_tab"),ex);
		}		
	}
				
	/**
	 * The CSS style associated to the specified row. <p>
	 * 
	 * @return A string with the CSS style suitable to use in a 'class' attribute in HTML. 
	 */
	public String getStyle(int row) { 
		try {
			if (styles != null && !styles.isEmpty()) {
				String result = (String) styles.get(new Integer(row));
				if (result != null) return result;
			}
			if (!getMetaTab().hasRowStyles()) return null;
			for (Iterator it = getMetaTab().getMetaRowStyles().iterator(); it.hasNext();) {
				MetaRowStyle rowStyle = (MetaRowStyle) it.next();
				String result = getStyle(rowStyle, row);
				if (result != null) return result;
			}
			return null;
		}
		catch (Exception ex) {
			log.warn(XavaResources.getString("warning_row_style"),ex);
			return null;
		}
	}

	/**
	 * Set the CSS style associated to the specified row. <p>
	 * 
	 * @param  row   Row number affected by this style.
	 * @param  style  A string with the CSS style suitable to use in a 'class' attribute in HTML. 
	 */	
	public void setStyle(int row, String style) {
		if (styles == null) styles = new HashMap();
		styles.put(new Integer(row), style);
	}
	
	/**
	 * Clear the effect of all calls to {@link #setStyle(int, String)}
	 *
	 */
	public void clearStyle() {
		if (styles != null) styles.clear();
	}
	
	private String getStyle(MetaRowStyle rowStyle, int row) {
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
			log.warn(XavaResources.getString("warning_row_style"));
			return null;
		}
	}
		
	public boolean isSortRemainingProperties() {
		return sortRemainingProperties;
	}
	public void setSortRemainingProperties(boolean sortRemainingProperties) {
		this.sortRemainingProperties = sortRemainingProperties;
	}
	
	/**
	 * An array with the keys (in <code>Map</code> format) of the selected
	 * rows. <p> 
	 * 
	 * @return Never null
	 */
	public Map [] getSelectedKeys() { 
		if (selected == null) return new Map[0];
		Map [] keys = new Map[selected.length];
		for (int i = 0; i < keys.length; i++) {
			try {
				keys[i] = (Map) tableModel.getObjectAt(selected[i]);
			}
			catch (Exception ex) {
				keys[i] = Collections.EMPTY_MAP;
				log.warn(XavaResources.getString("tab_row_key_warning", new Integer(i)),ex);
			}
		}
		return keys;
	}

	/**
	 * An array with the keys (in <code>Map</code> format) of the all
	 * rows. <p> 
	 * 
	 * @return Never null
	 */	
	public Map [] getAllKeys() { 
		Collection allKeys = new ArrayList();
		for (int i = 0; i < tableModel.getRowCount(); i++) {			
			try {
				allKeys.add(tableModel.getObjectAt(i));
			}
			catch (Exception ex) {
				allKeys.add(Collections.EMPTY_MAP);
				log.warn(XavaResources.getString("tab_row_key_warning", new Integer(i)),ex);
			}
		}
		Map [] keys = new Map[allKeys.size()];
		allKeys.toArray(keys);
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

	/**
	 * Filter used currently by this tab. <p>
	 * 
	 * By default the filter is the defined one in the &lt;tab/&gt; of the 
	 * component or using the filter attribute of @Tab annotation.<br>
	 * But, it's possible to assign in runtime other filter using the
	 * {@link #setFilter(IFilter)} method.<br>  
	 * 
	 * @return Can be null.
	 */
	public IFilter getFilter() throws XavaException { 
		if (filter != null) return filter;
		if (getMetaTab().hasFilter()) {
			return getMetaTab().getMetaFilter().getFilter();
		}		
		return null;
	}

	/**
	 * Sets the filter for this tab in runtime. <p>
	 * 
	 * This override the filter defined using &lt;tab&gt; or @Tab.<br>
	 */
	public void setFilter(IFilter filter) { 
		this.filter = filter;
	}
	
	
	/**
	 * Set the properties to be displayed by this <code>Tab</code> in runtime. <p>
	 * 
	 * This override the properties defined using &lt;tab&gt; or @Tab.<br>
	 */
	public void setPropertiesNames(String propertiesNames) throws XavaException { 
		cloneMetaTab();
		getMetaTab().setPropertiesNames(propertiesNames);		
		resetAfterChangeProperties();				
	}
	
	/**
	 * Set the default order for this <code>Tab</code> in runtime. <p>
	 * 
	 * This override the default order defined using &lt;tab&gt; or @Tab.<br>
	 */	
	public void setDefaultOrder(String defaultOrder) throws XavaException {  
		cloneMetaTab();
		getMetaTab().setDefaultOrder(defaultOrder);		
		resetAfterChangeProperties();				
	}

	/**
	 * If this tab represents a collection the collection view of that collection. <p>
	 * 
	 * If this tab does not represents a collection collectionView will be null.<br>
	 */
	public View getCollectionView() {
		return collectionView;
	}

	/**
	 * If this tab represents a collection the collection view of that collection. <p>
	 * 
	 * If this tab does not represents a collection collectionView will be null.<br>
	 */	
	public void setCollectionView(View collectionView) {
		this.collectionView = collectionView;
		this.filterVisible = XavaPreferences.getInstance().isShowFilterByDefaultInCollections();
	}
			
	public boolean isFilterVisible() {
		return filterVisible;
	}
	public void setFilterVisible(boolean filterVisible) { 
		this.filterVisible = filterVisible;
		saveUserPreferences();
	}
	
	
	public String toString() {
		return "Tab:" + oid;
	}

	public int getAddColumnsPageRowCount() {
		return addColumnsPageRowCount;
	}

	public void setAddColumnsPageRowCount(int addColumnsPageRowCount) {
		this.addColumnsPageRowCount = addColumnsPageRowCount;
	}

	public void clearSelected() {
		selected = null;
	}
	
}
