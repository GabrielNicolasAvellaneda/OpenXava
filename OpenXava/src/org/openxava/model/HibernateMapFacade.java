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
	
	/**
	 * Crea una nueva entidad a partir de un mapa con
	 * sus valores iniciales. <p>
	 * 
	 * @param modelName  Nombre Xava del modelo. No puede ser nulo.
	 * @param values  Valores iniciales para crear la entidad. No puede ser nulo.
	 * @return Entidad creadada, no es un mapa es el objeto creado
	 *          (EntityBean, objeto jdo o cualquiera que sea
	 *           el modelos subyacente usado). Nunca nulo.
	 * @exception CreateException  Problema de lógica al crear.
	 * @exception ValidacionException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */
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
	
	/**	 
	 * Crea un nueva agregado a partir de un mapa con
	 * sus valores iniciales. <p>
	 * 
	 * @param modelName  Nombre Xava del modelo. No puede ser nulo.
	 * @param containerKey  Clave de la entidad o agregado que contiene al agregado.
	 * @param counter  Contador usado para generar la clave del agregado, indica el número
	 * 				de orden. La implementación del agregado puede optar por ignorarlo.
	 * @param values  Valores iniciales para crear el agregado. No puede ser nulo.
	 * @return Agregado creadado, no es un mapa es el objeto creado
	 *          (EntityBean, objeto jdo o cualquiera que sea
	 *           el modelos subyacente usado). Nunca nulo.
	 * @exception CreateException  Problema de lógica al crear.
	 * @exception ValidacionException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */
	public static Object createAggregate(String modelName, Map containerKey, int counter, Map values) 
		throws
			CreateException,ValidationException, 
			XavaException, RemoteException 
	{
		Assert.arg(modelName, containerKey, values);
		throw new UnsupportedOperationException("Método todavía no implementado");					
	}
	
	/**	 
	 * Crea un nueva agregado a partir de un mapa con
	 * sus valores iniciales. <p>
	 * 
	 * @param modelName  Nombre Xava del modelo. No puede ser nulo.
	 * @param container  Objecto contenedor o clave (en formato objeto) del objeto contenedor.
	 * @param counter  Contador usado para generar la clave del agregado, indica el número
	 * 				de orden. La implementación del agregado puede optar por ignorarlo.
	 * @param values  Valores iniciales para crear el agregado. No puede ser nulo.
	 * @return Agregado creadado, no es un mapa es el objeto creado
	 *          (EntityBean, objeto jdo o cualquiera que sea
	 *           el modelos subyacente usado). Nunca nulo.
	 * @exception CreateException  Problema de lógica al crear.
	 * @exception ValidationException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */
	public static Object createAggregate(String modelName, Object container, int counter, Map values) 
		throws
			CreateException,ValidationException, 
			XavaException, RemoteException 
	{
		Assert.arg(modelName, container, values);
		throw new UnsupportedOperationException("Método todavía no implementado");					
	}
	
	/**	 
	 * Crea una nueva entidad a partir de un mapa con sus valores iniciales y
	 * devuelve un mapa con los valores de la entidad creada.
	 * <p>
	 *
	 * @param modelName  Nombre Xava del modelo. No puede ser nulo.
	 * @param values  Valores iniciales para crear la entidad. No puede ser nulo.
	 * @return Un mapa con los valores del objeto creado. Las propiedades
	 * contenidas son las enviadas al crear.
	 * @exception CreateException  Problema de lógica al crear.
	 * @exception ValidationException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */
	public static Map createReturningValues(String modelName, Map values)
		throws
			CreateException,ValidationException,
			XavaException, RemoteException
	{
		Assert.arg(modelName, values);		
		throw new UnsupportedOperationException("Método todavía no implementado");
	}
	
	
	/**
	 * Crea una nueva entidad a partir de un mapa con sus valores iniciales y
	 * devuelve un mapa con los valores de la clave de la entidad creada.
	 * <p>
	 *
	 * @param modelName  Nombre Xava del modelo. No puede ser nulo.
	 * @param values  Valores iniciales para crear la entidad. No puede ser nulo.
	 * @return Un mapa con los valores de la clave del objeto creado.
	 * @exception CreateException  Problema de lógica al crear.
	 * @exception ValidationException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */
	public static Map createReturningKey(String modelName, Map values)
		throws
			CreateException,ValidationException,
			XavaException, RemoteException
	{
		Assert.arg(modelName, values);		
		throw new UnsupportedOperationException("Método todavía no implementado");
	}
	
	
	
	
	/**	 	 
	 * Crea un nuevo agregado a partir de un mapa con
	 * sus valores iniciales y devuelve un mapa con la clave. <p>
	 * 
	 * @param modelName  Nombre Xava del modelo. No puede ser nulo.
	 * @param containerKey  Clave de la entidad o agregado que contiene al agregado.
	 * @param counter  Contador usado para generar la clave del agregado, indica el número
	 * 				de orden. La implementación del agregado puede optar por ignorarlo.
	 * @param values  Valores iniciales para crear el agregado. No puede ser nulo.
	 * @return Un mapa con la clave del agregado creado
	 * @exception CreateException  Problema de lógica al crear.
	 * @exception ValidationException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */
	public static Map createAggregateReturningKey(String modelName, Map containerKey, int counter, Map values) 
		throws
			CreateException,ValidationException, 
			XavaException, RemoteException 
	{
		Assert.arg(modelName, containerKey, values);
		throw new UnsupportedOperationException("Método todavía no implementado");							
	}
	
	
	/**
	 * Obtiene los valores especificados de la entidad/agreado a partir de un mapa con 
	 * los valores de la clave primaria. <p>
	 * 
	 * El parametro <tt>nombreMiembros</tt> es un mapa para
	 * poder tener una estructura jerarquica.
	 * Los nombres de propiedades están en la parte
	 * de clave. Si es una propiedad simple el valor será nulo, y
	 * en caso contrario tiene un mapa con la misma estructura.<br>
	 * Por ejemplo, si tenemos un <tt>Cliente</tt> que
	 * referencia a un <tt>Comercial</tt>,
	 * podriamos enviar un mapa con los siguiente valores:
	 * <pre>
	 * { "codigo", null }
	 * { "nombre", null }
	 * { "comercial", { {"codigo", null}, {"nombre", null} } }
	 * </pre>
	 * 
	 * @param modelName  Nombre Xava del modelo. Puede ser el nombre
	 * 	de un agregado cualificado. No puede ser nulo.
	 * @param keyValues  Valores de la clave de la entidad a buscar. No puede ser nulo.
	 * @param memberNames  Nombres de los miembros de los que se obtendrá 
	 * 						  la información. No puede ser nulo. 
	 * @return Mapa con los datos de la entidad. Nunca nulo.
	 * @exception FinderException  Problema de lógica al buscar.
	 * @exception ObjectNotFoundException  No existe una entidad con esa clave.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */	
	
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
	
		
	/**
	 * Obtiene todos los valores de la entidad a partir de la propia entidad. <p>
	 * 
	 * El parametro <tt>nombreMiembros</tt> es un mapa para
	 * poder tener una estructura jerarquica.
	 * Los nombres de propiedades están en la parte
	 * de clave. Si es una propiedad simple el valor será nulo, y
	 * en caso contrario tiene un mapa con la misma estructura.<br>
	 * Por ejemplo, si tenemos un <tt>Cliente</tt> que
	 * referencia a un <tt>Comercial</tt>,
	 * podriamos enviar un mapa con los siguiente valores:
	 * <pre>
	 * { "codigo", null }
	 * { "nombre", null }
	 * { "comercial", { {"codigo", null}, {"nombre", null} } }
	 * </pre>
	 * 
	 * @param modelName  Nombre Xava del modelo. No puede ser nulo.
	 * @param entity  Entidad de la queremos obtener los valores. No puede ser nulo.
	 * @param memberNames  Nombres de los miembros de los que se obtendrá 
	 * 						  la información. Los nombres se guardan
	 *                        en la clave del mapa. Nuncan nulo. 
	 * @return Mapa con los datos de la entidad. Nunca nulo.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */	
	public static Map getValues(String modelName, Object entity, Map memberNames)
		throws XavaException, RemoteException 
	{
		Assert.arg(modelName, entity, memberNames);
		throw new UnsupportedOperationException("Método todavía no implementado");		
	}
	
	/**
	 * Obtiene la entidad a partir de un mapa con 
	 * los valores de la clave primaria. <p>
	 * 
	 * @param modelName  Nombre Xava del modelo. No puede ser nulo.
	 * @param keyValues  Valores de la clave de la entidad a buscar. No puede ser nulo.
 	 * @return La entidad. Nunca nulo.
	 * @exception FinderException  Problema de lógica al buscar.
	 * @exception ObjectNotFoundException  No existe una entidad con esa clave.	 
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */
	public static Object findEntity(String modelName, Map keyValues)
		throws FinderException, RemoteException 
	{	
		if (keyValues==null) return null;
		Assert.arg(modelName, keyValues);
		throw new UnsupportedOperationException("Método todavía no implementado");		
	}	

	/**
	 * Borra la entidad a partir de un mapa con
	 * su clave. <p>
	 * 
	 * @param modelName  Nombre Xava del modelo. No puede ser nulo.
	 * @param keyValues  Valores con la clave de la entidad a borrar. Nunca nulo.
	 * @return Entidad creadada, no es un mapa es el objeto creado
	 *          (EntityBean, objeto jdo o cualquiera que sea
	 *           el modelos subyacente usado). Nunca nulo.
	 * @exception RemoveException  Problema de lógica al borrar.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 * @exception ValidationException  Si algún problema de validación impide que se borre la entidad
	 */
	
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
	

	/**
	 * Establece nuevos valores en la entidad obtenida a partir de un mapa con 
	 * los valores de la clave primaria. <p>
	 * 
	 * @param modelName  Nombre Xava del modelo. No puede ser nulo.
	 * @param keyValues  Valores de la clave de la entidad a buscar. No puede ser nulo.
	 * @param values  Nuevos valores a establecer. No puede ser nulo.
	 * @exception FinderException  Problema de lógica al buscar.
	 * @exception ObjectNotFoundException  No existe una entidad con esa clave.
	 * @exception ValidationException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */	
	
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
	
	
	/**	 
	 * Valida el mapa con los datos enviados pero sin crear o modificar ninguna entidad. <p>
	 * 
	 * Solo valida los datos enviados, no certifica que existen todos los datos necesarios
	 * para crear, algo que solo se hace al llamar a crera.
	 * 
	 * @param modelName  Nombre Xava de la entidad. No puede ser nulo.
	 * @param values  Nuevos valores a establecer. No puede ser nulo.
	 * @return Lista de mensajes con errores de valicación. Nunca nulo.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.	  
	 */	
	public static Messages validate(
		java.lang.String modelName,		
		Map values)
		throws XavaException,  RemoteException 
	{
		Assert.arg(modelName, values);			
		throw new UnsupportedOperationException("Método todavía no implementado");				
	}
	
	/**
	 * Convierte un mapa de objetos en la clave primaria del <i>EntityBean</i>. <p>
	 * 
	 * Solo funciona si el compomente especificado esta implementado usando
	 * <i>EntityBeans</i>. <br>	 
	 */		
	
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
	
	

	/**
	 * Borra un elemento de una colección. <p>
	 * 
	 * Si es un agregado borra el agregado, y si es una referencia a entidad
	 * hace que deje de apuntar al objeto padre, y por ende ya no está en la colección.<br>
	 * 
	 * Los agregados no se deben borrar directamente, sino mediante este método
	 * ya que así se ejecuta toda la lógica necesaria para quitar el elemento de la
	 * colección, que a veces puede ser algo más que borrar el agregado.<br> 
	 * 
	 * @param modelName  Nombre Xava del modelo. No puede ser nulo.
	 * @param keyValues  Valores de la clave de la entidad a buscar. No puede ser nulo.
	 * @param collectionName  Nombre de la colección del elemento a borrar. No puede ser nulo.
	 * @param collectionElementKeyValues  Clave del elemento a borrar. No puede ser nulo.
	 * @exception FinderException  Problema de lógica al buscar.
	 * @exception ObjectNotFoundException  No existe una entidad con esa clave.
	 * @exception ValidationException  Problema al validar al realizar el borrado.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacción.
	 * @exception RemoveExction Problema de lógica al realizar el borrado.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */	

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