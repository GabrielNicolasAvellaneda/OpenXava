package org.openxava.calculators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * To use identity database id generation. <p>
 * 
 *  An example of use:
 *  <pre>
 *  <property name="id" key="true" type="int" hidden="true">
 *		<default-value-calculator class="org.openxava.calculators.IdentityCalculator" on-create="true"/>
 *	</property>
 *  </pre>
 *  
 * It does not work with EJB2. It works with Hibernate and EJB3 JPA.
 *  
 * @author Javier Paniza
 */

public class IdentityCalculator implements IHibernateIdGeneratorCalculator { 
	
	private Log log = LogFactory.getLog(IdentityCalculator.class);
	
	public String hbmGeneratorCode() {		
		return "<generator class='identity'/>";
	}

	public Object calculate() throws Exception { 
		return null;
	}

}
