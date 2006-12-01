package org.openxava.model.impl;

import java.lang.reflect.*;
import java.rmi.*;
import java.util.*;

import javax.ejb.*;
import javax.rmi.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.ejbx.*;
import org.openxava.hibernate.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * @author Mª Carmen Gimeno Alabau
 */
public class EJBPersistenceProvider implements IPersistenceProvider {
	
	private Log log = LogFactory.getLog(EJBPersistenceProvider.class);
	
	public Object find(MetaModel metaModel, Map keyValues)
			throws FinderException, XavaException {
		Object key = metaModel.getMetaEJB().obtainPrimaryKeyFromKey(keyValues);
		return find(metaModel, key);
	}

	public Object find(MetaModel metaModel, Object key) throws FinderException {
		Class claseHome = null;
		Class clasePK = null;
		try {
			clasePK = metaModel.getMetaEJB().getPrimaryKeyClass();
			Class[] classArg = {
				clasePK
			};
			claseHome = metaModel.getMetaEJB().getHomeClass();
			Method m = claseHome.getMethod("findByPrimaryKey", classArg);
			Object home = metaModel.getMetaEJB().obtainHome();
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
				log.error(ex.getMessage(), ex);
				throw new EJBException(XavaResources.getString("find_error",
						metaModel.getName()));
			}
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new EJBException(XavaResources.getString("find_error", metaModel
					.getName()));
		}
	}

	public IPropertiesContainer toPropertiesContainer(MetaModel metaModel,
			Object o) throws XavaException {
		if (metaModel.getMetaEJB() == null) {
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

	public Object create(MetaModel metaModel, Map values)
			throws CreateException, ValidationException, XavaException {
		try {
			return EJBFactory.create(metaModel.getMetaEJB().obtainHome(), metaModel.getMetaEJB().getHomeClass(),
					values);
		}
		catch (NoSuchMethodException ex) {
			log.error(ex.getMessage(), ex);
			throw new XavaException("ejb_create_map_required", metaModel.getMetaEJB().getJndi()); 
		}
		catch (ValidationException ex) {
			throw ex;
		}
		catch (RemoteException ex) {
			log.error(ex.getMessage(), ex);
			throw new EJBException(XavaResources.getString("create_persistent_error",
					metaModel.getName(),
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
			log.error(ex.getMessage(), ex);
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
			throw new XavaException("method_expected", modelObject.getClass(), method);
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new XavaException("method_execution_error",
					modelObject.getClass(),					
					method,					
					ex.getLocalizedMessage());
		}
	}	
	
	
	public void begin() {
		XHibernate.setCmt(true); 
	}

	public void commit() {		
		XHibernate.commit(); 
		XHibernate.setCmt(false); 
	}

	public void rollback() {		
		XHibernate.rollback(); 
		XHibernate.setCmt(false); 
	}
	
	public void reassociate(Object entity) { 
	}

	public Object getKey(MetaModel metaModel, Map keyValues) throws XavaException {
		return metaModel.getMetaEJB().obtainPrimaryKeyFromKey(keyValues);
	}
	
	public Map keyToMap(MetaModel metaModel, Object key) throws XavaException {
		return metaModel.getMetaEJB().obtainMapFromPrimaryKey(key); 
	}
	
	public Object createAggregate( 
		MetaModel metaModel,
		Map values,
		MetaModel metaModelContainer,
		Object containerModel, // can be key
		int number)
		throws CreateException, ValidationException, RemoteException, XavaException {
		Class containerClass = containerModel.getClass();
		try {
			MetaModel ejbContainer = (MetaModel) metaModelContainer; 
			if (!containerClass.equals(ejbContainer.getMetaEJB().getPrimaryKeyClass())) {
				containerClass = ejbContainer.getMetaEJB().getRemoteClass();
			}									 
			Class aggregateHomeClass = metaModel.getMetaEJB().getHomeClass();
			Class[] argClass = { containerClass, int.class, java.util.Map.class };
			Method m = aggregateHomeClass.getDeclaredMethod("create", argClass);
			Object[] args = { containerModel, new Integer(number), values };
			return m.invoke(metaModel.getMetaEJB().obtainHome(), args);
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
			throw new XavaException("ejb_create_map3_required", metaModel.getMetaEJB().getJndi(), containerClass);  
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new RemoteException(XavaResources.getString("create_error", metaModel.getMetaEJB().getJndi()));				
		}
	}

	public void flush() {
	}

	public Object findByAnyProperty(MetaModel metaModel, Map searchingValues) throws ObjectNotFoundException, FinderException, XavaException {
		// Hibernate is used to implement the searching, although an EJB2 entity is returned
		// This is because EJB2 does not support dynamic queries, and hibernate and POJOs are always presents.
		HibernatePersistenceProvider hib = new HibernatePersistenceProvider();
		Object pojo = hib.findByAnyProperty(metaModel, searchingValues);		
		IPropertiesContainer pc = hib.toPropertiesContainer(metaModel, pojo);
		try {
			Map key = pc.executeGets(Strings.toString(metaModel.getAllKeyPropertiesNames(), ":"));				
			return find(metaModel, key);
		}
		catch (RemoteException ex) { 
			log.error(ex.getMessage(), ex);
			throw new PersistenceProviderException(ex.getLocalizedMessage());
		}		
	}
	
}
