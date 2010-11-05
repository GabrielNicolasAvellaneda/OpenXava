/**
 * 
 */
package org.openxava.web.editors;

import java.util.Map;

import org.openxava.model.MapFacade;
import org.openxava.model.meta.MetaProperty;
import org.openxava.tab.Tab;
import org.openxava.tab.impl.IXTableModel;

/**
 * Implements the Tree view reader
 * @author Federico Alcantara
 *
 */
public class TreeViewReaderImpl implements ITreeViewReader {

	private Tab tab;
	@SuppressWarnings("rawtypes")
	private Map[] allKeys;
	private IXTableModel tableModel;
	
	/**
	 * @see org.openxava.web.editors.ITreeViewReader#initialize(org.openxava.tab.Tab)
	 */
	public void initialize(Tab tab) {
		this.tab = tab;
		tableModel = tab.getTableModel();
		allKeys = tab.getAllKeys();
	}

	/**
	 * @see org.openxava.web.editors.ITreeViewReader#close()
	 */
	public void close() throws Exception {
	}


	/**
	 * @see org.openxava.web.editors.ITreeViewReader#getRowObject()
	 */
	public Object getObjectAt(int rowIndex) throws Exception {
		return MapFacade.findEntity(tab.getModelName(), allKeys[rowIndex]);
	}

	/**
	 * @see org.openxava.web.editors.ITreeViewReader#getRowCount()
	 */
	public int getRowCount() {
		return tableModel.getRowCount();
	}


	/**
	 * @see org.openxava.web.editors.ITreeViewReader#getColumnCount()
	 */
	public int getColumnCount() {
		return tableModel.getColumnCount();
	}

	/**
	 * @see org.openxava.web.editors.ITreeViewReader#getMetaProperty(int)
	 */
	public MetaProperty getMetaProperty(int column) {
		return tab.getMetaProperty(column);
	}

	/**
	 * @see org.openxava.web.editors.ITreeViewReader#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		return tableModel.getValueAt(rowIndex, columnIndex);
	}

}
