package org.openxava.invoicing.util;  

import java.io.*;
import java.math.*;
import java.util.*;
import org.apache.commons.logging.*;
import org.openxava.util.*;

public class InvoicingPreferences {
	
	private final static String 
		FILE_PROPERTIES="invoicing.properties";
	private static Log log = 
		LogFactory.getLog(InvoicingPreferences.class);
	private static Properties properties;	 

	private static Properties getProperties() {
		if (properties == null) {  
			PropertiesReader reader =	 
				new PropertiesReader(	
					InvoicingPreferences.class, 
					FILE_PROPERTIES);
			try {
				properties = reader.get();
			} 
			catch (IOException ex) {			
			log.error(
				XavaResources.getString(  
					"properties_file_error", 
					FILE_PROPERTIES),
				ex);
				properties = new Properties();
			}
		}		
		return properties;
	}
	
	
    public static BigDecimal getDefaultVatPercentage() {  
        return new BigDecimal(						
		getProperties().getProperty("defaultVatPercentage"));
    }

}