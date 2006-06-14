package org.openxava.model.impl;

import java.io.*;
import java.lang.reflect.*;
import java.rmi.*;
import java.util.*;

import javax.ejb.*;
import javax.ejb.ObjectNotFoundException;

import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * Abstract class for create <i>Persistence Providers</i> based in POJOs. <p>
 * 
 * @author Javier Paniza
 */
abstract public class BasePOJOPersistenceProvider implements IPersistenceProvider {
	
	/**
	 * Return the object associated to the sent key.
	 */
	abstract protected Object find(Class pojoClass, Serializable key);
	
	/**
	 * Refresh the object. If object does not exist, then NO exception is thrown.
	 */
	abstract protected void refresh(Object object);
	
	/**
	 * Marks the object as persistent. <p> 
	 */
	abstract protected void persist(Object object);

	public Object find(MetaModel metaModel, Map keyValues) throws FinderException {
		try {						
			Object key = null;		
			boolean multipleKey = metaModel.getAllKeyPropertiesNames().size() > 1;  
			if (!multipleKey) {
				String keyPropertyName = (String) metaModel.getKeyPropertiesNames().iterator().next();
				key = keyValues.get(keyPropertyName);
				if (key instanceof Number) { // Numbers can produce conversion problems. For example, NUMERIC to java.lang.Integer
					key = convertSingleKeyType(metaModel, keyPropertyName, key);
				}
			}
			else {
				key = getKey(metaModel, keyValues);
				refreshKeyReference(metaModel, key); 
			}			
			if (key == null) {
				throw new ObjectNotFoundException(XavaResources.getString(
						"object_with_key_not_found", metaModel.getName(), keyValues));
			}	
			Object result = find(metaModel.getPOJOClass(), (Serializable) key);			
			if (result == null) {
				throw new ObjectNotFoundException(XavaResources.getString(
						"object_with_key_not_found", metaModel.getName(), keyValues));
			}
			return result;
		}
		catch (FinderException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new PersistenceProviderException( 
					XavaResources.getString("find_error", metaModel.getName()));
		}
	}
	
	public Object find(MetaModel metaModel, Object key) throws FinderException { 
		try {														
			Object result = find(metaModel.getPOJOClass(), (Serializable) key);			
			if (result == null) {
				throw new ObjectNotFoundException(XavaResources.getString(
						"object_with_key_not_found", metaModel.getName(), key));
			}			
			return result;
		}
		catch (FinderException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new PersistenceProviderException( 
					XavaResources.getString("find_error", metaModel.getName()));
		}
	}
	
	private void refreshKeyReference(MetaModel metaModel, Object key) throws XavaException, InvocationTargetException, PropertiesManagerException { 
		// Refresh references keys can be a little inefficient (a SELECT by reference)
		// but it's needed in order to populate these references well, 
		// because these reference already have values in its keys thus 
		// they are not loaded automatically from database
		Collection references = metaModel.getMetaReferencesKey();
		if (references.isEmpty()) return;
		PropertiesManager pm = new PropertiesManager(key);
		for (Iterator it=metaModel.getMetaReferencesKey().iterator(); it.hasNext();) {
			MetaReference ref = (MetaReference) it.next();
			Object referencedObject = pm.executeGet(ref.getName());
			if (referencedObject != null) {
				refresh(referencedObject);
			}
		}
	}

	public IPropertiesContainer toPropertiesContainer(MetaModel metaModel,
			Object o) throws XavaException {
		return new POJOPropertiesContainerAdapter(o);
	}

	public Object create(MetaModel metaModel, Map values)
			throws CreateException, ValidationException, XavaException {		
		try {			
			find(metaModel, values);			
			throw new DuplicateKeyException(XavaResources.getString("no_create_exists", metaModel.getName())); 
		}
		catch (DuplicateKeyException ex) {
			throw ex;
		}
		catch (Exception ex) {						
			// If it does not exist then continue
		}
		Serializable object = null;
		try {
			object = (Serializable) metaModel.getPOJOClass().newInstance();
			PropertiesManager mp = new PropertiesManager(object);
			mp.executeSets(values);					
			persist(object);			
			return object;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new CreateException(XavaResources.getString(
					"create_persistent_error", metaModel.getName(), ex.getMessage()));
		}
	}

			
	public Object getKey(MetaModel metaModel, Map keyValues) throws XavaException {
		try {
			Class modelClass = metaModel.getPOJOClass();
			Object key = modelClass.newInstance();
			PropertiesManager pm = new PropertiesManager(key);
			pm.executeSets(Maps.plainToTree(keyValues));
			return key;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("key_for_pojo_error");
		}
	}
	
	private Object convertSingleKeyType(MetaModel metaModel, String property, Object value) {
		try {
			Class modelClass = metaModel.getPOJOClass();
			Object key = modelClass.newInstance();
			PropertiesManager pm = new PropertiesManager(key);
			pm.executeSet(property, value);
			return pm.executeGet(property);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(XavaResources.getString("key_type_conversion_warning", property, value, metaModel.getName()));
			return value;			
		}
	}

	public Object createAggregate(MetaModel metaModel, Map values, MetaModel metaModelContainer, Object containerModel, int number) throws CreateException, ValidationException, RemoteException, XavaException {		
		String container = Strings.firstLower(metaModelContainer.getName());
		values.put(container, containerModel);
		// The next two lines use Hibernate. At the momment for Hibernate and EJB3 
		// In order to support a EJB3 no hibernate implementations we will need to change them
		org.openxava.hibernate.impl.DefaultValueIdentifierGenerator.setCurrentContainerKey(containerModel);
		org.openxava.hibernate.impl.DefaultValueIdentifierGenerator.setCurrentCounter(number);
		return create(metaModel, values);
	}

}
