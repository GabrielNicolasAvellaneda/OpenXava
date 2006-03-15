package org.openxava.model.impl;

import java.lang.reflect.*;
import java.rmi.*;
import java.util.*;

import javax.ejb.*;
import javax.rmi.*;

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
		try { 
			return (IPropertiesContainer) PortableRemoteObject.narrow(o, IPropertiesContainer.class);
		}
		catch (ClassCastException ex) {
			// In some cases we can obtain POJO objects from a EJB; 
			// for example in a calculated collection. This can work thank to this 
			return new POJOPropertiesContainerAdapter(o);  
		}
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
			throws RemoveException, XavaException {
		try {
			if (!metaModel.getMetaCollectionsAgregate().isEmpty()) {
				removeAllAggregateCollections(metaModel, object);
			}

			((EJBReplicable) toPropertiesContainer(metaModel,
				object)).remove();
		}
		catch (RemoveException ex) {
			throw ex;
		}
		catch (XavaException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("remove_error",
					metaModel.getName(),
					ex.getLocalizedMessage()));
		}
	}

	private void removeAllAggregateCollections(MetaModel metaModel,	Object modelObject)	throws Exception {
			Iterator it =
				metaModel.getMetaCollectionsAgregate().iterator();
			while (it.hasNext()) {
				MetaCollection metaCollection = (MetaCollection) it.next();
				removeAggregateCollection(metaModel, modelObject, metaCollection);
			}
		}
	
	private void removeAggregateCollection( 
		MetaModel metaModel,
		Object modelObject,
		MetaCollection metaCollection)
		throws XavaException, FinderException, ValidationException, RemoveException, RemoteException {
		Enumeration enumeration = null;	
		Object existing =
			executeGetXX(metaModel, modelObject, metaCollection.getName());								
		if (existing instanceof Enumeration) {
			enumeration = (Enumeration) existing;
		}
		else if (existing instanceof Collection) {
			enumeration = Collections.enumeration((Collection) existing);
		}
		else {
			throw new XavaException("collection_type_not_supported");
		}									
		MetaModel metaModelAggregate = metaCollection.getMetaReference().getMetaModelReferenced();
		while (enumeration.hasMoreElements()) {
			remove(metaModelAggregate, enumeration.nextElement());
		}
	}
	
	private Object executeGetXX(  
		MetaModel metaModel,
		Object modelObject,
		String memberName)
		throws XavaException {
		String method = "get" + Strings.firstUpper(memberName);
		try {
			return Objects.execute(modelObject, method);
		} catch (NoSuchMethodException ex) {
			throw new XavaException("method_expected", metaModel.getClassName(), method);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("method_execution_error",
					metaModel.getClassName(),					
					method,					
					ex.getLocalizedMessage());
		}
	}	

	public void commit() {
	}

	public void rollback() {
	}

	public Object getKey(IMetaEjb metaModel, Map keyValues) throws XavaException {
		return metaModel.obtainPrimaryKeyFromKey(keyValues);
	}
	
	public Object createAggregate( 
		IMetaEjb metaEjb,
		Map values,
		MetaModel metaModelContainer,
		Object containerModel, // can be key
		int number)
		throws CreateException, ValidationException, RemoteException, XavaException {
		Class containerClass = containerModel.getClass();
		try {
			IMetaEjb ejbContainer = (IMetaEjb) metaModelContainer; 
			if (!containerClass.equals(ejbContainer.getPrimaryKeyClass())) {
				containerClass = ejbContainer.getRemoteClass();
			}									 
			Class aggregateHomeClass = metaEjb.getHomeClass();
			Class[] argClass = { containerClass, int.class, java.util.Map.class };
			Method m = aggregateHomeClass.getDeclaredMethod("create", argClass);
			Object[] args = { containerModel, new Integer(number), values };
			return m.invoke(metaEjb.obtainHome(), args);
		} catch (InvocationTargetException ex) {
			Throwable th = ex.getTargetException();
			try {
				throw th;
			} catch (CreateException ex2) {
				throw ex2;
			} catch (ValidationException ex2) {
				throw ex2;
			} catch (RemoteException ex2) {
				throw ex2;
			} catch (Throwable ex2) {
				throw new RemoteException(ex2.getLocalizedMessage(), ex2);
			}
		} catch (NoSuchMethodException ex) {
			throw new XavaException("ejb_create_map3_required", metaEjb.getJndi(), containerClass);  
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException(XavaResources.getString("create_error", metaEjb.getJndi()));				
		}
	}

	public void flush() {
	}
	
}
