package org.openxava.model.impl;

import java.lang.reflect.*;
import java.rmi.*;
import java.util.*;

import puntocom.negocio.persistencia.*;
import puntocom.negocio.util.*;
import puntocom.xava.negocio.persistencia.*;
import puntocom.xava.negocio.util.*;

/**
 * @author Javier Paniza
 */
public class POJOReplicable implements IReplicable {
	
	private ManejadorPropiedades manejadorPropiedades;
	
	public POJOReplicable(Object objeto) {
		manejadorPropiedades = new ManejadorPropiedades(objeto);
	}

	public Map ejecutarGets(String propiedadesAReplicar) throws RemoteException {
		try {
			return manejadorPropiedades.ejecutarGets(propiedadesAReplicar);	
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException("Imposible obtener propiedades del objeto");
		}
	}

	public void ejecutarSets(Map propiedadesAActualizar) throws ValidacionException, RemoteException {
		try {
			manejadorPropiedades.ejecutarSets(propiedadesAActualizar);	
		}
		catch (InvocationTargetException ex) {
			if (ex.getTargetException() instanceof ValidacionException) {
				throw (ValidacionException) ex.getTargetException(); 
			}
			ex.printStackTrace();
			throw new RemoteException("Imposible actualizar propiedades del objeto");			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException("Imposible actualizar propiedades del objeto");
		}		
	}

}
