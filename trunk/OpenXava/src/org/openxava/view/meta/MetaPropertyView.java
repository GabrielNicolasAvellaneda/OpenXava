package org.openxava.view.meta;

import java.util.*;

import org.openxava.actions.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */
public class MetaPropertyView implements java.io.Serializable {
	
	public final static int NORMAL_LABEL = 0;
	public final static int SMALL_LABEL = 1;
	public final static int NO_LABEL = 2;
		
	private String propertyName;
	private String label;
	private boolean readOnly;
	private String onChangeActionClassName;
	private Collection actionsNames;
	private int labelFormat;
	
	public void addActionName(String nombreAccion) {
		if (actionsNames == null) actionsNames = new ArrayList();
		actionsNames.add(nombreAccion);
	}
	public Collection getActionsNames() {		
		return actionsNames==null?Collections.EMPTY_LIST:actionsNames;
	}
			
	/**
	 * Returns the nombreReferencia.
	 * @return String
	 */
	public String getPropertyName() {
		return propertyName==null?"":propertyName.trim();
	}

	/**
	 * Sets the nombreReferencia.
	 * @param nombreReferencia The nombreReferencia to set
	 */
	public void setPropertyName(String nombreReferencia) {
		this.propertyName = nombreReferencia;
	}

	/**
	 * Returns the etiqueta.
	 * @return String
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the etiqueta.
	 * @param etiqueta The etiqueta to set
	 */
	public void setLabel(String etiqueta) {
		this.label = etiqueta;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setReadOnly(boolean b) {
		readOnly = b;
	}
	
	public boolean hasOnChangeAction() {
		return !Is.emptyString(getOnChangeActionClassName());
	}
	
	public IOnChangePropertyAction createOnChangeAction() throws XavaException {
		try {
			Object o = Class.forName(getOnChangeActionClassName()).newInstance();
			if (!(o instanceof IOnChangePropertyAction)) {
				throw new XavaException("on_change_action_implements_error", IOnChangePropertyAction.class.getName(), getOnChangeActionClassName());
			}
			IOnChangePropertyAction accion = (IOnChangePropertyAction) o;
			/* para más adelante
			if (tieneMetaPoners()) {
				asignarValoresPropiedades(calculador);
			}	
			*/					
			return accion;
		}
		catch (XavaException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("create_error", getOnChangeActionClassName());
		}		
	}

	public String getOnChangeActionClassName() {
		return onChangeActionClassName;
	}

	public void setOnChangeActionClassName(String string) {
		onChangeActionClassName = string;
	}

	public int getLabelFormat() {
		return labelFormat;
	}
	public void setLabelFormat(int formatoEtiqueta) {
		this.labelFormat = formatoEtiqueta;
	}
}
