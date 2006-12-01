package org.openxava.model.impl;


import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;


public interface IMapFacadeImpl {
	
	Object create(String user, String modelName, Map values)
		throws 
			CreateException, ValidationException, 
			XavaException, RemoteException;
	
	Map getValuesByAnyProperty(String user, String modelName, Map searchingValues, Map memberNames)
		throws FinderException, XavaException, RemoteException; 	
		
	Map getValues(String user, String modelName, Map keyValues, Map memberNames)
		throws FinderException, XavaException, RemoteException;
		
	Map getValues(String user, String modelName, Object modelObject, Map memberNames)
		throws XavaException, RemoteException;
	
	Map getKeyValues(String user, String modelName, Object modelObject) 
		throws XavaException, RemoteException;	
	
	void setValues(String user, String modelName, Map keyValues, Map values)
		throws FinderException, ValidationException, XavaException, RemoteException;
				
	void delete(String user, String modelName, Map keyValues)
		throws RemoveException, XavaException, ValidationException, RemoteException;
		
	Object findEntity(String user, String modelName, Map keyValues)
		throws FinderException, java.rmi.RemoteException;
		
	Map createReturningValues(String user, String modelName, Map values)
				throws CreateException, XavaException, ValidationException, RemoteException;
				
	Messages validate(String user, String modelName, Map values) throws XavaException, RemoteException;
	
	Object createAggregate(String user, String modelName, Map keyContainer, int counter, Map values)
		throws CreateException,ValidationException, XavaException, RemoteException; 
 
	Object createAggregate(String user, String modelName, Object container, int counter, Map values) 
		throws CreateException,ValidationException, XavaException, RemoteException;
		
	Map createAggregateReturningKey(String user, String modelName, Map containerKeyValues, int counter, Map values) 
		throws CreateException,ValidationException, XavaException, RemoteException; 
		 
	Map createReturningKey(String user, String modelName, Map values)		 
		throws CreateException, XavaException, ValidationException, RemoteException;

	void addCollectionElement(String user, String modelName, Map keyValues, String collectionName, Map collectionElementKeyValue) 
		throws FinderException, ValidationException, XavaException,  RemoteException; 
	
	void removeCollectionElement(String user, String modelName, Map keyValues, String collectionName, Map collectionElementKeyValue) 
		throws RemoveException, FinderException, ValidationException, XavaException,  RemoteException; 
		 			
	Object getKey(MetaModel metaModel, Map keyValues) throws XavaException, RemoteException;
	
	void reassociate(Object entity) throws RemoteException;
}