package org.openxava.model.meta;

import java.io.*;
import java.lang.reflect.*;
import java.math.*;
import java.util.*;

import javax.ejb.*;
import javax.rmi.*;

import org.openxava.converters.*;
import org.openxava.ejbx.*;
import org.openxava.mapping.*;
import org.openxava.util.*;


/**
 * 
 * @author Javier Paniza
 */
public class MetaEjbImpl implements Serializable {
	
	private java.lang.String remote;
	private java.lang.String home;
	private java.lang.String jndi;
	private Class remoteClass;
	private Class primaryKeyClass;
	private Class homeClass;
	private EJBHome ejbHome;
	private java.lang.String primaryKey;	
	private IMetaModel metaModel;
	
	
	public MetaEjbImpl(IMetaModel metaModelo) {
		this.metaModel = metaModelo;		
	}
	
	private String getPackageName() throws XavaException {
		return metaModel.getMetaComponent().getPackageName();
	}
	
	private String getModelName() {
		return metaModel.getName();
	}
	
	public Class getRemoteClass() throws XavaException {
		if (remoteClass == null) {
			try {
				remoteClass = Class.forName(getRemote());
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new XavaException(
					"Imposible crear la clase "
						+ getRemote()
						+ " por:\n"
						+ ex.getLocalizedMessage());
			}
		}
		return remoteClass;
	}
	/**
	 * 
	 * @return java.lang.String
	 * @throws XavaException
	 */
	public java.lang.String getHome() throws XavaException {
		if (Is.emptyString(home)) {
			home = getPackageName() + "." + getModelName() + "Home";			
		}
		return home;
	}
	/**
	 * 
	 * @return java.lang.String
	 * @throws XavaException
	 */
	public java.lang.String getJndi() throws XavaException {
		if (Is.emptyString(jndi)) {
			jndi = "ejb/" + getPackageName() + "/" + getModelName();			
		}
		return jndi;
	}
	
	/**
	 * 
	 * @return java.lang.String
	 * @throws XavaException
	 */
	public java.lang.String getPrimaryKey() throws XavaException {
		if (Is.emptyString(primaryKey)) {
			primaryKey = getPackageName() + "." + getModelName() + "Key";
		}
		return primaryKey;
	}
	/**
	 * 
	 * @param newPrimaryKey java.lang.String
	 */
	public void setPrimaryKey(java.lang.String newPrimaryKey) {
		primaryKey = newPrimaryKey;
	}
	
	
	/**
	 * 
	 * @return java.lang.String
	 * @throws XavaException
	 */
	public java.lang.String getRemote() throws XavaException {
		if (Is.emptyString(remote)) {
			remote = getPackageName() + "." + getModelName();
		}
		return remote;
	}	
	
	/**
	 * 
	 * @param newRemote java.lang.String
	 */
	public void setRemote(java.lang.String newRemote) {
		remote = newRemote;
	}
	
	
	/**
	 * 
	 * @param newHome java.lang.String
	 */
	public void setHome(java.lang.String newHome) {
		home = newHome;
	}
	/**
	 * 
	 * @param newJndi java.lang.String
	 */
	public void setJndi(java.lang.String newJndi) {
		jndi = newJndi;
	}
	
	
	public Class getHomeClass() throws XavaException {
		if (homeClass == null) {
			try {
				homeClass = Class.forName(getHome());
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new XavaException(
					"Imposible crear la clase " + getHome() + " por:\n" + ex.getLocalizedMessage());
			}
		}
		return homeClass;
	}
	public Class getPrimaryKeyClass() throws XavaException {
		if (primaryKeyClass == null) {
			try {
				primaryKeyClass = Class.forName(getPrimaryKey());
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new XavaException(
					"Imposible crear la clase "
						+ getPrimaryKey()
						+ " por:\n"
						+ ex.getLocalizedMessage());
			}
		}
		return primaryKeyClass;
	}
	
	/**
	 * Para usarse desde dentro de un EJB. <p>
	 *
	 * No se debería usar desde el cliente ya que lanza
	 * una <tt>EJBException</tt> si falla.<br>
	 *
	 * @exception EJBException Si hay algún problema
	 * @return Home moldado
	 * @throws XavaException
	 */
	public EJBHome obtainHome() throws XavaException {		
		if (ejbHome == null) { 
			try {
				IContext ctx = BeansContext.get();				
				Object o = ctx.lookup(getJndi());
				ejbHome = (EJBHome) PortableRemoteObject.narrow(o, getHomeClass());
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(
					"Imposible obtener y moldear home vinculado a " + getJndi());
			}
		}
		return ejbHome;
	}

	public Object obtainPrimaryKeyFromAllValues(Map valores)
		throws XavaException {
		Map valoresClave = metaModel.extractKeyValues(valores);
		return obtainPrimaryKeyFromKey(valoresClave);
	}
	
	public Object obtainPrimaryKeyFromKey(Map valoresClave) throws XavaException {
		return obtainPrimaryKeyFromKey(valoresClave, getPrimaryKeyClass(), true);
	}
	
	public Object obtainPrimaryKeyAFromKeyWithoutConversors(Map valoresClave) throws XavaException {
		return obtainPrimaryKeyFromKey(valoresClave, getPrimaryKeyClass(), false);
	}
		
	public Object obtainPrimaryKeyFromKey(Map valoresClave, Class clasePK, boolean usarConversores)
		throws XavaException {
		try {			
			if (valoresClave == null || valoresClave.isEmpty()) return null;			
			Object key = clasePK.newInstance();
			Field[] fields = clasePK.getFields();
			cambiarPuntoPorSubrayado(valoresClave);			
			valoresClave = getAplanado("", valoresClave);			
			for (int i = 0; i < fields.length; i++) {
				Field f = fields[i];
				String nombre = f.getName();					
				if (nombre.startsWith("_")) { // usa conversor
					nombre = Strings.firstLower(nombre.substring(1));
				}				
				try {
					Object value = valoresClave.get(nombre);					
					if (value == null && !valoresClave.containsKey(nombre)) {
						int idx = nombre.indexOf("_");
						if (idx >= 0) {
							String referencia = nombre.substring(0, idx);
							String miembro = nombre.substring(idx + 1);
							Map valoresReferencia = (Map) valoresClave.get(referencia);
							if (valoresReferencia != null) {
								value = valoresReferencia.get(miembro);
							}
						}
					}
					try {						 
						if (!(f.getType().isPrimitive() && value == null)) {
							if (usarConversores && getMapeo().hasConverter(nombre)) { 
								asignarUsandoConversor(key, nombre, f, value);
							}
							else {
								f.set(key, value);
							}													
						}						
					}
					catch (IllegalArgumentException ex) {
						// por si fuera un problema de molde desde BigDecimal
						if (value instanceof BigDecimal) {							
							asignarDesdeBigDecimal(key, f, (BigDecimal) value);
						}
						// por si fuera un problema de molde desde java.util.Date
						else if (value instanceof java.util.Date) {
							asignarDesdeUtilDate(key, f, (java.util.Date) value);
						}
						else {
							String valueType = value == null?"null":value.getClass().getName();
							throw new IllegalArgumentException(XavaResources.getString("assign_type_mismatch", f.getName(), key.getClass(), f.getType(), valueType));
						}
					}
				} catch (Exception ex) {					
					if (!valoresClave.containsKey(nombre)) {
						throw new IllegalArgumentException(
							"Es necesario que el mapa con valores de clave tenga una propiedad "
								+ nombre);
					} else {
						throw ex;
					}
				}
			}
			return key;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException(
				"Imposible obtener la clave primaria de "
					+ getModelName()
					+ " por:\n"
					+ ex.getLocalizedMessage());
		}
	}
	
	

	private Map getAplanado(String prefijo, Map valoresClave) {
		Map plano = new HashMap();
		Iterator it = valoresClave.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			if (e.getValue() instanceof Map) {				
				plano.putAll(getAplanado(prefijo + e.getKey() + "_", (Map) e.getValue()));
			}
			else {
				plano.put(prefijo + e.getKey(), e.getValue());
			}
		}
		return plano;
	}

	private void cambiarPuntoPorSubrayado(Map valores) {
		Iterator it = valores.keySet().iterator();
		Collection aBorrar = null;
		Map aAñadir = null;
		while (it.hasNext()) {
			String nombre = (String) it.next();
			if (nombre.indexOf('.') >= 0) {
				if (aBorrar == null) aBorrar = new ArrayList();
				aBorrar.add(nombre);
				String nuevoNombre = Strings.change(nombre, ".", "_");
				Object valor = valores.get(nombre);
				if (aAñadir == null) aAñadir = new HashMap();				
				aAñadir.put(nuevoNombre, valor);
			}
		}		
		if (aBorrar != null) {
			Iterator itBorrar = aBorrar.iterator();
			while (itBorrar.hasNext()) {
				valores.remove(itBorrar.next());
			}
			valores.putAll(aAñadir);
		}
		
	}

	private void asignarDesdeBigDecimal(Object o, Field f, BigDecimal valor) throws XavaException {
		try {
			Class tipo = f.getType();
			if (tipo.equals(int.class)) {
				f.setInt(o, valor.intValue());
			}
			else if (tipo.equals(Integer.class)) {
				f.set(o, new Integer(valor.intValue()));
			}
			else if (tipo.equals(long.class)) {
				f.setLong(o, valor.longValue());
			}
			else if (tipo.equals(Long.class)) {
				f.set(o, new Long(valor.longValue()));
			}
			else if (tipo.equals(float.class)) {
				f.setFloat(o, valor.floatValue());
			}
			else if (tipo.equals(Float.class)) {
				f.set(o, new Float(valor.floatValue()));
			}
			else if (tipo.equals(double.class)) {
				f.setDouble(o, valor.doubleValue());
			}
			else if (tipo.equals(Double.class)) {
				f.set(o, new Double(valor.doubleValue()));
			}
			else if (tipo.equals(short.class)) {
				f.setShort(o, valor.shortValue());
			}
			else if (tipo.equals(Short.class)) {
				f.set(o, new Short(valor.shortValue()));
			}
			else if (tipo.equals(byte.class)) {
				f.setByte(o, valor.byteValue());
			}
			else if (tipo.equals(Byte.class)) {
				f.set(o, new Byte(valor.byteValue()));
			}
			else {						
				throw new XavaException("bigdecimal_to_no_number_error");
			}
		}
		catch (Exception ex) {
			throw new XavaException("bigdecimal_to_member_error", f.getName(), o.getClass().getName(), ex.getLocalizedMessage());
		}

	}
	
	private void asignarDesdeUtilDate(Object o, Field f, java.util.Date valor) throws XavaException {
		try {			
			Class tipo = f.getType();
			if (tipo.equals(java.sql.Date.class)) {				
				f.set(o, new java.sql.Date(valor.getTime()));
			}
			else {						
				throw new XavaException("utildate_not_compatible");
			}
		}
		catch (Exception ex) {
			throw new XavaException("utildate_to_member_error", f.getName(), o.getClass().getName(), ex.getLocalizedMessage());
		}

	}
	
	private void asignarUsandoConversor(Object o, String nombrePropiedad, Field f, Object valor) throws XavaException {		
		try {			
			MetaProperty pr = metaModel.getMetaProperty(nombrePropiedad);			
			if (pr.hasValidValues() && valor instanceof String) {
				valor = new Integer(pr.getValidValueIndex(valor));
			}
			IConverter conversor = getMapeo().getConverter(nombrePropiedad);			
			f.set(o, conversor.toDB(valor));
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("value_to_member_error", nombrePropiedad,  o.getClass().getName(), ex.getLocalizedMessage());
		}

	}
	
	private ModelMapping getMapeo() throws XavaException {
		return metaModel.getMapping();	
	}
	
}

