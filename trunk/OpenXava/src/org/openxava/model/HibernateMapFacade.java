package org.openxava.model;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;
import javax.ejb.ObjectNotFoundException;

import net.sf.hibernate.*;
import net.sf.hibernate.cfg.*;

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
	 * @exception CreateException  Problema de l�gica al crear.
	 * @exception ValidacionException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacci�n.
	 * @exception RemoteException  Problemas de sistema. Anula la transacci�n.
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
	 * @param nombreModelo  Nombre Xava del modelo. No puede ser nulo.
	 * @param claveContenedor  Clave de la entidad o agregado que contiene al agregado.
	 * @param contador  Contador usado para generar la clave del agregado, indica el n�mero
	 * 				de orden. La implementaci�n del agregado puede optar por ignorarlo.
	 * @param valores  Valores iniciales para crear el agregado. No puede ser nulo.
	 * @return Agregado creadado, no es un mapa es el objeto creado
	 *          (EntityBean, objeto jdo o cualquiera que sea
	 *           el modelos subyacente usado). Nunca nulo.
	 * @exception CreateException  Problema de l�gica al crear.
	 * @exception ValidacionException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacci�n.
	 * @exception RemoteException  Problemas de sistema. Anula la transacci�n.
	 */
	public static Object crearAgregado(String nombreModelo, Map claveContenedor, int contador, Map valores) 
		throws
			CreateException,ValidationException, 
			XavaException, RemoteException 
	{
		Assert.arg(nombreModelo, claveContenedor, valores);
		throw new UnsupportedOperationException("M�todo todav�a no implementado");					
	}
	
	/**	 
	 * Crea un nueva agregado a partir de un mapa con
	 * sus valores iniciales. <p>
	 * 
	 * @param nombreModelo  Nombre Xava del modelo. No puede ser nulo.
	 * @param contenedor  Objecto contenedor o clave (en formato objeto) del objeto contenedor.
	 * @param contador  Contador usado para generar la clave del agregado, indica el n�mero
	 * 				de orden. La implementaci�n del agregado puede optar por ignorarlo.
	 * @param valores  Valores iniciales para crear el agregado. No puede ser nulo.
	 * @return Agregado creadado, no es un mapa es el objeto creado
	 *          (EntityBean, objeto jdo o cualquiera que sea
	 *           el modelos subyacente usado). Nunca nulo.
	 * @exception CreateException  Problema de l�gica al crear.
	 * @exception ValidationException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacci�n.
	 * @exception RemoteException  Problemas de sistema. Anula la transacci�n.
	 */
	public static Object crearAgregado(String nombreModelo, Object contenedor, int contador, Map valores) 
		throws
			CreateException,ValidationException, 
			XavaException, RemoteException 
	{
		Assert.arg(nombreModelo, contenedor, valores);
		throw new UnsupportedOperationException("M�todo todav�a no implementado");					
	}
	
	/**	 
	 * Crea una nueva entidad a partir de un mapa con sus valores iniciales y
	 * devuelve un mapa con los valores de la entidad creada.
	 * <p>
	 *
	 * @param nombreModelo  Nombre Xava del modelo. No puede ser nulo.
	 * @param valores  Valores iniciales para crear la entidad. No puede ser nulo.
	 * @return Un mapa con los valores del objeto creado. Las propiedades
	 * contenidas son las enviadas al crear.
	 * @exception CreateException  Problema de l�gica al crear.
	 * @exception ValidationException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacci�n.
	 * @exception RemoteException  Problemas de sistema. Anula la transacci�n.
	 */
	public static Map crearDevolviendoValores(String nombreModelo, Map valores)
		throws
			CreateException,ValidationException,
			XavaException, RemoteException
	{
		Assert.arg(nombreModelo, valores);		
		throw new UnsupportedOperationException("M�todo todav�a no implementado");
	}
	
	
	/**
	 * Crea una nueva entidad a partir de un mapa con sus valores iniciales y
	 * devuelve un mapa con los valores de la clave de la entidad creada.
	 * <p>
	 *
	 * @param nombreModelo  Nombre Xava del modelo. No puede ser nulo.
	 * @param valores  Valores iniciales para crear la entidad. No puede ser nulo.
	 * @return Un mapa con los valores de la clave del objeto creado.
	 * @exception CreateException  Problema de l�gica al crear.
	 * @exception ValidationException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacci�n.
	 * @exception RemoteException  Problemas de sistema. Anula la transacci�n.
	 */
	public static Map crearDevolviendoClave(String nombreModelo, Map valores)
		throws
			CreateException,ValidationException,
			XavaException, RemoteException
	{
		Assert.arg(nombreModelo, valores);		
		throw new UnsupportedOperationException("M�todo todav�a no implementado");
	}
	
	
	
	
	/**	 	 
	 * Crea un nuevo agregado a partir de un mapa con
	 * sus valores iniciales y devuelve un mapa con la clave. <p>
	 * 
	 * @param nombreModelo  Nombre Xava del modelo. No puede ser nulo.
	 * @param claveContenedor  Clave de la entidad o agregado que contiene al agregado.
	 * @param contador  Contador usado para generar la clave del agregado, indica el n�mero
	 * 				de orden. La implementaci�n del agregado puede optar por ignorarlo.
	 * @param valores  Valores iniciales para crear el agregado. No puede ser nulo.
	 * @return Un mapa con la clave del agregado creado
	 * @exception CreateException  Problema de l�gica al crear.
	 * @exception ValidationException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacci�n.
	 * @exception RemoteException  Problemas de sistema. Anula la transacci�n.
	 */
	public static Map crearAgregadoDevolviendoClave(String nombreModelo, Map claveContenedor, int contador, Map valores) 
		throws
			CreateException,ValidationException, 
			XavaException, RemoteException 
	{
		Assert.arg(nombreModelo, claveContenedor, valores);
		throw new UnsupportedOperationException("M�todo todav�a no implementado");							
	}
	
	
	/**
	 * Obtiene los valores especificados de la entidad/agreado a partir de un mapa con 
	 * los valores de la clave primaria. <p>
	 * 
	 * El parametro <tt>nombreMiembros</tt> es un mapa para
	 * poder tener una estructura jerarquica.
	 * Los nombres de propiedades est�n en la parte
	 * de clave. Si es una propiedad simple el valor ser� nulo, y
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
	 * @param nombreModelo  Nombre Xava del modelo. Puede ser el nombre
	 * 	de un agregado cualificado. No puede ser nulo.
	 * @param valoresClave  Valores de la clave de la entidad a buscar. No puede ser nulo.
	 * @param nombreMiembros  Nombres de los miembros de los que se obtendr� 
	 * 						  la informaci�n. No puede ser nulo. 
	 * @return Mapa con los datos de la entidad. Nunca nulo.
	 * @exception FinderException  Problema de l�gica al buscar.
	 * @exception ObjectNotFoundException  No existe una entidad con esa clave.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacci�n.
	 * @exception RemoteException  Problemas de sistema. Anula la transacci�n.
	 */	
	/*
	public static Map getValores(
		String nombreModelo,
		Map valoresClave,
		Map nombreMiembros)
		throws FinderException, XavaException, RemoteException 
	{
		Assert.arg(nombreModelo, valoresClave, nombreMiembros);		
		if (valoresClave.isEmpty()) {
			throw new ObjectNotFoundException("Objeto de tipo " + nombreModelo + " con clave vac�a no existe");						
		}
		PersistenceManager manager = getFactory().getPersistenceManager();
		try {			
			manager.currentTransaction().begin();
			impl.setSession(manager);
			Map valores = impl.getValues(nombreModelo, valoresClave, nombreMiembros);
			manager.currentTransaction().commit();
			manager.close();
			return valores;
		}
		catch (FinderException ex) {
			ex.printStackTrace();
			manager.currentTransaction().rollback();
			manager.close();
			throw ex;						
		}
		catch (XavaException ex) {
			ex.printStackTrace();
			manager.currentTransaction().rollback();
			manager.close();
			throw ex;						
		}
		catch (Exception ex) {
			ex.printStackTrace();
			manager.currentTransaction().rollback();
			manager.close();
			throw new RemoteException("Error de sistema al intengar obtener datos de " + nombreModelo + ": " + ex.getLocalizedMessage());						
		}						
	}
	*/
		
	/**
	 * Obtiene todos los valores de la entidad a partir de la propia entidad. <p>
	 * 
	 * El parametro <tt>nombreMiembros</tt> es un mapa para
	 * poder tener una estructura jerarquica.
	 * Los nombres de propiedades est�n en la parte
	 * de clave. Si es una propiedad simple el valor ser� nulo, y
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
	 * @param nombreModelo  Nombre Xava del modelo. No puede ser nulo.
	 * @param entidad  Entidad de la queremos obtener los valores. No puede ser nulo.
	 * @param nombreMiembros  Nombres de los miembros de los que se obtendr� 
	 * 						  la informaci�n. Los nombres se guardan
	 *                        en la clave del mapa. Nuncan nulo. 
	 * @return Mapa con los datos de la entidad. Nunca nulo.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacci�n.
	 * @exception RemoteException  Problemas de sistema. Anula la transacci�n.
	 */	
	public static Map getValores(String nombreModelo, Object entidad, Map nombreMiembros)
		throws XavaException, RemoteException 
	{
		Assert.arg(nombreModelo, entidad, nombreMiembros);
		throw new UnsupportedOperationException("M�todo todav�a no implementado");		
	}
	
	/**
	 * Obtiene la entidad a partir de un mapa con 
	 * los valores de la clave primaria. <p>
	 * 
	 * @param nombreModelo  Nombre Xava del modelo. No puede ser nulo.
	 * @param valoresClave  Valores de la clave de la entidad a buscar. No puede ser nulo.
 	 * @return La entidad. Nunca nulo.
	 * @exception FinderException  Problema de l�gica al buscar.
	 * @exception ObjectNotFoundException  No existe una entidad con esa clave.	 
	 * @exception RemoteException  Problemas de sistema. Anula la transacci�n.
	 */
	public static Object findEntidad(String nombreModelo, Map valoresClave)
		throws FinderException, RemoteException 
	{	
		if (valoresClave==null) return null;
		Assert.arg(nombreModelo, valoresClave);
		throw new UnsupportedOperationException("M�todo todav�a no implementado");		
	}	

	/**
	 * Borra la entidad a partir de un mapa con
	 * su clave. <p>
	 * 
	 * @param nombreModelo  Nombre Xava del modelo. No puede ser nulo.
	 * @param valoresClave  Valores con la clave de la entidad a borrar. Nunca nulo.
	 * @return Entidad creadada, no es un mapa es el objeto creado
	 *          (EntityBean, objeto jdo o cualquiera que sea
	 *           el modelos subyacente usado). Nunca nulo.
	 * @exception RemoveException  Problema de l�gica al borrar.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacci�n.
	 * @exception RemoteException  Problemas de sistema. Anula la transacci�n.
	 * @exception ValidationException  Si alg�n problema de validaci�n impide que se borre la entidad
	 */
	/*
	public static void borrar(String nombreModelo, Map valoresClave)
		throws RemoveException, RemoteException, XavaException, ValidationException {
		Assert.arg(nombreModelo, valoresClave);
		PersistenceManager manager = getFactory().getPersistenceManager();
		try {			
			manager.currentTransaction().begin();
			impl.setSession(manager);
			impl.borrar(nombreModelo, valoresClave);
			manager.currentTransaction().commit();
			manager.close();			
		}
		catch (RemoveException ex) {
			ex.printStackTrace();
			manager.currentTransaction().rollback();
			manager.close();
			throw ex;						
		}
		catch (ValidationException ex) {
			ex.printStackTrace();
			manager.currentTransaction().rollback();
			manager.close();
			throw ex;						
		}
		catch (XavaException ex) {
			ex.printStackTrace();
			manager.currentTransaction().rollback();
			manager.close();
			throw ex;						
		}
		catch (Exception ex) {
			ex.printStackTrace();
			manager.currentTransaction().rollback();
			manager.close();
			throw new RemoteException("Error de sistema al intengar borrar un objeto de tipo " + nombreModelo + ": " + ex.getLocalizedMessage());						
		}																			
	}
	*/

	/**
	 * Establece nuevos valores en la entidad obtenida a partir de un mapa con 
	 * los valores de la clave primaria. <p>
	 * 
	 * @param nombreModelo  Nombre Xava del modelo. No puede ser nulo.
	 * @param valoresClave  Valores de la clave de la entidad a buscar. No puede ser nulo.
	 * @param valores  Nuevos valores a establecer. No puede ser nulo.
	 * @exception FinderException  Problema de l�gica al buscar.
	 * @exception ObjectNotFoundException  No existe una entidad con esa clave.
	 * @exception ValidationException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacci�n.
	 * @exception RemoteException  Problemas de sistema. Anula la transacci�n.
	 */	
	/*
	public static void setValores(
		java.lang.String nombreModelo,
		Map valoresClave,
		Map valores)
		throws FinderException,	ValidationException,
				XavaException,  RemoteException 
	{
		Assert.arg(nombreModelo, valoresClave, valores);				
		PersistenceManager manager = getFactory().getPersistenceManager();
		try {			
			manager.currentTransaction().begin();
			impl.setSession(manager);
			impl.setValues(nombreModelo, valoresClave, valores);
			manager.currentTransaction().commit();
			manager.close();			
		}
		catch (FinderException ex) {
			ex.printStackTrace();
			manager.currentTransaction().rollback();
			manager.close();
			throw ex;						
		}
		catch (ValidationException ex) {
			ex.printStackTrace();
			manager.currentTransaction().rollback();
			manager.close();
			throw ex;						
		}
		catch (XavaException ex) {
			ex.printStackTrace();
			manager.currentTransaction().rollback();
			manager.close();
			throw ex;						
		}
		catch (Exception ex) {
			ex.printStackTrace();
			manager.currentTransaction().rollback();
			manager.close();
			throw new RemoteException("Error de sistema al intengar obtener datos de " + nombreModelo + ": " + ex.getLocalizedMessage());						
		}																					
	}
	*/
	
	/**	 
	 * Valida el mapa con los datos enviados pero sin crear o modificar ninguna entidad. <p>
	 * 
	 * Solo valida los datos enviados, no certifica que existen todos los datos necesarios
	 * para crear, algo que solo se hace al llamar a crera.
	 * 
	 * @param nombreModelo  Nombre Xava de la entidad. No puede ser nulo.
	 * @param valores  Nuevos valores a establecer. No puede ser nulo.
	 * @return Lista de mensajes con errores de valicaci�n. Nunca nulo.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacci�n.
	 * @exception RemoteException  Problemas de sistema. Anula la transacci�n.	  
	 */	
	public static Messages validar(
		java.lang.String nombreModelo,		
		Map valores)
		throws XavaException,  RemoteException 
	{
		Assert.arg(nombreModelo, valores);			
		throw new UnsupportedOperationException("M�todo todav�a no implementado");				
	}
	
	/**
	 * Convierte un mapa de objetos en la clave primaria del <i>EntityBean</i>. <p>
	 * 
	 * Solo funciona si el compomente especificado esta implementado usando
	 * <i>EntityBeans</i>. <br>	 
	 */		
	/*
	public static Object toPrimaryKey(String nombreEntidad, Map valoresClave) throws XavaException {
		try {
			MetaEntidadEjb m = (MetaEntidadEjb) Componente.get(nombreEntidad).getMetaEntidad();
			return m.obtenerPrimaryKeyAPartirDeClave(valoresClave);
		}
		catch (ClassCastException ex) {
			ex.printStackTrace();
			throw new XavaException("La entidad del componente " + nombreEntidad + " no est� implementado como un EntityBean");
		}
	}
	*/
	

	/**
	 * Borra un elemento de una colecci�n. <p>
	 * 
	 * Si es un agregado borra el agregado, y si es una referencia a entidad
	 * hace que deje de apuntar al objeto padre, y por ende ya no est� en la colecci�n.<br>
	 * 
	 * Los agregados no se deben borrar directamente, sino mediante este m�todo
	 * ya que as� se ejecuta toda la l�gica necesaria para quitar el elemento de la
	 * colecci�n, que a veces puede ser algo m�s que borrar el agregado.<br> 
	 * 
	 * @param nombreModelo  Nombre Xava del modelo. No puede ser nulo.
	 * @param valoresClave  Valores de la clave de la entidad a buscar. No puede ser nulo.
	 * @param nombreColeccion  Nombre de la colecci�n del elemento a borrar. No puede ser nulo.
	 * @param valoresClaveElementoColeccion  Clave del elemento a borrar. No puede ser nulo.
	 * @exception FinderException  Problema de l�gica al buscar.
	 * @exception ObjectNotFoundException  No existe una entidad con esa clave.
	 * @exception ValidationException  Problema al validar al realizar el borrado.
	 * @exception XavaException  Problemas relacionados con Xava. Anula la transacci�n.
	 * @exception RemoveExction Problema de l�gica al realizar el borrado.
	 * @exception RemoteException  Problemas de sistema. Anula la transacci�n.
	 */	

	public static void borrarElementoDeColeccion(String nombreModelo, Map valoresClave, String nombreColeccion, Map valoresClaveElementoColeccion) 
		throws FinderException,	ValidationException, RemoveException,
			XavaException,  RemoteException 
	{
		Assert.arg(nombreModelo, valoresClave, nombreColeccion, valoresClaveElementoColeccion);
		throw new UnsupportedOperationException("M�todo todav�a no implementado");		
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