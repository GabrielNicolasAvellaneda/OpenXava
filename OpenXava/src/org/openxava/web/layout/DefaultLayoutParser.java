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
 * Layout manager, class to prepare view presentation
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
		request = (HttpServletRequest)pageContext.getRequest();
		context = (ModuleContext) request.getSession().getAttribute("context");
		parseLayout(view);
		if (view.hasSections()) {
			addLayoutElement(createSectionMarker(view));
			addLayoutElement(createMarker(view, LayoutElementType.SECTIONS_END));
		}
		System.out.println("");
		for (LayoutElement readElement : elements) {
			System.out.println(StringUtils.repeat("    ", readElement.getGroupLevel()) 
					+ readElement.toString());
		}
		return elements;
	}

	/**
	 * Calculate cells by rows.
	 * 
	 * @param view. View to process it metamembers.
	 */
	private void parseLayout(View view) {
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
	private void parseMetamembers(Collection metaMembers, View view, boolean isDescriptionsList, boolean isGrouped) {
		boolean displayAsDescriptionsList = isDescriptionsList;
		boolean mustStartRow = isGrouped;
		boolean rowStarted = false;
		boolean frameOnSameColumn = false;
		int frameStartingRowIndex = -1;
		int frameMaxEndingRowIndex = -1;
		Iterator it = metaMembers.iterator();
		while (it.hasNext()) {
			MetaMember m = (MetaMember) it.next();
			LayoutElement element = null;
			if (!PropertiesSeparator.INSTANCE.equals(m)) {

		  		if (frameOnSameColumn) {
		  			rowIndex = frameStartingRowIndex;
		  		}

				if (m instanceof MetaProperty) {					
					MetaProperty p = (MetaProperty) m;
					LayoutElement last = elements.size() > 0 ? elements.get(elements.size() - 1) : null;
					LayoutElement beforeLast = elements.size() > 1 ? elements.get(elements.size() - 2) : null;
					LayoutElement propertyElement = elements.size() > 2 ? elements.get(elements.size() - 3) : null;
					MetaViewAction action = (p instanceof MetaViewAction) ? (MetaViewAction) p : null;
					boolean createProperty = true;
					if (action != null
							&& propertyElement != null
							&& propertyElement.getElementType().equals(LayoutElementType.CELL_START)
							&& beforeLast.getElementType().equals(LayoutElementType.CELL_END)
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
							createBeginRowMarker(view);
							rowStarted = true;
						}
						setEditable(view.isEditable(p));
						addLayoutElement(createStartColumn(view));
						int rowIndexBeforeFrame = rowIndex;
						boolean hasFrame = WebEditors.hasFrame(p, view.getViewName());
						if (hasFrame) {
					  		addLayoutElement(createBeginFrame(p, view, ""));
						}
						
						element = createProperty(p, displayAsDescriptionsList, hasFrame, view); // hasFrame:true = suppress label
						addLayoutElement(element);
						addLayoutElement(createMarker(view, LayoutElementType.CELL_END));
						if (hasFrame) {
					  		addLayoutElement(createEndFrame(p, view));
					  		rowIndex = rowIndexBeforeFrame;
						}
						addLayoutElement(createEndColumn(view));

					}
					
					this.groupLabel = "";


				} else if (m instanceof MetaReference) {
					if (!rowStarted	&& mustStartRow) {
						createBeginRowMarker(view);
						rowStarted = true;
					}
					MetaReference ref = (MetaReference) m;
					try {
						String viewObject = getViewObject(view) + "_" + ref.getName();
						View subView = view.getSubview(ref.getName());
						propertyInReferencePrefix = view.getPropertyPrefix() + ref.getName() + ".";
						subView.setPropertyPrefix(propertyInReferencePrefix);
						subView.setViewObject(viewObject);
						boolean isDescriptionList = view.displayAsDescriptionsList(ref);
						boolean isFramed = isDescriptionList ? false : subView.isFrame(); 

						context.put(request, subView.getViewObject(), subView);
						if (isFramed) {
							addLayoutElement(createStartColumn(view));
					  		addLayoutElement(createBeginFrame(ref, view, ""));
					  		frameOnSameColumn = true;
						}
						
						frameStartingRowIndex = rowIndex;
						
						groupLabel = ref.getLabel();
						parseMetamembers(subView.getMetaMembers(), subView, isDescriptionList, isFramed || (rowStarted && !isDescriptionList));
						if (isFramed) {
					  		addLayoutElement(createEndFrame(ref, view));
							addLayoutElement(createEndColumn(view));
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
						createBeginRowMarker(view);
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
					addLayoutElement(createStartColumn(view));
			  		addLayoutElement(createBeginGroup(group, subView, groupLabel));
					parseMetamembers(group.getMetaView().getMetaMembers(), subView, false, true);
					if (rowIndex > frameMaxEndingRowIndex) {
						frameMaxEndingRowIndex = rowIndex;
					}
			  		addLayoutElement(createEndGroup(group, subView));
					addLayoutElement(createEndColumn(view));
				} else if (m instanceof MetaCollection) {
					addLayoutElement(createCollection(m, view));
					addLayoutElement(createMarker(view, LayoutElementType.COLLECTION_END));
				}				
			} else {
				if (rowStarted) {
					createEndRowMarker(view);
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
			createEndRowMarker(view);
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
	private String getViewObject(View view) {
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
	private LayoutElement createBeginViewMarker(View view) {
		LayoutElement returnValue = new LayoutElement(view, groupLevel, LayoutElementType.VIEW_START);
		groupLevel++;
		containersStack.push(returnValue);
		return returnValue;
	}
	
	/**
	 * Creates an end view marker.
	 * @param view Originating view.
	 * @return Created layout element.
	 */
	private LayoutElement createEndViewMarker(View view) {
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
	private LayoutElement createBeginGroup(MetaGroup metaGroup, View view, String label) {
		return createBeginFrame(metaGroup, view, label, LayoutElementType.GROUP_START);
	}
	
	private LayoutElement createEndGroup(MetaGroup metaGroup, View view) {
		return createEndFrame(metaGroup, view, LayoutElementType.GROUP_END);
	}
	
	private LayoutElement createBeginFrame(MetaMember metaMember, View view, String label) {
		return createBeginFrame(metaMember, view, label, LayoutElementType.FRAME_START);
	}
	
	private LayoutElement createEndFrame(MetaMember metaMember, View view) {
		return createEndFrame(metaMember, view, LayoutElementType.FRAME_END);
	}

	private LayoutElement createBeginFrame(MetaMember metaMember, View view, String label, LayoutElementType elementType) {
		LayoutElement returnValue = createMetaMemberElement(metaMember, view, elementType);
		groupLevel++;
		if (currentRow != null) {
			int framesCount = currentRow.getMaxFramesCount() + 1;
			currentRow.setMaxFramesCount(framesCount);
		}
		containersStack.push(returnValue);
		return returnValue;
	}
	
	private LayoutElement createEndFrame(MetaMember metaMember, View view, LayoutElementType elementType) {
		containersStack.pop();
		groupLevel--;
		LayoutElement returnValue = createMetaMemberElement(metaMember, view, elementType);;
		return returnValue;
	}

	/**
	 * Returns a marker element. It basically has its type and view properties set.
	 * @param view Originating view.
	 * @param elementType Element type.
	 * @return Newly created element. Never null.
	 */
	private LayoutElement createMetaMemberElement(MetaMember m, View view, LayoutElementType elementType) {
		LayoutElement returnValue = new LayoutElement(view, groupLevel, elementType);
		returnValue.setView(view);
		returnValue.setPropertyPrefix("");
		returnValue.setLabel(m.getLabel());
		returnValue.setName(m.getName());
		return returnValue;
	}
	
	/**
	 * Creates the start of row.
	 * @param view Current view;
	 */
	private void createBeginRowMarker(View view) {
		if ((columnsPerRow.size() - 1) <= rowIndex) {
			columnsPerRow.add(0);
		}
		rowIndex++;
		rowsStack.push(currentRow);
		currentRow = createMarker(view, LayoutElementType.ROW_START);
		currentRow.setRowIndex(rowIndex);
		groupLevel++;
		addLayoutElement(currentRow);
	}
	
	/**
	 * Creates the end of row.
	 * @param view
	 * @param cellsCount
	 */
	private void createEndRowMarker(View view) {
		groupLevel--;
		LayoutElement last = null;
		if (elements.size() > 0) {
			last = elements.get(elements.size() - 1);
		}
		if (last != null && last.getElementType().equals(LayoutElementType.ROW_START)) { // empty row
			elements.remove(elements.size() - 1);
		} else {
			addLayoutElement(createMarker(view, LayoutElementType.ROW_END));
		}
		currentRow = rowsStack.pop();
	}

	/**
	 * Create element for section.
	 * 
	 * @param view. View object.
	 * @return returnValue. LayoutElement.
	 */
	private LayoutElement createSectionMarker(View view) {
		LayoutElement returnValue = new LayoutElement(view, groupLevel);
		returnValue.setSections(true);
		returnValue.setView(view);
		returnValue.setElementType(LayoutElementType.SECTIONS_START);
		return returnValue;
	}
	
	private LayoutElement createStartColumn(View view) {
		LayoutElement returnValue = createMarker(view, LayoutElementType.COLUMN_START);
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
	
	private LayoutElement createEndColumn(View view) {
		groupLevel--;
		return createMarker(view, LayoutElementType.COLUMN_END);
	}

	/**
	 * Returns a marker element. It basically has its type and view properties set.
	 * @param view Originating view.
	 * @param elementType Element type.
	 * @return Newly created element. Never null.
	 */
	private LayoutElement createMarker(View view, LayoutElementType elementType) {
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
	 * @return returnValue. Layout element.
	 */
	private LayoutElement createProperty(MetaMember m, boolean descriptionsList, boolean suppressLabel, View view) {
		LayoutElement returnValue = new LayoutElement(view, groupLevel);
		returnValue.setFrame(false);
		returnValue.setSections(false);
		returnValue.setElementType(LayoutElementType.CELL_START);
		String referenceForDescriptionsList = view.getPropertyPrefix();
		MetaProperty p = (MetaProperty) m;
		String propertyPrefix = propertyInReferencePrefix;
		String propertyLabel = null;
		if (!suppressLabel) {
			propertyLabel = descriptionsList ? groupLabel : view.getLabelFor(p);
			if (propertyLabel == null) {
				propertyLabel = p.getLabel();
			}
		}
		if (Is.empty(propertyPrefix)) {
			propertyPrefix = referenceForDescriptionsList;
		}
		String propertyKey= Ids.decorate(
				request.getParameter("application"),
				request.getParameter("module"),
				propertyPrefix + p.getName());
		returnValue.setMetaProperty(p);

		try {
			if (p.isKey() && view.isRepresentsEntityReference()) {
				returnValue.setSearch(view.isSearch());
				returnValue.setCreateNew(view.isCreateNew());
				returnValue.setModify(view.isModify());
			}
			if (view.getPropertyPrefix() == null) {
				view.setPropertyPrefix("");
			}
			returnValue.setActions(view.propertyHasActions(p));
			returnValue.setEditable(isEditable());//(view.isEditable(p)); // Must confirm this
			returnValue.setSearchAction(view.getSearchAction());
			returnValue.setLabel(propertyLabel);
			returnValue.setLabelFormat(view.getLabelFormatForProperty(p));
			returnValue.setThrowPropertyChanged(view.throwsPropertyChanged(p));
			returnValue.setPropertyKey(propertyKey);
			returnValue.setPropertyPrefix(view.getPropertyPrefix());
			returnValue.setLastSearchKey(view.isLastSearchKey(p));
			returnValue.setDisplayAsDescriptionsList(descriptionsList);
			if (returnValue.isEditable()) {
				returnValue.setActionsNameForReference(getActionsNameForReference(view, view.isLastSearchKey(p)));
			}
			returnValue.setActionsNameForProperty(getActionsNameForProperty(view, p, returnValue.isEditable()));
			if (referenceForDescriptionsList.length() > 1) {
				referenceForDescriptionsList = referenceForDescriptionsList.substring(0, referenceForDescriptionsList.length() - 1);
			}
			returnValue.setReferenceForDescriptionsList(referenceForDescriptionsList);
		} catch (Exception ex) {
			LOG.warn("Maybe this is a separator:" + p.getName());
		}
		returnValue.setGroupLevel(groupLevel);
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
	 * @return returnValue. Layout element.
	 */
	private LayoutElement createCollection(MetaMember m, View view) {
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
		returnValue.setElementType(LayoutElementType.COLLECTION_START);
		returnValue.setMetaCollection(collection);
		returnValue.setLabel(collection.getLabel(request));
		returnValue.setView(view);
		returnValue.setFrame(!view.isSection() || view.getMetaMembers().size() > 1);
		return returnValue;
	}
	

	@SuppressWarnings("unchecked")
	private Collection<String> getActionsNameForProperty(View view, MetaProperty p,
			boolean editable) {
		Collection<String> returnValues = new ArrayList<String>();
		for (java.util.Iterator<String> itActions = view.getActionsNamesForProperty(p, editable).iterator(); itActions.hasNext();) {
			returnValues.add((String) itActions.next());
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

	private void addLayoutElement(LayoutElement e) {
		if (elements == null) {
			elements = new ArrayList<LayoutElement>();
		}
		elements.add(e);
		System.out.println(StringUtils.repeat("    ", e.getGroupLevel()) 
				+ e.toString());

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
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

}
