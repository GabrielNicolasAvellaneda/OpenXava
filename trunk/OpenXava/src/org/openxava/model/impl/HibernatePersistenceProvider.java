package org.openxava.model.impl;

import java.io.*;
import java.util.*;

import javax.ejb.*;

import org.hibernate.*;

import org.openxava.hibernate.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;

/**
 * @author Mª Carmen Gimeno Alabau
 */
public class HibernatePersistenceProvider extends POJOPersistenceProviderBase {
	
	protected Object find(Class pojoClass, Serializable key) {
		return XHibernate.getSession().get(pojoClass, (Serializable) key);
	}

	protected void refresh(Object object) {
		try {
			XHibernate.getSession().refresh(object);
		}
		catch (UnresolvableObjectException ex) {
			// References as key that point to a non-existent object are supported
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
			ex.printStackTrace();
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
