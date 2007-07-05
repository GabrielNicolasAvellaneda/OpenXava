package org.openxava.annotations.parse;

import java.net.*;

import javax.xml.parsers.*;

import org.apache.commons.logging.*;
import org.openxava.jpa.*;
import org.openxava.util.*;
import org.w3c.dom.*;

/**
 * For reading persistence configuration (from META-INF/persistence.xml)
 * 
 * @author Javier Paniza
 */
class PersistenceConfig {
	
	private static Log log = LogFactory.getLog(PersistenceConfig.class);
	
	private static String defaultSchema;

	/**
	 * Read the property 'hibernate.default_schema' <p>
	 * 
	 * @return
	 */
	public static String getDefaultSchema() {
		if (defaultSchema == null) {
			try {
				DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				URL url = PersistenceConfig.class.getClassLoader().getResource("META-INF/persistence.xml");
				Document doc = builder.parse(url.toExternalForm());
				NodeList units = doc.getElementsByTagName("persistence-unit");
				int unitsCount = units.getLength();
				for (int i=0; i<unitsCount; i++) {
					Element unit = (Element) units.item(i);
					if (XPersistence.getPersistenceUnit().equals(unit.getAttribute("name"))) {																
						NodeList nodes = unit.getElementsByTagName("property");
						int length = nodes.getLength(); 
						for (int j=0; j<length; j++) {
							Element el = (Element) nodes.item(j);
							String name = el.getAttribute("name");
							if ("hibernate.default_schema".equals(name)) {
								defaultSchema = el.getAttribute("value") + ".";				
								return defaultSchema;
							}
						}
					}				
				}
				defaultSchema = "";
			}
			catch (Exception ex) {
				log.warn(XavaResources.getString("default_schema_warning"));
				return ""; 
			}
		}
		return defaultSchema;
	}

}
