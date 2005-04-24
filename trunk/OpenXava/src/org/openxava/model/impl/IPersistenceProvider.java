package org.openxava.model.impl;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import org.hibernate.*;

import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * Provides the implementation of the persistence services
 * used in {@link MapFacadeBean}. <p>
 * 
 * @author Mª Carmen Gimeno Alabau
 */
public interface IPersistenceProvider {
	
	void setSession(Session session); //tmp De momento
	Object find(IMetaEjb metaModel, Map keyValues) throws FinderException, XavaException;
	Object find(IMetaEjb metaEntidad, Object key)	throws FinderException;
	IPropertiesContainer toPropertiesContainer(MetaModel metaModelo, Object o) throws XavaException;
	Object create(IMetaEjb metaEjb, Map valores) throws CreateException, ValidationException, XavaException;
	void remove(MetaModel metaModel, Object model) throws RemoteException, RemoveException, XavaException;
}
