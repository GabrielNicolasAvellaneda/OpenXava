package org.openxava.calculators;

import java.sql.*;
import java.util.*;

import org.openxava.component.*;
import org.openxava.converters.*;
import org.openxava.filters.*;
import org.openxava.mapping.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;


/**
 * It obtain a description collection. <p>
 * 
 * @author Javier Paniza
 */
public class DescriptionsCalculator implements IJDBCCalculator {
	
	private boolean removeSpacesInKey;
	private String packageName;	
	private IConnectionProvider provider;
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
	private String select;
	private boolean orderByKey = false;
	private boolean useCache = true;
	private boolean useConvertersInKeys = false;

	private Collection keyPropertiesCollection;	
	

	public void setConnectionProvider(IConnectionProvider provider) {		
		this.provider = provider;
	}
	
	/**
	 * Pure execution, without cache, without call to server,... <p>
	 * 
	 * Better call to {@link #getDescriptions} if you wish to use
	 * directly.<br>
	 */
	public Object calculate() throws Exception {		
	 	checkPreconditions();			 	
	 	if (!XavaPreferences.getInstance().isDescriptionsCalculatorAsEJB()) {
	 		setConnectionProvider(DataSourceConnectionProvider.getByComponent(getComponentName()));
	 	}
		else if (XSystem.onClient()) {			
			return Server.calculate(this, getPackageName());
		}		
		Connection con = provider.getConnection();		
		if (keyProperty == null && keyProperties == null) {
			throw new XavaException("descriptions_calculator_keyProperty_required", getClass().getName());
		}
		try {			
			String select = getSelect();			
			PreparedStatement ps = con.prepareStatement(select);			
			if (hasParameters()) {
				Iterator it = getParameters().iterator();
				int i = 1;
				while (it.hasNext()) {	
					Object data = it.next();					
					ps.setObject(i++, data);
				}	
				
			}			
			ResultSet rs = ps.executeQuery();
			List result = new ArrayList();
			if (isMultipleKey()) {
				readWithMultipleKey(result, rs); 		
			}
			else {
				readWithSimpleKey(result, rs);
			}
			rs.close();
			ps.close();
			Comparator comparador = isOrderByKey()?
				KeyAndDescriptionComparator.getByKey():
				KeyAndDescriptionComparator.getByDescription();						
			Collections.sort(result, comparador);						
			return result;
		}
		finally {
			try {
				con.close();
			}
			catch (Exception ex) {
			}
		}
	}

	private String getPackageName() throws XavaException {
		if (packageName == null) {
			packageName = MetaComponent.get(getComponentName()).getPackageName();
		}
		return packageName;
	}

	private void checkPreconditions() throws XavaException {
		if (Is.emptyString(getModel())) {
			throw new XavaException("descriptions_calculator_model_required", getClass().getName());
		}
		if (Is.emptyString(getKeyProperty()) && Is.emptyString(getKeyProperties())) {
			throw new XavaException("descriptions_calculator_keyProperty_required", getClass().getName());
		}
		if (Is.emptyString(getDescriptionProperty()) && Is.emptyString(getDescriptionProperties())) {
			throw new XavaException("descriptions_calculator_descriptionProperty_required", getClass().getName());
		}				
	}

	private void readWithSimpleKey(Collection result, ResultSet rs) throws Exception {
		int columns = rs.getMetaData().getColumnCount();
		while (rs.next()) {
			KeyAndDescription el = new KeyAndDescription();
			Object key = getObject(getKeyProperty(), rs, 1, false);
			if (key instanceof String && isRemoveSpacesInKey()) key = ((String) key).trim();
			el.setKey(key);
			el.setDescription(obtainDescription(2, columns, rs));
			result.add(el);
		}		
	}

	private void readWithMultipleKey(Collection result, ResultSet rs) throws Exception {		
		int columns = rs.getMetaData().getColumnCount();
		while (rs.next()) {
			KeyAndDescription el = new KeyAndDescription();
			int idx=1;		
			Iterator itKeyNames = getKeyPropertiesCollection().iterator();
			Map key = new HashMap();
			while (itKeyNames.hasNext()) {
				String name = (String) itKeyNames.next();
				key.put(name, getObject(name, rs, idx++, isUseConvertersInKeys()));
			}		
			if(isUseConvertersInKeys()) {			
				el.setKey(getMetaModel().obtainPrimaryKeyFromKey(key));			
			} else {
				el.setKey(getMetaModel().obtainPrimaryKeyFromKeyWithoutConversors(key));			
			}
			el.setDescription(obtainDescription(idx, columns, rs));			
			result.add(el);
		}		
	}
	
	private String obtainDescription(int idx, int columns, ResultSet rs) throws SQLException {
		StringBuffer des = new StringBuffer();
		while (idx <= columns) {			
			des.append(rs.getObject(idx));				
			idx++;
			if (idx <= columns) {
				des.append(" ");			
			}
		}
		return des.toString();				
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

	private int getKeyFieldsCount() {
		return getKeyPropertiesCollection().size();
	}

	
	private Object getObject(String propertyName, ResultSet rs, int i, boolean useConverter) throws Exception {
		if (useConverter && getMapping().hasConverter(propertyName)) {
			return getObjectWithConverter(propertyName, rs, i);
		}
		Class type = getCodeType(propertyName);
		if (!type.isPrimitive()) return rs.getObject(i);
		if (type.equals(Integer.TYPE)) {
			return new Integer(rs.getInt(i));
		}
		else if (type.equals(Long.TYPE)) {
			return new Long(rs.getLong(i));
		}
		else if (type.equals(Short.TYPE)) {
			return new Short(rs.getShort(i));
		}		
		else if (type.equals(Double.TYPE)) {
			return new Double(rs.getDouble(i));
		}		
		else if (type.equals(Float.TYPE)) {
			return new Float(rs.getFloat(i));
		}				
		return rs.getObject(i);	
	}
	
	private boolean isMultipleKey() {
		return !Is.emptyString(keyProperties);
	}
		
	private Class getCodeType(String propertyName) throws XavaException {
		try {
			return getMapping().getType(propertyName);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("code_type_descriptions_calculator_error", ex.getLocalizedMessage());
		}
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
	}

	public void setDescriptionProperty(String descriptionProperty) {
		this.descriptionProperty = descriptionProperty;
	}
	
	private String getSelect() throws XavaException {
		if (select == null) {			
			ModelMapping mapping = getMapping();
			StringBuffer sb = new StringBuffer("select ");
			Iterator itKeyProperties = getKeyPropertiesCollection().iterator();
			while (itKeyProperties.hasNext()) {
				String keyProperty = (String) itKeyProperties.next();				
				sb.append(mapping.getColumn(keyProperty));				
				sb.append(", ");				
			}
			if (Is.emptyString(getDescriptionProperties())) {				
				sb.append(mapping.getColumn(getDescriptionProperty()));				
			}
			else {
				StringTokenizer st = new StringTokenizer(getDescriptionProperties(), ",");
				while (st.hasMoreTokens()) {
					String descriptionProperty = st.nextToken().trim();					
					sb.append(mapping.getColumn(descriptionProperty));					
					if (st.hasMoreTokens()) {
						sb.append(", ");
					}					
				}
			}			
			sb.append(" from ");
			sb.append(mapping.getTable());
			if (hasCondition()) {
				sb.append(" where ");
				sb.append(getConditionSQL(mapping));
			}
			select = sb.toString();						
		}				
		return select;
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
		
	private Object getObjectWithConverter(String propertyName, ResultSet rs, int i) throws Exception {
		IConverter converter = getMapping().getConverter(propertyName);
		return converter.toJava(rs.getObject(i));
	}
	
	/**
	 * It's used when there are more than one property that
	 * it's key, or with only one It's preferred use a wrapper
	 * class as primary key. <p>
	 * 
	 * It's exclusive with <tt>keyProperties</tt>. 
	 */
	public String getKeyProperties() {
		return keyProperties;
	}

	public void setKeyProperties(String keyProperties) {		
		this.keyProperties = keyProperties;
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
		return descriptionProperties;
	}

	public void setDescriptionProperties(String string) {
		descriptionProperties = string;
	}

	public boolean isRemoveSpacesInKey() {
		return removeSpacesInKey;
	}

	public void setRemoveSpacesInKey(boolean b) {
		removeSpacesInKey = b;
	}

	public boolean isUseConvertersInKeys() {
		return useConvertersInKeys;		
	}

	public void setUseConvertersInKeys(boolean b) {
		useConvertersInKeys = b;
	}

}
