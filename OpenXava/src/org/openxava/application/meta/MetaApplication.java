package org.openxava.application.meta;


import java.io.*;
import java.net.*;
import java.util.*;

import javax.persistence.*;

import org.apache.commons.logging.*;
import org.openxava.component.*;
import org.openxava.controller.meta.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;

/**
 * @author Javier Paniza
 */
public class MetaApplication extends MetaElement implements java.io.Serializable {

	private static Log log = LogFactory.getLog(MetaApplication.class);

	private Map metaModules = new HashMap();
	private Collection modulesNames = new ArrayList(); // to preserve the order
	private Collection folders;
	private Collection controllersForDefaultModule;
	private boolean defaultModulesGenerated = false;
	
	
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
		return Labels.get(folder, locale);
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
		generateDefaultModules();
		return metaModules.values();
	}

	private void generateDefaultModules() throws XavaException { 
		if (defaultModulesGenerated) return;
		generateDefaultModulesFromJPAEntities();
		generateDefaultModulesFromXMLComponents();
		defaultModulesGenerated = true;
	}

	private void generateDefaultModulesFromXMLComponents() throws XavaException {
		for (Iterator it=MetaComponent.getAll().iterator(); it.hasNext(); ) {
			String modelName = ((MetaComponent) it.next()).getName();
			if (!metaModules.containsKey(modelName)) {
				createDefaultModule(modelName);
			}			
		}		
	}

	private void generateDefaultModulesFromJPAEntities() throws XavaException {
		URL url = getAnchorURL();		
		if (url != null) {			
			File baseClassPath=new File(Strings.noLastToken(url.getPath(), "/"));
			generateDefaultModulesFromJPAEntities(baseClassPath, null, false);
		}
		else {			
			log.warn(XavaResources.getString("default_modules_from_jpa_anchor_not_found", "xava.properties, application.xml, aplicacion.xml"));
		}
	}
		
	private void generateDefaultModulesFromJPAEntities(File dir, String base, boolean model) throws XavaException {
		File [] files = dir.listFiles();
		for (int i=0; i<files.length; i++ ) {
			File file = files[i];
			String basePackage = base == null?"":base + dir.getName() + ".";
			if (file.isDirectory()) {
				boolean isModelPackage = "model".equals(file.getName()) || "modelo".equals(file.getName());				 
				generateDefaultModulesFromJPAEntities(file, basePackage, isModelPackage?true:model);
			}
			else if (model && file.getName().endsWith(".class")) {				
				String modelName = file.getName().substring(0, file.getName().length() - ".class".length());
				if (metaModules.containsKey(modelName)) continue;
				String className = basePackage + modelName;
				try { 
					Class entityClass = Class.forName(className);
					if (entityClass.isAnnotationPresent(Entity.class)) {						
						createDefaultModule(modelName);
					}
					
				}
				catch (ClassNotFoundException ex) {					
				}				
			}
		}		
	}
	
	private URL getAnchorURL() {
		URL url = getFileURL("/xava.properties");		
		if (url == null) url = getFileURL("/application.xml");
		if (url == null) url = getFileURL("/aplicacion.xml");
		return url;
	}
	
	private URL getFileURL(String file) {
		try {
			for (Enumeration en=ClassLoader.getSystemResources("xava.properties"); en.hasMoreElements(); ) {
				URL url = (URL) en.nextElement();
				if (url != null) {
					if ("file".equals(url.getProtocol())) {
						return url;
					}
				}
			}
			return null;
		}
		catch (Exception ex) {
			return null;
		}
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
			if (MetaComponent.exists(name)) {
				result = createDefaultModule(name);		
			}
		}
		if (result == null) {
			throw new ElementNotFoundException("module_not_found", name);
		}
		return result;
	}
	
	private MetaModule createDefaultModule(String modelName) throws XavaException { 				
		MetaModule module = new MetaModule();
		module.setMetaApplication(this);
		module.setName(modelName);			
		module.setModelName(modelName);
		module.addControllerName("Navigation");
		module.addControllerName("List");
		if (MetaControllers.contains(modelName)) {
			module.addControllerName(modelName);
		}
		else {
			for (Iterator it = getControllersForDefaultModule().iterator(); it.hasNext();) {
				module.addControllerName((String) it.next()); 
			}
		}
		metaModules.put(modelName, module);
		return module;		
	}
	
	public void addControllerForDefaultModule(String controllerName) { 
		if (controllersForDefaultModule == null) controllersForDefaultModule = new ArrayList();
		controllersForDefaultModule.add(controllerName);
	}

	private Collection getControllersForDefaultModule() { 
		if (controllersForDefaultModule == null) return Collections.EMPTY_LIST;
		return controllersForDefaultModule;
	}

	public String toString() {
		return "Application: " + getName(); 
	}

	public String getId() {
		return getName();
	}
	
}


