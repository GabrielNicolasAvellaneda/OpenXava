package org.openxava.invoicing.validators;

import org.hibernate.validator.*;
import org.openxava.invoicing.annotations.*;

import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

public class ISBNValidator implements Validator<ISBN> {
	
	private org.apache.commons.validator.ISBNValidator validator = new org.apache.commons.validator.ISBNValidator();

	public void initialize(ISBN isbn) {
	}

	public boolean isValid(Object value) {
		if (value == null) return true;
		// TMP if (!validator.isValid(value.toString())) return false;
		return isbnExists(value); 
	}

	private boolean isbnExists(Object isbn) {
		try {
			WebClient client = new WebClient();
			HtmlPage page = (HtmlPage) client.getPage(
					"http://www.bookfinder4u.com/IsbnSearch.aspx?isbn=" +
					isbn + "&mode=direct");
			System.out.println("[ISBNValidator.isbnExists] :\n" + page.asText()); // tmp
			return page.asText().indexOf("ISBN-13: " + isbn) >= 0;
		}
		catch (Exception ex) {
			// tmp Un mensaje en el log
			return false;
		}
	}

}

	
	


