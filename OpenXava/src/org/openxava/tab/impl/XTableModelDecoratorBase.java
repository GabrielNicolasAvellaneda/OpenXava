package org.openxava.tab.impl;

import java.rmi.*;

import javax.ejb.*;
import javax.swing.event.*;

import org.openxava.util.*;


/**
 * Clase base para construir decoradores de {@link IXTableModel}. <p>
 *
 * Al decir decorador nos referimos al patrón GoF <i>Decorator</i>.<br>
 * Por defecto simplemente redirecciona las llamadas al <tt>IXTableModel</tt>
 * enviado en el constructor.<br>
 *
 * @version 00.06.14
 * @author  Javier Paniza
 */

public class XTableModelDecoratorBase
	implements IXTableModel, java.io.Serializable {

	private IXTableModel impl;

	/**
	 * @param aDecorar <tt>IXTableModel</tt> a decorar.
	 * @exception IllegalArgumentException Si <tt>aDecorar == null</tt>.
	 */
	public XTableModelDecoratorBase(IXTableModel aDecorar) {
		Assert.arg(aDecorar);
		impl = aDecorar;
	}
	
	public void addTableModelListener(TableModelListener l) {
		impl.addTableModelListener(l);
	}
	
	public Class getColumnClass(int columnIndex) {
		return impl.getColumnClass(columnIndex);
	}
	
	public int getColumnCount() {
		return impl.getColumnCount();
	}
	
	public String getColumnName(int columnIndex) {
		return impl.getColumnName(columnIndex);
	}
	
	public Object getObjectAt(int rowIndex) throws FinderException {
		return impl.getObjectAt(rowIndex);
	}
	
	public int getRowCount() {
		return impl.getRowCount();
	}
	
	public Object getValueAt(int rowIndex, int columnIndex) {
		return impl.getValueAt(rowIndex, columnIndex);
	}
	
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return impl.isCellEditable(rowIndex, columnIndex);
	}
	
	public void refresh() throws TabException {
		impl.refresh();
	}
	/**
	 * @see org.openxava.tab.impl.IXTableModel
	 */
	public void removeAllRows() {
		impl.removeAllRows();
	}
	
	public void removeTableModelListener(TableModelListener l) {
		impl.removeTableModelListener(l);
	}
	
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		impl.setValueAt(aValue, rowIndex, columnIndex);
	}

	public int getTotalSize() throws RemoteException {
		return impl.getTotalSize();		
	}
	
}
