/**
 * 
 */
package org.openxava.web.layout;

import java.io.Serializable;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.util.Is;
import org.openxava.view.View;

/**
 * Base class for painters.
 * @author Juan Mendoza and Federico Alcantara
 *
 */
public abstract class AbstractBasePainter implements ILayoutPainter, Serializable {
	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(AbstractBasePainter.class);

	private Stack<ILayoutContainerElement> containersStack;
	private Stack<ILayoutRowBeginElement> rowsStack;
	private Stack<ILayoutColumnBeginElement> columnsStack;
	
	private View view;
	private ILayoutViewBeginElement viewElement;
	private PageContext pageContext;
	private LayoutPainterManager painterManager;

	/**
	 * @see org.openxava.web.layout.ILayoutPainter#initialize(org.openxava.view.View, javax.servlet.jsp.PageContext)
	 */
	public void initialize(View view, PageContext pageContext) {
		this.view = view;
		this.pageContext = pageContext;
		if (log.isDebugEnabled()) {
			log.debug(this.getClass().getName());
		}
		pageContext.getResponse().setContentType("text/html");
		setContainersStack(null);
		setRowsStack(null);
	}
	
	public void finalize(View view, PageContext pageContext){
	}

	/**
	 * @see org.openxava.web.layout.ILayoutPainter#setPainterManager(org.openxava.web.layout.LayoutPainterManager)
	 */
	public void setPainterManager(LayoutPainterManager painterManager) {
		this.painterManager = painterManager;
	}

	public LayoutPainterManager getPainterManager() {
		return painterManager;
	}
	
	/**
	 * 
	 * @return Current container.
	 */
	protected ILayoutContainerElement getContainer() {
		ILayoutContainerElement returnValue = null;
		try {
			returnValue = getContainersStack().peek();
		} catch (EmptyStackException e) {
			log.error(e.getMessage(), e);
		}
		return returnValue;
	}
	
	/**
	 * @return Current row.
	 */
	protected ILayoutRowBeginElement getRow() {
		ILayoutRowBeginElement returnValue = null;
		try {
			returnValue = getRowsStack().peek();
		} catch (EmptyStackException e) {
			log.error(e.getMessage(), e);
		}
		return returnValue;
	}
	
	/**
	 * @return Current column.
	 */
	protected ILayoutColumnBeginElement getColumn() {
		ILayoutColumnBeginElement returnValue = null;
		try {
			returnValue = getColumnsStack().peek();
		} catch (EmptyStackException e) {
			log.error(e.getMessage(), e);
		}
		return returnValue;
	}
	
	/**
	 * Sets element as the current container.
	 * @param layoutContainer.
	 */
	protected void setContainer(ILayoutContainerElement layoutElement) {
		getContainersStack().push(layoutElement);
		if ((layoutElement instanceof ILayoutViewBeginElement)) {
			viewElement = (ILayoutViewBeginElement)layoutElement;
		}
	}
	
	/**
	 * Release current container and sets the previous one.
	 */
	protected void unsetContainer() {
		try {
			getContainersStack().pop();
		} catch (EmptyStackException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Sets the elemen as the current column.
	 * @param layoutElement Element representing the current column.
	 */
	protected void setColumn(ILayoutColumnBeginElement layoutElement) {
		getColumnsStack().push(layoutElement);
	}
	
	protected void unsetColumn() {
		try {
			getColumnsStack().pop();
		} catch (EmptyStackException e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * Sets the element as the current row and resets its column count to 0.
	 * @param layoutElement
	 */
	protected void setRow(ILayoutRowBeginElement layoutElement) {
		layoutElement.setRowCurrentColumnsCount(0);
		getRowsStack().push(layoutElement);
	}

	/**
	 * Release current row and sets the previous one.
	 */
	protected void unsetRow() {
		try {
			getRowsStack().pop();
		} catch (EmptyStackException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * It is a stack of processed containers, the last element represents
	 * the current container.
	 * @return The current container stack;
	 */
	protected Stack<ILayoutContainerElement> getContainersStack() {
		if (containersStack == null) {
			containersStack = new Stack<ILayoutContainerElement>();
		}
		return containersStack;
	}

	/**
	 * Sets (or resets) the containers stack.
	 * @param containersStack New container stack to use.
	 */
	protected void setContainersStack(Stack<ILayoutContainerElement> containersStack) {
		this.containersStack = containersStack;
	}

	/**
	 * It is a stack of processed rows, where the last element (at the top of the stack)
	 * represents the current row.
	 * @return instance of row stack.
	 */
	protected Stack<ILayoutRowBeginElement> getRowsStack() {
		if (rowsStack == null) {
			rowsStack = new Stack<ILayoutRowBeginElement>();
		}
		return rowsStack;
	}

	/**
	 * Sets or (resets) the row stack.
	 * @param rowsStack New row stack to use.
	 */
	protected void setRowsStack(Stack<ILayoutRowBeginElement> rowsStack) {
		this.rowsStack = rowsStack;
	}
	
	/**
	 * @return the columnsStack
	 */
	protected Stack<ILayoutColumnBeginElement> getColumnsStack() {
		if (columnsStack == null) {
			columnsStack = new Stack<ILayoutColumnBeginElement>();
		}
		return columnsStack;
	}

	/**
	 * @param columnsStack the columnsStack to set
	 */
	protected void setColumnsStack(Stack<ILayoutColumnBeginElement> columnsStack) {
		this.columnsStack = columnsStack;
	}

	/**
	 * 
	 * @return The maximun number of columns in the view.
	 */
	protected int getMaxColumnsOnView() {
		return viewElement.getMaxContainerColumnsCount();
	}
	
	/**
	 * 
	 * @return The current view.
	 */
	protected View getView(){
		return view;
	}
	
	/**
	 * @return The current page context.
	 */
	protected PageContext getPageContext() {
		return pageContext;
	}
	
	/**
	 * Converts a string of html attributes into map of attributes.
	 * @param attributeString String containing the list of html attributes.
	 * @return A Map of html attributes, never null.
	 */
	protected Map<String, String> parseAttributes(String attributeString) {
		Map<String, String> returnValue = new HashMap<String, String>();
		if (!Is.emptyString(attributeString)) {
			boolean readingAttributeName = true;
			boolean dontAppendToAttributeName = false;
			boolean prepareForReadingAttributeValue = false;
			boolean readingAttributeValue = false;
			boolean acceptNextCharacter = false;
			char attributeValueDelimiter = ' '; 
			
			StringBuffer attributeName = new StringBuffer("");
			StringBuffer attributeValue = new StringBuffer("");
			for (int index = 0; index < attributeString.length(); index++) {
				char charAt = attributeString.charAt(index);
				if (readingAttributeName) {
					if (charAt == '=') {
						readingAttributeName = false;
						prepareForReadingAttributeValue = true;
					} else if (charAt == ' ' && attributeName.length() > 0){
						dontAppendToAttributeName = true;
					} else if (!dontAppendToAttributeName) {
						attributeName.append(charAt);
					}
				} else if (prepareForReadingAttributeValue) {
					if (charAt == '\'' ||
							charAt == '"') {
						attributeValueDelimiter = charAt;
						prepareForReadingAttributeValue = false;
						readingAttributeValue = true;
					}
				} else if (readingAttributeValue) {
					if (acceptNextCharacter) {
						attributeValue.append(charAt);
						acceptNextCharacter = false;
					} else if (charAt == 0x1b) {
						acceptNextCharacter = true;
					} else if (charAt == attributeValueDelimiter) {
						if (attributeName.length() > 0) {
							returnValue.put(attributeName.toString(), attributeValue.toString());
						}
						attributeName = new StringBuffer();
						attributeValue = new StringBuffer();
					}
				}
			}
		}
		return returnValue;
	}
}
