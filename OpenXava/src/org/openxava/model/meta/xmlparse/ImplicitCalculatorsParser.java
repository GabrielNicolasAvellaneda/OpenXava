package org.openxava.model.meta.xmlparse;

import org.openxava.util.*;
import org.openxava.util.xmlparse.*;
import org.w3c.dom.*;

/**
 * @author: Javier Paniza
 */
public class ImplicitCalculatorsParser extends ParserBase {
	

	public ImplicitCalculatorsParser(String xmlFileURL, int language) {
		super(xmlFileURL, language);
	}
		
	public static void configureImplicitCalculators() throws XavaException {
		ImplicitCalculatorsParser enParser = new ImplicitCalculatorsParser("implicit-calculators.xml", ENGLISH);
		enParser.parse();		
		ImplicitCalculatorsParser esParser = new ImplicitCalculatorsParser("calculadores-implicitos.xml", ESPANOL);
		esParser.parse();
	}
	
	private void createCalculators(Node n, int scope) throws XavaException {
		Element el = (Element) n;
		String models = el.getAttribute(xmodels[lang]);
		NodeList l = el.getChildNodes();
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			if (!(l.item(i) instanceof Element)) continue;
			Element d = (Element) l.item(i);
			String type = d.getTagName();
			if (type.equals(xpostcreate_calculator[lang])) {
				ImplicitCalculators.addMetaCalculatorPostCreate(models, scope, CalculatorParser.parseCalculator(d, lang));							
			}
			else if (type.equals(xpostload_calculator[lang])) {
				ImplicitCalculators.addMetaCalculatorPostLoad(models, scope, CalculatorParser.parseCalculator(d, lang));							
			}			
			else if (type.equals(xpostmodify_calculator[lang])) {
				ImplicitCalculators.addMetaCalculatorPostModify(models, scope, CalculatorParser.parseCalculator(d, lang));							
			}
			else if (type.equals(xpreremove_calculator[lang])) {
				ImplicitCalculators.addMetaCalculatorPreRemove(models, scope, CalculatorParser.parseCalculator(d, lang));							
			}						
		}		
	}
	
	private void createAll() throws XavaException {
		NodeList l = getRoot().getElementsByTagName(xfor_all_models[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			createCalculators(l.item(i), ImplicitCalculators.ALL);
		}
	}	
		
	private void createFors() throws XavaException {
		NodeList l = getRoot().getElementsByTagName(xfor[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			createCalculators(l.item(i), ImplicitCalculators.FOR);
		}
	}
	
	private void createExceptFors() throws XavaException {
		NodeList l = getRoot().getElementsByTagName(xexcept_for[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			createCalculators(l.item(i), ImplicitCalculators.EXCEPT_FOR);
		}
	}	
		
	protected void createObjects() throws XavaException {
		createAll();
		createFors();
		createExceptFors();
	}
			
}