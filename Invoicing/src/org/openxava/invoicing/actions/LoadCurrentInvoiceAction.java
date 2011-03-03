package org.openxava.invoicing.actions;

import java.util.*;
import javax.inject.*;
import org.openxava.actions.*;

public class LoadCurrentInvoiceAction 
	extends SearchByViewKeyAction {  
	
	@Inject
	private Map currentInvoiceKey;   
									
	public void execute() throws Exception {
		getView().setValues(currentInvoiceKey);  	
		super.execute();  
	}

}