package org.openxava.model.impl;

import java.io.*;

import javax.ejb.*;
import javax.persistence.*;

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
		return XPersistence.getManager().find(pojoClass, key);
	}

	protected void refresh(Object object) {		
		try {			
			XPersistence.getManager().refresh(object);			
		}
		catch (EntityNotFoundException ex) {
			ex.printStackTrace();
			// References as key that point to a non-existent object are supported
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
		XPersistence.getManager().flush();		
	}

}
