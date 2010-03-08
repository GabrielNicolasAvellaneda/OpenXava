package org.openxava.invoicing.actions;

import org.openxava.actions.*;

/**
 * 
 * @author Javier Paniza
 */

public class SaveInvoiceAction 
	extends SaveAction 
	implements IChangeModuleAction 
{

	public String getNextModule() {
		return PREVIOUS_MODULE;
	}

	public boolean hasReinitNextModule() {
		return false;
	}

}
