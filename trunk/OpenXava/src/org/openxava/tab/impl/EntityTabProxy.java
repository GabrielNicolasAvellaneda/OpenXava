package org.openxava.tab.impl;

import java.rmi.*;

import javax.ejb.*;
import javax.rmi.*;

import org.openxava.ejbx.*;
import org.openxava.util.*;


/**
 * Javabean para acceder a un <tt>IEntityTabImpl</tt> implementado como <i>Statefull SessionBean</i>. <p>
 * 
 * Tolera fallos en el ejb. Si encuentra alguna excepción 
 * de sistema (<tt>RemoteException</tt>) lo que hace es reintentar.<br>
 *
 * Su uso es muy sencillo, p. ejemplo:
 * <pre>
 * protected IEntityTab crearEntityTab () {
 * 	EntityTabProxy entidadTab = new EntityTabProxy();
 * 	entidadTab.setJndi("ejb/miaplicacion/MiTab");
 * 	return entidadTab;
 * }
 * </pre> 
 * @author Javier Paniza
 */

public class EntityTabProxy implements IEntityTabImpl, ILiberate {
	
	private boolean byIndex;
	private String condition;
	private Object key;
	private int index;
	private IEntityTabImpl remote;
	private String jndi;
	private int chunkCount = 0;
	
	public EntityTabProxy() {		
	}

	

	public Object findEntity(Object[] clave) throws FinderException, RemoteException {
		try {
			return getRemote().findEntity(clave);		
		}
		catch (RemoteException ex) {
			remote = null;
			return getRemote().findEntity(clave);
		}		
	}

	public DataChunk nextChunk() throws RemoteException {
		try {
			DataChunk t = getRemote().nextChunk();
			chunkCount++;
			return t;		
		}
		catch (RemoteException ex) {
			remote = null;
			try {
				if (byIndex) {
					getRemote().search(this.index, this.key);
				}
				else {
					getRemote().search(this.condition, this.key);
				}
			}
			catch (RemoteException e) {				
				e.printStackTrace();
			}
			catch (FinderException e) {				
				e.printStackTrace();
				throw new RemoteException(e.getMessage());
			}
			for (int i=0; i<chunkCount; i++) {
				getRemote().nextChunk();
			}
			DataChunk t = getRemote().nextChunk();
			chunkCount++;
			return t; 
		}		
	}

	public IXTableModel getTable() throws RemoteException {
		try {
			IXTableModel tabla = getRemote().getTable();
			if (tabla instanceof TableModelBean) {
				((TableModelBean) tabla).setEntityTab(this);
			}
			return tabla;		
		}
		catch (RemoteException ex) {
			remote = null;
			return getRemote().getTable();
		}		
		
	}

	public void search(int indice, Object clave)
		throws FinderException, RemoteException {
			try {
				this.byIndex = true;
				chunkCount = 0;
				this.index = indice;
				this.key = clave;
				getRemote().search(indice, clave);						
			}
			catch (RemoteException ex) {
				remote = null;
				getRemote().search(indice, clave);
			}		
	}
	
	public void search(String condicion, Object clave) throws FinderException, RemoteException {		
		try {
			this.byIndex = false;
			chunkCount = 0;
			this.condition = condicion;
			this.key = clave;
			getRemote().search(condicion, clave);						
		}
		catch (RemoteException ex) {
			remote = null;
			getRemote().search(condicion, clave);
		}								
	}
	

	/**
	 * @return
	 */
	public String getJndi() {
		return jndi;
	}

	/**
	 * @param string
	 */
	public void setJndi(String string) {
		jndi = string;
	}
	
	/**
	 * No es necesario hacer caché, solo obtener el objeto remoto y devolverlo.	 
	 */
	protected IEntityTabImpl obtainRemote() throws Exception {
		Object ohome = BeansContext.get().lookup(this.jndi);
		return (IEntityTabImpl) EJBFactory.create((EJBHome)PortableRemoteObject.narrow(ohome, EJBHome.class));
	}
	
	private IEntityTabImpl getRemote() throws RemoteException {
		if (remote == null) {		
			if (this.jndi == null) {
				throw new IllegalStateException(XavaResources.getString("tab_proxy_jndi_required"));
			}
  		try {
				remote = obtainRemote();
  		}
  		catch (NoSuchMethodException ex) {
				throw new RemoteException(XavaResources.getString("home_create_required", jndi));
  		}
  		catch (Exception ex) {
				ex.printStackTrace();
				throw new RemoteException(XavaResources.getString("tab_remote_error"));
  		}
		}
		return remote;
	}

	public void liberate() throws RemoteException {
		try {
			((EJBObject) getRemote()).remove();			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("ADVERTENCIA: No fue posible liberar un gestor remoto de datos tabulares");	
		}
	}

	public int getResultSize() throws RemoteException {
		return getRemote().getResultSize();		
	}
	
	public void reset() throws RemoteException {
		getRemote().reset();
		chunkCount = 0;		
	}
	
	

}
