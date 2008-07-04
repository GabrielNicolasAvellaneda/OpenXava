package org.openxava.controller;

import javax.persistence.*;

import org.hibernate.validator.*;
import org.openxava.controller.meta.*;
import org.openxava.util.*;
import org.openxava.validators.*;

/**
 * Code used by ModuleManager that only works with Java 5. <p>
 * 
 * @author Javier Paniza
 */

class ModuleManagerJava5 {
	
	static void manageExceptionJava5(ModuleManager manager, MetaAction metaAction, Messages errors, Messages messages, Exception ex) {
		if (ex instanceof ValidationException) {		
			errors.add(((ValidationException)ex).getErrors());
			messages.removeAll();
			manager.doRollback();			
		}
		else if (ex instanceof RollbackException) {
			if (ex.getCause() instanceof InvalidStateException) {
				manageInvalidStateException(metaAction, errors, messages, (InvalidStateException) ex.getCause());				
			}
			else {
				manager.manageException(metaAction, errors, messages, ex);
			}
			manager.doRollback();
		}
		else if (ex instanceof InvalidStateException) {			
			manageInvalidStateException(metaAction, errors, messages, (InvalidStateException) ex);				
			manager.doRollback();
		}				
		else {			
			manager.manageException(metaAction, errors, messages, ex);
			manager.doRollback();			
		}						
	}
	
	static private void manageInvalidStateException(MetaAction metaAction, Messages errors, Messages messages, InvalidStateException ex) {
		InvalidValue [] invalidValues = ex.getInvalidValues();
		for (int i=0; i<invalidValues.length; i++) {
			errors.add("invalid_state", 
					invalidValues[i].getPropertyName(), 
					Classes.getSimpleName(invalidValues[i].getBeanClass()), 
					invalidValues[i].getMessage(), 
					invalidValues[i].getValue());			
		}
		messages.removeAll();		
	}

}
