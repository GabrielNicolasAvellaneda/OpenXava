package org.openxava.model.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Runtime exception to be thrown by a <code>IPersistenceProvider</code>
 * when a system level error is produced. <p>
 * 
 * This exception exists for your convenience, also you can throw 
 * PersistenceException, JDOException, HibernateException, EJBException or 
 * whatever RuntimeException your want.<br>
 * 
 * @author Javier Paniza
 */

public class PersistenceProviderException extends RuntimeException {

	private static Log log = LogFactory.getLog(PersistenceProviderException.class);
	
	public PersistenceProviderException() {
		super();
	}

	public PersistenceProviderException(String message) {
		super(message);
	}

	public PersistenceProviderException(String message, Throwable cause) {
		super(message, cause);
	}

	public PersistenceProviderException(Throwable cause) {
		super(cause);
	}

}
