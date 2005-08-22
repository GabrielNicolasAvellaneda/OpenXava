package org.openxava.model;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;
import javax.rmi.*;

import org.openxava.component.*;
import org.openxava.ejbx.*;
import org.openxava.model.impl.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;


/**
 * Allows manage model objects in <code>Map</code> format. <p>
 * 
 * It's used in generic OpenXava action to make CRUD operations,
 * but if it's convenient for you, you can use directly.<p>
 * 
 * A principle a good dessign is use maps for generic or automatic
 * things, but in all other cases the use of the model objects directly
 * is better, because the compiler do a good work for us, we can use
 * method calls, etc.<p>
 * 
 * We use the EJB exceptions (CreateException, FinderException, RemoveException,
 * etc) with the typical semantic associated to each. Although the implementation
 * does not use EJB.<br>
 * We use RemoteException to indicate a system error. Although the implementation
 * is local.<br>
 * 
 * EJB exceptions and RemoteException for any system errors are used for years
 * by enterprise java programmer, because this we think a good idea keep this
 * exceptions and its semantics while we can change the implementation of MapFacade (for
 * example for use POJOs instead EJB, or local process instead of remote objects,
 * or hibernate/jdo instead of entitybeans).<p>
 * 
 * The first parameter of each method is <code>modelName</code>, this is a
 * name of a OpenXava component (Customer, Invoice, etc) or a qualified aggregate 
 * (Invoice.InvoiceDetail for example).<p>   
 * 
 * @author Javier Paniza
 */

public class MapFacade {
	
	private static Map remotes;
  private static boolean usesEJBObtained;
  private static boolean usesEJB;
  private static IMapFacadeImpl localImpl;

	/**
	 * Creates a new entity from a map with its initial values. <p> 
	 * 
	 * @param modelName  OpenXava model name. Not null
	 * @param values  Initial values for create the entity. Not null
	 * @return Created entity, not a map it's the created object
	 *          (EntityBean, POJO object o the form used in the underlying model). Not null.
	 * @exception CreateException  Logic problem on creation.
	 * @exception ValidationException  Data validation problems.
	 * @exception XavaException  Any problem related to OpenXava. Rollback transaction.
	 * @exception RemoteException  System problem. Rollback transaction.
	 */
	public static Object create(String modelName, Map values) 
		throws
			CreateException,ValidationException, 
			XavaException, RemoteException 
	{
		Assert.arg(modelName, values);					
		try {									
			return getImpl(modelName).create(modelName, values);
		}
		catch (RemoteException ex) {
			annulImpl(modelName);
			return getImpl(modelName).create(modelName, values);
		}							
	}
	
	/**
	 * Creates a new aggregate from a map with its initial values. <p>	 
	 * 
	 * @param modelName  OpenXava model name. Not null
	 * @param containerKey  Key of entity or aggregate that contains this aggregate
	 * @param counter Counter used to generate the aggregate key, indicates the
	 * 		order number. The aggregate implementation can ignorate it.  
	 * @param values  Initial values for create the aggregate. Not null.
	 * @return Aggregate created, not a map but the create object
	 *          (EntityBean, POJO object o the form used in the underlying model). Not null.
	 * @exception CreateException  Logic problem on creation.
	 * @exception ValidationException  Data validation problems.
	 * @exception XavaException  Any problem related to OpenXava. Rollback transaction.
	 * @exception RemoteException  System problem. Rollback transaction.
	 */
	public static Object createAggregate(String modelName, Map containerKey, int counter, Map values) 
		throws
			CreateException,ValidationException, 
			XavaException, RemoteException 
	{
		Assert.arg(modelName, containerKey, values);					
		try {		
			return getImpl(modelName).createAggregate(modelName, containerKey, counter, values);
		}
		catch (RemoteException ex) {
			annulImpl(modelName);
			return getImpl(modelName).createAggregate(modelName, containerKey, counter, values);
		}							
	}
	
	/**
	 * Creates a new aggregate from a map with its initial values. <p>	 
	 * 
	 * @param modelName  OpenXava model name. Not null
	 * @param container  Container object (or container key in object format) that contains
	 * 		the aggregate.
	 * @param counter Counter used to generate the aggregate key, indicates the
	 * 		order number. The aggregate implementation can ignorate it.  
	 * @param values  Initial values for create the aggregate. Not null.
	 * @return Aggregate created, not a map but the create object
	 *          (EntityBean, POJO object o the form used in the underlying model). Not null.
	 * @exception CreateException  Logic problem on creation.
	 * @exception ValidationException  Data validation problems.
	 * @exception XavaException  Any problem related to OpenXava. Rollback transaction.
	 * @exception RemoteException  System problem. Rollback transaction.
	 */
	public static Object createAggregate(String modelName, Object container, int counter, Map values) 
		throws
			CreateException,ValidationException, 
			XavaException, RemoteException 
	{
		Assert.arg(modelName, container, values);					
		try {									
			return getImpl(modelName).createAggregate(modelName, container, counter, values);
		}
		catch (RemoteException ex) {
			annulImpl(modelName);
			return getImpl(modelName).createAggregate(modelName, container, counter, values);
		}							
	}
	
	/**	
	 * Creates a new entity from a map with its initials values and
	 * return a map with the values of created entity. <p>
	 *  
	 * @param modelName  OpenXava model name. Not null
	 * @param values  Initial values to create entity. Not null
	 * @return A map with the created object values. The properties are the
	 * 		sent ones on create. 
	 * @exception CreateException  Logic problem on creation.
	 * @exception ValidationException  Data validation problems.
	 * @exception XavaException  Any problem related to OpenXava. Rollback transaction.
	 * @exception RemoteException  System problem. Rollback transaction.
	 */
	public static Map createReturningValues(String modelName, Map values)
		throws
			CreateException,ValidationException,
			XavaException, RemoteException
	{
		Assert.arg(modelName, values);		
		try {
			return getImpl(modelName).createReturningValues(modelName, values);
		}
		catch (RemoteException ex) {
			annulImpl(modelName);
			return getImpl(modelName).createReturningValues(modelName, values);
		}
		
	}
	
	
	/**
	 * Creates a new entity from a map with its initial values and
	 * return a map with the key values of the created entity. <p>
	 *
	 * @param modelName  OpenXava model name. Not null
	 * @param values  Initial values to create the entity. Not null
	 * @return A map with key value of created object
	 * @exception CreateException  Logic problem on creation.
	 * @exception ValidationException  Data validation problems.
	 * @exception XavaException  Any problem related to OpenXava. Rollback transaction.
	 * @exception RemoteException  System problem. Rollback transaction.
	 */
	public static Map createReturningKey(String modelName, Map values)
		throws
			CreateException,ValidationException,
			XavaException, RemoteException
	{
		Assert.arg(modelName, values);		
		try {
			return getImpl(modelName).createReturningKey(modelName, values);
		}
		catch (RemoteException ex) {
			annulImpl(modelName);
			return getImpl(modelName).createReturningKey(modelName, values);
		}		
	}
				
	/**	
	 * Creates a new aggregate from a map with its initial values
	 * and return a map with the key. <p>  	 
	 * 
	 * @param modelName  OpenXava model name. Not null
	 * @param containerKey  Key of entity or aggregate that contains this aggregate	
 	 * @param counter Counter used to generate the aggregate key, indicates the
	 * 		order number. The aggregate implementation can ignorate it.  
	 * @param values  Initial values for create the aggregate. Not null.
	 * @return Key values of created aggregate.
	 * @exception CreateException  Logic problem on creation.
	 * @exception ValidationException  Data validation problems.
	 * @exception XavaException  Any problem related to OpenXava. Rollback transaction.
	 * @exception RemoteException  System problem. Rollback transaction.
	 */
	public static Map createAggregateReturningKey(String modelName, Map containerKey, int counter, Map values) 
		throws
			CreateException,ValidationException, 
			XavaException, RemoteException 
	{
		Assert.arg(modelName, containerKey, values);					
		try {		
			return getImpl(modelName).createAggregateReturningKey(modelName, containerKey, counter, values);
		}
		catch (RemoteException ex) {
			annulImpl(modelName);
			return getImpl(modelName).createAggregateReturningKey(modelName, containerKey, counter, values);
		}							
	}
	
	
	/**
	 * Obtain the specified values from entity/aggregate from a map with 
	 * primary key values. <p>
	 * 
	 * The <code>memberNames</tt> parameter is a map to use a treelike structure.<br>
	 * The property names are in key part. If it's a simple property the value
	 * is null, otherwise it has a map with the same structure.<br>
	 * For example, if we have a <code>Customer</tt> that references
	 * to a <code>Seller</code>,
	 * we can send a map with the next values:
	 * <pre> 
	 * { "number", null }
	 * { "name", null }
	 * { "seller", { {"number", null}, {"name", null} } }
	 * </pre>
	 * 
	 * @param modelName  OpenXava model name. Not null.
	 * @param keyValues  Key values of object to find. Not null.
	 * @param memberNames Member names to obtain its values. Not null  
	 * @return Map with entity values. Not null.
	 * @exception ObjectNotFoundException  If object with this key does not exist 
	 * @exception FinderException  Logic problem on find.	
	 * @exception XavaException  Any problem related to OpenXava. Rollback transaction.
	 * @exception RemoteException  System problem. Rollback transaction.
	 */	
	public static Map getValues(
		String modelName,
		Map keyValues,
		Map memberNames)
		throws FinderException, XavaException, RemoteException 
	{						
		Assert.arg(modelName, keyValues, memberNames);		
		if (keyValues.isEmpty()) {
			throw new ObjectNotFoundException(XavaResources.getString("empty_key_object_not_found", modelName));						
		}
		try {					
			return getImpl(modelName).getValues(modelName, keyValues, memberNames);
		}
		catch (RemoteException ex) {
			annulImpl(modelName);
			return getImpl(modelName).getValues(modelName, keyValues, memberNames);
		}		
	}
		
	/**
	 * Obtain the values of the entity/aggregate from the own entity. <p> 
	 * 
	 * The <code>memberNames</tt> parameter is a map to use a treelike structure.<br>
	 * The property names are in key part. If it's a simple property the value
	 * is null, otherwise it has a map with the same structure.<br>
	 * For example, if we have a <code>Customer</tt> that references
	 * to a <code>Seller</code>,
	 * we can send a map with the next values:
	 * <pre> 
	 * { "number", null }
	 * { "name", null }
	 * { "seller", { {"number", null}, {"name", null} } }
	 * </pre>
	 * 
	 * @param modelName  OpenXava model name. Not null.
	 * @param entity  Object to obtain values from it. Not null.
	 * @param memberNames Member names to obtain its values. Not null  
	 * @return Map with entity values. Not null.
	 * @exception XavaException  Any problem related to OpenXava. Rollback transaction.
	 * @exception RemoteException  System problem. Rollback transaction.
	 */	
	public static Map getValues(String modelName, Object entity, Map memberNames)
		throws XavaException, RemoteException 
	{		
		Assert.arg(modelName, entity, memberNames);
		try {
			return getImpl(modelName).getValues(modelName, entity, memberNames);
		}
		catch (RemoteException ex) {
			annulImpl(modelName);
			return getImpl(modelName).getValues(modelName, entity, memberNames);
		}
			
	}
	
	/**
	 * Obtain the entity/aggregate from a map with key values. <p>
	 * 
	 * @param modelName  OpenXava model name. Not null
	 * @param keyValues  Key values of entity to find. Not null
 	 * @return The entity or aggregate. Not null
	 * @exception ObjectNotFoundException  If object with this key does not exist 
	 * @exception FinderException  Logic problem on find.	
	 * @exception RemoteException  System problem. Rollback transaction.
	 */ 
	public static Object findEntity(String modelName, Map keyValues)
		throws ObjectNotFoundException, FinderException, RemoteException 
	{	
		if (keyValues==null) return null;
		Assert.arg(modelName, keyValues);
		try {
			return getImpl(modelName).findEntity(modelName, keyValues);
		}
		catch (RemoteException ex) {
			annulImpl(modelName);
			return getImpl(modelName).findEntity(modelName, keyValues);
		}					
	}	

	/**
	 * Remove the entity/aggregate from a map with its key. <p> 
	 * 
	 * @param modelName  OpenXava model name. No puede ser nulo.
	 * @param keyValues  Valores con la clave de la entidad a borrar. Nunca nulo.
	 * @exception RemoveException  Logic problem on remove.
	 * @exception ValidationException  Data validation problems.
	 * @exception XavaException  Any problem related to OpenXava. Rollback transaction.
	 * @exception RemoteException  System problem. Rollback transaction.
	 */
	public static void remove(String modelName, Map keyValues)
		throws RemoveException, RemoteException, XavaException, ValidationException {
		Assert.arg(modelName, keyValues);
		try {
			getImpl(modelName).delete(modelName, keyValues);
		}
		catch (RemoteException ex) {
			annulImpl(modelName);
			getImpl(modelName).delete(modelName, keyValues);
		}
		
	}

	/**
	 * Set new values to a entity/aggregate that is found from its key values. <p>
	 * 
	 * @param modelName  OpenXava model name. Not null.
	 * @param keyValues  Key values of object. Not null.
	 * @param values  New values to set. Not null.
	 * @exception ObjectNotFoundException  If object with this key does not exist 
	 * @exception FinderException  Logic problem on find.	
	 * @exception ValidationException  Data validation problems.	 * 
	 * @exception XavaException  Any problem related to OpenXava. Rollback transaction.
	 * @exception RemoteException  System problem. Rollback transaction.
	 */	
	public static void setValues(String modelName, Map keyValues,	Map values)
		throws ObjectNotFoundException, FinderException,	ValidationException,
				XavaException,  RemoteException 
	{
		Assert.arg(modelName, keyValues, values);				
		try {
			getImpl(modelName).setValues(modelName, keyValues, values);								
		}
		catch (RemoteException ex) {
			annulImpl(modelName);
			getImpl(modelName).setValues(modelName, keyValues, values);			
		}				
	}
	
	/**	 
	 * Validates the sent values but does not create or update the object. <p>
	 *
	 * Only validates the sent data, it does not certify that exist all needed data
	 * to create a new object.<br> 
	 * 
	 * @param modelName  OpenXava model name. Not null.
	 * @param values  Values to validate. Not null.
	 * @return Message list with validation errors. Not null.
	 * @exception XavaException  Any problem related to OpenXava. Rollback transaction.
	 * @exception RemoteException  System problem. Rollback transaction.
	 */	
	public static Messages validate(String modelName, Map values)
		throws XavaException,  RemoteException 
	{
		Assert.arg(modelName, values);			
		try {
			return getImpl(modelName).validate(modelName, values);								
		}
		catch (RemoteException ex) {
			annulImpl(modelName);
			return getImpl(modelName).validate(modelName, values);			
		}
				
	}
	
									
	
	private static IMapFacadeImpl getImpl(String modelName) throws RemoteException {
		if (!usesEJB()) return getLocalImpl();
		try {			
			int idx = modelName.indexOf('.'); 
			if (idx >=0) {
				modelName = modelName.substring(0, idx);				 				
			}			
			String paquete = MetaComponent.get(modelName).getPackageNameWithSlashWithoutModel();			
			MapFacadeRemote remote = (MapFacadeRemote) getRemotes().get(paquete);
			if (remote == null) {							
				Object ohome = BeansContext.get().lookup("ejb/"+paquete+"/MapFacade");
				MapFacadeHome home = (MapFacadeHome) PortableRemoteObject.narrow(ohome, MapFacadeHome.class);
				remote = home.create();
				getRemotes().put(paquete, remote);				
			}		
			return remote;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException(XavaResources.getString("facade_remote", modelName));
		}		
	}
			
	/**
	 * Convert from a map with primary key values to primary key object. <p> 
	 */		
	public static Object toPrimaryKey(String entityName, Map keyValues) throws XavaException {
		try {
			MetaEntityEjb m = (MetaEntityEjb) MetaComponent.get(entityName).getMetaEntity();
			return m.obtainPrimaryKeyFromKey(keyValues);
		}
		catch (ClassCastException ex) {
			ex.printStackTrace();
			throw new XavaException("no_entity_bean", entityName);
		}
	}
	
	private static Map getRemotes() {
		if (remotes == null) {
			remotes = new HashMap();
		}
		return remotes;
	}
	
	private static void annulImpl(String modelName) {
		if (!usesEJB()) return;
		try {
			int idx = modelName.indexOf('.'); 
			if (idx >=0) {
				modelName = modelName.substring(0, idx);				 				
			}			
			String paquete = MetaComponent.get(modelName).getPackageNameWithSlashWithoutModel();			
			getRemotes().remove(paquete);			
		}
		catch (Exception ex) {
			ex.printStackTrace();			
			System.err.println(XavaResources.getString("cache_facade_remote_warning"));
		}		
	}


	/**
	 * Removes a elemente from a collection. <p>
	 * 
	 * If it's a aggregate remove the aggregate, and if it's a entity reference
	 * make the left to point to the parent object, hence left the collection.<br>
	 *
	 * Does not delete aggregates directly, but with this method, because
	 * thus the needed logic for remove a element from a collection is executed.<br>   
	 * 
	 * @param modelName  OpenXava model name. Not null.
	 * @param keyValues  Key value of the container of the collection. Not null.
	 * @param collectionName  Collection name of the container collection of element to remove. Not null.
	 * @param collectionElementKeyValues  Key value of element to remove. Not null.
	 * @exception ObjectNotFoundException  If object with this key does not exist 
	 * @exception FinderException  Logic problem on find.	
	 * @exception ValidationException  Data validation problems.
	 * @exception RemoveException  Logic problem on remove. 
	 * @exception XavaException  Any problem related to OpenXava. Rollback transaction.
	 * @exception RemoteException  System problem. Rollback transaction.
	 */	
	public static void removeCollectionElement(String modelName, Map keyValues, String collectionName, Map collectionElementKeyValues) 
		throws ObjectNotFoundException, FinderException,	ValidationException, RemoveException,
			XavaException,  RemoteException 
	{
		Assert.arg(modelName, keyValues, collectionName, collectionElementKeyValues);
		try {
			getImpl(modelName).removeCollectionElement(modelName, keyValues, collectionName, collectionElementKeyValues);
		}
		catch (RemoteException ex) {
			annulImpl(modelName);
			getImpl(modelName).removeCollectionElement(modelName, keyValues, collectionName, collectionElementKeyValues);
		}
	}	

	private static boolean usesEJB() {
		if (!usesEJBObtained) {
			usesEJB = XavaPreferences.getInstance().isMapFacadeAsEJB();
			usesEJBObtained = true;
		}
		return usesEJB;
	}
	
	private static IMapFacadeImpl getLocalImpl() {
		if (localImpl==null) {
			localImpl = new MapFacadeBean();
		}
		return localImpl;
	}
	
}