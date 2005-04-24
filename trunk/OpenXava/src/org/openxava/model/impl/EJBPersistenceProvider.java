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

	public Object find(IMetaEjb metaEntidad, Map valoresClave)
			throws FinderException, XavaException {
		Object key = metaEntidad.obtainPrimaryKeyFromKey(valoresClave);
		return find(metaEntidad, key);
	}

	public Object find(IMetaEjb metaEntidad, Object key) throws FinderException {
		Class claseHome = null;
		Class clasePK = null;
		try {
			clasePK = metaEntidad.getPrimaryKeyClass();
			Class[] classArg = {
				clasePK
			};
			claseHome = metaEntidad.getHomeClass();
			Method m = claseHome.getMethod("findByPrimaryKey", classArg);
			Object home = metaEntidad.obtainHome();
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
						metaEntidad.getName()));
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("find_error", metaEntidad
					.getName()));
		}
	}

	public IPropertiesContainer toPropertiesContainer(MetaModel metaModelo,
			Object o) throws XavaException {
		if (!(metaModelo instanceof IMetaEjb)) {
			throw new XavaException("only_ejb_error");
		}
		return (IPropertiesContainer) PortableRemoteObject.narrow(o,
				((IMetaEjb) metaModelo).getRemoteClass());
	}

	public Object create(IMetaEjb metaEjb, Map valores)
			throws CreateException, ValidationException, XavaException {
		try {
			return EJBFactory.create(metaEjb.obtainHome(), metaEjb.getHomeClass(),
					valores);
		}
		catch (NoSuchMethodException ex) {
			ex.printStackTrace();
			throw new XavaException("Es obligado que el bean " + metaEjb.getJndi()
					+ " tenga un constructor create(Map )");
		}
		catch (ValidationException ex) {
			throw ex;
		}
		catch (RemoteException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("create_persistent_error",
					ex.getLocalizedMessage()));
		}
	}

	public void remove(MetaModel metaModelo, Object modelo)
			throws RemoteException, RemoveException, XavaException {
		((EJBReplicable) toPropertiesContainer(metaModelo,
				modelo)).remove();
	}

	public void setSession(Session session) {
		// TODO Auto-generated method stub

	}

}
