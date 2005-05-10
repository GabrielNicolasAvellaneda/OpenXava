package org.openxava.tab.impl;

import java.util.*;

import org.openxava.converters.*;
import org.openxava.mapping.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */

class TabConverter implements java.io.Serializable {
	
	private Map cmpFields;
	private int index;
	private String propertyName;
	private IConverter converter;
	private IMultipleConverter multipleConverter;

	public TabConverter(String nombrePropiedad, int indice, IConverter conversor)
		throws XavaException {
		this.index = indice;
		this.propertyName = nombrePropiedad;
		this.converter = conversor;
	}
	
	public TabConverter(String nombrePropiedad, int indice, IMultipleConverter conversor, Collection camposCmp, String [] columnas, String tabla)
		throws XavaException {
		this.index = indice;
		this.propertyName = nombrePropiedad;
		this.multipleConverter = conversor;
		Iterator it = camposCmp.iterator();
		this.cmpFields = new HashMap();
		List listaColumnas = Arrays.asList(columnas);				
		while (it.hasNext()) {
			CmpField campo =  (CmpField) it.next();
			int indiceCmp = listaColumnas.indexOf(tabla + "." + campo.getColumn());
			this.cmpFields.put(campo, new Integer(indiceCmp));
		}		 
	}
		
	public Collection getCmpFields() {
		return cmpFields == null?Collections.EMPTY_SET:cmpFields.keySet();	
	}
	
	public int getIndex(CmpField campo) throws ElementNotFoundException {
		if (this.cmpFields == null) {
			throw new ElementNotFoundException("column_multiple_not_found", campo.getColumn()); 
		}
		Integer indice = (Integer) this.cmpFields.get(campo);
		if (indice == null) {
			throw new ElementNotFoundException("column_multiple_not_found", campo.getColumn());
		}
		return indice.intValue();
	}
	
	/**
	 * Returns the calculador.
	 * @return ICalculador
	 */
	public IConverter getConverter() {
		return converter;
	}
	
	public IMultipleConverter getMultipleConverter() {
		return multipleConverter;
	}
	
	public boolean hasMultipleConverter() {
		return multipleConverter != null;
	}

	/**
	 * Returns the indice.
	 * @return int
	 */
	public int getIndex() {
		return index;
	}


	/**
	 * Returns the nombrePropiedad.
	 * @return String
	 */
	public String getPropertyName() {
		return propertyName;
	}

}

