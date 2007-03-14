package org.openxava.test.tests;

import java.sql.*;

import org.openxava.hibernate.*;
import org.openxava.test.model.*;
import org.openxava.util.*;

import junit.framework.*;

/**
 * 
 * @author Javier Paniza
 */

public class HibernateTest extends TestCase {
	
	static {
		XHibernate.setConfigurationFile("hibernate-junit.cfg.xml");
		DataSourceConnectionProvider.setUseHibernateConnection(true);
	}
	
	public HibernateTest(String name) {
		super(name);
	}
	
	protected void tearDown() throws Exception {
		XHibernate.commit();		
	}
	
	public void testConvertersAllPropertiesOnCreate() throws Exception { // One way to avoid nulls	
		Subfamily sf = new Subfamily();
		sf.setNumber(77);
		sf.setDescription("PROVA JUNIT 77");
		XHibernate.getSession().save(sf);
		XHibernate.getSession().flush();
		
		org.hibernate.Query query = XHibernate.getSession().createQuery(
				"select sf.remarks from Subfamily sf where sf.number = 77");
		
		String remarks = (String) query.uniqueResult();
				
		assertEquals("", remarks);
		
		XHibernate.getSession().delete(sf);
	}
	
}
