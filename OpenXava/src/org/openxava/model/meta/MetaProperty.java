package org.openxava.model.meta;


import java.math.*;
import java.rmi.*;
import java.text.*;
import java.util.*;

import javax.servlet.*;

import org.openxava.calculators.*;
import org.openxava.mapping.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;
import org.openxava.validators.*;
import org.openxava.validators.meta.*;



/**
 * @author Javier Paniza
 */
public class MetaProperty extends MetaMember implements Cloneable {

	private Collection propertyNamesThatIDepend;
	private NumberFormat numberFormat;
	private Collection metaValidators;
	private DateFormat dateFormat;
	private Collection validators;
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
	private ICalculator calculator;	
	private ICalculator calculatorDefaultValue;
	


	private Collection dependentPropertiesNames;

	private String typeName;
	private boolean key;
	private boolean isKeySet;
	private boolean mappingSet;
	private PropertyMapping mapping;
	
	
	/**
	 * Comentario de constructor Propiedad.
	 */
	public MetaProperty() {
		super();
	}
	/**
	 * Inserte aquí la descripción del método.
	 * Fecha de creación: (26/07/2001 17:38:34)
	 * @return java.util.Collection
	 */
	public void addValidValue(Object valorPosible) {
		getValidValues().add(valorPosible);
	}
	/**
	 * Inserte aquí la descripción del método.
	 * Fecha de creación: (26/07/2001 17:49:19)
	 * @param valor java.lang.Object
	 */
	public void containsValidValue(Object valor) {
		getValidValues().contains(valor);
	}
	
	public Object getValidValue(int i) {
		try {		
			if (i == 0) return "";
			return getValidValues().get(i-1);
		}
		catch (IndexOutOfBoundsException ex) {
			ex.printStackTrace();
			System.err.println("[MetaPropiedad.getValorPosible] ¡ADVERTENCIA!: No existe valor posible para índice " + i);
			return "[" + i  + "]"; 
		}
	}
	
	public String getValidValueLabel(ServletRequest request, int i) { 	
		return getValidValueLabel(XavaResources.getLocale(request), i);
	}

	public String getValidValueLabel(Locale locale, Object valor) { 	
		return obtenerEtiquetaValorPosible(locale, valor);
	}
	
	public String getValidValueLabel(ServletRequest request, Object valor) { 	
		return obtenerEtiquetaValorPosible(XavaResources.getLocale(request), valor);
	}
		
	public String getValidValueLabel(Locale locale, int i) { 
		return obtenerEtiquetaValorPosible(locale, getValidValue(i));
	}

	/**
	 * A string with the localized labels separate with '|'.
	 */
	public String getValidValuesLabels(ServletRequest request) {
		return getValidValuesLabels(request.getLocale());
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
	
	private String obtenerEtiquetaValorPosible(Locale locale, Object valor) { 
		String id = getId() + "." + valor;
		try {											
			return Labels.get(id, locale);
		}
		catch (Exception ex) {			
			System.err.println(XavaResources.getString("label_i18n_warning", id)); 				
			return Strings.firstUpper(valor.toString());
		}		
	}
	
	
	
	private IPropertyValidator crearValidadorRequerido() throws XavaException {
		// assert(getValidador()
		String claseValidador = null;
		try {
			MetaValidatorRequired vr = null;
			if (!Is.emptyString(getStereotype())) {
				vr = MetaValidators.getMetaValidatorRequiredFor(getStereotype());
			}
			if (vr == null) {
				vr = MetaValidators.getMetaValidatorRequiredFor(getType().getName());	
			}
			if (vr == null) {
				System.err.println(
					"ADVERTENCIA: No hay definido un validador para comprobar una propiedad requerida de tipo "
						+ getType());
				return new TolerantValidator();
			}
			claseValidador = vr.getValidatorClass();
			return (IPropertyValidator) Class.forName(claseValidador).newInstance();
		} catch (ClassCastException ex) {
			ex.printStackTrace();
			throw new XavaException(
				"El validador " + claseValidador + " ha de implementar IValidadorPropiedad");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException(
				"Imposible instanciar validador para comprobar si la propiedad está presente"
					+ getName()
					+ " por:\n"
					+ ex.getLocalizedMessage());
		}
	}
	
	/**
	 * 
	 * @return java.lang.String
	 */
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
	 * @return Puede devolver -1, si para ese tipo de dato no tiene
	 *         sentido el concepto de longitud.
	 */
	public int getSize() throws XavaException {		
		if (size == 0) {
			if (!Is.emptyString(getStereotype())) {				
				try {					
					size = DefaultSize.forStereotype(getStereotype());					
					return size;
				}
				catch (ElementNotFoundException ex) {
					// Lo dejamos correr y asi coge la del tipo
				}
			}
						
			if (hasValidValues()) {
				size = crearLogitudSegunValoresPosibles();
			}
			else {
				try {
					size = DefaultSize.forType(getType());
				}
				catch (ElementNotFoundException ex) {
					size = -1; // Sin longitud. Hay tipos de datos donde no tiene sentido la longitud
				}
			}
		}
		return size;
	}

	/**
	 * Method crearLogitudSegunValoresPosibles.
	 * @return int
	 */
	private int crearLogitudSegunValoresPosibles() {		
		Iterator it = getValidValues().iterator();
		int t = 0;
		while (it.hasNext()) {
			String valor = (String) it.next();			
			if (valor != null && valor.length() > t) t = valor.length();
		}
		return t;
	}

	/**
	 * 
	 * @return java.lang.Class
	 */
	public Class getType() throws XavaException {				
		if (type == null) {			
			if (Is.emptyString(getTypeName())) {				
				type = obtenerTipoDesdeModelo(getName());
			}
			else {
				try { 					
					type = obtenerTipo(getTypeName());
				}
				catch (XavaException ex) {
					try {
						type = obtenerTipoDesdeModelo(getName());
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
	
	private Class obtenerTipoDesdeModelo(String nombrePropiedad) throws XavaException {
		try {
			return getMetaModel().getPropertyDescriptor(nombrePropiedad).getPropertyType();
		}
		catch (ElementNotFoundException ex) {
			return java.lang.Object.class;
		}		
		catch (Exception ex) {			
			throw new XavaException("type_from_model_error", nombrePropiedad);
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
	
	public void setTypeName(String tipo) throws XavaException {
		this.typeName = tipo;
	}
	
	private Class obtenerTipo(String tipo) throws XavaException {		
		if (Is.emptyString(tipo)) {
			return null;			
		}
		Class result = getClasePrimitivo(tipo);
		if ("byte[]".equals(tipo) || "byte []".equals(tipo)) return byte[].class;
		if (result == null) {
			try {
				result = Class.forName(tipo);
			}
			catch (ClassNotFoundException ex) {
				try {
					result = Class.forName("java.lang." + tipo);
				}
				catch (ClassNotFoundException ex2) {
					throw new XavaException("set_type_error", getName(), tipo);
				}
			}
		}
		return result;
	}
	
	/**
	 * Method getClasePrimitivo.
	 * @param tipo
	 * @return Class
	 */
	private Class getClasePrimitivo(String tipo) {
		if (tipo.equals("boolean")) {
			return Boolean.TYPE;	
		}
		else if (tipo.equals("byte")) {
			return Byte.TYPE;
		}
		else if (tipo.equals("char")) {
			return Character.TYPE;
		}
		else if (tipo.equals("short")) {
			return Short.TYPE;
		}
		else if (tipo.equals("int")) {
			return Integer.TYPE;
		}
		else if (tipo.equals("long")) {
			return Long.TYPE;
		}
		else if (tipo.equals("float")) {
			return Float.TYPE;
		}
		else if (tipo.equals("double")) {
			return Double.TYPE;
		}
		return null;
	}



	
	public boolean hasCalculatorDefaultValue() {		
		return metaCalculatorDefaultValue != null;
	}
	
	public boolean hasCalculator() {		
		return metaCalculator != null;
	}
	
	
	
	
		
	/**
	 * 
	 * @return null si no tiene calculador para valor inicial.
	 */
	public ICalculator getCalculatorDefaultValue() throws XavaException {
		if (!hasCalculatorDefaultValue()) return null;
		if (calculatorDefaultValue == null) {
			calculatorDefaultValue = metaCalculatorDefaultValue.createCalculator();
		}
		return calculatorDefaultValue;
	}
	
	/**
	 * 
	 * @return null si no tiene calculador.
	 */
	public ICalculator getCalculator() throws XavaException {
		if (!hasCalculator()) return null;
		if (calculator == null) {
			calculator = metaCalculator.createCalculator();
		}
		return calculator;
	}
	
	
	/**
	 * @return de IValidadorPropiedad
	 */
	private Collection getValidators() throws XavaException {
		if (validators == null) {
			validators = new ArrayList();
			if (metaValidators != null) {
				Iterator it = metaValidators.iterator();
				while (it.hasNext()) {
					MetaValidator metaValidador = (MetaValidator) it.next();
					validators.add(metaValidador.createPropertyValidator());
				}
			}
			if (isRequired()) {
				validators.add(crearValidadorRequerido());
			}
		}
		return validators;
	}
	
	/**
	 * Inserte aquí la descripción del método.
	 * Fecha de creación: (26/07/2001 17:38:34)
	 * @return java.util.Collection
	 */
	private List getValidValues() {
		if (validValues == null) {
			validValues = new ArrayList();
		}
		return validValues;
	}
	/**
	 * 
	 * @return boolean
	 */
	public boolean isKey() {
		if (!isKeySet) {			
			try {
				if (!(getMetaModel() instanceof MetaEntity)) {								
					key= false;
				}
				else if (getMetaModel().isGenerateXDocLet()) {						
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
	
	public void setKey(boolean clave) {		
		this.key = clave;
		isKeySet = true;
	}
	
	
	public boolean isCalculated() {
		return getMetaCalculator() != null;
	}
	
	public boolean isPersistent() throws XavaException {
		return getMapping() != null;
	}
	
	/**
	 * 
	 * @return boolean
	 */
	public boolean isRequired() {
		return required;
	}
	/**
	 * 
	 * @param newEstereotipo java.lang.String
	 */
	public void setStereotype(java.lang.String newEstereotipo) {
		stereotype = newEstereotipo;
	}
	/**
	 * 
	 * @param newRequerido boolean
	 */
	public void setRequired(boolean newRequerido) {
		required = newRequerido;
	}
	/**
	 * 
	 * @param newTamaño int
	 */
	public void setSize(int newLongitud) {
		size = newLongitud;
	}
	/**
	 * Inserte aquí la descripción del método.
	 * Fecha de creación: (26/07/2001 18:39:08)
	 * @return boolean
	 */
	public boolean hasValidValues() {
		if (validValues == null) return false;
		return validValues.size() > 0;
	}
	
	
	public void validate(Messages errores,	Object objeto) throws RemoteException {
		try {
			Iterator it = getValidators().iterator();
			while (it.hasNext()) {
				IPropertyValidator v = (IPropertyValidator) it.next();								
				v.validate(errores, objeto, getName(), getMetaModel().getName());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException(
				"Imposible validar por:\n" + ex.getLocalizedMessage());
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
		Collection etiquetas = new ArrayList();
		while (it.hasNext()) {
			etiquetas.add(obtenerEtiquetaValorPosible(locale, it.next()));
		}
		return etiquetas.iterator();
	}
	
	/**
	 * Si es clave no se considera de solo lectura. <p>
	 */
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
			PropertiesManager man = new PropertiesManager(
				getMetaModel().getPropertiesClass());
			return !man.hasSetter(getName());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("[MetaProperty.calculateIfReadOnly] ¡ADVERTENCIA! Imposible comprobar si la propiedad " + getName() + " de " + getMetaModel().getName() + " es de solo lectura");
			return true;			
		}
	}
	
	/**
	 * @see java.lang.Object#clone()
	 */
	public MetaProperty cloneMetaProperty() throws XavaException {
		try {
			MetaProperty clon = (MetaProperty) super.clone();
			// Lo siguiente es para obligar a calcular las propiedades, y
			// así poder desconectar la metapropiedad clonada del modelo (p. ej. cambiando el nombre de propiedad)
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
	
	

	
	
	/**
	 * Returns the metaCalculador.
	 * @return MetaCalculador
	 */
	public MetaCalculator getMetaCalculator() {
		return metaCalculator;
	}

	/**
	 * Sets the metaCalculador.
	 * @param metaCalculador The metaCalculador to set
	 */
	public void setMetaCalculator(MetaCalculator metaCalculador) {
		this.metaCalculator = metaCalculador;
	}

	/**
	 * Returns the metaCalculadorValorInicial.
	 * @return MetaCalculador
	 */
	public MetaCalculator getMetaCalculatorDefaultValue() {
		return metaCalculatorDefaultValue;
	}

	/**
	 * Sets the metaCalculadorValorInicial.
	 * @param metaCalculadorValorInicial The metaCalculadorValorInicial to set
	 */
	public void setMetaCalculatorDefaultValue(MetaCalculator metaCalculadorValorInicial) {
		this.metaCalculatorDefaultValue = metaCalculadorValorInicial;
	}
	
	public boolean hasDependentProperties() throws XavaException {
		return !getDependentPropertiesNames().isEmpty();
	}
	
	public Collection getDependentPropertiesNames() throws XavaException {
		if (dependentPropertiesNames == null) {
			dependentPropertiesNames = new ArrayList();	
			if (hasMetaModel()) {
				llenarNombresPropiedadesDependientes(getMetaModel().getMetaPropertiesCalculated(), false);
				llenarNombresPropiedadesDependientes(getMetaModel().getMetaPropertiesWithDefaultValueCalculator(), true);
			}
		}
		return dependentPropertiesNames;
	}
	
	/**
	 * 
	 * @param otra Puede ser nulo, en cuyo caso devuelve falso.
	 */
	public boolean depends(MetaProperty otra) throws XavaException {
		if (otra == null) return false;
		if (!otra.hasDependentProperties()) return false;
		return otra.getDependentPropertiesNames().contains(getName());
	}
		
	public boolean hasMetaModel() {		
		return getMetaModel() != null;
	}
	
	
	
	private void llenarNombresPropiedadesDependientes(Collection metaPropiedades, boolean valorInicial) {
		Iterator it = metaPropiedades.iterator();
		while (it.hasNext()) {
			MetaProperty metaPropiedad = (MetaProperty) it.next();
			MetaSetsContainer metaCalculador = valorInicial?metaPropiedad.getMetaCalculatorDefaultValue():metaPropiedad.getMetaCalculator();
			if (!metaCalculador.containsMetaSets()) continue;
			Iterator itMetaPoners = metaCalculador.getMetaSets().iterator();
			while (itMetaPoners.hasNext()) {
				MetaSet metaPoner = (MetaSet) itMetaPoners.next();
				if (metaPoner.getPropertyNameFrom().equals(getName())) {
					dependentPropertiesNames.add(metaPropiedad.getName());
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
			else if (hasCalculatorDefaultValue()) {
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
			Iterator itMetaPoners = metaCalculador.getMetaSets().iterator();
			while (itMetaPoners.hasNext()) {
				MetaSet metaPoner = (MetaSet) itMetaPoners.next();
				if (!metaPoner.hasValue()) {
					propertyNamesThatIDepend.add(metaPoner.getPropertyNameFrom());
				}										
			}
		}
		return propertyNamesThatIDepend;				
	}
	

	/**
	 * Sets the soloLectura.
	 * @param soloLectura The soloLectura to set
	 */
	public void setReadOnly(boolean soloLectura) {		
		this.readOnly = soloLectura;
		this.readOnlyCalculated = true;				
	}

	/**
	 * <tt>isOculto</tt> en femenino.
	 * @return boolean
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * Sets the oculta.
	 * @param oculta The oculta to set
	 */
	public void setHidden(boolean oculta) {
		this.hidden = oculta;
	}
	/**
	 * Method tieneCalculadorValorDefectoAlCrear.
	 * @return boolean
	 */
	public boolean hasCalcultaroDefaultValueOnCreate() {
		return 	metaCalculatorDefaultValue != null &&
				metaCalculatorDefaultValue.isOnCreate();		
	}
	
	/**
	 * Puede ser nulo.
	 */
	public PropertyMapping getMapping() throws XavaException {	
		if (!mappingSet) {
			mappingSet = true;
			if (getMetaModel() == null) {							
				mapping = null;
				return null;
			}
			String propertyName = null;
			ModelMapping mapeoModelo = null;
			if (getMetaModel() instanceof MetaAggregateBean) {
				if (getQualifiedName().indexOf('.')>= 0) {
					propertyName = Strings.change(getQualifiedName(), ".", "_");
					mapeoModelo = getMetaModel().getMetaComponent().getEntityMapping();
				}
				else {
					mapping = null;
					return null;
				}
			}
			if (propertyName == null) propertyName = getName();
			if (mapeoModelo == null) mapeoModelo = getMetaModel().getMapping();									
			if (mapeoModelo == null) mapping = null;
			else {
				try {
					mapping = mapeoModelo.getPropertyMapping(propertyName);
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
	 * @return Puede ser nulo. 
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
	 * @return Puede ser nulo. 
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
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new ParseException(XavaResources.getString("from_string_on_property_error", value, type.getName(), getName()), -1);
		}
		throw new ParseException(XavaResources.getString("from_string_on_property_not_supported", type), -1);
	}
	
	
	/**
	 * Conver a valid value for this property in a String
	 * valid for display to user. <p>
	 * 
	 * @return Can be null. 	 	 
	 */
	public String format(Object value) throws XavaException {
		return format(value, Locale.getDefault());
	}
	
	/**
	 * Conver a valid value for this property in a String
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
				return NumberFormat.getNumberInstance(locale).format(value);				
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
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("to_string_on_property_error", value, type.getName(), getName());
		}
		throw new XavaException("to_string_on_property_not_supported", type);
	}
		
	public void addMetaValidator(MetaValidator metaValidador) {
		if (metaValidators == null) metaValidators = new ArrayList();
		metaValidators.add(metaValidador);		
	}
	
	public String toString() {
		return "MetaPropiedad:" + getId();
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof MetaProperty)) return false;
		MetaProperty otra = (MetaProperty) o;
		return getQualifiedName().equals(otra.getQualifiedName());
	}

}