package org.openxava.test.actions;

import org.openxava.actions.*;

/**
 * 
 * @author Javier Paniza
 */

public class GoFamilyProductsReportAction extends ViewBaseAction implements IChangeControllersAction, IChangeModeAction {

	public void execute() throws Exception {
		// tmp showNewView();
		showDialog(); // En migraci�n y gu�a de referencia (di�logo)
		getView().setModelName("FilterBySubfamily");
		getView().setViewName("Family1");
	}
	
	public String[] getNextControllers() throws Exception {
		return new String[] { "FamilyProductsReport" };
	}

	public String getNextMode() {
		return DETAIL;
	}
	
}
