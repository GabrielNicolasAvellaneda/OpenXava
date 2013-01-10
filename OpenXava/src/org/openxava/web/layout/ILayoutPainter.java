/**
 * 
 */
package org.openxava.web.layout;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.openxava.view.View;

/**
 * Interface contract for a painter.
 * @author Federico Alcantara
 *
 */
public interface ILayoutPainter {
	/**
	 * Starts to paint form.
	 * @param view Originating view.
	 * @param request Originating request.
	 * @param pageContext Page within the form is going to be painted.
	 * @param errors Errors container.
	 * @throws IOException 
	 * @throws JspException 
	 */
	public void initialize(View view, PageContext pageContext);
	public void finalize(View view, PageContext pageContext);
	
	public void setPainterManager(LayoutPainterManager painterManager);
	
	public void beginView(LayoutElement element);
	public void endView(LayoutElement element);
	
	public void beginGroup(LayoutElement element);
	public void endGroup(LayoutElement element);

	public void beginFrame(LayoutElement element);
	public void endFrame(LayoutElement element);
	
	public void beginRow(LayoutElement element);
	public void endRow(LayoutElement element);
	
	public void beginColumn(LayoutElement element);
	public void endColumn(LayoutElement element);
	
	public void beginProperty(LayoutElement element);
	public void endProperty(LayoutElement element);
	
	public void beginCollection(LayoutElement element);
	public void endCollection(LayoutElement element);
	
	public void beginSections(LayoutElement element);
	public void endSections(LayoutElement element);

}
