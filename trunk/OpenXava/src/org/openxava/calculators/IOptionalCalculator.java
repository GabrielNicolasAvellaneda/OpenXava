package org.openxava.calculators;

/**
 * Un calculador que puede optar por no realizar
 * el calculo bajo ciertas circunstancias. <p>
 * 
 * @author Javier Paniza
 */
public interface IOptionalCalculator extends ICalculator {
	
	/**
	 * El resultado resuelto solo tendra sentido si se llama
	 * antes a <tt>calcular</tt>.
	 * 
	 */
	boolean isCalculate();

}
