package org.openxava.generators;

import org.openxava.model.meta.*;

/**
 * @author Javier Paniza
 */
public interface IPropertyCodeGenerator {
	
	void setMetaProperty(MetaProperty metaPropiedad);
	
	String generate() throws Exception;

}
