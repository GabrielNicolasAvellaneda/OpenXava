package org.openxava.model.impl;

import java.util.*;

import javax.ejb.*;

import net.sf.hibernate.*;


/**
 * @author Javier Paniza
 */
public class HibernateMapFacadeImpl extends MapFacadeBean {
	
	private Session session;
		
	protected Object findEntidad(IMetaEjb metaEntidad, Map valoresClave) throws FinderException {
		try {
			MetaEjbImpl ejbImpl = new MetaEjbImpl(metaEntidad);
			Class clase = metaEntidad.getClasePropiedades();
			String nombreClaseKey = clase.getName() + "$Key";
			Class claseKey = Class.forName(nombreClaseKey);
			Object key = ejbImpl.obtenerPrimaryKeyAPartirDeClave(valoresClave, claseKey, true);
			return getSession().getObjectById(key, true);			
		}	
		catch (JDOObjectNotFoundException ex) {
			throw new ObjectNotFoundException(ex.getLocalizedMessage());
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new EJBException(
				"Imposible realizar la búsqueda de "
					+ metaEntidad.getNombre()
					+ " por:\n"
					+ ex.getLocalizedMessage());
		}
	}
	
	protected IReplicable narrowReplicable(MetaModelo metaModelo, Object o) throws XavaException {
		return new POJOReplicable(o); 
	}
	
		
	protected Object crearObjetoPersistente(IMetaEjb metaEjb, Map valores)
		throws CreateException, ValidacionException, XavaException {
			Object objeto = null;
			try {				
				objeto = metaEjb.getClaseRemote().newInstance();
				ManejadorPropiedades mp = new ManejadorPropiedades(objeto);
				mp.ejecutarSets(valores);
				getSession().makePersistent(objeto);
				return objeto;
			}
			catch (JDODataStoreException ex) {
				// Comprobamos que ya existe
				try {					
					Object key = getSession().getObjectId(objeto);	
					getSession().getObjectById(key, true);
				}
				catch (JDOObjectNotFoundException ex2) {
					ex2.printStackTrace();
					throw new EJBException("Imposible crear un nuevo objeto por: " + ex2.getLocalizedMessage());
				}
				throw new DuplicateKeyException("Ya existe un objeto con esa clave");						
			}						
			catch (XavaException ex) {
				throw ex;
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new EJBException("Imposible crear un nuevo objeto por: " + ex.getLocalizedMessage());
			}
	}
	
	protected void borrarObjetoPersistente(MetaModelo metaModelo, Object modelo) throws RemoveException, XavaException {
		getSession().deletePersistent(modelo);
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	
	

}
