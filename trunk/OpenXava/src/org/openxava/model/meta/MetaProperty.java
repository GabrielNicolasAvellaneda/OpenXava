package org.openxava.model.meta;

import java.math.*;
import java.rmi.*;
import java.text.*;
import java.util.*;

import javax.servlet.*;

import org.openxava.calculators.*;
import org.openxava.mapping.*;
import org.openxava.model.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;
import org.openxava.validators.*;
import org.openxava.validators.meta.*;

/**
 * @author Javier Paniza
 */
public class MetaProperty extends MetaMember implements Cloneable {

	private Collection propertyNamesThatIDepend;
	private Collection metaValidators;	
	private Collection validators;
	private Collection onlyOnCreateValidators;
	private Class type;
	private int size;
	private boolean required;
	private boolean hidden;
	private java.lang.String stereotype;
	private List validValues;
	private boolean readOnly;
	private boolean readOnlyCalculated = false;
	private MetaCalculator metaCalculator;	
	private MetaCalculator metaCalculatorDefaultValue;		
	private Collection dependentPropertiesNames;
	private String typeName;
	private boolean key;
	private boolean isKeySet;
	private boolean mappingSet;
	private PropertyMapping mapping;
		
	public void addValidValue(Object validValue) {
		getValidValues().add(validValue);
	}
	public void containsValidValue(Object value) {
		getValidValues().contains(value);
	}
	
	public Object getValidValue(int i) {
		try {		
			if (i == 0) return "";
			return getValidValues().get(i-1);
		}
		catch (IndexOutOfBoundsException ex) {
			ex.printStackTrace();
			System.err.println("[MetaProperty.getValidValue] " + XavaResources.getString("valid_value_not_found_for_index_warning")); 			
			return "[" + i  + "]"; 
		}
	}
	
	/**
	 * The first value is 1, to left 0 for no value case.
	 */
	public int getValidValueIndex(Object value) { 
		return getValidValues().indexOf(value) + 1;
	}
	
	public String getValidValueLabel(ServletRequest request, int i) { 	
		return getValidValueLabel(XavaResources.getLocale(request), i);
	}

	public String getValidValueLabel(Locale locale, Object value) { 	
		return obtainValidValueLabel(locale, value);
	}
	
	public String getValidValueLabel(ServletRequest request, Object value) { 	
		return obtainValidValueLabel(XavaResources.getLocale(request), value);
	}
		
	public String getValidValueLabel(Locale locale, int i) { 
		return obtainValidValueLabel(locale, getValidValue(i));
	}

	/**
	 * A string with the localized labels separate with '|'.
	 */
	public String getValidValuesLabels(ServletRequest request) {
		return getValidValuesLabels(Locales.getCurrent());
	}
	
	/**
	 * A string with the localized labels separate with '|'.
	 */
	public String getValidValuesLabels(Locale locale) {
		if (!hasValidValues()) return "";
		StringBuffer sb = new StringBuffer();
		Iterator it = getValidValues().iterator();
		while (it.hasNext()) {
			sb.append(getValidValueLabel(locale, it.next()));
			if (it.hasNext()) {
				sb.append('|');
			}
		}
		return sb.toString();
	}
	
	private String obtainValidValueLabel(Locale locale, Object value) { 
		String id = getId() + "." + value;											
		return Labels.get(id, locale, Strings.firstUpper(value.toString()));
	}
		
	private IPropertyValidator createRequiredValidator() throws XavaException {
		String validatorClass = null;
		try {
			MetaValidatorRequired vr = null;
			if (!Is.emptyString(getStereotype())) {
				vr = MetaValidators.getMetaValidatorRequiredFor(getStereotype());
			}
			if (vr == null) {
				vr = MetaValidators.getMetaValidatorRequiredFor(getType().getName());	
			}
			if (vr == null) {
				System.err.println(XavaResources.getString("required_validator_not_found_for_type", getType()));
				return new TolerantValidator();
			}
			validatorClass = vr.getValidatorClass();
			return (IPropertyValidator) Class.forName(validatorClass).newInstance();
		} catch (ClassCastException ex) {
			ex.printStackTrace();
			throw new XavaException("property_validator_invalid_class", validatorClass); 
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("create_validator_error", getName(), ex.getLocalizedMessage());
		}
	}
	
	public java.lang.String getStereotype() {
		return stereotype;
	}
	
	public boolean hasStereotype() {
		return !Is.emptyString(stereotype);
	}
	
	public boolean isNumber() throws XavaException {
		return 
			java.lang.Integer.class.equals(getType()) ||
			int.class.equals(getType()) ||
			java.lang.Long.class.equals(getType()) ||
			long.class.equals(getType()) ||
			java.math.BigDecimal.class.equals(getType()) ||
			java.lang.Short.class.equals(getType()) ||
			short.class.equals(getType()) ||
			java.lang.Float.class.equals(getType()) ||
			float.class.equals(getType()) ||
			java.lang.Double.class.equals(getType()) ||
			double.class.equals(getType());		
	}
	
	/**
	 * 
	 * @return Can return -1 if the datatype does not have length concept
	 */
	public int getSize() throws XavaException {		
		if (size == 0) {
			if (!Is.emptyString(getStereotype())) {				
				try {					
					size = DefaultSize.forStereotype(getStereotype());					
					return size;
				}
				catch (ElementNotFoundException ex) {
					// left that get size from type
				}
			}
						
			if (hasValidValues()) {
				size = createLengthFromValidValues();
			}
			else {
				try {
					size = DefaultSize.forType(getType());
				}
				catch (ElementNotFoundException ex) {
					size = -1; // Without length. In some datatypes this is fine
				}
			}
		}
		return size;
	}

	private int createLengthFromValidValues() {		
		Iterator it = getValidValues().iterator();
		int t = 0;
		while (it.hasNext()) {
			Object value = it.next();			 
			if (value != null && value.toString().length() > t) t = value.toString().length();
		}
		return t;
	}

	public Class getType() throws XavaException {				
		if (type == null) {			
			if (Is.emptyString(getTypeName())) {				
				type = obtainTypeFromModel(getName());
			}
			else {
				try { 					
					type = obtainType(getTypeName());
				}
				catch (XavaException ex) {
					try {
						type = obtainTypeFromModel(getName());
					}
					catch (XavaException ex2) {
						throw new XavaException("incorrect_type_for_property", getName(), getTypeName());
					}
				}
			}
		}
		return type;		
	}
	
	public String getCMPTypeName() throws XavaException {
		PropertyMapping mapeo = getMapping();
		if (mapeo == null) return getType().getName();
		if (!mapeo.hasConverter()) return getType().getName();
		return mapeo.getCmpTypeName();
	}
	
	private Class obtainTypeFromModel(String propertyName) throws XavaException {
		try {
			return getMetaModel().getPropertyDescriptor(propertyName).getPropertyType();
		}
		catch (ElementNotFoundException ex) {
			return java.lang.Object.class;
		}		
		catch (Exception ex) {			
			throw new XavaException("type_from_model_error", propertyName);
		}
	}
		
	
	public String getTypeName() throws XavaException {
		if (Is.emptyString(typeName)){
			if (hasValidValues()) setTypeName("int");
			if (!Is.emptyString(getStereotype())) {		
				try {		
					String t = TypeStereotypeDefault.forStereotype(getStereotype());
					setTypeName(t);
				}
				catch (ElementNotFoundException ex) {					
					System.err.println(XavaResources.getString("type_from_stereotype_warning", getStereotype()));
				}
			}
		}
		return typeName;
	}
	
	public void setTypeName(String type) throws XavaException {
		this.typeName = type;
	}
	
	private Class obtainType(String type) throws XavaException {		
		try {
			return Primitives.classForName(type);
		}
		catch (ClassNotFoundException ex2) {
			throw new XavaException("set_type_error", getName(), type);
		}
	}
	

	public boolean hasDefaultValueCalculator() {		
		return metaCalculatorDefaultValue != null;
	}
	
	public boolean hasCalculator() {		
		return metaCalculator != null;
	}
	
	/**
	 * 
	 * @return null if this does not have calculator for default value
	 */
	public ICalculator createDefaultValueCalculator() throws XavaException {
		if (!hasDefaultValueCalculator()) return null;
		return metaCalculatorDefaultValue.createCalculator();		
	}
	
	/**
	 * 
	 * @return null if this does not have calculator 
	 */
	public ICalculator getCalculator() throws XavaException {
		if (!hasCalculator()) return null;		
		return metaCalculator.createCalculator();
	}
	
	public boolean isDefaultCalculatorHibernateIdGenerator() throws XavaException {
		try {
			if (!hasCalculatorDefaultValueOnCreate()) return false;		
			Class calculatorClass = Class.forName(getMetaCalculatorDefaultValue().getClassName()); 
			return IHibernateIdGeneratorCalculator.class.isAssignableFrom(calculatorClass);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("is_default_calculator_hibernate_id_generator_error", getName());
		}
	}	
	
	/**
	 * 	 
	 * @precondition  isDefaultCalculatorHibernateIdGenerator() == true
	 */
	public String getHbmGeneratorCode() throws XavaException {		
		return metaCalculatorDefaultValue.createHibernateIdGeneratorCalculator().hbmGeneratorCode();
	}
	
	/**
	 * @return of IPropertyValidator
	 */
	private Collection getValidators() throws XavaException {
		if (validators == null) {
			validators = new ArrayList();
			if (metaValidators != null) {
				Iterator it = metaValidators.iterator();
				while (it.hasNext()) {
					MetaValidator metaValidator = (MetaValidator) it.next();
					if (metaValidator.isOnlyOnCreate()) {						
						if (onlyOnCreateValidators == null) onlyOnCreateValidators = new ArrayList();
						onlyOnCreateValidators.add(metaValidator.createPropertyValidator());
					}
					else {
						validators.add(metaValidator.createPropertyValidator());
					}
				}
			}
			if (isRequired()) {
				validators.add(createRequiredValidator());
			}
		}
		return validators;
	}
	
	private Collection getOnlyOnCreateValidators() throws XavaException {
		return onlyOnCreateValidators;
	}
	
		
	private List getValidValues() {
		if (validValues == null) {
			validValues = new ArrayList();
		}
		return validValues;
	}
	
	public boolean isKey() {		
		if (!isKeySet) {			
			try {
				if (!(getMetaModel() instanceof MetaEntity)) {								
					key= false;
				}
				else if (getMetaModel().isPojoGenerated() || getMetaModel().isEjbGenerated()) {						
					key = false;						
				}
				else {				
					key= ((MetaEntity) getMetaModel()).isKey(getName());
				}
			}
			catch (XavaException ex) {  
				// false is assumed, but isKeySet is not changed, for retry in future 
				System.err.println(XavaResources.getString("is_key_warning", getName()));
				return false;
			}
			isKeySet = true;
		}		
		return key;
	}
	
	public void setKey(boolean key) {		
		this.key = key;
		isKeySet = true;		
	}
	
	
	public boolean isCalculated() {
		return getMetaCalculator() != null;
	}
	
	public boolean isPersistent() throws XavaException {
		return getMapping() != null;
	}
	
	public boolean isRequired() {
		return required;
	}

	public void setStereotype(String newStereotype) {
		stereotype = newStereotype;
	}
	
	public void setRequired(boolean newRequired) {
		required = newRequired;
	}
	
	public void setSize(int newSize) {
		size = newSize;
	}
	
	public boolean hasValidValues() {
		if (validValues == null) return false;
		return validValues.size() > 0;
	}
			
	public void validate(Messages errors, Object object) throws RemoteException {		
		try {
			validate(errors, object, getValidators());
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException(
					XavaResources.getString("validate_error", getName(), getMetaModel().getName()));					
		}
	}
	
	public void validate(Messages errors, Object object, boolean creating) throws RemoteException {
		try {
			validate(errors, object, getValidators());
			if (creating) validate(errors, object, getOnlyOnCreateValidators());
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException(
					XavaResources.getString("validate_error", getName(), getMetaModel().getName()));					
		}
	}
			
	private void validate(Messages errors, Object object, Collection validators) throws Exception {
		if (validators == null) return;
		Iterator it = validators.iterator();			
		while (it.hasNext()) {
			IPropertyValidator v = (IPropertyValidator) it.next();								
			v.validate(errors, object, getName(), getMetaModel().getName());
		}
	}
		
	public Iterator validValues() {
		return getValidValues().iterator();	
	}
	
	public Iterator validValuesLabels(ServletRequest request) {
		return validValuesLabels(XavaResources.getLocale(request));
	}
	
	public Iterator validValuesLabels(Locale locale) {
		Iterator it = validValues();
		Collection labels = new ArrayList();
		while (it.hasNext()) {
			labels.add(obtainValidValueLabel(locale, it.next()));
		}
		return labels.iterator();
	}
	
	public boolean isReadOnly() throws XavaException {
		if (!readOnlyCalculated) {
			if (isKey()) readOnly = false;
			else if (isCalculated()) readOnly = true;
			else readOnly = calculateIfReadOnly();
			readOnlyCalculated = true;
		}		
		return readOnly;		
	}
	
	private boolean calculateIfReadOnly() { 
		try {			
			if (getMetaModel() == null) return false;
			if (getMetaModel().isPojoGenerated() || getMetaModel().isEjbGenerated()) return false;
			PropertiesManager man = new PropertiesManager(
				getMetaModel().getPropertiesClass());
			return !man.hasSetter(getName());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("[MetaProperty.calculateIfReadOnly] " + XavaResources.getString("read_only_property_warning", getName(), getMetaModel().getName()));
			return true;			
		}
	}
	
	public MetaProperty cloneMetaProperty() throws XavaException {
		try {
			MetaProperty clon = (MetaProperty) super.clone();
			// The next is to force calculate its properties and thus
			// the cloned metaproperty can be desconected from model 
			clon.isReadOnly(); 
			clon.getType();
			return clon;
		}
		catch (XavaException ex) {
			ex.printStackTrace();
			throw new XavaException("property_clone_error", getName(), ex.getLocalizedMessage());	
		}
		catch (CloneNotSupportedException ex) {
			ex.printStackTrace();
			throw new RuntimeException(XavaResources.getString("property_implements_cloneable"));
		}
	}
	
	public MetaCalculator getMetaCalculator() {
		return metaCalculator;
	}

	public void setMetaCalculator(MetaCalculator metaCalculator) {
		this.metaCalculator = metaCalculator;
	}

	public MetaCalculator getMetaCalculatorDefaultValue() {
		return metaCalculatorDefaultValue;
	}

	public void setMetaCalculatorDefaultValue(MetaCalculator metaCalculatorDefaultValue) {
		this.metaCalculatorDefaultValue = metaCalculatorDefaultValue;
	}
	
	public boolean hasDependentProperties() throws XavaException {
		return !getDependentPropertiesNames().isEmpty();
	}
	
	public Collection getDependentPropertiesNames() throws XavaException {
		if (dependentPropertiesNames == null) {
			dependentPropertiesNames = new ArrayList();	
			if (hasMetaModel()) {
				fillDependedPropertiesNames(getMetaModel().getMetaPropertiesCalculated(), false);
				fillDependedPropertiesNames(getMetaModel().getMetaPropertiesWithDefaultValueCalculator(), true);
			}
		}
		return dependentPropertiesNames;
	}
	
	/**
	 * 
	 * @param other  Can be null, in which case return false
	 */
	public boolean depends(MetaProperty other) throws XavaException {
		if (other == null) return false;
		if (!other.hasDependentProperties()) return false;
		return other.getDependentPropertiesNames().contains(getName());
	}
	
	public boolean hasMetaModel() {		
		return getMetaModel() != null;
	}
	
	private void fillDependedPropertiesNames(Collection metaProperties, boolean initialValue) {
		Iterator it = metaProperties.iterator();
		while (it.hasNext()) {
			MetaProperty metaProperty = (MetaProperty) it.next();
			MetaSetsContainer metaCalculator = initialValue?metaProperty.getMetaCalculatorDefaultValue():metaProperty.getMetaCalculator();
			if (!metaCalculator.containsMetaSets()) continue;
			Iterator itMetaSets = metaCalculator.getMetaSets().iterator();
			while (itMetaSets.hasNext()) {
				MetaSet metaSet = (MetaSet) itMetaSets.next();
				if (metaSet.getPropertyNameFrom().equals(getName())) {
					dependentPropertiesNames.add(metaProperty.getName());
				}					
			}
		}		
	}
		
	public Collection getPropertyNamesThatIDepend() {
		if (propertyNamesThatIDepend == null) {		
			MetaSetsContainer metaCalculador = null;
			if (hasCalculator()) {
				metaCalculador = getMetaCalculator();
			}
			else if (hasDefaultValueCalculator()) {
				metaCalculador = getMetaCalculatorDefaultValue();
			}
			else {
				propertyNamesThatIDepend = Collections.EMPTY_LIST;
				return propertyNamesThatIDepend;
			}
				
			if (!metaCalculador.containsMetaSets()) {
				propertyNamesThatIDepend = Collections.EMPTY_LIST;
				return propertyNamesThatIDepend;
			} 
			
			propertyNamesThatIDepend = new ArrayList();
			Iterator itMetaSets = metaCalculador.getMetaSets().iterator();
			while (itMetaSets.hasNext()) {
				MetaSet metaSet = (MetaSet) itMetaSets.next();
				if (!metaSet.hasValue()) {
					propertyNamesThatIDepend.add(metaSet.getPropertyNameFrom());
				}										
			}
		}
		return propertyNamesThatIDepend;				
	}
	
	public void setReadOnly(boolean readOnly) {		
		this.readOnly = readOnly;
		this.readOnlyCalculated = true;				
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
		
	public boolean hasCalculatorDefaultValueOnCreate() {
		return 	metaCalculatorDefaultValue != null &&
				metaCalculatorDefaultValue.isOnCreate();		
	}
	
	/**
	 * Can be null
	 */
	public PropertyMapping getMapping() throws XavaException {	
		if (!mappingSet) {
			mappingSet = true;
			if (getMetaModel() == null) {							
				mapping = null;
				return null;
			}
			String propertyName = null;
			ModelMapping modelMapping = null;			
			if (getMetaModel() instanceof MetaAggregateForReference) {				
				if (getQualifiedName().indexOf('.')>= 0) {
					propertyName = Strings.change(getQualifiedName(), ".", "_");
					modelMapping = getMetaModel().getMetaComponent().getEntityMapping();
				}
				else {
					mapping = null;
					return null;
				}
			}
			if (propertyName == null) propertyName = getName();
			if (modelMapping == null) modelMapping = getMetaModel().getMapping();									
			if (modelMapping == null) mapping = null;
			else {
				try {
					mapping = modelMapping.getPropertyMapping(propertyName);
				}
				catch (ElementNotFoundException ex) {								
					mapping = null;
				}
			}
		}		
		return mapping;
	}

	
	/**
	 * Convert the argument in a object of type valid
	 * for assign to this property. <p>
	 * Convierte el argumento enviado en un objeto de tipo válido
	 * para asignar a esta propiedad. <p>
	 * 
	 * If argument is primitive return the match wrapper.
	 * 
	 * @return Can be null 
	 */
	public Object parse(String value) throws ParseException, XavaException {
		return parse(value, Locale.getDefault());
	}
	
	/**
	 * Convert the argument in a object of type valid
	 * for assign to this property. <p>
	 * Convierte el argumento enviado en un objeto de tipo válido
	 * para asignar a esta propiedad. <p>
	 * 
	 * If argument is primitive return the match wrapper.
	 * 
	 * @return Can be null 
	 */
	public Object parse(String value, Locale locale) throws ParseException, XavaException {
		if (value == null) return null;		
		boolean emptyString = value.equals("");
		Class type = getType();
		if (String.class.isAssignableFrom(type)) return value;
		value = value.trim();
		try { 
			if (Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type)) {
				return emptyString?new Integer(0):new Integer(value);
			}
			if (BigDecimal.class.isAssignableFrom(type)) {
				if (emptyString) return new BigDecimal("0.00");				
				Number n = NumberFormat.getNumberInstance(locale).parse(value);
				return new BigDecimal(n.toString());
			}
			if (java.util.Date.class.isAssignableFrom(type)) {
				return emptyString?null:DateFormat.getDateInstance(DateFormat.SHORT, locale).parse(value);
			}
			if (Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type)) {
				return emptyString?new Long(0l):new Long(value);
			}
			if (Short.class.isAssignableFrom(type) || short.class.isAssignableFrom(type)) {
				return emptyString?new Short((short)0):new Short(value);
			}
			if (Float.class.isAssignableFrom(type) || float.class.isAssignableFrom(type)) {
				if (emptyString) return new Float(0);
				Number n = NumberFormat.getNumberInstance(locale).parse(value);
				return new Float(n.floatValue());
			}
			if (Double.class.isAssignableFrom(type) || double.class.isAssignableFrom(type)) {
				if (emptyString) return new Double(0);
				Number n = NumberFormat.getNumberInstance(locale).parse(value);
				return new Double(n.doubleValue());
			}
			if (Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type)) {							
				return Boolean.valueOf(value);
			}
			if (BigInteger.class.isAssignableFrom(type)) {
				if (emptyString) return BigInteger.ZERO;				
				Number n = NumberFormat.getNumberInstance(locale).parse(value);
				return new BigInteger(n.toString());
			}			
			if (IModel.class.isAssignableFrom(type)) { 
				return parseModelObject(type, value);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new ParseException(XavaResources.getString("from_string_on_property_error", value, type.getName(), getName()), -1);
		}
		throw new ParseException(XavaResources.getString("from_string_on_property_not_supported", type), -1);
	}
	
	
	private Object parseModelObject(Class modelClass, String string) throws Exception {
		if (Is.emptyString(string)) return null;
		IModel model = (IModel) modelClass.newInstance();
		StringTokenizer stringValues = new StringTokenizer(string, "[.]");
		parseModelObject(model, stringValues, "");
		return model;
	}
	
	private void parseModelObject(IModel model, StringTokenizer stringValues, String prefix) throws Exception { 
		java.lang.reflect.Field [] fields = model.getClass().getDeclaredFields();
		Arrays.sort(fields, FieldComparator.getInstance());
		MetaModel metaModel = model.getMetaModel();
		PropertiesManager pm = new PropertiesManager(model);
		for (int i=0; i < fields.length; i++) {			
			if (metaModel.isKey(fields[i].getName())) {
				if (metaModel.containsMetaReference(fields[i].getName())) {
					IModel ref = (IModel) pm.executeGet(fields[i].getName());
					if (ref == null) {
						ref = (IModel) metaModel.getMetaReference(fields[i].getName()).getMetaModelReferenced().getPOJOClass().newInstance();
						pm.executeSet(fields[i].getName(), ref);
					}
					parseModelObject(ref, stringValues, prefix + fields[i].getName() + ".");
				}
				else {
					pm.executeSetFromString(fields[i].getName(), stringValues.nextToken());
				}
			}
		}		
	}
	
	/**
	 * Convert a valid value for this property in a String
	 * valid for display to user. <p>
	 * 
	 * @return Can be null. 	 	 
	 */
	public String format(Object value) throws XavaException {
		return format(value, Locale.getDefault());
	}
	
	/**
	 * Convert a valid value for this property in a String
	 * valid for display to user. <p>
	 * 
	 * @return Can be null. 	 	 
	 */
	public String format(Object value, Locale locale) throws XavaException {
		if (value == null) return "";				
		Class type = getType();
		if (String.class.isAssignableFrom(type)) return (String) value;
		try { 
			if (Integer.class.isAssignableFrom(type) || int.class.isAssignableFrom(type)) {
				return value.toString();
			}
			if (BigDecimal.class.isAssignableFrom(type)) {
				NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
				numberFormat.setMaximumFractionDigits(Integer.MAX_VALUE);
				return numberFormat.format(value);				
			}
			if (java.util.Date.class.isAssignableFrom(type)) {
				return DateFormat.getDateInstance(DateFormat.SHORT, locale).format(value);
			}
			if (Long.class.isAssignableFrom(type) || long.class.isAssignableFrom(type)) {
				return value.toString();
			}
			if (Short.class.isAssignableFrom(type) || short.class.isAssignableFrom(type)) {
				return value.toString();
			}
			if (Float.class.isAssignableFrom(type) || float.class.isAssignableFrom(type)) {
				return NumberFormat.getNumberInstance(locale).format(value);
			}
			if (Double.class.isAssignableFrom(type) || double.class.isAssignableFrom(type)) {
				return NumberFormat.getNumberInstance(locale).format(value);
			}
			if (BigInteger.class.isAssignableFrom(type)) {
				NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
				numberFormat.setMaximumFractionDigits(0);
				return numberFormat.format(value);				
			}			
			if (IModel.class.isAssignableFrom(type)) { 
				return value.toString();
			}

		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("to_string_on_property_error", value, type.getName(), getName());
		}
		throw new XavaException("to_string_on_property_not_supported", type);
	}
		
	public void addMetaValidator(MetaValidator metaValidator) {
		if (metaValidators == null) metaValidators = new ArrayList();
		metaValidators.add(metaValidator);		
	}
	
	public String toString() {
		return "MetaProperty:" + getId();
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof MetaProperty)) return false;
		MetaProperty other = (MetaProperty) o;
		return getQualifiedName().equals(other.getQualifiedName());
	}
	
}