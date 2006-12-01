package org.openxava.model.meta;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.component.*;
import org.openxava.util.*;


/**
 * @author: Javier Paniza
 */
abstract public class MetaAggregate extends MetaModel {
	
	private Log log = LogFactory.getLog(MetaAggregate.class);
	
	static MetaAggregate getAggregate(String name) throws ElementNotFoundException, XavaException {
		int idx = name.indexOf('.');
		if (idx < 0) {
			throw new ElementNotFoundException("aggregate_need_qualified", name);
		}
		String component = name.substring(0, idx);
		String aggregate = name.substring(idx + 1);
		return MetaComponent.get(component).getMetaAggregate(aggregate);
	}
	
	
	public void setName(String newName) {
		super.setName(newName);
	}
	
	public String getId() {
		return getMetaComponent().getName() + "." + getName();		
	}
				
}