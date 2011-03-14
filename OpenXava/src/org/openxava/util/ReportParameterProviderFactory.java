package org.openxava.util;

import org.apache.commons.logging.*;

/**
 * For obtaining a instance of report parameters.
 * 
 * Create on 11/03/2011 (12:59:00)
 * @author Ana Andres
 */
public class ReportParameterProviderFactory {
	private static Log log = LogFactory.getLog(ReportParameterProviderFactory.class);
	private static IReportParameterProvider instance;
	
	public static IReportParameterProvider getInstance() {
		if (instance == null) {
			try {
				instance = (IReportParameterProvider) Class.forName(XavaPreferences.getInstance().getReportParameterProviderClass()).newInstance();
			}
			catch (Exception ex) {
				log.error(ex.getMessage(), ex);
				throw new XavaException(XavaResources.getString("report_parameter_provider_error"));
			}
		}		
		return instance;
	}
}
