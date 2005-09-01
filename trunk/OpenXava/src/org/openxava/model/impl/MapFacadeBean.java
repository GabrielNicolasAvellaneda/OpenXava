package org.openxava.model.impl;

import java.lang.reflect.*;
import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import org.openxava.calculators.*;
import org.openxava.component.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;
import org.openxava.validators.*;
import org.openxava.validators.meta.*;


/**
 * Implement the logic of MapFacade. <p>
 * 
 * @author Javier Paniza
 */

// tmp: Quitar IMetaEjb (si hay)
// tmp: Quitar EJBException

public class MapFacadeBean implements IMapFacadeImpl, SessionBean {
	
	
	private javax.ejb.SessionContext mySessionCtx = null;
	private final static long serialVersionUID = 3206093459760846163L;
	
	public Object create(String modelName, Map values)
		throws CreateException, XavaException, ValidationException, RemoteException {
		IPersistenceProvider persistenceProvider = createPersistenceProvider();
		try {
			persistenceProvider.begin();
			MetaModel metaModel = getMetaModel(modelName);					
			Object result = create(persistenceProvider, metaModel, values, null, null, 0);
			persistenceProvider.commit();
			return result;
		} 
		catch (CreateException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (ValidationException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (XavaException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			rollback(persistenceProvider);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	public Map getValues(
			String modelName,
			Map keyValues,
			Map membersNames)
			throws FinderException, XavaException, RemoteException {		
		IPersistenceProvider persistenceProvider = createPersistenceProvider();
		try {
			persistenceProvider.begin();
			MetaModel metaModel = getMetaModel(modelName);					
			Map result = getValuesImpl(persistenceProvider, modelName, keyValues, membersNames);
			persistenceProvider.commit();
			return result;
		} 
		catch (FinderException ex) {
			ex.printStackTrace();
			rollback(persistenceProvider);
			throw ex;
		}
		catch (XavaException ex) {
			ex.printStackTrace();
			rollback(persistenceProvider);
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			rollback(persistenceProvider);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	public void delete(String modelName, Map keyValues)
		throws RemoveException, ValidationException, XavaException, RemoteException 
	{		
		IPersistenceProvider persistenceProvider = createPersistenceProvider();
		try {
			persistenceProvider.begin();
			MetaModel metaModel = getMetaModel(modelName);					
			remove(persistenceProvider, metaModel, keyValues);
			persistenceProvider.commit();
		}
		catch (RemoveException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (ValidationException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (XavaException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (Exception ex) {
			rollback(persistenceProvider);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	public void setValues(String modelName, Map keyValues, Map values)
		throws FinderException, ValidationException, XavaException, RemoteException 
	{				
		IPersistenceProvider persistenceProvider = createPersistenceProvider();
		try {
			persistenceProvider.begin();
			MetaModel metaModel = getMetaModel(modelName);					
			setValues(persistenceProvider, metaModel, keyValues, values);
			persistenceProvider.commit();
		}
		catch (FinderException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (ValidationException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (XavaException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			rollback(persistenceProvider);
			throw new RemoteException(ex.getMessage());
		}
	}

	public Object findEntity(String modelName, Map keyValues)
		throws FinderException, RemoteException {
		IPersistenceProvider persistenceProvider = createPersistenceProvider();
		try {
			persistenceProvider.begin();
			MetaModel metaModel = getMetaModel(modelName);					
			Object result = findEntity(persistenceProvider, modelName, keyValues);
			persistenceProvider.commit();
			return result;
		}	
		catch (FinderException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			rollback(persistenceProvider);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	public Map createReturningValues(String modelName, Map values) 
		throws CreateException, XavaException, ValidationException, RemoteException {
		IPersistenceProvider persistenceProvider = createPersistenceProvider();
		try {
			persistenceProvider.begin();
			MetaModel metaModel = getMetaModel(modelName);					
			Map result = createReturningValues(persistenceProvider, modelName, values);
			persistenceProvider.commit();
			return result;
		}	
		catch (CreateException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (ValidationException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (XavaException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			rollback(persistenceProvider);
			throw new RemoteException(ex.getMessage());
		}		
	}
	
	public Map createReturningKey(String modelName, Map values) 
		throws CreateException, XavaException, ValidationException, RemoteException {
		IPersistenceProvider persistenceProvider = createPersistenceProvider();
		try {
			persistenceProvider.begin();
			MetaModel metaModel = getMetaModel(modelName);					
			Map result = createReturningKey(persistenceProvider, modelName, values);
			persistenceProvider.commit();
			return result;
		}	
		catch (CreateException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (ValidationException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (XavaException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			rollback(persistenceProvider);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	public Object createAggregate(String modelName, Map containerKeyValues, int counter, Map values)  
		throws CreateException,ValidationException, XavaException, RemoteException 
	{		
		IPersistenceProvider persistenceProvider = createPersistenceProvider();
		try {
			persistenceProvider.begin();
			MetaModel metaModel = getMetaModel(modelName);					
			Object result = createAggregate(persistenceProvider, modelName, containerKeyValues, counter, values);
			persistenceProvider.commit();
			return result;
		}	
		catch (CreateException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (ValidationException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (XavaException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			rollback(persistenceProvider);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	public Object createAggregate(String modelName, Object container, int counter, Map values)  
		throws CreateException,ValidationException, XavaException, RemoteException
	{		
		IPersistenceProvider persistenceProvider = createPersistenceProvider();
		try {
			persistenceProvider.begin();
			MetaModel metaModel = getMetaModel(modelName);					
			Object result = createAggregate(persistenceProvider, modelName, container, counter, values);
			persistenceProvider.commit();
			return result;
		}	
		catch (CreateException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (ValidationException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (XavaException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			rollback(persistenceProvider);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	public Map createAggregateReturningKey(String modelName, Map containerKeyValues, int counter, Map values)  
		throws CreateException,ValidationException, XavaException, RemoteException 
	{		
		IPersistenceProvider persistenceProvider = createPersistenceProvider();
		try {
			persistenceProvider.begin();
			MetaModel metaModel = getMetaModel(modelName);					
			Map result = createAggregateReturningKey(persistenceProvider, modelName, containerKeyValues, counter, values);
			persistenceProvider.commit();
			return result;
		}	
		catch (CreateException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (ValidationException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (XavaException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			rollback(persistenceProvider);
			throw new RemoteException(ex.getMessage());
		}
	}

	public Map getValues(
		String modelName,
		Object modelObject,
		Map memberNames) throws XavaException, RemoteException  
		 {		
		IPersistenceProvider persistenceProvider = createPersistenceProvider();
		try {
			persistenceProvider.begin();
			MetaModel metaModel = getMetaModel(modelName);					
			Map result = getValues(persistenceProvider, modelName, modelObject, memberNames);
			persistenceProvider.commit();
			return result;
		}	
		catch (XavaException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			rollback(persistenceProvider);
			throw new RemoteException(ex.getMessage());
		}
	}

	public Messages validate(String modelName, Map values) throws XavaException, RemoteException {   			
		IPersistenceProvider persistenceProvider = createPersistenceProvider();
		try {
			persistenceProvider.begin();
			MetaModel metaModel = getMetaModel(modelName);					
			Messages result = validate(persistenceProvider, modelName, values);
			persistenceProvider.commit();
			return result;
		}	
		catch (XavaException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			rollback(persistenceProvider);
			throw new RemoteException(ex.getMessage());
		}
	}
	
	public void removeCollectionElement(String modelName, Map keyValues, String collectionName, Map collectionElementKeyValues)   
		throws FinderException,	ValidationException, XavaException, RemoveException, RemoteException 
	{
		IPersistenceProvider persistenceProvider = createPersistenceProvider();
		try {
			persistenceProvider.begin();
			MetaModel metaModel = getMetaModel(modelName);					
			removeCollectionElement(persistenceProvider, modelName, keyValues, collectionName, collectionElementKeyValues);
			persistenceProvider.commit();
		} 
		catch (FinderException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (ValidationException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (RemoveException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (XavaException ex) {
			rollback(persistenceProvider);
			throw ex;
		}
		catch (Exception ex) {
			rollback(persistenceProvider);
			throw new RemoteException(ex.getMessage());
		}						
	}
		
	private void removeCollectionElement(IPersistenceProvider persistenceProvider, String modelName, Map keyValues, String collectionName, Map collectionElementKeyValues) 
		throws FinderException,	ValidationException, XavaException, RemoveException, RemoteException 
	{
		MetaModel metaModel = getMetaModel(modelName);
		MetaCollection metaCollection = metaModel.getMetaCollection(collectionName);
		MetaModel metaModelReferenced = metaCollection.getMetaReference().getMetaModelReferenced();
		if (metaCollection.isAggregate()) {						
			remove(persistenceProvider, metaModelReferenced, collectionElementKeyValues);
		}
		else {
			Map nullParentKey = new HashMap();
			nullParentKey.put(Strings.firstLower(modelName), null);						
			setValues(persistenceProvider, metaModelReferenced, collectionElementKeyValues, nullParentKey);					
		}												
		if (metaCollection.hasPostRemoveCalculators()) {
			executePostremoveCollectionElement(persistenceProvider, metaModel, keyValues, metaCollection);			
		}						
	}
	
	private Messages validate(IPersistenceProvider persistenceProvider, String modelName, Map values) throws XavaException { 			
		MetaEntityEjb metaEntity = (MetaEntityEjb) MetaComponent.get(modelName).getMetaEntity();		
		Messages validationErrors = new Messages(); 				
		validate(persistenceProvider, validationErrors, metaEntity, values, null, null);
		return validationErrors;
	}
	
	private Map getValues(
		IPersistenceProvider persistenceProvider, 	
		String modelName,
		Object modelObject,
		Map memberNames) throws XavaException
		 {		
		try {			
			MetaModel metaModel = getMetaModel(modelName);
			Map result = getValues(persistenceProvider, metaModel, modelObject, memberNames);						
			return result;
		} catch (XavaException ex) {
			ex.printStackTrace();
			throw new XavaException("get_values_error", modelName);
		}
	}
	
	private Map createAggregateReturningKey(IPersistenceProvider persistenceProvider, String modelName, Map containerKeyValues, int counter, Map values) 
		throws CreateException,ValidationException, XavaException 
	{		
		MetaModel metaModel = getMetaModel(modelName);
		MetaModel metaModelContainer = metaModel.getMetaModelContainer();
		try {					
			Object containerKey = ((IMetaEjb) metaModelContainer).obtainPrimaryKeyFromKey(containerKeyValues);
			Object aggregate = createAggregate(persistenceProvider, metaModel, containerKey, counter, values);						
			return getValues(persistenceProvider, metaModel, aggregate, getKeyNames(metaModel));			
		}
		catch (ClassCastException ex) {
			throw new XavaException("aggregate_must_be_persistent_for_create", metaModelContainer.getName());					
		}
	}
	
	private Object createAggregate(IPersistenceProvider persistenceProvider, String modelName, Object container, int counter, Map values) 
		throws CreateException,ValidationException, XavaException
	{		
		MetaModel metaModel = getMetaModel(modelName);		
		return createAggregate(persistenceProvider, metaModel, container, counter, values);
	}
	
	private Object createAggregate(IPersistenceProvider persistenceProvider, String modelName, Map containerKeyValues, int counter, Map values) 
		throws CreateException,ValidationException, XavaException 
	{		
		MetaModel metaModel = getMetaModel(modelName);
		MetaModel metaModelContainer = metaModel.getMetaModelContainer();
		try {					
			Object containerKey = ((IMetaEjb) metaModelContainer).obtainPrimaryKeyFromKey(containerKeyValues);
			return createAggregate(persistenceProvider, metaModel, containerKey, counter, values);
		}
		catch (ClassCastException ex) {
			throw new XavaException("aggregate_must_be_persistent_for_create", metaModelContainer.getName());					
		}
	}
	
	private Map createReturningKey(IPersistenceProvider persistenceProvider, String modelName, Map values)
		throws CreateException, XavaException, ValidationException {
		MetaEntityEjb metaEntity = (MetaEntityEjb) MetaComponent.get(modelName).getMetaEntity();
		Object entity = create(persistenceProvider, metaEntity, values, null, null, 0);
		return getValues(persistenceProvider, metaEntity, entity, getKeyNames(metaEntity));
	}
	
	private Map createReturningValues(IPersistenceProvider persistenceProvider, String modelName, Map values)
		throws CreateException, XavaException, ValidationException {
		MetaEntityEjb metaEntity = (MetaEntityEjb) MetaComponent.get(modelName).getMetaEntity();
		Object entity = create(persistenceProvider, metaEntity, values, null, null, 0);
		return getValues(persistenceProvider, metaEntity, entity, values);
	}
		
	private Map getValuesImpl(
		IPersistenceProvider	persistenceProvider,	
		String modelName,
		Map keyValues,
		Map membersNames)
		throws FinderException, XavaException {		
		try {			
			MetaModel metaModel = getMetaModel(modelName);						 
			Map result =
				getValues(
					persistenceProvider, 
					metaModel,
					findEntity(persistenceProvider, modelName, keyValues),
					membersNames);						
			return result;
		} catch (XavaException ex) {
			ex.printStackTrace();
			throw new XavaException("get_values_error", modelName);
		}
	}
		
	private Map getKeyNames(MetaModel metaModel) throws XavaException {
		Iterator itProperties = metaModel.getKeyPropertiesNames().iterator();
		Map names = new HashMap();
		while (itProperties.hasNext()) {
			names.put(itProperties.next(), null);
		}
		Iterator itReferences = metaModel.getMetaReferencesKey().iterator();
		while (itReferences.hasNext()) {
			MetaReference ref = (MetaReference) itReferences.next();
			names.put(ref.getName(), getKeyNames(ref.getMetaModelReferenced()));
		}		
		return names;
	}
	
	private Object createAggregate(IPersistenceProvider persistenceProvider, MetaModel metaModel, Object container, int counter, Map values) 
		throws CreateException,ValidationException, XavaException 
	{				
		MetaModel metaModelContainer = metaModel.getMetaModelContainer();								
		int attempts = 0;
		// Loop with 10 attempts, because the counter can be repeated because deleted objects
		do {				 
			try {
				return create(persistenceProvider, metaModel, values, metaModelContainer, container, counter);
			}
			catch (DuplicateKeyException ex) {
				if (attempts > 10) throw ex;
				counter++;
				attempts++;
			}				
		}
		while (true);			
	}
	
	

	/**
	 * Allows create independent entities or aggregates to another entities. <p>
	 *
	 * If the argument <tt>metaModelContainer</tt> is null it assume
	 * a independent entity else a aggregate.<p>
	 * 
	 * @param metaModel  of entity or aggregate to create. It must to implement IMetaEjb
	 * @param values  to assign to entity to create.
	 * @param metaModelContainer  Only if the object is a aggregate. It's the MetaModel of container model.
	 * @param containerModel Only if object to create is a aggregate.
	 * @param number  Only if object to create is a aggregate. It's a secuential number.
	 * @return The created entity.
	 */
	private Object create(
		IPersistenceProvider persistenceProvider,	
		MetaModel metaModel,
		Map values,
		MetaModel metaModelContainer,
		Object containerKey,
		int number)
		throws CreateException, ValidationException, XavaException {						
		try {
			if (!(metaModel instanceof IMetaEjb)) {
				throw new IllegalArgumentException(XavaResources.getString("argument_type_error", "metaEjb", "MapFacadeBean.createEjb", "IMetaEjb"));
			}
			//removeReadOnlyFields(metaEjb, values); // not remove the read only fields because it maybe needed initialized on create
			removeCalculatedFields(metaModel, values); 			
			Messages validationErrors = new Messages(); 
			validateExistRequired(validationErrors, metaModel, values);
			Map collections = extractCollections(metaModel, values);			
			validate(persistenceProvider, validationErrors, metaModel, values, null, containerKey);
			removeViewProperties(metaModel, values); 
			if (validationErrors.contains()) {
				throw new ValidationException(validationErrors);			
			}
			Map convertedValues = convertSubmapsInObject(persistenceProvider, metaModel, values, XavaPreferences.getInstance().isEJB2Persistence());
			Object newObject = null;
			if (metaModelContainer == null) {
				newObject = persistenceProvider.create((IMetaEjb)metaModel, convertedValues);
			} else {				
				newObject =
					executeEJBCreate(
						(IMetaEjb) metaModel,
						convertedValues,
						metaModelContainer,
						containerKey,
						number);
			}			

			if (collections != null) {
				addCollections(persistenceProvider, metaModel, newObject, collections);
			}
			return newObject;
		} catch (ValidationException ex) {
			throw ex;
		} catch (DuplicateKeyException ex) {
			ex.printStackTrace();
			throw new DuplicateKeyException(
				XavaResources.getString("no_create_exists", metaModel.getName()));	
		} catch (CreateException ex) {
			ex.printStackTrace();
			throw new CreateException(XavaResources.getString("create_error", metaModel.getName()));		} catch (RemoteException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("create_error", metaModel.getName()));
		} catch (XavaException ex) {
			ex.printStackTrace();
			throw new XavaException("create_error", metaModel.getName());
		}
	}

	private Object executeEJBCreate(
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


	/**
	 * @param values  is modified
	 * @return Nulo  If nothing
	 */
	private Map extractCollections(MetaModel metaModel, Map values)
		throws XavaException {
		Iterator it = metaModel.getColectionsNames().iterator();
		Map result = new HashMap();
		while (it.hasNext()) {
			Object name = it.next();
			if (values.containsKey(name)) {
				result.put(name, values.get(name));
				values.remove(name);
			}
		}
		return result.size() == 0 ? null : result;
	}


	private void addCollections(
		IPersistenceProvider persistenceProvider,	
		MetaModel metaModel,
		Object modelObject,
		Map collections)
		throws ValidationException, XavaException {
		Iterator it = collections.entrySet().iterator();
		Messages errors = new Messages();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			String memberName = (String) e.getKey();
			Collection collection = null;
			try {				
				collection = (Collection) e.getValue();
			}
			catch (ClassCastException ex) {
				throw new XavaException("collection_type_required", memberName, metaModel.getName(), e.getValue().getClass().getName());
			}
			
			addCollection(persistenceProvider, errors, metaModel, modelObject, memberName, collection);
			
			if (errors.contains()) {
				throw new ValidationException(errors);
			}
		}
	}


	private void modifyCollections(
		IPersistenceProvider persistenceProvider,	
		MetaModel metaModel,
		Object modelObject,
		Map collections)
		throws ValidationException, XavaException {		
		Iterator it = collections.entrySet().iterator();
		Messages errors = new Messages();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			String memberName = (String) e.getKey();
			Collection collection = null;
			try {
				collection = (Collection) e.getValue();				
			}
			catch (ClassCastException ex) {
				throw new XavaException("collection_type_required", memberName, metaModel.getName(), e.getValue().getClass().getName());
			}
			modifyCollection(persistenceProvider, errors, metaModel, modelObject, memberName, collection);
		}
		if (errors.contains()) {
			throw new ValidationException(errors);		
		}		
	} 


	private void modifyCollection(
		IPersistenceProvider persistenceProvider,
		Messages errors,
		MetaModel metaModel,
		Object modelObject,
		String memberName,
		Collection collection)
		throws XavaException {		
		try {
			// The next is not the most efficient option
			refreshCollection(persistenceProvider, metaModel, modelObject, memberName, collection);
			removeCollection(persistenceProvider, metaModel, modelObject, memberName); 
			addCollection(persistenceProvider, errors, metaModel, modelObject, memberName, collection);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("assign_collection_error", memberName, metaModel.getName(), ex.getLocalizedMessage());
		}		
	}

	private void removeCollection(
		IPersistenceProvider persistenceProvider,	
		MetaModel metaModel,
		Object modelObject,
		MetaCollection metaCollection)
		throws XavaException {
		try {
			if (metaCollection.isAggregate()) {
				removeAggregateCollection(persistenceProvider, metaModel, modelObject, metaCollection);
			} else {
				removeEntityCollection(metaModel, modelObject, metaCollection);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("remove_collection_error", metaCollection.getName(), metaModel.getName(), ex.getLocalizedMessage());
		}
	}

	private void removeCollection(
		IPersistenceProvider persistenceProvider,	
		MetaModel metaModel,
		Object modelObject,
		String memberName)
		throws XavaException {
		removeCollection(persistenceProvider, metaModel, modelObject, metaModel.getMetaCollection(memberName));
	}


	private void removeEntityCollection(
		MetaModel metaModel,
		Object modelObject,
		MetaCollection metaCollection)
		throws XavaException {
		Object existing =
			executeGetXX(metaModel, modelObject, metaCollection.getName());
		if (existing instanceof Enumeration) {
			Enumeration e = (Enumeration) existing;
			while (e.hasMoreElements()) {
				Object associatedEntity = e.nextElement();
				executeRemoveXX(metaModel, modelObject, metaCollection, associatedEntity);
			}
		}
		else if (existing instanceof Collection){
			Iterator it = ((Collection) existing).iterator();
			while (it.hasNext()) {
				Object associatedEntity = it.next();
				executeRemoveXX(metaModel, modelObject, metaCollection, associatedEntity);
			}			
		}
		else {
			throw new XavaException("collection_type_not_supported", existing.getClass());
		}
	}


	private void removeAggregateCollection(
		IPersistenceProvider persistenceProvider,	
		MetaModel metaModel,
		Object modelObject,
		MetaCollection metaCollection)
		throws XavaException, FinderException, ValidationException, RemoveException, RemoteException {
		Enumeration enum = null;	
		Object existing =
			executeGetXX(metaModel, modelObject, metaCollection.getName());								
		if (existing instanceof Enumeration) {
			enum = (Enumeration) existing;
		}
		else if (existing instanceof Collection) {
			enum = Collections.enumeration((Collection) existing);
		}
		else {
			throw new XavaException("collection_type_not_supported");
		}									
		MetaModel metaModelAggregate = metaCollection.getMetaReference().getMetaModelReferenced();
		while (enum.hasMoreElements()) {
			try {		
				remove(persistenceProvider, metaModelAggregate, enum.nextElement());
			}
			catch (ValidationException ex) {
				throw ex;
			}			
		}
	}

	private void removeAllAggregateCollections(
		IPersistenceProvider persistenceProvider,	
		MetaModel metaModel,
		Object modelObject)
		throws Exception {

		Iterator it =
			metaModel.getMetaCollectionsAgregate().iterator();
		while (it.hasNext()) {
			MetaCollection metaCollection = (MetaCollection) it.next();
			removeAggregateCollection(persistenceProvider, metaModel, modelObject, metaCollection);
		}
	}


	private void addCollection(
		IPersistenceProvider persistenceProvider,	
		Messages errors,
		MetaModel metaModel,
		Object model,
		String memberName,
		Collection collection)
		throws XavaException {
		addCollection(
			persistenceProvider, 	
			errors,
			metaModel,
			model,
			metaModel.getMetaCollection(memberName),
			collection);
	}


	private void addCollection(
		IPersistenceProvider persistenceProvider,	
		Messages errors,
		MetaModel metaModel,
		Object model,
		MetaCollection metaCollection,
		Collection collection)
		throws XavaException {
		try {						
			metaCollection.validate(errors, collection, null, null);
			MetaReference metaReference = metaCollection.getMetaReference();
			if (metaReference.isAggregate()) {
				addAggregateCollection(
					persistenceProvider,	
					metaModel,
					model,
					metaCollection,
					collection);
			} else { // Normal entity
				addEntityCollection(persistenceProvider, metaModel, model, metaCollection, collection);
			}
		}
		catch (ValidationException ex) {
			errors.add(ex.getErrors());
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("add_to_collection_error",
					metaCollection.getName(),
					metaModel.getName(),
					ex.getLocalizedMessage());
		}
	}
	
	private void refreshCollection(
		IPersistenceProvider persistenceProvider,	
		MetaModel metaModel,
		Object modelObject,
		String memberName,
		Collection collection)
		throws XavaException {
		try {									
			MetaCollection metaCollection = metaModel.getMetaCollection(memberName);
			MetaReference metaReference = metaCollection.getMetaReference();
			if (metaReference.isAggregate()) {
				refreshAggregateCollection(					
					persistenceProvider,
					metaModel,
					modelObject,
					metaCollection,
					collection);
			}
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("refresh_error", memberName, metaModel.getName());
		}
	}
	


	private void addAggregateCollection(
		IPersistenceProvider persistenceProvider,
		MetaModel metaModel,
		Object modelObject,
		MetaCollection metaCollection,
		Collection collection)
		throws
			XavaException,
			CreateException,
			RemoteException,
			FinderException,
			ValidationException {
		Object object = persistenceProvider.toPropertiesContainer(metaModel, modelObject);
		MetaAggregateEjb metaAgregadoEjb = (MetaAggregateEjb) metaCollection.getMetaReference().getMetaModelReferenced();
		if (collection == null) collection = Collections.EMPTY_LIST;
		Iterator it = collection.iterator();
		int number = 0;
		while (it.hasNext()) {
			Map values = (Map) it.next();
			Object aggregateEntity =
				createAggregateEjb(
					persistenceProvider,	
					metaModel,
					object,
					metaAgregadoEjb,
					values,
					number++);
		}
	}
	
	private void refreshAggregateCollection(
		IPersistenceProvider persistenceProvider,	
		MetaModel metaModel,
		Object modelObject,
		MetaCollection metaCollection,
		Collection collection)
		throws
			XavaException,
			CreateException,
			RemoteException,
			FinderException {
		if (collection == null) return;		
		Object object = persistenceProvider.toPropertiesContainer(metaModel, modelObject);
		MetaAggregateEjb metaAgregadoEjb = (MetaAggregateEjb) metaCollection.getMetaReference().getMetaModelReferenced();
		
		Iterator it = collection.iterator();
		int number = 0;
		String aggregateModelName = metaModel.getName() + "." + metaAgregadoEjb.getName(); 
		while (it.hasNext()) {
			Map values = (Map) it.next();						
			try {							
				Map oldValues = getValuesImpl(persistenceProvider, aggregateModelName, values, getMemberNames(metaAgregadoEjb)); 
				Map newValues = new HashMap(values);
				values.clear();
				values.putAll(oldValues);
				values.putAll(newValues);				
			}
			catch (FinderException ex) {
			}
		}
	}
	

	private Object createAggregateEjb(
		IPersistenceProvider persistenceProvider,	
		MetaModel metaModelMain,
		Object mainModel,
		MetaAggregateEjb metaAggregateEjb,
		Map values,
		int number)
		throws CreateException, ValidationException, RemoteException, XavaException {
		return create(
			persistenceProvider, 	
			metaAggregateEjb,
			values,
			metaModelMain,
			mainModel,
			number);
	}


	private void addEntityCollection(
		IPersistenceProvider persistenceProvider,
		MetaModel metaModel,
		Object modelObject,
		MetaCollection metaCollection,
		Collection collection)
		throws XavaException, FinderException {
		Object object = persistenceProvider.toPropertiesContainer(metaModel, modelObject);
		MetaEntity metaEntityReferenced =
			(MetaEntity) metaCollection.getMetaReference().getMetaModelReferenced();
		Iterator it = collection.iterator();
		while (it.hasNext()) {
			Map values = (Map) it.next();
			Object associatedEntity =
				findAssociatedEntity(persistenceProvider, metaEntityReferenced, values);
			executeAddXX(metaModel, object, metaCollection, associatedEntity);
		}
	}


	public void ejbActivate() throws java.rmi.RemoteException {
	}
	public void ejbCreate()
		throws javax.ejb.CreateException, java.rmi.RemoteException {
	}
	public void ejbPassivate() throws java.rmi.RemoteException {
	}
	public void ejbRemove() throws java.rmi.RemoteException {
	}

	private Object getReferencedObject(Object container, String memberName) {
		try {
			PropertiesManager man =
				new PropertiesManager(container);
			Object result = man.executeGet(memberName);
			return result;
		} catch (PropertiesManagerException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_property_error", memberName));
		} catch (InvocationTargetException ex) {
			Throwable th = ex.getTargetException();
			th.printStackTrace();
			throw new EJBException(XavaResources.getString("get_property_error", memberName));
		}
	}

	public javax.ejb.SessionContext getSessionContext() {
		return mySessionCtx;
	}

	
	private Map getValues( 
		IPersistenceProvider persistenceProvider,	
		MetaModel metaModel,
		Map keyValues,
		Map memberNames)
		throws FinderException, XavaException { 
		try {									 
			Map result =
				getValues(
					persistenceProvider,	
					metaModel,
					findEntity(persistenceProvider, (IMetaEjb)metaModel, keyValues),
					memberNames);			
			return result;
		} catch (XavaException ex) {
			ex.printStackTrace();
			throw new XavaException("get_values_error", metaModel.getName());
		}
	}
	
	private MetaModel getMetaModel(String modelName) throws XavaException { 
		int idx = modelName.indexOf('.');			
		if (idx < 0) {
			return MetaComponent.get(modelName).getMetaEntity();
		}
		else {
			String component = modelName.substring(0, idx);
			idx = modelName.lastIndexOf('.'); // just in case we have: MyEntity.MyAggregate.MyAnotherAggregate --> It get MyAnotherAggregate within MyEntity Component
			String aggregate = modelName.substring(idx + 1);
			return MetaComponent.get(component).getMetaAggregate(aggregate);
		}
	}

	private Map getValues(
		IPersistenceProvider persistenceProvider,		
		MetaModel metaModel, 
		Object modelObject,
		Map membersNames) throws XavaException {
		try {
			if (modelObject == null)
				return null;						
			if (membersNames == null) return Collections.EMPTY_MAP;					
			IPropertiesContainer r = persistenceProvider.toPropertiesContainer(metaModel, modelObject);			
			StringBuffer names = new StringBuffer();
			addKey(metaModel, membersNames); // always return the key althought it don't is aunque no se solicit
			removeViewProperties(metaModel, membersNames);			
			Iterator it = membersNames.keySet().iterator();			
			Map result = new HashMap();			
			while (it.hasNext()) {
				String memberName = (String) it.next();
				if (metaModel.containsMetaProperty(memberName) ||
					(metaModel.isKey(memberName) && !metaModel.containsMetaReference(memberName))) {
					names.append(memberName);
					names.append(":");
				} 
				else {
					Map submemberNames = (Map) membersNames.get(memberName);
					if (metaModel.containsMetaReference(memberName)) {						
						result.put(
							memberName,
							getReferenceValues(persistenceProvider, metaModel, modelObject, memberName, submemberNames));
					}
					else if (metaModel.containsMetaCollection(memberName)) {						
						result.put(
							memberName,
							getCollectionValues(persistenceProvider, metaModel, modelObject, memberName, submemberNames));
					} 
					else {
						throw new XavaException("member_not_found", memberName, metaModel.getName());
					}
				}
			}			
			result.putAll(r.executeGets(names.toString()));			
			return result;
		} catch (RemoteException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_values_error", metaModel.getName()));
		}
	}
	
	private Map getMemberNames(MetaModel metaModel) throws XavaException {
		return getMemberNames(metaModel, false);
	}
	
	private Map getMemberNames(MetaModel metaModel, boolean onlyKey) throws XavaException {
		Map memberNames = new HashMap();
		Collection properties = onlyKey?metaModel.getKeyPropertiesNames():metaModel.getPropertiesNames(); 
		Iterator itProperties = properties.iterator();
		while (itProperties.hasNext()) {
			memberNames.put(itProperties.next(), null);
		}
		
		Iterator itReferences = metaModel.getMetaReferences().iterator();
		while (itReferences.hasNext()) {
			MetaReference ref = (MetaReference) itReferences.next();
			if (onlyKey && !ref.isKey()) break;
			memberNames.put(ref.getName(), getMemberNames(ref.getMetaModelReferenced(), true));
		}
		
		return memberNames;		
	}
	
	private void addKey(MetaModel metaModel, Map memberNames) throws XavaException {
		Iterator it = metaModel.getKeyPropertiesNames().iterator();
		while (it.hasNext()) {
			String name = (String) it.next();
			memberNames.put(name, null);
		}		
		Iterator itRef = metaModel.getMetaReferencesKey().iterator();
		while (itRef.hasNext()) {
			MetaReference ref = (MetaReference) itRef.next();
			Map referenceKeyNames = null;
			referenceKeyNames = (Map) memberNames.get(ref.getName());
			if (referenceKeyNames == null) {
				referenceKeyNames = new HashMap();
			}
			addKey(ref.getMetaModelReferenced(), referenceKeyNames);
			memberNames.put(ref.getName(), referenceKeyNames);
		}				
	}
	
	/**
	 * If we send null as <tt>nombresPropiedades</tt> it return a empty Map. <p>
	 */
	private Map getAggregateValues(IPersistenceProvider persistenceProvider, MetaAggregate metaAggregate, Object aggregate, Map memberNames) throws XavaException {
		if (memberNames == null) return Collections.EMPTY_MAP;
		PropertiesManager man = new PropertiesManager(aggregate);
		StringBuffer names = new StringBuffer();
				
		Map result = new HashMap();
		
		Iterator itKeys = metaAggregate.getKeyPropertiesNames().iterator();
		while (itKeys.hasNext()) {
			names.append(itKeys.next());
			names.append(":");			
		}
		
		removeViewProperties(metaAggregate, memberNames); 
		 
		Iterator it = memberNames.keySet().iterator();		
		while (it.hasNext()) {
			String memberName = (String) it.next();
			if (metaAggregate.containsMetaProperty(memberName)) {
				names.append(memberName);
				names.append(":");
			} else
				if (metaAggregate.containsMetaReference(memberName)) {
					Map submemberNames = (Map) memberNames.get(memberName);
					result.put(
						memberName,
						getReferenceValues(persistenceProvider, metaAggregate, aggregate, memberName, submemberNames));
				} else {
					throw new XavaException("member_not_found", memberName, metaAggregate.getName());
				}
		}
		try {
			result.putAll(man.executeGets(names.toString()));
		} catch (PropertiesManagerException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("get_values_error", metaAggregate.getName()));
		} catch (InvocationTargetException ex) {
			Throwable th = ex.getTargetException();
			th.printStackTrace();
			throw new EJBException(XavaResources.getString("get_values_error", metaAggregate.getName()));
		}
		return result;
	}


	/**
	 * If <tt>memberNames</tt> is null then return a empty map.
	 */
	private Map getAssociatedEntityValues(IPersistenceProvider persistenceProvider, MetaEntity metaEntity, Object modelObject, Map memberNames) throws XavaException {
		if (memberNames == null) return new HashMap();
		Map result = getValues(persistenceProvider, metaEntity, modelObject, memberNames);
		return result;
	}

	private Map getReferenceValues(
		IPersistenceProvider persistenceProvider,	
		MetaModel metaModel,
		Object model,
		String memberName,
		Map submembersNames) throws XavaException {
		try {			
			MetaReference r = metaModel.getMetaReference(memberName);
			Object object = getReferencedObject(model, memberName);
			if (r.isAggregate()) {
				return getAggregateValues(persistenceProvider, (MetaAggregate) r.getMetaModelReferenced(), object, submembersNames);
			} 
			else {				
				return getAssociatedEntityValues(persistenceProvider, (MetaEntity) r.getMetaModelReferenced(), object, submembersNames);
			}
		} catch (XavaException ex) {
			ex.printStackTrace();
			throw new XavaException("get_reference_error", memberName, metaModel.getName());
		}
	}
	
	private Collection getCollectionValues(
		IPersistenceProvider persistenceProvider,	
		MetaModel metaModel,
		Object modelObject,
		String memberName,
		Map memberNames) throws XavaException {
		try {
			MetaCollection c = metaModel.getMetaCollection(memberName);
			Object object = getReferencedObject(modelObject, memberName);
			return getCollectionValues( persistenceProvider,
					c.getMetaReference().getMetaModelReferenced(),
					c.isAggregate(),	object, memberNames);
		} catch (XavaException ex) {
			ex.printStackTrace();
			throw new XavaException("get_collection_elements_error", memberName, metaModel.getName());
		}
	}
	
	private Collection getCollectionValues(
		IPersistenceProvider persistenceProvider,	
		MetaModel metaModel,
		boolean aggregate,
		Object elements, Map memberNames) throws XavaException {
		Collection result = new ArrayList();
		Enumeration enum = null;
		if (elements instanceof Enumeration) {
			enum = (Enumeration) elements;
		}
		else if (elements instanceof Collection) {
			enum = Collections.enumeration((Collection) elements);
		}
		else {
			String collectionType = elements == null?"null":elements.getClass().getName();
			throw new XavaException("collection_type_not_supported", collectionType);
		}		
		while (enum.hasMoreElements()) {
			Object object = enum.nextElement();			
			result.add(getValues(persistenceProvider, metaModel, object, memberNames));
		}
		return result;
	}


	private Object instanceAggregate(IPersistenceProvider persistenceProvider, MetaAggregateBean metaAggregate, Map values)
		throws ValidationException, XavaException {
		try {
			Object object = Class.forName(metaAggregate.getBeanClass()).newInstance();
			PropertiesManager man = new PropertiesManager(object);			
			removeViewProperties(metaAggregate, values);
			values = convertSubmapsInObject(persistenceProvider, metaAggregate, values, false);
			man.executeSets(values);
			return object;
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("instantiate_error", metaAggregate.getBeanClass()));
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("instantiate_error", metaAggregate.getBeanClass()));
		} catch (InstantiationException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("instantiate_error", metaAggregate.getBeanClass()));
		} catch (InvocationTargetException ex) {
			throwsValicationException(
				ex, XavaResources.getString("assign_values_error", metaAggregate.getBeanClass(), ex.getLocalizedMessage())); 				
			throw new EJBException(); // Never
		} catch (PropertiesManagerException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("assign_values_error", metaAggregate.getBeanClass(), ex.getLocalizedMessage()));
		}
	}

	private void throwsValicationException(
		InvocationTargetException ex,
		String ejbmessage)
		throws ValidationException {
		Throwable th = ex.getTargetException();
		if (th instanceof ValidationException) {
			throw (ValidationException) th;
		}
		th.printStackTrace();
		throw new EJBException(ejbmessage);
	}

	private Object mapToReferencedObject(
		IPersistenceProvider persistenceProvider,	
		MetaModel metaModel,
		String memberName,
		Map values)
		throws ValidationException, XavaException {
		MetaReference r = null;
		try {
			if (Maps.isEmpty(values)) return null;			
			r = metaModel.getMetaReference(memberName);
			if (r.isAggregate()) {
				return instanceAggregate(persistenceProvider, (MetaAggregateBean) r.getMetaModelReferenced(), values);
			} else {
				return findAssociatedEntity(persistenceProvider, (MetaEntity) r.getMetaModelReferenced(), values);
			}
		} catch (FinderException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("map_to_reference_error",
					r.getName(),					
					metaModel.getName(),					
					memberName));
		} catch (XavaException ex) {
			throw ex;
		}
	}

	private Object findAssociatedEntity(IPersistenceProvider persistenceProvider, MetaEntity metaEntity, Map values)
		throws FinderException, XavaException {
		Map keyValues = extractKeyValues(metaEntity, values);
		return findEntity(persistenceProvider, metaEntity.getName(), keyValues);
	}


	private Map extractKeyValues(MetaEntity metaEntity, Map values)
		throws XavaException {
		return metaEntity.extractKeyValues(values);
	}

	private void removeKeyFields(MetaModel metaModel, Map values)
		throws XavaException {
		Iterator keys = metaModel.getKeyPropertiesNames().iterator();
		while (keys.hasNext()) {
			values.remove(keys.next());
		}
		Iterator itRef = metaModel.getMetaReferencesKey().iterator();
		while (itRef.hasNext()) {
			MetaReference ref = (MetaReference) itRef.next();
			values.remove(ref.getName());
		}		
	}

	private void removeReadOnlyFields(MetaModel metaModel, Map values)
		throws XavaException {
		Iterator toRemove = metaModel.getOnlyReadPropertiesNames().iterator();
		while (toRemove.hasNext()) {
			values.remove(toRemove.next());
		}
	}
		
	private void removeCalculatedFields(MetaModel metaModel, Map values)
		throws XavaException {
		Iterator toRemove = metaModel.getCalculatedPropertiesNames().iterator();
		while (toRemove.hasNext()) {
			values.remove(toRemove.next());
		}
	}
	
	private void removeViewProperties(MetaModel metaModel, Map values)
		throws XavaException {
		Iterator toRemove = metaModel.getViewPropertiesNames().iterator();
		while (toRemove.hasNext()) {
			values.remove(toRemove.next());
		}
	}	
		
	private void remove(IPersistenceProvider persistenceProvider, MetaModel metaModel, Map keyValues)
		throws RemoveException, ValidationException, XavaException {
		try {			
			Object object = findEntity(persistenceProvider, (IMetaEjb)metaModel, keyValues);
			remove(persistenceProvider, metaModel, object);
		} catch (FinderException ex) {
			ex.printStackTrace();
			throw new RemoveException(XavaResources.getString("remove_error",
				metaModel.getName(), ex.getLocalizedMessage()));
		}		
	}
		
	private void remove(IPersistenceProvider persistenceProvider, MetaModel metaModel, Object modelObject)
		throws RemoveException, ValidationException, XavaException {
		try {
			Messages errors = validateOnRemove(metaModel, modelObject);
			if (!errors.isEmpty()) {
				throw new ValidationException(errors);
			}
			if (!metaModel.getMetaCollectionsAgregate().isEmpty()) {
				removeAllAggregateCollections(persistenceProvider, metaModel, modelObject);
			}
			persistenceProvider.remove(metaModel, modelObject);			
		} catch (ValidationException ex) {
			throw ex;					
		} catch (XavaException ex) {
			ex.printStackTrace();
			throw new XavaException("remove_error", metaModel.getName(), ex.getLocalizedMessage());				
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("remove_error",
				metaModel.getName(), ex.getLocalizedMessage()));
		}
	}
	
	private Messages validateOnRemove(MetaModel metaModel, Object modelObject) throws Exception {		
		Messages errors = new Messages();
		Iterator it = metaModel.getMetaValidatorsRemove().iterator();
		while (it.hasNext()) {
			MetaValidator metaValidator = (MetaValidator) it.next(); 
			IRemoveValidator validator = metaValidator.getRemoveValidator();
			validator.setEntity(modelObject);
			validator.validate(errors);			
		}				 		
		return errors;		
	}
		
	public void setSessionContext(javax.ejb.SessionContext ctx)
		throws java.rmi.RemoteException {
		mySessionCtx = ctx;
	}
		
	private void setValues(IPersistenceProvider persistenceProvider, MetaModel metaModel, Map keyValues, Map values)
		throws FinderException, ValidationException, XavaException {		
		try {			
			removeKeyFields(metaModel, values);			
			removeReadOnlyFields(metaModel, values);
			removeViewProperties(metaModel, values);
			Map collections = extractCollections(metaModel, values);
			validate(persistenceProvider, metaModel, values, keyValues, null);			
			Object entity = findEntity(persistenceProvider, (IMetaEjb) metaModel, keyValues);			
			IPropertiesContainer r = persistenceProvider.toPropertiesContainer(metaModel, entity);			
			r.executeSets(convertSubmapsInObject(persistenceProvider, metaModel, values, XavaPreferences.getInstance().isEJB2Persistence()));			
			if (collections != null) {
				modifyCollections(persistenceProvider, metaModel, entity, collections);
			}			
		} catch (ValidationException ex) {
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("assign_values_error", metaModel.getName(), ex.getLocalizedMessage()); 
		}
	}
	

	private void validate(
		IPersistenceProvider persistenceProvider,	
		Messages errors,
		MetaModel metaModel,
		String memberName,
		Object values) throws XavaException {			
		try {			
			if (metaModel.containsMetaProperty(memberName)) {
				metaModel.getMetaProperty(memberName).validate(errors, values);
			} else
				if (metaModel.containsMetaReference(memberName)) {
					MetaReference ref = metaModel.getMetaReference(memberName); 
					MetaModel referencedModel = ref.getMetaModelReferenced();
					Map mapValues = (Map) values;					
					if (hasValue(mapValues)) {
						if (ref.isAggregate()) validate(persistenceProvider, errors, referencedModel, mapValues, mapValues, null);
					} else
						if (metaModel
							.getMetaReference(memberName)
							.isRequired()) {
							errors.add("required", memberName, metaModel.getName());
						}
						
				} else if (metaModel.containsMetaCollection(memberName)) {
					// It never happens this way 
					metaModel.getMetaCollection(memberName).validate(errors, values, null, null);
				} else if (metaModel.containsMetaPropertyView(memberName)) { 										
					metaModel.getMetaPropertyView(memberName).validate(errors, values);									
				} else {					
					System.err.println(XavaResources.getString("not_validate_member_warning", memberName, metaModel.getName()));
				}
		} catch (XavaException ex) {
			ex.printStackTrace();
			throw new XavaException("validate_error", memberName, metaModel.getName());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("validate_error", memberName, metaModel.getName()));				
		}
	}
	
	private boolean hasValue(Map values) {
		if (values == null) return false;
		Iterator it = values.values().iterator();
		while (it.hasNext()) {
			Object v = it.next();
			if (v == null) continue;
			if (v instanceof String && ((String) v).trim().equals("")) continue;
			if (v instanceof Number && ((Number)  v).intValue()==0) continue;
			return true;
		}		
		return false;
	}
	
	private void validate(IPersistenceProvider persistenceProvider, Messages errors, MetaModel metaModel, Map values, Map keyValues, Object containerKey)	  
		throws XavaException {		
		Iterator it = values.entrySet().iterator();		
		while (it.hasNext()) {
			Map.Entry en = (Map.Entry) it.next();
			String name = (String) en.getKey();
			Object value = en.getValue();
			validate(persistenceProvider, errors, metaModel, name, value);
		}
		if (metaModel.containsValidadors()) {
			validateWintModelValidator(persistenceProvider, errors, metaModel, values, keyValues, containerKey);			
		}
	}
	
	private void validateWintModelValidator(IPersistenceProvider persistenceProvider, Messages errors, MetaModel metaModel, Map values, Map keyValues, Object containerKey) throws XavaException {				
		try {
			String containerReferenceName = Strings.firstLower(metaModel.getMetaModelContainer().getName());
			Iterator itValidators = metaModel.getMetaValidators().iterator();
			while (itValidators.hasNext()) {
				MetaValidator metaValidator = (MetaValidator) itValidators.next();
				Iterator itSets =  metaValidator.getMetaSetsWithoutValue().iterator();
				IValidator v = metaValidator.createValidator();
				PropertiesManager mp = new PropertiesManager(v);		
				while (itSets.hasNext()) {
					MetaSet set = (MetaSet) itSets.next();					
					Object value = values.get(set.getPropertyNameFrom());
					if (value == null && !values.containsKey(set.getPropertyNameFrom())) {
						if (keyValues != null) { 
							Map memberName = new HashMap();
							memberName.put(set.getPropertyNameFrom(), null);
							Map memberValue = getValues(persistenceProvider, metaModel, keyValues, memberName);
							value = memberValue.get(set.getPropertyNameFrom());
						}											
					}
					if (set.getPropertyNameFrom().equals(containerReferenceName)) {
						if (containerKey == null) {							
							Object object = findEntity(persistenceProvider, ((IMetaEjb)metaModel), keyValues);
							value = Objects.execute(object, "get" + metaModel.getMetaModelContainer().getName());
						}
						else {
							IMetaEjb containerReference = (IMetaEjb) metaModel.getMetaModelContainer();
							try {							
								value = persistenceProvider.find(containerReference, containerKey);
							}
							catch (ObjectNotFoundException ex) {
								value = null;
							}			
						}
					}
					else if (metaModel.containsMetaReference(set.getPropertyNameFrom())) {
						MetaReference ref = metaModel.getMetaReference(set.getPropertyNameFrom());
						if (ref.isAggregate()) {
							value = mapToReferencedObject(persistenceProvider, metaModel, set.getPropertyNameFrom(), (Map) value);
						}
						else {
							IMetaEjb referencedEntity = (IMetaEjb) ref.getMetaModelReferenced();
							try {
								value = findEntity(persistenceProvider, referencedEntity, (Map) value);
							}
							catch (ObjectNotFoundException ex) {
								value = null;
							}																															
						}						
					}
					mp.executeSet(set.getPropertyName(), value);									
				}			
				v.validate(errors);
			}		
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("validate_model_error", metaModel.getName());
		}
	}
	
	private void validate(IPersistenceProvider persistenceProvider, MetaModel metaModel, Map values, Map keyValues, Object containerKey)
		throws ValidationException, XavaException, RemoteException {
		Messages errors = new Messages();		
		validate(persistenceProvider, errors, metaModel, values, keyValues, containerKey);		
		if (errors.contains()) {
			throw new ValidationException(errors);
		}
	}

	private void validateExistRequired(Messages errors, MetaModel metaModel, Map values)
		throws XavaException {		
		Iterator it = metaModel.getRequiredMemberNames().iterator();		
		while (it.hasNext()) {
			String name = (String) it.next();			
			if (!values.containsKey(name)) {				
				errors.add("required", name, metaModel.getName());
			}
		}
	}
				
	private Object findEntity(IPersistenceProvider persistenceProvider, IMetaEjb metaEntity, Map keyValues)	throws FinderException, XavaException {
		return persistenceProvider.find(metaEntity, keyValues);
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

	private void executeAddXX(
		MetaModel metaModel,
		Object modelObject,
		MetaCollection metaCollection,
		Object argument)
		throws XavaException {
		IMetaEjb metaEjb = null;	
		try {	
			metaEjb = (IMetaEjb) metaCollection.getMetaReference().getMetaModelReferenced();
		}
		catch (ClassCastException ex) {
			throw new XavaException("only_ejb_collections_error");
		}
		String collectionName = Strings.firstUpper(metaCollection.getName()); 
		try {
			execute(metaModel, modelObject, 
				"addTo" + collectionName, 
				metaEjb.getRemoteClass(), argument);
		}
		catch (NoSuchMethodException ex) {
			try {
				execute(metaModel, modelObject, 
					"add" + collectionName, 
					metaEjb.getRemoteClass(), argument);			
			}
			catch (NoSuchMethodException ex2) {
				throw new XavaException("add_method_expected", 
						metaModel.getPropertiesClass(),
						collectionName,
						argument.getClass().getName());
			}
		}
	}

	private void executeRemoveXX(
		MetaModel metaModel,
		Object modelObject,
		MetaCollection metaCollection,
		Object argument)
		throws XavaException {
			
		IMetaEjb metaEjb = null;	
		try {	
			metaEjb = (IMetaEjb) metaCollection.getMetaReference().getMetaModelReferenced();
		}
		catch (ClassCastException ex) {
			throw new XavaException("only_ejb_collections_error");
		}
		String collectionName = Strings.firstUpper(metaCollection.getName()); 
		try {			
			execute(metaModel, modelObject, 
				"removeFrom" + collectionName, 
				metaEjb.getRemoteClass(), argument);
		}
		catch (NoSuchMethodException ex) {
			try {
				execute(metaModel, modelObject, 
					"remove" + collectionName, 
					metaEjb.getRemoteClass(), argument);			
			}
			catch (NoSuchMethodException ex2) {
				throw new XavaException("remove_method_expected",				
					metaModel.getPropertiesClass(),
					collectionName, 					
					argument.getClass().getName()); 				
			}
		}			
	}

	/**
	 * Execute the method (add, remove, etc) on the member. <p>
	 */
	private void execute(
		MetaModel metaModel,
		Object modelObject,
		String methodName,
		Class argumentClass,
		Object argument)
		throws NoSuchMethodException, XavaException  {		
		if (argument == null) {
			throw new XavaException("not_null_argument_error",				
					modelObject.getClass().getName(),
					methodName);
		}
		try {
			Objects.execute(
				modelObject,
				methodName,
				argumentClass,
				argument);
		} catch (NoSuchMethodException ex) {
			throw new NoSuchMethodException(XavaResources.getString("method_expected",				
					metaModel.getPropertiesClass(),					
					methodName+ "("	+ argument.getClass().getName()+ ")"));
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("method_execution_error",				
					metaModel.getPropertiesClass().getName(),					
					methodName,					
					ex.getLocalizedMessage());
		}
	}

	private void rollback (IPersistenceProvider pp) {
		if (getSessionContext() != null) getSessionContext().setRollbackOnly();
		pp.rollback();
	}
		
	private void executePostremoveCollectionElement(IPersistenceProvider persistenceProvider, MetaModel metaModel, Map keyValues, MetaCollection metaCollection) throws FinderException, ValidationException, XavaException {
		Iterator itCalculators = metaCollection.getMetaCalculatorsPostRemove().iterator();
		while (itCalculators.hasNext()) {
			MetaCalculator metaCalculator = (MetaCalculator) itCalculators.next();			
			ICalculator calculator = metaCalculator.createCalculator();
			PropertiesManager mp = new PropertiesManager(calculator);
			Collection sets =  metaCalculator.getMetaSetsWithoutValue();
			if (metaCalculator.containsMetaSetsWithoutValue()) {
				Map neededPropertyNames = new HashMap();
				Iterator itSets = sets.iterator();
				while (itSets.hasNext()) {
					MetaSet set = (MetaSet) itSets.next();
					neededPropertyNames.put(set.getPropertyNameFrom(), null);
				}												
				Map values = getValues(persistenceProvider, metaModel, keyValues, neededPropertyNames);
				
				itSets = sets.iterator();											
				while (itSets.hasNext()) {
					MetaSet set = (MetaSet) itSets.next();
					Object value = values.get(set.getPropertyNameFrom());
					if (value == null && !values.containsKey(set.getPropertyNameFrom())) {
						if (keyValues != null) { 
							Map memberName = new HashMap();
							memberName.put(set.getPropertyNameFrom(), null);
							Map memberValue = getValues(persistenceProvider, metaModel, keyValues, memberName);
							value = memberValue.get(set.getPropertyNameFrom());
						}											
					}
						
					if (metaModel.containsMetaReference(set.getPropertyNameFrom())) {
						MetaReference ref = metaModel.getMetaReference(set.getPropertyNameFrom());
						if (ref.isAggregate()) {
							value = mapToReferencedObject(persistenceProvider, metaModel, set.getPropertyNameFrom(), (Map) value);
						}
						else {
							IMetaEjb referencedEntity = (IMetaEjb) ref.getMetaModelReferenced();
							try {
								value = findEntity(persistenceProvider, referencedEntity, (Map) value);
							}
							catch (ObjectNotFoundException ex) {
								value = null;
							}
																																
						}						
					}
					try {			
						mp.executeSet(set.getPropertyName(), value);
					}
					catch (Exception ex) {
						ex.printStackTrace();
						throw new XavaException("calculator_property_error", value, set.getPropertyName());
					}									
				}
			}			
			
			if (calculator instanceof IEntityCalculator) {
				Object entity = findEntity(persistenceProvider, (IMetaEjb) metaModel, keyValues);
				try {
					((IEntityCalculator) calculator).setEntity(entity);
				}
				catch (Exception ex) {
					ex.printStackTrace();
					throw new XavaException("assign_entity_to_calculator_error", metaModel.getName(), keyValues);
				}									
				
			}
			try {
				calculator.calculate();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("postremove_error", metaModel.getName(), keyValues));
			}
		}				
	}
	
	private Map convertSubmapsInObject(IPersistenceProvider persistenceProvider, MetaModel metaModel, Map values,
			boolean referencesAsKey) throws ValidationException, XavaException {
		Map result = new HashMap();
		Iterator it = values.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry en = (Map.Entry) it.next();
			String memberName = (String) en.getKey();
			Object value = null;
			if (metaModel.containsMetaProperty(memberName)) {
				value = en.getValue();
			}
			else if (metaModel.containsMetaReference(memberName)) {
				MetaReference ref = metaModel.getMetaReference(memberName);
				if (!referencesAsKey || ref.isAggregate()) { 
					value = mapToReferencedObject(persistenceProvider, metaModel, memberName, (Map) en
							.getValue());
				}
				else {
					MetaEntityEjb referencedEntity = (MetaEntityEjb) ref
							.getMetaModelReferenced();
					memberName = memberName + "Key";
					value = referencedEntity.obtainPrimaryKeyFromKey((Map) en.getValue());
				}
			}
			else if (metaModel.getMapping().hasPropertyMapping(memberName)) {
				value = en.getValue();
			}
			else {
				throw new XavaException("member_not_found", memberName, metaModel
						.getName());
			}
			result.put(memberName, value);
		}
		return result;
	}
	
	private Object findEntity(IPersistenceProvider persistenceProvider,	String modelName, Map keyValues)
		throws FinderException {
		try {
			return findEntity(persistenceProvider, (IMetaEjb) getMetaModel(modelName), keyValues);			
		} catch (FinderException ex) {
			throw ex;
		} catch (ElementNotFoundException ex) {
			throw new EJBException(XavaResources.getString("model_not_found", modelName));
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("find_error", modelName));
		}
	}

	private IPersistenceProvider createPersistenceProvider() throws RemoteException {
		try {
			return (IPersistenceProvider) Class.forName(XavaPreferences.getInstance().getPersistenceProviderClass()).newInstance();
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException(XavaResources.getString("persistence_provider_creation_error"));
		}
	}

}


