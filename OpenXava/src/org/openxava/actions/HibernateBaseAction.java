package org.openxava.actions;

import java.util.*;

import org.openxava.model.impl.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;

import org.hibernate.*;
import org.hibernate.cfg.*;

/**
 * @author Mª Carmen Gimeno
 */

abstract public class HibernateBaseAction extends ViewBaseAction {

	private Session session;
	// tmp private static SessionFactory sessionFactory;
	
	
	final public void execute() throws Exception {
		Transaction tx = null;
		try {
			session = getSessionFactory().openSession();
			tx = session.beginTransaction();				
			executeHibernate();
			tx.commit();
			session.close();
		}
								
		catch (Exception ex) {
			if (tx != null)	tx.rollback();
			if (session != null) session.close();
			throw ex;
		}						
	}
	
	protected abstract void executeHibernate() throws Exception;
	
	private static SessionFactory getSessionFactory() throws HibernateException, XavaException {
		return HibernatePersistenceProvider.getSessionFactory();
	}
	
	protected Session getSession() {
		return session;
	}
					
}
