package org.openxava.model.impl;


import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import org.openxava.util.*;
import org.openxava.validators.*;


public interface IMapFacadeImpl {

	Object create(String modelName, Map values)
		throws 
			CreateException, ValidationException, 
			XavaException, RemoteException;
			
		
	Map getValues(String modelName, Map keyValues, Map memberNames)
		throws FinderException, XavaException, RemoteException;
		
	Map getValues(String modelName, Object modelObject, Map memberNames)
		throws XavaException, RemoteException;
		
	void setValues(String modelName, Map keyValues, Map values)
		throws FinderException, ValidationException, XavaException, RemoteException;
				
	void remove(String modelName, Map keyValues)
		throws RemoveException, XavaException, ValidationException, RemoteException;
		
	Object findEntity(String modelName, Map keyValues)
		throws FinderException, java.rmi.RemoteException;
		
	Map createReturningValues(String modelName, Map values)
				throws CreateException, XavaException, ValidationException, RemoteException;
				
	Messages validate(String modelName, Map values) throws XavaException, RemoteException;
	
	Object createAggregate(String modelName, Map keyContainer, int counter, Map values)
		throws CreateException,ValidationException, XavaException, RemoteException; 
 
	Object createAggregate(String modelName, Object container, int counter, Map values) 
		throws CreateException,ValidationException, XavaException, RemoteException;
		
	Map createAggregateReturningKey(String modelName, Map containerKeyValues, int counter, Map values) 
		throws CreateException,ValidationException, XavaException, RemoteException; 
		 
	Map createReturningKey(String modelName, Map values)		 
		throws CreateException, XavaException, ValidationException, RemoteException;
		
	void removeCollectionElement(String modelName, Map keyValues, String collectionName, Map collectionElementKeyValue) 
		throws RemoveException, FinderException,	ValidationException, XavaException,  RemoteException; 
		 							
}