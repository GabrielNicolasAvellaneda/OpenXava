package org.openxava.test.actions;

import org.openxava.actions.*;

public class ChangeProductPriceAction extends BaseAction implements IChangeControllersAction {

	public void execute() throws Exception {
	}

	public String[] getNextControllers() throws Exception {
		return new String [] {
			"ChangeProductsPrice"
		};
	}

}
