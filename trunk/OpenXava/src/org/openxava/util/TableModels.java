package org.openxava.util;

import java.io.*;

import javax.swing.table.*;

/**
 * @author Javier Paniza
 */
public class TableModels {
	
	private final static char SEPARATOR = ';';
	
	public static String toCSV(TableModel table) {
		if (table == null) return "";
		StringBuffer cvs = new StringBuffer();
		int columnas = table.getColumnCount(); 
		for (int i=0; i<columnas; i++) {
			cvs.append(table.getColumnName(i));
			if (i < columnas - 1) cvs.append(SEPARATOR);
		}
		cvs.append('\n');
		for (int fila=0; fila < table.getRowCount(); fila++) {
			for (int i=0; i<columnas; i++) {
				cvs.append(table.getValueAt(fila, i));
				if (i < columnas - 1) cvs.append(SEPARATOR);
			}
			cvs.append('\n');			
		}
		cvs.append('\n');
		return cvs.toString();
	}

	public static void saveCSV(TableModel table, String file) throws IOException {
		FileOutputStream ostream = new FileOutputStream(file);				
		ostream.write(toCSV(table).getBytes());				
		ostream.close();		
	}
	
}
