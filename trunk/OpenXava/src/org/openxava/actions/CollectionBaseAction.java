package org.openxava.actions;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;
import org.openxava.util.*;

/**
 * Base class for creating actions to be used as list actions.<p>
 * 
 * That is in <list-action/> of <collection-view/> or
 * in @ListAction annotation.<br>
 * 
 * @author Javier Paniza
 */

abstract public class CollectionBaseAction extends CollectionElementViewBaseAction {

	private List mapValues = null;
	private List mapsSelectedValues;
	private List objects;
	private List selectedObjects;
	

	/**
	 * A list of all collection element when each element is a map 
	 * with the values of the collection element.<p>
	 * 
	 * The values only include the displayed data in the row.<br>
	 * @return  Of type <tt>Map</tt>. Never null.
	 */
	protected List getMapValues() throws XavaException {
		if (mapValues == null) {
			mapValues = getCollectionElementView().getCollectionValues();
		}
		return mapValues;
	}
	
	/**
	 * A list of selected collection element when each element is a map 
	 * with the values of the collection element.<p>
	 * 
	 * The values only include the displayed data in the row.<br>
	 * @return  Of type <tt>Map</tt>. Never null.
	 */
	protected List getMapsSelectedValues() throws XavaException {
		if (mapsSelectedValues == null) {
			mapsSelectedValues = getCollectionElementView().getCollectionSelectedValues();
		}
		return mapsSelectedValues;
	}
	
	
	/**
	 * A list of all objects (POJOs or EntityBeans) in the collection.<p>
	 * 
	 * Generally the objects are POJOs, although if you use EJBPersistenceProvider
	 * the they will be EntityBeans (of EJB2).<br> 
	 *  
	 * @return  Never null.
	 */	
	protected List getObjects() throws RemoteException, FinderException, XavaException {
		if (objects == null) {
			objects = getCollectionElementView().getCollectionObjects(); 
		}
		return objects;
	}
	
	/**
	 * A list of selected objects (POJOs or EntityBeans) in the collection.<p>
	 * 
	 * Generally the objects are POJOs, although if you use EJBPersistenceProvider
	 * the they will be EntityBeans (of EJB2).<br> 
	 *  
	 * @return  Never null.
	 */	
	protected List getSelectedObjects() throws RemoteException, FinderException, XavaException {
		if (selectedObjects == null) {
			selectedObjects = getCollectionElementView().getCollectionSelectedObjects();							
		}
		return selectedObjects;		
	}
	
}
