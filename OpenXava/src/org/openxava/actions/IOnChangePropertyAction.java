package org.openxava.actions;

import org.openxava.view.*;

/**
 * @author Javier Paniza
 */

public interface IOnChangePropertyAction extends IAction {
	
	void setChangedProperty(String nombrePropiedad);
	
	void setNewValue(Object valor);
	
	void setView(View vista);

}
