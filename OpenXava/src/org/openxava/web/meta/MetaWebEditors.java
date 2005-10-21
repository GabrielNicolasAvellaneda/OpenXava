package org.openxava.web.meta;

import java.util.*;

import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.web.meta.xmlparse.*;

/**
 * 
 * @author Javier Paniza
 */
public class MetaWebEditors {
		
	private static Map editorsByType;
	private static Map editorsByStereotype;
	private static Map editorsByModelProperty;

	public static void addMetaEditorForType(String type, MetaEditor editor) throws XavaException {
		if (editorsByType == null) {
			throw new XavaException("only_from_parse", "MetaWebEditors.addMetaEditorForType");
		}
		editorsByType.put(type, editor);
	}
	
	public static void addMetaEditorForStereotype(String stereotype, MetaEditor editor) throws XavaException {		
		if (editorsByStereotype == null) {
			throw new XavaException("only_from_parse", "MetaWebEditors.addMetaEditorForStereotype");
		}
		editorsByStereotype.put(stereotype, editor);
	}
	
	public static void addMetaEditorForModelProperty(String property, String model, MetaEditor editor) throws XavaException {
		if (editorsByModelProperty == null) {
			throw new XavaException("only_from_parse", "MetaWebEditors.addMetaEditorForModelProperty");
		}
		editorsByModelProperty.put(createPropertyModelKey(property, model), editor);
	}
	
	private static String createPropertyModelKey(String property, String model) {
		return model + "::" + property;
	}
	
	/**
	 * @return Null if no editor registered for the specified type
	 */
	public static MetaEditor getMetaEditorForType(String type)	throws XavaException {
		return (MetaEditor) getEditorsByType().get(type);
	}

	/**
	 * @return Null if no editor registered for the specified stereotype
	 */
	public static MetaEditor getMetaEditorForStereotype(String stereotype)	throws XavaException {
		return (MetaEditor) getEditorsByStereotype().get(stereotype);
	}
	
	/**
	 * @return Null if no editor registered for the specified property/model
	 */
	public static MetaEditor getMetaEditorForModelProperty(String property, String model) throws XavaException {
		return (MetaEditor) getEditorsByModelProperty().get(createPropertyModelKey(property, model));
	}
		
	private static Map getEditorsByType() throws XavaException {
		if (editorsByType == null) {
			initMaps();
			EditorsParser.setupEditors();
		}
		return editorsByType;
	}
	
	private static Map getEditorsByStereotype() throws XavaException {
		if (editorsByStereotype == null) {
			initMaps();
			EditorsParser.setupEditors();								
		}	
		return editorsByStereotype;
	}
	
	private static Map getEditorsByModelProperty() throws XavaException {
		if (editorsByModelProperty == null) {
			initMaps();
			EditorsParser.setupEditors();
		}
		return editorsByModelProperty;
	}
		
	private static void initMaps() {
		editorsByType = new HashMap();
		editorsByStereotype = new HashMap();
		editorsByModelProperty = new HashMap();		
	}

	
	/**	 
	 * @return Not null
	 * @throws ElementNotFoundException If no editor for property	 
	 */
	public static MetaEditor getMetaEditorFor(MetaProperty p) throws ElementNotFoundException, XavaException {							
		if (p.hasMetaModel()) {			
			MetaEditor r = (MetaEditor) getMetaEditorForModelProperty(p.getName(), p.getMetaModel().getName());
			if (r != null) {				
				return r;				
			}
		}				
		if (p.hasStereotype()) {			
			MetaEditor r = (MetaEditor) getMetaEditorForStereotype(p.getStereotype());				
			if (r != null) {				
				return r;
			}
		}				
		MetaEditor r = (MetaEditor) getMetaEditorForType(p.getType().getName());		
		if (r == null) {
			throw new ElementNotFoundException("editor_not_found", p.getId());
		}		
		return r;
	}
	
}