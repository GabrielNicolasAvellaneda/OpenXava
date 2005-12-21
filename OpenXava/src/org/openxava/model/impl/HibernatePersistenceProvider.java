package org.openxava.model.impl;

import java.io.*;
import java.util.*;

import javax.ejb.*;
import javax.ejb.ObjectNotFoundException;

import org.hibernate.*;
import org.hibernate.cfg.*;

import org.openxava.hibernate.*;
import org.openxava.mapping.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * tmp: MOover a paquete 'hibernate', o refactorizar algo de funcionalidad
 * tmp: quitar todas las referencias e ejb
 * @author Mª Carmen Gimeno Alabau
 */
public class HibernatePersistenceProvider implements IPersistenceProvider {

	private static SessionFactory sessionFactory;
	private static Map sessions = new HashMap();
	
	private Session session;
	private Transaction transaction;
		
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
			Object result = getSession().get(modelClass, (Serializable) key);
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
		sessions.remove(Thread.currentThread());
		transaction.commit();
		session.close();
		transaction = null;
		session = null;
	}

	public void rollback() {
		sessions.remove(Thread.currentThread());
		if (transaction != null) transaction.rollback();
		if (session != null) session.close();
		transaction = null;		
		session = null;
	}

	public void begin() {
		session = getSessionFactory().openSession();
		transaction = session.beginTransaction();
		sessions.put(Thread.currentThread(), session);
	}
	
	public static Session getCurrentSession() {
		return (Session) sessions.get(Thread.currentThread());
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
			if (ReferenceMappingDetail.someMappingUsesConverters()) {
				// toJava conversion is not enabled because in references it's useless thus we avoid an unnecessary overload 
				configuration.getSessionEventListenerConfig().setPreInsertEventListener(new ReferenceConverterToDBListener());
			}
			return configuration.buildSessionFactory();
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			throw new HibernateException(XavaResources.getString("hibernate_session_factory_creation_error"));
		}
	}
}
