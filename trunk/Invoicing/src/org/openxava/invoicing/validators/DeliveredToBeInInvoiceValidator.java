package org.openxava.invoicing.validators;

import org.openxava.invoicing.model.*;
import org.openxava.util.*;
import org.openxava.validators.*;

public class DeliveredToBeInInvoiceValidator implements IValidator {
	
	private int year;
	private int number;
	private boolean delivered;
	private Invoice invoice;

	public void validate(Messages errors) throws Exception {
		if (invoice == null) return;
		if (!delivered) errors.add("order_must_be_delivered", year, number); 
	}
	
	// Getters and setters
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
		
	public boolean isDelivered() {
		return delivered;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}
		
}
