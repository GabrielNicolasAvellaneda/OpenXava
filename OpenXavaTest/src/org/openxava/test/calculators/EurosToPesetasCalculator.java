package org.openxava.test.calculators;

import java.math.*;

import org.openxava.calculators.*;


/**
 * @author Javier Paniza
 */
public class EurosToPesetasCalculator implements ICalculator {
	
	private BigDecimal euros;

	public EurosToPesetasCalculator() {
		super();
	}

	public Object calculate() throws Exception {
		if (euros == null) return null;
		return euros.multiply(new BigDecimal("166.386")).setScale(0, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getEuros() {
		return euros;
	}

	public void setEuros(BigDecimal euros) {
		this.euros = euros;
	}

}
