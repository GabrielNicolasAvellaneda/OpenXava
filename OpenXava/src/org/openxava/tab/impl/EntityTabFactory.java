package org.openxava.tab.impl;

import java.rmi.*;

import javax.ejb.*;

import org.openxava.tab.meta.*;
import org.openxava.util.*;



public class EntityTabFactory {
	
		
	public static IEntityTab create(String nombreComponente) throws CreateException, RemoteException {
		EntityTab tab = new EntityTab();
		tab.setComponentName(nombreComponente);
		try {
			tab.init();
		}
		catch (InitException ex) {
			ex.printStackTrace();
			throw new CreateException(XavaResources.getString("tab_create_default_error", nombreComponente));
		}		
		return tab; 
	}
	
	public static IEntityTab create(String nombreComponente, String nombreTab) throws CreateException, RemoteException {
		EntityTab tab = new EntityTab();		
		tab.setComponentName(nombreComponente);
		tab.setTabName(nombreTab);
		try {
			tab.init();
		}
		catch (InitException ex) {
			ex.printStackTrace();
			throw new CreateException(XavaResources.getString("tab_create_error", nombreTab, nombreComponente));
		}		
		return tab; 		
	}
	
	/**
	 * A IEntityTab with on-demmand data reading.
	 */
	public static IEntityTab create(MetaTab metaTab) throws CreateException, RemoteException, XavaException {
		return create(metaTab, -1);
	}

	/**
	 * A IEntityTab that load all data at once.
	 */
	public static IEntityTab createAllData(MetaTab metaTab) throws CreateException, RemoteException, XavaException {
		return create(metaTab, Integer.MAX_VALUE);
	}	
	
	private static IEntityTab create(MetaTab metaTab, int chunkSize) throws CreateException, RemoteException, XavaException {
		EntityTab tab = new EntityTab();		
		tab.setComponentName(metaTab.getMetaModel().getMetaComponent().getName());
		tab.setMetaTab(metaTab);
		if (chunkSize > 0) tab.setChunkSize(chunkSize);		
		try {
			tab.init();
		}
		catch (InitException ex) {
			ex.printStackTrace();
			throw new CreateException(XavaResources.getString("tab_create_error", metaTab.getName(), tab.getComponentName()));
		}		
		return tab; 		
	}
	
	
	

}

