package org.openxava.model.meta;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.component.*;
import org.openxava.mapping.*;
import org.openxava.util.*;

/**
 * 
 * @author Javier Paniza
 */

public class MetaAggregateForCollection extends MetaAggregate {
	
	private MetaEJB metaEJB = null;
	private static Log log = LogFactory.getLog(MetaAggregateForCollection.class);
	
	public ModelMapping getMapping() throws XavaException {
		return getMetaComponent().getAggregateMapping(getName());
	}
		
	public void setMetaComponent(MetaComponent metaComponent) {
		super.setMetaComponent(metaComponent);
	}
			
	public Class getBeanClass() throws XavaException {
		throw new UnsupportedOperationException ("Still not supported");
		
	}
	
	public MetaEJB getMetaEJB() {
		if (metaEJB == null) {
			if (super.getMetaEJB() != null) metaEJB =  super.getMetaEJB();
			else {
				metaEJB = new MetaEJB();
				metaEJB.setMetaModel(this);
			}
		}
		return metaEJB;
	}
	
}

