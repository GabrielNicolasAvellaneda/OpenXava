package org.openxava.jpa;

import java.util.*;

import javax.persistence.*;

/**
 * Allows to work easily with EJB3 JPA inside OpenXava applications. <p>
 * 
 * You can use this class in any part of your OpenXava application:
 * calculators, validators, actions, junit tests, etc.<br>
 * To use JPA with this class you can write something as:
 * <pre>
 * Invoice invoice = new Invoice();
 * XPersistence.getManager().persist(invoice);
 * </pre>
 * 
 * And no more.<br>
 * The method getManager() creates a session and transaction the first time 
 * in the thread, and the OpenXava close the manager at the end of action execution.<br>
 * Also the next code is legal:
 * <pre>
 * Invoice invoice = new Invoice();
 * XPersistence.getManager().persist(invoice);
 * XPersistence.commit(); // In this way you commit and close manually.
 * ...
 * XPersistence.getManager().persist(customer); // As manager has been closed, a new one is created
 * </pre> 
 * 
 * @author Javier Paniza
 */

public class XPersistence {

	private static EntityManagerFactory entityManagerFactory; 
	private static String persistenceUnitName = "hibernate";	
	private static ThreadLocal currentManager = new ThreadLocal();		 

	/**
	 * <code>EntityManager</code> associated to current thread. <p>
	 * 
	 * If no manager exists or it's closed, then create a new one, and start a transaction.<br>
	 *
	 * @return Not null
	 */
	public static EntityManager getManager() {		
		EntityManager s = (EntityManager) currentManager.get();
		if (s == null || !s.isOpen()) {
			s = openManager();
		}		
		return s;
	}
	
	/**
	 * Create a new <code>EntityManager</code>. <p>
	 * 
	 * This manager is not associated with the current thread,
	 * and no transaction is started.
	 *
	 * @return Not null
	 */
	public static EntityManager createManager() {
		return getEntityManagerFactory().createEntityManager();
	}
				
	private static EntityManager openManager() {
		EntityManager m = getEntityManagerFactory().createEntityManager();
		m.getTransaction().begin();		
		currentManager.set(m);
		return m;
	}
	
	/**
	 * Commits changes and closes the <code>EntityManager</code> associated to 
	 * the current thread. <p>
	 * 
	 * If no manager or it is closed this method does nothing.<br>
	 * In most cases this method is called by OpenXava automatically, 
	 * hence the programmer that uses JPA APIs does not need to call it.
	 */
	public static void commit() {		
		EntityManager m = (EntityManager) currentManager.get();
		if (m == null) return;
		if (m.isOpen()) {			
			EntityTransaction t = (EntityTransaction) m.getTransaction();
			try {
				if (t != null) t.commit();
				else m.flush();
			}
			finally {
				currentManager.set(null);
				m.close();				
			}
		}
		else {					
			currentManager.set(null);
		}
	}
	
	/**
	 * Rollback changes and closes the <code>EntityManager</code> associated to 
	 * the current thread. <p>
	 * 
	 * If no manager or it is closed this method does nothing.<br>
	 */
	public static void rollback() {
		EntityManager m = (EntityManager) currentManager.get();
		if (m == null) return;
		if (m.isOpen()) {
			EntityTransaction t = (EntityTransaction) m.getTransaction();
			try {
				if (t != null) t.rollback(); // tmp: ¿in this way?
			}
			finally {
				currentManager.set(null);
				m.close();
			}
		}					
		else {
			currentManager.set(null);
		}
	}	
	
	private static EntityManagerFactory getEntityManagerFactory() {
		if (entityManagerFactory == null) {			
			entityManagerFactory = Persistence.createEntityManagerFactory(getPersistenceUnitName());
		}
		return entityManagerFactory; 
	}

	/**
	 * By default is <code>hibernate</code>. <p>
	 * 
	 * You must set value to this property before use any othe method of this class. 
	 */
	public static String getPersistenceUnitName() {
		return persistenceUnitName;
	}

	/**
	 * By default is <code>hibernate</code>. <p>
	 * 
	 * You must set value to this property before use any othe method of this class. 
	 */	
	public static void setPersistenceUnitName(String configurationFile) {
		XPersistence.persistenceUnitName = configurationFile;
	}
		
}
