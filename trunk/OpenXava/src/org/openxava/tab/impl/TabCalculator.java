package org.openxava.tab.impl;

import org.openxava.calculators.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;


/**
 *@author Javier Paniza
 */

public class TabCalculator implements java.io.Serializable {
	private int index;
	private String propertyName;
	private MetaCalculator metaCalculator;
	private ICalculator calculator;
	private PropertiesManager propertiesManager;

	public TabCalculator(MetaProperty metaPropiedad, int indicePropiedad)
		throws XavaException {
		this.index = indicePropiedad;
		this.propertyName = metaPropiedad.getQualifiedName();
		this.metaCalculator = metaPropiedad.getMetaCalculator();
		this.calculator = metaCalculator.createCalculator();
		this.propertiesManager = new PropertiesManager(calculator);
	}

	/**
	 * Returns the calculador.
	 * @return ICalculador
	 */
	public ICalculator getCalculator() {
		return calculator;
	}

	/**
	 * Returns the indice.
	 * @return int
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Returns the manejadorPropiedades.
	 * @return ManejadorPropiedades
	 */
	public PropertiesManager getPropertiesManager() {
		return propertiesManager;
	}

	/**
	 * Returns the meta.
	 * @return MetaCalculador
	 */
	public MetaCalculator getMetaCalculator() {
		return metaCalculator;
	}

	/**
	 * Returns the nombrePropiedad.
	 * @return String
	 */
	public String getPropertyName() {
		return propertyName;
	}

}

