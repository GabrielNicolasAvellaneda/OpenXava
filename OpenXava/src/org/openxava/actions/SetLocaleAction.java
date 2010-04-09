package org.openxava.actions;

import org.openxava.actions.BaseAction;
import org.openxava.util.Locales;

/**
 * 
 * @author Federico Alcantara 
 */

public class SetLocaleAction extends BaseAction {
	public void execute() throws Exception {
		String locale = getRequest().getParameter("locale");
		if (locale != null && !locale.equals("")) {
			getRequest().getSession().setAttribute("xava.portal.locale",
					new java.util.Locale(locale));
			Locales.setCurrent(getRequest());
		}
	}

}
