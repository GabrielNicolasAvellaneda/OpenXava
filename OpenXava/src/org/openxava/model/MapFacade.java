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
 * Permite manipular los objetos del modelo en formato
 * mapa de Java. <p>
 * 
 * @author Javier Paniza
 */
public class MapFacade {
	
	private static Map remotes;


	/**
	 * Crea una nueva entidad a partir de un mapa con
	 * sus valores iniciales. <p>
	 * 
	 * @param nombreModelo  Nombre Xava del modelo. No puede ser nulo.
	 * @param valores  Valores iniciales para crear la entidad. No puede ser nulo.
	 * @return Entidad creadada, no es un mapa es el objeto creado
	 *          (EntityBean, objeto jdo o cualquiera que sea
	 *           el modelos subyacente usado). Nunca nulo.
	 * @exception CreateException  Problema de lógica al crear.
	 * @exception ValidationException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con OpenXava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */
	public static Object create(String nombreModelo, Map valores) 
		throws
			CreateException,ValidationException, 
			XavaException, RemoteException 
	{
		Assert.arg(nombreModelo, valores);					
		try {									
			return getRemote(nombreModelo).create(nombreModelo, valores);
		}
		catch (RemoteException ex) {
			anularRemote(nombreModelo);
			return getRemote(nombreModelo).create(nombreModelo, valores);
		}							
	}
	
	/**	 
	 * Crea un nueva agregado a partir de un mapa con
	 * sus valores iniciales. <p>
	 * 
	 * @param nombreModelo  Nombre Xava del modelo. No puede ser nulo.
	 * @param claveContenedor  Clave de la entidad o agregado que contiene al agregado.
	 * @param contador  Contador usado para generar la clave del agregado, indica el número
	 * 				de orden. La implementación del agregado puede optar por ignorarlo.
	 * @param valores  Valores iniciales para crear el agregado. No puede ser nulo.
	 * @return Agregado creadado, no es un mapa es el objeto creado
	 *          (EntityBean, objeto jdo o cualquiera que sea
	 *           el modelos subyacente usado). Nunca nulo.
	 * @exception CreateException  Problema de lógica al crear.
	 * @exception ValidationException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con OpenXava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */
	public static Object createAggregate(String nombreModelo, Map claveContenedor, int contador, Map valores) 
		throws
			CreateException,ValidationException, 
			XavaException, RemoteException 
	{
		Assert.arg(nombreModelo, claveContenedor, valores);					
		try {		
			return getRemote(nombreModelo).createAggregate(nombreModelo, claveContenedor, contador, valores);
		}
		catch (RemoteException ex) {
			anularRemote(nombreModelo);
			return getRemote(nombreModelo).createAggregate(nombreModelo, claveContenedor, contador, valores);
		}							
	}
	
	/**	 
	 * Crea un nueva agregado a partir de un mapa con
	 * sus valores iniciales. <p>
	 * 
	 * @param nombreModelo  Nombre Xava del modelo. No puede ser nulo.
	 * @param contenedor  Objecto contenedor o clave (en formato objeto) del objeto contenedor.
	 * @param contador  Contador usado para generar la clave del agregado, indica el número
	 * 				de orden. La implementación del agregado puede optar por ignorarlo.
	 * @param valores  Valores iniciales para crear el agregado. No puede ser nulo.
	 * @return Agregado creadado, no es un mapa es el objeto creado
	 *          (EntityBean, objeto jdo o cualquiera que sea
	 *           el modelos subyacente usado). Nunca nulo.
	 * @exception CreateException  Problema de lógica al crear.
	 * @exception ValidationException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con OpenXava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */
	public static Object createAggregate(String nombreModelo, Object contenedor, int contador, Map valores) 
		throws
			CreateException,ValidationException, 
			XavaException, RemoteException 
	{
		Assert.arg(nombreModelo, contenedor, valores);					
		try {									
			return getRemote(nombreModelo).createAggregate(nombreModelo, contenedor, contador, valores);
		}
		catch (RemoteException ex) {
			anularRemote(nombreModelo);
			return getRemote(nombreModelo).createAggregate(nombreModelo, contenedor, contador, valores);
		}							
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
	 * @exception CreateException  Problema de lógica al crear.
	 * @exception ValidationException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con OpenXava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */
	public static Map createReturningValues(String nombreModelo, Map valores)
		throws
			CreateException,ValidationException,
			XavaException, RemoteException
	{
		Assert.arg(nombreModelo, valores);		
		try {
			return getRemote(nombreModelo).createReturningValues(nombreModelo, valores);
		}
		catch (RemoteException ex) {
			anularRemote(nombreModelo);
			return getRemote(nombreModelo).createReturningValues(nombreModelo, valores);
		}
		
	}
	
	
	/**
	 * Crea una nueva entidad a partir de un mapa con sus valores iniciales y
	 * devuelve un mapa con los valores de la clave de la entidad creada.
	 * <p>
	 *
	 * @param nombreModelo  Nombre Xava del modelo. No puede ser nulo.
	 * @param valores  Valores iniciales para crear la entidad. No puede ser nulo.
	 * @return Un mapa con los valores de la clave del objeto creado.
	 * @exception CreateException  Problema de lógica al crear.
	 * @exception ValidationException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con OpenXava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */
	public static Map createReturningKey(String nombreModelo, Map valores)
		throws
			CreateException,ValidationException,
			XavaException, RemoteException
	{
		Assert.arg(nombreModelo, valores);		
		try {
			return getRemote(nombreModelo).createReturningKey(nombreModelo, valores);
		}
		catch (RemoteException ex) {
			anularRemote(nombreModelo);
			return getRemote(nombreModelo).createReturningKey(nombreModelo, valores);
		}
		
	}
	
	
	
	
	/**	 	 
	 * Crea un nuevo agregado a partir de un mapa con
	 * sus valores iniciales y devuelve un mapa con la clave. <p>
	 * 
	 * @param nombreModelo  Nombre Xava del modelo. No puede ser nulo.
	 * @param claveContenedor  Clave de la entidad o agregado que contiene al agregado.
	 * @param contador  Contador usado para generar la clave del agregado, indica el número
	 * 				de orden. La implementación del agregado puede optar por ignorarlo.
	 * @param valores  Valores iniciales para crear el agregado. No puede ser nulo.
	 * @return Un mapa con la clave del agregado creado
	 * @exception CreateException  Problema de lógica al crear.
	 * @exception ValidationException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con OpenXava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */
	public static Map createAggregateReturningKey(String nombreModelo, Map claveContenedor, int contador, Map valores) 
		throws
			CreateException,ValidationException, 
			XavaException, RemoteException 
	{
		Assert.arg(nombreModelo, claveContenedor, valores);					
		try {		
			return getRemote(nombreModelo).createAggregateReturningKey(nombreModelo, claveContenedor, contador, valores);
		}
		catch (RemoteException ex) {
			anularRemote(nombreModelo);
			return getRemote(nombreModelo).createAggregateReturningKey(nombreModelo, claveContenedor, contador, valores);
		}							
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
	 * @param nombreModelo  Nombre Xava del modelo. Puede ser el nombre
	 * 	de un agregado cualificado. No puede ser nulo.
	 * @param valoresClave  Valores de la clave de la entidad a buscar. No puede ser nulo.
	 * @param nombreMiembros  Nombres de los miembros de los que se obtendrá 
	 * 						  la información. No puede ser nulo. 
	 * @return Mapa con los datos de la entidad. Nunca nulo.
	 * @exception FinderException  Problema de lógica al buscar.
	 * @exception ObjectNotFoundException  No existe una entidad con esa clave.
	 * @exception XavaException  Problemas relacionados con OpenXava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */	
	public static Map getValues(
		String nombreModelo,
		Map valoresClave,
		Map nombreMiembros)
		throws FinderException, XavaException, RemoteException 
	{						
		Assert.arg(nombreModelo, valoresClave, nombreMiembros);		
		if (valoresClave.isEmpty()) {
			throw new ObjectNotFoundException(XavaResources.getString("empty_key_object_not_found", nombreModelo));						
		}
		try {					
			return getRemote(nombreModelo).getValues(nombreModelo, valoresClave, nombreMiembros);
		}
		catch (RemoteException ex) {
			anularRemote(nombreModelo);
			return getRemote(nombreModelo).getValues(nombreModelo, valoresClave, nombreMiembros);
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
	 * @param nombreModelo  Nombre Xava del modelo. No puede ser nulo.
	 * @param entidad  Entidad de la queremos obtener los valores. No puede ser nulo.
	 * @param nombreMiembros  Nombres de los miembros de los que se obtendrá 
	 * 						  la información. Los nombres se guardan
	 *                        en la clave del mapa. Nuncan nulo. 
	 * @return Mapa con los datos de la entidad. Nunca nulo.
	 * @exception XavaException  Problemas relacionados con OpenXava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */	
	public static Map getValues(String nombreModelo, Object entidad, Map nombreMiembros)
		throws XavaException, RemoteException 
	{		
		Assert.arg(nombreModelo, entidad, nombreMiembros);
		try {
			return getRemote(nombreModelo).getValues(nombreModelo, entidad, nombreMiembros);
		}
		catch (RemoteException ex) {
			anularRemote(nombreModelo);
			return getRemote(nombreModelo).getValues(nombreModelo, entidad, nombreMiembros);
		}
			
	}
	
	/**
	 * Obtiene la entidad a partir de un mapa con 
	 * los valores de la clave primaria. <p>
	 * 
	 * @param nombreModelo  Nombre Xava del modelo. No puede ser nulo.
	 * @param valoresClave  Valores de la clave de la entidad a buscar. No puede ser nulo.
 	 * @return La entidad. Nunca nulo.
	 * @exception FinderException  Problema de lógica al buscar.
	 * @exception ObjectNotFoundException  No existe una entidad con esa clave.	 
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */
	public static Object findEntity(String nombreModelo, Map valoresClave)
		throws FinderException, RemoteException 
	{	
		if (valoresClave==null) return null;
		Assert.arg(nombreModelo, valoresClave);
		try {
			return getRemote(nombreModelo).findEntity(nombreModelo, valoresClave);
		}
		catch (RemoteException ex) {
			anularRemote(nombreModelo);
			return getRemote(nombreModelo).findEntity(nombreModelo, valoresClave);
		}					
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
	 * @exception RemoveException  Problema de lógica al borrar.
	 * @exception XavaException  Problemas relacionados con OpenXava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 * @exception ValidationException  Si algún problema de validación impide que se borre la entidad
	 */
	public static void remove(String nombreModelo, Map valoresClave)
		throws RemoveException, RemoteException, XavaException, ValidationException {
		Assert.arg(nombreModelo, valoresClave);
		try {
			getRemote(nombreModelo).remove(nombreModelo, valoresClave);
		}
		catch (RemoteException ex) {
			anularRemote(nombreModelo);
			getRemote(nombreModelo).remove(nombreModelo, valoresClave);
		}
		
	}

	/**
	 * Establece nuevos valores en la entidad obtenida a partir de un mapa con 
	 * los valores de la clave primaria. <p>
	 * 
	 * @param nombreModelo  Nombre Xava del modelo. No puede ser nulo.
	 * @param valoresClave  Valores de la clave de la entidad a buscar. No puede ser nulo.
	 * @param valores  Nuevos valores a establecer. No puede ser nulo.
	 * @exception FinderException  Problema de lógica al buscar.
	 * @exception ObjectNotFoundException  No existe una entidad con esa clave.
	 * @exception ValidationException  Problema al validar los valores enviados.
	 * @exception XavaException  Problemas relacionados con OpenXava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */	
	public static void setValues(
		java.lang.String nombreModelo,
		Map valoresClave,
		Map valores)
		throws FinderException,	ValidationException,
				XavaException,  RemoteException 
	{
		Assert.arg(nombreModelo, valoresClave, valores);				
		try {			
			getRemote(nombreModelo).setValues(nombreModelo, valoresClave, valores);								
		}
		catch (RemoteException ex) {
			anularRemote(nombreModelo);
			getRemote(nombreModelo).setValues(nombreModelo, valoresClave, valores);			
		}				
	}
	
	/**	 
	 * Valida el mapa con los datos enviados pero sin crear o modificar ninguna entidad. <p>
	 * 
	 * Solo valida los datos enviados, no certifica que existen todos los datos necesarios
	 * para crear, algo que solo se hace al llamar a crera.
	 * 
	 * @param nombreModelo  Nombre Xava de la entidad. No puede ser nulo.
	 * @param valores  Nuevos valores a establecer. No puede ser nulo.
	 * @return Lista de mensajes con errores de valicación. Nunca nulo.
	 * @exception XavaException  Problemas relacionados con OpenXava. Anula la transacción.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.	  
	 */	
	public static Messages validate(
		java.lang.String nombreModelo,		
		Map valores)
		throws XavaException,  RemoteException 
	{
		Assert.arg(nombreModelo, valores);			
		try {
			return getRemote(nombreModelo).validate(nombreModelo, valores);								
		}
		catch (RemoteException ex) {
			anularRemote(nombreModelo);
			return getRemote(nombreModelo).validate(nombreModelo, valores);			
		}
				
	}
	
									
	
	private static MapFacadeRemote getRemote(String nombreModelo) throws RemoteException {
		try {			
			int idx = nombreModelo.indexOf('.'); 
			if (idx >=0) {
				nombreModelo = nombreModelo.substring(0, idx);				 				
			}			
			String paquete = MetaComponent.get(nombreModelo).getPackageNameWithSlashWithoutModel();			
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
			throw new RemoteException(XavaResources.getString("facade_remote", nombreModelo));
		}		
	}
			
	/**
	 * Convierte un mapa de objetos en la clave primaria del <i>EntityBean</i>. <p>
	 * 
	 * Solo funciona si el compomente especificado esta implementado usando
	 * <i>EntityBeans</i>. <br>	 
	 */		
	public static Object toPrimaryKey(String nombreEntidad, Map valoresClave) throws XavaException {
		try {
			MetaEntityEjb m = (MetaEntityEjb) MetaComponent.get(nombreEntidad).getMetaEntity();
			return m.obtainPrimaryKeyFromKey(valoresClave);
		}
		catch (ClassCastException ex) {
			ex.printStackTrace();
			throw new XavaException("no_entity_bean", nombreEntidad);
		}
	}
	
	private static Map getRemotes() {
		if (remotes == null) {
			remotes = new HashMap();
		}
		return remotes;
	}
	
	private static void anularRemote(String nombreModelo) {
		try {
			int idx = nombreModelo.indexOf('.'); 
			if (idx >=0) {
				nombreModelo = nombreModelo.substring(0, idx);				 				
			}			
			String paquete = MetaComponent.get(nombreModelo).getPackageNameWithSlashWithoutModel();			
			getRemotes().remove(paquete);			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("¡ADVERTENCIA! Imposible eliminar FachadaMapaRemote del caché local");
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
	 * @param nombreModelo  Nombre Xava del modelo. No puede ser nulo.
	 * @param valoresClave  Valores de la clave de la entidad a buscar. No puede ser nulo.
	 * @param nombreColeccion  Nombre de la colección del elemento a borrar. No puede ser nulo.
	 * @param valoresClaveElementoColeccion  Clave del elemento a borrar. No puede ser nulo.
	 * @exception FinderException  Problema de lógica al buscar.
	 * @exception ObjectNotFoundException  No existe una entidad con esa clave.
	 * @exception ValidationException  Problema al validar al realizar el borrado.
	 * @exception XavaException  Problemas relacionados con OpenXava. Anula la transacción.
	 * @exception RemoveExction Problema de lógica al realizar el borrado.
	 * @exception RemoteException  Problemas de sistema. Anula la transacción.
	 */	

	public static void removeCollectionElement(String nombreModelo, Map valoresClave, String nombreColeccion, Map valoresClaveElementoColeccion) 
		throws FinderException,	ValidationException, RemoveException,
			XavaException,  RemoteException 
	{
		Assert.arg(nombreModelo, valoresClave, nombreColeccion, valoresClaveElementoColeccion);
		try {
			getRemote(nombreModelo).removeCollectionElement(nombreModelo, valoresClave, nombreColeccion, valoresClaveElementoColeccion);
		}
		catch (RemoteException ex) {
			anularRemote(nombreModelo);
			getRemote(nombreModelo).removeCollectionElement(nombreModelo, valoresClave, nombreColeccion, valoresClaveElementoColeccion);
		}
	}	

}