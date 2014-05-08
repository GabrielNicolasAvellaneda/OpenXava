package com.openxava.naviox.impl;

import java.util.*;

import javax.persistence.*;

import org.openxava.annotations.parse.*;
import org.openxava.application.meta.*;
import org.openxava.util.*;

/**
 * 
 * @author Javier Paniza
 */

public class MetaModuleFactory {
	
	public static MetaModule create(String application, String module) {		
		return MetaApplications.getMetaApplication(application).getMetaModule(module);				
	}
	
	public static List<MetaModule> createAll() {
		List<MetaModule> result = new ArrayList<MetaModule>();
		for (Object oapp: MetaApplications.getMetaApplications()) {
			MetaApplication app = (MetaApplication) oapp;
			createDefaultMetaModules(app);
			createAdditionalMetaModules(app); 
			result.addAll(app.getMetaModules());
		}
		return result;
	}

	private static void createDefaultMetaModules(MetaApplication app) {
		for (String className: AnnotatedClassParser.getManagedClassNames()) {
			if (className.endsWith(".GalleryImage") || className.endsWith(".AttachedFile")) continue;
			if (isEmbeddable(className)) continue;
			app.getMetaModule(Strings.lastToken(className, "."));
		}		
	}
	
	private static void createAdditionalMetaModules(MetaApplication app) {
		for (String moduleName: AdditionalModules.get()) {
			app.getMetaModule(moduleName);
		}		
	}
	

	private static boolean isEmbeddable(String className) {
		try {
			return Class.forName(className).isAnnotationPresent(Embeddable.class);
		} 
		catch (ClassNotFoundException e) {
			return false;
		}		
	}

}
