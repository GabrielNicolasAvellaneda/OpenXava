package org.openxava.model.impl;

import java.lang.reflect.*;
import java.util.*;

import javax.ejb.*;

import net.sf.hibernate.*;

import org.openxava.model.meta.*;
import org.openxava.util.*;

/**
 * @author Mª Carmen Gimeno Alabau
 */
public class EJBPersistenceProvider implements IPersistenceProvider {

	public Object find(IMetaEjb metaEntidad, Map valoresClave)	throws FinderException, XavaException {
		Object key = metaEntidad.obtainPrimaryKeyFromKey(valoresClave);
		return find(metaEntidad, key);
}

	private Object find(IMetaEjb metaEntidad, Object key)	throws FinderException {		
		Class claseHome = null;
		Class clasePK = null;
		try {
			clasePK = metaEntidad.getPrimaryKeyClass();
			Class[] classArg = { clasePK };
			claseHome = metaEntidad.getHomeClass();
			Method m = claseHome.getMethod("findByPrimaryKey", classArg);								
			Object home = metaEntidad.obtainHome();
			Object[] arg = { key };
			return m.invoke(home, arg);
		} catch (NoSuchMethodException ex) {
			throw new EJBException(XavaResources.getString("findByPrimaryKey_expected", claseHome.getName(), clasePK.getName()));				
		} catch (InvocationTargetException ex) {
			Throwable th = ex.getTargetException();
			if (th instanceof FinderException) {
				throw (FinderException) th;
			} else {
				th.printStackTrace();
				throw new EJBException(XavaResources.getString("find_error", metaEntidad.getName()));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("find_error", metaEntidad.getName()));			
		}
	}

	public void setSession(Session session) {
		// TODO Auto-generated method stub
		
	}

}
