package org.openxava.model.impl;

import java.io.*;
import java.util.*;

import javax.ejb.*;
import javax.ejb.ObjectNotFoundException;

import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;

import net.sf.hibernate.*;


/**
 * @author Mª Carmen Gimeno
 */
public class HibernateMapFacadeImpl extends MapFacadeBean {
	
	private Session session;
		
	protected Object findEntity(IMetaEjb metaModel, Map keyValues) throws FinderException {
		try {
			MetaEjbImpl ejbImpl = new MetaEjbImpl(metaModel);
			Class className = metaModel.getBeanClass();
			Object key = null;
			if (keyValues.size() == 1) {
				key = keyValues.values().iterator().next();
			}
			else {
				throw new RuntimeException("Claves múltiples todavía no soportadas");
				/* String nombreClaseKey = clase.getName() + "$Key";
				Class claseKey = Class.forName(nombreClaseKey);
				Object key = ejbImpl.obtainPrimaryKeyFromKey(keyValues, claseKey, true); */
			}
			
			Object result = getSession().get(className, (Serializable) key);
			if (result == null) {
				throw new ObjectNotFoundException(
						XavaResources.getString(
								"object_with_key_not_found",
								metaModel.getName(),keyValues));
			}
			return result;
		}	
		catch (FinderException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(  //tmp: ¿HibernateException?
					XavaResources.getString("find_error",metaModel.getName()));
		}
	}
	
	protected IPropertiesContainer narrowPropertiesContainer(MetaModel metaModel, Object o) throws XavaException {
		return new POJOPropertiesContainerAdapter(o); 
	}
	
		
	protected Object createPersistentObject(IMetaEjb metaEjb, Map values)
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
				throw new CreateException(XavaResources.getString("create_persistent_error",ex.getMessage())); 
			}
	}
	
	protected void removePersistentObject(MetaModel metaModel, Object model) throws RemoveException, XavaException {
		try {
			getSession().delete(model);
		}
		catch (HibernateException ex) {
			ex.printStackTrace();
			throw new RemoveException(XavaResources.getString("remove_error",metaModel.getName(),ex.getMessage())); 
		}
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	
	

}
