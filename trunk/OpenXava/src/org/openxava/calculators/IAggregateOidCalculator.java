package org.openxava.calculators;

/**
 * @author Javier Paniza
 */
public interface IAggregateOidCalculator extends ICalculator {
	
	
	void setContainerKey(Object containerKey);
	
	
	void setCounter(int counter);

}
