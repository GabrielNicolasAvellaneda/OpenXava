package org.openxava.model.impl;

import java.io.*;
import java.util.*;

import javax.ejb.*;
import javax.ejb.ObjectNotFoundException;

import org.hibernate.*;

import org.openxava.hibernate.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * tmp: Si no tiene estado ¿crear solo una vez? Comprobar transacciones y sesión
 * tmp: quitar todas las referencias e ejb
 * @author Mª Carmen Gimeno Alabau
 */
public class HibernatePersistenceProvider implements IPersistenceProvider {

	public Object find(IMetaEjb metaModel, Map keyValues) throws FinderException {
		try {			
			Class modelClass = metaModel.getPOJOClass();
			Object key = null;
						
			if (metaModel.getAllKeyPropertiesNames().size() == 1) {
				key = keyValues.get(metaModel.getKeyPropertiesNames().iterator().next());
			}
			else {
				key = modelClass.newInstance();
				PropertiesManager pm = new PropertiesManager(key);
				pm.executeSets(keyValues);
			}		
			Object result = XHibernate.getSession().get(modelClass, (Serializable) key);
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

	public IPropertiesContainer toPropertiesContainer(MetaModel metaModel,
			Object o) throws XavaException {
		return new POJOPropertiesContainerAdapter(o);
	}

	public Object create(IMetaEjb metaEjb, Map values)
			throws CreateException, ValidationException, XavaException {
		Object object = null;
		try {
			object = metaEjb.getBeanClass().newInstance();
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

	public Object find(IMetaEjb metaEntidad, Object key) throws FinderException {
		return null;
	}

	public void commit() {
		XHibernate.commit();
	}

	public void rollback() {	
		XHibernate.rollback();
	}

}
