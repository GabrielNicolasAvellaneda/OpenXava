package org.openxava.test.actions;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.actions.IModuleContextAction;
import org.openxava.actions.ViewBaseAction;
import org.openxava.controller.ModuleContext;
import org.openxava.controller.ModuleManager;

/**
 * Create on 10/08/2009 (14:01:01)
 * @autor Ana Andrés
 */
public class InitModulesAction extends ViewBaseAction implements IModuleContextAction{ 
	private static Log log = LogFactory.getLog(InitModulesAction.class);
	
	private ModuleContext context;
	
	public void setContext(ModuleContext context) {
		this.context = context;
	}

	public void execute() throws Exception {
		Collection managers = (Collection) context.getAll("manager");
		Iterator it = managers.iterator();
		while(it.hasNext()){
			ModuleManager manager = (ModuleManager) it.next();
			manager.reset();
		}
	}

}
