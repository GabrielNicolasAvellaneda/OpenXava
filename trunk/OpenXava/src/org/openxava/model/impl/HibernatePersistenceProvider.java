package org.openxava.model.impl;

import java.io.*;
import java.util.*;

import javax.ejb.*;
import javax.ejb.ObjectNotFoundException;

import org.hibernate.*;
import org.hibernate.cfg.*;

import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * @author Mª Carmen Gimeno Alabau
 */
public class HibernatePersistenceProvider implements IPersistenceProvider {

	private Session session;
	private Transaction transaction;
	private static SessionFactory sessionFactory;
	
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
			getSession().save(object);
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
		return null;
	}

	public void commit() {
	  transaction.commit();
		session.close();
		transaction = null;
		session = null;
	}

	public void rollback() {
		transaction.rollback();
		session.close();
		transaction = null;
		session = null;
	}

	public void begin() {
		session = getSessionFactory().openSession();
		transaction = session.beginTransaction();	
	}
	
	public static SessionFactory getSessionFactory() throws HibernateException {
		if (sessionFactory == null) {
			sessionFactory = createSessionFactory("/hibernate.cfg.xml");
		}
		return sessionFactory; 
	}
	
	public static SessionFactory createSessionFactory(String hibernateCfg) throws HibernateException {
		try {
			Configuration configuration = new Configuration().configure(hibernateCfg);
			for (Iterator it = MetaModel.getAllGenerated().iterator(); it.hasNext();) {
				MetaModel model = (MetaModel) it.next();
				try {
					configuration.addResource(model.getName() + ".hbm.xml");
				}
				catch (Exception ex) {
					ex.printStackTrace();
					System.err.println(XavaResources.getString("hibernate_mapping_not_loaded_warning", model.getName()));
				}
			}
			return configuration.buildSessionFactory();
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			throw new HibernateException(XavaResources.getString("hibernate_session_factory_creation_error"));
		}
	}
}
