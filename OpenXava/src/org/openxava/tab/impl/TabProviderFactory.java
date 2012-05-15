package org.openxava.tab.impl;

import org.apache.commons.logging.*;
import org.openxava.util.*;

/**
 * 
 * 
 * @author Javier Paniza 
 */

public class TabProviderFactory {
	
	private static Log log = LogFactory.getLog(TabProviderFactory.class);
	
	public static ITabProvider create() {
		try {
			// tmp return (ITabProvider) Class.forName(XavaPreferences.getInstance().getTabProviderClass()).newInstance();
			// tmp return new JPATabProvider();
			return new JDBCTabProvider();
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new XavaException("tab_provider_creation_error"); // tmp i18n
		}		
	}


}
