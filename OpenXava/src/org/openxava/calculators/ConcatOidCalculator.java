package org.openxava.calculators;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * It create a strint that concat string1:string2[:string3]:counter. <p>
 * 
 * Default separator is :  
 * 
 * @author Javier Paniza
 */

public class ConcatOidCalculator
	extends ConcatCalculator
	implements IAggregateOidCalculator {
		
	private int contador;
	private Log log = LogFactory.getLog(ConcatOidCalculator.class);
	
	public ConcatOidCalculator() {
		setSeparator(":");
	}

	public void setContainer(Object contenedorKey) {
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
