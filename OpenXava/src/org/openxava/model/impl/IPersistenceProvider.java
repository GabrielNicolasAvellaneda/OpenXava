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
 * tmp: Doc each method
 * tmp: IMetaEjb
 * tmp: Como excepci�n de sistema
 * se puede usar cualquier RuntimeException como por ejemplo JDOException,
 * HibernateException, EJBException etc.
 * 
 * @author M� Carmen Gimeno Alabau
 */
public interface IPersistenceProvider {
	
	void setSession(Session session); //tmp at the momment
	Object find(IMetaEjb metaModel, Map keyValues) throws FinderException, XavaException;
	Object find(IMetaEjb metaModel, Object key)	throws FinderException;
	IPropertiesContainer toPropertiesContainer(MetaModel metaModel, Object modelObject) throws XavaException;
	Object create(IMetaEjb metaEjb, Map values) throws CreateException, ValidationException, XavaException; // tmp : IMetaEjb
	void remove(MetaModel metaModel, Object modelObject) throws RemoveException, XavaException;
	void commit();
	void rollback();
}
