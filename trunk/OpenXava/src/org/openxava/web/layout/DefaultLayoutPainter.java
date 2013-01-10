/**
 * 
 */
package org.openxava.web.layout;

import static org.openxava.web.layout.LayoutJspKeys.ATTRVAL_STYLE_WIDTH_100P;
import static org.openxava.web.layout.LayoutJspKeys.ATTR_CLASS;
import static org.openxava.web.layout.LayoutJspKeys.ATTR_COLSPAN;
import static org.openxava.web.layout.LayoutJspKeys.ATTR_ID;
import static org.openxava.web.layout.LayoutJspKeys.ATTR_LIST;
import static org.openxava.web.layout.LayoutJspKeys.ATTR_SRC;
import static org.openxava.web.layout.LayoutJspKeys.ATTR_STYLE;
import static org.openxava.web.layout.LayoutJspKeys.TAG_DIV;
import static org.openxava.web.layout.LayoutJspKeys.TAG_IMG;
import static org.openxava.web.layout.LayoutJspKeys.TAG_SPAN;
import static org.openxava.web.layout.LayoutJspKeys.TAG_TABLE;
import static org.openxava.web.layout.LayoutJspKeys.TAG_TD;
import static org.openxava.web.layout.LayoutJspKeys.TAG_TR;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.annotations.LabelFormatType;
import org.openxava.controller.ModuleContext;
import org.openxava.model.meta.MetaReference;
import org.openxava.util.Is;
import org.openxava.view.View;
import org.openxava.view.meta.MetaView;
import org.openxava.web.Ids;
import org.openxava.web.taglib.ActionTag;
import org.openxava.web.taglib.EditorTag;
import org.openxava.web.taglib.LinkTag;

/**
 * Implements a basic Painter.
 * @author Federico Alcantara
 *
 */
public class DefaultLayoutPainter extends AbstractJspPainter {
	private static final Log LOG = LogFactory.getLog(DefaultLayoutPainter.class);
	private boolean firstCellPainted = false;
	private int tdPerColumn = 2; // One TD for the label and another for Data and other cells.
	private int paintedProperties = 0;
	
	/**
	 * @see org.openxava.web.layout.ILayoutPainter#beginView(org.openxava.web.layout.LayoutElement)
	 */
	public void beginView(LayoutElement element) {
		resetLog();
		setContainer(element);
		attributes.clear();
		if (element.isRepresentsSection()) {
			if (LayoutPreferences.getInstance().areSectionFramesMaximized()) {
				attributes.put(ATTR_STYLE, ATTRVAL_STYLE_WIDTH_100P);
			}
		} else {
			if (LayoutPreferences.getInstance().areViewFramesMaximized()) {
				attributes.put(ATTR_STYLE, ATTRVAL_STYLE_WIDTH_100P);
			}
		}
		write(LayoutJspUtils.INSTANCE.startTag(TAG_TABLE, attributes));
	}

	/**
	 * @see org.openxava.web.layout.ILayoutPainter#endView(org.openxava.web.layout.LayoutElement)
	 */
	public void endView(LayoutElement element) {
		level = 0;
		write(LayoutJspUtils.INSTANCE.endTag(TAG_TABLE));
		outputLog();
	}

	/**
	 * This has the same behavior as the startFrame method.
	 * @see org.openxava.web.layout.ILayoutPainter#beginGroup(org.openxava.web.layout.LayoutElement)
	 */
	public void beginGroup(LayoutElement element) {
		beginFrame(element);
	}

	/**
	 * This has the same behavior as the endFrame method.
	 * @see org.openxava.web.layout.ILayoutPainter#endGroup(org.openxava.web.layout.LayoutElement)
	 */
	public void endGroup(LayoutElement element) {
		endFrame(element);
	}

	/**
	 * Creates the frame. This implementation uses the same style as the original OX design.
	 * @see org.openxava.web.layout.ILayoutPainter#beginFrame(org.openxava.web.layout.LayoutElement)
	 */
	public void beginFrame(LayoutElement element) {
		// Frame should occupy as many columns as needed. 
		// In this design each column is 2 TD wide.
		// However if this frame is the only one in the row
		// Takes the full size of the view.
		Integer columnSpan = 0;
		boolean maximizeTable = true;
		int count = getRow().getRowCurrentColumnsCount() + 1;
		if (getRow().getMaxFramesCount() == getRow().getMaxRowColumnsCount() &&
				getRow().getMaxFramesCount() == 1 &&
				element.getGroupLevel() <= 3) { // A single frame topmost
			columnSpan = getMaxColumnsOnView();
			count = getMaxColumnsOnView();
		}
		columnSpan = columnSpan * tdPerColumn;
		
		getRow().setRowCurrentColumnsCount(count);
		
		attributes.clear();
		if (columnSpan > 0) {
			attributes.put(ATTR_COLSPAN, columnSpan.toString());
		}
		write(LayoutJspUtils.INSTANCE.startTag(TAG_TD, attributes));
		
		write(getStyle().getFrameHeaderStartDecoration(maximizeTable ? 100 : 0));
			write(getStyle().getFrameTitleStartDecoration());
				write(element.getLabel());
			write(getStyle().getFrameTitleEndDecoration());
			write(getStyle().getFrameActionsStartDecoration());
				String frameId = Ids.decorate(getRequest(), "frame_group_" + getView().getPropertyPrefix() + element.getName());
				String frameActionsURL = "frameActions.jsp?frameId=" + frameId + 
					"&closed=" + getView().isFrameClosed(frameId);
				includeJspPage(frameActionsURL);
			write(getStyle().getFrameActionsEndDecoration());
		write(getStyle().getFrameHeaderEndDecoration());
		write(getStyle().getFrameContentStartDecoration(frameId + "content", getView().isFrameClosed(frameId)));
		// Start the property container
		attributes.clear();
		attributes.put(ATTR_STYLE, ATTRVAL_STYLE_WIDTH_100P);
		write(LayoutJspUtils.INSTANCE.startTag(TAG_TABLE, attributes));
		setContainer(element);
	}

	/**
	 * @see org.openxava.web.layout.ILayoutPainter#endFrame(org.openxava.web.layout.LayoutElement)
	 */
	public void endFrame(LayoutElement element) {
		write(LayoutJspUtils.INSTANCE.endTag(TAG_TABLE));
		write(getStyle().getFrameContentEndDecoration());
		write(LayoutJspUtils.INSTANCE.endTag(TAG_TD));
		createTdColumnSpan();
		unsetContainer();
	}

	/**
	 * Actually starts a row where all the columns are painted.
	 * @see org.openxava.web.layout.ILayoutPainter#beginRow(org.openxava.web.layout.LayoutElement)
	 */
	public void beginRow(LayoutElement element) {
		setRow(element);
		if (element.getMaxRowColumnsCount() > 0) {
			attributes.clear();
			if (getRow().getMaxFramesCount() > 1) {
				attributes.put("valign", "top");
			}
			write(LayoutJspUtils.INSTANCE.startTag(TAG_TR, attributes));
			if (element.getMaxFramesCount() > 0) {
				attributes.clear();
				Integer columnSpan = getMaxColumnsOnView() * tdPerColumn;
				attributes.put(ATTR_COLSPAN, columnSpan.toString());
				write(LayoutJspUtils.INSTANCE.startTag(TAG_TD, attributes));
				
				attributes.clear();
				attributes.put(ATTR_STYLE, ATTRVAL_STYLE_WIDTH_100P);
				write(LayoutJspUtils.INSTANCE.startTag(TAG_TABLE, attributes));
				attributes.clear();
				attributes.put("valign", "top");
				write(LayoutJspUtils.INSTANCE.startTag(TAG_TR, attributes));
			}
		}
	}

	/**
	 * @see org.openxava.web.layout.ILayoutPainter#endRow(org.openxava.web.layout.LayoutElement)
	 */
	public void endRow(LayoutElement element) {
		if (getRow().getMaxRowColumnsCount() > 0 ) {
			createTdColumnSpan();
			if (getRow().getMaxFramesCount() > 0) {
				write(LayoutJspUtils.INSTANCE.endTag(TAG_TR));
				write(LayoutJspUtils.INSTANCE.endTag(TAG_TABLE));
				write(LayoutJspUtils.INSTANCE.endTag(TAG_TD));
			}
			write(LayoutJspUtils.INSTANCE.endTag(TAG_TR));
			createRowSpacer();
		}
		unsetRow();
	}
	
	protected void createRowSpacer() {
		attributes.clear();
		attributes.put(ATTR_CLASS, getStyle().getLayoutRowSpacer());
		write(LayoutJspUtils.INSTANCE.startTag(TAG_TR, attributes));
		for (int count = 0; count < paintedProperties; count++) {
			// Label content
			attributes.clear();
			if (!Is.emptyString(LayoutPreferences.getInstance().getLabelStyle())) {
				attributes.put(ATTR_STYLE, LayoutPreferences.getInstance().getLabelStyle());
			}
			write(LayoutJspUtils.INSTANCE.startTag(TAG_TD, attributes));
			write(LayoutJspUtils.INSTANCE.endTag(TAG_TD));
			// Data content
			attributes.clear();
			if (!Is.emptyString(LayoutPreferences.getInstance().getDataStyle())) {
				attributes.put(ATTR_STYLE, LayoutPreferences.getInstance().getDataStyle());
			}
			write(LayoutJspUtils.INSTANCE.startTag(TAG_TD, attributes));
			write(LayoutJspUtils.INSTANCE.endTag(TAG_TD));
		}
		Integer columnSpan = (getMaxColumnsOnView() - paintedProperties) * tdPerColumn;
		attributes.clear();
		attributes.put(ATTR_COLSPAN, columnSpan.toString());
		if (!Is.emptyString(LayoutPreferences.getInstance().getDataStyle())) {
			attributes.put(ATTR_STYLE, LayoutPreferences.getInstance().getDataStyle());
		}
		write(LayoutJspUtils.INSTANCE.startTag(TAG_TD, attributes));
		write(LayoutJspUtils.INSTANCE.endTag(TAG_TD));
		write(LayoutJspUtils.INSTANCE.endTag(TAG_TR));
		paintedProperties = 0;
	}
	
	/**
	 * Creates the necessary td for column span
	 */
	protected void createTdColumnSpan() {
		createTdColumnSpan(getRow().getRowCurrentColumnsCount(), LayoutJspKeys.CHAR_SPACE);
	}

	/**
	 * Creates the necessary td for column span
	 * @param currentColumnsCount Number of columns so far displayed
	 */
	protected void createTdColumnSpan(int currentColumnsCount, String spacer) {
		// Separation line
		int maxColumnsCount = getMaxColumnsOnView();
		if (maxColumnsCount > currentColumnsCount) {
			Integer lastColumnSpan = maxColumnsCount - currentColumnsCount;
			lastColumnSpan = lastColumnSpan * tdPerColumn; // Each column has 2 TD elements.

			if (lastColumnSpan > 0) {
				attributes.clear();
				attributes.put(ATTR_COLSPAN, lastColumnSpan.toString());
				write(LayoutJspUtils.INSTANCE.startTag(TAG_TD, attributes));
				write(LayoutJspUtils.INSTANCE.endTag(TAG_TD));
			}
		}
	}

	
	/**
	 * Each column does not open a table element (TD), this is done by the 
	 * startCell method. However, each column can contain more than one cell,
	 * but only three TD elements are allowed in the column, so the first cell
	 * creates one TD for the left spacer, another TD for the label and 
	 * a final TD for the data and any other remaining cell of the column.
	 * By parsing contiguous cells without labels are considered as part of the column.<br />
	 * In this implementation columns are composed of three TD.
	 * @see org.openxava.web.layout.ILayoutPainter#beginColumn(org.openxava.web.layout.LayoutElement)
	 */
	public void beginColumn(LayoutElement element) {
		int count = getRow().getRowCurrentColumnsCount() + 1;
		getRow().setRowCurrentColumnsCount(count);
		firstCellPainted = false; // to indicate to the cell renderer that the TD pair is about to start.
	}

	/**
	 * In this painter implementation the column does end the last TD. So the cell
	 * implementation must start the first TD but NOT close the last one
	 * @see org.openxava.web.layout.ILayoutPainter#endColumn(org.openxava.web.layout.LayoutElement)
	 */
	public void endColumn(LayoutElement element) {
		write(LayoutJspUtils.INSTANCE.endTag(TAG_TD));
	}

	/**
	 * @see org.openxava.web.layout.ILayoutPainter#beginProperty(org.openxava.web.layout.LayoutElement)
	 */
	public void beginProperty(LayoutElement element) {
		Integer width =  getMaxColumnsOnView() > 0 ? 50 / getMaxColumnsOnView() : 0;
		width = 0;
		paintedProperties++;
		if (!firstCellPainted) {
			attributes.clear();
			attributes.put(ATTR_CLASS, getStyle().getLabel() + " " + getStyle().getLayoutLabelCell());
			if (width > 0) {
				attributes.put(ATTR_STYLE, "width:" + width.toString() + "%");
			}
			attributes.put("valign", "center");
			if (!Is.emptyString(LayoutPreferences.getInstance().getLabelStyle())) {
				attributes.put(ATTR_STYLE, LayoutPreferences.getInstance().getLabelStyle());
			}
			write(LayoutJspUtils.INSTANCE.startTag(TAG_TD, attributes));
		}
		
		// Left spacer
		beginPropertySpacer(element, getStyle().getLayoutLabelLeftSpacer());
		
		// Label
		beginPropertyLabel(element);
		
		// Left spacer
		beginPropertySpacer(element, getStyle().getLayoutLabelRightSpacer());
		
		if (!firstCellPainted) {
			write(LayoutJspUtils.INSTANCE.endTag(TAG_TD));
		}

		// Data. There is no end TD tag this one is closed by the end column method.
		if (!firstCellPainted) {
			attributes.clear();
			attributes.put(ATTR_CLASS, getStyle().getLayoutDataCell());
			StringBuffer style = new StringBuffer("");
			if (width > 0) {
				style.append("width:" + width.toString() + "%");
			}
			if (!Is.emptyString(LayoutPreferences.getInstance().getDataStyle())) {
				style.append(';')
						.append(LayoutPreferences.getInstance().getDataStyle());
			}
			if (style.length() > 0) {
				attributes.put(ATTR_STYLE, style.toString());
			}
			attributes.put("valign", "center");
			startTd();
		}
		if (element.isDisplayAsDescriptionsList()) {
			beginPropertyDescriptionList(element);
		} else {
			beginPropertyData(element);
		}
		
		// Mark first cell painted
		firstCellPainted = true;
	}
	
	/**
	 * Starts a TD properly (with column span if needed).
	 */
	private void startTd() {
		if (getRow().getMaxRowColumnsCount() == getRow().getRowCurrentColumnsCount()) { // last column
			Integer columnSpan = getMaxColumnsOnView() - getRow().getRowCurrentColumnsCount();
			if (columnSpan > 0) {
				columnSpan = columnSpan * tdPerColumn;
				columnSpan = columnSpan + 1;
				attributes.put(ATTR_COLSPAN, columnSpan.toString());
				getRow().setRowCurrentColumnsCount(getMaxColumnsOnView()); // No td to add.
			}
		}
		write(LayoutJspUtils.INSTANCE.startTag(TAG_TD, attributes));
	}

	/**
	 * Paints the cell left spacer.
	 * @param element Representing cell element.
	 */
	protected void beginPropertySpacer(LayoutElement element, String classType) {
		attributes.clear();
		attributes.put(ATTR_CLASS, classType);
		attributes.put(ATTR_SRC, getRequest().getContextPath() + "/xava/images/spacer.gif");
		write(LayoutJspUtils.INSTANCE.startTag(TAG_IMG, attributes));
		write(LayoutJspUtils.INSTANCE.endTag(TAG_IMG));
	}
	
	/**
	 * Paints the cell label.
	 * @param element Representing cell element.
	 */
	protected void beginPropertyLabel(LayoutElement element) {
		attributes.clear();
		attributes.put(ATTR_CLASS, getStyle().getLayoutLabel());
		attributes.put(ATTR_ID, Ids.decorate(getRequest(), "label_" + element.getPropertyPrefix() 
				+ element.getName()));
		write(LayoutJspUtils.INSTANCE.startTag(TAG_SPAN, attributes));
		String label = element.getLabelFormat() != LabelFormatType.NO_LABEL.ordinal() &&
				element.getLabel() != null ? element.getLabel() + LayoutJspKeys.CHAR_SPACE : LayoutJspKeys.CHAR_SPACE;
		label = label.replaceAll(" ", LayoutJspKeys.CHAR_SPACE);
		write(label);
		String img = "";
		if (!element.isDisplayAsDescriptionsList()) {
			if (element.isKey()) {
				img = "key.gif";
			} else if (element.isRequired()) {
				if (element.isEditable()) { // No need to mark it as required, since the user can not change it anyway
					img = "required.gif";
				}
			}
		} else if (element.isRequired()) {
			img = "required.gif";
		}
		if (!Is.emptyString(img)) {
			attributes.clear();
			attributes.put(ATTR_SRC, getRequest().getContextPath() + "/xava/images/" + img);
			write(LayoutJspUtils.INSTANCE.startTag(TAG_IMG, attributes));
			write(LayoutJspUtils.INSTANCE.endTag(TAG_IMG));
		}
		write(LayoutJspUtils.INSTANCE.endTag(TAG_SPAN));
		attributes.clear();
		attributes.put(ATTR_ID, Ids.decorate(getRequest(), "error_image_" + element.getQualifiedName()));
		write(LayoutJspUtils.INSTANCE.startTag(TAG_SPAN, attributes));
			if (getErrors().memberHas(element.getMetaMember())) {
				attributes.clear();
				attributes.put(ATTR_SRC, getRequest().getContextPath() + "/xava/images/error.gif");
				write(LayoutJspUtils.INSTANCE.startTag(TAG_IMG, attributes));
				write(LayoutJspUtils.INSTANCE.endTag(TAG_IMG));
			}
		write(LayoutJspUtils.INSTANCE.endTag(TAG_SPAN));
	}
	
	/**
	 * Paints the input controls.
	 * @param element Element to be painted.
	 */
	@SuppressWarnings("rawtypes")
	protected void beginPropertyData(LayoutElement element) {
		attributes.clear();
		attributes.put(ATTR_CLASS, getStyle().getLayoutData());
		attributes.put(ATTR_ID, Ids.decorate(getRequest(), "editor_" + element.getPropertyPrefix() + element.getMetaProperty().getName()));
		write(LayoutJspUtils.INSTANCE.startTag(TAG_SPAN, attributes));
		EditorTag editorTag = new EditorTag();
		editorTag.setProperty(element.getPropertyPrefix() + element.getMetaProperty().getName());
		editorTag.setEditable(element.isEditable());
		if (element.isEditable()) {
			if (element.getMetaProperty().isKey()) {
				editorTag.setEditable(element.getView().isKeyEditable());
			}
			if (element.isLastSearchKey() && element.isSearch()) {
				editorTag.setEditable(element.isEditable());
			}
		}
		editorTag.setPageContext(getPageContext());
		editorTag.setThrowPropertyChanged(element.isThrowPropertyChanged());
		try {
			editorTag.doStartTag();
		} catch (JspException e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		write(LayoutJspUtils.INSTANCE.endTag(TAG_SPAN));
		String propertyPrefix = element.getPropertyPrefix() == null ? "" : element.getPropertyPrefix();
		ActionTag actionTag;
		try {
			if (element.hasActions()) {
				attributes.clear();
				attributes.put("id", Ids.decorate(getRequest(), "property_actions_" + propertyPrefix + element.getMetaProperty().getName()));
				write(LayoutJspUtils.INSTANCE.startTag(TAG_SPAN, attributes));
				if (element.isLastSearchKey()) {
					if (element.isSearch() && element.isEditable()) {
						actionTag = new ActionTag();
						actionTag.setAction(element.getSearchAction());
						actionTag.setArgv("keyProperty="+Ids.undecorate(element.getPropertyKey()));
						actionTag.setPageContext(getPageContext());
						actionTag.doStartTag();
					}
					if (element.isCreateNew() && element.isEditable()) {
						actionTag = new ActionTag();
						actionTag.setAction("Reference.createNew");
						actionTag.setArgv("model=" + 
							element.getMetaProperty().getMetaModel().getName() + ",keyProperty="+Ids.undecorate(element.getPropertyKey()));
						actionTag.setPageContext(getPageContext());
						actionTag.doStartTag();
					}
					if (element.isModify() && element.isEditable()) {
						actionTag = new ActionTag();
						actionTag.setAction("Reference.modify");
						actionTag.setArgv("model=" + 
								element.getMetaProperty().getMetaModel().getName() + ",keyProperty="+Ids.undecorate(element.getPropertyKey()));
						actionTag.setPageContext(getPageContext());
						actionTag.doStartTag();
					}
				}
				if (element.isEditable() && element.getActionsNameForReference() != null
						&& element.getActionsNameForReference().size() > 0) {
					Iterator it = element.getActionsNameForReference().iterator();
					while(it.hasNext()) {
						String action = (String) it.next();
						actionTag = new ActionTag();
						actionTag.setAction(action);
						actionTag.setPageContext(getPageContext());
						actionTag.doStartTag();
						actionTag.doEndTag();
					}
				}
				if (element.getActionsNameForProperty() != null 
						&& element.getActionsNameForProperty().size() > 0) {
					Iterator it = element.getActionsNameForProperty().iterator();
					while(it.hasNext()) {
						String action = (String) it.next();
						actionTag = new ActionTag();
						actionTag.setAction(action);
						actionTag.setArgv("xava.keyProperty=" + Ids.undecorate(element.getPropertyKey()));
						actionTag.setPageContext(getPageContext());
						actionTag.doStartTag();
						actionTag.doEndTag();
					}
				}
				write(LayoutJspUtils.INSTANCE.endTag(TAG_SPAN));
			}
		} catch (JspException e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Display element as description list.
	 * @param element Element to be displayed.
	 */
	private void beginPropertyDescriptionList(LayoutElement element) {
		View view = element.getView().getParent() != null ? element.getView().getParent() : element.getView();
		ModuleContext context = (ModuleContext) getPageContext().getSession().getAttribute("context");
		MetaReference metaReference = getView().getMetaReference(element.getReferenceForDescriptionsList());
		String referenceKey = Ids.decorate(getRequest(), element.getReferenceForDescriptionsList());
		context.put(getRequest(), view.getViewObject(), view);
		getRequest().setAttribute(referenceKey, metaReference);
		String editorURL = "reference.jsp"
				+ "?referenceKey=" + referenceKey 
				+ "&onlyEditor=true"
				+ "&frame=false"
				+ "&composite=false"
				+ "&descriptionsList=true"
				+ "&viewObject=" + view.getViewObject(); 
		includeJspPage(editorURL);
	}
	
	/**
	 * In this implementation nothing is done at cell end.
	 * @see org.openxava.web.layout.ILayoutPainter#endProperty(org.openxava.web.layout.LayoutElement)
	 */
	public void endProperty(LayoutElement element) {

	}

	/**
	 * Besides the frame handling, it lets the collection.jsp to take care of the collection rendering.
	 * @see org.openxava.web.layout.ILayoutPainter#beginCollection(org.openxava.web.layout.LayoutElement)
	 */
	public void beginCollection(LayoutElement element) {
		String propertyPrefix = element.getView().getPropertyPrefix();
		String collectionPrefix = propertyPrefix == null ? element.getMetaCollection().getName() + "." :
				propertyPrefix + element.getMetaCollection().getName() + ".";
		String collectionId = Ids.decorate(getRequest(), "collection_" + collectionPrefix);
		ModuleContext context = (ModuleContext) getPageContext().getSession().getAttribute("context");
		context.put(getRequest(), element.getView().getViewObject(), element.getView());
		attributes.clear();
		startTd();
		if (element.hasFrame()) {
			write(getStyle().getFrameHeaderStartDecoration(100));
			write(getStyle().getFrameTitleStartDecoration());
			write(element.getLabel());
			String frameId = Ids.decorate(getRequest(), "frame_" + element.getView().getPropertyPrefix() + element.getMetaCollection().getName());
			String collectionHeaderId = frameId + "header";
			attributes.clear();
			attributes.put("id", collectionHeaderId);
			write(LayoutJspUtils.INSTANCE.startTag(TAG_SPAN, attributes));
			includeJspPage("collectionFrameHeader.jsp" 
				+ "?collectionName=" + element.getMetaCollection().getName()
				+ "&viewObject=" + element.getView().getViewObject()
				+ "&propertyPrefix=" + propertyPrefix);
			write(LayoutJspUtils.INSTANCE.endTag(TAG_SPAN));
			write(getStyle().getFrameTitleEndDecoration());
			write(getStyle().getFrameActionsStartDecoration());
			String frameActionsURL = "frameActions.jsp?frameId=" + frameId +
					"&closed=" + element.getView().isFrameClosed(frameId);
			includeJspPage(frameActionsURL);
			write(getStyle().getFrameActionsEndDecoration());
			write(getStyle().getFrameHeaderEndDecoration());
			write(getStyle().getFrameContentStartDecoration(frameId + "content", element.getView().isFrameClosed(frameId)));
		}
		attributes.clear();
		attributes.put("id", collectionId);
		write(LayoutJspUtils.INSTANCE.startTag(TAG_DIV, attributes));
			includeJspPage("collection.jsp"
					+ "?collectionName=" + element.getMetaCollection().getName() 
					+ "&viewObject=" + element.getView().getViewObject()
					+ "&propertyPrefix=" + propertyPrefix);
		write(LayoutJspUtils.INSTANCE.endTag(TAG_DIV));
		if (element.hasFrame()) {
			write(getStyle().getFrameContentEndDecoration());
		}
		write(LayoutJspUtils.INSTANCE.endTag(TAG_TD));
	}

	/**
	 * Actually all code is performed in the startCollection method.
	 * However for future implementations this might be useful for adding
	 * features to the collection that might be painted at end of the collection
	 * renderization.
	 * @see org.openxava.web.layout.ILayoutPainter#endCollection(org.openxava.web.layout.LayoutElement)
	 */
	public void endCollection(LayoutElement element) {

	}

	/**
	 * Each section behave as a marker. This is called upon section change or page reload. 
	 * @see org.openxava.web.layout.ILayoutPainter#beginSections(org.openxava.web.layout.LayoutElement)
	 */
	@SuppressWarnings("rawtypes")
	public void beginSections(LayoutElement element) {
		View view = getView().hasSections() ? getView() : element.getView();
		Collection sections = view.getSections();
		int activeSection = view.getActiveSection();
		getView().setActiveSection(activeSection); // as getView() and view might be different.

		attributes.clear();
		attributes.put(ATTR_ID, Ids.decorate(getRequest(), "sections_" + view.getViewObject()));
		write(LayoutJspUtils.INSTANCE.startTag(TAG_DIV, attributes));
			attributes.clear();
			attributes.put(ATTR_STYLE, ATTRVAL_STYLE_WIDTH_100P);
			write(LayoutJspUtils.INSTANCE.startTag(TAG_TABLE, attributes));
				write(LayoutJspUtils.INSTANCE.startTag(TAG_TR));
					write(LayoutJspUtils.INSTANCE.startTag(TAG_TD));
						
						attributes.clear();
						attributes.put(ATTR_CLASS, getStyle().getSection());
						write(LayoutJspUtils.INSTANCE.startTag(TAG_DIV, attributes));
							
							attributes.clear();
							attributes.put(ATTR_LIST, getStyle().getSectionTableAttributes());
							write(LayoutJspUtils.INSTANCE.startTag(TAG_TABLE, attributes));
								write(LayoutJspUtils.INSTANCE.startTag(TAG_TR));
									
									write(getStyle().getSectionBarStartDecoration());
									// Loop to paint section(s)
									Iterator itSections = sections.iterator();
									int i = 0;
									while(itSections.hasNext()) {
										MetaView section = (MetaView) itSections.next();
										if (activeSection == i) {
											write(getStyle().getActiveSectionTabStartDecoration(i == 0, !itSections.hasNext()));
											write(section.getLabel(getRequest()));
											write(getStyle().getActiveSectionTabEndDecoration());
										} else {
											try {
												write(getStyle().getSectionTabStartDecoration(i == 0, !itSections.hasNext()));
												String viewObjectArgv = "xava_view".equals(view.getViewObject())?"":",viewObject=" + view.getViewObject();
												LinkTag linkTag = new LinkTag();
												linkTag.setAction("Sections.change");
												linkTag.setArgv("activeSection=" + i + viewObjectArgv);
												linkTag.setCssClass(getStyle().getSectionLink());
												linkTag.setCssStyle(getStyle().getSectionLinkStyle());
												linkTag.setPageContext(getPageContext());
												linkTag.doStartTag();
												write(section.getLabel(getRequest()));
												linkTag.doAfterBody();
												linkTag.doEndTag();
												write(getStyle().getSectionTabEndDecoration());
											} catch (JspException e) {
												LOG.error(e.getMessage(), e);
												throw new RuntimeException(e);
											}
										}
										i++;
									}
									write(getStyle().getSectionBarEndDecoration());
								write(LayoutJspUtils.INSTANCE.endTag(TAG_TR));
							write(LayoutJspUtils.INSTANCE.endTag(TAG_TABLE));
						write(LayoutJspUtils.INSTANCE.endTag(TAG_DIV));
					write(LayoutJspUtils.INSTANCE.endTag(TAG_TD));
				write(LayoutJspUtils.INSTANCE.endTag(TAG_TR));
				write(LayoutJspUtils.INSTANCE.startTag(TAG_TR));
					attributes.clear();
					attributes.put(ATTR_CLASS, getStyle().getActiveSection());
					write(LayoutJspUtils.INSTANCE.startTag(TAG_TD, attributes));
				
						String viewName = getView().getViewObject();
						String viewObject = view.getViewObject() + "_section" + activeSection;
						ModuleContext context = (ModuleContext) getPageContext().getSession().getAttribute("context");
						View currentView = (View) context.get(getRequest(), viewName);
						context.put(getRequest(), viewName, view.getSectionView(activeSection));
						context.put(getRequest(), viewObject, view.getSectionView(activeSection));
						view.getSectionView(activeSection).setViewObject(viewObject);
						// This actually prepares the section content by calling the layout manager with 
						// the current section marker.
						view.getSectionView(activeSection).setPropertyPrefix("");
						List<LayoutElement> elementsSection = 
								LayoutFactory.getLayoutParserInstance(getRequest()).parseView(view.getSectionView(activeSection), getPageContext(), true);
						attributes.clear();
						// only resize to max if view contains frames.
						if (elementsSection.size() > 1 &&
								elementsSection.get(1).getMaxFramesCount() > 0 && ((
								elementsSection.get(1).getElementType().equals(LayoutElementType.ROW_BEGIN)) ||
								LayoutPreferences.getInstance().areSectionFramesMaximized())) {
							attributes.put(ATTR_STYLE, ATTRVAL_STYLE_WIDTH_100P);
						}
						write(LayoutJspUtils.INSTANCE.startTag(TAG_TABLE, attributes));
							write(LayoutJspUtils.INSTANCE.startTag(TAG_TR));
								write(LayoutJspUtils.INSTANCE.startTag(TAG_TD));
									// And paint it.
									view.getSectionView(activeSection).setPropertyPrefix(view.getPropertyPrefix());
									getPainterManager().renderElements(this, elementsSection, view.getSectionView(activeSection), getPageContext());
									context.put(getRequest(), viewName, currentView);
								write(LayoutJspUtils.INSTANCE.endTag(TAG_TD));
							write(LayoutJspUtils.INSTANCE.endTag(TAG_TR));
						write(LayoutJspUtils.INSTANCE.endTag(TAG_TABLE));
					write(LayoutJspUtils.INSTANCE.endTag(TAG_TD));
				write(LayoutJspUtils.INSTANCE.endTag(TAG_TR));
			write(LayoutJspUtils.INSTANCE.endTag(TAG_TABLE));
		write(LayoutJspUtils.INSTANCE.endTag(TAG_DIV));
	}

	/**
	 * @see org.openxava.web.layout.ILayoutPainter#endSections(org.openxava.web.layout.LayoutElement)
	 */
	public void endSections(LayoutElement element) {
		
	}
}
