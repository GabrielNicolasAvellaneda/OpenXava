/**
 * 
 */
package org.openxava.web.layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.controller.ModuleContext;
import org.openxava.model.meta.MetaCollection;
import org.openxava.model.meta.MetaMember;
import org.openxava.model.meta.MetaProperty;
import org.openxava.model.meta.MetaReference;
import org.openxava.util.Is;
import org.openxava.view.View;
import org.openxava.view.meta.MetaGroup;
import org.openxava.view.meta.MetaViewAction;
import org.openxava.view.meta.PropertiesSeparator;
import org.openxava.web.Ids;
import org.openxava.web.WebEditors;

/**
 * Layout manager, class to prepare view presentation. 
 * 
 * @author Juan Mendoza and Federico Alcantara
 *
 */
public class DefaultLayoutParser implements ILayoutParser {

	private static Log LOG = LogFactory.getLog(DefaultLayoutParser.class);

	private String propertyInReferencePrefix = "";
	private String groupLabel;
	private List<LayoutElement> elements;
	private HttpServletRequest request;
	private ModuleContext context;
	private boolean editable;
	private int groupLevel;
	private LayoutElement currentRow;
	private LayoutElement currentView;
	private Stack<LayoutElement> containersStack;
	private Stack<LayoutElement> rowsStack;
	private int rowIndex;
	private List<Integer> columnsPerRow;
	private List<View> sectionViews;

	public DefaultLayoutParser() {
	}
	
	/**
	 * Parses the layout in order to determine its size. No rendering
	 * occurs in this phase.
	 * @param view Originating view.
	 * @param pageContext pageContext.
	 * @return returnValue Integer value containing the count
	 */
	public List<LayoutElement> parseView(View view, PageContext pageContext) {
		return parseView(view, pageContext, false);
	}
	
	/**
	 * Parses the layout in order to determine its size. No rendering
	 * occurs in this phase.
	 * @param view Originating view.
	 * @param pageContext pageContext.
	 * @param representsSection If true this view is within a section.
	 * @return returnValue Integer value containing the count
	 */
	public List<LayoutElement> parseView(View view, PageContext pageContext, boolean representsSection) {
		request = (HttpServletRequest)pageContext.getRequest();
		context = (ModuleContext) request.getSession().getAttribute("context");
		sectionViews = new ArrayList<View>();
		parseLayout(view, representsSection);
		if (!representsSection) {
			if (!sectionViews.isEmpty()) {
				for (View sectionView : sectionViews) {
					addLayoutElement(createBeginSectionMarker(sectionView));
					addLayoutElement(createEndSectionMarker(sectionView));
					for (int index = 0; index < sectionView.getSections().size(); index++) {
						sectionView.getSectionView(index).setPropertyPrefix(sectionView.getPropertyPrefix());
					}
				}
			}
		}
		if (LOG.isDebugEnabled()) {
			StringBuffer buffer = new StringBuffer('\n');
			for (LayoutElement readElement : elements) {
				buffer.append(StringUtils.repeat("    ", readElement.getGroupLevel()) 
						+ readElement.toString());
				buffer.append('\n');
			}
			LOG.debug(buffer.toString());
		}
		return elements;
	}

	/**
	 * Calculate cells by rows.
	 * 
	 * @param view. View to process it metamembers.
	 */
	protected void parseLayout(View view, boolean representsSection) {
		groupLevel = 0;
		elements = new ArrayList<LayoutElement>();
		containersStack = new Stack<LayoutElement>();
		rowsStack = new Stack<LayoutElement>();
		rowIndex = -1;
		groupLevel = 0;
		columnsPerRow = new ArrayList<Integer>();
		currentRow = null;
		editable = false;
		currentView = createBeginViewMarker(view);
		currentView.setRepresentsSection(representsSection);
		addLayoutElement(currentView);
		parseMetamembers(view.getMetaMembers(), view, false, true);
		addLayoutElement(createEndViewMarker(view));
	}

	/**
	 * Parses each meta member in order to get a hint of the view to display.
	 * 
	 * @param metaMembers. Metamembers to processed.
	 * @param view. View to be processed.
	 * @param descriptionsList. True if the meta property is a descriptionsList
	 * @param isGrouped. True if this parsing should be treated as a group.
	 * 
	 */
	@SuppressWarnings("rawtypes")
	protected void parseMetamembers(Collection metaMembers, View view, boolean isDescriptionsList, boolean isGrouped) {
		boolean displayAsDescriptionsList = isDescriptionsList;
		boolean mustStartRow = isGrouped;
		boolean rowStarted = false;
		boolean frameOnSameColumn = false;
		int frameStartingRowIndex = -1;
		int frameMaxEndingRowIndex = -1;
		Iterator it = metaMembers.iterator();
		if (view.hasSections()) {
			sectionViews.add(view);
		}
		while (it.hasNext()) {
			MetaMember m = (MetaMember) it.next();
			LayoutElement element = null;
			if (!PropertiesSeparator.INSTANCE.equals(m)) {

		  		if (frameOnSameColumn) {
		  			rowIndex = frameStartingRowIndex;
		  		}

				if (m instanceof MetaProperty) {					
					MetaProperty p = (MetaProperty) m;
					if (view.isHidden(p.getName())) {
						continue;
					}
					LayoutElement last = elements.size() > 0 ? elements.get(elements.size() - 1) : null;
					LayoutElement beforeLast = elements.size() > 1 ? elements.get(elements.size() - 2) : null;
					LayoutElement propertyElement = elements.size() > 2 ? elements.get(elements.size() - 3) : null;
					MetaViewAction action = (p instanceof MetaViewAction) ? (MetaViewAction) p : null;
					boolean createProperty = true;
					if (action != null
							&& propertyElement != null
							&& propertyElement.getElementType().equals(LayoutElementType.PROPERTY_BEGIN)
							&& beforeLast.getElementType().equals(LayoutElementType.PROPERTY_END)
							&& last.getElementType().equals(LayoutElementType.COLUMN_END)
							&& !Is.emptyString(action.getAction())) {
						if (action.isAlwaysEnabled() || beforeLast.isEditable()) {
							propertyElement.getActionsNameForProperty().add(action.getAction());
							propertyElement.setActions(true);
						}
						createProperty = false;
					}

					if (createProperty) {
						if (!rowStarted	&& mustStartRow) {
							addLayoutElement(createBeginRowMarker(view));
							rowStarted = true;
						}
						setEditable(view.isEditable(p));
						
						addLayoutElement(createBeginColumnMarker(view));
						int rowIndexBeforeFrame = rowIndex;
						boolean hasFrame = WebEditors.hasFrame(p, view.getViewName());
						if (hasFrame) {
					  		addLayoutElement(createBeginFrameMarker(p, view, ""));
						}
						element = createBeginPropertyMarker(p, displayAsDescriptionsList, hasFrame, view); // hasFrame:true = suppress label
						addLayoutElement(element);
						addLayoutElement(createEndPropertyMarker(view));
						if (hasFrame) {
					  		addLayoutElement(createEndFrameMarker(p, view));
					  		rowIndex = rowIndexBeforeFrame;
						}
						addLayoutElement(createEndColumnMarker(view));

					}
					
					this.groupLabel = "";
					if (displayAsDescriptionsList) {
						break; // Only one
					}

				} else if (m instanceof MetaReference) {
					MetaReference ref = (MetaReference) m;
					if (view.isHidden(ref.getName())) {
						continue;
					}
					if (!rowStarted	&& mustStartRow) {
						addLayoutElement(createBeginRowMarker(view));
						rowStarted = true;
					}
					try {
						String viewObject = getViewObject(view) + "_" + ref.getName();
						View subView = view.getSubview(ref.getName());
						propertyInReferencePrefix = view.getPropertyPrefix() + ref.getName() + ".";
						subView.setPropertyPrefix(propertyInReferencePrefix);
						subView.setViewObject(viewObject);
						boolean isReferenceAsDescriptionsList = view.displayAsDescriptionsList(ref);
						boolean isFramed = isReferenceAsDescriptionsList ? false : subView.isFrame(); 

						context.put(request, subView.getViewObject(), subView);
						if (isFramed) {
							addLayoutElement(createBeginColumnMarker(view));
					  		addLayoutElement(createBeginFrameMarker(ref, view, ""));
					  		frameOnSameColumn = true;
						}
						
						frameStartingRowIndex = rowIndex;
						
						groupLabel = ref.getLabel();
						if (isReferenceAsDescriptionsList && subView.getMetaMembers().isEmpty()) {
							addLayoutElement(createBeginColumnMarker(view));
							addLayoutElement(createBeginPropertyMarker(ref, isReferenceAsDescriptionsList, false, subView));
							addLayoutElement(createEndColumnMarker(view));
						} else {
							parseMetamembers(subView.getMetaMembers(), subView, isReferenceAsDescriptionsList, isFramed || (rowStarted && !isReferenceAsDescriptionsList));
						}
						if (isFramed) {
					  		addLayoutElement(createEndFrameMarker(ref, view));
							addLayoutElement(createEndColumnMarker(view));
							if (rowIndex > frameMaxEndingRowIndex) {
								frameMaxEndingRowIndex = rowIndex;
							}
						}
						propertyInReferencePrefix = "";
					} catch (Exception ex) {
						LOG.info("Sub-view not found: " + ref.getName());
					}
			  	} else if (m instanceof MetaGroup) {
					if (!rowStarted	&& mustStartRow) {
						addLayoutElement(createBeginRowMarker(view));
						rowStarted = true;
					}
					MetaGroup group = (MetaGroup) m;
					View subView = view.getGroupView(group.getName());
					String viewObject = getViewObject(view) + "_" + group.getName();
					groupLabel = group.getLabel(request);
					if (subView != null) {
						subView.setPropertyPrefix(view.getPropertyPrefix());
						subView.setViewObject(viewObject);
					}

					frameOnSameColumn = true;
					frameStartingRowIndex = rowIndex;
					
					context.put(request, subView.getViewObject(), subView);
					addLayoutElement(createBeginColumnMarker(view));
			  		addLayoutElement(createBeginGroupMarker(group, subView, groupLabel));
					parseMetamembers(group.getMetaView().getMetaMembers(), subView, false, true);
					if (rowIndex > frameMaxEndingRowIndex) {
						frameMaxEndingRowIndex = rowIndex;
					}
			  		addLayoutElement(createEndGroupMarker(group, subView));
					addLayoutElement(createEndColumnMarker(view));
				} else if (m instanceof MetaCollection) {
					if (!rowStarted	&& mustStartRow) {
						addLayoutElement(createBeginRowMarker(view));
						rowStarted = true;
					}
					addLayoutElement(createBeginCollectionMarker(m, view));
					addLayoutElement(createEndCollectionMarker(view));
				}				
			} else {
				if (rowStarted) {
					addLayoutElement(createEndRowMarker(view));
					rowStarted = false;
				}
				mustStartRow = true;
				if (frameOnSameColumn) {
					rowIndex = frameMaxEndingRowIndex;
				}
				frameOnSameColumn = false;
			}
		}
		if (rowStarted) {
			addLayoutElement(createEndRowMarker(view));
			rowStarted = false;
		}
		if (frameOnSameColumn) {
			rowIndex = frameMaxEndingRowIndex;
		}
	}

	/**
	 * Method to get the viewObject of a view.
	 * 
	 * @param view. View to process.
	 * @return returnValue. Value with viewobject.
	 */
	protected String getViewObject(View view) {
		String returnValue = view.getViewObject();
		if (returnValue == null) {
			returnValue = LayoutKeys.LAYOUT_DEFAULT_VIEW_NAME;
		}
		return returnValue;
	}

	/**
	 * Creates a begin view marker.
	 * @param view Originating view.
	 * @return Created layout element.
	 */
	protected LayoutElement createBeginViewMarker(View view) {
		LayoutElement returnValue = new LayoutElement(view, groupLevel, LayoutElementType.VIEW_BEGIN);
		groupLevel++;
		containersStack.push(returnValue);
		return returnValue;
	}
	
	/**
	 * Creates an end view marker.
	 * @param view Originating view.
	 * @return Created layout element.
	 */
	protected LayoutElement createEndViewMarker(View view) {
		containersStack.pop();
		groupLevel--;
		return new LayoutElement(view, groupLevel, LayoutElementType.VIEW_END);
	}
	
	/**
	 * 
	 * @param m
	 * @param view
	 * @param label
	 * @return
	 */
	protected LayoutElement createBeginGroupMarker(MetaGroup metaGroup, View view, String label) {
		return createBeginFrameMarker(metaGroup, view, label, LayoutElementType.GROUP_BEGIN);
	}
	
	protected LayoutElement createEndGroupMarker(MetaGroup metaGroup, View view) {
		return createEndFrameMarker(metaGroup, view, LayoutElementType.GROUP_END);
	}
	
	protected LayoutElement createBeginFrameMarker(MetaMember metaMember, View view, String label) {
		return createBeginFrameMarker(metaMember, view, label, LayoutElementType.FRAME_BEGIN);
	}
	
	protected LayoutElement createEndFrameMarker(MetaMember metaMember, View view) {
		return createEndFrameMarker(metaMember, view, LayoutElementType.FRAME_END);
	}

	protected LayoutElement createBeginFrameMarker(MetaMember metaMember, View view, String label, LayoutElementType elementType) {
		LayoutElement returnValue = createMetaMemberElement(metaMember, view, elementType);
		groupLevel++;
		if (currentRow != null) {
			int framesCount = currentRow.getMaxFramesCount() + 1;
			currentRow.setMaxFramesCount(framesCount);
		}
		containersStack.push(returnValue);
		return returnValue;
	}
	
	protected LayoutElement createEndFrameMarker(MetaMember metaMember, View view, LayoutElementType elementType) {
		containersStack.pop();
		groupLevel--;
		LayoutElement returnValue = createMetaMemberElement(metaMember, view, elementType);;
		return returnValue;
	}
	/**
	 * Creates the start of row.
	 * @param view Current view;
	 */
	protected LayoutElement createBeginRowMarker(View view) {
		if ((columnsPerRow.size() - 1) <= rowIndex) {
			columnsPerRow.add(0);
		}
		rowIndex++;
		rowsStack.push(currentRow);
		currentRow = createMarker(view, LayoutElementType.ROW_BEGIN);
		currentRow.setRowIndex(rowIndex);
		groupLevel++;
		return currentRow;
	}
	
	/**
	 * Creates the end of row.
	 * @param view
	 * @param cellsCount
	 */
	private LayoutElement createEndRowMarker(View view) {
		LayoutElement returnValue = null;
		groupLevel--;
		LayoutElement last = null;
		if (elements.size() > 0) {
			last = elements.get(elements.size() - 1);
		}
		if (last != null && last.getElementType().equals(LayoutElementType.ROW_BEGIN)) { // empty row
			elements.remove(elements.size() - 1);
		} else {
			returnValue = createMarker(view, LayoutElementType.ROW_END);
		}
		currentRow = rowsStack.pop();
		return returnValue;
	}

	/**
	 * Create element for section begin.
	 * 
	 * @param view. View object.
	 * @return A Layout Element of type SECTIONS_BEGIN
	 */
	protected LayoutElement createBeginSectionMarker(View view) {
		LayoutElement returnValue = new LayoutElement(view, groupLevel);
		returnValue.setSections(true);
		returnValue.setView(view);
		returnValue.setElementType(LayoutElementType.SECTIONS_BEGIN);
		return returnValue;
	}
	
	/**
	 * Create element for section end.
	 * 
	 * @param view. View object.
	 * @return A Layout Element of type SECTIONS_BEGIN
	 */
	protected LayoutElement createEndSectionMarker(View view) {
		createMarker(view, LayoutElementType.SECTIONS_END);
		LayoutElement returnValue = new LayoutElement(view, groupLevel);
		returnValue.setSections(true);
		returnValue.setView(view);
		returnValue.setElementType(LayoutElementType.SECTIONS_END);
		return returnValue;
	}

	/**
	 * Create a marker for the beginning of a column
	 * @param view Current view object
	 * @return A 
	 */
	protected LayoutElement createBeginColumnMarker(View view) {
		LayoutElement returnValue = createMarker(view, LayoutElementType.COLUMN_BEGIN);
		// Add to column
		int maxRowColumnsCount = currentRow.getMaxRowColumnsCount() + 1;
		currentRow.setMaxRowColumnsCount(maxRowColumnsCount);
		
		// Add to indexedRow
		int rowIndex = currentRow.getRowIndex();
		int columnsPerIndexedRow = columnsPerRow.get(rowIndex) + 1;
		columnsPerRow.set(rowIndex, columnsPerIndexedRow);
		
		// Add to container
		int maxContainerColumnsCount = containersStack.peek().getMaxContainerColumnsCount();
		if (columnsPerIndexedRow > maxContainerColumnsCount) {
			containersStack.peek().setMaxContainerColumnsCount(columnsPerIndexedRow);
		}
		
		// Add to view
		int maxViewColumnsCount = currentView.getMaxViewColumnsCount();
		if (columnsPerIndexedRow > maxViewColumnsCount) {
			currentView.setMaxViewColumnsCount(columnsPerIndexedRow);
		}
		
		groupLevel++;
		return returnValue;
	}
	
	/**
	 * Creates the marker for column end.
	 * @param view Current view.
	 * @return A layout element of type COLUMN_END
	 */
	protected LayoutElement createEndColumnMarker(View view) {
		groupLevel--;
		return createMarker(view, LayoutElementType.COLUMN_END);
	}

	/**
	 * Returns a marker element. It basically has its type and view properties set.
	 * @param view Current view.
	 * @param elementType Element type.
	 * @return A layout element of the type defined in elementType.
	 */
	protected LayoutElement createMarker(View view, LayoutElementType elementType) {
		LayoutElement returnValue = new LayoutElement(view, groupLevel, elementType);
		returnValue.setView(view);
		return returnValue;
	}
	
	/**
	 * Method to create layout elements.
	 * 
	 * @param m. Metamember to process
	 * @param section. If view is a section
	 * @param frame. If view has frame.
	 * @param descriptionsList. If view must be display as description list.
	 * @param view. View with special meaning.
	 * @return returnValue. Layout element of type PROPERTY_BEGIN.
	 */
	protected LayoutElement createBeginPropertyMarker(MetaMember m, boolean descriptionsList, boolean suppressLabel, View view) {
		LayoutElement returnValue = new LayoutElement(view, groupLevel);
		returnValue.setFrame(false);
		returnValue.setSections(false);
		returnValue.setElementType(LayoutElementType.PROPERTY_BEGIN);
		String referenceForDescriptionsList = view.getPropertyPrefix();
		
		String propertyPrefix = propertyInReferencePrefix;
		String propertyLabel = null;
		boolean isKey = false;
		boolean isSearchKey = false;
		boolean isLastSearchKey = false;
		boolean isSearch = false;
		boolean throwsChanged = false;
		boolean hasActions = false;
		int labelFormat = 0;
		
		MetaProperty p = null;
		if (m instanceof MetaProperty) {
			p = (MetaProperty) m;
			isKey = p.isKey();
			isSearchKey = p.isSearchKey();
			isLastSearchKey = view.isLastSearchKey(p);
			throwsChanged = view.throwsPropertyChanged(p);
			hasActions = view.propertyHasActions(p);
			labelFormat = view.getLabelFormatForProperty(p);
			returnValue.setMetaProperty(p);
		}
		
		MetaReference ref = null;
		if (m instanceof MetaReference) {
			ref = (MetaReference) m;
			isKey = ref.isKey();
			isSearchKey = ref.isSearchKey();
			isLastSearchKey = view.isLastSearchKey(ref.getName());
			throwsChanged = view.throwsReferenceChanged(ref);
			labelFormat = view.getLabelFormatForReference(ref);
			returnValue.setMetaReference(ref);
		}
		
		if (!suppressLabel) {
			propertyLabel = descriptionsList ? groupLabel : view.getLabelFor(m);
			if (propertyLabel == null) {
				propertyLabel = m.getLabel();
			}
		}
		if (Is.empty(propertyPrefix)) {
			propertyPrefix = referenceForDescriptionsList;
		}
		String propertyKey= Ids.decorate(
				request.getParameter("application"),
				request.getParameter("module"),
				propertyPrefix + m.getName());

		try {
			if ((isKey || (isSearchKey && isLastSearchKey))
					&& view.isRepresentsEntityReference()) {
				isSearch = view.isSearch();
				returnValue.setSearch(isSearch);
				returnValue.setCreateNew(view.isCreateNew());
				returnValue.setModify(view.isModify());
			}
			if (view.getPropertyPrefix() == null) {
				view.setPropertyPrefix("");
			}
			returnValue.setName(m.getName());
			returnValue.setEditable(isEditable());//(view.isEditable(p)); // Must confirm this
			returnValue.setSearchAction(view.getSearchAction());
			returnValue.setLabel(propertyLabel);
			returnValue.setLabelFormat(labelFormat);
			returnValue.setThrowPropertyChanged(throwsChanged);
			returnValue.setPropertyKey(propertyKey);
			returnValue.setPropertyPrefix(view.getPropertyPrefix());
			returnValue.setLastSearchKey(isLastSearchKey);
			returnValue.setSearch(isSearch || isLastSearchKey);
			returnValue.setDisplayAsDescriptionsList(descriptionsList);
			if (returnValue.isEditable()) {
				returnValue.setActionsNameForReference(getActionsNameForReference(view, isLastSearchKey));
			}
			returnValue.setActionsNameForProperty(getActionsNameForProperty(view, p, returnValue.isEditable()));
			if (referenceForDescriptionsList.length() > 1) {
				referenceForDescriptionsList = referenceForDescriptionsList.substring(0, referenceForDescriptionsList.length() - 1);
			}
			returnValue.setReferenceForDescriptionsList(referenceForDescriptionsList);
			returnValue.setActions(hasActions ||
					(returnValue.getActionsNameForReference() != null &&
					!returnValue.getActionsNameForReference().isEmpty()) ||
					(returnValue.getActionsNameForProperty() != null &&
					!returnValue.getActionsNameForProperty().isEmpty()));
		} catch (Exception ex) {
			LOG.warn("Maybe this is a separator:" + p.getName());
		}
		returnValue.setGroupLevel(groupLevel);
		return returnValue;
	}

	/**
	 * Creates an end property marker.
	 * @param view Current view.
	 * @return Layout element of type PROPERTY_END.
	 */
	protected LayoutElement createEndPropertyMarker(View view) {
		return createMarker(view, LayoutElementType.PROPERTY_END);
	}

	/**
	 * Returns a marker element. It basically has its type and view properties set.
	 * @param view Originating view.
	 * @param elementType Element type.
	 * @return Newly created element. Never null.
	 */
	protected LayoutElement createMetaMemberElement(MetaMember m, View view, LayoutElementType elementType) {
		LayoutElement returnValue = new LayoutElement(view, groupLevel, elementType);
		returnValue.setView(view);
		returnValue.setPropertyPrefix("");
		returnValue.setLabel(m.getLabel());
		returnValue.setName(m.getName());
		return returnValue;
	}
	
	/**
	 * Method to create collection layout elements.
	 * 
	 * @param m. Metamember to process
	 * @param section. If view is a section
	 * @param frame. If view has frame.
	 * @param descriptionsList. If view must be display as description list.
	 * @param view. View with special meaning.
	 * @return returnValue. Layout element of type COLLECTION_BEGIN
	 */
	protected LayoutElement createBeginCollectionMarker(MetaMember m, View view) {
		LayoutElement returnValue = new LayoutElement(view, groupLevel);
		returnValue.setFrame(false);
		returnValue.setSections(false);
		returnValue.setView(view);
		if (Is.empty(view.getMemberName())) {
			view.setMemberName("");
		}
		if (Is.empty(view.getPropertyPrefix())) {
			view.setPropertyPrefix("");
		}
		MetaCollection collection = (MetaCollection) m;
		returnValue.setElementType(LayoutElementType.COLLECTION_BEGIN);
		returnValue.setMetaCollection(collection);
		returnValue.setLabel(collection.getLabel(request));
		returnValue.setView(view);
		returnValue.setFrame(!view.isSection() || view.getMetaMembers().size() > 1);
		return returnValue;
	}
	
	/**
	 * Creates a collection end marker.
	 * @param view Current view.
	 * @return A layout element of type COLLECTION_END.
	 */
	protected LayoutElement createEndCollectionMarker(View view) {
		return createMarker(view, LayoutElementType.COLLECTION_END);
	}
	
	
	@SuppressWarnings("unchecked")
	private Collection<String> getActionsNameForProperty(View view, MetaProperty p,
			boolean editable) {
		Collection<String> returnValues = new ArrayList<String>();
		if (p != null) {
			for (java.util.Iterator<String> itActions = view.getActionsNamesForProperty(p, editable).iterator(); itActions.hasNext();) {
				returnValues.add((String) itActions.next());
			}
		}
		return returnValues;
	}

	@SuppressWarnings("unchecked")
	private Collection<String> getActionsNameForReference(View view, boolean lastSearchKey) {
		Collection<String> returnValues = new ArrayList<String>();
		for (java.util.Iterator<String> itActions = view.getActionsNamesForReference(lastSearchKey).iterator(); itActions.hasNext();) {
			returnValues.add((String) itActions.next());
		}
		return returnValues;
	}

	/**
	 * Adds a layout element to the list of displayable objects.
	 * @param e Layout element to add
	 */
	private void addLayoutElement(LayoutElement e) {
		if (elements == null) {
			elements = new ArrayList<LayoutElement>();
		}
		if (e != null) {
			elements.add(e);
			LOG.trace(StringUtils.repeat("    ", e.getGroupLevel()) 
				+ e.toString());
		}
	}

	/**
	 * @return the elements
	 */
	public List<LayoutElement> getElements() {
		return elements;
	}

	/**
	 * @param elements the elements to set
	 */
	public void setElements(List<LayoutElement> elements) {
		this.elements = elements;
	}

	/**
	 * @return the editable
	 */
	protected boolean isEditable() {
		return editable;
	}

	/**
	 * @param editable the editable to set
	 */
	protected void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * @return the request
	 */
	protected HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	protected void setRequest(HttpServletRequest request) {
		this.request = request;
	}

}
