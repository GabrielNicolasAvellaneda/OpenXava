package org.openxava.application.meta;


import java.util.*;

import org.openxava.util.*;
import org.openxava.util.meta.*;

/**
 * @author Javier Paniza
 */
public class MetaApplication extends MetaElement implements java.io.Serializable {


	private Map metaModules = new HashMap();	
	
	/**
	 * 
	 * @param newModule Not null
	 */
	public void addMetaModule(MetaModule newModule) {
		metaModules.put(newModule.getName(), newModule);
		newModule.setMetaApplication(this);
	}
	
	
	/**
	 * 
	 * @exception XavaException  Any problem 
	 * @return de <tt>MetaModule</tt>. Not null.
	 */
	public Collection getMetaModules() throws XavaException {
		return metaModules.values();
	}
	
	/**
     * @exception ElementNotFoundException
	 */
	public MetaModule getMetaModule(String name) throws ElementNotFoundException, XavaException {
		MetaModule result = (MetaModule) metaModules.get(name);
		if (result == null) {
			throw new ElementNotFoundException("component_not_found", name);
		}
		return result;
	}
	
	public String toString() {
		return "Application: " + getName(); 
	}

	public String getId() {
		return getName();
	}
	
}


