package org.openxava.component;

import org.openxava.mapping.xmlparse.*;
import org.openxava.model.meta.xmlparse.*;
import org.openxava.tab.meta.xmlparse.*;
import org.openxava.util.*;
import org.openxava.util.xmlparse.*;
import org.openxava.view.meta.xmlparse.*;
import org.w3c.dom.*;

/**
 * @author: Javier Paniza
 */
class ComponentParser extends ParserBase {
	
	private MetaComponent component;

	private ComponentParser(String name) {
		super(name + ".xml");		
	}
	
	synchronized public static MetaComponent parse(String name) throws XavaException {		
		ComponentParser parser = new ComponentParser(name);				
		parser.parse();		
		return parser.getComponent();
	}
	
	private void createAggregate() throws XavaException {
		NodeList l = getRoot().getElementsByTagName(xaggregate[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			component.addMetaAggregate(ModelParser.parseAggregate(l.item(i), component.getMetaEntity(), lang));
		}
	}
	
	private void createAggregateMappings() throws XavaException {
		NodeList l = getRoot().getElementsByTagName(xaggregate_mapping[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			component.addAggregateMapping(MappingParser.parseAggregateMapping(this.component, l.item(i), lang));
		}
	}	
	
	private void createEntity() throws XavaException {
		NodeList l = getRoot().getElementsByTagName(xentity[lang]);
		int c = l.getLength();
		if (c != 1) {
			throw new XavaException("component_only_1_entity", component.getName());
		}
		component.setMetaEntity(ModelParser.parseEntity(l.item(0), lang));
	}
	
	private void createViews() throws XavaException {
		NodeList l = getRoot().getElementsByTagName(xview[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			component.addMetaView(ViewParser.parseView(l.item(i), lang));
		}		
	}
	
	private void createTabs() throws XavaException {
		NodeList l = getRoot().getElementsByTagName(xtab[lang]);
		int c = l.getLength();
		for (int i = 0; i < c; i++) {
			component.addMetaTab(TabParser.parseTab(l.item(i), lang));
		}				
	}
	
	private void createEntityMapping() throws XavaException {
		NodeList l = getRoot().getElementsByTagName(xentity_mapping[lang]);
		int c = l.getLength();
		if (c > 1) { 
			throw new XavaException("component_only_1_entity_mapping");
		}
		
		if (c == 1) {
			component.setEntityMapping(MappingParser.parseEntityMapping(l.item(0), lang));
		}
	}
	
	protected void createObjects() throws XavaException {
		lang = "componente".equals(getRoot().getNodeName())?ESPAÑOL:ENGLISH;
		createComponent();
		createEntity();
		createAggregate();
		createViews();
		createTabs();
		createEntityMapping();
		createAggregateMappings();
	}
	
	private void createComponent() throws XavaException {
		this.component = new MetaComponent();
		component.setName(getRoot().getAttribute(xname[lang]));
	}
	
	private MetaComponent getComponent() {
		return component;
	}
	
}