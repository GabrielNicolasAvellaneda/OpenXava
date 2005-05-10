package org.openxava.web.meta.xmlparse;


import org.openxava.util.*;
import org.openxava.util.meta.*;
import org.openxava.util.xmlparse.*;
import org.openxava.web.meta.*;
import org.w3c.dom.*;

/**
 * @author: Javier Paniza
 */
public class EditorsParser extends ParserBase {
			
	public EditorsParser(String xmlFileURL, int language) {
		super(xmlFileURL, language);
	}
	
	public static void setupEditors() throws XavaException {
		EditorsParser defaultParser = new EditorsParser("default-editors.xml", ENGLISH);
		defaultParser.parse();		
		EditorsParser enParser = new EditorsParser("editors.xml", ENGLISH);
		enParser.parse();				
		EditorsParser esParser = new EditorsParser("editores.xml", ESPANOL);
		esParser.parse();		
	}
	
	private void addEditors(Element el) throws XavaException {
		String url = el.getAttribute(xurl[lang]);		
		if (Is.emptyString(url)) return;
		MetaEditor editor = new MetaEditor();				
		editor.setUrl(url);
		editor.setFormat(getAttributeBoolean(el, xformat[lang]));
		editor.setFrame(getAttributeBoolean(el, xwithframe[lang]));		
		String dependsStereotypes = el.getAttribute(xdepends_stereotypes[lang]);
		String dependsProperties = el.getAttribute(xdepends_properties[lang]);
		if (
			!Is.emptyString(dependsStereotypes) &&
			!Is.emptyString(dependsProperties)) {
			throw new XavaException("editor_definition_error", editor.getUrl());	
		}
		editor.setDependsStereotypes(dependsStereotypes);
		editor.setDependsProperties(dependsProperties);
		fillProperties(editor, el);				
		editor.setFormatterClassName(getFormatterClass(el));
		editor.setFormatterFromType(getFormatterFromType(el));
		if (editor.isFormatterFromType() && !Is.emptyString(editor.getFormatterClassName())) {
			throw new XavaException("formatter_class_and_from_type_not_compatible");
		}
		fillSets(el, editor);
		addEditorsForType(editor, el);
		addEditorsForStereotype(editor, el);
		addEditorsForModelProperty(editor, el);
	}	

	private void fillSets(Element el, MetaEditor container)	throws XavaException {
		NodeList l = el.getElementsByTagName(xset[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			container._addFormatterMetaSet(createSet(l.item(i)));
		}
	}
	
	private MetaSet createSet(Node n) throws XavaException {
		Element el = (Element) n;
		MetaSet a = new MetaSet();		
		a.setPropertyName(el.getAttribute(xproperty[lang]));
		a.setValue(el.getAttribute(xvalue[lang]));		
		return a;
	}
	
	
	private String getFormatterClass(Element n) throws XavaException {
		NodeList l = n.getElementsByTagName(xformatter[lang]);
		int c = l.getLength();
		if (c > 1) {
			throw new XavaException("no_more_1_formatter");
		}
		if (c < 1) return null;
		Element el = (Element) l.item(0);					
		return el.getAttribute(xclass[lang]);						
	}
	
	private boolean getFormatterFromType(Element n) throws XavaException {
		NodeList l = n.getElementsByTagName(xformatter[lang]);
		int c = l.getLength();
		if (c > 1) {
			throw new XavaException("no_more_1_formatter");
		}
		if (c < 1) return false;
		Element el = (Element) l.item(0);					
		return getAttributeBoolean(el, xfrom_type[lang]);						
	}
		
	private void fillProperties(MetaEditor editor, Element n) {
		NodeList l = n.getElementsByTagName(xproperty[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			Element el = (Element) l.item(i);					
			editor.addProperty(el.getAttribute(xname[lang]), el.getAttribute(xvalue[lang]));
		}				
	}

	
	private void addEditorsForType(MetaEditor editor, Element n) throws XavaException {		
		NodeList l = n.getElementsByTagName(xfor_type[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			Element el = (Element) l.item(i);		
			MetaWebEditors.addMetaEditorForType(el.getAttribute(xtype[lang]), editor);
		}		
	}
	
	private void addEditorsForStereotype(MetaEditor editor, Element n) throws XavaException {		
		NodeList l = n.getElementsByTagName(xfor_stereotype[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			Element el = (Element) l.item(i);		
			MetaWebEditors.addMetaEditorForStereotype(el.getAttribute(xstereotype[lang]), editor);
		}		
	}
	
	private void addEditorsForModelProperty(MetaEditor editor, Element n) throws XavaException {		
		NodeList l = n.getElementsByTagName(xfor_model_property[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			Element el = (Element) l.item(i);		
			MetaWebEditors.addMetaEditorForModelProperty(el.getAttribute(xproperty[lang]), el.getAttribute(xmodel[lang]), editor);
		}		
	}
			
	private void createEditors() throws XavaException {
		NodeList l = getRoot().getElementsByTagName(xeditor[lang]);
		int c = l.getLength();		
		for (int i = 0; i < c; i++) {
			Element el = (Element) l.item(i);						
			addEditors(el);			
		}						
	}
			
	protected void createObjects() throws XavaException {
		createEditors();				
	}
		
}