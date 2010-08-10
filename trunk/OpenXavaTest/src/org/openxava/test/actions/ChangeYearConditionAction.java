package org.openxava.test.actions;

import org.openxava.actions.*;

public class ChangeYearConditionAction extends TabBaseAction {

	private int year;

	public void execute() throws Exception {
		getTab().setConditionValue("year", year); 
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getYear() {
		return year;
	}

}
