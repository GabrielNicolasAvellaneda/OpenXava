package org.openxava.mapping;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.model.meta.*;
import org.openxava.util.*;


/** 
 * @author Javier Paniza
 */

public class EntityMapping extends ModelMapping {
	
	private Log log = LogFactory.getLog(EntityMapping.class);
	
	public String getModelName() throws XavaException {
		return getMetaModel().getName();
	}

	public MetaModel getMetaModel() throws XavaException {
		return getMetaComponent().getMetaEntity();
	}
	
	

}


