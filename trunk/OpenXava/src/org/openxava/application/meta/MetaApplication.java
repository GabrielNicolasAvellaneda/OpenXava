package org.openxava.application.meta;


import java.util.*;

import org.openxava.util.*;
import org.openxava.util.meta.*;

/**
 * @author Javier Paniza
 */
public class MetaApplication extends MetaElement implements java.io.Serializable {


	private Map metaModules = new HashMap();
	private Collection modulesNames = new ArrayList(); // to preserve the order
	
	/**
	 * 
	 * @param newModule Not null
	 */
	public void addMetaModule(MetaModule newModule) {
		metaModules.put(newModule.getName(), newModule);
		newModule.setMetaApplication(this);
		modulesNames.add(newModule.getName()); // to preserve the order
	}

	
	
	/**
	 * 
	 * @exception XavaException  Any problem 
	 * @return of <tt>MetaModule</tt>. Not null.
	 */
	public Collection getMetaModules() throws XavaException {
		return metaModules.values();
	}

	/**
	 * In the same order that they are found in application.xml/aplicacion.xml. <p>
	 *  
	 * @return of <tt>MetaModule</tt>. Not null.
	 */	
	public Collection getModulesNames() {
		return modulesNames;
	}
	
	/**
     * @exception ElementNotFoundException
	 */
	public MetaModule getMetaModule(String name) throws ElementNotFoundException, XavaException {
		MetaModule result = (MetaModule) metaModules.get(name);
		if (result == null) {
			throw new ElementNotFoundException("module_not_found", name);
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


