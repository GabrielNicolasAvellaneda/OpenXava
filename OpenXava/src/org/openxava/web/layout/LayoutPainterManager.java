/**
 * 
 */
package org.openxava.web.layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.openxava.view.View;

/**
 * Painter manager. Parses and paints views.
 * Views are painted from top to bottom, left to right.
 * @author Federico Alcantara
 *
 */
public class LayoutPainterManager {
	/** 
	 * Render the view.
	 * @param view Originating view.
	 * @param pageContext page context.
	 * @return True if a suitable parser / painter combination is found and used.
	 */
	public boolean renderView(View view, PageContext pageContext) {
		boolean returnValue = false;
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		ILayoutParser parser = 	LayoutFactory.getLayoutParserInstance(request);
		if (parser != null) {
			ILayoutPainter painter = LayoutFactory.getLayoutPainterInstance(
					(HttpServletRequest) pageContext.getRequest());
			if (painter != null) {
				returnValue = true;
				Collection<LayoutElement> elements = parser.parseView(view, pageContext);
				painter.initialize(view, pageContext);
				renderElements(painter, elements, view, pageContext);
				painter.finalize(view, pageContext);
			}
		}
		return returnValue;
	}

	/**
	 * Render a section.
	 * @param view Originating view.
	 * @param pageContext page context.
	 * @return True if a suitable parser / painter combination is found and used.
	 */
	public boolean renderSection(View view, PageContext pageContext) {
		boolean returnValue = false;
		ILayoutPainter painter = LayoutFactory.getLayoutPainterInstance(
				(HttpServletRequest) pageContext.getRequest());
		if (painter != null) {
			returnValue = true;
			painter.initialize(view, pageContext);
			LayoutElement element = new LayoutElement(view, 0);
			element.setElementType(LayoutElementType.SECTIONS_BEGIN);
			element.setSections(true);
			element.setView(view);
			Collection<LayoutElement> elements = new ArrayList<LayoutElement>();
			elements.add(element);
			renderElements(painter, elements, view, pageContext);
			painter.finalize(view, pageContext);
		}
		return returnValue;
	}
	
	/**
	 * Render each element.
	 * @param painter Painter which render the elements.
	 * @param elements Collection of layout elements.
	 */
	public void renderElements(ILayoutPainter painter, Collection<LayoutElement> elements, View view, PageContext pageContext) {
		painter.setPainterManager(this);
		Iterator<LayoutElement> it = elements.iterator();
		while(it.hasNext()) {
			LayoutElement element = (LayoutElement) it.next();
			switch(element.getElementType()) {
				case VIEW_BEGIN: painter.beginView(element); break;
				case VIEW_END: painter.endView(element); break;
				case GROUP_BEGIN: painter.beginGroup(element); break;
				case GROUP_END: painter.endGroup(element); break;
				case FRAME_BEGIN: painter.beginFrame(element); break;
				case FRAME_END: painter.endFrame(element); break;
				case COLLECTION_BEGIN: painter.beginCollection(element); break;
				case COLLECTION_END: painter.endCollection(element); break;
				case SECTIONS_BEGIN: painter.beginSections(element); break;
				case SECTIONS_END: painter.endSections(element); break;
				case ROW_BEGIN: painter.beginRow(element); break;
				case ROW_END: painter.endRow(element); break;
				case COLUMN_BEGIN: painter.beginColumn(element); break;
				case COLUMN_END: painter.endColumn(element); break;
				case PROPERTY_BEGIN: painter.beginProperty(element); break;
				case PROPERTY_END: painter.endProperty(element); break;
			}
		}
	}
	
}
