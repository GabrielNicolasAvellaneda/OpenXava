package org.openxava.web.layout;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.util.PropertiesReader;
import org.openxava.util.XavaResources;

/**
 * @author Javier Paniza
 */
public class LayoutPreferences {

	private final static String FILE_PROPERTIES = "xava-layout.properties";
	private static Log log = LogFactory.getLog(LayoutPreferences.class);

	private static LayoutPreferences instance;

	private Properties properties;


	private LayoutPreferences() {
	}

	public static LayoutPreferences getInstance() {
		if (instance == null) {
			instance = new LayoutPreferences();
		}
		return instance;
	}

	private Properties getProperties() {
		if (properties == null) {
			PropertiesReader reader = new PropertiesReader(
					LayoutPreferences.class, FILE_PROPERTIES);
			try {
				properties = reader.get();
			} catch (IOException ex) {
				log.debug(XavaResources.getString("properties_file_error",
						FILE_PROPERTIES)); // This is an expected level.
				properties = new Properties();
			}
		}
		return properties;
	}

	/**
	 * If false, topmost frames in views are not maximized.
	 * @return True or false. Default value is true.
	 */
	public boolean areViewFramesMaximized() {
		boolean returnValue = true;
		if ("false".equalsIgnoreCase(getProperties().getProperty("viewFramesMaximized"))) {
			returnValue = true;
		}
		return returnValue;
	}

	/**
	 * If false, topmost in sections are not maximized.
	 * @return True or false. Default value is true.
	 */
	public boolean areSectionFramesMaximized() {
		boolean returnValue = true;
		if ("false".equalsIgnoreCase(getProperties().getProperty("sectionFramesMaximized"))) {
			returnValue = true;
		}
		return returnValue;
	}
	
	/**
	 * 
	 * @return A label style for the renderer.
	 */
	public String getLabelStyle() {
		return getProperties().getProperty("labelStyle");
	}
	
	/**
	 * 
	 * @return A label style for the renderer.
	 */
	public String getDataStyle() {
		return getProperties().getProperty("dataStyle");
	}
}
