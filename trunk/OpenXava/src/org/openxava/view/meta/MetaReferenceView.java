package org.openxava.view.meta;

/**
 * @author Javier Paniza
 */
public class MetaReferenceView implements java.io.Serializable {
	
	private String referenceName;
	private String viewName;
	private MetaSearchAction metaSearchAction;
	private MetaDescriptionsList metaDescriptionsList;
	private boolean frame = true;
	private boolean create;
	private boolean search;
	private boolean readOnly;
	
	
	/**
	 * Returns the nombreReferencia.
	 * @return String
	 */
	public String getReferenceName() {
		return referenceName==null?"":referenceName.trim();
	}

	/**
	 * Sets the nombreReferencia.
	 * @param nombreReferencia The nombreReferencia to set
	 */
	public void setReferenceName(String nombreReferencia) {
		this.referenceName = nombreReferencia;
	}

	/**
	 * Returns the metaAccionBuscar.
	 * @return MetaAccionBuscar
	 */
	public MetaSearchAction getMetaSearchAction() {
		return metaSearchAction;
	}

	/**
	 * Sets the metaAccionBuscar.
	 * @param metaAccionBuscar The metaAccionBuscar to set
	 */
	public void setMetaSearchAction(MetaSearchAction metaAccionBuscar) {
		this.metaSearchAction = metaAccionBuscar;
	}

	/**
	 * Returns the nombreVista.
	 * @return String
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * Sets the nombreVista.
	 * @param nombreVista The nombreVista to set
	 */
	public void setViewName(String nombreVista) {
		this.viewName = nombreVista;
	}

	/**
	 * Returns the sacarMarco.
	 * @return boolean
	 */
	public boolean isFrame() {
		return frame;
	}

	/**
	 * Sets the sacarMarco.
	 * @param sacarMarco The sacarMarco to set
	 */
	public void setFrame(boolean sacarMarco) {
		this.frame = sacarMarco;
	}
	
	public MetaDescriptionsList getMetaDescriptionsList() {
		return metaDescriptionsList;
	}

	public void setMetaDescriptionsList(MetaDescriptionsList descripciones) {
		metaDescriptionsList = descripciones;
	}

	public boolean isCreate() {
		return create;
	}
	public void setCreate(boolean create) {
		this.create = create;
	}
	public boolean isSearch() {
		return search;
	}
	public void setSearch(boolean search) {
		this.search = search;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
}
