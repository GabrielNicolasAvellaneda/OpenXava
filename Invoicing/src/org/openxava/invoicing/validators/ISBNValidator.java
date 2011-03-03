package org.openxava.invoicing.validators;

import org.apache.commons.logging.*;
import org.hibernate.validator.*;
import org.openxava.util.*;
import org.openxava.invoicing.annotations.*;
import com.gargoylesoftware.htmlunit.*;  
import com.gargoylesoftware.htmlunit.html.*;  

public class ISBNValidator implements Validator<ISBN> {
	
	private static Log log = LogFactory.getLog(ISBNValidator.class);
	private static org.apache.commons.validator.ISBNValidator 
		validator = 
			new org.apache.commons.validator.ISBNValidator();

	private boolean search;  

	public void initialize(ISBN isbn) {  
		this.search = isbn.search();
	}

	public boolean isValid(Object value) {
		if (Is.empty(value)) return true;
		if (!validator.isValid(value.toString())) return false;
		return search?isbnExists(value):true;  
	}
	
	private boolean isbnExists(Object isbn) {
		try {
			WebClient client = new WebClient();
			HtmlPage page = (HtmlPage) client.getPage(  
				"http://www.bookfinder4u.com/" + 
				"IsbnSearch.aspx?isbn=" +  
				isbn + "&mode=direct"); 
			return page.asText()  
				.indexOf("ISBN: " + isbn) >= 0;
		}
		catch (Exception ex) {
			log.warn("Impossible to connect to bookfinder4u" +
			"to validate the ISBN. Validation fails", ex);
			return false;  
		}
	}	

}