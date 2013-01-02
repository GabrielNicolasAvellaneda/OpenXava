/**
 * 
 */
package org.openxava.web.layout;

import java.io.Serializable;
import java.util.Collection;

import org.openxava.model.meta.MetaCollection;
import org.openxava.model.meta.MetaProperty;
import org.openxava.model.meta.MetaReference;
import org.openxava.view.View;

/**
 * @author Juan Mendoza and Federico Alcantara
 *
 */
public class LayoutElement implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private MetaProperty metaProperty;
	private MetaReference metaReference;
	private MetaCollection metaCollection;
	private Collection<String> actionsNameForReference;
	private Collection<String> actionsNameForProperty;
	private View view;
	
	private LayoutElementType elementType;
	private boolean actions = false;
	private boolean sections = false;
	private boolean frame = false;
	private boolean separator = false;
	private boolean editable = false;
	private boolean search = false;
	private boolean lastSearchKey = false;
	private boolean createNew = false;
	private boolean modify = false;
	private boolean throwPropertyChanged = false;
	private boolean displayAsDescriptionsList = false;
	private String label;
	private String searchAction;
	private int labelFormat;
	private String propertyKey;
	private String propertyPrefix = "";
	private String referenceForDescriptionsList;
	private String name;
	private int groupLevel;
	private Integer maxRowColumnsCount;
	private Integer rowCurrentColumnsCount;
	private Integer maxContainerColumnsCount;
	private Integer maxViewColumnsCount;
	private Integer maxFramesCount;
	private Integer rowIndex;
	private boolean rowClosed;
	
	/**
	 * Default constructor.
	 * @param view Originating view.
	 * @param groupLevel Group level.
	 */
	public LayoutElement(View view, int groupLevel) {
		this.view = view;
		this.groupLevel = groupLevel;
		elementType = LayoutElementType.ROW_START;
		maxRowColumnsCount = 0;
		rowCurrentColumnsCount = 0;
		maxContainerColumnsCount = 0;
		maxFramesCount = 0;
		maxViewColumnsCount = 0;
		rowIndex = -1;
	}
	
	/**
	 * Preferred constructor.
	 * @param view Originating view.
	 * @param groupLevel Group level.
	 * @param elementType Type of element that is being created.
	 */
	public LayoutElement(View view, int groupLevel, LayoutElementType elementType) {
		this(view, groupLevel);
		this.elementType = elementType;
	}
	
	/**
	 * @return the hasSection
	 */
	public boolean hasSections() {
		return sections;
	}
	/**
	 * @param hasSection the hasSection to set
	 */
	public void setSections(boolean sections) {
		this.sections = sections;
	}
	/**
	 * @return the metaProperty
	 */
	public MetaProperty getMetaProperty() {
		return metaProperty;
	}
	/**
	 * @param metaProperty the metaProperty to set
	 */
	public void setMetaProperty(MetaProperty metaProperty) {
		this.metaProperty = metaProperty;
	}
	/**
	 * @return the metaReference
	 */
	public MetaReference getMetaReference() {
		return metaReference;
	}
	/**
	 * @param metaReference the metaReference to set
	 */
	public void setMetaReference(MetaReference metaReference) {
		this.metaReference = metaReference;
	}
	/**
	 * @return the metaCollection
	 */
	public MetaCollection getMetaCollection() {
		return metaCollection;
	}
	/**
	 * @param metaCollection the metaCollection to set
	 */
	public void setMetaCollection(MetaCollection metaCollection) {
		this.metaCollection = metaCollection;
	}
	/**
	 * @param actionsNameForReference the actionsNameForReference to set
	 */
	public void setActionsNameForReference(Collection<String> actionsNameForReference) {
		this.actionsNameForReference = actionsNameForReference;
	}
	/**
	 * @return the actionsNameForReference
	 */
	public Collection<String> getActionsNameForReference() {
		return actionsNameForReference;
	}
	/**
	 * @param actionsNameForProperty the actionsNameForProperty to set
	 */
	public void setActionsNameForProperty(Collection<String> actionsNameForProperty) {
		this.actionsNameForProperty = actionsNameForProperty;
	}
	/**
	 * @return the actionsNameForProperty
	 */
	public Collection<String> getActionsNameForProperty() {
		return actionsNameForProperty;
	}
	/**
	 * @param actions the actions to set
	 */
	public void setActions(boolean hasActions) {
		this.actions = hasActions;
	}
	/**
	 * @return the actions
	 */
	public boolean hasActions() {
		return actions;
	}
	/**
	 * @return the frame
	 */
	public boolean hasFrame() {
		return frame;
	}
	/**
	 * @param frame the frame to set
	 */
	public void setFrame(boolean hasframe) {
		this.frame = hasframe;
	}
	/**
	 * @return the separator
	 */
	public boolean isSeparator() {
		return separator;
	}
	/**
	 * @param separator the separator to set
	 */
	public void setSeparator(boolean separator) {
		this.separator = separator;
	}
	/**
	 * @return the editable
	 */
	public boolean isEditable() {
		return editable;
	}
	/**
	 * @param editable the editable to set
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	/**
	 * @return the search
	 */
	public boolean isSearch() {
		return search;
	}
	/**
	 * @param search the search to set
	 */
	public void setSearch(boolean search) {
		this.search = search;
	}
	/**
	 * @param lastSearchKey the lastSearchKey to set
	 */
	public void setLastSearchKey(boolean lastSearchKey) {
		this.lastSearchKey = lastSearchKey;
	}
	/**
	 * @return the lastSearchKey
	 */
	public boolean isLastSearchKey() {
		return lastSearchKey;
	}
	/**
	 * @return the createNew
	 */
	public boolean isCreateNew() {
		return createNew;
	}
	/**
	 * @param createNew the createNew to set
	 */
	public void setCreateNew(boolean createNew) {
		this.createNew = createNew;
	}
	/**
	 * @return the modify
	 */
	public boolean isModify() {
		return modify;
	}
	/**
	 * @param modify the modify to set
	 */
	public void setModify(boolean modify) {
		this.modify = modify;
	}
	/**
	 * @param throwPropertyChanged the throwPropertyChanged to set
	 */
	public void setThrowPropertyChanged(boolean throwPropertyChanged) {
		this.throwPropertyChanged = throwPropertyChanged;
	}
	/**
	 * @return the throwPropertyChanged
	 */
	public boolean isThrowPropertyChanged() {
		return throwPropertyChanged;
	}
	/**
	 * @param displayAsDescriptionsList the displayAsDescriptionsList to set
	 */
	public void setDisplayAsDescriptionsList(boolean displayAsDescriptionsList) {
		this.displayAsDescriptionsList = displayAsDescriptionsList;
	}
	/**
	 * @return the displayAsDescriptionsList
	 */
	public boolean isDisplayAsDescriptionsList() {
		return displayAsDescriptionsList;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @return the searchAction
	 */
	public String getSearchAction() {
		return searchAction;
	}
	/**
	 * @param searchAction the searchAction to set
	 */
	public void setSearchAction(String searchAction) {
		this.searchAction = searchAction;
	}
	/**
	 * @param labelFormat the labelFormat to set
	 */
	public void setLabelFormat(int labelFormat) {
		this.labelFormat = labelFormat;
	}
	/**
	 * @return the labelFormat
	 */
	public int getLabelFormat() {
		return labelFormat;
	}
	/**
	 * @param propertyPrefix the propertyPrefix to set
	 */
	public void setPropertyPrefix(String propertyPrefix) {
		this.propertyPrefix = propertyPrefix;
	}
	/**
	 * @return the propertyPrefix
	 */
	public String getPropertyPrefix() {
		if (propertyPrefix == null) {
			return "";
		}
		return propertyPrefix;
	}
	/**
	 * @param propertyKey the propertyKey to set
	 */
	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}
	/**
	 * @return the propertyKey
	 */
	public String getPropertyKey() {
		return propertyKey;
	}
	/**
	 * @param referenceForDescriptionsList the referenceForDescriptionsList to set
	 */
	public void setReferenceForDescriptionsList(
			String referenceForDescriptionsList) {
		this.referenceForDescriptionsList = referenceForDescriptionsList;
	}
	/**
	 * @return the referenceForDescriptionsList
	 */
	public String getReferenceForDescriptionsList() {
		return referenceForDescriptionsList;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param groupLevel the groupLevel to set
	 */
	public void setGroupLevel(int groupLevel) {
		this.groupLevel = groupLevel;
	}
	/**
	 * @return the groupLevel
	 */
	public int getGroupLevel() {
		return groupLevel;
	}
	/**
	 * @param view the view to set
	 */
	public void setView(View view) {
		if (view.getPropertyPrefix() == null) {
			view.setPropertyPrefix("");
		}
		if (view.getMemberName() == null) {
			view.setMemberName("");
		}
		this.view = view;
	}
	/**
	 * @return the view
	 */
	public View getView() {
		return view;
	}
	/**
	 * @return the elementType
	 */
	public LayoutElementType getElementType() {
		return elementType;
	}
	/**
	 * @param elementType the elementType to set
	 */
	public void setElementType(LayoutElementType elementType) {
		this.elementType = elementType;
	}
	
	/**
	 * @return The maximum number of columns displayed in the row.
	 */
	public Integer getMaxRowColumnsCount() {
		return maxRowColumnsCount;
	}

	/**
	 * @param maxRowColumnsCount the maxRowColumnsCount to set
	 */
	public void setMaxRowColumnsCount(Integer maxRowCellsCount) {
		this.maxRowColumnsCount = maxRowCellsCount;
	}

	/**
	 * This value only makes sense if the layout element is of
	 * type LayoutElementType.ROW_START.
	 * @return While rendering keeps the already processed columns.
	 */
	public Integer getRowCurrentColumnsCount() {
		return rowCurrentColumnsCount;
	}

	/**
	 * @param rowCurrentColumnsCount the rowCurrentColumnsCount to set
	 */
	public void setRowCurrentColumnsCount(Integer currentRowCellsCount) {
		this.rowCurrentColumnsCount = currentRowCellsCount;
	}

	/**
	 * This value only makes sense if the layout element is of
	 * type LayoutElementType.GROUP_START or LayoutElementType.FRAME_START.
	 * @return the maxContainerColumnsCount
	 */
	public Integer getMaxContainerColumnsCount() {
		return maxContainerColumnsCount;
	}

	/**
	 * @param maxContainerColumnsCount the maxContainerColumnsCount to set
	 */
	public void setMaxContainerColumnsCount(Integer maxContainerCellsCount) {
		this.maxContainerColumnsCount = maxContainerCellsCount;
	}

	/**
	 * @return the maxViewColumnsCount
	 */
	public Integer getMaxViewColumnsCount() {
		return maxViewColumnsCount;
	}

	/**
	 * @param maxViewColumnsCount the maxViewColumnsCount to set
	 */
	public void setMaxViewColumnsCount(Integer maxViewColumnsCount) {
		this.maxViewColumnsCount = maxViewColumnsCount;
	}

	/**
	 * @return the maxFramesCount
	 */
	public Integer getMaxFramesCount() {
		return maxFramesCount;
	}

	/**
	 * @param maxFramesCount the maxFramesCount to set
	 */
	public void setMaxFramesCount(Integer maxFramesCount) {
		this.maxFramesCount = maxFramesCount;
	}

	/**
	 * @return the rowIndex
	 */
	public Integer getRowIndex() {
		return rowIndex;
	}

	/**
	 * @param rowIndex the rowIndex to set
	 */
	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

	/**
	 * @return the rowClosed
	 */
	public boolean isRowClosed() {
		return rowClosed;
	}

	/**
	 * @param rowClosed the rowClosed to set
	 */
	public void setRowClosed(boolean rowClosed) {
		this.rowClosed = rowClosed;
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[" + elementType 
				+ (label != null ? ", " + label : "")
				+ (maxViewColumnsCount > 0 ? ", View cols:" + maxViewColumnsCount : "")
				+ (maxContainerColumnsCount > 0 ? ", Container cols:" + maxContainerColumnsCount : "")
				+ (maxRowColumnsCount > 0 ? ", Row cols:" + maxRowColumnsCount : "")
				+ (maxFramesCount > 0 ? ", Frames:" + maxFramesCount : "")
				+ "]" ;
	}


}
