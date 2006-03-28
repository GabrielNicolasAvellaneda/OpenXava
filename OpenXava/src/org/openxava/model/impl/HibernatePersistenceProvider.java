package org.openxava.model.impl;

import java.io.*;
import java.lang.reflect.*;
import java.rmi.*;
import java.util.*;

import javax.ejb.*;
import javax.ejb.ObjectNotFoundException;

import org.hibernate.*;

import org.openxava.hibernate.*;
import org.openxava.hibernate.impl.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * tmp: Si no tiene estado ¿crear solo una vez? Comprobar transacciones y sesión
 * tmp: quitar todas las referencias e ejb (incluido IMetaEjb)
 * @author Mª Carmen Gimeno Alabau
 */
public class HibernatePersistenceProvider implements IPersistenceProvider {

	public Object find(IMetaEjb metaModel, Map keyValues) throws FinderException {
		try {						
			Object key = null;						
			if (metaModel.getAllKeyPropertiesNames().size() == 1) {
				key = keyValues.get(metaModel.getKeyPropertiesNames().iterator().next());
			}
			else {
				key = getKey(metaModel, keyValues);
				refreshKeyReference(metaModel, key);
			}			
			if (key == null) {
				throw new ObjectNotFoundException(XavaResources.getString(
						"object_with_key_not_found", metaModel.getName(), keyValues));
			}						
			Object result = XHibernate.getSession().get(metaModel.getPOJOClass(), (Serializable) key);			
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
			throw new HibernateException( 
					XavaResources.getString("find_error", metaModel.getName()));
		}
	}
	
	public Object find(IMetaEjb metaModel, Object key) throws FinderException { 
		try {														
			Object result = XHibernate.getSession().get(metaModel.getPOJOClass(), (Serializable) key);			
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
			throw new HibernateException( 
					XavaResources.getString("find_error", metaModel.getName()));
		}
	}
	
	private void refreshKeyReference(IMetaEjb metaModel, Object key) throws XavaException, HibernateException, InvocationTargetException, PropertiesManagerException {
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
				try {
					XHibernate.getSession().refresh(referencedObject);
				}
				catch (UnresolvableObjectException ex) {
					// References as key that point to a non-existent object are supported
				}
			}
		}
	}

	public IPropertiesContainer toPropertiesContainer(MetaModel metaModel,
			Object o) throws XavaException {
		return new POJOPropertiesContainerAdapter(o);
	}

	public Object create(IMetaEjb metaEjb, Map values)
			throws CreateException, ValidationException, XavaException {		
		try {			
			find(metaEjb, values);			
			throw new DuplicateKeyException(XavaResources.getString("no_create_exists", metaEjb.getName())); 
		}
		catch (DuplicateKeyException ex) {
			throw ex;
		}
		catch (Exception ex) {						
			// If it does not exist then continue
		}
		Serializable object = null;
		try {
			object = (Serializable) metaEjb.getPOJOClass().newInstance();
			PropertiesManager mp = new PropertiesManager(object);
			mp.executeSets(values);					
			XHibernate.getSession().save(object);			
			return object;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new CreateException(XavaResources.getString(
					"create_persistent_error", metaEjb.getName(), ex.getMessage()));
		}
	}

	public void remove(MetaModel metaModel, Object model)
			throws RemoveException, XavaException {
		try {
			XHibernate.getSession().delete(model);
		}
		catch (HibernateException ex) {
			ex.printStackTrace();
			throw new RemoveException(XavaResources.getString("remove_error",
					metaModel.getName(), ex.getMessage()));
		}
	}

		
	public void commit() {
		flush(); 
		XHibernate.commit();
	}

	public void rollback() {	
		XHibernate.rollback();
	}

	public Object getKey(IMetaEjb metaModel, Map keyValues) throws XavaException {
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

	public Object createAggregate(IMetaEjb metaEjb, Map values, MetaModel metaModelContainer, Object containerModel, int number) throws CreateException, ValidationException, RemoteException, XavaException {		
		String container = Strings.firstLower(metaModelContainer.getName());
		values.put(container, containerModel);
		DefaultValueIdentifierGenerator.setCurrentContainerKey(containerModel);
		DefaultValueIdentifierGenerator.setCurrentCounter(number);
		return create(metaEjb, values);
	}

	public void flush() {
		XHibernate.getSession().flush();		
	}

}
