package org.openxava.web.meta;

import java.util.*;

import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.web.meta.xmlparse.*;



/**
 * Inserte aquí la descripción del tipo.
 * 
 * @author Javier Paniza
 */
public class MetaWebEditors {
		
	private static Map editorsByType;
	private static Map editorsByStereotype;
	private static Map editorsByModelProperty;

	public static void addMetaEditorForType(String tipo, MetaEditor nuevo) throws XavaException {
		if (editorsByType == null) {
			throw new XavaException("only_from_parse", "MetaWebEditors.addMetaEditorForType");
		}
		editorsByType.put(tipo, nuevo);
	}
	
	public static void addMetaEditorForStereotype(String estereotipo, MetaEditor nuevo) throws XavaException {		
		if (editorsByStereotype == null) {
			throw new XavaException("only_from_parse", "MetaWebEditors.addMetaEditorForStereotype");
		}
		editorsByStereotype.put(estereotipo, nuevo);
	}
	
	public static void addMetaEditorForModelProperty(String propiedad, String modelo, MetaEditor nuevo) throws XavaException {
		if (editorsByModelProperty == null) {
			throw new XavaException("only_from_parse", "MetaWebEditors.addMetaEditorForModelProperty");
		}
		editorsByModelProperty.put(crearClavePropiedadModelo(propiedad, modelo), nuevo);
	}
	
	private static String crearClavePropiedadModelo(String propiedad,	String modelo) {
		return modelo + "::" + propiedad;
	}
	
	/**
	 * @return Nulo si no un editor registrado para el tipo indicado.
	 */
	public static MetaEditor getMetaEditorForType(String tipo)	throws XavaException {
		return (MetaEditor) getEditorsByType().get(tipo);
	}

	/**
	 * @return Nulo si no un editor registrado para el estereotipo indicado.
	 */
	public static MetaEditor getMetaEditorForStereotype(String estereotipo)	throws XavaException {
		return (MetaEditor) getEditorsByStereotype().get(estereotipo);
	}
	
	/**
	 * @return Nulo si no un editor registrado para la propiedad/modelo indicado.
	 */
	public static MetaEditor getMetaEditorForModelProperty(String propiedad, String modelo)	throws XavaException {
		return (MetaEditor) getEditorsByModelProperty().get(crearClavePropiedadModelo(propiedad, modelo));
	}
		
	private static Map getEditorsByType() throws XavaException {
		if (editorsByType == null) {
			iniciarMapas();
			EditorsParser.setupEditors();
		}
		return editorsByType;
	}
	
	private static Map getEditorsByStereotype() throws XavaException {
		if (editorsByStereotype == null) {
			iniciarMapas();
			EditorsParser.setupEditors();								
		}	
		return editorsByStereotype;
	}
	
	private static Map getEditorsByModelProperty() throws XavaException {
		if (editorsByModelProperty == null) {
			iniciarMapas();
			EditorsParser.setupEditors();
		}
		return editorsByModelProperty;
	}
		
	private static void iniciarMapas() {
		editorsByType = new HashMap();
		editorsByStereotype = new HashMap();
		editorsByModelProperty = new HashMap();		
	}

	
	/**	 
	 * @return Nunca nulo
	 * @throws ElementNotFoundException Si no hay ningún editor disponible	 
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