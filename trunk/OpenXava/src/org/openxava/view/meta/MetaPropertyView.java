package org.openxava.view.meta;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.actions.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */
public class MetaPropertyView extends MetaMemberView implements java.io.Serializable {
	
	public final static int NORMAL_LABEL = 0;
	public final static int SMALL_LABEL = 1;
	public final static int NO_LABEL = 2;
		
	private String propertyName;
	private String label;
	private boolean readOnly;
	private String onChangeActionClassName;	
	private int labelFormat;
				
	private Log log = LogFactory.getLog(MetaPropertyView.class);
	
	public String getPropertyName() {
		return propertyName==null?"":propertyName.trim();
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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
			IOnChangePropertyAction action = (IOnChangePropertyAction) o;
			return action;
		}
		catch (XavaException ex) {
			throw ex;
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
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
	public void setLabelFormat(int labelFormat) {
		this.labelFormat = labelFormat;
	}
	
}
