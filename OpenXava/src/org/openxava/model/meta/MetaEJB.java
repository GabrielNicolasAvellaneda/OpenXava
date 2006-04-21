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
public class MetaEJB implements Serializable {
	
	private java.lang.String remote;
	private java.lang.String home;
	private java.lang.String jndi;
	private Class remoteClass;
	private Class primaryKeyClass;
	private Class homeClass;
	private EJBHome ejbHome;
	private java.lang.String primaryKey;	
	private MetaModel metaModel;
		
	public void setMetaModel(MetaModel metaModel) {
		this.metaModel = metaModel;
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
			} 
			catch (Exception ex) {
				ex.printStackTrace();
				throw new XavaException("create_class_error", getRemote());
			}
		}
		return remoteClass;
	}
	public java.lang.String getHome() throws XavaException {
		if (Is.emptyString(home)) {
			home = getPackageName() + "." + getModelName() + "Home";			
		}
		return home;
	}
	public java.lang.String getJndi() throws XavaException {
		if (Is.emptyString(jndi)) {
			jndi = "ejb/" + getPackageName() + "/" + getModelName();			
		}
		return jndi;
	}
	
	public java.lang.String getPrimaryKey() throws XavaException {
		if (Is.emptyString(primaryKey)) {
			primaryKey = getPackageName() + "." + getModelName() + "Key";
		}
		return primaryKey;
	}
	public void setPrimaryKey(java.lang.String newPrimaryKey) {
		primaryKey = newPrimaryKey;
	}
	
	public boolean isPrimaryKeyClassAvailable() { 
		try {
			Class.forName(getPrimaryKey());
			return true;
		} 
		catch (Exception ex) {
			return false;
		}
	}
	
	public java.lang.String getRemote() throws XavaException {
		if (Is.emptyString(remote)) {
			remote = getPackageName() + "." + getModelName() + "Remote";
		}
		return remote;
	}	
	
	public void setRemote(java.lang.String newRemote) {
		remote = newRemote;
	}
	
	public void setHome(java.lang.String newHome) {
		home = newHome;
	}
	
	public void setJndi(java.lang.String newJndi) {
		jndi = newJndi;
	}	
	
	public Class getHomeClass() throws XavaException {
		if (homeClass == null) {
			try {
				homeClass = Class.forName(getHome());
			} 
			catch (Exception ex) {
				ex.printStackTrace();
				throw new XavaException("create_class_error", getHome());
			}
		}
		return homeClass;
	}
	
	public Class getPrimaryKeyClass() throws XavaException {
		if (primaryKeyClass == null) {
			try {
				primaryKeyClass = Class.forName(getPrimaryKey());
			} 
			catch (Exception ex) {
				ex.printStackTrace();
				throw new XavaException("create_class_error", getPrimaryKey());
			}
		}
		return primaryKeyClass;
	}
	
	public EJBHome obtainHome() throws XavaException {		
		if (ejbHome == null) { 
			try {
				IContext ctx = BeansContext.get();				
				Object o = ctx.lookup(getJndi());
				ejbHome = (EJBHome) PortableRemoteObject.narrow(o, getHomeClass());
			} 
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(
						XavaResources.getString("ejbhome_error") + ". JNDI: " + getJndi());
			}
		}
		return ejbHome;
	}
	
	public Map obtainMapFromPrimaryKey(Object primaryKey) throws XavaException {
		try {
			Map result = new HashMap();
			PropertiesManager pk = new PropertiesManager(primaryKey); 
			for (Iterator it = metaModel.getAllMetaPropertiesKey().iterator(); it.hasNext();) {
				MetaProperty property = (MetaProperty) it.next(); 
				String propertyName = property.getName();
				String propertyNameInKey = property.getMapping().hasConverter()?"_" + Strings.firstUpper(propertyName):propertyName;
				Object value = pk.executeGet(propertyNameInKey);
				result.put(propertyName, value);
			}		
			return result;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("primary_key_to_map_error", (primaryKey==null?null:primaryKey.getClass()));
		}
	}
	
	public Object obtainPrimaryKeyFromAllValues(Map values)
		throws XavaException {
		Map keyValues = metaModel.extractKeyValues(values);
		return obtainPrimaryKeyFromKey(keyValues);
	}
	
	public Object obtainPrimaryKeyFromKey(Map keyValues) throws XavaException {
		return obtainPrimaryKeyFromKey(keyValues, getPrimaryKeyClass(), true);
	}
	
	public Object obtainPrimaryKeyAFromKeyWithoutConversors(Map keyValues) throws XavaException {
		return obtainPrimaryKeyFromKey(keyValues, getPrimaryKeyClass(), false);
	}
		
	public Object obtainPrimaryKeyFromKey(Map keyValues, Class pkClass, boolean useConverters)
		throws XavaException {
		try {			
			if (keyValues == null || keyValues.isEmpty()) return null;			
			Object key = pkClass.newInstance();
			Field[] fields = pkClass.getFields();
			changeDotByUnderline(keyValues);			
			keyValues = getFlatten("", keyValues);			
			for (int i = 0; i < fields.length; i++) {
				Field f = fields[i];
				String name = f.getName();					
				if (name.startsWith("_")) { // uses converter
					name = Strings.firstLower(name.substring(1));
				}				
				try {
					Object value = keyValues.get(name);					
					if (value == null && !keyValues.containsKey(name)) {
						int idx = name.indexOf("_");
						if (idx >= 0) {
							String reference = name.substring(0, idx);
							String member = name.substring(idx + 1);
							Map referenceValues = (Map) keyValues.get(reference);
							if (referenceValues != null) {
								value = referenceValues.get(member);
							}
						}
					}
					try {						 
						if (!(f.getType().isPrimitive() && value == null)) {
							if (useConverters && getMapping().hasConverter(name)) { 
								assignUsingConverter(key, name, f, value);
							}
							else {
								f.set(key, value);
							}													
						}						
					}
					catch (IllegalArgumentException ex) {
						// In case of a cast problem from BigDecimal
						if (value instanceof BigDecimal) {							
							assignFromBigDecimal(key, f, (BigDecimal) value);
						}
						// In case of a cast problem from java.util.Date
						else if (value instanceof java.util.Date) {
							assignFromUtilDate(key, f, (java.util.Date) value);
						}
						else if (value instanceof String) {
							if (!assignFromValidValues(key, name, f, (String) value)) {
								String valueType = value == null?"null":value.getClass().getName();
								throw new IllegalArgumentException(XavaResources.getString("assign_type_mismatch", f.getName(), key.getClass(), f.getType(), valueType));																
							}
						}
						else {
							String valueType = value == null?"null":value.getClass().getName();
							throw new IllegalArgumentException(XavaResources.getString("assign_type_mismatch", f.getName(), key.getClass(), f.getType(), valueType));
						}
					}
				} 
				catch (Exception ex) {					
					if (!keyValues.containsKey(name)) {
						throw new IllegalArgumentException(
							XavaResources.getString("property_in_key_map_required", name));	
					} else {
						throw ex;
					}
				}
			}
			return key;
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("primary_key_error", getModelName());
		}
	}
	
	
	private Map getFlatten(String prefix, Map keyValues) {
		Map flatten = new HashMap();
		Iterator it = keyValues.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			if (e.getValue() instanceof Map) {				
				flatten.putAll(getFlatten(prefix + e.getKey() + "_", (Map) e.getValue()));
			}
			else {
				flatten.put(prefix + e.getKey(), e.getValue());
			}
		}
		return flatten;
	}

	private void changeDotByUnderline(Map values) {
		Iterator it = values.keySet().iterator();
		Collection toRemove = null;
		Map toAdd = null;
		while (it.hasNext()) {
			String name = (String) it.next();
			if (name.indexOf('.') >= 0) {
				if (toRemove == null) toRemove = new ArrayList();
				toRemove.add(name);
				String newName = Strings.change(name, ".", "_");
				Object value = values.get(name);
				if (toAdd == null) toAdd = new HashMap();				
				toAdd.put(newName, value);
			}
		}		
		if (toRemove != null) {
			Iterator itRemove = toRemove.iterator();
			while (itRemove.hasNext()) {
				values.remove(itRemove.next());
			}
			values.putAll(toAdd);
		}
		
	}

	private void assignFromBigDecimal(Object o, Field f, BigDecimal value) throws XavaException {
		try {
			Class type = f.getType();
			if (type.equals(int.class)) {
				f.setInt(o, value.intValue());
			}
			else if (type.equals(Integer.class)) {
				f.set(o, new Integer(value.intValue()));
			}
			else if (type.equals(long.class)) {
				f.setLong(o, value.longValue());
			}
			else if (type.equals(Long.class)) {
				f.set(o, new Long(value.longValue()));
			}
			else if (type.equals(float.class)) {
				f.setFloat(o, value.floatValue());
			}
			else if (type.equals(Float.class)) {
				f.set(o, new Float(value.floatValue()));
			}
			else if (type.equals(double.class)) {
				f.setDouble(o, value.doubleValue());
			}
			else if (type.equals(Double.class)) {
				f.set(o, new Double(value.doubleValue()));
			}
			else if (type.equals(short.class)) {
				f.setShort(o, value.shortValue());
			}
			else if (type.equals(Short.class)) {
				f.set(o, new Short(value.shortValue()));
			}
			else if (type.equals(byte.class)) {
				f.setByte(o, value.byteValue());
			}
			else if (type.equals(Byte.class)) {
				f.set(o, new Byte(value.byteValue()));
			}
			else {						
				throw new XavaException("bigdecimal_to_no_number_error");
			}
		}
		catch (Exception ex) {
			throw new XavaException("bigdecimal_to_member_error", f.getName(), o.getClass().getName(), ex.getLocalizedMessage());
		}

	}
	
	private void assignFromUtilDate(Object o, Field f, java.util.Date value) throws XavaException {
		try {			
			Class tipo = f.getType();
			if (tipo.equals(java.sql.Date.class)) {				
				f.set(o, new java.sql.Date(value.getTime()));
			}
			else {						
				throw new XavaException("utildate_not_compatible");
			}
		}
		catch (Exception ex) {
			throw new XavaException("utildate_to_member_error", f.getName(), o.getClass().getName(), ex.getLocalizedMessage());
		}

	}
	
	private void assignUsingConverter(Object o, String propertyName, Field f, Object value) throws XavaException {		
		try {			
			MetaProperty pr = metaModel.getMetaProperty(propertyName);			
			if (pr.hasValidValues() && value instanceof String) {
				value = new Integer(pr.getValidValueIndex(value));
			}
			IConverter converter = getMapping().getConverter(propertyName);
			value = converter.toDB(value);
			f.set(o, value);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("value_to_member_error", propertyName,  o.getClass().getName(), ex.getLocalizedMessage());
		}

	}
		
	private boolean assignFromValidValues(Object key, String propertyName, Field f, String value) throws ElementNotFoundException, XavaException, IllegalArgumentException, IllegalAccessException {
		MetaProperty pr = metaModel.getMetaProperty(propertyName);			
		if (pr.hasValidValues()) {
			f.set(key, new Integer(pr.getValidValueIndex(value)));
			return true;
		}
		return false;
	}
	
	
	private ModelMapping getMapping() throws XavaException {
		return metaModel.getMapping();	
	}
	
}
