package org.openxava.model.impl;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * Provides the implementation of the persistence services
 * used in {@link MapFacadeBean}. <p>
 * 
 * For all methods you can use RuntimeException as system exception
 * (this exception always abort the operation and rollback the transaction).
 * You these cases you can throw JDOException, HibernateException, EJBException
 * or whatever RuntimeException your want.<br>  
 * 
 * @author Mª Carmen Gimeno Alabau
 */
public interface IPersistenceProvider {
	
	/**
	 * Find a object from its key in map format. <p>
	 * 
	 * @return Never null.
	 */
	Object find(MetaModel metaModel, Map keyValues) throws ObjectNotFoundException, FinderException, XavaException;
	
	/**
	 * Find a object from its key object. <p>
	 * 
	 * @return Never null.
	 */	
	Object find(MetaModel metaModel, Object key) throws ObjectNotFoundException, FinderException;
	
	/**
	 * Return an IPropertiesContainer to manage using introspection the sent object. <p>
	 */
	IPropertiesContainer toPropertiesContainer(MetaModel metaModel, Object modelObject) throws XavaException;
	
	/**
	 * Create a persistent object (saved in database) from the data passed in map format. <p> 
	 */
	Object create(MetaModel metaModel, Map values) throws DuplicateKeyException, CreateException, ValidationException, XavaException;
	
	/**
	 * Create an aggregate (saving it in database) from the data passed in map format. <p>
	 * 
	 * @param metaModel  of the aggregate to create.
	 * @param values  Values to fill aggregate before save.
	 * @param metaModelContainer  of model that will contain the aggregate.
	 * @param containerModel  The object that will contain the new aggregate.
	 * @param number  This number will be passed to calculator of type IAggregateOidCalculator, it can
	 * 		use this number to calculate the oid. It's a simple counter.   
	 */	
	Object createAggregate(MetaModel metaModel, Map values, MetaModel metaModelContainer,
			Object containerModel, int number)
			throws CreateException, ValidationException, RemoteException, XavaException;
	
	/**
	 * Return an object that can be used as primary key in model layer. <p>
	 * 
	 * For example, in EJB2 will be the Key class, in Hibernate can be the
	 * POJO class, and JPA ...
	 */
	Object getKey(MetaModel metaModel, Map keyValues) throws XavaException;
	
	/**
	 * Remove the object from persistent storage.
	 */
	void remove(MetaModel metaModel, Object modelObject) throws RemoveException, XavaException;
	
	/**
	 * Commit the work made by this persistent provider. <p>
	 * 
	 * This method may be empty (for example in case of using CMT).	 
	 */
	void commit();
	
	/**
	 * Rollback the work made by this persistent provider. <p>
	 * 
	 * This method may be empty (for example in case of using CMT).	 
	 */	
	void rollback();
	
	/**
	 * Save in database all persistent data still in memory. <p>
	 * 
	 * This method may be empty, because in some technologies has no sense.<br>	 
	 */
	void flush();	
	
}
