package org.openxava.model.impl;

import java.io.*;
import java.util.*;

import javax.ejb.*;

import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;

import net.sf.hibernate.*;


/**
 * @author Javier Paniza
 */
public class HibernateMapFacadeImpl extends MapFacadeBean {
	
	private Session session;
		
	protected Object findEntity(IMetaEjb metaModel, Map keyValues) throws FinderException {
		try {
			MetaEjbImpl ejbImpl = new MetaEjbImpl(metaModel);
			Class clase = metaModel.getPropertiesClass();
			String nombreClaseKey = clase.getName() + "$Key";
			Class claseKey = Class.forName(nombreClaseKey);
			Object key = ejbImpl.obtainPrimaryKeyFromKey(keyValues, claseKey, true);
			return getSession().get(metaModel.getPropertiesClass(), (Serializable) key);			
		}	
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(
				"Imposible realizar la búsqueda de "
					+ metaModel.getName()
					+ " por:\n"
					+ ex.getLocalizedMessage()); // tmp i18n
		}
	}
	
	protected IPropertiesContainer narrowPropertiesContainer(MetaModel metaModel, Object o) throws XavaException {
		return new POJOPropertiesContainerAdapter(o); 
	}
	
		
	protected Object createPersistentObject(IMetaEjb metaEjb, Map values)
		throws CreateException, ValidationException, XavaException {
			Object object = null;
			try {				
				object = metaEjb.getRemoteClass().newInstance();
				PropertiesManager mp = new PropertiesManager(object);
				mp.executeSets(values);
				getSession().save(object);
				return object;
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new CreateException("Imposible grabar"); // tmp: i18n
			}
	}
	
	protected void removePersistentObject(MetaModel metaModel, Object model) throws RemoveException, XavaException {
		try {
			getSession().delete(model);
		}
		catch (HibernateException ex) {
			ex.printStackTrace();
			throw new RemoveException("Imposible borrar " + model); // tmp: i18n
		}
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	
	

}
