package org.openxava.test.actions;

import java.util.*;
import javax.ejb.*;

import org.openxava.actions.*;


/**
 * @author Javier Paniza
 */
public class ViewProductFromInvoiceDetailAction
	extends CollectionElementViewBaseAction implements INavigationAction{

	private Map invoiceValues;

	public void execute() throws Exception {				
		try {			
			setInvoiceValues(getView().getValues());
			Object number = getCollectionElementView().getValue("product.number");
			Map key = new HashMap();
			key.put("number", number);
			getView().setModelName("Product");			
			getView().setValues(key);
			getView().findObject();			
			getView().setKeyEditable(false);
			getView().setEditable(false);
		}
		catch (ObjectNotFoundException ex) {
			getView().clear();
			addError("object_not_found");
		}			
		catch (Exception ex) {
			ex.printStackTrace();
			addError("system_error");			
		}								
	}

	public String[] getNextControllers() {		
		return new String [] { "ProductFromInvoice" };
	}

	public String getCustomView() {
		return SAME_VIEW;
	}

	public Map getInvoiceValues() {
		return invoiceValues;
	}

	public void setInvoiceValues(Map map) {
		invoiceValues = map;
	}

}
