package org.openxava.actions;

import java.util.*;

import org.openxava.model.meta.*;
import org.openxava.util.*;

import org.hibernate.*;
import org.hibernate.cfg.*;

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
	
	private static SessionFactory getSessionFactory() throws HibernateException, XavaException {
		if (sessionFactory == null) {
			Configuration configuration = new Configuration().configure("/hibernate.cfg.xml");
			for (Iterator it = MetaModel.getAllGenerated().iterator(); it.hasNext();) {
				MetaModel model = (MetaModel) it.next();
				try {
					configuration.addResource(model.getName() + ".hbm.xml");					
				}
				catch (Exception ex) {
					System.err.println("¡ADVERTENCIA! Mapeo hibernate para " + model.getName() + " no añadido"); // tmp: i18n
				}
			}
			sessionFactory = configuration.buildSessionFactory();
		}
		return sessionFactory; 
	}
	
	protected Session getSession() {
		return session;
	}
					
}
