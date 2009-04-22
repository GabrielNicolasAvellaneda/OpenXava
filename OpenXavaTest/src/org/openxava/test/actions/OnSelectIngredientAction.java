package org.openxava.test.actions;

import org.openxava.actions.*;

/**
 * Create on 14/04/2009 (18:37:04)
 * 
 * @autor Ana Andrés
 */
public class OnSelectIngredientAction extends OnSelectElementBaseAction {
	
	public void execute() throws Exception {		
		int size = getView().getValueInt("selectedIngredientSize");
		size = isSelected() ? size + 1 : size - 1;
		getView().setValue("selectedIngredientSize", new Integer(size));
	}

}
