package org.openxava.model.impl;

import java.lang.reflect.*;
import java.rmi.*;
import java.util.*;

import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * @author Javier Paniza
 */
public class POJOPropertiesContainerAdapter implements IPropertiesContainer {
	
	private PropertiesManager propertiesManager;
	
	public POJOPropertiesContainerAdapter(Object object) {
		propertiesManager = new PropertiesManager(object);
	}

	public Map executeGets(String properties) throws RemoteException {
		try {
			return propertiesManager.executeGets(properties);	
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException("Imposible obtener propiedades del objeto"); // tmp: i18n
		}
	}

	public void executeSets(Map properties) throws ValidationException, RemoteException {
		try {
			propertiesManager.executeSets(properties);	
		}
		catch (InvocationTargetException ex) {
			if (ex.getTargetException() instanceof ValidationException) {
				throw (ValidationException) ex.getTargetException(); 
			}
			ex.printStackTrace();
			throw new RemoteException("Imposible actualizar propiedades del objeto"); // tmp: i18n			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException("Imposible actualizar propiedades del objeto"); // tmp: i18n
		}		
	}

}
