package org.openxava.model.meta;

import java.io.*;

import org.openxava.calculators.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;


/**
 * @author Javier Paniza
 */
public class MetaCalculator extends MetaSetsContainer implements Serializable {
	
	private String className;	
	private ICalculator calculator;
	private boolean onCreate;
		
	/**
	 * Crea un calculador cada vez que se llama a este método,
	 * configurado con los valores asignados en xml.
	 * 
	 * @return ICalculador
	 * @throws XavaException
	 */
	public ICalculator createCalculator() throws XavaException {		
		try {
			Object o = Class.forName(getClassName()).newInstance();
			if (!(o instanceof ICalculator)) {
				throw new XavaException("calculator_implements_icalculator", getClassName());
			}
			ICalculator calculador = (ICalculator) o;
			if (containsMetaSets()) {
				assignPropertiesValues(calculador);
			}									
			return calculador;
		}
		catch (XavaException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("create_calculater_error", getClassName(), ex.getLocalizedMessage());
		}
	}
	
	/**
	 * La primera vez crea, y las veces sucesivas devuelve el creado la primera
	 * vez. <p>
	 * 
	 * @return ICalculador
	 */
	public ICalculator getCalculator() throws XavaException {
		if (calculator == null) {
			calculator = createCalculator();
		}
		return calculator;
	}


	
	

	/**
	 * Returns the nombreClase.
	 * @return String
	 */
	public String getClassName() {
		return className;
	}
	
	/**
	 * Sets the nombreClase.
	 * @param nombreClase The nombreClase to set
	 */
	public void setClassName(String nombreClase) {
		this.className = nombreClase;
	}

	/**
	 * Returns the alCrear.
	 * @return boolean
	 */
	public boolean isOnCreate() {
		return onCreate;
	}

	/**
	 * Sets the alCrear.
	 * @param alCrear The alCrear to set
	 */
	public void setOnCreate(boolean alCrear) {
		this.onCreate = alCrear;
	}

}
