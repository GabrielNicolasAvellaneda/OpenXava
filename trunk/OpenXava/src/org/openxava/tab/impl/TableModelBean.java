package org.openxava.tab.impl;

import java.math.*;
import java.rmi.*;
import java.util.*;

import javax.ejb.*;
import javax.swing.event.*;

/**
 * Implementación de un <code>IXTableModel</code> en forma de JavaBean. <p>
 *
 * Es un <code>TableModel</code> de solo lectura.<br>
 * Llamar al método {@link #invariante} antes de empezar a usar.
 *
 * @version 00.03.29
 */

/*
00.01.27  Creación
00.03.29  Se resuelve un bug que hacía que una vez leido todos los datos
		   siguiera solicitando al servidor. Cambios hecho en getValueAt(int, int)
		   refrescar() y añadimos todosCargados
*/

public class TableModelBean implements IXTableModel, java.io.Serializable {
	
	private final static String MENSAJE_INVARIANTE =
		"Invariante TableModelBean incumplida: Las propiedades cabeceras, entidadTab, indicesPK son requeridas";
	private final static int TODAVIA_NO_OBTENIDO = -1;
	private int tamañoTotal = TODAVIA_NO_OBTENIDO;
	private Vector oyentes;
	private IEntityTabImpl entidadTab;
	private String[] clasesColumnas;
	private String[] cabeceras = new String[0];
	private int[] indicesPK = { 0 };
	boolean todosCargados;
	// Ya se han leido todos los datos desde el proveedor de datos

	private Vector datos;
	// rowCount siempre tiene una más de las realmente cargada,
	// así cuando se intenta cargar esa última se piden más. Si
	// ya se han cargado todas rowCount marca la cantidad total
	// que se ha cargado
	private int rowCount;
	private boolean translateHeading = true;

	/** Constructor por defecto. */
	public TableModelBean() {
		refresh();
	}
	// implementa TableModel
	public void addTableModelListener(TableModelListener l) {
		if (oyentes == null)
			oyentes = new Vector();
		oyentes.addElement(l);
	}
	// Comunica a los oyentes que ha cambiado el modelo.
	private void fireModeloCambiado() {
		if (oyentes != null) {
			TableModelEvent ev = new TableModelEvent(this);
			Enumeration e = oyentes.elements();
			while (e.hasMoreElements()) {
				((TableModelListener) e.nextElement()).tableChanged(ev);
			}
		}
	}
	/** Titulillos que se visualizarán en las cabeceras de las columnas. */
	public String[] getCabeceras() {
		return cabeceras;
	}
	/**
	 * Nombre de las clases asociadas a las columnas. <br>
	 * Esto indica la forma de formatear los datos.
	 */
	public String[] getClasesColumnas() {
		return clasesColumnas;
	}
	// implementa TableModel
	public Class getColumnClass(int columnIndex) {
		Class rs = Object.class;
		if (clasesColumnas != null) {
			try {
				rs = Class.forName(clasesColumnas[columnIndex]);
			}
			catch (ClassNotFoundException ex) {
				ex.printStackTrace();
				System.err.println(
					"ADVERTENCIA: No se encuentra la clase para la columna "
						+ columnIndex);
			}
			catch (IndexOutOfBoundsException ex) {
			}
		}
		return rs;
	}
	// implementa TableModel
	public int getColumnCount() {
		return cabeceras.length;
	}
	// implementa TableModel
	public String getColumnName(int columnIndex) {
		String etiqueta = cabeceras[columnIndex];
		if (!isTranslateHeading()) return etiqueta;
		int idx = etiqueta.indexOf('.');
		if (idx < 0) return etiqueta;
		String bundle = etiqueta.substring(0, idx);
		String id = etiqueta.substring(idx+1);
		try {
			return ResourceBundle.getBundle(bundle).getString(id); 
		}
		catch (MissingResourceException ex) {
			System.err.println("¡ADVERTENCIA!: Imposible conseguir traducción para " + id + " en archivo " + bundle);
			return etiqueta;
		}
	}
	
	// Propiedades

	/** De donde se obtienen los datos a visualizar. */
	public IEntityTabImpl getEntityTab() {
		return entidadTab;
	}
	// Ojo, puede devolver nulo, p. ej. si está la lista vacia
	// Se encarga de cargar datos bajo demanda si se pide una fila todavía no cargada
	private Object[] getRow(int rowIndex) throws RemoteException {
		long ini = System.currentTimeMillis();
		try {

			if (!todosCargados
				&& rowIndex >= rowCount - 1) { // Si se pide el última y hay más
				long iniSiguienteTrozo = System.currentTimeMillis();
				DataChunk sig = entidadTab.nextChunk();
				long cuestaSiguienteTrozo =
					System.currentTimeMillis() - iniSiguienteTrozo;
				System.out.println(
					"[TableModelBean.getRow] nextChunk="
						+ cuestaSiguienteTrozo);
				List data = sig.getData();
				Iterator it = data.iterator();
				while (it.hasNext()) {
					datos.addElement(it.next());
				}
				todosCargados = sig.isLast();
				rowCount = todosCargados ? datos.size() : datos.size() + 1;
				fireModeloCambiado();
			}
			if (rowIndex >= rowCount)
				return null; // P. ej. si la lista está vacía
			return (Object[]) datos.elementAt(rowIndex);
		}
		finally {
			long cuesta = System.currentTimeMillis() - ini;
			//System.out.println("[TableModelBean.getFila] " + cuesta);
		}
	}
	/**
	 * Indice de las columnas que continen la clave principal. <br>
	 * La clave principal (Primery Key) se usa para construir
	 * el objeto asociado a la fila.
	 */
	public int[] getPKIndexes() {
		return indicesPK;
	}
	// Implementa IXTableModel
	public Object getObjectAt(int rowIndex) throws FinderException {
		try {
			Object[] clave = new Object[indicesPK.length];
			Object[] fila = getRow(rowIndex);
			if (fila == null) {
				//throw new FinderException("Objeto no encontrado");
				return null;
			}
			for (int i = 0; i < clave.length; i++) {
				clave[i] = fila[indicesPK[i]];
			}
			return entidadTab.findEntity(clave);
		}
		catch (RemoteException ex) {
			throw new FinderException(
				"Imposible conseguir entidad asociada a la fila "
					+ rowIndex
					+ ": "
					+ ex.getLocalizedMessage());
		}
	}
	// Implementa TableModel
	public int getRowCount() {
		return rowCount;
	}
	// implementa TableModel
	public Object getValueAt(int rowIndex, int columnIndex)
		throws NoSuchElementException {
		try {
			Object[] fila = getRow(rowIndex);
			if (fila == null)
				return null;
			return convertir(fila[columnIndex], columnIndex);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return "¡ERROR!: Dato no obtenido";
		}
	}
	private Object convertir(Object object, int columnIndex) {
		if (object == null) return null;
		if (object.getClass().equals(BigDecimal.class)) {
			if (Integer.class.equals(getColumnClass(columnIndex))) {
				return new Integer(((Number) object).intValue());
			}
			if (Long.class.equals(getColumnClass(columnIndex))) {
				return new Long(((Number) object).longValue());
			}						
		}				
		return object;		 
	}
	
	/**
	 * Comprueba la invariante del objeto. <br>
	 * <b>Invariante:</b>
	 * <ul>
	 * <li> cabeceras != null
	 * <li> entidad != null
	 * <li> indicesPK != null
	 * </ul>
	 * @exception IllegalStateException  Si no se cumple la invariante.
	 */
	public void invariant() throws IllegalStateException {
		if (cabeceras != null && entidadTab != null && indicesPK != null)
			throw new IllegalStateException(MENSAJE_INVARIANTE);
	}
	// implementa TableModel
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}
	// Implementa IXTableModel
	public void refresh() {		
		tamañoTotal = TODAVIA_NO_OBTENIDO;
		datos = new Vector(50, 50);
		rowCount = 1;
		todosCargados = false;
		try {
			if (entidadTab != null) {
				entidadTab.reset();
			}
		}
		catch (Exception e) {			
			e.printStackTrace();
			System.out.println("¡ADVERTENCIA!: Ha sido imposible reiniciar el proveedor de datos tabulares");
		}
		fireModeloCambiado();
	}
	/**
	 * @see org.openxava.tab.impl.IXTableModel
	 */
	public void removeAllRows() {
		datos.clear();
		rowCount = 0;
		fireModeloCambiado();
	}
	// implementa TableModel
	public void removeTableModelListener(TableModelListener l) {
		if (oyentes != null)
			oyentes.removeElement(l);
	}
	/** Titulillos que se visualizarán en las cabeceras de las columnas. */
	public void setHeading(String[] cabeceras) {
		this.cabeceras = cabeceras;
	}
	/**
	 * Nombre de las clases asociadas a las columnas. <br>
	 * Esto indica la forma de formatear los datos.
	 */
	public void setColumnsClasses(String[] clasesColumnas) {
		this.clasesColumnas = clasesColumnas;
	}
	/** De donde se obtienen los datos a visualizar. */
	public void setEntityTab(IEntityTabImpl entidadTab) {
		this.entidadTab = entidadTab;
	}
	/**
	 * Indice de las columnas que continen la clave principal. <br>
	 * La clave principal (Primery Key) se usa para construir
	 * el objeto asociado a la fila.
	 */
	public void setPKIndexes(int[] indicesPK) {
		this.indicesPK = indicesPK;
	}
	// implementa TableModel
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// no se hace nada, es solo lectura
	}
	/** Saca <i>TablaModelBean> Filas: x, Columnas: x. */
	public String toString() {
		return "TableModelBean> Filas: "
			+ getRowCount()
			+ ", Columnas: "
			+ getColumnCount();
	}
	
	public int getTotalSize() throws RemoteException {
		if (tamañoTotal == TODAVIA_NO_OBTENIDO) {
			tamañoTotal = entidadTab.getResultSize(); 
		}
		return tamañoTotal; 
	}
	
	/**
	 * Cuando es <tt>true</tt> intenta internacionalizar las cabeceras. <p> 
	 * 
	 * Si encuentra un punto en la etiqueta de la cabecera lo toma como
	 * nombre de archivo de recursos y nombre del id.<br>
	 * Por ejemplo, si la etiqueta es <tt>RecursosMiAplicacion.plazo</tt> buscará
	 * en un archvio llamado <tt>RecursosMiAplicacion</tt> por el identificador
	 * <tt>plazo</tt>.<br>
	 * Si no hay punto toma la etiqueta tal cual.<br>
	 * Por supuesto, si esta propiedad vale false no hace nada de esto.<br>
	 * Por defecto vale <tt>true</tt> 
	 * @return
	 */
	public boolean isTranslateHeading() {
		return translateHeading;
	}

	public void setTranslateHeading(boolean b) {
		translateHeading = b;
	}

}
