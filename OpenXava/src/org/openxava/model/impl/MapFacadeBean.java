package org.openxava.model.impl;

import java.lang.reflect.*;
import java.rmi.*;
import java.util.*;

import javax.ejb.*;
import javax.rmi.*;

import org.hibernate.Session;

import org.openxava.calculators.*;
import org.openxava.component.*;
import org.openxava.ejbx.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;
import org.openxava.validators.*;
import org.openxava.validators.meta.*;


/**
 * This is a Session Bean Class
 */
public class MapFacadeBean implements SessionBean {
	private javax.ejb.SessionContext mySessionCtx = null;
	private final static long serialVersionUID = 3206093459760846163L;
	private IPersistenceProvider persistenceProvider;
	private Session session;

	/**
	 * 
	 * @param entidad javax.ejb.EJBObject
	 * @param valores java.util.Map
	 */
	private Map convertSubmapsInObject(MetaModel metaModelo, Map valores, boolean referencesAsKey)
		throws ValidationException, XavaException {		
		Map result = new HashMap();		
		Iterator it = valores.entrySet().iterator();				
		while (it.hasNext()) {
			Map.Entry en = (Map.Entry) it.next();
			String nombreMiembro = (String) en.getKey();
			Object valor = null;			
			if (metaModelo.containsMetaProperty(nombreMiembro)) {
				valor = en.getValue();
			} else
				if (metaModelo.containsMetaReference(nombreMiembro)) {
					MetaReference ref = metaModelo.getMetaReference(nombreMiembro);
					if (!referencesAsKey || ref.isAggregate()) {
						valor = mapaToObjetoReferenciado(metaModelo, nombreMiembro, (Map) en.getValue());
					}
					else {
						MetaEntityEjb entidadReferenciada = (MetaEntityEjb) ref.getMetaModelReferenced();
						nombreMiembro = nombreMiembro + "Key";						
						valor = entidadReferenciada.obtainPrimaryKeyFromKey((Map) en.getValue());									
					}
				} else if (metaModelo.getMapping().hasPropertyMapping(nombreMiembro)) {  
					valor = en.getValue();
				} else {
					throw new EJBException(
						"No se reconoce el miembro "
							+ nombreMiembro
							+ " en "
							+ metaModelo.getName());
				}
			result.put(nombreMiembro, valor);
		}
		return result;
	}
	/**
	 * 
	 * @param valores void
	 */
	public Object create(String nombreModelo, Map valores)
		throws CreateException, XavaException, ValidationException {
		MetaModel metaModelo = getMetaModel(nombreModelo);					
		return createEjb(metaModelo, valores, null, null, 0);			
	}
	
		
	public Map createReturningValues(String nombreEntidad, Map valores)
			throws CreateException, XavaException, ValidationException {
		MetaEntityEjb metaEntidad = (MetaEntityEjb) MetaComponent.get(nombreEntidad).getMetaEntity();
		Object entidad = createEjb(metaEntidad, valores, null, null, 0);
		return getValues(metaEntidad, entidad, valores);
	}
	
	public Map createReturningKey(String nombreEntidad, Map valores)
			throws CreateException, XavaException, ValidationException {
		MetaEntityEjb metaEntidad = (MetaEntityEjb) MetaComponent.get(nombreEntidad).getMetaEntity();
		Object entidad = createEjb(metaEntidad, valores, null, null, 0);
		return getValues(metaEntidad, entidad, getNombresClave(metaEntidad));
	}
	
	
	public Object createAggregate(String nombreModelo, Map valoresClaveContenedor, int contador, Map valores) 
		throws CreateException,ValidationException, XavaException 
	{		
		MetaModel metaModelo = getMetaModel(nombreModelo);
		MetaModel metaModeloContenedor = metaModelo.getMetaModelContainer();
		try {					
			Object claveContenedor = ((IMetaEjb) metaModeloContenedor).obtainPrimaryKeyFromKey(valoresClaveContenedor);
			return crearAgregado(metaModelo, claveContenedor, contador, valores);
		}
		catch (ClassCastException ex) {
			throw new XavaException(metaModeloContenedor.getName() + " ha de estar implementado como un EJB (y no como un bean) para poder crearlo con FachadaMapa.crearAgregado");					
		}
	}
	
	public Object createAggregate(String nombreModelo, Object contenedor, int contador, Map valores) 
		throws CreateException,ValidationException, XavaException
	{		
		MetaModel metaModelo = getMetaModel(nombreModelo);		
		return crearAgregado(metaModelo, contenedor, contador, valores);
	}
	
	public Map createAggregateReturningKey(String nombreModelo, Map valoresClaveContenedor, int contador, Map valores) 
		throws CreateException,ValidationException, XavaException 
	{		
		MetaModel metaModelo = getMetaModel(nombreModelo);
		MetaModel metaModeloContenedor = metaModelo.getMetaModelContainer();
		try {					
			Object claveContenedor = ((IMetaEjb) metaModeloContenedor).obtainPrimaryKeyFromKey(valoresClaveContenedor);
			Object agregado = crearAgregado(metaModelo, claveContenedor, contador, valores);						
			return getValues(metaModelo, agregado, getNombresClave(metaModelo));			
		}
		catch (ClassCastException ex) {
			throw new XavaException(metaModeloContenedor.getName() + " ha de estar implementado como un EJB (y no como un bean) para poder crearlo con FachadaMapa.crearAgregado");					
		}
	}
	
	
	private Map getNombresClave(MetaModel metaModelo) throws XavaException {
		Iterator itPropiedades = metaModelo.getKeyPropertiesNames().iterator();
		Map nombres = new HashMap();
		while (itPropiedades.hasNext()) {
			nombres.put(itPropiedades.next(), null);
		}
		Iterator itReferencias = metaModelo.getMetaReferencesKey().iterator();
		while (itReferencias.hasNext()) {
			MetaReference ref = (MetaReference) itReferencias.next();
			nombres.put(ref.getName(), getNombresClave(ref.getMetaModelReferenced()));
		}		
		return nombres;
	}
	
	private Object crearAgregado(MetaModel metaModelo, Object contenedor, int contador, Map valores) 
		throws CreateException,ValidationException, XavaException 
	{				
		MetaModel metaModeloContenedor = metaModelo.getMetaModelContainer();								
		int intentos = 0;
		// Un bucle con 10 intentos, por si el contador está repetido, por haber borrado
		do {				 
			try {
				return createEjb(metaModelo, valores, metaModeloContenedor, contenedor, contador);
			}
			catch (DuplicateKeyException ex) {
				if (intentos > 10) throw ex;
				contador++;
				intentos++;
			}				
		}
		while (true);			
	}
	
	

	/**
	 * Permite crear <i>EntityBeans</i> independientes o agregadas a otros
	 * <i>EntityBeans</i>. <p>
	 * 
	 * Una entidad independiente requiere un <i>home</i> con un
	 * método <tt>create(Map valoresIniciales)</tt>, mientras que una entidad agregada
	 * requiere un <i>home</i> con un método 
	 * <tt>create(Object entidadContenedora, int numeroSecuencia, Map valoresIniciales)</tt>. <p>
	 * 
	 * Si el argumento <tt>metaEntidadContenedora</tt> es nulo se tomará
	 * por una entidad independiente, en caso contrarío por una agregada.<p>
	 * 
	 * 
	 * @param metaEjb  de la entidad o agregado a crear. Ha de implementar IMetaEjb
	 * @param valores a asignar a la entidad a crear.
	 * @param metaEntidadContenedora  Solo si la entidad a crear es un agregado. Sería
	 *                                la metaentidad de la entidad contenedora.
	 * @param entidadContenedora Solo si la entidad a crear es un agregado. Sería
	 *                                la entidad contenedora.
	 * @param numero Solo si la entidad a crear es un agregado. Es un número de secuencia.
	 * @return La entidad creada.
	 */
	private Object createEjb(
		MetaModel metaEjb,
		Map valores,
		MetaModel metaModeloContenedor,
		Object containerKey,
		int numero)
		throws CreateException, ValidationException, XavaException {						
		try {
			if (!(metaEjb instanceof IMetaEjb)) {
				throw new IllegalArgumentException(XavaResources.getString("argument_type_error", "metaEjb", "MapFacadeBean.createEjb", "IMetaEjb"));
			}
			//quitarCamposSoloLectura(metaEjb, valores); // no quitamos los de solo lectura porque puede que se necesiten inicializar al crear
			quitarCamposCalculados(metaEjb, valores); 			
			Messages erroresValidacion = new Messages(); 
			validarExistenRequeridos(erroresValidacion, metaEjb, valores);
			Map colecciones = extractCollections(metaEjb, valores);			
			validar(erroresValidacion, metaEjb, valores, null, containerKey);
			removeViewProperties(metaEjb, valores); 
			if (erroresValidacion.contains()) {
				throw new ValidationException(erroresValidacion);			
			}
			Map valoresConvertidos = convertSubmapsInObject(metaEjb, valores, true);
			Object entidadNueva = null;
			if (metaModeloContenedor == null) {				
				entidadNueva = createPersistentObject((IMetaEjb)metaEjb, valoresConvertidos);
			} else {				
				entidadNueva =
					executeEJBCreate(
						(IMetaEjb) metaEjb,
						valoresConvertidos,
						metaModeloContenedor,
						containerKey,
						numero);
			}			

			if (colecciones != null) {
				añadirColecciones(metaEjb, entidadNueva, colecciones);
			}
			return entidadNueva;
		} catch (ValidationException ex) {
			throw ex;
		} catch (DuplicateKeyException ex) {
			ex.printStackTrace();
			throw new DuplicateKeyException(
				"Imposible crear una nueva entidad de tipo "
					+ metaEjb.getName()
					+ " porque ya existe un objeto con esa clave");				
		} catch (CreateException ex) {
			ex.printStackTrace();
			throw new CreateException(
				"Imposible crear una nueva entidad de tipo "
					+ metaEjb.getName()
					+ " por:\n"
					+ ex.getLocalizedMessage());
		} catch (RemoteException ex) {
			ex.printStackTrace();
			throw new EJBException(
				"Imposible crear una nueva entidad de tipo "
					+ metaEjb.getName()
					+ " por:\n"
					+ ex.getLocalizedMessage());
		} catch (XavaException ex) {
			setRollbackOnly();
			ex.printStackTrace();
			throw new XavaException(
				"Imposible crear una nueva entidad de tipo "
					+ metaEjb.getName()
					+ " por:\n"
					+ ex.getLocalizedMessage());
		}
	}


	protected Object createPersistentObject(IMetaEjb metaEjb, Map valores)
		throws CreateException, ValidationException, XavaException {
		try {
			return EJBFactory.create(metaEjb.obtainHome(), metaEjb.getHomeClass(), valores);		
		} 
		catch (NoSuchMethodException ex) {
			ex.printStackTrace();
			throw new XavaException(
				"Es obligado que el bean "
					+ metaEjb.getJndi()
					+ " tenga un constructor create(Map )");
		} 
		catch (ValidationException ex) {
			throw ex;
		} 
		catch (RemoteException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("create_persistent_error", ex.getLocalizedMessage()));
		}
	}


	protected Object executeEJBCreate(
		IMetaEjb metaEjb,
		Map valores,
		MetaModel metaModeloContenedor,
		Object modeloContenedor, // puede ser la clave
		int numero)
		throws CreateException, ValidationException, RemoteException, XavaException {
		Class claseDeContenedor = modeloContenedor.getClass();
		try {
			IMetaEjb contenedorEjb = (IMetaEjb) metaModeloContenedor; 
			if (!claseDeContenedor.equals(contenedorEjb.getPrimaryKeyClass())) {
				claseDeContenedor = contenedorEjb.getRemoteClass();
			}									 
			Class claseHomeAgregado = metaEjb.getHomeClass();
			Class[] argClass = { claseDeContenedor, int.class, java.util.Map.class };
			Method m = claseHomeAgregado.getDeclaredMethod("create", argClass);
			Object[] args = { modeloContenedor, new Integer(numero), valores };
			return m.invoke(metaEjb.obtainHome(), args);
		} catch (InvocationTargetException ex) {
			Throwable th = ex.getTargetException();
			try {
				throw th;
			} catch (CreateException ex2) {
				throw ex2;
			} catch (ValidationException ex2) {
				throw ex2;
			} catch (RemoteException ex2) {
				throw ex2;
			} catch (Throwable ex2) {
				throw new RemoteException(ex2.getLocalizedMessage(), ex2);
			}
		} catch (NoSuchMethodException ex) {
			throw new XavaException(
				"Es obligado que el bean "
					+ metaEjb.getJndi()
					+ " tenga un constructor "
					+ "create("
					+ claseDeContenedor
					+ "  , int , Map )");
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException(
				"Imposible crear un nuevo "
					+ metaEjb.getJndi()
					+ " por:\n"
					+ ex.getLocalizedMessage());
		}
	}


	/**
	 * @param valores Se modifica.
	 * @return Nulo si no hay ninguna.
	 */
	private Map extractCollections(MetaModel metaModelo, Map valores)
		throws XavaException {
		Iterator it = metaModelo.getColectionsNames().iterator();
		Map result = new HashMap();
		while (it.hasNext()) {
			Object nombre = it.next();
			if (valores.containsKey(nombre)) {
				result.put(nombre, valores.get(nombre));
				valores.remove(nombre);
			}
		}
		return result.size() == 0 ? null : result;
	}


	private void añadirColecciones(
		MetaModel metaModelo,
		Object modelo,
		Map colecciones)
		throws ValidationException, XavaException {
		Iterator it = colecciones.entrySet().iterator();
		Messages errores = new Messages();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			String nombreMiembro = (String) e.getKey();
			Collection coleccion = null;
			try {				
				coleccion = (Collection) e.getValue();
			}
			catch (ClassCastException ex) {
				throw new XavaException("collection_type_required", nombreMiembro, metaModelo.getName(), e.getValue().getClass().getName());
			}
			
			añadirColeccion(errores, metaModelo, modelo, nombreMiembro, coleccion);
			
			if (errores.contains()) {
				setRollbackOnly();
				throw new ValidationException(errores);
			}
		}
	}


	private void modifyCollections(
		MetaModel metaModelo,
		Object entidad,
		Map colecciones)
		throws ValidationException, XavaException {		
		Iterator it = colecciones.entrySet().iterator();
		Messages errores = new Messages();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			String nombreMiembro = (String) e.getKey();
			Collection coleccion = null;
			try {
				coleccion = (Collection) e.getValue();				
			}
			catch (ClassCastException ex) {
				throw new XavaException("collection_type_required", nombreMiembro, metaModelo.getName(), e.getValue().getClass().getName());
			}
			modificarColeccion(errores, metaModelo, entidad, nombreMiembro, coleccion);
		}
		if (errores.contains()) {
			setRollbackOnly();
			throw new ValidationException(errores);		
		}		
	} 


	private void modificarColeccion(
		Messages errores,
		MetaModel metaModelo,
		Object entidad,
		String nombreMiembro,
		Collection coleccion)
		throws XavaException {		
		try {
			// Lo siguiente no es la opción más eficiente
			refrescarColeccion(metaModelo, entidad, nombreMiembro, coleccion);
			borrarColeccion(metaModelo, entidad, nombreMiembro); 
			añadirColeccion(errores, metaModelo, entidad, nombreMiembro, coleccion);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("assign_collection_error", nombreMiembro, metaModelo.getName(), ex.getLocalizedMessage());
		}		
	}


	private void borrarColeccion(
		MetaModel metaModelo,
		Object entidad,
		MetaCollection metaColeccion)
		throws XavaException {
		try {
			if (metaColeccion.isAggregate()) {
				borrarColeccionAgregados(metaModelo, entidad, metaColeccion);
			} else {
				borrarColeccionEntidades(metaModelo, entidad, metaColeccion);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("remove_collection_error", metaColeccion.getName(), metaModelo.getName(), ex.getLocalizedMessage());
		}
	}


	private void borrarColeccion(
		MetaModel metaModelo,
		Object entidad,
		String nombreMiembro)
		throws XavaException {
		borrarColeccion(metaModelo, entidad, metaModelo.getMetaCollection(nombreMiembro));
	}


	private void borrarColeccionEntidades(
		MetaModel metaModelo,
		Object entidad,
		MetaCollection metaColeccion)
		throws XavaException {
		Object existentes =
			ejecutarGetXX(metaModelo, entidad, metaColeccion.getName());
		if (existentes instanceof Enumeration) {
			Enumeration e = (Enumeration) existentes;
			while (e.hasMoreElements()) {
				Object entidadAsociada = e.nextElement();
				ejecutarRemoveXX(metaModelo, entidad, metaColeccion, entidadAsociada);
			}
		}
		else if (existentes instanceof Collection){
			Iterator it = ((Collection) existentes).iterator();
			while (it.hasNext()) {
				Object entidadAsociada = it.next();
				ejecutarRemoveXX(metaModelo, entidad, metaColeccion, entidadAsociada);
			}			
		}
		else {
			throw new XavaException("collection_type_not_supported", existentes.getClass());
		}
	}


	private void borrarColeccionAgregados(
		MetaModel metaModelo,
		Object entidad,
		MetaCollection metaColeccion)
		throws XavaException, FinderException, ValidationException, RemoveException, RemoteException {
		Enumeration enum = null;	
		Object existentes =
			ejecutarGetXX(metaModelo, entidad, metaColeccion.getName());								
		if (existentes instanceof Enumeration) {
			enum = (Enumeration) existentes;
		}
		else if (existentes instanceof Collection) {
			enum = Collections.enumeration((Collection) existentes);
		}
		else {
			throw new XavaException("collection_type_not_supported");
		}									
		MetaModel metaModeloAgregado = metaColeccion.getMetaReference().getMetaModelReferenced();
		while (enum.hasMoreElements()) {
			try {		
				borrar(metaModeloAgregado, enum.nextElement());
			}
			catch (ValidationException ex) {
				setRollbackOnly();
				throw ex;
			}			
		}
	}


	private void borrarTodasColeccionesAgregados(
		MetaModel metaModelo,
		Object entidad)
		throws Exception {


		Iterator it =
			metaModelo.getMetaCollectionsAgregate().iterator();
		while (it.hasNext()) {
			MetaCollection metaColeccion = (MetaCollection) it.next();
			borrarColeccionAgregados(metaModelo, entidad, metaColeccion);
		}
	}


	private void añadirColeccion(
		Messages errores,
		MetaModel metaModelo,
		Object modelo,
		String nombreMiembro,
		Collection coleccion)
		throws XavaException {
		añadirColeccion(
			errores,
			metaModelo,
			modelo,
			metaModelo.getMetaCollection(nombreMiembro),
			coleccion);
	}


	private void añadirColeccion(
		Messages errores,
		MetaModel metaModelo,
		Object modelo,
		MetaCollection metaColeccion,
		Collection coleccion)
		throws XavaException {
		try {						
			metaColeccion.validate(errores, coleccion, null, null);
			MetaReference metaReferencia = metaColeccion.getMetaReference();
			if (metaReferencia.isAggregate()) {
				añadirColeccionAgregados(					
					metaModelo,
					modelo,
					metaColeccion,
					coleccion);
			} else { // Entidad normal
				añadirColeccionEntidades(metaModelo, modelo, metaColeccion, coleccion);
			}
		}
		catch (ValidationException ex) {
			errores.add(ex.getErrors());
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("add_to_collection_error",
					metaColeccion.getName(),
					metaModelo.getName(),
					ex.getLocalizedMessage());
		}
	}
	
	private void refrescarColeccion(		
		MetaModel metaModelo,
		Object modelo,
		String nombreMiembro,
		Collection coleccion)
		throws XavaException {
		try {									
			MetaCollection metaColeccion = metaModelo.getMetaCollection(nombreMiembro);
			MetaReference metaReferencia = metaColeccion.getMetaReference();
			if (metaReferencia.isAggregate()) {
				refrescarColeccionAgregados(					
					metaModelo,
					modelo,
					metaColeccion,
					coleccion);
			}
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException(
				"Imposible refrescar elementos de la colección "
					+ nombreMiembro
					+ " a "
					+ metaModelo.getName()
					+ " por:\n"
					+ ex.getLocalizedMessage());
		}
	}
	


	private void añadirColeccionAgregados(
		MetaModel metaModelo,
		Object modelo,
		MetaCollection metaColeccion,
		Collection coleccion)
		throws
			XavaException,
			CreateException,
			RemoteException,
			FinderException,
			ValidationException {
		Object entidadMoldeada = narrowEntidad(metaModelo, modelo);
		MetaAggregateEjb metaAgregadoEjb = (MetaAggregateEjb) metaColeccion.getMetaReference().getMetaModelReferenced();
		if (coleccion == null) coleccion = Collections.EMPTY_LIST;
		Iterator it = coleccion.iterator();
		int numero = 0;
		while (it.hasNext()) {
			Map valores = (Map) it.next();
			Object entidadAgregada =
				crearAgregadoEjb(
					metaModelo,
					entidadMoldeada,
					metaAgregadoEjb,
					valores,
					numero++);
			// Al crear el agregado ya enviamos el contenedor por lo que ya nace
			// agregado y no es necesario (ni posible) hacer un add explicito		
			//ejecutarAddXX(metaModelo, entidadMoldeada, metaColeccion, entidadAgregada);
		}
	}
	
	private void refrescarColeccionAgregados( 
		MetaModel metaModelo,
		Object modelo,
		MetaCollection metaColeccion,
		Collection coleccion)
		throws
			XavaException,
			CreateException,
			RemoteException,
			FinderException {
		if (coleccion == null) return;		
		Object entidadMoldeada = narrowEntidad(metaModelo, modelo);
		MetaAggregateEjb metaAgregadoEjb = (MetaAggregateEjb) metaColeccion.getMetaReference().getMetaModelReferenced();
		
		Iterator it = coleccion.iterator();
		int numero = 0;
		String nombreModeloAgregado = metaModelo.getName() + "." + metaAgregadoEjb.getName(); 
		while (it.hasNext()) {
			Map valores = (Map) it.next();						
			try {							
				Map valoresViejos = getValues(nombreModeloAgregado, valores, getNombresMiembros(metaAgregadoEjb)); 
				Map valoresNuevos = new HashMap(valores);
				valores.clear();
				valores.putAll(valoresViejos);
				valores.putAll(valoresNuevos);				
			}
			catch (FinderException ex) {
			}
		}
	}
	

	private Object crearAgregadoEjb(
		MetaModel metaModeloPrincipal,
		Object modeloPrincipal,
		MetaAggregateEjb metaAgregadoEjb,
		Map valores,
		int numero)
		throws CreateException, ValidationException, RemoteException, XavaException {
		return createEjb(
			metaAgregadoEjb,
			valores,
			metaModeloPrincipal,
			modeloPrincipal,
			numero);
	}


	private void añadirColeccionEntidades(
		MetaModel metaModelo,
		Object modelo,
		MetaCollection metaColeccion,
		Collection coleccion)
		throws XavaException, FinderException {
		Object entidadMoldeada = narrowEntidad(metaModelo, modelo);
		MetaEntity metaEntidadReferenciado =
			(MetaEntity) metaColeccion.getMetaReference().getMetaModelReferenced();
		Iterator it = coleccion.iterator();
		while (it.hasNext()) {
			Map valores = (Map) it.next();
			Object entidadAsociada =
				buscarEntidadAsociada(metaEntidadReferenciado, valores);
			ejecutarAddXX(metaModelo, entidadMoldeada, metaColeccion, entidadAsociada);
		}
	}


	/**
	 * ejbActivate method comment
	 * @exception java.rmi.RemoteException La descripción de excepción.
	 */
	public void ejbActivate() throws java.rmi.RemoteException {
	}
	/**
	 * ejbCreate method comment
	 * @exception javax.ejb.CreateException La descripción de excepción.
	 * @exception java.rmi.RemoteException La descripción de excepción.
	 */
	public void ejbCreate()
		throws javax.ejb.CreateException, java.rmi.RemoteException {
	}
	/**
	 * ejbPassivate method comment
	 * @exception java.rmi.RemoteException La descripción de excepción.
	 */
	public void ejbPassivate() throws java.rmi.RemoteException {
	}
	/**
	 * ejbRemove method comment
	 * @exception java.rmi.RemoteException La descripción de excepción.
	 */
	public void ejbRemove() throws java.rmi.RemoteException {
	}


	/**
	 * Fecha de creación: (31/07/2001 11:25:15)
	 */
	private Object getObjetoReferenciado(Object contenedor, String nombreMiembro) {
		try {
			PropertiesManager man =
				new PropertiesManager(contenedor);
			Object result = man.executeGet(nombreMiembro);
			return result;
		} catch (PropertiesManagerException ex) {
			ex.printStackTrace();
			throw new EJBException(
				"Imposible obtener el valor de la propiedad "
					+ nombreMiembro
					+ " por:\n"
					+ ex.getLocalizedMessage());
		} catch (InvocationTargetException ex) {
			Throwable th = ex.getTargetException();
			th.printStackTrace();
			throw new EJBException(
				"Imposible obtener el valor de la propiedad "
					+ nombreMiembro
					+ " por:\n"
					+ th.getLocalizedMessage());
		}
	}
	/**
	 * getSessionContext method comment
	 * @return javax.ejb.SessionContext
	 */
	public javax.ejb.SessionContext getSessionContext() {
		return mySessionCtx;
	}


	public Map getValues(
		String modelName,
		Map keyValues,
		Map membersNames)
		throws FinderException, XavaException {		
		try {			
			MetaModel metaModel = getMetaModel(modelName);						 
			Map result =
				getValues(
					metaModel,
					findEntity(modelName, keyValues),
					membersNames);						
			return result;
		} catch (XavaException ex) {
			setRollbackOnly();
			ex.printStackTrace();
			throw new XavaException(
				"Imposible obtener valores de "
					+ modelName
					+ " por:\n"
					+ ex.getLocalizedMessage());
		}
	}
	
	private Map getValores( 
		MetaModel metaModelo,
		Map valoresClave,
		Map nombreMiembros)
		throws FinderException, XavaException { 
		try {									 
			Map result =
				getValues(
					metaModelo,
					findEntity((IMetaEjb)metaModelo, valoresClave),
					nombreMiembros);			
			return result;
		} catch (XavaException ex) {
			setRollbackOnly();
			ex.printStackTrace();
			throw new XavaException(
				"Imposible obtener valores de "
					+ metaModelo.getName()
					+ " por:\n"
					+ ex.getLocalizedMessage());
		}
	}
	

	/** 
	 * @param nombreModelo Puede ser un agregado si lo cualificamos	  
	 */
	private MetaModel getMetaModel(String nombreModelo) throws XavaException { 
		int idx = nombreModelo.indexOf('.');			
		if (idx < 0) {
			return MetaComponent.get(nombreModelo).getMetaEntity();
		}
		else {
			String componente = nombreModelo.substring(0, idx);
			idx = nombreModelo.lastIndexOf('.'); // por si tenemos: MiEntidad.MiAgregado.MiOtroAgregado --> Coge MiOtroAgregado dentro del Componente MiEntidad
			String agregado = nombreModelo.substring(idx + 1);
			return MetaComponent.get(componente).getMetaAggregate(agregado);
		}
	}

	public Map getValues(
		String nombreModelo,
		Object entidad,
		Map nombreMiembros) throws XavaException
		 {		
		try {			
			MetaModel metaModelo = getMetaModel(nombreModelo);
			Map result = getValues(metaModelo, entidad, nombreMiembros);						
			return result;
		} catch (XavaException ex) {
			setRollbackOnly();
			ex.printStackTrace();
			throw new XavaException(
				"Imposible obtener valores de "
					+ nombreModelo
					+ " por:\n"
					+ ex.getLocalizedMessage());
		}
	}


	/**
	 * 
	 * @return java.util.Map
	 * @param entity javax.ejb.EJBObject Si es nulo se devolverá nulo
	 */
	private Map getValues(
		MetaModel metaModel, 
		Object entity,
		Map membersNames) throws XavaException {
		try {
			if (entity == null)
				return null;						
			if (membersNames == null) return Collections.EMPTY_MAP;					
			IPropertiesContainer r = narrowPropertiesContainer(metaModel, entity);			
			StringBuffer nombres = new StringBuffer();
			addKey(metaModel, membersNames); // always return the key althought it don't is aunque no se solicit
			removeViewProperties(metaModel, membersNames);			
			Iterator it = membersNames.keySet().iterator();			
			Map result = new HashMap();			
			while (it.hasNext()) {
				String nombreMiembro = (String) it.next();
				if (metaModel.containsMetaProperty(nombreMiembro) ||
					(metaModel.isKey(nombreMiembro) && !metaModel.containsMetaReference(nombreMiembro))) {
//					if (!metaEntidad.esCalculada(nombreMiembro)) {
						nombres.append(nombreMiembro);
						nombres.append(":");
//					} // de momento los calculados se excluyen
				} else {
					Map nombresSubmiembros = (Map) membersNames.get(nombreMiembro);
					if (metaModel.containsMetaReference(nombreMiembro)) {						
						result.put(
							nombreMiembro,
							getValoresReferencia(metaModel, entity, nombreMiembro, nombresSubmiembros));
					}else if (metaModel.containsMetaCollection(nombreMiembro)) {						
						result.put(
							nombreMiembro,
							getValoresColeccion(metaModel, entity, nombreMiembro, nombresSubmiembros));
					} else {
						throw new XavaException(
							"No se reconoce el miembro "
								+ nombreMiembro
								+ " en "
								+ metaModel.getName());
					}
				}
			}			
			result.putAll(r.executeGets(nombres.toString()));			
			return result;
		} catch (RemoteException ex) {
			ex.printStackTrace();
			throw new EJBException(
				"Problemas al obtener valores de una entidad:\n" + ex.getLocalizedMessage());
		}
	}
	
	private Map getNombresMiembros(MetaModel metaModelo) throws XavaException {
		return getNombresMiembros(metaModelo, false);
	}
	
	private Map getNombresMiembros(MetaModel metaModelo, boolean soloClave) throws XavaException {
		Map nombresMiembros = new HashMap();
		Collection propiedades = soloClave?metaModelo.getKeyPropertiesNames():metaModelo.getPropertiesNames(); 
		Iterator itPropiedades = propiedades.iterator();
		while (itPropiedades.hasNext()) {
			nombresMiembros.put(itPropiedades.next(), null);
		}
		
		Iterator itReferencias = metaModelo.getMetaReferences().iterator();
		while (itReferencias.hasNext()) {
			MetaReference ref = (MetaReference) itReferencias.next();
			if (soloClave && !ref.isKey()) break;
			nombresMiembros.put(ref.getName(), getNombresMiembros(ref.getMetaModelReferenced(), true));
		}
		
		return nombresMiembros;		
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
			Map nombresClaveReferencia = null;
			nombresClaveReferencia = (Map) memberNames.get(ref.getName());
			if (nombresClaveReferencia == null) {
				nombresClaveReferencia = new HashMap();
			}
			addKey(ref.getMetaModelReferenced(), nombresClaveReferencia);
			memberNames.put(ref.getName(), nombresClaveReferencia);
		}				
	}
	
	/**
	 * Si se envía nulo como <tt>nombresPropiedades</tt> se devuelve un mapa vacío. <p>
	 */
	private Map getValoresAgregado(MetaAggregate metaAgregado, Object agregado, Map nombresMiembros) throws XavaException {
		if (nombresMiembros == null) return Collections.EMPTY_MAP;
		PropertiesManager man = new PropertiesManager(agregado);
		StringBuffer nombres = new StringBuffer();
				
		Map result = new HashMap();
		
		Iterator itClaves = metaAgregado.getKeyPropertiesNames().iterator();
		while (itClaves.hasNext()) {
			nombres.append(itClaves.next());
			nombres.append(":");			
		}
		
		removeViewProperties(metaAgregado, nombresMiembros); 
		 
		Iterator it = nombresMiembros.keySet().iterator();		
		while (it.hasNext()) {
			String nombreMiembro = (String) it.next();
			if (metaAgregado.containsMetaProperty(nombreMiembro)) {
				nombres.append(nombreMiembro);
				nombres.append(":");
			} else
				if (metaAgregado.containsMetaReference(nombreMiembro)) {
					Map nombresSubmiembros = (Map) nombresMiembros.get(nombreMiembro);
					result.put(
						nombreMiembro,
						getValoresReferencia(metaAgregado, agregado, nombreMiembro, nombresSubmiembros));
				} else {
					throw new XavaException(
						"No se reconoce el miembro "
							+ nombreMiembro
							+ " en "
							+ metaAgregado.getName());
				}
		}
		try {
			result.putAll(man.executeGets(nombres.toString()));
		} catch (PropertiesManagerException ex) {
			ex.printStackTrace();
			throw new EJBException(
				"Imposible obtener el valor de las propiedades de "
					+ metaAgregado.getName()
					+ " por:\n"
					+ ex.getLocalizedMessage());
		} catch (InvocationTargetException ex) {
			Throwable th = ex.getTargetException();
			th.printStackTrace();
			throw new EJBException(
				"Imposible obtener el valor de la propiedades de "
					+ metaAgregado.getName()
					+ " por:\n"
					+ th.getLocalizedMessage());
		}
		return result;
	}


	/**
	 * Si <tt>nombreMiembros</tt> es nulo se devuelve un mapa vacío
	 */
	private Map getValoresEntidadAsociada(MetaEntity metaEntidad, Object entidad, Map nombresMiembros) throws XavaException {
		if (nombresMiembros == null) return new HashMap();
		Map result = getValues(metaEntidad, entidad, nombresMiembros);
		return result;
	}


	/**
	 * 
	 * @param entidad javax.ejb.EJBObject
	 */
	private Map getValoresReferencia(
		MetaModel metaModel,
		Object model,
		String memberName,
		Map submembersNames) throws XavaException {
		try {			
			MetaReference r = metaModel.getMetaReference(memberName);
			Object objeto = getObjetoReferenciado(model, memberName);
			if (r.isAggregate()) {
				return getValoresAgregado((MetaAggregate) r.getMetaModelReferenced(), objeto, submembersNames);
			} else {				
				return getValoresEntidadAsociada((MetaEntity) r.getMetaModelReferenced(), objeto, submembersNames);
			}
		} catch (XavaException ex) {
			ex.printStackTrace();
			throw new XavaException(
				"Imposible obtener valores de la referencia "
					+ memberName
					+ " de "
					+ metaModel.getName());
		}
	}
	
	/**
	 * 
	 * @param entidad javax.ejb.EJBObject
	 */
	private Collection getValoresColeccion(
		MetaModel metaModelo,
		Object modelo,
		String nombreMiembro,
		Map nombresMiembros) throws XavaException {
		try {
			MetaCollection c = metaModelo.getMetaCollection(nombreMiembro);
			Object objeto = getObjetoReferenciado(modelo, nombreMiembro);
			return getValoresColeccion(
					c.getMetaReference().getMetaModelReferenced(),
					c.isAggregate(),	objeto, nombresMiembros);
		} catch (XavaException ex) {
			ex.printStackTrace();
			throw new XavaException(
				"Imposible obtener valores de la coleccion "
					+ nombreMiembro
					+ " de "
					+ metaModelo.getName());
		}
	}
	
	private Collection getValoresColeccion(
		MetaModel modelo,
		boolean agregado,
		Object elementos, Map nombresMiembros) throws XavaException {
		Collection result = new ArrayList();
		Enumeration enum = null;
		if (elementos instanceof Enumeration) {
			enum = (Enumeration) elementos;
		}
		else if (elementos instanceof Collection) {
			enum = Collections.enumeration((Collection) elementos);
		}
		else {
			throw new XavaException("collection_type_not_supported");
		}		
		while (enum.hasMoreElements()) {
			Object objeto = enum.nextElement();			
			result.add(getValues(modelo, objeto, nombresMiembros));
		}
		return result;
	}


	private Object instanceAggregate(MetaAggregateBean metaAggregate, Map values)
		throws ValidationException, XavaException {
		try {
			Object object = Class.forName(metaAggregate.getBeanClass()).newInstance();
			PropertiesManager man = new PropertiesManager(object);			
			removeViewProperties(metaAggregate, values);
			values = convertSubmapsInObject(metaAggregate, values, false);
			man.executeSets(values);
			return object;
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("instantiate_error", metaAggregate.getBeanClass()));
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("instantiate_error", metaAggregate.getBeanClass()));
		} catch (InstantiationException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("instantiate_error", metaAggregate.getBeanClass()));
		} catch (InvocationTargetException ex) {
			lanzarValidacionException(
				ex, XavaResources.getString("assign_values_error", metaAggregate.getBeanClass(), ex.getLocalizedMessage())); 				
			throw new EJBException(); // Nunca ocurre
		} catch (PropertiesManagerException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("assign_values_error", metaAggregate.getBeanClass(), ex.getLocalizedMessage()));
		}
	}


	/**
	 * Lanza ValidacionException o EJBException
	 * Fecha de creación: (31/07/2001 13:15:09)
	 */
	private void lanzarValidacionException(
		InvocationTargetException ex,
		String ejbmensaje)
		throws ValidationException {
		Throwable th = ex.getTargetException();
		if (th instanceof ValidationException) {
			throw (ValidationException) th;
		}
		th.printStackTrace();
		throw new EJBException(ejbmensaje);
	}


	/**
	 * 
	 * @return java.util.Map
	 * @param entidad javax.ejb.EJBObject
	 */
	private Object mapaToObjetoReferenciado(
		MetaModel metaModelo,
		String nombreMiembro,
		Map valores)
		throws ValidationException, XavaException {
		MetaReference r = null;
		try {
			if (Maps.isEmpty(valores)) return null;			
			r = metaModelo.getMetaReference(nombreMiembro);
			if (r.isAggregate()) {
				return instanceAggregate((MetaAggregateBean) r.getMetaModelReferenced(), valores);
			} else {
				return buscarEntidadAsociada((MetaEntity) r.getMetaModelReferenced(), valores);
			}
		} catch (FinderException ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("map_to_reference_error",
					r.getName(),					
					metaModelo.getName(),					
					nombreMiembro));
		} catch (XavaException ex) {
			throw ex;
		}
	}

	private Object buscarEntidadAsociada(MetaEntity metaEntidad, Map valores)
		throws FinderException, XavaException {
		Map valoresClave = extraerValoresClave(metaEntidad, valores);
		return findEntity(metaEntidad.getName(), valoresClave);
	}


	private Map extraerValoresClave(MetaEntity metaEntidad, Map valores)
		throws XavaException {
		return metaEntidad.extractKeyValues(valores);
	}


	/**
	 * 
	 * @return pruebas.xvista.ab.modelo.Producto
	 * @param o java.lang.Object
	 */
	protected IPropertiesContainer narrowPropertiesContainer(MetaModel metaModelo, Object o) throws XavaException {
		return (EJBReplicable) narrowEntidad(metaModelo, o); 
	}

	private void removeKeyFields(MetaModel metaModelo, Map valores)
		throws XavaException {
		Iterator claves = metaModelo.getKeyPropertiesNames().iterator();
		while (claves.hasNext()) {
			valores.remove(claves.next());
		}
		Iterator itRef = metaModelo.getMetaReferencesKey().iterator();
		while (itRef.hasNext()) {
			MetaReference ref = (MetaReference) itRef.next();
			valores.remove(ref.getName());
		}		
	}


	/**
	 * 
	 * @param metaEntidad paniza.xava.xmodelo.modelo.Entidad
	 * @param valores java.util.Map
	 */
	private void removeReadOnlyFields(MetaModel metaModelo, Map valores)
		throws XavaException {
		Iterator aQuitar = metaModelo.getOnlyReadPropertiesNames().iterator();
		while (aQuitar.hasNext()) {
			valores.remove(aQuitar.next());
		}
	}
	
	/**
	 * 
	 * @param metaEntidad paniza.xava.xmodelo.modelo.Entidad
	 * @param valores java.util.Map
	 */
	private void quitarCamposCalculados(MetaModel metaModelo, Map valores)
		throws XavaException {
		Iterator aQuitar = metaModelo.getCalculatedPropertiesNames().iterator();
		while (aQuitar.hasNext()) {
			valores.remove(aQuitar.next());
		}
	}
	
	private void removeViewProperties(MetaModel metaModelo, Map valores)
		throws XavaException {
		Iterator aQuitar = metaModelo.getViewPropertiesNames().iterator();
		while (aQuitar.hasNext()) {
			valores.remove(aQuitar.next());
		}
	}	
		


	public void remove(String nombreModelo, Map valoresClave)
		throws RemoveException, ValidationException, XavaException 
	{		
		MetaModel  metaModelo = getMetaModel(nombreModelo);
		remove(metaModelo, valoresClave);			
	}
	
	public void remove(MetaModel metaModelo, Map valoresClave)
		throws RemoveException, ValidationException, XavaException {
		try {			
			Object entidad = findEntity((IMetaEjb)metaModelo, valoresClave);
			borrar(metaModelo, entidad);
		} catch (FinderException ex) {
			ex.printStackTrace();
			throw new RemoveException(XavaResources.getString("remove_error",
				metaModelo.getName(), ex.getLocalizedMessage()));
		}		
	}
		
	private void borrar(MetaModel  metaModelo, Object modelo)
		throws RemoveException, ValidationException, XavaException {
		try {
			Messages errores = validarParaBorrar(metaModelo, modelo);
			if (!errores.isEmpty()) {
				throw new ValidationException(errores);
			}
			if (!metaModelo.getMetaCollectionsAgregate().isEmpty()) {
				borrarTodasColeccionesAgregados(metaModelo, modelo);
			}
			removePersistentObject(metaModelo, modelo);			
		} catch (ValidationException ex) {
			throw ex;					
		} catch (XavaException ex) {
			setRollbackOnly();
			ex.printStackTrace();
			throw new XavaException("remove_error", metaModelo.getName(), ex.getLocalizedMessage());				
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("remove_error",
				metaModelo.getName(), ex.getLocalizedMessage()));
		}
	}
	
	protected void removePersistentObject(MetaModel metaModelo, Object modelo) throws RemoteException, RemoveException, XavaException {
		((EJBReplicable) narrowPropertiesContainer(metaModelo, modelo)).remove();		
	}
	
	/**
	 * @param metaEntidad
	 * @param entidad
	 */
	private Messages validarParaBorrar(MetaModel metaModelo, Object entidad) throws Exception {		
		Messages errores = new Messages();
		Iterator it = metaModelo.getMetaValidatorsRemove().iterator();
		while (it.hasNext()) {
			MetaValidator metaValidador = (MetaValidator) it.next(); 
			IRemoveValidator validador = metaValidador.getRemoveValidator();
			validador.setEntity(entidad);
			validador.validate(errores);			
		}				 		
		return errores;		
	}
		
	public void setSessionContext(javax.ejb.SessionContext ctx)
		throws java.rmi.RemoteException {
		mySessionCtx = ctx;
	}

	public void setValues(String modelName, Map keyValues, Map values)
		throws FinderException, ValidationException, XavaException 
	{				
		MetaModel metaModel = getMetaModel(modelName);
		setValues(metaModel, keyValues, values);
	}
	
	private void setValues(MetaModel metaModel, Map keyValues, Map values)
		throws FinderException, ValidationException, XavaException {		
		try {			
			removeKeyFields(metaModel, values);			
			removeReadOnlyFields(metaModel, values);
			removeViewProperties(metaModel, values);
			Map collections = extractCollections(metaModel, values);
			validar(metaModel, values, keyValues, null);			
			Object entity = findEntity((IMetaEjb) metaModel, keyValues);			
			IPropertiesContainer r = narrowPropertiesContainer(metaModel, entity);			
			r.executeSets(convertSubmapsInObject(metaModel, values, true));			
			if (collections != null) {
				modifyCollections(metaModel, entity, collections);
			}			
		} catch (ValidationException ex) {
			throw ex;
		} catch (Exception ex) {
			setRollbackOnly();
			ex.printStackTrace();
			throw new XavaException("assign_values_error", metaModel.getName(), ex.getLocalizedMessage()); 
		}
	}
	

	private void validar(
		Messages errores,
		MetaModel metaModelo,
		String nombreMiembro,
		Object valor) throws XavaException {			
		try {			
			if (metaModelo.containsMetaProperty(nombreMiembro)) {
				metaModelo.getMetaProperty(nombreMiembro).validate(errores, valor);
			} else
				if (metaModelo.containsMetaReference(nombreMiembro)) {
					MetaReference ref = metaModelo.getMetaReference(nombreMiembro); 
					MetaModel modeloReferenciado = ref.getMetaModelReferenced();
					Map valores = (Map) valor;					
					if (tieneValor(valores)) {
						if (ref.isAggregate()) validar(errores, modeloReferenciado, valores, valores, null);
					} else
						if (metaModelo
							.getMetaReference(nombreMiembro)
							.isRequired()) {
							errores.add("required", nombreMiembro, metaModelo.getName());
						}
						
				} else if (metaModelo.containsMetaCollection(nombreMiembro)) {
					// Por aquí nunca pasa porque las colecciones se tratan antes
					metaModelo.getMetaCollection(nombreMiembro).validate(errores, valor, null, null);
				} else if (metaModelo.containsMetaPropertyView(nombreMiembro)) { 										
					metaModelo.getMetaPropertyView(nombreMiembro).validate(errores, valor);									
				} else {					
					System.err.println(XavaResources.getString("not_validate_member_warning", nombreMiembro, metaModelo.getName()));
				}
		} catch (XavaException ex) {
			ex.printStackTrace();
			throw new XavaException("validate_error", nombreMiembro, metaModelo.getName());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("validate_error", nombreMiembro, metaModelo.getName()));				
		}
	}
	
	private boolean tieneValor(Map valores) {
		if (valores == null) return false;
		Iterator it = valores.values().iterator();
		while (it.hasNext()) {
			Object v = it.next();
			if (v == null) continue;
			if (v instanceof String && ((String) v).trim().equals("")) continue;
			if (v instanceof Number && ((Number)  v).intValue()==0) continue;
			return true;
		}		
		return false;
	}
	
	public Messages validate(String nombreModelo, Map valores) throws XavaException { 			
		MetaEntityEjb metaEntidad = (MetaEntityEjb) MetaComponent.get(nombreModelo).getMetaEntity();		
		Messages erroresValidacion = new Messages(); 				
		validar(erroresValidacion, metaEntidad, valores, null, null);
		return erroresValidacion;
	}
	
	/**
	 * 
	 * @param valores void
	 */
	private void validar(Messages errores, MetaModel metaModelo, Map valores, Map valoresClave, Object containerKey)	  
		throws XavaException {		
		Iterator it = valores.entrySet().iterator();		
		while (it.hasNext()) {
			Map.Entry en = (Map.Entry) it.next();
			String nombre = (String) en.getKey();
			Object valor = en.getValue();
			validar(errores, metaModelo, nombre, valor);
		}
		if (metaModelo.containsValidadors()) {
			validateWintModelValidator(errores, metaModelo, valores, valoresClave, containerKey);			
		}
	}
	
	private void validateWintModelValidator(Messages errors, MetaModel metaModel, Map values, Map keyValues, Object containerKey) throws XavaException {				
		try {
			String containerReferenceName = Strings.firstLower(metaModel.getMetaModelContainer().getName());
			Iterator itValidadores = metaModel.getMetaValidators().iterator();
			while (itValidadores.hasNext()) {
				MetaValidator metaValidador = (MetaValidator) itValidadores.next();
				Iterator itPoners =  metaValidador.getMetaSetsWithoutValue().iterator();
				IValidator v = metaValidador.createValidator();
				PropertiesManager mp = new PropertiesManager(v);		
				while (itPoners.hasNext()) {
					MetaSet poner = (MetaSet) itPoners.next();					
					Object valor = values.get(poner.getPropertyNameFrom());
					if (valor == null && !values.containsKey(poner.getPropertyNameFrom())) {
						if (keyValues != null) { 
							Map nombreMiembro = new HashMap();
							nombreMiembro.put(poner.getPropertyNameFrom(), null);
							Map valorMiembro = getValores(metaModel, keyValues, nombreMiembro);
							valor = valorMiembro.get(poner.getPropertyNameFrom());
						}											
					}
					if (poner.getPropertyNameFrom().equals(containerReferenceName)) {
						if (containerKey == null) {							
							Object object = findEntity(((IMetaEjb)metaModel), keyValues);
							valor = Objects.execute(object, "get" + metaModel.getMetaModelContainer().getName());
						}
						else {
							IMetaEjb containerReference = (IMetaEjb) metaModel.getMetaModelContainer();
							try {							
								valor = findEntity(containerReference, containerKey);
							}
							catch (ObjectNotFoundException ex) {
								valor = null;
							}			
						}
					}
					else if (metaModel.containsMetaReference(poner.getPropertyNameFrom())) {
						MetaReference ref = metaModel.getMetaReference(poner.getPropertyNameFrom());
						if (ref.isAggregate()) {
							valor = mapaToObjetoReferenciado(metaModel, poner.getPropertyNameFrom(), (Map) valor);
						}
						else {
							IMetaEjb entidadReferenciada = (IMetaEjb) ref.getMetaModelReferenced();
							try {
								valor = findEntity(entidadReferenciada, (Map) valor);
							}
							catch (ObjectNotFoundException ex) {
								valor = null;
							}																															
						}						
					}
					mp.executeSet(poner.getPropertyName(), valor);									
				}			
				v.validate(errors);
			}		
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("validate_model_error", metaModel.getName());
		}
	}
	
	private void validar(MetaModel metaModelo, Map valores, Map valoresClave, Object containerKey)
		throws ValidationException, XavaException, RemoteException {
		Messages errores = new Messages();		
		validar(errores, metaModelo, valores, valoresClave, containerKey);		
		if (errores.contains()) {
			throw new ValidationException(errores);
		}
	}

	private void validarExistenRequeridos(Messages errores, MetaModel metaModelo, Map valores)
		throws XavaException {		
		Iterator it = metaModelo.getRequiredMemberNames().iterator();		
		while (it.hasNext()) {
			String nombre = (String) it.next();			
			if (!valores.containsKey(nombre)) {				
				errores.add("required", nombre, metaModelo.getName());
			}
		}
	}


	public Object findEntity(String modelName, Map keyValues)
		throws FinderException {
		try {
			return findEntity((IMetaEjb) getMetaModel(modelName), keyValues);			
		} catch (FinderException ex) {
			throw ex;
		} catch (ElementNotFoundException ex) {
			throw new EJBException(XavaResources.getString("model_not_found", modelName));
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(
				"Imposible realizar la búsqueda de "
					+ modelName
					+ " por:\n"
					+ ex.getLocalizedMessage());
		}
	}

	protected Object findEntity(IMetaEjb metaEntidad, Map valoresClave)	throws FinderException, XavaException {
		/* tmp: Para versión hibernate
			getPersistenceProvider().setSession(getSession());
			return getPersistenceProvider().find(metaEntidad, valoresClave);
		*/
		Object key = metaEntidad.obtainPrimaryKeyFromKey(valoresClave);
		return findEntity(metaEntidad, key);  
	}
	
	protected Object findEntity(IMetaEjb metaEntidad, Object key)	throws FinderException { 		
		Class claseHome = null;
		Class clasePK = null;
		try {
			clasePK = metaEntidad.getPrimaryKeyClass();
			Class[] classArg = { clasePK };
			claseHome = metaEntidad.getHomeClass();
			Method m = claseHome.getMethod("findByPrimaryKey", classArg);								
			Object home = metaEntidad.obtainHome();
			Object[] arg = { key };
			return m.invoke(home, arg);
		} catch (NoSuchMethodException ex) {
			throw new EJBException(XavaResources.getString("findByPrimaryKey_expected", claseHome.getName(), clasePK.getName()));				
		} catch (InvocationTargetException ex) {
			Throwable th = ex.getTargetException();
			if (th instanceof FinderException) {
				throw (FinderException) th;
			} else {
				th.printStackTrace();
				throw new EJBException(XavaResources.getString("find_error", metaEntidad.getName()));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(XavaResources.getString("find_error", metaEntidad.getName()));			
		}
	} 
	

	/**
	 * @param entidadMoldeada Nunca nulo.
	 * @param nombreMiembro Nunca nulo.
	 */
	private Object ejecutarGetXX(
		MetaModel metaModelo,
		Object entidadMoldeada,
		String nombreMiembro)
		throws XavaException {
		String metodo = "get" + primeraAMayuscula(nombreMiembro);
		try {
			return Objects.execute(entidadMoldeada, metodo);
		} catch (NoSuchMethodException ex) {
			throw new XavaException("method_expected", metaModelo.getClassName(), metodo);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("method_execution_error",
					metaModelo.getClassName(),					
					metodo,					
					ex.getLocalizedMessage());
		}
	}


	/**
	 * @param entidadMoldeada Nunca nulo.
	 * @param nombreMiembro Nunca nulo.
	 * @param argumento Nunca nulo.
	 */
	private void ejecutarAddXX(
		MetaModel metaModelo,
		Object modeloMoldeado,
		MetaCollection metaColeccion,
		Object argumento)
		throws XavaException {
		IMetaEjb metaEjb = null;	
		try {	
			metaEjb = (IMetaEjb) metaColeccion.getMetaReference().getMetaModelReferenced();
		}
		catch (ClassCastException ex) {
			throw new XavaException("only_ejb_collections_error");
		}
		String nombreColeccion = primeraAMayuscula(metaColeccion.getName()); 
		try {
			ejecutar(metaModelo, modeloMoldeado, 
				"addTo" + nombreColeccion, 
				metaEjb.getRemoteClass(), argumento);
		}
		catch (NoSuchMethodException ex) {
			try {
				ejecutar(metaModelo, modeloMoldeado, 
					"add" + nombreColeccion, 
					metaEjb.getRemoteClass(), argumento);			
			}
			catch (NoSuchMethodException ex2) {
				throw new XavaException("add_method_expected", 
						metaModelo.getPropertiesClass(),
						nombreColeccion,
						argumento.getClass().getName());
			}
		}
	}


	/**
	 * @param entidadMoldeada Nunca nulo.
	 * @param nombreMiembro Nunca nulo.
	 * @param argumento Nunca nulo.
	 */
	private void ejecutarRemoveXX(
		MetaModel metaModelo,
		Object entidadMoldeada,
		MetaCollection metaColeccion,
		Object argumento)
		throws XavaException {
			
		IMetaEjb metaEjb = null;	
		try {	
			metaEjb = (IMetaEjb) metaColeccion.getMetaReference().getMetaModelReferenced();
		}
		catch (ClassCastException ex) {
			throw new XavaException("only_ejb_collections_error");
		}
		String nombreColeccion = primeraAMayuscula(metaColeccion.getName()); 
		try {			
			ejecutar(metaModelo, entidadMoldeada, 
				"removeFrom" + nombreColeccion, 
				metaEjb.getRemoteClass(), argumento);
		}
		catch (NoSuchMethodException ex) {
			try {
				ejecutar(metaModelo, entidadMoldeada, 
					"remove" + nombreColeccion, 
					metaEjb.getRemoteClass(), argumento);			
			}
			catch (NoSuchMethodException ex2) {
				throw new XavaException("remove_method_expected",				
					metaModelo.getPropertiesClass(),
					nombreColeccion, 					
					argumento.getClass().getName()); 				
			}
		}			
	}


	/**
	 * Ejecuta el método indicado (add, remove, etc) sobre el miembor indicado. <p>
	 * 
	 * @param entidadMoldeada Nunca nulo.
	 * @param nombreMiembro Nunca nulo.
	 * @param argumento Nunca nulo.
	 */
	private void ejecutar(
		MetaModel metaModelo,
		Object modeloMoldeado,
		String nombreMetodo,
		Class claseArgumento,
		Object argumento)
		throws NoSuchMethodException, XavaException  {		
		if (argumento == null) {
			throw new XavaException("not_null_argument_error",				
					modeloMoldeado.getClass().getName(),
					nombreMetodo);
		}
		try {
			Objects.execute(
				modeloMoldeado,
				nombreMetodo,
				claseArgumento,
				argumento);
		} catch (NoSuchMethodException ex) {
			throw new NoSuchMethodException(XavaResources.getString("method_expected",				
					metaModelo.getPropertiesClass(),					
					nombreMetodo+ "("	+ argumento.getClass().getName()+ ")"));
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("method_execution_error",				
					metaModelo.getPropertiesClass().getName(),					
					nombreMetodo,					
					ex.getLocalizedMessage());
		}
	}


	private String primeraAMayuscula(String s) {
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}


	/**
	 * Un <i>narrow</i> al tipo concreto que tiene el <i>EntityBean</i>. <p>
	 */
	private Object narrowEntidad(MetaModel metaModelo, Object modelo)
		throws XavaException {
		if (!(metaModelo instanceof IMetaEjb)) {
			throw new XavaException("only_ejb_error");
		}
		return PortableRemoteObject.narrow(modelo, ((IMetaEjb) metaModelo).getRemoteClass());
	}
	
	private void setRollbackOnly() {
		getSessionContext().setRollbackOnly();
	}

	public void removeCollectionElement(String nombreModelo, Map valoresClave, String nombreColeccion, Map valoresClaveElementoColeccion) 
		throws FinderException,	ValidationException, XavaException, RemoveException 
	{
		MetaModel metaModelo = getMetaModel(nombreModelo);
		MetaCollection metaColeccion = metaModelo.getMetaCollection(nombreColeccion);
		MetaModel metaModeloReferenciado = metaColeccion.getMetaReference().getMetaModelReferenced();
		if (metaColeccion.isAggregate()) {						
			remove(metaModeloReferenciado, valoresClaveElementoColeccion);
		}
		else {
			Map clavePapaNula = new HashMap();
			clavePapaNula.put(Strings.firstLower(nombreModelo), null);						
			setValues(metaModeloReferenciado, valoresClaveElementoColeccion, clavePapaNula);					
		}												
		if (metaColeccion.hasPostRemoveCalculators()) {
			ejecutarPosborrarElementoColeccion(metaModelo, valoresClave, metaColeccion);			
		}						
	} 
	
	private void ejecutarPosborrarElementoColeccion(MetaModel metaModelo, Map valoresClave, MetaCollection metaColeccion) throws FinderException, ValidationException, XavaException {
		Iterator itCalculadores = metaColeccion.getMetaCalculatorsPostRemove().iterator();
		while (itCalculadores.hasNext()) {
			MetaCalculator metaCalculador = (MetaCalculator) itCalculadores.next();			
			ICalculator calculador = metaCalculador.createCalculator();
			PropertiesManager mp = new PropertiesManager(calculador);
			Collection poners =  metaCalculador.getMetaSetsWithoutValue();
			if (metaCalculador.containsMetaSetsWithoutValue()) {
				Map nombrePropiedadesNecesitadas = new HashMap();
				Iterator itPoners = poners.iterator();
				while (itPoners.hasNext()) {
					MetaSet poner = (MetaSet) itPoners.next();
					nombrePropiedadesNecesitadas.put(poner.getPropertyNameFrom(), null);
				}												
				Map valores = getValores(metaModelo, valoresClave, nombrePropiedadesNecesitadas);
				
				itPoners = poners.iterator();											
				while (itPoners.hasNext()) {
					MetaSet poner = (MetaSet) itPoners.next();
					Object valor = valores.get(poner.getPropertyNameFrom());
					if (valor == null && !valores.containsKey(poner.getPropertyNameFrom())) {
						if (valoresClave != null) { 
							Map nombreMiembro = new HashMap();
							nombreMiembro.put(poner.getPropertyNameFrom(), null);
							Map valorMiembro = getValores(metaModelo, valoresClave, nombreMiembro);
							valor = valorMiembro.get(poner.getPropertyNameFrom());
						}											
					}
						
					if (metaModelo.containsMetaReference(poner.getPropertyNameFrom())) {
						MetaReference ref = metaModelo.getMetaReference(poner.getPropertyNameFrom());
						if (ref.isAggregate()) {
							valor = mapaToObjetoReferenciado(metaModelo, poner.getPropertyNameFrom(), (Map) valor);
						}
						else {
							IMetaEjb entidadReferenciada = (IMetaEjb) ref.getMetaModelReferenced();
							try {
								valor = findEntity(entidadReferenciada, (Map) valor);
							}
							catch (ObjectNotFoundException ex) {
								valor = null;
							}
																																
						}						
					}
					try {			
						mp.executeSet(poner.getPropertyName(), valor);
					}
					catch (Exception ex) {
						ex.printStackTrace();
						throw new XavaException("calculator_property_error", valor, poner.getPropertyName());
					}									
				}
			}			
			
			if (calculador instanceof IEntityCalculator) {
				Object entidad = findEntity((IMetaEjb) metaModelo, valoresClave);
				try {
					((IEntityCalculator) calculador).setEntity(entidad);
				}
				catch (Exception ex) {
					ex.printStackTrace();
					throw new XavaException("assign_entity_to_calculator_error", metaModelo.getName(), valoresClave);
				}									
				
			}
			try {
				calculador.calculate();
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException(XavaResources.getString("postremove_error", metaModelo.getName(), valoresClave));
			}
		}				
	}
	
	private IPersistenceProvider getPersistenceProvider() {
		if (persistenceProvider == null) {
			persistenceProvider = new HibernatePersistenceProvider();  //tmp De momento
		}
		return persistenceProvider;
	}
	
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
}