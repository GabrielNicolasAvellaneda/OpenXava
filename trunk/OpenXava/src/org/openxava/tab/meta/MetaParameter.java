package org.openxava.tab.meta;

import org.openxava.filters.meta.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;


/**
 * 
 * @author Javier Paniza
 */
public class MetaParameter extends MetaElement {
	
	private boolean hasLabel = false;
	private String propertyName;
	private String labelId;
	private MetaConsult metaConsult;
	private boolean range=false;
	private boolean like=false;
	private MetaFilter metaFilter;
	
		
	public MetaProperty getMetaProperty() throws XavaException {
		if (metaConsult == null) {
			throw new XavaException("parameter_consult_required");
		}
		return metaConsult.getMetaModel().getMetaProperty(getPropertyName());
	}
		
	
	/**
	 * Gets the propiedad
	 * @return Returns a String
	 */
	public String getPropertyName() {
		return propertyName;
	}
	/**
	 * Sets the propiedad
	 * @param propiedad The propiedad to set
	 */
	public void setPropertyName(String nombrePropiedad) {
		this.propertyName = nombrePropiedad;
	}


	/**
	 * Gets the consulta
	 * @return Returns a Consulta
	 */
	public MetaConsult getMetaConsult() {
		return metaConsult;
	}
	
	/**
	 * Sets the consulta
	 * @param consulta The consulta to set
	 */
	public void setMetaConsult(MetaConsult consulta) {
		this.metaConsult = consulta;
	}

	/**
	 * Returns the rango.
	 * @return boolean
	 */
	public boolean isRange() {
		return range;
	}

	/**
	 * Sets the rango.
	 * @param rango The rango to set
	 */
	public void setRange(boolean rango) {
		this.range = rango;
	}

	/**
	 * Returns the like.
	 * @return boolean
	 */
	public boolean isLike() {
		return like;
	}

	/**
	 * Sets the like.
	 * @param like The like to set
	 */
	public void setLike(boolean like) {
		this.like = like;
	}

	/**
	 * Returns the metaFiltro.
	 * @return MetaFiltro
	 */
	public MetaFilter getMetaFilter() {
		return metaFilter;
	}

	/**
	 * Sets the metaFiltro.
	 * @param metaFiltro The metaFiltro to set
	 */
	public void setMetaFilter(MetaFilter metaFiltro) {
		this.metaFilter = metaFiltro;
	}


	public void setLabel(String newEtiqueta) {		
		super.setLabel(newEtiqueta);
		hasLabel = !Is.emptyString(newEtiqueta);
	}


	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String id) {
		labelId = id;
		hasLabel = !Is.emptyString(id);
	}

	public String getId() {
		return labelId;		
	}
	
	/**
	 * Si tiene etiqueta propia, y no usa la de la propiedad. 
	 */
	public boolean hasLabel() {
		return hasLabel;
	}
	
	

}


