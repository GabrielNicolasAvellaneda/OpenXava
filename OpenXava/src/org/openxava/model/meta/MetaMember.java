package org.openxava.model.meta;


import org.openxava.util.*;
import org.openxava.util.meta.*;


/**
 * @author Javier Paniza
 */
abstract public class MetaMember extends MetaElement {

	private MetaModel metaModel;
	private String labelId;
	private String qualifiedName;

	/**
	 * Comentario de constructor Propiedad.
	 */
	public MetaMember() {
		super();
	}
	
	public MetaModel getMetaModel() {		
		return metaModel;
	}

	public void setMetaModel(MetaModel newContenedor) {
		metaModel = newContenedor;		
	}
	
	public String getQualifiedName() {
		if (qualifiedName == null) {
			if (getMetaModel() == null) qualifiedName = getName();
			else qualifiedName = getMetaModel().getName() + "." + getName();
		}
		return qualifiedName;
	}
	
	/**
	 * For can set a qualified name manually.
	 */
	public void setQualifiedName(String newQualifiedName) {
		qualifiedName = newQualifiedName;
	}
		
	/**
	 * Idem que {@link #isHidden()} pero en femenino. <p>
	 *
	 * La mayoría de los descendientes de miembro son femenino, así
	 * que en femenino suena mejor.
	 * 
	 * @return boolean
	 * @see #isHidden()
	 */
	public boolean isHidden() {
		return false;
	}
	
	public String getId() {
		if (!Is.emptyString(labelId)) {
			return labelId;		
		}
		MetaModel m = getMetaModel(); 
		return m==null?getName():m.getId() + "." + getName();
	}
	
	/**
	 * Id usado para buscar la etiqueta en el archivo de recursos. <p>
	 * 
	 * Esta propiedad no se llena desde los archivos xml, solo
	 * se usa programáticamente; de momento.	 
	 */ 	
	public String getLabelId() {
		return labelId;
	}
	public void setLabelId(String id) {
		this.labelId = id;		
	}
	
}