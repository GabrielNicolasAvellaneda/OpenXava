package org.openxava.model.impl;

import java.io.*;
import java.util.*;

import javax.ejb.*;
import javax.ejb.ObjectNotFoundException;

import net.sf.hibernate.*;

import org.openxava.model.meta.*;
import org.openxava.util.*;

/**
 * @author Mª Carmen Gimeno Alabau
 */
public class HibernatePersistenceProvider implements IPersistenceProvider {
  
	private Session session;
	
	public Object find(IMetaEjb metaModel, Map keyValues)
			throws FinderException {
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

	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
}
