package org.openxava.test.actions;

import java.util.*;

import org.openxava.actions.*;
import org.openxava.test.ejb.*;
import org.openxava.test.ejb.*;



/**
 * @author Javier Paniza
 */
public class TranslateCarrierNameAction extends CollectionBaseAction {

	public void execute() throws Exception {		
		Iterator it = getSelectedObjects().iterator();
		while (it.hasNext()) {
			Carrier carrier = (Carrier) it.next();
			carrier.translate();
		}
	}

}
