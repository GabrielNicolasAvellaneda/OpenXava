package org.openxava.invoicing.calculators;

import org.openxava.calculators.*;
import org.openxava.invoicing.util.*;

public class VatPercentageCalculator implements ICalculator {

	public Object calculate() throws Exception {		
		return InvoicingPreferences.getDefaultVatPercentage();
	}

}
