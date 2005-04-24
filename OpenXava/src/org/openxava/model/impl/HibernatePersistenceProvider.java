package org.openxava.model.impl;

import java.io.*;
import java.util.*;

import javax.ejb.*;
import javax.ejb.ObjectNotFoundException;

import org.hibernate.*;

import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * @author Mª Carmen Gimeno Alabau
 */
public class HibernatePersistenceProvider implements IPersistenceProvider {

	private Session session;

	public Object find(IMetaEjb metaModel, Map keyValues) throws FinderException {
		try {
			MetaEjbImpl ejbImpl = new MetaEjbImpl(metaModel);
			Class className = metaModel.getBeanClass();
			Object key = null;
			if (keyValues.size() == 1) {
				key = keyValues.values().iterator().next();
			}
			else {
				throw new RuntimeException("Claves múltiples todavía no soportadas");
				/*
				 * String nombreClaseKey = clase.getName() + "$Key"; Class claseKey =
				 * Class.forName(nombreClaseKey); Object key =
				 * ejbImpl.obtainPrimaryKeyFromKey(keyValues, claseKey, true);
				 */
			}

			Object result = getSession().get(className, (Serializable) key);
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
			throw new RuntimeException( //tmp: ¿HibernateException?
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
			getSession().save(object);
			return object;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new CreateException(XavaResources.getString(
					"create_persistent_error", ex.getMessage()));
		}
	}

	public void remove(MetaModel metaModel, Object model)
			throws RemoveException, XavaException {
		try {
			getSession().delete(model);
		}
		catch (HibernateException ex) {
			ex.printStackTrace();
			throw new RemoveException(XavaResources.getString("remove_error",
					metaModel.getName(), ex.getMessage()));
		}
	}

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Object find(IMetaEjb metaEntidad, Object key) throws FinderException {
		// TODO Auto-generated method stub
		return null;
	}
}
