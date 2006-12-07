package org.openxava.model.impl;

import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import javax.ejb.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.*;

import org.openxava.hibernate.*;
import org.openxava.hibernate.impl.*;
import org.openxava.model.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;

/**
 * @author Mª Carmen Gimeno Alabau
 */
public class HibernatePersistenceProvider extends POJOPersistenceProviderBase {
	
	private static Log log = LogFactory.getLog(HibernatePersistenceProvider.class);
	
	protected Object find(Class pojoClass, Serializable key) {
		Object result = XHibernate.getSession().get(pojoClass, (Serializable) key);
		if (key instanceof IModel && result != null) { // if it has multiple key
			// Sometime Hibernate (at least until Hibernate 3.2.1) does not get 
			// the object well if the key is multiple. It's needed to refresh
			refresh(result);  			
		}
		return result;
	}

	protected void refresh(Object object) {
		CalculatorsListener.setOffForCurrentThread(); // In order to reduce postload-calculator calling frequency
		try {
			XHibernate.getSession().refresh(object);			
		}
		catch (UnresolvableObjectException ex) {
			// References as key that point to a non-existent object are supported
		}
		finally { 
			CalculatorsListener.setOnForCurrentThread();
		}
	}
	
	protected void persist(Object object) {
		XHibernate.getSession().save(object);		
	}
	
	public void remove(MetaModel metaModel, Object model)
			throws RemoveException, XavaException {
		try {
			XHibernate.getSession().delete(model);
		}
		catch (HibernateException ex) {
			log.error(ex.getMessage(), ex);
			throw new RemoveException(XavaResources.getString("remove_error",
					metaModel.getName(), ex.getMessage()));
		}
	}

	public void begin() {
		XHibernate.setCmt(XavaPreferences.getInstance().isMapFacadeAsEJB()); 
	}
		
	public void commit() {
		flush(); 
		XHibernate.commit();
		XHibernate.setCmt(false); 				
	}

	public void rollback() {	
		XHibernate.rollback();
		XHibernate.setCmt(false); 
	}
	
	public void reassociate(Object entity) {
		XHibernate.getSession().lock(entity, LockMode.NONE);  		
	}
	
	public void flush() {
		XHibernate.getSession().flush();		
	}

	protected Object createQuery(String query) { 
		return XHibernate.getSession().createQuery(query);
	}

	protected void setParameterToQuery(Object query, String name, Object value) {
		((Query) query).setParameter(name, value);
	}

	protected Object getUniqueResult(Object query) {
		Iterator it = ((Query) query).list().iterator();
		if (!it.hasNext()) return null;
		return it.next();
	}

}
