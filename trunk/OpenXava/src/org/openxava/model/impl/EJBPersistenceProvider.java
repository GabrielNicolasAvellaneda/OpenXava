package org.openxava.model.impl;

import java.lang.reflect.*;
import java.rmi.*;
import java.util.*;

import javax.ejb.*;
import javax.rmi.*;

import org.hibernate.*;

import org.openxava.ejbx.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * @author Mª Carmen Gimeno Alabau
 */
public class EJBPersistenceProvider implements IPersistenceProvider {

	public Object find(IMetaEjb metaEntity, Map keyValues)
			throws FinderException, XavaException {
		Object key = metaEntity.obtainPrimaryKeyFromKey(keyValues);
		return find(metaEntity, key);
	}

	public Object find(IMetaEjb metaEntity, Object key) throws FinderException {
		Class claseHome = null;
		Class clasePK = null;
		try {
			clasePK = metaEntity.getPrimaryKeyClass();
			Class[] classArg = {
				clasePK
			};
			claseHome = metaEntity.getHomeClass();
			Method m = claseHome.getMethod("findByPrimaryKey", classArg);
			Object home = metaEntity.obtainHome();
			Object[] arg = {
				key
			};
			return m.invoke(home, arg);
		}
		catch (NoSuchMethodException ex) {
			throw new EJBException(XavaResources.getString(
					"findByPrimaryKey_expected", claseHome.getName(), clasePK.getName()));
		}
		catch (InvocationTargetException ex) {
			Throwable th = ex.getTargetException();
			if (th instanceof FinderException) {
				throw (FinderException) th;
			}
			else {
				th.printStackTrace();
				throw new EJBException(XavaResources.getString("find_error",
						metaEntity.getName()));
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("find_error", metaEntity
					.getName()));
		}
	}

	public IPropertiesContainer toPropertiesContainer(MetaModel metaModel,
			Object o) throws XavaException {
		if (!(metaModel instanceof IMetaEjb)) {
			throw new XavaException("only_ejb_error");
		}
		return (IPropertiesContainer) PortableRemoteObject.narrow(o,
				((IMetaEjb) metaModel).getRemoteClass());
	}

	public Object create(IMetaEjb metaEjb, Map values)
			throws CreateException, ValidationException, XavaException {
		try {
			return EJBFactory.create(metaEjb.obtainHome(), metaEjb.getHomeClass(),
					values);
		}
		catch (NoSuchMethodException ex) {
			ex.printStackTrace();
			throw new XavaException("ejb_create_map_required", metaEjb.getJndi()); 
		}
		catch (ValidationException ex) {
			throw ex;
		}
		catch (RemoteException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("create_persistent_error",
					metaEjb.getName(),
					ex.getLocalizedMessage()));
		}
	}

	public void remove(MetaModel metaModel, Object object)
			throws RemoteException, RemoveException, XavaException {
		((EJBReplicable) toPropertiesContainer(metaModel,
				object)).remove();
	}

	public void setSession(Session session) { // tmp: remove
	}

}
