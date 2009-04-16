package org.openxava.test.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.actions.ViewBaseAction;

/**
 * Create on 14/04/2009 (18:37:04)
 * @autor Ana Andrés
 */
public class OnSelectedIngredientAction extends ViewBaseAction{
	private static Log log = LogFactory.getLog(OnSelectedIngredientAction.class);
	
	private int row;
	private String viewObject;
	private boolean selected;
	
	public void execute() throws Exception {
		int size = getView().getValueInt("selectedIngredientSize");
		size = isSelected() ? size + 1 : size - 1;
		getView().setValue("selectedIngredientSize", new Integer(size));
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public String getViewObject() {
		return viewObject;
	}

	public void setViewObject(String viewObject) {
		this.viewObject = viewObject;
	}

}
