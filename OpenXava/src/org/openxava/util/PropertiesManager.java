package org.openxava.util;

import java.beans.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * Maneja mediante reflexión las propiedades de un objeto. <p>
 *
 * Excluye automáticamente la propiedad <tt>class</tt> de <tt>Object</tt> y
 * las propiedades indexadas que no tengan acceso no indexado.<br>
 *
 * @author  Javier Paniza
 */

public class PropertiesManager implements java.io.Serializable {

	private transient Map propertyDescriptors;
	private Object objeto;
	private Class clase;

	/**
	 * Constructor por defecto.
	 */
	public PropertiesManager() {
	}

	/**
	 * Constructor a partir de la clase del objeto a manejar.
	 *
	 * Este valor inicial puede cambiar a lo largo de la vida
	 * de <tt>this</tt>.<br>
	 */
	public PropertiesManager(Class clase) {
		this.clase = clase;
	}

	/**
	 * Constructor a partir del objeto a manejar. <p>
	 *
	 * @param objeto  Objeto a manejar
	 */
	public PropertiesManager(Object objeto) {
		this.objeto = objeto;
	}
	/**
	 * Permite obtener el valor de la propiedad indicada. <p>
	 *
	 * <p>Precondición</b>
	 * <ul>
	 * <li> <tt>this.objeto != null</tt>
	 * <li> <tt>this.tieneGetter(nombrePropiedad)</tt>
	 * </ul>
	 *
	 * @param nombrePropiedad  Nombre de la propiedad de la que se quiere obtener el valro
	 * @return Valor de la propiedad
	 * @exception InvocationTargetException  Excepción originada por el método de acceso original.
	 * @exception PropertiesManagerException  Algún problema inesperado.
	 */
	public Object executeGet(String nombrePropiedad)
		throws InvocationTargetException, PropertiesManagerException {
		try {
			PropertyDescriptor pd = getPropertyDescriptor(nombrePropiedad);
			Method met = pd.getReadMethod();
			if (met == null) {
				throw new PropertiesManagerException(
					"La propiedad "
						+ nombrePropiedad
						+ " en "
						+ getClase()
						+ " es de solo escritura");
			}
			return met.invoke(objeto, null);
		}
		catch (PropertiesManagerException ex) {
			throw ex;
		}
		catch (InvocationTargetException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(
				"ADVERTENCIA: Imposible obtener el valor de la propiedad "
					+ nombrePropiedad);
			throw new PropertiesManagerException(
				"Imposible obtener el valor de la propiedad "
					+ nombrePropiedad);
		}
	}
	/**
	 * Permite obtener los valores de un conjunto de propiedades de una vez. <p>
	 *
	 * <p>Precondición</b>
	 * <ul>
	 * <li> <tt>this.objeto != null</tt>
	 * </ul>
	 *
	 * @param propiedadesAReplica  Nombres de las propiedades a replicar, separadas
	 *                             por dos puntos (:). Las propiedades han de
	 *                             existir en el objeto receptor. Si se envía nulo
	 *                             se devuelve un mapa vacío.
	 * @return Mapa con <tt>String nombrePropiedad:Object valor</tt>. Nunca será nulo.
	 * @exception InvocationTargetException  Excepción originada por el método de acceso original.
	 * @exception PropertiesManagerException  Algún problema inesperado.
	 */
	public Map executeGets(String propiedadesAReplicar)
		throws InvocationTargetException, PropertiesManagerException {
		Map rs = new HashMap();
		if (propiedadesAReplicar == null)
			return rs;
		Iterator ipropiedades =
			stringToArrayString(propiedadesAReplicar).iterator();
		while (ipropiedades.hasNext()) {
			String nombre = (String) ipropiedades.next();
			Object valor = executeGet(nombre);
			rs.put(nombre, valor);
		}
		return rs;
	}
	/**
	 * Actualiza la propiedad indicada. <p>
	 *
	 * <p>Precondición</b>
	 * <ul>
	 * <li> <tt>this.objeto != null</tt>
	 * </ul>   
	 *
	 * @param nombrePropiedad Nombre de la propiedad a actualizar.
	 * @param valor  Nuevo valor para la propiedad. Tiene que ser de un tipo permitido.
	 * @exception InvocationTargetException  Excepción originada por el método de acceso original.
	 * @exception PropertiesManagerException  Algún problema inesperado.
	 */
	public void executeSet(String nombrePropiedad, Object valor)
		throws InvocationTargetException, PropertiesManagerException {
		Method met = null;
		PropertyDescriptor pd = null; 	
		try {
			pd = getPropertyDescriptor(nombrePropiedad);
			met = pd.getWriteMethod();
			if (met == null) {
				throw new PropertiesManagerException(
					"La propiedad "
						+ nombrePropiedad
						+ " en "
						+ getClase()
						+ " es de solo lectura");
			}

			if (valor == null && pd.getPropertyType().isPrimitive()) {
				valor = nuloToValorDefecto(pd.getPropertyType());
			}
			Object[] arg = { valor };

			met.invoke(objeto, arg);
		}
		catch (PropertiesManagerException ex) {
			throw ex;
		}
		catch (IllegalArgumentException ex) {			
			Object numero = intentarConvertirEnNumero(pd.getPropertyType(), valor);  			
			if (numero != null) {
				Object [] arg = { numero };
				try {
					met.invoke(objeto, arg);
				}
				catch (Exception ex2) {
					ex.printStackTrace();
					throw new PropertiesManagerException(
						"Imposible actualizar el valor de la propiedad "
							+ nombrePropiedad);
				}				
			}
			else {
				throw ex;
			} 			
		}
		catch (InvocationTargetException ex) {
			throw ex; 
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new PropertiesManagerException(
				"Imposible actualizar el valor de la propiedad "
					+ nombrePropiedad);
		}
	}

	/**
	 * Actualiza la propiedad indicada conviertiendolo
	 * en el tipo adecuado desde una cadena. <p>
	 *
	 * <p>Precondición</b>
	 * <ul>
	 * <li> <tt>this.objeto != null</tt>
	 * </ul>   
	 *
	 * @param nombrePropiedad Nombre de la propiedad a actualizar.
	 * @param valor  Nuevo valor para la propiedad. Tiene que ser de un tipo permitido, o bien una
	 *                        cadena cuyo contenido pueda convertirse al tipo permitido.
	 * @exception InvocationTargetException  Excepción originada por el método de acceso original.
	 * @exception PropertiesManagerException  Algún problema inesperado.
	 */
	public void executeSetFromString(String nombrePropiedad, Object valor)
		throws InvocationTargetException, PropertiesManagerException {
		try {
			PropertyDescriptor pd = getPropertyDescriptor(nombrePropiedad);
			Method met = pd.getWriteMethod();
			if (met == null) {
				throw new PropertiesManagerException(
					"La propiedad "
						+ nombrePropiedad
						+ " en "
						+ getClase()
						+ " es de solo lectura");
			}

			if (valor == null && pd.getPropertyType().isPrimitive()) {
				valor = nuloToValorDefecto(pd.getPropertyType());
			}
			Class tipo = pd.getPropertyType();
			if (valor instanceof String && !tipo.equals(String.class)) {
				valor = Strings.toObject(tipo, (String) valor);
			}
			Object[] arg = { valor };

			met.invoke(objeto, arg);
		}
		catch (PropertiesManagerException ex) {
			throw ex;
		}
		catch (InvocationTargetException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new PropertiesManagerException(
				"Imposible actualizar el valor de la propiedad "
					+ nombrePropiedad);
		}
	}

	/**
	 * Method nuloToValorDefecto.
	 * @param valor
	 * @return Object
	 */
	private Object nuloToValorDefecto(Class tipo) {
		if (tipo.equals(java.lang.Integer.TYPE)) {
			return new Integer(0);
		}
		else if (tipo.equals(java.lang.Long.TYPE)) {
			return new Long(0);
		}
		else if (tipo.equals(java.lang.Short.TYPE)) {
			return new Short((short) 0);
		}
		else if (tipo.equals(java.lang.Boolean.TYPE)) {
			return new Boolean(false);
		}
		else if (tipo.equals(java.lang.Double.TYPE)) {
			return new Double(0);
		}
		else if (tipo.equals(java.lang.Float.TYPE)) {
			return new Float(0);
		}
		else if (tipo.equals(java.lang.Byte.TYPE)) {
			return new Byte((byte) 0);
		}
		return null;
	}

	/**
	 * Actualiza las propiedades indicadas de un solo golpe. <p>
	 *
	 * <p>Precondición</b>
	 * <ul>
	 * <li> <tt>this.objeto != null</tt>
	 * <li> <tt>this.tieneSetter(nombrePropiedad)</tt>   
	 * </ul>   
	 *
	 * @param propiedadesAActualizar Mapa con <tt>String nombrePropiedad:Object valor</tt>.
	 *                               Nulo se toma como un mapa vacío.
	 * @exception InvocationTargetException  Excepción originada por el método de acceso original.
	 * @exception PropertiesManagerException  Algún problema inesperado.
	 */
	public void executeSets(Map propiedadesAActualizar)
		throws InvocationTargetException, PropertiesManagerException {
		if (propiedadesAActualizar == null)
			return;
		Iterator ipro = propiedadesAActualizar.entrySet().iterator();
		while (ipro.hasNext()) {
			Map.Entry e = (Map.Entry) ipro.next();
			executeSet((String) e.getKey(), e.getValue());
		}
	}

	/**
	 * Actualiza las propiedades indicadas de un solo golpe intentando
	 * convertir los valores <tt>String</tt> en el tipo adecuado. <p>
	 *
	 * <p>Precondición</b>
	 * <ul>
	 * <li> <tt>this.objeto != null</tt>
	 * <li> <tt>this.tieneSetter(nombrePropiedad)</tt>   
	 * </ul>   
	 *
	 * @param propiedadesAActualizar Mapa con <tt>String nombrePropiedad:Object valor</tt>.
	 *                               Nulo se toma como un mapa vacío.
	 * @exception InvocationTargetException  Excepción originada por el método de acceso original.
	 * @exception PropertiesManagerException  Algún problema inesperado.
	 */
	public void executeSetsFromStrings(Map propiedadesAActualizar)
		throws InvocationTargetException, PropertiesManagerException {
		if (propiedadesAActualizar == null)
			return;
		Iterator ipro = propiedadesAActualizar.entrySet().iterator();
		while (ipro.hasNext()) {
			Map.Entry e = (Map.Entry) ipro.next();
			executeSetFromString((String) e.getKey(), e.getValue());
		}
	}

	/**
	 * Si la propiedad indicada existe.
	 */
	public boolean exists(String nombrePropiedad)
		throws PropertiesManagerException {
		return getPropertyDescriptors().containsKey(nombrePropiedad);
	}
	/**
	 */
	private Class getClase() {
		if (clase == null) {
			if (objeto == null) {
				throw new IllegalStateException(XavaResources.getString("properties_manager_object_required"));
			}
			clase = objeto.getClass();
		}
		return clase;
	}
	/**
	 * Los nombres de todas las propiedades manejadas por <tt>this</tt>. <p>
	 * 
	 * @return Nunca será nulo
	 */
	public String[] getPropertiesNames()
		throws PropertiesManagerException {
		Map pd = getPropertyDescriptors();
		int c = pd.size();
		String[] result = new String[c];
		Iterator it = pd.keySet().iterator();
		for (int i = 0; i < c; i++) {
			result[i] = (String) it.next();
		}
		return result;
	}
	/**
	 * Los nombres de todas las propiedades manejadas por <tt>this</tt> con
	 * método <i>set</i>. <p>
	 * 
	 * @return Nunca será nulo
	 */
	public String[] getWritablesPropertiesNames()
		throws PropertiesManagerException {
		Map pd = getPropertyDescriptors();
		int c = pd.size();
		Collection result = new ArrayList();
		Iterator it = pd.keySet().iterator();
		for (int i = 0; i < c; i++) {
			String nombre = (String) it.next();
			if (hasSetter(nombre)) {
				result.add(nombre);
			}
		}
		String[] rs = new String[result.size()];
		result.toArray(rs);
		return rs;
	}
	/**
	 * Los nombres de todas las propiedades manejadas por <tt>this</tt> con
	 * método <i>get</i>. <p>
	 * 
	 * @return Nunca será nulo
	 */
	public String[] getReadablesPropertiesNames()
		throws PropertiesManagerException {
		Map pd = getPropertyDescriptors();
		int c = pd.size();
		Collection result = new ArrayList();
		Iterator it = pd.keySet().iterator();
		for (int i = 0; i < c; i++) {
			String nombre = (String) it.next();
			if (hasGetter(nombre)) {
				result.add(nombre);
			}
		}
		String[] rs = new String[result.size()];
		result.toArray(rs);
		return rs;
	}
	/**
	 * Objeto del que se manejan las propiedades.
	 */
	public java.lang.Object getObject() {
		return objeto;
	}
	public PropertyDescriptor getPropertyDescriptor(String nombrePropiedad)
		throws PropertiesManagerException {
		PropertyDescriptor rs =
			(PropertyDescriptor) getPropertyDescriptors().get(nombrePropiedad);
		if (rs == null) {
			throw new PropertiesManagerException(
				"Propiedad "
					+ nombrePropiedad
					+ " no encontrada en "
					+ getClase().getName());
		}
		return rs;
	}
	private Map getPropertyDescriptors() throws PropertiesManagerException {
		if (propertyDescriptors == null) {
			try {
				propertyDescriptors = new HashMap();
				BeanInfo info = Introspector.getBeanInfo(getClase());
				PropertyDescriptor[] pds = info.getPropertyDescriptors();
				for (int i = 0; i < pds.length; i++) {
					if (!(pds[i].getName().equals("class")
						|| pds[i].getPropertyType() == null)) {
						propertyDescriptors.put(pds[i].getName(), pds[i]);
					}
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new PropertiesManagerException(XavaResources.getString("properties_manager_properties_error"));
			}
		}
		return propertyDescriptors;
	}
	/**
	 * Objeto del que se manejan las propiades.
	 */
	public void setObject(java.lang.Object newObjeto) {
		if (clase == null || !clase.isInstance(newObjeto)) {
			clase = null;
			propertyDescriptors = null;
		}
		objeto = newObjeto;
	}
	// of String
	// assert(propiedadesAReplicar)
	private Collection stringToArrayString(String propiedadesAReplicar) {
		Collection rs = new Vector();
		StringTokenizer st = new StringTokenizer(propiedadesAReplicar, ":");
		while (st.hasMoreTokens()) {
			rs.add(st.nextToken());
		}
		return rs;
	}
	/**
	 * Si la propiedad indicada tiene <i>getter</i>. <p>
	 * @return boolean
	 * @param nombrePropiedad java.lang.String
	 */
	public boolean hasGetter(String nombrePropiedad)
		throws PropertiesManagerException {
		return getPropertyDescriptor(nombrePropiedad).getReadMethod() != null;
	}
	/**
	 * Si la propiedad indicada tiene <i>setter</i>. <p>
	 * @return boolean
	 * @param nombrePropiedad java.lang.String
	 */
	public boolean hasSetter(String nombrePropiedad)
		throws PropertiesManagerException {
		return getPropertyDescriptor(nombrePropiedad).getWriteMethod() != null;
	}
	
	private Object intentarConvertirEnNumero(Class tipo, Object o)  {
		if (!(o instanceof Number)) return null;
		Number valor = (Number) o;		
		if (tipo.equals(int.class)) {
			return new Integer(valor.intValue());
		}
		else if (tipo.equals(Integer.class)) {
			return new Integer(valor.intValue());
		}
		else if (tipo.equals(long.class)) {
			return new Long(valor.longValue());
		}
		else if (tipo.equals(Long.class)) {
			return new Long(valor.longValue());
		}
		else if (tipo.equals(float.class)) {
			return new Float(valor.floatValue());
		}
		else if (tipo.equals(Float.class)) {
			return new Float(valor.floatValue());
		}
		else if (tipo.equals(double.class)) {
			return new Double(valor.doubleValue());
		}
		else if (tipo.equals(Double.class)) {
			return new Double(valor.doubleValue());
		}
		else if (tipo.equals(short.class)) {
			return new Short(valor.shortValue());
		}
		else if (tipo.equals(Short.class)) {
			return new Short(valor.shortValue());
		}
		else if (tipo.equals(byte.class)) {
			return new Byte(valor.byteValue());
		}
		else if (tipo.equals(Byte.class)) {
			return new Byte(valor.byteValue());
		}
		return null;
	}
	
}
