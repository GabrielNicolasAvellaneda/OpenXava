package org.openxava.invoicing.actions;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import org.openxava.actions.*;
import org.openxava.invoicing.model.*;
import org.openxava.model.*;
import org.openxava.util.*;
import org.openxava.validators.*;

public class AddOrdersToInvoiceAction extends AddElementsToCollectionAction {
		
	public void execute() throws Exception { 	
		super.execute();
		getView().refresh(); 
	}

	protected void associateEntity(Map keyValues) throws ValidationException,
			XavaException, ObjectNotFoundException, FinderException,
			RemoteException 
	{
		super.associateEntity(keyValues);
		Order order = (Order) MapFacade.findEntity("Order", keyValues);
		order.copyDetailsToInvoice();
	}

}
