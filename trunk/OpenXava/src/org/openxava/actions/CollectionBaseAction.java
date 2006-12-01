package org.openxava.actions;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openxava.model.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */

abstract public class CollectionBaseAction extends CollectionElementViewBaseAction {

	private List mapValues = null;
	private List mapsSelectedValues;
	private List objects;
	private List selectedObjects;
	private Log log = LogFactory.getLog(CollectionBaseAction.class);

	protected List getMapValues() throws XavaException {
		if (mapValues == null) {
			mapValues = getCollectionElementView().getCollectionValues();
		}
		return mapValues;
	}
	
	protected List getMapsSelectedValues() throws XavaException {
		if (mapsSelectedValues == null) {
			mapsSelectedValues = new ArrayList();
			List values = getCollectionElementView().getCollectionValues();
			int [] sel = getCollectionElementView().getListSelected();
			for (int i=0; i<sel.length; i++) {
				Map val = (Map) values.get(sel[i]);
				mapsSelectedValues.add(val);
			}		
		}
		return mapsSelectedValues;
	}
	
	protected List getObjects() throws RemoteException, FinderException, XavaException {
		if (objects == null) {
			objects = new ArrayList();
			Iterator it = getMapValues().iterator();
			while (it.hasNext()) {
				Map key = (Map) it.next();
				objects.add(MapFacade.findEntity(getCollectionElementView().getModelName(), key));
			}
		}
		return objects;
	}
	
	protected List getSelectedObjects() throws RemoteException, FinderException, XavaException {
		if (selectedObjects == null) {
			selectedObjects = new ArrayList();
			List values = getCollectionElementView().getCollectionValues();
			int [] sel = getCollectionElementView().getListSelected();
			for (int i=0; i<sel.length; i++) {
				Map clave = (Map) values.get(sel[i]);
				selectedObjects.add(MapFacade.findEntity(getCollectionElementView().getModelName(), clave));
			}					
		}
		return selectedObjects;
	}
	
}
