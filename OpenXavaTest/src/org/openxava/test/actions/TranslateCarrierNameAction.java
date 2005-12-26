package org.openxava.test.actions;

import java.rmi.*;
import java.util.*;

import org.openxava.actions.*;
import org.openxava.model.impl.*;
import org.openxava.test.model.*;
import org.openxava.util.*;



/**
 * @author Javier Paniza
 */
public class TranslateCarrierNameAction extends CollectionBaseAction {

	public void execute() throws Exception {
		Iterator it = getSelectedObjects().iterator();
		while (it.hasNext()) {
			ICarrier carrier = (ICarrier) it.next();
			carrier.translate();
		}		
	}
	
}
