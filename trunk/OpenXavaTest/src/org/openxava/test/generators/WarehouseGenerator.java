package org.openxava.test.generators;

import java.io.*;

import org.openxava.generators.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;


/**
 * @author Javier Paniza
 */
public class WarehouseGenerator implements IPropertyCodeGenerator {
		
	private static String template;

	public void setMetaProperty(MetaProperty metaPropiedad) {		
	}

	public String generate() throws Exception {
		return getTemplate();
	}
	
	private String getTemplate() throws IOException {
		if (template == null) {
			template = Resources.loadAsString(WarehouseGenerator.class, "org/openxava/test/generators/warehouse_property.template");  
		}
		return template;
	}

}
