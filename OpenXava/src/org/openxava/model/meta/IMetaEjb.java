package org.openxava.model.meta;

import java.util.*;

import javax.ejb.*;

import org.openxava.util.*;


/**
 * 
 * @author Javier Paniza
 */

public interface IMetaEjb extends IMetaModel {
	
	Class getRemoteClass() throws XavaException;	
	java.lang.String getHome() throws XavaException;
	java.lang.String getJndi() throws XavaException;
	java.lang.String getPrimaryKey() throws XavaException;
	void setPrimaryKey(java.lang.String newPrimaryKey);
	java.lang.String getRemote() throws XavaException;
	void setRemote(java.lang.String newRemote);
	void setHome(java.lang.String newHome);
	void setJndi(java.lang.String newJndi);	
	Class getHomeClass() throws XavaException;	
	Class getPrimaryKeyClass() throws XavaException;
	boolean isPrimaryKeyClassAvailable();
	
	/**
	 * For use inside EJB. <p>
	 * Para usarse desde dentro de un EJB. <p>
	 * 
	 * One would not use from client because throws
	 * <tt>EJBExceptin</tt> on fail. 
	 *
	 * @exception EJBException If any problem
	 * @return Molded (with corba cast) home
	 */
	EJBHome obtainHome() throws XavaException;
	
	Object obtainPrimaryKeyFromAllValues(Map values) throws XavaException;
	Object obtainPrimaryKeyFromKey(Map keyValues) throws XavaException;	
	Object obtainPrimaryKeyFromKeyWithoutConversors(Map keyValues) throws XavaException;	
	Map obtainMapFromPrimaryKey(Object primaryKey) throws XavaException;	
	Class getBeanClass() throws XavaException;
		
}

