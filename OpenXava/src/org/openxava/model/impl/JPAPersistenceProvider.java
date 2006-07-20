package org.openxava.model.impl;

import java.io.*;
import java.util.*;

import javax.ejb.*;
import javax.persistence.*;

import org.hibernate.validator.*;
import org.hibernate.*;
import org.openxava.jpa.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;

/**
 * Persistence Manager for EJB3 JPA.
 * 
 * @author Javier Paniza
 */
public class JPAPersistenceProvider extends BasePOJOPersistenceProvider {
	
	protected Object find(Class pojoClass, Serializable key) {
		try {
			return XPersistence.getManager().find(pojoClass, key);
		}
		catch (EntityNotFoundException ex) {
			// As in JPA specification find does not throw EntityNotFoundException
			// but Hibernate (at least 3.2RC2) throw it (maybe an bug?)
			return null;
		}
	}

	protected void refresh(Object object) {				
		if (!isEmpty(object)) { // In this way because EntityNotFoundException rollback transaction
			XPersistence.getManager().refresh(object);		
		}				
	}
	
	private boolean isEmpty(Object object) {
		try {
			Object emptyObject = object.getClass().newInstance();
			return object.equals(emptyObject);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new PersistenceProviderException(XavaResources.getString("is_empty_error", object));
		}
	}

	
	protected void persist(Object object) {
		XPersistence.getManager().persist(object);		
	}
	
	public void remove(MetaModel metaModel, Object model)
			throws RemoveException, XavaException {
		try {
			XPersistence.getManager().remove(model);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoveException(XavaResources.getString("remove_error",
					metaModel.getName(), ex.getMessage()));
		}
	}

	public void begin() {		 
	}
		
	public void commit() {
		flush(); 
		XPersistence.commit();		 				
	}

	public void rollback() {	
		XPersistence.rollback();		 
	}
	
	public void reassociate(Object entity) {
		XPersistence.getManager().lock(entity, LockModeType.WRITE);  		
	}

	
	public void flush() {
		try {
			XPersistence.getManager().flush();
		}
		catch (InvalidStateException ex) {
			System.out.println("[JPAPersistenceProvider.flush] Validation message=" + ex.getLocalizedMessage());
			InvalidValue [] invalidValues = ex.getInvalidValues();
			for (int i=0; i < invalidValues.length; i++) {
				System.out.println("[JPAPersistenceProvider.flush] BeanClass=" + invalidValues[i].getBeanClass()); //  tmp
				System.out.println("[JPAPersistenceProvider.flush] PropertyName=" + invalidValues[i].getPropertyName() ); //  tmp
				System.out.println("[JPAPersistenceProvider.flush] Message=" + invalidValues[i].getMessage()); //  tmp
				System.out.println("[JPAPersistenceProvider.flush] Value=" + invalidValues[i].getValue()); //  tmp
			}
		}
		
	}

	protected Object createQuery(String query) { 		
		return XPersistence.getManager().createQuery(query);
	}

	protected void setParameterToQuery(Object query, String name, Object value) {
		((javax.persistence.Query) query).setParameter(name, value);
	}

	protected Object getUniqueResult(Object query) {
		Iterator it = ((javax.persistence.Query) query).getResultList().iterator();
		if (!it.hasNext()) return null;
		return it.next();
	}

}
