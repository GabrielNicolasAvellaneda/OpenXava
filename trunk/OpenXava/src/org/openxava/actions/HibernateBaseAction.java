package org.openxava.actions;

import net.sf.hibernate.*;
import net.sf.hibernate.cfg.*;

/**
 * @author Mª Carmen Gimeno
 */

abstract public class HibernateBaseAction extends ViewBaseAction {

	private Session session;
	private static SessionFactory sessionFactory;
	
	
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
	
	private static SessionFactory getSessionFactory() throws HibernateException {
		if (sessionFactory == null) {
			sessionFactory = new Configuration().configure("/hibernate.cfg.xml").buildSessionFactory();
		}
		return sessionFactory; 
	}
	
	protected Session getSession() {
		return session;
	}
					
}
