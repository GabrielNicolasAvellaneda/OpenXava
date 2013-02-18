/**
 * 
 */
package org.openxava.web.layout;

/**
 * Begin marker for row elements.
 * @author Federico Alcantara
 *
 */
public interface ILayoutRowBeginElement extends ILayoutElement {
	/**
	 * 
	 * @return Maximum number of frames within the row.
	 */
	Integer getMaxFramesCount();
	
	/**
	 * Sets the maximum number of frames within the row.
	 * @param maxFramesCount New frames count.
	 */
	void setMaxFramesCount(Integer maxFramesCount);
	
	/**
	 * 
	 * @return Current row index relative to the whole page (without the sections).
	 */
	Integer getRowIndex();
	
	/**
	 * Sets the row index relative to the whole page.
	 * @param rowIndex New row index value.
	 */
	void setRowIndex(Integer rowIndex);
	
	/**
	 * 
	 * @return Maximum number of columns count within the row.
	 */
	Integer getMaxRowColumnsCount();
	
	/**
	 * Sets the maximum number of columns within the row.
	 * @param maxRowColumnsCount Maximum number of columns to set for the row.
	 */
	void setMaxRowColumnsCount(Integer maxRowColumnsCount);
	
	/**
	 * 
	 * @return Current rendered columns count.
	 */
	Integer getRowCurrentColumnsCount();
	
	/**
	 * Sets the current rendered columns.
	 * @param rowCurrentColumnsCount Number of columns already displayed.
	 */
	void setRowCurrentColumnsCount(Integer rowCurrentColumnsCount);
	
	
	/**
	 * 
	 * @return If true indicates that the properties are displayed using free form.
	 * Each row has its own table. This is very similar to original OX renderer.
	 */
	boolean isFreeForm();
	
	/**
	 * Sets the freeForm value.
	 * @param freeForm
	 */
	void setFreeForm(boolean freeForm);
	
}
