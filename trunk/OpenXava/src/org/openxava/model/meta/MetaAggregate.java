package org.openxava.model.meta;

import org.openxava.component.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;


/**
 * @author: Javier Paniza
 */
abstract public class MetaAggregate extends MetaModel {
	
	public MetaAggregate() {
		super();
	}
		
	public static MetaAggregate get(String name) throws XavaException {
		int idx = name.indexOf('.');
		if (idx < 0) {
			throw new ElementNotFoundException("aggregate_need_qualified", name);
		}
		String component = name.substring(0, idx);
		String aggregate = name.substring(idx + 1);
		return MetaComponent.get(component).getMetaAggregate(aggregate);
	}
	
	/**
	 * @see MetaElement#setName(String)
	 */	
	public void setName(String newNombre) {
		super.setName(newNombre);
	}
	
	public String getId() {
		return getMetaComponent().getName() + "." + getName();		
	}
		
	
}