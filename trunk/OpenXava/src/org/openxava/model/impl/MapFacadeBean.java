package org.openxava.model.impl;

import java.lang.reflect.*;
import java.rmi.*;
import java.util.*;

import javax.ejb.*;



import org.apache.commons.logging.*;
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

public class MapFacadeBean implements IMapFacadeImpl, SessionBean {
	
	private static Log log = LogFactory.getLog(MapFacadeBean.class);
	private javax.ejb.SessionContext sessionContext = null;
	private final static long serialVersionUID = 3206093459760846163L;
	private static IPersistenceProvider persistenceProvider;
	
	
	public Object create(String user, String modelName, Map values)
		throws CreateException, XavaException, ValidationException, RemoteException {		
		Users.setCurrent(user);
		values = Maps.recursiveClone(values); 		
		try {
			MetaModel metaModel = getMetaModel(modelName);	
			getPersistenceProvider().begin(); 
			Object result = create(metaModel, values, null, null, 0);
			getPersistenceProvider().commit();
			return result;
		} 
		catch (CreateException ex) {
			rollback();
			throw ex;
		}
		catch (ValidationException ex) {
			rollback();
			throw ex;
		}
		catch (XavaException ex) {
			rollback();
			throw ex;
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(ex.getMessage());
		}
	}
	
	public Map getValues(
			String user, 
			String modelName,
			Map keyValues,
			Map membersNames)
			throws FinderException, XavaException, RemoteException {		
		Users.setCurrent(user);
		keyValues = Maps.recursiveClone(keyValues); 
		membersNames = Maps.recursiveClone(membersNames); 		
		try {			
			getPersistenceProvider().begin();
			Map result = getValuesImpl(modelName, keyValues, membersNames);			
			getPersistenceProvider().commit();			
			return result;
		} 
		catch (ObjectNotFoundException ex) {
			getPersistenceProvider().commit();
			throw ex;
		}
		catch (FinderException ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw ex;
		}
		catch (XavaException ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw ex;
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(ex.getMessage());
		}
	}
	
	public Map getValuesByAnyProperty( 
			String user, 
			String modelName,
			Map searchingValues,
			Map membersNames)
			throws FinderException, XavaException, RemoteException {		
		Users.setCurrent(user);
		searchingValues = Maps.recursiveClone(searchingValues); 
		membersNames = Maps.recursiveClone(membersNames); 		
		try {			
			getPersistenceProvider().begin();
			Map result = getValuesByAnyPropertyImpl(modelName, searchingValues, membersNames);			
			getPersistenceProvider().commit();			
			return result;
		} 
		catch (ObjectNotFoundException ex) {
			getPersistenceProvider().commit();
			throw ex;
		}
		catch (FinderException ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw ex;
		}
		catch (XavaException ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw ex;
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(ex.getMessage());
		}
	}
	
	
	public void delete(String user, String modelName, Map keyValues)
		throws RemoveException, ValidationException, XavaException, RemoteException 
	{		
		Users.setCurrent(user);
		keyValues = Maps.recursiveClone(keyValues); 		
		try {
			MetaModel metaModel = getMetaModel(modelName);	
			getPersistenceProvider().begin();
			remove(metaModel, keyValues);
			getPersistenceProvider().commit();
		}
		catch (RemoveException ex) {
			rollback();
			throw ex;
		}
		catch (ValidationException ex) {
			rollback();
			throw ex;
		}
		catch (XavaException ex) {
			rollback();
			throw ex;
		}
		catch (Exception ex) {
			rollback();
			throw new RemoteException(ex.getMessage());
		}
	}
	
	public void setValues(String user, String modelName, Map keyValues, Map values)
		throws FinderException, ValidationException, XavaException, RemoteException 
	{						
		Users.setCurrent(user);
		keyValues = Maps.recursiveClone(keyValues); 
		values = Maps.recursiveClone(values); 		
		try {
			MetaModel metaModel = getMetaModel(modelName);
			getPersistenceProvider().begin();
			setValues(metaModel, keyValues, values);			
			getPersistenceProvider().commit();
		}
		catch (FinderException ex) {
			rollback();
			throw ex;
		}
		catch (ValidationException ex) {
			rollback();
			throw ex;
		}
		catch (XavaException ex) {
			rollback();
			throw ex;
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(ex.getMessage());
		}
	}

	public Object findEntity(String user, String modelName, Map keyValues)
		throws FinderException, RemoteException {
		Users.setCurrent(user);
		keyValues = Maps.recursiveClone(keyValues); 			
		return findEntity(modelName, keyValues);
	}
	
	public Map createReturningValues(String user, String modelName, Map values) 
		throws CreateException, XavaException, ValidationException, RemoteException {
		Users.setCurrent(user);
		values = Maps.recursiveClone(values); 		
		try {
			getPersistenceProvider().begin();
			Map result = createReturningValues(modelName, values);
			getPersistenceProvider().commit();
			return result;
		}	
		catch (CreateException ex) {
			rollback();
			throw ex;
		}
		catch (ValidationException ex) {
			rollback();
			throw ex;
		}
		catch (XavaException ex) {
			rollback();
			throw ex;
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(ex.getMessage());
		}		
	}
	
	public Map createReturningKey(String user, String modelName, Map values) 
		throws CreateException, XavaException, ValidationException, RemoteException {
		Users.setCurrent(user);
		values = Maps.recursiveClone(values); 			
		try {				
			getPersistenceProvider().begin();
			Map result = createReturningKey(modelName, values);
			getPersistenceProvider().commit();
			return result;
		}	
		catch (CreateException ex) {
			rollback();
			throw ex;
		}
		catch (ValidationException ex) {
			rollback();
			throw ex;
		}
		catch (XavaException ex) {
			rollback();
			throw ex;
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(ex.getMessage());
		}
	}
	
	public Object createAggregate(String user, String modelName, Map containerKeyValues, int counter, Map values)  
		throws CreateException,ValidationException, XavaException, RemoteException 
	{		
		Users.setCurrent(user);
		containerKeyValues = Maps.recursiveClone(containerKeyValues); 
		values = Maps.recursiveClone(values); 		
		try {		
			getPersistenceProvider().begin(); 
			Object result = createAggregate(modelName, containerKeyValues, counter, values);
			getPersistenceProvider().commit();
			return result;
		}	
		catch (CreateException ex) {
			rollback();
			throw ex;
		}
		catch (ValidationException ex) {
			rollback();
			throw ex;
		}
		catch (XavaException ex) {
			rollback();
			throw ex;
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(ex.getMessage());
		}
	}
	
	public Object createAggregate(String user, String modelName, Object container, int counter, Map values)  
		throws CreateException,ValidationException, XavaException, RemoteException
	{		
		Users.setCurrent(user);
		values = Maps.recursiveClone(values); 			
		try {		
			getPersistenceProvider().begin();
			Object result = createAggregate(modelName, container, counter, values);
			getPersistenceProvider().commit();
			return result;
		}	
		catch (CreateException ex) {
			rollback();
			throw ex;
		}
		catch (ValidationException ex) {
			rollback();
			throw ex;
		}
		catch (XavaException ex) {
			rollback();
			throw ex;
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(ex.getMessage());
		}
	}
	
	public Map createAggregateReturningKey(String user, String modelName, Map containerKeyValues, int counter, Map values)  
		throws CreateException,ValidationException, XavaException, RemoteException 
	{		
		Users.setCurrent(user);
		containerKeyValues = Maps.recursiveClone(containerKeyValues); 
		values = Maps.recursiveClone(values); 		
		try {			
			getPersistenceProvider().begin();
			Map result = createAggregateReturningKey(modelName, containerKeyValues, counter, values);
			getPersistenceProvider().commit();
			return result;
		}	
		catch (CreateException ex) {
			rollback();
			throw ex;
		}
		catch (ValidationException ex) {
			rollback();
			throw ex;
		}
		catch (XavaException ex) {
			rollback();
			throw ex;
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(ex.getMessage());
		}
	}

	public Map getValues( 
		String user, 
		String modelName,
		Object modelObject,
		Map memberNames) throws XavaException, RemoteException  
		 {				
		Users.setCurrent(user);
		memberNames = Maps.recursiveClone(memberNames); 		
		try {	
			getPersistenceProvider().begin();
			Map result = getValues(modelName, modelObject, memberNames);
			getPersistenceProvider().commit();
			return result;
		}	
		catch (XavaException ex) {
			rollback();
			throw ex;
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(ex.getMessage());
		}
	}
	

	public Messages validate(String user, String modelName, Map values) throws XavaException, RemoteException {
		Users.setCurrent(user);
		values = Maps.recursiveClone(values); 				
		try {			
			getPersistenceProvider().begin();
			Messages result = validate(modelName, values, false);
			getPersistenceProvider().commit();
			return result;
		}	
		catch (XavaException ex) {
			rollback();
			throw ex;
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(ex.getMessage());
		}
	}
	
	public void removeCollectionElement(String user, String modelName, Map keyValues, String collectionName, Map collectionElementKeyValues)   
		throws FinderException,	ValidationException, XavaException, RemoveException, RemoteException 
	{
		Users.setCurrent(user);
		keyValues = Maps.recursiveClone(keyValues); 
		collectionElementKeyValues = Maps.recursiveClone(collectionElementKeyValues); 		
		try {		
			getPersistenceProvider().begin();
			removeCollectionElement(modelName, keyValues, collectionName, collectionElementKeyValues);
			getPersistenceProvider().commit();
		} 
		catch (FinderException ex) {
			rollback();
			throw ex;
		}
		catch (ValidationException ex) {
			rollback();
			throw ex;
		}
		catch (RemoveException ex) {
			rollback();
			throw ex;
		}
		catch (XavaException ex) {
			rollback();
			throw ex;
		}
		catch (Exception ex) {
			rollback();
			throw new RemoteException(ex.getMessage());
		}						
	}	
		
	private void removeCollectionElement(String modelName, Map keyValues, String collectionName, Map collectionElementKeyValues) 
		throws FinderException,	ValidationException, XavaException, RemoveException, RemoteException, InvocationTargetException, PropertiesManagerException 
	{
		MetaModel parentMetaModel = getMetaModel(modelName);
		MetaCollection metaCollection = parentMetaModel.getMetaCollection(collectionName);
		MetaModel childMetaModel = metaCollection.getMetaReference().getMetaModelReferenced();
		if (metaCollection.isAggregate()) {						
			remove(childMetaModel, collectionElementKeyValues);
		}
		else {
			String refToParent = metaCollection.getMetaReference().getRole();
			if (childMetaModel.containsMetaReference(refToParent)) {
				// If the child contains the reference to its parent we simply update this reference
				Map nullParentKey = new HashMap();
				nullParentKey.put(Strings.firstLower(modelName), null);						
				setValues(childMetaModel, collectionElementKeyValues, nullParentKey);
			}
			else {
				// If not (as in ManyToMany relationship), we update the collection in parent
				Object parent = findEntity(parentMetaModel, keyValues);
				Object child = findEntity(childMetaModel, collectionElementKeyValues);
				PropertiesManager pm = new PropertiesManager(parent);
				Collection collection = (Collection) pm.executeGet(collectionName);
				collection.remove(child);
			}					
		}												
		if (metaCollection.hasPostRemoveCalculators()) {
			executePostremoveCollectionElement(parentMetaModel, keyValues, metaCollection);			
		}						
	}
	
	public void addCollectionElement(String user, String modelName, Map keyValues, String collectionName, Map collectionElementKeyValues)   
		throws FinderException,	ValidationException, XavaException, RemoteException 
	{
		Users.setCurrent(user);
		keyValues = Maps.recursiveClone(keyValues); 
		collectionElementKeyValues = Maps.recursiveClone(collectionElementKeyValues); 		
		try {		
			getPersistenceProvider().begin();
			addCollectionElement(modelName, keyValues, collectionName, collectionElementKeyValues);
			getPersistenceProvider().commit();
		} 
		catch (FinderException ex) {
			rollback();
			throw ex;
		}
		catch (ValidationException ex) {
			rollback();
			throw ex;
		}
		catch (XavaException ex) {
			rollback();
			throw ex;
		}
		catch (Exception ex) {
			rollback();
			throw new RemoteException(ex.getMessage());
		}						
	}	
		
	private void addCollectionElement(String modelName, Map keyValues, String collectionName, Map collectionElementKeyValues) 
		throws FinderException,	ValidationException, XavaException, RemoteException, InvocationTargetException, PropertiesManagerException 
	{		
		MetaModel parentMetaModel = MetaComponent.get(modelName).getMetaEntity();
		MetaCollection metaCollection = parentMetaModel.getMetaCollection(collectionName);		
		String refToParent = metaCollection.getMetaReference().getRole();
		MetaModel childMetaModel = metaCollection.getMetaReference().getMetaModelReferenced();
		
		if (childMetaModel.containsMetaReference(refToParent)) {
			// If the child contains the reference to its parent we simply update this reference
			Map parentKey = new HashMap();
			parentKey.put(refToParent, keyValues);		
			setValues(childMetaModel, collectionElementKeyValues, parentKey);								
		}
		else {
			// If not (as in ManyToMany relationship), we update the collection in parent
			Object parent = findEntity(parentMetaModel, keyValues);
			Object child = findEntity(childMetaModel, collectionElementKeyValues);
			PropertiesManager pm = new PropertiesManager(parent);
			Collection collection = (Collection) pm.executeGet(collectionName);
			collection.add(child);
		}		
	}
		
	private Messages validate(String modelName, Map values, boolean creating) throws XavaException, RemoteException { 			
		MetaEntity metaEntity = (MetaEntity) MetaComponent.get(modelName).getMetaEntity();		
		Messages validationErrors = new Messages(); 				
		validate(validationErrors, metaEntity, values, null, null, creating);
		return validationErrors;
	}
	
	private Map getValues(		 	
		String modelName,
		Object modelObject,
		Map memberNames) throws XavaException, RemoteException
		 {		
		try {			
			MetaModel metaModel = getMetaModel(modelName);
			Map result = getValues(metaModel, modelObject, memberNames);						
			return result;
		} catch (XavaException ex) {
			log.error(ex.getMessage(), ex);
			throw new XavaException("get_values_error", modelName);
		}
	}
	
	private Map createAggregateReturningKey(String modelName, Map containerKeyValues, int counter, Map values) 
		throws CreateException,ValidationException, XavaException, RemoteException 
	{		
		MetaModel metaModel = getMetaModel(modelName);
		MetaModel metaModelContainer = metaModel.getMetaModelContainer();
		try {								
			Object containerKey = getPersistenceProvider().getKey(metaModelContainer, containerKeyValues);
			Object aggregate = createAggregate(metaModel, containerKey, counter, values);						
			return getValues(metaModel, aggregate, getKeyNames(metaModel));			
		}
		catch (ClassCastException ex) {
			throw new XavaException("aggregate_must_be_persistent_for_create", metaModelContainer.getName());					
		}
	}
	
	private Object createAggregate(String modelName, Object container, int counter, Map values) 
		throws CreateException,ValidationException, XavaException, RemoteException
	{		
		MetaModel metaModel = getMetaModel(modelName);		
		return createAggregate(metaModel, container, counter, values);
	}
	
	private Object createAggregate(String modelName, Map containerKeyValues, int counter, Map values) 
		throws CreateException,ValidationException, XavaException, RemoteException 
	{		
		MetaModel metaModel = getMetaModel(modelName);		
		try {					
			Object containerKey = getPersistenceProvider().getContainer(metaModel, containerKeyValues);			
			return createAggregate(metaModel, containerKey, counter, values);
		}
		catch (ClassCastException ex) {
			throw new XavaException("aggregate_must_be_persistent_for_create", metaModel.getMetaModelContainer().getName());					
		}
	}
	
	private Map createReturningKey(String modelName, Map values)
		throws CreateException, XavaException, ValidationException, RemoteException {
		MetaEntity metaEntity = (MetaEntity) MetaComponent.get(modelName).getMetaEntity();
		Object entity = create(metaEntity, values, null, null, 0);
		return getValues(metaEntity, entity, getKeyNames(metaEntity));
	}
	
	private Map createReturningValues(String modelName, Map values)
		throws CreateException, XavaException, ValidationException, RemoteException {
		MetaEntity metaEntity = (MetaEntity) MetaComponent.get(modelName).getMetaEntity();
		Object entity = create(metaEntity, values, null, null, 0);
		getPersistenceProvider().flush(); // to execute calculators
		return getValues(metaEntity, entity, values);
	}
		
	private Map getValuesImpl(	
		String modelName,
		Map keyValues,
		Map membersNames)
		throws FinderException, XavaException, RemoteException {		
		try {			
			MetaModel metaModel = getMetaModel(modelName);						 
			Map result =
				getValues(					 
					metaModel,
					findEntity(modelName, keyValues),
					membersNames); 						
			return result;
		} catch (XavaException ex) {
			log.error(ex.getMessage(), ex);
			throw new XavaException("get_values_error", modelName);
		}
	}
	
	private Map getValuesByAnyPropertyImpl( 	
		String modelName,
		Map keyValues,
		Map membersNames)
		throws FinderException, XavaException, RemoteException {		
		try {
			MetaModel metaModel = getMetaModel(modelName);						 
			Map result =
				getValues(					 
					metaModel,
					findEntityByAnyProperty(modelName, keyValues),
					membersNames); 						
			return result;
		} catch (XavaException ex) {
			log.error(ex.getMessage(), ex);
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
	
	private Object createAggregate(MetaModel metaModel, Object container, int counter, Map values) 
		throws CreateException,ValidationException, XavaException, RemoteException 
	{						
		MetaModel metaModelContainer = metaModel.getMetaModelContainer();								
		int attempts = 0;
		// Loop with 10 attempts, because the counter can be repeated because deleted objects
		do {				 
			try {
				return create(metaModel, values, metaModelContainer, container, counter);
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
		MetaModel metaModel,
		Map values,
		MetaModel metaModelContainer,
		Object containerKey,
		int number)
		throws CreateException, ValidationException, XavaException, RemoteException {						
		try {			
			//removeReadOnlyFields(metaEjb, values); // not remove the read only fields because it maybe needed initialized on create
			removeCalculatedFields(metaModel, values); 			
			Messages validationErrors = new Messages(); 
			validateExistRequired(validationErrors, metaModel, values);						
			validate(validationErrors, metaModel, values, null, containerKey, true);
			removeViewProperties(metaModel, values); 
			if (validationErrors.contains()) {
				throw new ValidationException(validationErrors);			
			}
			updateReferencedEntities(metaModel, values); 
			Map convertedValues = convertSubmapsInObject(metaModel, values, XavaPreferences.getInstance().isEJB2Persistence());
			Object newObject = null;
			if (metaModelContainer == null) {
				newObject = getPersistenceProvider().create(metaModel, convertedValues);
			} else {				
				newObject = getPersistenceProvider().createAggregate(					
						metaModel,
						convertedValues,
						metaModelContainer,
						containerKey,
						number);
			}			
			// Collections are not managed
			return newObject;
		} catch (ValidationException ex) {
			throw ex;
		} catch (DuplicateKeyException ex) {
			log.error(ex.getMessage(), ex);
			throw new DuplicateKeyException(
				XavaResources.getString("no_create_exists", metaModel.getName()));	
		} catch (CreateException ex) {
			log.error(ex.getMessage(), ex);
			throw new CreateException(XavaResources.getString("create_error", metaModel.getName()));		
		} catch (RemoteException ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(XavaResources.getString("create_error", metaModel.getName()));
		} catch (XavaException ex) {
			log.error(ex.getMessage(), ex);
			throw new XavaException("create_error", metaModel.getName());
		}
	}

	private void updateReferencedEntities(MetaModel metaModel, Map values) throws XavaException, RemoteException, CreateException, ValidationException {
		for (Iterator it = metaModel.getMetaReferencesToEntity().iterator(); it.hasNext(); ) {
			MetaReference ref = (MetaReference) it.next();			
			Map referenceValues = (Map) values.get(ref.getName());			
			if (referenceValues != null) {
				int hiddenKeyNotPresent = getHiddenKeyNotPressent(ref, referenceValues);
				if (referenceValues.size() + hiddenKeyNotPresent > ref.getMetaModelReferenced().getMetaMembersKey().size()) {
					try {									
						setValues(ref.getMetaModelReferenced(), new HashMap(referenceValues), new HashMap(referenceValues));
					}
					catch (FinderException ex) {
						referenceValues = createReturningValues(ref.getMetaModelReferenced().getName(), new HashMap(referenceValues));
						values.put(ref.getName(), referenceValues);
					}
				}					
			}			
		}
	}

	private int getHiddenKeyNotPressent(MetaReference ref, Map referenceValues) throws XavaException {
		int hiddenKeyNotPresent = 0;
		for (Iterator itKeys = ref.getMetaModelReferenced().getMetaMembersKey().iterator(); itKeys.hasNext(); ) {
			MetaMember key = (MetaMember) itKeys.next();					
			if (key.isHidden()) {
				if (!referenceValues.containsKey(key.getName())) {
					hiddenKeyNotPresent++;
				}
			}
		}
		return hiddenKeyNotPresent;
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

	private Object getReferencedObject(Object container, String memberName) throws XavaException, RemoteException {
		try {
			PropertiesManager man =
				new PropertiesManager(container);
			Object result = man.executeGet(memberName);
			return result;
		} catch (PropertiesManagerException ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(XavaResources.getString("get_property_error", memberName));
		} catch (InvocationTargetException ex) {
			Throwable th = ex.getTargetException();
			log.error(th.getMessage(), th);
			rollback();
			throw new RemoteException(XavaResources.getString("get_property_error", memberName));
		}
	}

	public javax.ejb.SessionContext getSessionContext() {
		return sessionContext;
	}

	
	private Map getValues( 	
		MetaModel metaModel,
		Map keyValues,
		Map memberNames)
		throws FinderException, XavaException, RemoteException { 
		try {									 
			Map result =
				getValues(	
					metaModel,
					findEntity(metaModel, keyValues),
					memberNames);			
			return result;
		} catch (XavaException ex) {
			log.error(ex.getMessage(), ex);
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
		MetaModel metaModel, 
		Object modelObject,
		Map membersNames) throws XavaException, RemoteException {
		try {
			if (modelObject == null)
				return null;						
			if (membersNames == null) return Collections.EMPTY_MAP;			 
			IPropertiesContainer r = getPersistenceProvider().toPropertiesContainer(metaModel, modelObject);			
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
							getReferenceValues(metaModel, modelObject, memberName, submemberNames));
					}
					else if (metaModel.containsMetaCollection(memberName)) {						
						result.put(
							memberName,
							getCollectionValues(metaModel, modelObject, memberName, submemberNames));
					} 
					else {
						throw new XavaException("member_not_found", memberName, metaModel.getName());
					}
				}
			}			
			result.putAll(r.executeGets(names.toString()));			
			return result;
		} catch (RemoteException ex) {			
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(XavaResources.getString("get_values_error", metaModel.getName()));
		}
	}
	
	public Map getKeyValues(String user, String modelName, Object entity) throws RemoteException, XavaException {
		Users.setCurrent(user);
		MetaModel metaModel = getMetaModel(modelName);
		return getValues(metaModel, entity, getKeyNames(metaModel));
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
	 * @throws RemoteException 
	 */
	private Map getAggregateValues(MetaAggregate metaAggregate, Object aggregate, Map memberNames) throws XavaException, RemoteException {
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
						getReferenceValues(metaAggregate, aggregate, memberName, submemberNames));
				} else {
					throw new XavaException("member_not_found", memberName, metaAggregate.getName());
				}
		}
		try {
			result.putAll(man.executeGets(names.toString()));
		} catch (PropertiesManagerException ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(XavaResources.getString("get_values_error", metaAggregate.getName()));
		} catch (InvocationTargetException ex) {
			Throwable th = ex.getTargetException();
			log.error(th.getMessage(), th);
			rollback();
			throw new RemoteException(XavaResources.getString("get_values_error", metaAggregate.getName()));
		}
		return result;
	}


	/**
	 * If <tt>memberNames</tt> is null then return a empty map.
	 * @throws RemoteException 
	 */
	private Map getAssociatedEntityValues(MetaEntity metaEntity, Object modelObject, Map memberNames) throws XavaException, RemoteException {
		if (memberNames == null) return Collections.EMPTY_MAP;
		Map result = getValues(metaEntity, modelObject, memberNames);
		return result;
	}

	private Map getReferenceValues(	
		MetaModel metaModel,
		Object model,
		String memberName,
		Map submembersNames) throws XavaException, RemoteException {		
		try {					
			MetaReference r = metaModel.getMetaReference(memberName);
			Object object = getReferencedObject(model, memberName);
			if (r.isAggregate()) {
				return getAggregateValues((MetaAggregate) r.getMetaModelReferenced(), object, submembersNames);
			} 
			else {				
				return getAssociatedEntityValues((MetaEntity) r.getMetaModelReferenced(), object, submembersNames);
			}
		} catch (XavaException ex) {
			log.error(ex.getMessage(), ex);
			throw new XavaException("get_reference_error", memberName, metaModel.getName());
		}
	}
	
	private Collection getCollectionValues(	
		MetaModel metaModel,
		Object modelObject,
		String memberName,
		Map memberNames) throws XavaException, RemoteException {
		try {
			MetaCollection c = metaModel.getMetaCollection(memberName);
			Object object = getReferencedObject(modelObject, memberName);
			return getCollectionValues( 
					c.getMetaReference().getMetaModelReferenced(),
					c.isAggregate(),	object, memberNames);
		} catch (XavaException ex) {
			log.error(ex.getMessage(), ex);
			throw new XavaException("get_collection_elements_error", memberName, metaModel.getName());
		}
	}
	
	private Collection getCollectionValues(	
		MetaModel metaModel,
		boolean aggregate,
		Object elements, Map memberNames) throws XavaException, RemoteException {
		Collection result = new ArrayList();
		Enumeration enumeration = null;
		if (elements instanceof Enumeration) {
			enumeration = (Enumeration) elements;
		}
		else if (elements instanceof Collection) {
			enumeration = Collections.enumeration((Collection) elements);
		}
		else {
			String collectionType = elements == null?"null":elements.getClass().getName();
			throw new XavaException("collection_type_not_supported", collectionType);
		}		
		while (enumeration.hasMoreElements()) {
			Object object = enumeration.nextElement();			
			result.add(getValues(metaModel, object, memberNames));
		}
		return result;
	}


	private Object instanceAggregate(MetaAggregateForReference metaAggregate, Map values)
		throws ValidationException, XavaException, RemoteException {
		try {
			Object object = Class.forName(metaAggregate.getBeanClass()).newInstance();
			PropertiesManager man = new PropertiesManager(object);			
			removeViewProperties(metaAggregate, values);
			values = convertSubmapsInObject(metaAggregate, values, false);
			man.executeSets(values);
			return object;
		} catch (ClassNotFoundException ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(XavaResources.getString("instantiate_error", metaAggregate.getBeanClass()));
		} catch (IllegalAccessException ex) {			
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(XavaResources.getString("instantiate_error", metaAggregate.getBeanClass()));
		} catch (InstantiationException ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(XavaResources.getString("instantiate_error", metaAggregate.getBeanClass()));
		} catch (InvocationTargetException ex) {
			throwsValidationException(
				ex, XavaResources.getString("assign_values_error", metaAggregate.getBeanClass(), ex.getLocalizedMessage()));
			rollback();
			throw new RemoteException(); // Never
		} catch (PropertiesManagerException ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(XavaResources.getString("assign_values_error", metaAggregate.getBeanClass(), ex.getLocalizedMessage()));
		}
	}

	private void throwsValidationException(
		InvocationTargetException ex,
		String ejbmessage)
		throws ValidationException, RemoteException {
		Throwable th = ex.getTargetException();
		if (th instanceof ValidationException) {
			throw (ValidationException) th;
		}
		log.error(ex.getMessage(), ex);
		throw new RemoteException(ejbmessage);
	}

	private Object mapToReferencedObject(	
		MetaModel metaModel,
		String memberName,
		Map values)
		throws ValidationException, XavaException, RemoteException {		
		MetaReference r = null;
		try {
			if (Maps.isEmpty(values)) return null;			
			r = metaModel.getMetaReference(memberName);
			if (r.isAggregate()) {
				return instanceAggregate((MetaAggregateForReference) r.getMetaModelReferenced(), values);
			} else {
				return findAssociatedEntity((MetaEntity) r.getMetaModelReferenced(), values);
			}
		}
		catch (ObjectNotFoundException ex) {
			return null; 
		} 
		catch (FinderException ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(XavaResources.getString("map_to_reference_error",
					r.getName(),					
					metaModel.getName(),					
					memberName));
		} catch (XavaException ex) {
			throw ex;
		}
	}

	private Object findAssociatedEntity(MetaEntity metaEntity, Map values)
		throws FinderException, XavaException, RemoteException {
		Map keyValues = extractKeyValues(metaEntity, values);		
		return findEntity(metaEntity.getName(), keyValues);
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
		
	private void remove(MetaModel metaModel, Map keyValues) 
		throws RemoveException, ValidationException, XavaException, RemoteException {
		try {
			Messages errors = validateOnRemove(metaModel, keyValues);			
			if (!errors.isEmpty()) {
				throw new ValidationException(errors);
			}			
			// removing collections are resposibility of persistence provider						
			getPersistenceProvider().remove(metaModel, keyValues); 
		} catch (ValidationException ex) {
			throw ex; 					
		} catch (XavaException ex) {
			log.error(ex.getMessage(), ex);
			throw new XavaException("remove_error", metaModel.getName(), ex.getLocalizedMessage());				
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(XavaResources.getString("remove_error",
				metaModel.getName(), ex.getLocalizedMessage()));
		}
	}
	
	private Messages validateOnRemove(MetaModel metaModel, Map keyValues) throws Exception {
		Messages errors = new Messages();
		Collection validators = metaModel.getMetaValidatorsRemove();		
		if (validators.isEmpty()) return errors;		
		Iterator it = validators.iterator();
		Object modelObject = findEntity(metaModel, keyValues);
		while (it.hasNext()) {
			MetaValidator metaValidator = (MetaValidator) it.next();
			IRemoveValidator validator = null;
			if (metaValidator.containsMetaSetsWithoutValue()) {
				validator = metaValidator.createRemoveValidator();
				PropertiesManager pmValidator = new PropertiesManager(validator);				
				PropertiesManager pmModelObject = new PropertiesManager(modelObject);
				for (Iterator itSets=metaValidator.getMetaSetsWithoutValue().iterator(); itSets.hasNext();) {
					MetaSet metaSet = (MetaSet) itSets.next();
					try {
						pmValidator.executeSet(metaSet.getPropertyName(), pmModelObject.executeGet(metaSet.getPropertyNameFrom()));
					}
					catch (Exception ex) {
						log.error(ex.getMessage(), ex);
						throw new XavaException("validator_set_property_error", metaSet.getPropertyName(), validator.getClass(), metaSet.getPropertyNameFrom(), modelObject.getClass(), ex.getMessage());
					}
				}
			}
			else {
				validator = metaValidator.getRemoveValidator();
			}
			validator.setEntity(modelObject);
			validator.validate(errors);			
		}				 		
		return errors;		
	}
		
	public void setSessionContext(javax.ejb.SessionContext ctx)
		throws java.rmi.RemoteException {
		sessionContext = ctx;
	}
		
	private void setValues(MetaModel metaModel, Map keyValues, Map values)
		throws FinderException, ValidationException, XavaException {		
		try {			
			updateReferencedEntities(metaModel, values);
			removeKeyFields(metaModel, values);			
			removeReadOnlyFields(metaModel, values);
			validate(metaModel, values, keyValues, null, false);
			removeViewProperties(metaModel, values);									
			Object entity = findEntity(metaModel, keyValues);			
			IPropertiesContainer r = getPersistenceProvider().toPropertiesContainer(metaModel, entity);			
			r.executeSets(convertSubmapsInObject(metaModel, values, XavaPreferences.getInstance().isEJB2Persistence()));			
			// Collections are not managed			
		} 
		catch (FinderException ex) { 
			throw ex;
		}		
		catch (ValidationException ex) {
			throw ex;
		} 
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new XavaException("assign_values_error", metaModel.getName(), ex.getLocalizedMessage()); 
		}
	}
	
	private void validate(	
		Messages errors,
		MetaModel metaModel,
		String memberName,
		Object values,
		boolean creating) throws XavaException, RemoteException {			
		try {			
			if (metaModel.containsMetaProperty(memberName)) {
				metaModel.getMetaProperty(memberName).validate(errors, values, creating); 
			} else
				if (metaModel.containsMetaReference(memberName)) {
					MetaReference ref = metaModel.getMetaReference(memberName); 
					MetaModel referencedModel = ref.getMetaModelReferenced();
					Map mapValues = (Map) values;					
					if (referenceHasValue(mapValues)) {
						if (ref.isAggregate()) validate(errors, referencedModel, mapValues, mapValues, null, creating);
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
					metaModel.getMetaPropertyView(memberName).validate(errors, values, creating);									
				} else {					
					log.warn(XavaResources.getString("not_validate_member_warning", memberName, metaModel.getName()));
				}
		} catch (XavaException ex) {
			log.error(ex.getMessage(), ex);
			throw new XavaException("validate_error", memberName, metaModel.getName());
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(XavaResources.getString("validate_error", memberName, metaModel.getName()));				
		}
	}
	
	private boolean referenceHasValue(Map values) {
		if (values == null) return false;
		Iterator it = values.values().iterator();
		while (it.hasNext()) {
			Object v = it.next();
			if (v == null) continue;
			if (v instanceof String && ((String) v).trim().equals("")) continue;
			// 0 is not treated as no-value, because 0 can be a valid key for a reference 
			return true;
		}		
		return false;
	}
	
	private void validate(Messages errors, MetaModel metaModel, Map values, Map keyValues, Object containerKey, boolean creating)	  
		throws XavaException, RemoteException {		
		Iterator it = values.entrySet().iterator();		
		while (it.hasNext()) {
			Map.Entry en = (Map.Entry) it.next();
			String name = (String) en.getKey();
			Object value = en.getValue();
			validate(errors, metaModel, name, value, creating);
		}
		if (metaModel.containsValidadors()) {
			validateWithModelValidator(errors, metaModel, values, keyValues, containerKey, creating);			
		}
	}
	
	private void validateWithModelValidator(Messages errors, MetaModel metaModel, Map values, Map keyValues, Object containerKey, boolean creating) throws XavaException {
		try {
			String containerReferenceName = Strings.firstLower(metaModel.getMetaModelContainer().getName());
			Iterator itValidators = metaModel.getMetaValidators().iterator();
			while (itValidators.hasNext()) {
				MetaValidator metaValidator = (MetaValidator) itValidators.next();
				Iterator itSets =  metaValidator.getMetaSetsWithoutValue().iterator();
				if (!creating && metaValidator.isOnlyOnCreate()) continue; 
				IValidator v = metaValidator.createValidator();
				PropertiesManager mp = new PropertiesManager(v);		
				while (itSets.hasNext()) {
					MetaSet set = (MetaSet) itSets.next();					
					Object value = values.get(set.getPropertyNameFrom());
					if (value == null && !values.containsKey(set.getPropertyNameFrom())) {						
						if (keyValues != null) {							
							Map memberName = new HashMap();
							memberName.put(set.getPropertyNameFrom(), null);
							Map memberValue = getValues(metaModel, keyValues, memberName);
							value = memberValue.get(set.getPropertyNameFrom());
						}											
					}					
					if (set.getPropertyNameFrom().equals(containerReferenceName)) {					
						if (containerKey == null) {							
							Object object = findEntity(metaModel, keyValues);
							value = Objects.execute(object, "get" + metaModel.getMetaModelContainer().getName());
						}
						else {							
							MetaModel containerReference = metaModel.getMetaModelContainer();
							try {
								Map containerKeyMap = getPersistenceProvider().keyToMap(containerReference, containerKey); 
								value = getPersistenceProvider().find(containerReference, containerKeyMap);								
							}
							catch (ObjectNotFoundException ex) {								
								value = null;
							}			
						}
					}
					else if (metaModel.containsMetaReference(set.getPropertyNameFrom())) {						
						MetaReference ref = metaModel.getMetaReference(set.getPropertyNameFrom());
						if (ref.isAggregate()) {							
							value = mapToReferencedObject(metaModel, set.getPropertyNameFrom(), (Map) value);
						}
						else {							
							MetaModel referencedEntity = ref.getMetaModelReferenced();
							try {
								value = findEntity(referencedEntity, (Map) value);								
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
			log.error(ex.getMessage(), ex);
			throw new XavaException("validate_model_error", metaModel.getName());
		}
	}
	
	private void validate(MetaModel metaModel, Map values, Map keyValues, Object containerKey, boolean creating)
		throws ValidationException, XavaException, RemoteException {
		Messages errors = new Messages();		
		validate(errors, metaModel, values, keyValues, containerKey, creating);		
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
				
	private Object findEntity(MetaModel metaModel, Map keyValues) throws FinderException, XavaException, RemoteException {
		return getPersistenceProvider().find(metaModel, keyValues);
	}
	
	private Object findEntityByAnyProperty(MetaModel metaModel, Map keyValues) throws FinderException, XavaException, RemoteException { 
		return getPersistenceProvider().findByAnyProperty(metaModel, keyValues);
	}
	
	
	private void rollback () throws RemoteException {
		if (getSessionContext() != null) getSessionContext().setRollbackOnly();
		getPersistenceProvider().rollback();
	}
		
	private void executePostremoveCollectionElement(MetaModel metaModel, Map keyValues, MetaCollection metaCollection) throws FinderException, ValidationException, XavaException, RemoteException {
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
				Map values = getValues(metaModel, keyValues, neededPropertyNames);
				
				itSets = sets.iterator();											
				while (itSets.hasNext()) {
					MetaSet set = (MetaSet) itSets.next();
					Object value = values.get(set.getPropertyNameFrom());
					if (value == null && !values.containsKey(set.getPropertyNameFrom())) {
						if (keyValues != null) { 
							Map memberName = new HashMap();
							memberName.put(set.getPropertyNameFrom(), null);
							Map memberValue = getValues(metaModel, keyValues, memberName);
							value = memberValue.get(set.getPropertyNameFrom());
						}											
					}
						
					if (metaModel.containsMetaReference(set.getPropertyNameFrom())) {
						MetaReference ref = metaModel.getMetaReference(set.getPropertyNameFrom());
						if (ref.isAggregate()) {
							value = mapToReferencedObject(metaModel, set.getPropertyNameFrom(), (Map) value);
						}
						else {
							MetaModel referencedEntity = ref.getMetaModelReferenced();
							try {
								value = findEntity(referencedEntity, (Map) value);
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
						log.error(ex.getMessage(), ex);
						throw new XavaException("calculator_property_error", value, set.getPropertyName());
					}									
				}
			}			
			
			if (calculator instanceof IModelCalculator) {
				Object entity = findEntity(metaModel, keyValues);
				try {
					((IModelCalculator) calculator).setModel(entity);
				}
				catch (Exception ex) {
					log.error(ex.getMessage(), ex);
					throw new XavaException("assign_entity_to_calculator_error", metaModel.getName(), keyValues);
				}									
				
			}
			if (calculator instanceof IEntityCalculator) {
				Object entity = findEntity( metaModel, keyValues);
				try {
					((IEntityCalculator) calculator).setEntity(entity);
				}
				catch (Exception ex) {
					log.error(ex.getMessage(), ex);
					throw new XavaException("assign_entity_to_calculator_error", metaModel.getName(), keyValues);
				}									
				
			}
			
			try {
				calculator.calculate();
			}
			catch (Exception ex) {
				log.error(ex.getMessage(), ex);
				rollback();
				throw new RemoteException(XavaResources.getString("postremove_error", metaModel.getName(), keyValues));
			}
		}				
	}
	
	private Map convertSubmapsInObject(MetaModel metaModel, Map values,
			boolean referencesAsKey) throws ValidationException, XavaException, RemoteException {		
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
					value = mapToReferencedObject(metaModel, memberName, (Map) en
							.getValue());
				}
				else {
					MetaEntity referencedEntity = (MetaEntity) ref
							.getMetaModelReferenced();
					memberName = memberName + "Key";
					value = referencedEntity.getMetaEJB().obtainPrimaryKeyFromKey((Map) en.getValue());
				}
			}
			else if (metaModel.getMapping().hasPropertyMapping(memberName)) {
				value = en.getValue();
			}
			else {				
				throw new XavaException("member_not_found", memberName, metaModel.getName());
			}
			result.put(memberName, value);
		}
		return result;
	}
	
	private Object findEntity(String modelName, Map keyValues)
		throws FinderException, RemoteException {
		try {
			return findEntity(getMetaModel(modelName), keyValues);			
		} catch (FinderException ex) {
			throw ex;
		} catch (ElementNotFoundException ex) {			
			rollback();
			throw new RemoteException(XavaResources.getString("model_not_found", modelName));
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(XavaResources.getString("find_error", modelName));
		}
	}
	
	private Object findEntityByAnyProperty(String modelName, Map keyValues) 
		throws FinderException, RemoteException {
		try {
			return findEntityByAnyProperty(getMetaModel(modelName), keyValues);			
		} catch (FinderException ex) {
			throw ex;
		} catch (ElementNotFoundException ex) {
			rollback();
			throw new RemoteException(XavaResources.getString("model_not_found", modelName));
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			rollback();
			throw new RemoteException(XavaResources.getString("find_error", modelName));
		}
	}	
	
	public Object getKey(MetaModel metaModel, Map keyValues) throws XavaException, RemoteException {
		return getPersistenceProvider().getKey(metaModel, keyValues);
	}
	
	public void reassociate(Object entity) throws RemoteException {
		getPersistenceProvider().reassociate(entity); 
	}

	private static IPersistenceProvider getPersistenceProvider() throws RemoteException {
		if (persistenceProvider == null) {
			try {
				persistenceProvider = (IPersistenceProvider) Class.forName(XavaPreferences.getInstance().getPersistenceProviderClass()).newInstance();
			}
			catch (Exception ex) {
				log.error(ex.getMessage(), ex);
				throw new RemoteException(XavaResources.getString("persistence_provider_creation_error"));
			}
		}		
		return persistenceProvider;
	}

}


