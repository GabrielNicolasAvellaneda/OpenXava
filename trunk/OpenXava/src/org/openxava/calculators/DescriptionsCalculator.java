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
 * Obtiene una colección de descripciones. <p>
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
	private IMetaEjb metaModel;
	private transient Map cache;
	private String select;
	private boolean orderByKey = false;
	private boolean useCache = true;
	private boolean useConvertersInKeys = false;

	private Collection keyPropertiesCollection;	
	

	/**
	 * @see org.openxava.calculators.IJDBCCalculator#setConnectionProvider(IConnectionProvider)
	 */
	public void setConnectionProvider(IConnectionProvider proveedor) {		
		this.provider = proveedor;
	}
	
	/**
	 * La ejecucion pura, sin cache, sin llamada al servidor,...
	 * 
	 * Mejor llamar a {@link #getDescripciones} si se quiere usar
	 * directamente.<br>
	 * 
	 * @see org.openxava.calculators.ICalculator#calculate()
	 */
	public Object calculate() throws Exception {		
	 	comprobarPrecondiciones();			 	
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
					Object dato = it.next();					
					ps.setObject(i++, dato);
				}	
				
			}			
			ResultSet rs = ps.executeQuery();
			List result = new ArrayList();
			if (esClaveMultiple()) {
				leerConClaveMultiple(result, rs); 		
			}
			else {
				leerConClaveSimple(result, rs);
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

	private void comprobarPrecondiciones() throws XavaException {
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

	private void leerConClaveSimple(Collection result, ResultSet rs) throws Exception {
		int columnas = rs.getMetaData().getColumnCount();
		while (rs.next()) {
			KeyAndDescription el = new KeyAndDescription();
			Object clave = getObject(getKeyProperty(), rs, 1, false);
			if (clave instanceof String && isRemoveSpacesInKey()) clave = ((String) clave).trim();
			el.setKey(clave);
			el.setDescription(obtenerDescripcion(2, columnas, rs));
			result.add(el);
		}		
	}


	/**
	 * Method leerConClaveMultiple.
	 * @param result
	 * @param rs
	 */
	private void leerConClaveMultiple(Collection result, ResultSet rs) throws Exception {		
		int columnas = rs.getMetaData().getColumnCount();
		while (rs.next()) {
			KeyAndDescription el = new KeyAndDescription();
			int idx=1;		
			Iterator itNombresClave = getKeyPropertiesCollection().iterator();
			Map clave = new HashMap();
			while (itNombresClave.hasNext()) {
				String nombre = (String) itNombresClave.next();
				clave.put(nombre, getObject(nombre, rs, idx++, isUseConvertersInKeys()));
			}		
			if(isUseConvertersInKeys()) {			
				el.setKey(getMetaModel().obtainPrimaryKeyFromKey(clave));			
			} else {
				el.setKey(getMetaModel().obtainPrimaryKeyFromKeyWithoutConversors(clave));			
			}
			el.setDescription(obtenerDescripcion(idx, columnas, rs));			
			result.add(el);
		}		
	}
	
	private String obtenerDescripcion(int idx, int columnas, ResultSet rs) throws SQLException {
		StringBuffer des = new StringBuffer();
		while (idx <= columnas) {			
			des.append(rs.getObject(idx));				
			idx++;
			if (idx <= columnas) {
				des.append(" ");			
			}
		}
		return des.toString();				
	}

	private IMetaEjb getMetaModel() throws XavaException {
		if (metaModel == null) {
			if (esAgregado()) {
				metaModel = (IMetaEjb) MetaComponent.get(getComponentName()).getMetaAggregate(getAggregateName());
			}	
			else {		
				metaModel = (IMetaEjb) MetaComponent.get(getComponentName()).getMetaEntity();
			}
		}
		return metaModel;
	}

	/**
	 * Method getCantidadCamposClaves.
	 * @return int
	 */
	private int getCantidadCamposClaves() {
		return getKeyPropertiesCollection().size();
	}

	
	private Object getObject(String nombrePropiedad, ResultSet rs, int i, boolean usarConversor) throws Exception {
		if (usarConversor && getMapeo().hasConverter(nombrePropiedad)) {
			return getObjectConConversor(nombrePropiedad, rs, i);
		}
		Class tipo = getTipoCodigo(nombrePropiedad);
		if (!tipo.isPrimitive()) return rs.getObject(i);
		if (tipo.equals(Integer.TYPE)) {
			return new Integer(rs.getInt(i));
		}
		else if (tipo.equals(Long.TYPE)) {
			return new Long(rs.getLong(i));
		}
		else if (tipo.equals(Short.TYPE)) {
			return new Short(rs.getShort(i));
		}		
		else if (tipo.equals(Double.TYPE)) {
			return new Double(rs.getDouble(i));
		}		
		else if (tipo.equals(Float.TYPE)) {
			return new Float(rs.getFloat(i));
		}				
		return rs.getObject(i);	
	}
	
	private boolean esClaveMultiple() {
		return !Is.emptyString(keyProperties);
	}
		
	private Class getTipoCodigo(String nombrePropiedad) throws XavaException {
		try {
			return getMapeo().getType(nombrePropiedad);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("code_type_descriptions_calculator_error", ex.getLocalizedMessage());
		}
	}
	
	/**
	 * Hace una llamada remota si estamos en un cliente.<br>
	 * Hace caché según para los valores de los parametros actuales.<br>
	 * 
	 * @return Coleccion de <tt>CodigoDescripcion</tt>. Nunca nulo.
	 */
	public Collection getDescriptions() throws Exception {	
		if (condicionTieneArgumentos() && !hasParameters()) return Collections.EMPTY_LIST;
		if (!isUseCache()) {
			return (Collection) calculate();
		}
		Collection guardado = (Collection) getCache().get(getParameters());
		if (guardado != null) {						
			return guardado;
		}
		Collection resultado = (Collection) calculate();
		getCache().put(getParameters(), resultado);				
		return resultado;	
	}

	private boolean condicionTieneArgumentos() {
		return this.condition != null && this.condition.indexOf('?') >= 0;		
	}

	private Map getCache() {
		if (cache == null) {
			cache = new HashMap();
		}
		return cache;		
	}

	/**
	 * Se usa cuando hay una sola propiedad que actua
	 * como clave. <p>
	 * 
	 * Es excluyente con <tt>propiedadesClave</tt> 
     *
	 * @return String
	 */
	public String getKeyProperty() {
		return keyProperty;
	}

	/**
	 * Returns the propiedadDescripcion.
	 * @return String
	 */
	public String getDescriptionProperty() {
		return descriptionProperty;
	}
	
	private Collection getKeyPropertiesCollection() {
		if (keyPropertiesCollection == null) {
			keyPropertiesCollection = new ArrayList();
			String fuente = Is.emptyString(keyProperty)?keyProperties:keyProperty;
			StringTokenizer st = new StringTokenizer(fuente, ",");
			while (st.hasMoreElements()) {
				keyPropertiesCollection.add(st.nextToken().trim());
			}
		}
		return keyPropertiesCollection;
	}
	

	

	public void setKeyProperty(String propiedadCodigo) {		
		this.keyProperty = propiedadCodigo;		
	}

	/**
	 * Sets the propiedadDescripcion.
	 * @param propiedadDescripcion The propiedadDescripcion to set
	 */
	public void setDescriptionProperty(String propiedadDescripcion) {
		this.descriptionProperty = propiedadDescripcion;
	}
	
	private String getSelect() throws XavaException {
		if (select == null) {			
			ModelMapping mapeo = getMapeo();
			StringBuffer sb = new StringBuffer("select ");
			Iterator itPropiedadesClave = getKeyPropertiesCollection().iterator();
			while (itPropiedadesClave.hasNext()) {
				String propiedadClave = (String) itPropiedadesClave.next();				
				sb.append(mapeo.getColumn(propiedadClave));				
				sb.append(", ");				
			}
			if (Is.emptyString(getDescriptionProperties())) {				
				sb.append(mapeo.getColumn(getDescriptionProperty()));				
			}
			else {
				StringTokenizer st = new StringTokenizer(getDescriptionProperties(), ",");
				while (st.hasMoreTokens()) {
					String propiedadDescripcion = st.nextToken().trim();					
					sb.append(mapeo.getColumn(propiedadDescripcion));					
					if (st.hasMoreTokens()) {
						sb.append(", ");
					}					
				}
			}			
			sb.append(" from ");
			sb.append(mapeo.getTable());
			if (hayCondicion()) {
				sb.append(" where ");
				sb.append(getCondicionSQL(mapeo));
			}
			select = sb.toString();						
		}				
		return select;
	}
	
	private boolean hayCondicion() {
		return !Is.emptyString(condition);
	}
	
	private String getCondicionSQL(ModelMapping mapeo) throws XavaException {
		return mapeo.changePropertiesByColumns(getCondition());
	}

	/**
	 * Returns the modelo.
	 * @return String
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets the modelo.
	 * @param modelo The modelo to set
	 */
	public void setModel(String modelo) {
		this.model = modelo==null?"":modelo;
		this.metaModel = null;		
		this.componentName = null;
		this.aggregateName = null;
		StringTokenizer st = new StringTokenizer(this.model, ".");		
		if (st.hasMoreTokens()) this.componentName = st.nextToken();
		if (st.hasMoreTokens()) this.aggregateName = st.nextToken();
	}

	/**
	 * Returns the condiciones.
	 * @return String
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * Sets the condiciones.
	 * @param condiciones The condiciones to set
	 */
	public void setCondition(String condiciones) {	    
	    if (condiciones!=null && condiciones.toLowerCase().indexOf("year(curdate())")>=0){
	        Calendar cal=Calendar.getInstance();
	        cal.setTime(new java.util.Date());
	    	condiciones = Strings.change(condiciones,"year(curdate())",String.valueOf(cal.get(Calendar.YEAR)));
	    	System.out.println("[DescriptionsCalculator.setCondition:] La sustituimos:" + condiciones);
	    }
	    
		this.condition = condiciones;		
	}


	public boolean hasParameters() {
		return parameters != null && !parameters.isEmpty();
	}
	/**
	 * Returns the parametro.
	 * @return Object
	 */
	public Collection getParameters() {
		return parameters;
	}

	public void setParameters(Collection parametros) {
		this.parameters = parametros;		
	}
	
	public void setParameters(Collection parametros, IFilter filtro) throws FilterException {		
		if (filtro != null) {
			Object [] param = parametros==null?null:parametros.toArray();			
			param = (Object []) filtro.filter(param);			
			parametros = Arrays.asList(param);			
		}
		this.parameters = parametros;				
	}
		
	private Object getObjectConConversor(String nombrePropiedad, ResultSet rs, int i) throws Exception {
		IConverter conversor = getMapeo().getConverter(nombrePropiedad);
		return conversor.toJava(rs.getObject(i));
	}
	
	/**
	 * Se usa cuando hay una mas de una propiedad que actua
	 * como clave, o habiendo solo una se quiere usar una
	 * clase envoltorio de clave primaria. <p>
	 * 
	 * Es excluyente con <tt>propiedadesClave</tt> 
     *
	 * @return String
	 */
	public String getKeyProperties() {
		return keyProperties;
	}

	/**
	 * Sets the propiedadesClave.
	 * @param propiedadesClave The propiedadesClave to set
	 */
	public void setKeyProperties(String propiedadesClave) {		
		this.keyProperties = propiedadesClave;
	}


	private String getAggregateName() {
		return aggregateName;
	}

	private String getComponentName() {
		return componentName;
	}
	
	private boolean esAgregado() {
		return !Is.emptyString(aggregateName);
	}
	
	private ModelMapping getMapeo() throws XavaException {
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

	/**
	 * @return
	 */
	public boolean isRemoveSpacesInKey() {
		return removeSpacesInKey;
	}

	/**
	 * @param b
	 */
	public void setRemoveSpacesInKey(boolean b) {
		removeSpacesInKey = b;
	}

	/**
	 * 
	 */
	public boolean isUseConvertersInKeys() {
		return useConvertersInKeys;		
	}

	/**
	 * 
	 */
	public void setUseConvertersInKeys(boolean b) {
		useConvertersInKeys = b;
	}

}
