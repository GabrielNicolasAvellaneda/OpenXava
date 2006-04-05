package org.openxava.test.formatters;

import java.text.*;
import java.util.*;

import javax.servlet.http.*;

import org.openxava.formatters.*;
import org.openxava.test.model.*;
import org.openxava.util.*;


/**
 * @author Javier Paniza
 */

public class DeliveryFormatter implements IFormatter {

	private final static String BAD_STRING =
		"String for create Delivery must have format: '[.[.invoiceNumber.invoiceYear.].number.[.deliveryType.].]'"; 
		

	public String format(HttpServletRequest request, Object object) {		
		return object==null?"":object.toString();
	}
	
	public Object parse(HttpServletRequest request, String string) throws ParseException {		
		if (Is.emptyString(string) || "0".equals(string)) {
			return null; 
		}				
		StringTokenizer st = new StringTokenizer(string, "[.");
		
		if (!st.hasMoreTokens()) {
			throw new ParseException(BAD_STRING, 0);			
		}
		String sinvoiceNumber = st.nextToken().trim();
		
		if (!st.hasMoreTokens()) {
			throw new ParseException(BAD_STRING, 0);			
		}
		String sinvoiceYear = st.nextToken().trim();
		
		if (!st.hasMoreTokens()) {
			throw new ParseException(BAD_STRING, 0);			
		}
		String snumber = st.nextToken().trim();
		
		if (!st.hasMoreTokens()) {
			throw new ParseException(BAD_STRING, 0);			
		}
		String sdeliveryType = st.nextToken().trim();
				
		Delivery delivery = new Delivery();
		try {
			Invoice invoice = new Invoice();
			invoice.setYear("null".equals(sinvoiceYear)?0:Integer.parseInt(sinvoiceYear));
			invoice.setNumber("null".equals(sinvoiceNumber)?0:Integer.parseInt(sinvoiceNumber));
			DeliveryType type = new DeliveryType();
			type.setNumber("null".equals(sdeliveryType)?0:Integer.parseInt(sdeliveryType));
			delivery.setInvoice(invoice);
			delivery.setType(type);
			delivery.setNumber("null".equals(snumber)?0:Integer.parseInt(snumber));
		}
		catch (NumberFormatException ex) {
			throw new ParseException("Impossible to parse Delivery: invoiceNumber,invoiceYear,number and deliveryType must be numerics", 0);			
		}		
		
		return delivery;
		
	}

}
