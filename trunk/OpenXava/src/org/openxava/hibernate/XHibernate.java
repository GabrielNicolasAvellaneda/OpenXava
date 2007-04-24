package org.openxava.hibernate;

import java.util.*;



import org.apache.commons.logging.*;
import org.hibernate.*;
import org.hibernate.cfg.*;
import org.hibernate.event.*;
import org.openxava.hibernate.impl.*;
import org.openxava.mapping.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;

/**
 * Allows to work easily with hibernate inside OpenXava applications. <p>
 * 
 * You can use this class in any part of your OpenXava application:
 * calculators, validators, actions, junit tests, etc.<br>
 * To use hibernate with this class you can write something as:
 * <pre>
 * Invoice invoice = new Invoice();
 * XHibernate.getSession().save(invoice);
 * </pre>
 * 
 * And no more.<br>
 * The method getSession() create a session and transaction the first time 
 * in the thread, and the OpenXava close the session at the end of action execution.<br>
 * Also the next code is legal:
 * <pre>
 * Invoice invoice = new Invoice();
 * XHibernate.getSession().save(invoice);
 * XHibernate.commit(); // In this way you commit and close manually.
 * ...
 * XHibernate.getSession().save(customer); // As session has been closed, a new one is created
 * </pre> 
 * 
 * @author Javier Paniza
 */

public class XHibernate {

	private static Log log = LogFactory.getLog(XHibernate.class);
	private static SessionFactory sessionFactory;	
	private static String configurationFile = "/hibernate.cfg.xml";	
	private static ThreadLocal currentSession = new ThreadLocal();	
	private static ThreadLocal currentTransaction = new ThreadLocal();
	private static ThreadLocal currentCmt = new ThreadLocal(); 
	

	/**
	 * Session associated to current thread. <p>
	 * 
	 * If no session exists or it's closed, then create a new one, and start a transaction.<br>
	 *
	 * @return Not null
	 */
	public static Session getSession() {		
		Session s = (Session) currentSession.get();
		if (s == null || !s.isOpen()) {
			s = openSession();
		}		
		return s;
	}
	
	/**
	 * Create a new session. <p>
	 * 
	 * This session is not associated with the current thread,
	 * and no transaction is started.
	 *
	 * @return Not null
	 */
	public static Session createSession() {
		return getSessionFactory().openSession();
	}
				
	private static Session openSession() {
		Session s = getSessionFactory().openSession();
		currentTransaction.set(isCmt()?null:s.beginTransaction());
		currentSession.set(s);
		return s;
	}
			
	/**
	 * Commits changes and closes the session associated to current thread. <p>
	 * 
	 * If no session or the it is closed this method does nothing.<br>
	 * In most cases this method is called by OpenXava automatically, 
	 * hence the programmer that uses hibernate APIs does not need to call it.
	 */
	public static void commit() {		
		Session s = (Session) currentSession.get();
		if (s == null) return;
		if (s.isOpen()) {			
			Transaction t = (Transaction) currentTransaction.get();
			try {
				if (t != null) t.commit();
				else s.flush();
			}
			finally {
				currentTransaction.set(null);
				currentSession.set(null);												
				s.close();
			}
		}
		else {			
			currentTransaction.set(null);
			currentSession.set(null);
		}
	}
	
	/**
	 * Rollback changes and closes the session associated to current thread. <p>
	 * 
	 * If no session or the it is closed this method does nothing.<br>
	 */
	public static void rollback() {
		Session s = (Session) currentSession.get();
		if (s == null) return;
		if (s.isOpen()) {
			Transaction t = (Transaction) currentTransaction.get();
			try {
				if (t != null) t.rollback();
			}
			finally {				
				currentTransaction.set(null);
				currentSession.set(null);
				s.close();
			}			
		}
		else {		
			currentTransaction.set(null);
			currentSession.set(null);
		}
	}	
	
	private static SessionFactory createSessionFactory(String hibernateCfg) throws HibernateException {
		try {
			Configuration configuration = new Configuration().configure(hibernateCfg);			
			
			for (Iterator it = MetaModel.getAllPojoGenerated().iterator(); it.hasNext();) {
				MetaModel model = (MetaModel) it.next();
				if (model.getMetaComponent().isTransient()) continue;
				try {
					configuration.addResource(model.getName() + ".hbm.xml");
				}
				catch (Exception ex) {
					log.error(XavaResources.getString("hibernate_mapping_not_loaded_warning", model.getName()), ex); 
				}
			}
						
			Collection preInsertListeners = new ArrayList();
			Collection preUpdateListeners = new ArrayList();
			Collection preDeleteListeners = new ArrayList();
			Collection postLoadListeners = new ArrayList();
			
			if (ReferenceMappingDetail.someMappingUsesConverters()) {				
				// toJava conversion is not enabled because in references it's useless thus we avoid an unnecessary overload
				ReferenceConverterToDBListener referenceConverterToDBListener = new ReferenceConverterToDBListener();
				preInsertListeners.add(referenceConverterToDBListener); 
				preUpdateListeners.add(referenceConverterToDBListener); 
			}
			
			if (MetaModel.someModelHasDefaultCalculatorOnCreate()) {
				preInsertListeners.add(new DefaultValueCalculatorsListener());
			}
			
			if (MetaModel.someModelHasPostCreateCalculator()) {
				preInsertListeners.add(CalculatorsListener.getInstance());
			}
			
			if (MetaModel.someModelHasPostModifyCalculator()) {				
				preUpdateListeners.add(CalculatorsListener.getInstance());
			} 
			
			if (MetaModel.someModelHasPreRemoveCalculator()) {				
				preDeleteListeners.add(CalculatorsListener.getInstance());
			}
			
			if (MetaModel.someModelHasPostLoadCalculator()) {				
				postLoadListeners.add(CalculatorsListener.getInstance());
			}
			
			
			if (!preInsertListeners.isEmpty()) {
				PreInsertEventListener [] preInsertListenersArray = new PreInsertEventListener[preInsertListeners.size()];
				preInsertListeners.toArray(preInsertListenersArray);
				configuration.getEventListeners().setPreInsertEventListeners(preInsertListenersArray);
			}

			if (!preUpdateListeners.isEmpty()) {
				PreUpdateEventListener [] preUpdateListenersArray = new PreUpdateEventListener[preUpdateListeners.size()];
				preUpdateListeners.toArray(preUpdateListenersArray);			
				configuration.getEventListeners().setPreUpdateEventListeners(preUpdateListenersArray);
			}
			
			if (!preDeleteListeners.isEmpty()) {
				PreDeleteEventListener [] preDeleteListenersArray = new PreDeleteEventListener[preDeleteListeners.size()];
				preDeleteListeners.toArray(preDeleteListenersArray);			
				configuration.getEventListeners().setPreDeleteEventListeners(preDeleteListenersArray);
			}
			
			if (!postLoadListeners.isEmpty()) {
				PostLoadEventListener [] postLoadListenersArray = new PostLoadEventListener[postLoadListeners.size()];
				postLoadListeners.toArray(postLoadListenersArray);			
				configuration.getEventListeners().setPostLoadEventListeners(postLoadListenersArray);
			}		
									
			return configuration.buildSessionFactory();
		} 
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new HibernateException(XavaResources.getString("hibernate_session_factory_creation_error"));
		}
	}

	private static SessionFactory getSessionFactory() throws HibernateException {
		if (sessionFactory == null) {
			sessionFactory = createSessionFactory(getConfigurationFile());
		}
		return sessionFactory; 
	}

	/**
	 * By default is <code>/hibernate.cfg.xml</code>. <p>
	 * 
	 * You must set value to this property before use any othe method of this class. 
	 */
	public static String getConfigurationFile() {
		return configurationFile;
	}

	/**
	 * By default is <code>/hibernate.cfg.xml</code>. <p>
	 * 
	 * You must set value to this property before use any othe method of this class. 
	 */	
	public static void setConfigurationFile(String configurationFile) {
		XHibernate.configurationFile = configurationFile;
	}
	
	/**
	 * Indicate that the current thread is executing inside a CMT context. <p>
	 * 
	 * CMT is Container Managed Transaction. The usual inside EJB.
	 */
	public static void setCmt(boolean cmt) {
		currentCmt.set(cmt?"":null);
	}
	/**
	 * Indicate that the current thread is executing inside a CMT context. <p>
	 * 
	 * CMT is Container Managed Transaction. The usual inside EJB.
	 */	
	public static boolean isCmt() { 
		return currentCmt.get() != null;
	}
	
}
