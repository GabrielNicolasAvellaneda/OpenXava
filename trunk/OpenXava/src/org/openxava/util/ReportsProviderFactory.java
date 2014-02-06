package org.openxava.util;

import org.apache.commons.logging.*;

/**
 * Create on 16/01/2014 (16:45:24)
 * @author Ana Andres
 */
public class ReportsProviderFactory {
	private static Log log = LogFactory.getLog(ReportsProviderFactory.class);
	private static IReportsProvider instance;
	
	public static IReportsProvider getInstance() {
		if (instance == null) {
			try {
				instance = (IReportsProvider) Class.forName(XavaPreferences.getInstance().getReportsProviderClass()).newInstance();
			}
			catch (Exception ex) {
				log.error(ex.getMessage(), ex);
				throw new XavaException(XavaResources.getString("report_provider_error"));	// tmp i18n
			}
		}		
		return instance;
	}
}
