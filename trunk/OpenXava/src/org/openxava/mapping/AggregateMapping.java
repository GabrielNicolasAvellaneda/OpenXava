package org.openxava.mapping;

import org.openxava.model.meta.*;
import org.openxava.util.*;


/**
 * @author Javier Paniza
 */
public class AggregateMapping extends ModelMapping {

	private String modelName;
		
	public void addReferenceMapping(ReferenceMapping mapeoReferencia) throws XavaException { 		
		super.addReferenceMapping(mapeoReferencia);

		if (!getMetaModel().containsMetaReference(mapeoReferencia.getReference())) {
			MetaReference r = new MetaReference();
			r.setReferencedModelName(Strings.firstUpper(mapeoReferencia.getReference()));		
			getMetaModel().addMetaReference(r);
			getMetaModel().setContainerModelName(r.getReferencedModelName()); 
		}

	}
	
	public String getModelName() {
		return modelName;
	}

	/**
	 * Sets the nombreAgregado.
	 * @param nombreAgregado The nombreAgregado to set
	 */
	public void setModelName(String nombreAgregado) {
		this.modelName = nombreAgregado;
	}


	public MetaModel getMetaModel() throws XavaException {		
		return getMetaComponent().getMetaAggregate(getModelName());
	}
		

}
