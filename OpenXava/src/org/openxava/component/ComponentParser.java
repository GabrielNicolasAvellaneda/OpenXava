package org.openxava.component;

import java.util.*;

import org.apache.commons.logging.*;
import org.openxava.mapping.xmlparse.*;
import org.openxava.model.meta.*;
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
	
	private static Log log = LogFactory.getLog(ComponentParser.class);
	
	private MetaComponent component;	

	private ComponentParser(String name) {
		super(name + ".xml");		
	}
	
	synchronized public static MetaComponent parse(String name) throws XavaException {		
		ComponentParser parser = new ComponentParser(name);				
		parser.parse();		
		MetaComponent r = parser.getComponent();		
		if (r == null) {
			r = parseAnnotatedClass(name);
		}
		else if (!r.getName().equals(name)) {
			throw new XavaException("component_file_not_match", name, r.getName());
		}
		return r;
	}
	
	private static MetaComponent parseAnnotatedClass(String name) throws XavaException {
		Object parser = null;
		try {
			// At the momment, annotated EJB is parsed only if parser is available in classpath
			parser = Class.forName("org.openxava.ox3.AnnotatedClassParser").newInstance();						
		}
		catch (Exception ex) {
			log.warn(XavaResources.getString("annotated_parser_not_found_warning", name));
			return null;
		}		
		
		try {
			return (MetaComponent) Objects.execute(parser, "parse", String.class, name);			
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			throw new XavaException("ejb3_annotations_parse_error", name);
		}		
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
		component.setMetaEntity(ModelParser.parseEntity(l.item(0), component.getName(), lang));
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
		if (this.component != null) {
			if (XavaPreferences.getInstance().isDuplicateComponentWarnings()) {
				log.warn(XavaResources.getString("trying_to_load_component_twice_warning", this.component.getName()));
			}
			return;
		}
		lang = "componente".equals(getRoot().getNodeName())?ESPANOL:ENGLISH;
		createComponent();
		createEntity();
		createAggregate();
		createViews();
		createTabs();
		createEntityMapping();
		createAggregateMappings();
		fillDefaultFinders();
		setContainerModelToAggregateReference(); 
	}
	
	private void setContainerModelToAggregateReference() throws XavaException { 
		setContainerModelToAggregateReference(component.getMetaEntity());		
		for (Iterator it = component.getMetaAggregates().iterator(); it.hasNext(); ) {
			setContainerModelToAggregateReference((MetaModel) it.next());
		}
	}

	private void setContainerModelToAggregateReference(MetaModel metaModel) throws XavaException { 
		Collection references = metaModel.getMetaReferences();
		if (references.isEmpty()) return;		
		for (Iterator it = references.iterator(); it.hasNext();) {
			MetaReference ref = (MetaReference) it.next();
			if (component.hasMetaAggregate(ref.getReferencedModelName())) {
				MetaModel referencedModel = ref.getMetaModelReferenced();
				if (referencedModel instanceof MetaAggregateForReference) {
					referencedModel.setContainerModelName(metaModel.getName());					
				}
			}
		}
	}

	/**
	 * Add finder for the fields of primary key
	 * @throws XavaException
	 */
	private void fillDefaultFinders()	throws XavaException {
		MetaModel model = component.getMetaEntity();
		if (!model.getMetaReferencesKey().isEmpty()) return;
		StringBuffer finderName = new StringBuffer("by");
		StringBuffer arguments = new StringBuffer(); 
		StringBuffer condition = new StringBuffer();
		
		int i = 0;
		for (Iterator it = model.getMetaPropertiesKey().iterator(); it.hasNext(); i++) {
			MetaProperty property = (MetaProperty) it.next(); 
			finderName.append(Strings.firstUpper(property.getName()));			
			arguments.append(property.getCMPTypeName());
			arguments.append(' ');
			arguments.append(property.getName());
			if (it.hasNext()) arguments.append(',');
			condition.append("${");
			condition.append(property.getName());
			condition.append("} = {");
			condition.append(i);
			condition.append("}");
			if (it.hasNext()) condition.append(" and ");
		}
		MetaFinder finder = new MetaFinder();
		finder.setMetaModel(model);
		finder.setName(finderName.toString());
		if (model.getMetaFinders().contains(finder)) return;
		finder.setArguments(arguments.toString());
		finder.setCondition(condition.toString());
		model.addMetaFinder(finder);
	}
	
	
	private void createComponent() throws XavaException {
		this.component = new MetaComponent();
		component.setName(getRoot().getAttribute(xname[lang]));
	}
	
	private MetaComponent getComponent() {
		return component;
	}
	
}