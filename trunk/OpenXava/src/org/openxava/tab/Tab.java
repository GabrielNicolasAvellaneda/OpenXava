package org.openxava.tab;

import java.text.*;
import java.util.*;

import javax.servlet.http.*;

import net.sf.hibernate.*;
import net.sf.hibernate.cfg.*;

import org.openxava.component.*;
import org.openxava.filters.*;
import org.openxava.mapping.*;
import org.openxava.model.meta.*;
import org.openxava.tab.impl.*;
import org.openxava.tab.meta.*;
import org.openxava.util.*;


/** 
 * Objeto de sesión para trabajar con datos tabulares. <p>
 * 
 * @author Javier Paniza
 */

public class Tab {

	private final static int BLOCK_SIZE = 10;	
	private final static String STARTS_COMPARATOR = "starts_comparator";
	private final static String CONTAINS_COMPARATOR = "contains_comparator";
	private final static String YEAR_COMPARATOR = "year_comparator";
	private final static String MONTH_COMPARATOR = "month_comparator";
	
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
	
	public Tab() {			
	}
	
	
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
		return getMetaTab().getRemainingPropertiesNames();
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
	
	
	public void setBaseCondition(String condicion) throws XavaException { 		
		if (Is.equal(this.baseCondition, condicion)) return;
		this.baseCondition = condicion;		
		this.condition = null;
	}
		
	/**
	 * Esto es una alternativa a setModelo, y se usa cuando este tab representa
	 * una colección de referencia 
	 */
	public void setReferencesCollection(String modelo, String nombreColeccion) throws XavaException {
		MetaModel metaModelo = MetaComponent.get(modelo).getMetaEntity(); 
		MetaReference ref = metaModelo.getMetaCollection(nombreColeccion).getMetaReference();
		setModelName(ref.getReferencedModelName());		
		referencesCollectionMapping = ref.getMetaModelReferenced().getMapping().getReferenceMapping(ref.getRole());
		crearCondicionBaseParaReferencia();
		clonarMetaTab();
		getMetaTab().setPropertiesNames("*");
	}
	
	private void crearCondicionBaseParaReferencia() throws XavaException {				
		Iterator it = referencesCollectionMapping.getDetails().iterator();
		StringBuffer condicion = new StringBuffer();
		while (it.hasNext()) {
			ReferenceMappingDetail detalle = (ReferenceMappingDetail) it.next();
			condicion.append(detalle.getColumn());
			condicion.append(" = ?");
			if (it.hasNext()) condicion.append(" and "); 
		}
		setBaseConditionForReference(condicion.toString());
	}
	
	public void setBaseConditionValuesForReference(Map valores) throws XavaException { 
		ReferenceMapping mapeo = referencesCollectionMapping;
		Iterator it = mapeo.getDetails().iterator();
		baseConditionValuesForReference = new Object[mapeo.getDetails().size()];
		for (int i=0; i<baseConditionValuesForReference.length; i++) {
			ReferenceMappingDetail detalle = (ReferenceMappingDetail) it.next();
			baseConditionValuesForReference[i] = valores.get(detalle.getReferencedModelProperty());
		}		
	}
		
	private void clonarMetaTab() throws XavaException { 
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
	 * A table model with on-demmand data reading. <p>
	 * 
	 * Suitable for UI.
	 */
	public IXTableModel getTableModel() throws Exception {		
		if (tableModel == null) {						
			IEntityTab tab = EntityTabFactory.create(getMetaTab());
			tab.search(getCondition(), getKey());
			tableModel = tab.getTable();			
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
		boolean primeraCondicion = true;
		metaPropertiesKey = null;
		if (!Is.emptyString(getBaseConditionForReference())) {
			sb.append(getSQLBaseConditionForReference());
			primeraCondicion = false;						
		}
		else if (!Is.emptyString(getBaseCondition())) {
			sb.append(getSQLBaseCondition());
			primeraCondicion = false;			
		}		
		if (!(conditionValues == null || conditionValues.length == 0)) {
			MetaProperty pOrder = null;
								
			for (int i = 0; i < this.conditionValues.length; i++) {				
				MetaProperty p = (MetaProperty) getMetaPropertiesNotCalculated().get(i);				
				if (orderBy != null && p.getQualifiedName().equals(orderBy)) {
					pOrder = p;
				}				
				if (Is.emptyString(this.conditionComparators[i])) {
					this.conditionValues[i] = null; 
				}
				if (!Is.emptyString(this.conditionValues[i])) {												
					if (primeraCondicion) primeraCondicion = false;
					else sb.append(" and ");
					ModelMapping mapping = getMetaTab().getMetaModel().getMapping();					
					String column = mapping.getQualifiedColumn(p.getQualifiedName());					
					sb.append(decorateColumn(p, column, i));
					sb.append(' ');
					sb.append(convertComparator(this.conditionComparators[i]));
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


	private Object convertComparator(String comparator) {
		if (STARTS_COMPARATOR.equals(comparator)) return "like";
		if (CONTAINS_COMPARATOR.equals(comparator)) return "like";
		if (YEAR_COMPARATOR.equals(comparator)) return "=";
		if (MONTH_COMPARATOR.equals(comparator)) return "=";
		return comparator;
	}
	
	private Object [] getKey() throws XavaException {
		if (conditionValues == null || conditionValues.length == 0) { 
			return filterKey(null);
		}
		Collection clave = new ArrayList();
		
		for (int i = 0; i < this.conditionValues.length; i++) {
			String valor = this.conditionValues[i];
			if (!Is.emptyString(valor)) {
								
				if (STARTS_COMPARATOR.equals(this.conditionComparators[i])) {
					valor = valor.trim().toUpperCase() + "%";
					clave.add(valor);
				}
				else if (CONTAINS_COMPARATOR.equals(this.conditionComparators[i])) {
					valor = "%" + valor.trim().toUpperCase() + "%";
					clave.add(valor);
				} 
				else if (YEAR_COMPARATOR.equals(this.conditionComparators[i]) || MONTH_COMPARATOR.equals(this.conditionComparators[i])) {
					valor = valor.trim().toUpperCase();
					try {					
						clave.add(new Integer(valor));
					}
					catch (Exception ex) {
						ex.printStackTrace();
						System.err.println(XavaResources.getString("tab_key_value_warning"));
						clave.add(null);
					}										
				}
				else {
					valor = valor.trim().toUpperCase();
					MetaProperty p = (MetaProperty) getMetaPropertiesNotCalculated().get(i);
					try {					
						clave.add(p.parse(valor));
					}
					catch (Exception ex) {
						ex.printStackTrace();
						System.err.println(XavaResources.getString("tab_key_value_warning"));
						clave.add(null);
					}					
				}
				
			}
		}		
		return filterKey(clave.toArray());
	}
	
	private Object[] filterKey(Object[] key) throws XavaException {		
		// first, for references
		if (baseConditionValuesForReference != null && baseConditionValuesForReference.length > 0) {
			if (key==null) key=new Object[0];
			Object [] nuevaClave = new Object[baseConditionValuesForReference.length + key.length];
			int j = 0;
			for (int i = 0; i < baseConditionValuesForReference.length; i++) {
				nuevaClave[j++] = baseConditionValuesForReference[i];
			}
			for (int i = 0; i < key.length; i++) {
				 nuevaClave[j++] = key[i];
			}			
			key = nuevaClave;
		}		
		
		// Filter of meta tabs
		int indexIncrement = 0;
		if (getMetaTab().hasFilter()) {
			IFilter filtro = getMetaTab().getMetaFilter().getFilter();
			
			if (filtro instanceof IRequestFilter) {
				((IRequestFilter) filtro).setRequest(request);
			}
			int original = key == null?0:key.length;
			key = (Object[]) filtro.filter(key);
			indexIncrement = key == null?0:key.length - original;
		}
		
		// To db format		
		if (key != null && metaPropertiesKey != null) {			
			for (int i = indexIncrement; i < key.length; i++) {
				MetaProperty p = (MetaProperty) metaPropertiesKey.get(i - indexIncrement);
				// If has a converter, apply
				if (p.getMapping().hasConverter()) {					
					key[i] = p.getMapping().getConverter().toDB(key[i]);
				}
			}									
		}
		
		// Filter dates, since some jdbc drivers not support java.util.Date		
		if (key != null) {
			for (int i = 0; i < key.length; i++) {
				if (key[i] instanceof java.util.Date) {
					key[i] = Dates.toSQL((java.util.Date) key[i]);
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
	
	public Map [] getClavesSeleccionados() {
		if (selected == null) return new Map[0];
		Map [] claves = new Map[selected.length];
		for (int i = 0; i < claves.length; i++) {
			try {
				claves[i] = (Map) tableModel.getObjectAt(selected[i]);
			}
			catch (Exception ex) {
				claves[i] = Collections.EMPTY_MAP;
				System.err.println(XavaResources.getString("tab_row_key_warning", new Integer(i)));
			}
		}
		return claves;
	}
	
	public boolean hasSelected() {
		return selected != null && selected.length > 0;
	}
	
	/**
	 * Cambia todos los seleccionados. <p>
	 *
	 * Se cumple la postcondición <tt>this.seleccinados == valores</tt>
	 */
	public void setAllSelected(int [] valores) {
		this.selected = valores;
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
	 * Cambias los seleccionados solo dentro del rango de la página actual. <p>
	 * 
	 * Es decir, que NO se cumple la postcondición de <tt>this.seleccinados == valores</tt>	 
	 */
	public void setSelected(int [] valores) {				
		List r = new ArrayList();
		if (selected != null) {	
			for (int i=0; i<selected.length; i++) {
				int num = selected[i];
				if (num< getInitialIndex() || num>getFinalIndex()-1) {					
					r.add(new Integer(num));
				}
			}
		}				
		for (int i=0; i<valores.length; i++) {			
			r.add(new Integer(valores[i]));			
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
	
	
	
	private String toString(int[] valores) {
		if (valores == null) return "[VACIO]";
		StringBuffer sb = new StringBuffer("[");
		for (int i = 0; i < valores.length; i++) {
			sb.append(valores[i]);
			sb.append(", ");
		}
		sb.append("]");
		return sb.toString();
	}


	public boolean isSelected(int fila) {
		if (selected == null || selected.length == 0) return false;
		return Arrays.binarySearch(selected, fila) >= 0;
	}
	
	public int getInitialIndex() {		
		return initialIndex;
	}
	
	public int getFinalIndex() {		
		return initialIndex + BLOCK_SIZE;
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
		return (tableModel.getRowCount() - 1)/ BLOCK_SIZE + 1;
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
		initialIndex = (page - 1) * BLOCK_SIZE;		
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
	
	public void setConditionValues(String [] valores) throws XavaException {		
	  if (Arrays.equals(this.conditionValues, valores)) return;
		if (getMetaPropertiesNotCalculated().size() != valores.length) return; // para evitar problemas al cambiar de módulo
	  this.conditionValues = valores;
	  condition = null;	  
	}
	
	public String [] getConditionValues() {
		return conditionValues; 
	}
	
	public void setConditionComparators(String [] comparadores) throws XavaException {		
		if (Arrays.equals(this.conditionComparators, comparadores)) return; 		
		if (getMetaPropertiesNotCalculated().size() != comparadores.length) return;		
		this.conditionComparators = comparadores;
		condition = null;						
	}
	
	public String [] getConditionComparators() {
		return conditionComparators;
	}
	
	public void orderBy(String propiedad) {
		if (Is.equal(propiedad, orderBy)) {
			descendingOrder = !descendingOrder;
		}
		else {
			descendingOrder = false;
			orderBy = propiedad;
		}
		condition = null;		
	}
	
	public String getOrderBy() {
		return orderBy;
	}
	
	public boolean isOrderAscending(String nombre) {
		return !descendingOrder && Is.equal(nombre, orderBy);
	}
	
	public boolean isOrderDescending(String nombre) {
		return descendingOrder && Is.equal(nombre, orderBy);
	}
	
	
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String nuevo) {
		if (Is.equal(modelName, nuevo)) return;
		modelName = nuevo;		
		reiniciarEstado();
		loadUserPreferences();
	}

	private void reiniciarEstado() {		
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

	public void setTabName(String nuevo) {		
		if (Is.equal(tabName, nuevo)) return;
		tabName = nuevo;
		reiniciarEstado();		
		loadUserPreferences();
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
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
		String etiquetaModelo = MetaComponent.get(modelName).getMetaEntity().getLabel();
		return XavaResources.getString(request, "report_title", etiquetaModelo);					
	}
	
	private String putTitleArguments(Locale locale, String titulo) {
		if (titleArguments == null || titleArguments.length == 0) return titulo;
		MessageFormat formateador = new MessageFormat("");
		formateador.setLocale(locale);
		formateador.applyPattern(titulo);
		return formateador.format(titleArguments);		
	}

	public static String getTitleI18n(Locale locale, String nombreModelo, String nombreTab) throws XavaException {
		String id = null;
		if (Is.emptyString(nombreTab)) {
			id = nombreModelo + ".tab.title"; 
		}
		else {
			id = nombreModelo + ".tabs." + nombreTab + ".title";
		}
		if (Labels.exists(id)) {
			return Labels.get(id, locale);
		}		
		else {
			return null;
		}
	}
	
	
	public String getBaseConditionForReference() {
		return baseConditionForReference;
	}
	public void setBaseConditionForReference(
			String condicionBaseParaReferencia) {
		this.baseConditionForReference = condicionBaseParaReferencia;
	}

	public void addProperty(String propertyName) throws XavaException {
		clonarMetaTab();
		getMetaTab().addProperty(propertyName);
		resetAfterChangeProperties();
		saveUserPreferences();
	}
	
	public void addProperty(int index, String propertyName) throws XavaException {
		clonarMetaTab();
		getMetaTab().addProperty(index, propertyName);
		resetAfterChangeProperties();
		saveUserPreferences();
	}
	
	public void addProperties(Collection properties) throws XavaException {
		clonarMetaTab();
		for (Iterator it=properties.iterator(); it.hasNext();) {
			getMetaTab().addProperty((String)it.next());
		}		
		resetAfterChangeProperties();
		saveUserPreferences();
	}
	
	

	public void removeProperty(String propertyName) throws XavaException {
		clonarMetaTab();
		getMetaTab().removeProperty(propertyName);
		resetAfterChangeProperties();
		saveUserPreferences();
	}
	
	public void removeProperty(int index) throws XavaException {
		clonarMetaTab();
		getMetaTab().removeProperty(index);
		resetAfterChangeProperties();
		saveUserPreferences();
	}	
	
	public void movePropertyToRight(int index) throws XavaException {
		clonarMetaTab();
		getMetaTab().movePropertyToRight(index);		
		resetAfterChangeProperties();
		saveUserPreferences();
	}
	
	public void movePropertyToLeft(int index) throws XavaException {
		clonarMetaTab();
		getMetaTab().movePropertyToLeft(index);
		resetAfterChangeProperties();
		saveUserPreferences();
	}
		
	public void clearProperties() throws XavaException {	
		clonarMetaTab();
		getMetaTab().clearProperties();		
		resetAfterChangeProperties();
		saveUserPreferences();
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
				clonarMetaTab();
				getMetaTab().setPropertiesNames(userPreferences.getPropertiesNames());		
				resetAfterChangeProperties();
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
		Object rundata = request.getAttribute("rundata");
		if (rundata == null) { 
			return "openxava"; // Default user use out of jetspeed, for testing for example
		}	
		else {
			PropertiesManager pmRundata = new PropertiesManager(rundata);
			try {
				// Using introspection for no link OpenXava to turbine and jetspeed1.x
				// This is temporal. In future JSR-168 compatible, and remove this code 
				Object jetspeedUser = pmRundata.executeGet("user");
				PropertiesManager pmUser = new PropertiesManager(jetspeedUser);
				return (String) pmUser.executeGet("userName");
			}
			catch (Exception ex) {				
				ex.printStackTrace(); 
				System.err.println(XavaResources.getString("warning_get_user"));
				return "openxava";
			}
		}
				
		
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
			session.saveOrUpdate(userPreferences);
			
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
			MetaProperty p = getMetaProperty(column);
			Object value = getTableModel().getValueAt(row, column);
			if (Is.equalAsString(value, rowStyle.getValue())) {
				return rowStyle.getStyle();
			}
			return null;
		}
		catch (Exception ex) {			
			System.err.println("[Tab.getStyle] " + XavaResources.getString("warning_row_style"));
			return null;
		}
	}
		
}
