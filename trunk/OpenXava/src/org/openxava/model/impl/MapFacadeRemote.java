package org.openxava.model.impl;


import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import org.openxava.util.*;
import org.openxava.validators.*;


/**
 * This is an Enterprise Java Bean Remote Interface
 */
public interface MapFacadeRemote extends javax.ejb.EJBObject {


	Object create(String nombreEntidad, Map valores)
		throws 
			CreateException, ValidationException, 
			XavaException, RemoteException;
			
		
	Map getValues(String nombreEntidad, Map valoresClave, Map nombreMiembros)
		throws FinderException, XavaException, RemoteException;
		
	Map getValues(String nombreEntidad, Object entidad, Map nombreMiembros)
		throws XavaException, RemoteException;
		
	void setValues(String nombreEntidad, Map valoresClave, Map valores)
		throws FinderException, ValidationException, XavaException, RemoteException;
		
		
	void remove(String nombreEntidad, Map valoresClave)
		throws RemoveException, XavaException, ValidationException, RemoteException;
		
	Object findEntity(String nombreEntidad, Map valoresClave)
		throws FinderException, java.rmi.RemoteException;
		
	Map createReturningValues(String nombreEntidad, Map valores)
				throws CreateException, XavaException, ValidationException, RemoteException;
				
	Messages validate(String nombreModelo, Map valores) throws XavaException, RemoteException;
	
	Object createAggregate(String nombreModelo, Map claveContenedor, int contador, Map valores)
		throws CreateException,ValidationException, XavaException, RemoteException; 
 
	Object createAggregate(String nombreModelo, Object contenedor, int contador, Map valores) 
		throws CreateException,ValidationException, XavaException, RemoteException;
		
	Map createAggregateReturningKey(String nombreModelo, Map valoresClaveContenedor, int contador, Map valores) 
		throws CreateException,ValidationException, XavaException, RemoteException; 
		 
	Map createReturningKey(String nombreEntidad, Map valores)		 
		throws CreateException, XavaException, ValidationException, RemoteException;
		
	void removeCollectionElement(String nombreModelo, Map valoresClave, String nombreColeccion, Map valoresClaveElementoColeccion) 
		throws RemoveException, FinderException,	ValidationException, XavaException,  RemoteException; 
		 							
}