/**
 * 
 */
package org.openxava.web.editors;

import org.openxava.model.meta.MetaProperty;
import org.openxava.tab.Tab;

/**
 * Defines the contract for tree view readers.
 * @author Federico Alcantara
 *
 */
public interface ITreeViewReader {
	/**
	 * Initialization of the reader
	 * @param tab
	 */
	void initialize(Tab tab) throws Exception;
	
	/**
	 * Ends the process of reading
	 * @throws Exception
	 */
	void close() throws Exception;
	
	/**
	 * Returns the object representing the column
	 * @param rowIndex index of the row to read
	 * @return
	 */
	Object getObjectAt(int rowIndex) throws Exception;
	
	/**
	 * Returns the number of records (row) to be processed
	 * @return actual row count
	 */
	int getRowCount();
	
	/**
	 * Returns the column count
	 * @return actual column count
	 */
	int getColumnCount();
	
	/**
	 * Returns the column base meta property
	 * @param columnIndex column number to get
	 * @return Associated meta property for the given column number
	 */
	MetaProperty getMetaProperty(int columnIndex) throws Exception;
	
	/**
	 * Retrieves the object value found at row / column
	 * @param row row to be assessed
	 * @param column to be read
	 * @return the object 
	 */
	Object getValueAt(int rowIndex, int columnIndex) throws Exception;
}
