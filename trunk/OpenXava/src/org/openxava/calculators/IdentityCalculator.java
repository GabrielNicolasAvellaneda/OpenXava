package org.openxava.calculators;




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
	
	
	
	public String hbmGeneratorCode() {		
		return "<generator class='identity'/>";
	}

	public Object calculate() throws Exception { 
		return null;
	}

}
