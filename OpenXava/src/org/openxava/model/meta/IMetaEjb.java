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
	
	/**
	 * 
	 * @return java.lang.String
	 */
	java.lang.String getHome() throws XavaException;
	
	/**
	 * 
	 * @return java.lang.String
	 */
	java.lang.String getJndi() throws XavaException;
	
	/**
	 * 
	 * @return java.lang.String
	 */
	java.lang.String getPrimaryKey() throws XavaException;
	
	/**
	 * 
	 * @param newPrimaryKey java.lang.String
	 */
	void setPrimaryKey(java.lang.String newPrimaryKey);
	
	
	/**
	 * 
	 * @return java.lang.String
	 */
	java.lang.String getRemote() throws XavaException;
	
	/**
	 * 
	 * @param newRemote java.lang.String
	 */
	void setRemote(java.lang.String newRemote);
	
	
	/**
	 * 
	 * @param newHome java.lang.String
	 */
	void setHome(java.lang.String newHome);
	
	/**
	 * 
	 * @param newJndi java.lang.String
	 */
	void setJndi(java.lang.String newJndi);
	
	
	Class getHomeClass() throws XavaException;
	
	Class getPrimaryKeyClass() throws XavaException;
	
	/**
	 * Para usarse desde dentro de un EJB. <p>
	 *
	 * No se debería usar desde el cliente ya que lanza
	 * una <tt>EJBException</tt> si falla.<br>
	 *
	 * @exception EJBException Si hay algún problema
	 * @return Home moldado
	 */
	EJBHome obtainHome() throws XavaException;
	
	Object obtainPrimaryKeyFromAllValues(Map valores) throws XavaException;
		

	Object obtainPrimaryKeyFromKey(Map valoresClave) throws XavaException;
	
	Object obtainPrimaryKeyFromKeyWithoutConversors(Map valoresClave) throws XavaException;
		
}

