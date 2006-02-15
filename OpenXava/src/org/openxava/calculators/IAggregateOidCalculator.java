package org.openxava.calculators;

/**
 * Calculator for the oid of an aggregete. <p>
 * 
 * The oid of an aggregate can be calculated an calculator of any type,
 * but if this type is used some additional info is provided.<br>
 * 
 * @author Javier Paniza
 */
public interface IAggregateOidCalculator extends ICalculator {
	
	/**
	 * In the case of EJB the key object of the container,
	 * in the case of POJO the container object itself. <p>
	 */
	void setContainerKey(Object containerKey);
	
	/**
	 * A number that can be used to create the oid. <p>
	 * 
	 * Usually this is a secuential number
	 */
	void setCounter(int counter);

}
