package org.openxava.actions;

import org.openxava.controller.meta.*;

/**
 * 
 * @author Javier Paniza
 */

public class CancelDialogAction extends ViewBaseAction implements IChainAction { 
	
	private String nextAction;
		
	public void execute() throws Exception {
		// tmp Actualizar documentación
		for (MetaAction action: getManager().getMetaActions()) {
			if (action.getName().equals("cancel") || 
				action.getName().equals("cancelar") || 
				action.getName().equals("hideDetail")) 
			{
				nextAction = action.getQualifiedName();
			}
		}		
		if (nextAction == null) closeDialog();
	}
	
	public String getNextAction() throws Exception {
		return nextAction;
	}

}
