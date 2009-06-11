package org.openxava.invoicing.calculators;

import javax.persistence.*;
import org.openxava.calculators.*;
import org.openxava.jpa.*;

public class NextNumberForYearCalculator implements ICalculator {
	
	private int year;
	
	public Object calculate() throws Exception {
		Query query = XPersistence.getManager()
			.createQuery("select max(i.number) from Invoice i " +
					"where i.year = :year");
		query.setParameter("year", year);		
		return ((Integer) query.getSingleResult()) + 1;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
}
