package org.openxava.view.meta;

/**
 * @author Javier Paniza
 */
public class MetaReferenceView extends MetaMemberView implements java.io.Serializable {
	
	private String referenceName;
	private String viewName;
	private MetaSearchAction metaSearchAction;
	private MetaDescriptionsList metaDescriptionsList;
	private boolean frame = true;
	private boolean create = true;
	private boolean modify = true; 
	private boolean search = true;
	private boolean readOnly = false;
	private boolean asAggregate = false; 
	
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

	public boolean isAsAggregate() {
		return asAggregate;
	}

	public void setAsAggregate(boolean asAggregate) {
		this.asAggregate = asAggregate;
	}

	public boolean isModify() {
		return modify;		
	}

	public void setModify(boolean modify) {
		this.modify = modify;
	}
	
}
