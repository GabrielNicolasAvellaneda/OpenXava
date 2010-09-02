package org.openxava.invoicing.actions;

import org.openxava.actions.*;
import org.openxava.util.*;
import org.openxava.view.*;

public class NewOrderForCurrentUserAction extends NewAction {
		
	public void execute() throws Exception {		
		super.execute();
		String user = Users.getCurrent();
		if (user == null) {
			getView().setEditable(false);
			addError("no_user_logged");
			return;
		}
		int customerNumber = Integer.parseInt(user);
		View customerView = getView().getSubview("customer");
		customerView.setValue("number", customerNumber);
		customerView.findObject();
		customerView.setKeyEditable(false);		
	}

}
