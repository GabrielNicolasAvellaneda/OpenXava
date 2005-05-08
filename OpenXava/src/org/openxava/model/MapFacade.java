package org.openxava.model;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;
import javax.ejb.ObjectNotFoundException;

import org.openxava.component.*;
import org.openxava.model.impl.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;


public class MapFacade {
	
	private static MapFacadeBean impl = new MapFacadeBean(); 
		
	public static Object create(String modelName, Map values) 
		throws
			CreateException,ValidationException, 
			XavaException, RemoteException 
	{
		Assert.arg(modelName, values);
		return impl.create(modelName, values);
	}
	
	public static Object createAggregate(String modelName, Map containerKey, int counter, Map values) 
		throws
			CreateException,ValidationException, 
			XavaException, RemoteException 
	{
		Assert.arg(modelName, containerKey, values);
		throw new UnsupportedOperationException("Método todavía no implementado");					
	}
	
	public static Object createAggregate(String modelName, Object container, int counter, Map values) 
		throws
			CreateException,ValidationException, 
			XavaException, RemoteException 
	{
		Assert.arg(modelName, container, values);
		throw new UnsupportedOperationException("Método todavía no implementado");					
	}
	
	public static Map createReturningValues(String modelName, Map values)
		throws
			CreateException,ValidationException,
			XavaException, RemoteException
	{
		Assert.arg(modelName, values);		
		throw new UnsupportedOperationException("Método todavía no implementado");
	}
	
	public static Map createReturningKey(String modelName, Map values)
		throws
			CreateException,ValidationException,
			XavaException, RemoteException
	{
		Assert.arg(modelName, values);		
		throw new UnsupportedOperationException("Método todavía no implementado");
	}
	
	public static Map createAggregateReturningKey(String modelName, Map containerKey, int counter, Map values) 
		throws
			CreateException,ValidationException, 
			XavaException, RemoteException 
	{
		Assert.arg(modelName, containerKey, values);
		throw new UnsupportedOperationException("Método todavía no implementado");							
	}
	
	public static Map getValues(
		String modelName,
		Map keyValues,
		Map memberNames)
		throws FinderException, XavaException, RemoteException 
	{
		Assert.arg(modelName, keyValues, memberNames);
		if (keyValues.isEmpty()) {
			throw new ObjectNotFoundException("Objeto de tipo " + modelName + " con clave vacía no existe");						
		}
	  return impl.getValues(modelName,  keyValues, memberNames);
	}
	
	public static Map getValues(String modelName, Object entity, Map memberNames)
		throws XavaException, RemoteException 
	{
		Assert.arg(modelName, entity, memberNames);
		throw new UnsupportedOperationException("Método todavía no implementado");		
	}
	
	public static Object findEntity(String modelName, Map keyValues)
		throws FinderException, RemoteException 
	{	
		if (keyValues==null) return null;
		Assert.arg(modelName, keyValues);
		throw new UnsupportedOperationException("Método todavía no implementado");		
	}	

	public static void remove(String modelName, Map keyValues)
		throws RemoveException, RemoteException, XavaException, ValidationException {
		Assert.arg(modelName, keyValues);
		impl.remove(modelName,  keyValues);
	}
	
	public static void setValues(
		java.lang.String modelName,
		Map keyValues,
		Map values)
		throws FinderException,	ValidationException,
				XavaException,  RemoteException 
	{
		Assert.arg(modelName, keyValues, values);
		impl.setValues(modelName,keyValues,values);
	}
	
	public static Messages validate(
		java.lang.String modelName,		
		Map values)
		throws XavaException,  RemoteException 
	{
		Assert.arg(modelName, values);			
		throw new UnsupportedOperationException("Método todavía no implementado");				
	}
	
	public static Object toPrimaryKey(String entityName, Map keyValues) throws XavaException {
		try {
			MetaEntityEjb m = (MetaEntityEjb) MetaComponent.get(entityName).getMetaEntity();
			return m.obtainPrimaryKeyFromKey(keyValues);
		}
		catch (ClassCastException ex) {
			ex.printStackTrace();
			throw new XavaException("La entidad del componente " + entityName + " no está implementado como un EntityBean");
		}
	}
	
	public static void removeCollectionElement(String modelName, Map keyValues, String collectionName, Map collectionElementKeyValues) 
		throws FinderException,	ValidationException, RemoveException,
			XavaException,  RemoteException 
	{
		Assert.arg(modelName, keyValues, collectionName, collectionElementKeyValues);
		throw new UnsupportedOperationException("Método todavía no implementado");		
	}
		
}