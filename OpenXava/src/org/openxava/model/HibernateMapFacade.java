package org.openxava.model;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;
import javax.ejb.ObjectNotFoundException;

import net.sf.hibernate.*;
import net.sf.hibernate.cfg.*;

import org.openxava.component.*;
import org.openxava.model.impl.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;


public class HibernateMapFacade {
	
	private static HibernateMapFacadeImpl impl = new HibernateMapFacadeImpl(); 
	private static SessionFactory sessionFactory;
	
	public static Object create(String modelName, Map values) 
		throws
			CreateException,ValidationException, 
			XavaException, RemoteException 
	{
		Assert.arg(modelName, values);
		Session session = null;
		Transaction tx = null;
		try {			
			session = getSessionFactory().openSession();
			tx = session.beginTransaction();	
		  impl.setSession(session);
		  Object object = impl.create(modelName, values);
		  tx.commit();
			session.close();
			return object;
		}
		catch (CreateException ex) {
			rollback(tx, session);
			throw ex;			
		}
		catch (ValidationException ex) {
			rollback(tx, session);
			throw ex;			
		}													
		catch (XavaException ex) {
			rollback(tx, session);
			throw ex;			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			rollback(tx, session);
			throw new RemoteException (ex.getMessage());			
		}
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
		Session session = null;
		Transaction tx = null;
		try {			
			session = getSessionFactory().openSession();
			tx = session.beginTransaction();	
		  impl.setSession(session);
		  Map values = impl.getValues(modelName,  keyValues, memberNames);
		  tx.commit();
			session.close();
			return values;
		}
		catch (FinderException ex) {
			ex.printStackTrace();
			rollback(tx, session);
			throw ex;						
		}
		catch (XavaException ex) {
			ex.printStackTrace();
			rollback(tx, session);
			throw ex;			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			rollback(tx, session);
			throw new RemoteException (ex.getMessage());			
		}
								
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
		Session session = null;
		Transaction tx = null;
		try {			
			session = getSessionFactory().openSession();
			tx = session.beginTransaction();	
		  impl.setSession(session);
		  impl.remove(modelName,  keyValues);
		  tx.commit();
			session.close();
		}
		catch (RemoveException ex) {
			ex.printStackTrace();
			rollback(tx, session);
			throw ex;						
		}
		catch (ValidationException ex) {
			ex.printStackTrace();
			rollback(tx, session);
			throw ex;						
		}
		catch (XavaException ex) {
			ex.printStackTrace();
			rollback(tx, session);
			throw ex;			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			rollback(tx, session);
			throw new RemoteException (ex.getMessage());			
		}
																						
	}
	
	public static void setValues(
		java.lang.String modelName,
		Map keyValues,
		Map values)
		throws FinderException,	ValidationException,
				XavaException,  RemoteException 
	{
		Assert.arg(modelName, keyValues, values);
		Session session = null;
		Transaction tx = null;
		try {			
			session = getSessionFactory().openSession();
			tx = session.beginTransaction();	
		  impl.setSession(session);
		  impl.setValues(modelName,keyValues,values);
		  tx.commit();
			session.close();
		}
		catch (FinderException ex) {
			ex.printStackTrace();
			rollback(tx, session);
			throw ex;						
		}
		catch (ValidationException ex) {
			ex.printStackTrace();
			rollback(tx, session);
			throw ex;						
		}
		catch (XavaException ex) {
			ex.printStackTrace();
			rollback(tx, session);
			throw ex;			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			rollback(tx, session);
			throw new RemoteException (ex.getMessage());			
		}
																							
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
		
	private static SessionFactory getSessionFactory() throws HibernateException, XavaException {
		if (sessionFactory == null) {
			Configuration configuration = new Configuration().configure("/hibernate.cfg.xml");
			for (Iterator it = MetaModel.getAllGenerated().iterator(); it.hasNext();) {
				MetaModel model = (MetaModel) it.next();
				configuration.addResource(model.getName() + ".hbm.xml");
			}
			sessionFactory = configuration.buildSessionFactory();
		}
		return sessionFactory; 
	}

	private static void rollback(Transaction tx, Session session) throws RemoteException {
		try {
			if (tx != null)	tx.rollback();
			if (session != null) session.close();
		} 
		catch (HibernateException ex) {
			ex.printStackTrace();
			throw new RemoteException(ex.getMessage());
		}	
	}

}