package org.openxava.calculators;

import java.util.*;

import javax.swing.table.*;

import org.openxava.component.*;
import org.openxava.filters.*;
import org.openxava.mapping.*;
import org.openxava.model.meta.*;
import org.openxava.tab.impl.*;
import org.openxava.tab.meta.*;
import org.openxava.util.*;

/**
 * It obtain a description collection. <p>
 * 
 * Use tab infrastructure for it, so you can make that this execute
 * within a EJB server or nor configuring tab in xava.properties.
 * 
 * @author Javier Paniza
 */
public class DescriptionsCalculator implements ICalculator {
	
	private String keyProperty;
	private String keyProperties;
	private String descriptionProperty;
	private String descriptionProperties;
	private String condition;
	private Collection parameters;
	private String model;
	private String componentName;
	private String aggregateName;
	private transient IMetaEjb metaModel;
	private transient Map cache;
	private boolean orderByKey = false;
	private boolean useCache = true;
	private boolean useConvertersInKeys = false;
	private Collection keyPropertiesCollection;
	private MetaTab metaTab;	
	
	
	/**
	 * Pure execution, without cache... <p>
	 * 
	 * Better call to {@link #getDescriptions} if you wish to use
	 * directly.<br>
	 */
	public Object calculate() throws Exception {		
	 	checkPreconditions();			 			
		if (keyProperty == null && keyProperties == null) {
			throw new XavaException("descriptions_calculator_keyProperty_required", getClass().getName());
		}
		List result = read();
		Comparator comparador = isOrderByKey()?
			KeyAndDescriptionComparator.getByKey():
			KeyAndDescriptionComparator.getByDescription();						
		Collections.sort(result, comparador);						
		return result;
	}

	private void checkPreconditions() throws XavaException {
		if (Is.emptyString(getModel())) {
			throw new XavaException("descriptions_calculator_model_required", getClass().getName());
		}
		if (Is.emptyString(getKeyProperties())) {
			throw new XavaException("descriptions_calculator_keyProperty_required", getClass().getName());
		}
		if (Is.emptyString(getDescriptionProperties())) {
			throw new XavaException("descriptions_calculator_descriptionProperty_required", getClass().getName());
		}				
	}
	
	private List read() throws Exception {
		List result = new ArrayList();
		TableModel table = executeQuery();		 
		for (int i=0; i<table.getRowCount(); i++) {
			KeyAndDescription el = new KeyAndDescription();			
			int iKey = 0;
			if (isMultipleKey()) {
				Iterator itKeyNames = getKeyPropertiesCollection().iterator();
				Map key = new HashMap();
				while (itKeyNames.hasNext()) {
					String name = (String) itKeyNames.next();
					key.put(name, table.getValueAt(i, iKey++));
				}		
				if(isUseConvertersInKeys()) {			
					el.setKey(getMetaModel().obtainPrimaryKeyFromKey(key));			
				} else {
					el.setKey(getMetaModel().obtainPrimaryKeyFromKeyWithoutConversors(key));			
				}				
			}
			else {
				el.setKey(table.getValueAt(i, iKey++));
			}
			StringBuffer value = new StringBuffer();
			for (int j=iKey; j<table.getColumnCount(); j++) {
				value.append(table.getValueAt(i, j));
				if (j < table.getColumnCount() - 1) value.append(' ');
			}
			el.setDescription(value.toString());
			el.setShowCode(true);
			if (el.getKey() != null) result.add(el);
		}
		return result;
	}	
	
	private IMetaEjb getMetaModel() throws XavaException {
		if (metaModel == null) {
			if (isAggregate()) {
				metaModel = (IMetaEjb) MetaComponent.get(getComponentName()).getMetaAggregate(getAggregateName());
			}	
			else {		
				metaModel = (IMetaEjb) MetaComponent.get(getComponentName()).getMetaEntity();
			}
		}
		return metaModel;
	}
	
	private boolean isMultipleKey() {
		return !Is.emptyString(keyProperties);
	}
			
	/**
	 * It uses caché depend on current parameter values. <p>
	 * 
	 * @return Collection of <tt>KeyAndDescription</tt>. Not null.
	 */
	public Collection getDescriptions() throws Exception {	
		if (conditionHasArguments() && !hasParameters()) return Collections.EMPTY_LIST;
		if (!isUseCache()) {
			return (Collection) calculate();
		}
		Collection saved = (Collection) getCache().get(getParameters());
		if (saved != null) {						
			return saved;
		}
		Collection result = (Collection) calculate();
		getCache().put(getParameters(), result);				
		return result;	
	}

	private boolean conditionHasArguments() {
		return this.condition != null && this.condition.indexOf('?') >= 0;		
	}

	private Map getCache() {
		if (cache == null) {
			cache = new HashMap();
		}
		return cache;		
	}

	/**
	 * It's used when there is only a key property.
	 * 
	 * It's exclusive with <tt>keyProperties</tt>. 
	 */
	public String getKeyProperty() {
		return keyProperty;
	}

	public String getDescriptionProperty() {
		return descriptionProperty;
	}
	
	private Collection getKeyPropertiesCollection() {
		if (keyPropertiesCollection == null) {
			keyPropertiesCollection = new ArrayList();
			String source = Is.emptyString(keyProperty)?keyProperties:keyProperty;
			StringTokenizer st = new StringTokenizer(source, ",");
			while (st.hasMoreElements()) {
				keyPropertiesCollection.add(st.nextToken().trim());
			}
		}
		return keyPropertiesCollection;
	}
		
	public void setKeyProperty(String keyProperty) {		
		this.keyProperty = keyProperty;		
		metaTab = null;
	}

	public void setDescriptionProperty(String descriptionProperty) {
		this.descriptionProperty = descriptionProperty;
		metaTab = null;
	}
	
	private MetaTab getMetaTab() throws XavaException {
		if (metaTab == null) {
			metaTab = new MetaTab();
			metaTab.setMetaModel(getMetaModel());
			metaTab.setPropertiesNames(getKeyProperties() + ", " +  getDescriptionProperties());				
		}
		return metaTab;
	}
	
	private TableModel executeQuery() throws Exception {
		IEntityTab tab = EntityTabFactory.createAllData(getMetaTab());		
		String condition = "";
		if (hasCondition()) {
			condition = getConditionSQL(getMapping());
		}
		Object [] key = null;
		if (hasParameters()) {
			key = new Object[getParameters().size()];
			Iterator it = getParameters().iterator();
			for (int i=0; i<key.length; i++) {	
				key[i] = it.next();
			}				
		}				
		tab.search(condition, key);
		return tab.getTable();
	}
		
	private boolean hasCondition() {
		return !Is.emptyString(condition);
	}
	
	private String getConditionSQL(ModelMapping mapping) throws XavaException {
		return mapping.changePropertiesByColumns(getCondition());
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model==null?"":model;
		this.metaModel = null;		
		this.componentName = null;
		this.aggregateName = null;
		StringTokenizer st = new StringTokenizer(this.model, ".");		
		if (st.hasMoreTokens()) this.componentName = st.nextToken();
		if (st.hasMoreTokens()) this.aggregateName = st.nextToken();
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {	    
		if (condition!=null && condition.toLowerCase().indexOf("year(curdate())")>=0){
			Calendar cal=Calendar.getInstance();
			cal.setTime(new java.util.Date());
			condition = Strings.change(condition,"year(curdate())",String.valueOf(cal.get(Calendar.YEAR)));
		}	    
		this.condition = condition;		
	}

	public boolean hasParameters() {
		return parameters != null && !parameters.isEmpty();
	}
	public Collection getParameters() {
		return parameters;
	}

	public void setParameters(Collection parameters) {
		this.parameters = parameters;		
	}
	
	public void setParameters(Collection parameters, IFilter filter) throws FilterException {		
		if (filter != null) {
			Object [] param = parameters==null?null:parameters.toArray();			
			param = (Object []) filter.filter(param);			
			parameters = Arrays.asList(param);			
		}
		this.parameters = parameters;				
	}
		
	/**
	 * It's used when there are more than one property that
	 * it's key, or with only one It's preferred use a wrapper
	 * class as primary key. <p>
	 * 
	 * It's exclusive with <tt>keyProperties</tt>. 
	 */
	public String getKeyProperties() {
		return Is.emptyString(keyProperties)?getKeyProperty():keyProperties;
	}

	public void setKeyProperties(String keyProperties) {		
		this.keyProperties = keyProperties;
		metaTab = null;
	}


	private String getAggregateName() {
		return aggregateName;
	}

	private String getComponentName() {
		return componentName;
	}
	
	private boolean isAggregate() {
		return !Is.emptyString(aggregateName);
	}
	
	private ModelMapping getMapping() throws XavaException {
		return getMetaModel().getMapping();
	}

	public boolean isOrderByKey() {		
		return orderByKey;
	}

	public void setOrderByKey(boolean b) {		
		orderByKey = b;
	}
	
	public void setOrderByKey(String b) {
		setOrderByKey("true".equalsIgnoreCase(b));
	}

	public boolean isUseCache() {
		return useCache;
	}

	public void setUseCache(boolean b) {
		useCache = b;
	}

	public String getDescriptionProperties() {
		return Is.emptyString(descriptionProperties)?getDescriptionProperty():descriptionProperties;
	}

	public void setDescriptionProperties(String string) {
		descriptionProperties = string;
		metaTab = null;
	}

	public boolean isUseConvertersInKeys() {
		return useConvertersInKeys;		
	}

	public void setUseConvertersInKeys(boolean b) {
		useConvertersInKeys = b;
	}

}
