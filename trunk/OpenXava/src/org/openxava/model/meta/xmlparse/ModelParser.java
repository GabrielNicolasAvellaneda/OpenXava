package org.openxava.model.meta.xmlparse;

import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;
import org.openxava.util.xmlparse.*;
import org.openxava.validators.meta.*;
import org.w3c.dom.*;

/**
 * @author Javier Paniza
 */
public class ModelParser extends XmlElementsNames {
				
	public static MetaEntity parseEntity(Node n, int lang) throws XavaException {
		Element el = (Element) n;
		MetaEntityEjb e = new MetaEntityEjb();
		e.setName(el.getAttribute(xname[lang]));		
		e.setLabel(el.getAttribute(xlabel[lang]));
		if (hasEjb(el, lang)) {							
			fillEjbInfo(el, e, lang);
			e.setGenerateXDocLet(false);
		}
		else {			
			e.setGenerateXDocLet(true);
		}		
		fillMembers(el, e, lang);								
		return e;
	}
		
	public static MetaAggregate parseAggregate(Node n, MetaModel container, int lang) throws XavaException {
		Element el = (Element) n;
		if (hasEjb(el, lang)) {
			MetaAggregateEjb r = crearAgregadoEjb(n, lang);
			r.setGenerateXDocLet(false);
			return r;			
		}
		else if (hasBean(el, lang)) {
			MetaAggregateBean r = createAggregateBean(n, lang);
			r.setGenerate(false);
			return r;
		}
		else {
			String name = el.getAttribute(xname[lang]);
			if (container.containsMetaReferenceWithModel(name)) {
				MetaAggregateBean r = createAggregateBean(n, lang);
				r.setGenerate(true);
				return r;
			}
			else {
				MetaAggregateEjb r = crearAgregadoEjb(n, lang);
				r.setGenerateXDocLet(true);
				return r;
			}
		}		
	}

	private static MetaAggregateBean createAggregateBean(Node n, int lang) throws XavaException {	
		Element el = (Element) n;
		MetaAggregateBean a = new MetaAggregateBean();
		a.setName(el.getAttribute(xname[lang]));
		a.setLabel(el.getAttribute(xlabel[lang]));		
		if (hasBean(el, lang)) {
			a.setBeanClass(getBeanClass(el, lang));
		}				
		fillMembers(el, a, lang);
		return a;
	}
	
	private static MetaAggregateEjb crearAgregadoEjb(Node n, int lang) throws XavaException {	
		Element el = (Element) n;		
		MetaAggregateEjb a = new MetaAggregateEjb();
		a.setName(el.getAttribute(xname[lang]));
		a.setLabel(el.getAttribute(xlabel[lang]));
		if (hasEjb(el, lang)) {
			fillEjbInfo(el, a, lang);
		}
		fillMembers(el, a, lang);
		return a;
	}
		
	private static void fillEjbInfo(Element el, IMetaEjb ejb, int lang) throws XavaException {
		Element elEjb = ParserUtil.getElement(el, xejb[lang]);
		if (elEjb == null) {
			throw new XavaException("ejb_expected", el.getAttribute(xname[lang]));
		}
		ejb.setRemote(elEjb.getAttribute(xremote[lang]));
		ejb.setHome(elEjb.getAttribute(xhome[lang]));
		ejb.setPrimaryKey(elEjb.getAttribute(xprimaryKey[lang]));
		ejb.setJndi(elEjb.getAttribute(xjndi[lang]));
	}
	
	private static void fillMembers(Element el, MetaModel container, int lang)
		throws XavaException {
		NodeList l = el.getChildNodes();
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			if (!(l.item(i) instanceof Element)) continue;
			Element d = (Element) l.item(i);
			String type = d.getTagName();
			if (type.equals(xproperty[lang])) {
				container.addMetaProperty(createProperty(d, lang));
			}
			else if (type.equals(xreference[lang])) {
				container.addMetaReference(createReference(d, lang));
			}
			else if (type.equals(xcollection[lang])) {
				container.addMetaCollection(createCollection(d, lang));
			}
			else if (type.equals(xmethod[lang])) {
				container.addMetaMethod(createMethod(d, lang));
			}			
			else if (type.equals(xfinder[lang])) {				
				container.addMetaFinder(createFinder(d, lang));
			}
			else if (type.equals(xpostcreate_calculator[lang])) {
				container.addMetaCalculatorPostCreate(CalculatorParser.parseCalculator(d, lang));							
			}
			else if (type.equals(xpostmodify_calculator[lang])) {
				container.addMetaCalculatorPostModify(CalculatorParser.parseCalculator(d, lang));							
			}			
			else if (type.equals(xvalidator[lang])) {
				container.addMetaValidator(createValidator(d, lang));
			}									
			else if (type.equals(xremove_validator[lang])) {
				container.addMetaValidatorRemove(createValidator(d, lang));
			}
			else if (type.equals(ximplements[lang])) {
				container.addInterfaceName(d.getAttribute(xinterface[lang]));
			}			
		}
	}
	
	private static void fillValidator(Element el, MetaProperty container, int lang)
		throws XavaException {
		NodeList l = el.getChildNodes();
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			if (!(l.item(i) instanceof Element)) continue;
			Element d = (Element) l.item(i);
			String type = d.getTagName();
			if (type.equals(xvalidator[lang])) {
				container.addMetaValidator(createValidator(d, lang));
			}
		}
	}
	
	private static void fillPostremoveCalculator(Element el, MetaCollection container, int lang)
		throws XavaException {
		NodeList l = el.getChildNodes();
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			if (!(l.item(i) instanceof Element)) continue;
			Element d = (Element) l.item(i);
			String type = d.getTagName();
			if (type.equals(xpostremove_calculator[lang])) {
				container.addMetaCalculatorPostRemove(CalculatorParser.parseCalculator(d, lang));
			}
		}
	}
	
				
	private static boolean hasEjb(Element el, int lang) {
		return ParserUtil.getElement(el, xejb[lang]) != null;
	}
	
	private static boolean hasBean(Element el, int lang) {
		return ParserUtil.getElement(el, xbean[lang]) != null;
	}	
	
	/**
	 * @return Null if no exists a bean element.
	 */
	private static String getBeanClass(Element el, int lang) throws XavaException {
		Element elBean = ParserUtil.getElement(el, xbean[lang]);
		if (elBean == null) {
			throw new XavaException("xml_element_not_found", xbean[lang], el.getAttribute(xname[lang]));
		}
		return elBean.getAttribute(xclass[lang]);
	}
		

	public static MetaProperty createProperty(Node n, int lang) throws XavaException {
		Element el = (Element) n;
		MetaProperty p = new MetaProperty();
		p.setName(el.getAttribute(xname[lang]));
		p.setLabel(el.getAttribute(xlabel[lang]));
		p.setStereotype(el.getAttribute(xstereotype[lang]));
		p.setTypeName(el.getAttribute(xtype[lang]));
		p.setSize(ParserUtil.getAttributeInt(el, xsize[lang]));
		p.setRequired(ParserUtil.getAttributeBoolean(el, xrequired[lang]));
		p.setHidden(ParserUtil.getAttributeBoolean(el, xhidden[lang]));
		boolean key = ParserUtil.getAttributeBoolean(el, xkey[lang]);
		if (key) p.setKey(key);
		fillValidator(el, p, lang);				
		Element elValidValues = ParserUtil.getElement(el, xvalid_values[lang]);
		if (elValidValues != null) {
			NodeList l = elValidValues.getElementsByTagName(xvalid_value[lang]);
			int c = l.getLength();
			for (int i = 0; i < c; i++) {
				Element validValue = (Element) l.item(i);
				p.addValidValue(validValue.getAttribute(xvalue[lang]));
			}
		}
		p.setMetaCalculator(createCalculator(el, lang));
		p.setMetaCalculatorDefaultValue(crearCalculadorValorDefeto(el, lang));
		return p;
	}
	
	private static MetaFinder createFinder(Node n, int lang) throws XavaException {
		Element el = (Element) n;
		MetaFinder b = new MetaFinder();
		b.setName(el.getAttribute(xname[lang]));		
		b.setArguments(el.getAttribute(xarguments[lang]));
		b.setCollection(ParserUtil.getAttributeBoolean(el, xcollection[lang]));		
		b.setCondition(ParserUtil.getString(el, xcondition[lang]));		
		b.setOrder(ParserUtil.getString(el, xorder[lang]));
		return b;
	}
	
	private static MetaMethod createMethod(Node n, int lang) throws XavaException {
		Element el = (Element) n;
		MetaMethod m = new MetaMethod();
		m.setName(el.getAttribute(xname[lang]));
		m.setTypeName(el.getAttribute(xtype[lang]));		
		m.setArguments(el.getAttribute(xarguments[lang]));
		m.setExceptions(el.getAttribute(xexceptions[lang]));
		m.setMetaCalculator(createCalculator(el, lang));
		return m;
	}
			
	private static MetaValidator createValidator(Node n, int lang) throws XavaException {
		Element el = (Element) n;
		String name = el.getAttribute(xname[lang]);
		String className = el.getAttribute(xclass[lang]);
			
		if (!Is.emptyString(name) && !Is.emptyString(className)) {
			throw new XavaException("name_and_class_not_compatible");
		}
		if (Is.emptyStringAll(name, className)) {
			throw new XavaException("name_or_class_required");
		}
		
		MetaValidator e = new MetaValidator();
		e.setName(name);
		e.setClassName(className);		
		fillSet(el, e, lang);
		return e;
	}
	
	private static void fillSet(Element el, MetaSetsContainer container, int lang)
		throws XavaException {
		NodeList l = el.getElementsByTagName(xset[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			container.addMetaSet(createSet(l.item(i), lang));
		}
	}
	
	
	private static MetaSet createSet(Node n, int lang) throws XavaException {
		Element el = (Element) n;
		MetaSet a = new MetaSet();		
		a.setPropertyName(el.getAttribute(xproperty[lang]));
		a.setPropertyNameFrom(el.getAttribute(xfrom[lang]));
		a.setValue(el.getAttribute(xvalue[lang]));		
		return a;
	}
	
	
			
	private static MetaCalculator createCalculator(Element el, int lang) throws XavaException {
		NodeList l = el.getElementsByTagName(xcalculator[lang]);
		int c = l.getLength();
		if (c > 1) {			
			throw new XavaException("property_no_more_1_calculator");
		}
		if (c < 1) return null;
		return CalculatorParser.parseCalculator(l.item(0), lang);
	}
	
	private static MetaCalculator crearCalculadorValorDefeto(Element el, int lang) throws XavaException {
		NodeList l = el.getElementsByTagName(xdefault_value_calculator[lang]);
		int c = l.getLength();
		if (c > 1) {			
			throw new XavaException("property_no_more_1_default_value_calculator");
		}
		if (c < 1) return null;
		return CalculatorParser.parseCalculator(l.item(0), lang);
	}
		
	private static MetaReference createReference(Node n, int lang) throws XavaException {
		Element el = (Element) n;
		MetaReference r = new MetaReference();
		String name = el.getAttribute(xname[lang]); 		
		String model = el.getAttribute(xmodel[lang]);
		if (Is.emptyString(name) && Is.emptyString(model)) {
			throw new XavaException("name_or_model_required");
		}
		r.setName(name);
		r.setReferencedModelName(model);
		r.setLabel(el.getAttribute(xlabel[lang]));		
		r.setRequired(ParserUtil.getAttributeBoolean(el, xrequired[lang]));
		r.setKey(ParserUtil.getAttributeBoolean(el, xkey[lang]));
		r.setRole(el.getAttribute(xrole[lang]));
		
		return r;
	}
		
	private static MetaCollection createCollection(Node n, int lang) throws XavaException {
		Element el = (Element) n;
		MetaCollection c = new MetaCollection();
		c.setName(el.getAttribute(xname[lang]));
		c.setLabel(el.getAttribute(xlabel[lang]));				
		c.setMinimum(ParserUtil.getAttributeInt(el, xminimum[lang]));
		c.setCondition(ParserUtil.getString(el, xcondition[lang]));
		c.setOrder(ParserUtil.getString(el, xorder[lang]));
		c.setMetaReference(createReference(ParserUtil.getElement(el, xreference[lang]), lang));
		c.setMetaCalculator(createCalculator(el, lang));
		fillPostremoveCalculator(el, c, lang);
		return c;
	}	
	
}

