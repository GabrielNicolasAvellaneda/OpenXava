package org.openxava.test.actions;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.actions.IModuleContextAction;
import org.openxava.actions.SetDefaultSchemaAction;
import org.openxava.controller.ModuleContext;
import org.openxava.controller.ModuleManager;
import org.openxava.view.View;

/**
 * Create on 03/09/2009 (14:49:04)
 * @autor Ana Andrés
 */
public class SelectSchemaAction extends SetDefaultSchemaAction implements IModuleContextAction{
	private static Log log = LogFactory.getLog(SelectSchemaAction.class);
	
	private View view;
	private ModuleContext context;
	
	public void setContext(ModuleContext context) {
		this.context = context;
	}
	
	public void execute() throws Exception {
		setNewDefaultSchema(getView().getValueString("schema"));
		super.execute();
		
		// restart all modules less SelectSchema
		Collection managers = (Collection) context.getAll("manager");
		Iterator it = managers.iterator();
		while(it.hasNext()){
			ModuleManager manager = (ModuleManager) it.next();
			if (!manager.getModuleName().equalsIgnoreCase("SelectSchema")) manager.reset();
		}
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

}
