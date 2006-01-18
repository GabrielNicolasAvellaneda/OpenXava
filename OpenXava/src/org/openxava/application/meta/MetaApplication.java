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
	private Collection folders;
	
	/**
	 * 
	 * @param newModule Not null
	 */
	public void addMetaModule(MetaModule newModule) {
		metaModules.put(newModule.getName(), newModule);
		newModule.setMetaApplication(this);
		modulesNames.add(newModule.getName()); // to preserve the order
	}
	
	public String getFolderLabel(Locale locale, String folder) {
		folder = Strings.change(folder, "/", ".");
		try {					
			return Labels.get(folder, locale);
		}
		catch (Exception ex) {
			System.err.println(XavaResources.getString("element_i18n_warning", folder));			
			return firstUpper(folder); 			
		}				
	}
	
	public String getFolderLabel(String folder) {
		return getFolderLabel(Locale.getDefault(), folder);
	}
	
	
	public Collection getFolders() throws XavaException { 
		if (folders == null) {
			folders = new HashSet();
			for (Iterator it = getMetaModules().iterator(); it.hasNext(); ) {
				MetaModule metaModule = (MetaModule) it.next(); 
				folders.add(metaModule.getFolder());
			}
		}
		return folders;
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
	 * @return of <tt>String</tt>. Not null.
	 */	
	public Collection getModulesNames() {
		return modulesNames;
	}
	
	/**
	 * The modules in the indicated folder 
	 * in the same order that they are found in application.xml/aplicacion.xml. <p>
	 *  
	 * @return of <tt>String</tt>. Not null.
	 * @throws XavaException 
	 */	
	public Collection getModulesNamesByFolder(String folder) throws XavaException {
		if (Is.emptyString(folder) || folder.trim().equals("/")) folder = ""; 
		Collection result = new ArrayList();
		for (Iterator it=getModulesNames().iterator(); it.hasNext();) {
			String moduleName = (String) it.next();
			String moduleFolder = getMetaModule(moduleName).getFolder();
			if (Is.equal(folder, moduleFolder)) {
				result.add(moduleName);
			}
		}
		return result;
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


