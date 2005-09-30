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
	
	public String getReferenceName() {
		return referenceName==null?"":referenceName.trim();
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public MetaSearchAction getMetaSearchAction() {
		return metaSearchAction;
	}

	public void setMetaSearchAction(MetaSearchAction metaSearchAction) {
		this.metaSearchAction = metaSearchAction;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public boolean isFrame() {
		return frame;
	}

	public void setFrame(boolean frame) {
		this.frame = frame;
	}
	
	public MetaDescriptionsList getMetaDescriptionsList() {
		return metaDescriptionsList;
	}

	public void setMetaDescriptionsList(MetaDescriptionsList descriptions) {
		metaDescriptionsList = descriptions;
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
