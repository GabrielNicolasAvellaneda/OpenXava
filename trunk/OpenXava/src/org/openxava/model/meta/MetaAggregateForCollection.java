package org.openxava.model.meta;




import org.openxava.component.*;
import org.openxava.mapping.*;
import org.openxava.util.*;

/**
 * 
 * @author Javier Paniza
 */

public class MetaAggregateForCollection extends MetaAggregate {
	
	private MetaEJB metaEJB = null;
	
	
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

