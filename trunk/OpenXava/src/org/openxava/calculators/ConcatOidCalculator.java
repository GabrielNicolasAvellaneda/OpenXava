package org.openxava.calculators;


/**
 * Crea una cadena en la que concatena cadena1:cadena2[:cadena3]:contador. <p>
 * 
 * El separador por defecto son los : (dos puntos) 
 * 
 * @author Javier Paniza
 */

public class ConcatOidCalculator
	extends ConcatCalculator
	implements IAggregateOidCalculator {
		
	private int contador;
	
	public ConcatOidCalculator() {
		setSeparator(":");
	}

	public void setContainerKey(Object contenedorKey) {
	}

	public void setCounter(int contador) {
		this.contador = contador;
	}
	
	public Object calculate() throws Exception {
		StringBuffer sb = new StringBuffer((String)super.calculate());
		sb.append(getSeparator());
		sb.append(contador);
		return sb.toString();		
	}


}
