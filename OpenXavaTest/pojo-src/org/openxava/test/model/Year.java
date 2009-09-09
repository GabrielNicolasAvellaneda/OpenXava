package org.openxava.test.model;

import org.hibernate.validator.*;
import org.openxava.annotations.*;

/**
 * Not persistent, only used for create a dialog for entry active year. <p>
 * 
 * Note that is not marked as @Entity <br>
 * 
 * @author Javier Paniza
 */

public class Year {
		
	@Max(9999) @Required
	private  int year;

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
