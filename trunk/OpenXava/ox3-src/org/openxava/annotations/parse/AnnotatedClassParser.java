package org.openxava.annotations.parse;

import java.beans.*;
import java.lang.reflect.*;
import java.util.*;

import javax.persistence.*;

import org.apache.commons.logging.*;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Parameter;
import org.hibernate.usertype.CompositeUserType;
import org.hibernate.validator.*;
import org.openxava.annotations.*;
import org.openxava.component.*;
import org.openxava.converters.typeadapters.*;
import org.openxava.filters.*;
import org.openxava.filters.meta.*;
import org.openxava.jpa.*;
import org.openxava.mapping.*;
import org.openxava.model.meta.*;
import org.openxava.tab.meta.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;
import org.openxava.validators.meta.*;
import org.openxava.view.meta.*;

/**
 * Parse EJB3 Entities (POJOs with JPA annotations) into OpenXava components. <p>
 * 
 * @author Javier Paniza
 */

public class AnnotatedClassParser {
	
	private static Log log = LogFactory.getLog(AnnotatedClassParser.class);
	
	private static Collection<String> managedClassNames;
	private static Collection<String> managedClassPackages;
	
	
	public MetaComponent parse(String name) throws Exception {
		MetaComponent component = new MetaComponent();
		component.setName(name);
		MetaEntity entity = new MetaEntity();
		String className = getClassNameFor(name);
		entity.setPOJOClassName(className);		
		entity.setName(name);
		entity.setAnnotatedEJB3(true);
		component.setMetaEntity(entity);
		component.setPackageName(Strings.noLastTokenWithoutLastDelim(className, "."));		
		
		Class pojoClass = entity.getPOJOClass();
		if (!pojoClass.isAnnotationPresent(Entity.class)) {
			component.setTransient(true);
		}
		
		if (pojoClass.isAnnotationPresent(IdClass.class)) {
			IdClass idClass = (IdClass) pojoClass.getAnnotation(IdClass.class);
			entity.setPOJOKeyClass(idClass.value());
		}
		
		
		EntityMapping mapping = new EntityMapping();
		mapping.setTable(getTable(name, pojoClass));
		component.setEntityMapping(mapping);
		
		// View
		parseViews(component, pojoClass);

		// Members				
		parseMembers(entity, pojoClass, mapping, null);
												
		// Tab
		parseTabs(component, pojoClass);
		
		// Other model level annotations
		processAnnotations(entity, pojoClass);
		
		return component;
	}


	private String getTable(String modelName, Class pojoClass) {
		Table table = (Table) pojoClass.getAnnotation(Table.class);
		if (table != null) {
			String tableName = Is.emptyString(table.name())?modelName:table.name();
			return Is.emptyString(table.schema())?tableName:table.schema() + "." + tableName;			
		}
		else {
			return modelName;
		}
	}

	
	private void parseMembers(MetaModel model, Class pojoClass, ModelMapping mapping, String embedded) throws Exception {
		// Using declared fields in order to preserve the order in source code
		Map<String, PropertyDescriptor> propertyDescriptors = getPropertyDescriptors(pojoClass);
		for (Field f: pojoClass.getDeclaredFields()) {
			PropertyDescriptor pd = propertyDescriptors.get(f.getName());
			if (pd == null) continue;
			addMember(model, mapping, pd, f, embedded);
			propertyDescriptors.remove(f.getName());
		}
		
		// Loop over methods in order to preserve the order in source code
		for (Method m: pojoClass.getMethods()) {			
			String propertyName = null;
			if (m.getName().startsWith("get")) {
				propertyName = Strings.firstLower(m.getName().substring(3));
			}
			else if (m.getName().startsWith("is")) {
				propertyName = Strings.firstLower(m.getName().substring(2));
			}
			else continue;
			PropertyDescriptor pd = propertyDescriptors.get(propertyName);
			if (pd == null) continue;
			addMember(model, mapping, pd, null, embedded);
		}
	}
	
	private void addMember(MetaModel model, ModelMapping mapping, PropertyDescriptor pd, Field f, String embedded) throws Exception {
		if (pd.getName().equals("class") || pd.getPropertyType() == null) return;
		if (isReference(pd, f)) addReference(model, mapping, pd, f, embedded);
		else if (Collection.class.isAssignableFrom(pd.getPropertyType())) addCollection(model, mapping, pd, f);
		else if (pd.getPropertyType().isAnnotationPresent(Embeddable.class)) addEmbeddable(model, mapping, pd, f);
		else addProperty(model, mapping, pd, f, embedded);
	}
	
	private boolean isReference(PropertyDescriptor pd, Field f) {
		return (f != null && f.isAnnotationPresent(ManyToOne.class)) || 
			pd.getReadMethod().isAnnotationPresent(ManyToOne.class) ||
			(f != null && f.isAnnotationPresent(OneToOne.class)) || 
			pd.getReadMethod().isAnnotationPresent(OneToOne.class);
	}
	
	private void addCollection(MetaModel model, ModelMapping mapping, PropertyDescriptor pd, Field field) throws Exception { 		
		if (!(pd.getReadMethod().getGenericReturnType() instanceof ParameterizedType)) {
			log.warn(XavaResources.getString("collection_must_be_parametrized", pd.getName(), model.getName()));
			return;			
		}
		
		MetaCollection collection = new MetaCollection();
		collection.setName(pd.getName());
		MetaReference ref = new MetaReference();
		ref.setReferencedModelName( ((Class) (((ParameterizedType) pd.getReadMethod().getGenericReturnType()).getActualTypeArguments()[0])).getSimpleName() );
		
		collection.setMetaReference(ref);
		model.addMetaCollection(collection);
		
		collection.setMetaCalculator(new MetaCalculator()); // This may be annuled by processAnnotations (below)
		
		processAnnotations(collection, pd.getReadMethod());
		processAnnotations(collection, field);				
				
	}
	
	private void addEmbeddable(MetaModel model, ModelMapping mapping, PropertyDescriptor pd, Field field) throws Exception {
		String modelName = pd.getPropertyType().getSimpleName();
		if (!model.getMetaComponent().hasMetaAggregate(modelName)) {
			MetaAggregateForReference metaAggregate = new MetaAggregateForReference();			
			metaAggregate.setName(modelName);
			metaAggregate.setBeanClass(pd.getPropertyType().getName());
			metaAggregate.setPOJOClassName(pd.getPropertyType().getName());
			model.getMetaComponent().addMetaAggregate(metaAggregate);
			parseViews(model.getMetaComponent(), pd.getPropertyType(), modelName);
			parseMembers(metaAggregate, pd.getPropertyType(), mapping, pd.getName());
		}				
		addReference(model, mapping, pd, field, pd.getName());
	}
	
	private void addAggregateForCollection(MetaModel model, String typeName) throws Exception {
		Class type = Class.forName(typeName);
		String modelName = type.getSimpleName();
		if (!model.getMetaComponent().hasMetaAggregate(modelName)) {
			MetaAggregateForCollection metaAggregate = new MetaAggregateForCollection();
			metaAggregate.setContainerModelName(model.getName());
			metaAggregate.setName(modelName);
			metaAggregate.setPOJOClassName(type.getName());
			Class pojoClass = metaAggregate.getPOJOClass();
			if (pojoClass.isAnnotationPresent(IdClass.class)) {
				IdClass idClass = (IdClass) pojoClass.getAnnotation(IdClass.class);
				metaAggregate.setPOJOKeyClass(idClass.value());
			}
			model.getMetaComponent().addMetaAggregate(metaAggregate);
			parseViews(model.getMetaComponent(), type, modelName);
			AggregateMapping mapping = new AggregateMapping();
			mapping.setModelName(modelName);
			mapping.setTable(getTable(modelName, pojoClass));
			model.getMetaComponent().addAggregateMapping(mapping);
			parseMembers(metaAggregate, type, mapping, null);
			processAnnotations(metaAggregate, type);			
		}						
	}
			
	private void addReference(MetaModel model, ModelMapping mapping, PropertyDescriptor pd, Field field, String embedded) throws XavaException {
		MetaReference ref = new MetaReference();
		ref.setName(pd.getName());
		ref.setReferencedModelName(pd.getPropertyType().getSimpleName());
		model.addMetaReference(ref);
		processAnnotations(ref, pd.getReadMethod());
		processAnnotations(ref, field);
		if (!ref.isRequired() && ref.isKey() &&
			!(model instanceof MetaAggregateForCollection &&  
			Strings.firstLower(model.getContainerModelName()).equals(ref.getName())))  
		{
			ref.setRequired(true);			
		}		
		
		// The mapping part
		
		MetaModel metaModelReferenced = null;						
		// Self reference
		if (ref.getReferencedModelName().equals(model.getName())) 
			metaModelReferenced = model; 
		// Cyclical reference
		else if (ref.getReferencedModelName().equals(ref.getMetaModel().getContainerModelName())) 
			metaModelReferenced = ref.getMetaModel().getMetaModelContainer();
		// Other cases
		else metaModelReferenced = ref.getMetaModelReferenced();
		if (mapping != null && !(metaModelReferenced instanceof MetaAggregateForReference)) { 
			ReferenceMapping refMapping = new ReferenceMapping();
			refMapping.setReference(embedded==null?pd.getName():embedded + "_" + pd.getName());			
			
			for (Object oreferencedModelPropertyName: metaModelReferenced.getAllKeyPropertiesNames()) {
				String column = null;
				String referencedModelPropertyName = (String) oreferencedModelPropertyName;								
				if (field != null && field.isAnnotationPresent(JoinColumn.class)) {
					JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);			
					column = joinColumn.name();
				}
				else if (field != null && field.isAnnotationPresent(JoinColumns.class)) {
					column = getColumnFor(field.getAnnotation(JoinColumns.class), metaModelReferenced, referencedModelPropertyName); 
				}
				else if (pd.getReadMethod().isAnnotationPresent(JoinColumn.class)) {
					JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
					column = joinColumn.name();
				}
				else if (pd.getReadMethod().isAnnotationPresent(JoinColumns.class)) {
					column = getColumnFor(pd.getReadMethod().getAnnotation(JoinColumns.class), metaModelReferenced, referencedModelPropertyName); 
				}
				
				if (Is.emptyString(column)) {			
					column = pd.getName() + "_" + metaModelReferenced.getMapping().getColumn(referencedModelPropertyName); 
				}		
				
				ReferenceMappingDetail detail = new ReferenceMappingDetail();
				detail.setReferencedModelProperty(referencedModelPropertyName);
				detail.setColumn(column);				
				refMapping.addDetail(detail);				
			}					
			mapping.addReferenceMapping(refMapping);					
		}
	}
	
	private String getColumnFor(JoinColumns joinColumns, MetaModel referencedModel, String referencedModelProperty) throws XavaException {
		String referencedColumn = referencedModel.getMapping().getColumn(referencedModelProperty);
		for (JoinColumn joinColumn: joinColumns.value()) {
			if (referencedColumn.trim().equalsIgnoreCase(joinColumn.referencedColumnName().trim())) {
				return joinColumn.name();
			}
		}
		return null;
	}


	private void parseViews(MetaComponent component, Class pojoClass) throws XavaException {
		parseViews(component, pojoClass, null);
	}
	
	private void parseViews(MetaComponent component, Class pojoClass, String modelName) throws XavaException {
		if (pojoClass.isAnnotationPresent(View.class)) {
			View view = (View) pojoClass.getAnnotation(View.class);
			addView(component, view, modelName);
		}
		if (pojoClass.isAnnotationPresent(Views.class)) {
			Views views = (Views) pojoClass.getAnnotation(Views.class);
			for (View view: views.value()) {
				addView(component, view, modelName);
			}
		}
		
		// For force create and add a view by default if it does not exist
		if (modelName == null) component.getMetaEntity().getMetaViewByDefault();
		else component.getMetaAggregate(modelName).getMetaViewByDefault();
	}
	
	private void addView(MetaComponent component, View view, String modelName) throws XavaException {
		MetaView metaView = new MetaView();
		metaView.setName(view.name());
		if (modelName != null) {
			metaView.setModelName(modelName);
		}		
		if (Is.empty(view.members())) metaView.setMembersNames("*");
		else addMembersToView(null, null, metaView, new StringTokenizer(view.members(), ";,{}[]#", true));
		component.addMetaView(metaView);
	}
	
	private String addMembersToView(String sectionName, String groupName, MetaView metaView, StringTokenizer st) throws XavaException {
		boolean alignedByColumns = false;
		StringBuffer members = new StringBuffer();
		boolean stHasMoreTokens = st.hasMoreTokens();
		String token = stHasMoreTokens?st.nextToken().trim():null;		
		while (stHasMoreTokens) {	
			stHasMoreTokens = st.hasMoreTokens();			
			String nextToken = stHasMoreTokens?st.nextToken().trim():"";
			if (token.equals("}")) {				
				metaView.setMembersNamesNotResetSections(members.toString());
				metaView.setAlignedByColumns(alignedByColumns);
				return nextToken;
			}
			else if (nextToken.equals("{")) {				
				String nestedSection = token;				 
				MetaView section = metaView.addSection(nestedSection, null, "", false); 
				nextToken = addMembersToView(nestedSection, null, section, st);
				if (",;".indexOf(nextToken) < 0) members.append(';');
			}		
			else if (token.equals("]")) {										
				metaView.addMetaGroup(groupName, null, members.toString(), alignedByColumns);	
				return nextToken;
			}
			else if (nextToken.equals("[")) {				
				String nestedGroup = token;
				members.append("__GROUP__" + nestedGroup); 
				nextToken = addMembersToView(null, nestedGroup, metaView, st); 
				if (",;".indexOf(nextToken) < 0) members.append(',');
			}
			else {
				if (token.endsWith("()")) { // An action
					members.append("__ACTION__");
					String action = token.substring(0, token.length() - "()".length());
					members.append(action);
				}
				else if (token.endsWith("(ALWAYS)")) { // An always present action
					members.append("__ACTION__AE__");
					String action = token.substring(0, token.length() - "(ALWAYS)".length());
					members.append(action);
				}
				else if (token.equals("#")) {
					alignedByColumns = true;
				}
				else {	
					members.append(token); // A conventional member
				}
			}	
			token = nextToken;
		}
		if (groupName != null) throw new XavaException("group_unclosed", groupName); 		
		if (sectionName != null) throw new XavaException("section_unclosed", sectionName);		
		metaView.setMembersNamesNotResetSections(members.toString());
		return null;
	}

	private void parseTabs(MetaComponent component, Class pojoClass) throws XavaException {
		if (pojoClass.isAnnotationPresent(Tab.class)) {
			Tab listMode = (Tab) pojoClass.getAnnotation(Tab.class);
			addTab(component, listMode);		
		}
		if (pojoClass.isAnnotationPresent(Tabs.class)) {
			Tabs listModes = (Tabs) pojoClass.getAnnotation(Tabs.class);
			for (Tab listMode: listModes.value()) {
				addTab(component, listMode);
			}
		}		
	}

	private void addTab(MetaComponent component, Tab listMode) throws XavaException {
		MetaTab tab = new MetaTab();
		tab.setName(listMode.name());
		if (Is.emptyString(listMode.properties())) {
			tab.setDefaultPropertiesNames("*");
		}
		else {
			tab.setDefaultPropertiesNames(listMode.properties());
		}
		tab.setBaseCondition(listMode.baseCondition());
		tab.setDefaultOrder(listMode.defaultOrder());
		if (!listMode.filter().equals(VoidFilter.class)) {
			MetaFilter metaFilter = new MetaFilter();
			metaFilter.setClassName(listMode.filter().getName());
			tab.setMetaFilter(metaFilter);
		}
		
		for (RowStyle rowStyle: listMode.rowStyles()) {
			MetaRowStyle metaRowStyle = new MetaRowStyle();
			metaRowStyle.setStyle(rowStyle.style());
			metaRowStyle.setProperty(rowStyle.property());
			metaRowStyle.setValue(rowStyle.value());			
			tab.addMetaRowStyle(metaRowStyle);
		}
		
		component.addMetaTab(tab);
	}

	private void addProperty(MetaModel model, ModelMapping mapping, PropertyDescriptor pd, Field field, String embedded) throws Exception {		
		MetaProperty property = new MetaProperty();
		property.setName(pd.getName());		
		
		if (pd.getPropertyType().isEnum()) {
			for (Object validValue: pd.getPropertyType().getEnumConstants()) {
				property.addValidValue(validValue);
			}
		}
		property.setTypeName(pd.getPropertyType().getName());		
		model.addMetaProperty(property);
		
		processAnnotations(property, pd.getReadMethod());
		processAnnotations(property, field);
		if (!property.isRequired() && property.isKey()) {
			if (!(
					(field != null && field.isAnnotationPresent(GeneratedValue.class)) ||							
					pd.getReadMethod().isAnnotationPresent(GeneratedValue.class)
				)
				&&
				!(
					(field != null && field.isAnnotationPresent(Hidden.class)) ||							
					pd.getReadMethod().isAnnotationPresent(Hidden.class)
				)				
			)
			{	
				property.setRequired(true);
			}
		}
	
		if (field == null && pd.getWriteMethod() == null) {
			// It's calculated
			property.setReadOnly(true);
			MetaCalculator metaCalculator = new MetaCalculator();
			metaCalculator.setClassName(org.openxava.calculators.ModelPropertyCalculator.class.getName());
			MetaSet metaSet = new MetaSet();
			metaSet.setPropertyName("property");
			metaSet.setValue(property.getName());
			metaCalculator.addMetaSet(metaSet);
			if (pd.getReadMethod().isAnnotationPresent(Depends.class)) {
				Depends depends = pd.getReadMethod().getAnnotation(Depends.class);
				for (Object propertyFrom: Strings.toCollection(depends.value())) {
					MetaSet metaSetPropertyFrom = new MetaSet();
					metaSetPropertyFrom.setPropertyName("valueOfDependsProperty");
					metaSetPropertyFrom.setPropertyNameFrom((String) propertyFrom);
					metaCalculator.addMetaSet(metaSetPropertyFrom);
				}
			}			
			property.setMetaCalculator(metaCalculator);
		}
		
		
		
		// The mapping part
		if (mapping != null && field != null) { 
			PropertyMapping pMapping = new PropertyMapping(mapping);			
			pMapping.setProperty(embedded==null?pd.getName():embedded + "_" + pd.getName());
			// Column
			if (field != null && field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);
				pMapping.setColumn(column.name());
			}
			else if (pd.getReadMethod().isAnnotationPresent(Column.class)) {
				Column column = pd.getReadMethod().getAnnotation(Column.class);
				pMapping.setColumn(column.name());
			}
			if (Is.emptyString(pMapping.getColumn())) {
				pMapping.setColumn(pd.getName());
			}		
			// Converter (from Hibernate Type)
			if (field != null && field.isAnnotationPresent(Type.class)) {				
				Type type = field.getAnnotation(Type.class);
				addConverter(pMapping, type, field.getAnnotation(Columns.class));
			}
			else if (pd.getReadMethod().isAnnotationPresent(Type.class)) {
				Type type = pd.getReadMethod().getAnnotation(Type.class);
				addConverter(pMapping, type, pd.getReadMethod().getAnnotation(Columns.class));
			}
			else if (property.hasValidValues()) { 
				// To convert the parameters sent for filtering in the tabs
				pMapping.setConverterClassName(EnumIntConverter.class.getName()); 
			}
			else {
				pMapping.setDefaultConverter();
			}		
			
			mapping.addPropertyMapping(pMapping);
		}		
	}
	
	
	private void addConverter(PropertyMapping mapping, Type type, Columns columns) throws Exception {					
		if (CompositeUserType.class.isAssignableFrom(Class.forName(type.type()))) {				
			mapping.setMultipleConverterClassName(HibernateCompositeTypeConverter.class.getName());
			
			MetaSet typeMetaSet = new MetaSet(); 
			typeMetaSet.setPropertyName("type");
			typeMetaSet.setValue(type.type());
			mapping.addMetaSet(typeMetaSet);
			
			if (columns != null) {				
				MetaSet valueCountMetaSet = new MetaSet();
				valueCountMetaSet.setPropertyName("valuesCount");
				valueCountMetaSet.setValue(String.valueOf(columns.columns().length));
				mapping.addMetaSet(valueCountMetaSet);										

				int valueIndex = 0;
				for (Column column: columns.columns()) {
					CmpField cmp = new CmpField();
					cmp.setConverterPropertyName("value" + valueIndex++);
					cmp.setColumn(column.name());
					mapping.addCmpField(cmp);						
				}
			}	
			
			mapping.setColumn(""); 
		}
		else {				
			mapping.setConverterClassName(HibernateTypeConverter.class.getName());
			MetaSet metaSet = new MetaSet();
			metaSet.setPropertyName("type");
			metaSet.setValue(type.type());
			mapping.addMetaSet(metaSet);
		}
		
		// Parameters
		if (type.parameters().length > 0) {
			StringBuffer parameters = new StringBuffer();
			for (Parameter parameter: type.parameters()) {
				parameters.append(parameter.name());
				parameters.append('=');
				parameters.append('"');
				parameters.append(parameter.value());
				parameters.append('"');
				parameters.append(',');
			}
			
			MetaSet parameterMetaSet = new MetaSet();
			parameterMetaSet.setPropertyName("parameters");
			parameterMetaSet.setValue(parameters.toString());					
			mapping.addMetaSet(parameterMetaSet);
		}
	}

	private void processAnnotations(MetaProperty property, AnnotatedElement element) throws XavaException {
		if (element == null) return;
		// key
		if (element.isAnnotationPresent(Id.class)) {
			property.setKey(true);
		}
		// size
		if (element.isAnnotationPresent(Max.class)) {
			Max max = element.getAnnotation(Max.class);			
			property.setSize((int) (Math.log10(max.value()) + 1));
		}
		else if (element.isAnnotationPresent(Length.class)) {
			Length length = element.getAnnotation(Length.class);			
			property.setSize(length.max());
		}
		else if (element.isAnnotationPresent(Size.class)) {
			Size size = element.getAnnotation(Size.class);			
			property.setSize(size.max());
		}
		else if (element.isAnnotationPresent(Column.class)) {
			Column column = element.getAnnotation(Column.class);			
			property.setSize(column.length());
		}
		else if (element.isAnnotationPresent(Digits.class)) {
			Digits digits = element.getAnnotation(Digits.class);
			if (digits.fractionalDigits() > 0) {
				property.setSize(digits.integerDigits() + 1 + digits.fractionalDigits());
				property.setScale(digits.fractionalDigits());
			}
			else {
				property.setSize(digits.integerDigits());
			}
		}
				
		// required
		if (element.isAnnotationPresent(Required.class)) {						
			property.setRequired(true);
		}
		else if (element.isAnnotationPresent(Min.class)) {
			Min min = element.getAnnotation(Min.class);
			if (min.value() > 0) property.setRequired(true);			
		}
		else if (element.isAnnotationPresent(Range.class)) {
			Range range = element.getAnnotation(Range.class);
			if (range.min() > 0) property.setRequired(true);			
		}
		else if (element.isAnnotationPresent(Length.class)) {
			Length length = element.getAnnotation(Length.class);
			if (length.min() > 0) property.setRequired(true);			
		}							
		
		// hidden
		if (element.isAnnotationPresent(Hidden.class)) {  						
			property.setHidden(true);
		}
		
		// transient
		if (element.isAnnotationPresent(Transient.class)) {  						
			property.setTransient(true);
		}
		
		// stereotype
		if (element.isAnnotationPresent(Stereotype.class)) {
			Stereotype stereotype = element.getAnnotation(Stereotype.class);
			property.setStereotype(stereotype.value());
		}
		
		// default value calculator
		if (element.isAnnotationPresent(DefaultValueCalculator.class)) {
			DefaultValueCalculator calculator = element.getAnnotation(DefaultValueCalculator.class);
			MetaCalculator metaCalculator = new MetaCalculator();
			metaCalculator.setClassName(calculator.value().getName());
			for (PropertyValue put: calculator.properties()) {
				metaCalculator.addMetaSet(toMetaSet(put));
			}
			property.setMetaCalculatorDefaultValue(metaCalculator);
		}
		
		// generated value
		if (element.isAnnotationPresent(GeneratedValue.class)) {
			// The MetaCalculator does not have class because is only for having a 
			// calculator on-create
			MetaCalculator metaCalculator = new MetaCalculator();
			metaCalculator.setOnCreate(true);
			property.setMetaCalculatorDefaultValue(metaCalculator);
			if (element.isAnnotationPresent(DefaultValueCalculator.class)) {
				log.warn("default_value_calculator_generated_value_incompatible");
			}
		}
		
		// validator
		if (element.isAnnotationPresent(PropertyValidator.class)) {			
			PropertyValidator validator = element.getAnnotation(PropertyValidator.class);
			addPropertyValidator(property, validator);
		}
		if (element.isAnnotationPresent(PropertyValidators.class)) {
			PropertyValidator [] validators = element.getAnnotation(PropertyValidators.class).value();
			for (PropertyValidator validator: validators) {				
				addPropertyValidator(property, validator);				
			}
		}		
						
		// for View
		for (Object oMetaView: property.getMetaModel().getMetaViews()) {
			MetaView metaView = (MetaView) oMetaView;			
			MetaPropertyView propertyView = new MetaPropertyView();
			propertyView.setPropertyName(property.getName());
			boolean mustAddMetaView = false;
			
			// Action
			mustAddMetaView = addAction(element, metaView, propertyView, mustAddMetaView);			
		
			// OnChange
			if (element.isAnnotationPresent(OnChange.class)) {
				OnChange onChange = element.getAnnotation(OnChange.class);
				if (isForView(metaView, onChange.forViews(), onChange.notForViews())) {
					propertyView.setOnChangeActionClassName(onChange.value().getName());
					mustAddMetaView = true;				
				}
			}
			if (element.isAnnotationPresent(OnChanges.class)) {
				OnChange [] onChanges = element.getAnnotation(OnChanges.class).value();				
				for (OnChange onChange: onChanges) {
					if (isForView(metaView, onChange.forViews(), onChange.notForViews())) {
						propertyView.setOnChangeActionClassName(onChange.value().getName());
						mustAddMetaView = true;				
					}
				}					
			}
		
			// LabelFormat
			if (element.isAnnotationPresent(LabelFormat.class)) {
				LabelFormat labelFormat = element.getAnnotation(LabelFormat.class);
				if (isForView(metaView, labelFormat.forViews(), labelFormat.notForViews())) {
					propertyView.setLabelFormat(labelFormat.value().ordinal());
					mustAddMetaView = true;				
				}
			}					
			if (element.isAnnotationPresent(LabelFormats.class)) {
				LabelFormat [] labelFormats = element.getAnnotation(LabelFormats.class).value();				
				for (LabelFormat labelFormat: labelFormats) {
					if (isForView(metaView, labelFormat.forViews(), labelFormat.notForViews())) {
						propertyView.setLabelFormat(labelFormat.value().ordinal());
						mustAddMetaView = true;
					}
				}					
			}
			
			// ReadOnly
			if (element.isAnnotationPresent(ReadOnly.class)) {
				ReadOnly readOnly = element.getAnnotation(ReadOnly.class);
				if (isForView(metaView, readOnly.forViews(), readOnly.notForViews())) {
					propertyView.setReadOnly(true);
					mustAddMetaView = true;				
				}
			}					
			
			// Editor
			if (element.isAnnotationPresent(Editor.class)) {
				Editor editor = element.getAnnotation(Editor.class);
				if (isForView(metaView, editor.forViews(), editor.notForViews())) {
					propertyView.setEditor(editor.value());
					mustAddMetaView = true;				
				}
			}
			if (element.isAnnotationPresent(Editors.class)) {
				Editor [] editores = element.getAnnotation(Editors.class).value();				
				for (Editor editor: editores) {
					if (isForView(metaView, editor.forViews(), editor.notForViews())) {
						propertyView.setEditor(editor.value());
						mustAddMetaView = true;				
					}
				}					
			}
			
			// DisplaySize
			if (element.isAnnotationPresent(DisplaySize.class)) {
				DisplaySize displaySize = element.getAnnotation(DisplaySize.class);
				if (isForView(metaView, displaySize.forViews(), displaySize.notForViews())) {
					propertyView.setDisplaySize(displaySize.value());
					mustAddMetaView = true;				
				}
			}
			if (element.isAnnotationPresent(DisplaySizes.class)) {
				DisplaySize [] displaySizes = element.getAnnotation(DisplaySizes.class).value();				
				for (DisplaySize displaySize: displaySizes) {
					if (isForView(metaView, displaySize.forViews(), displaySize.notForViews())) {
						propertyView.setDisplaySize(displaySize.value());
						mustAddMetaView = true;				
					}
				}					
			}			
			
			
						
			if (mustAddMetaView) {				
				metaView.addMetaViewProperty(propertyView);
			}

		}
		
		
		// Not applicable
		if (element.isAnnotationPresent(ListProperties.class)) {
			notApply(property.getName(), ListProperties.class, "collections");
		}
		if (element.isAnnotationPresent(ListsProperties.class)) {
			notApply(property.getName(), ListsProperties.class, "collections");
		}		
		if (element.isAnnotationPresent(DescriptionsList.class)) {
			notApply(property.getName(), DescriptionsList.class, "references");
		}
		if (element.isAnnotationPresent(DescriptionsLists.class)) {
			notApply(property.getName(), DescriptionsLists.class, "references");
		}		
		if (element.isAnnotationPresent(CollectionView.class)) {
			notApply(property.getName(), CollectionView.class, "collections");
		}
		if (element.isAnnotationPresent(CollectionViews.class)) {
			notApply(property.getName(), CollectionViews.class, "collections");
		}		
		if (element.isAnnotationPresent(ListAction.class)) {
			notApply(property.getName(), ListAction.class, "collections");
		}
		if (element.isAnnotationPresent(ListActions.class)) {
			notApply(property.getName(), ListActions.class, "collections");
		}				
		if (element.isAnnotationPresent(EditOnly.class)) {
			notApply(property.getName(), EditOnly.class, "collections");
		}
		if (element.isAnnotationPresent(SearchAction.class)) {
			notApply(property.getName(), SearchAction.class, "references");
		}
		if (element.isAnnotationPresent(SearchActions.class)) {
			notApply(property.getName(), SearchActions.class, "references");
		}		
		if (element.isAnnotationPresent(ReferenceView.class)) {
			notApply(property.getName(), ReferenceView.class, "references");
		}
		if (element.isAnnotationPresent(ReferenceViews.class)) {
			notApply(property.getName(), ReferenceViews.class, "references");
		}												
		if (element.isAnnotationPresent(NoFrame.class)) {
			notApply(property.getName(), NoFrame.class, "references");
		}
		if (element.isAnnotationPresent(NoCreate.class)) {
			notApply(property.getName(), NoCreate.class, "references & collections");
		}
		if (element.isAnnotationPresent(NoModify.class)) {
			notApply(property.getName(), NoModify.class, "references & collections");
		}
		if (element.isAnnotationPresent(AsEmbedded.class)) {
			notApply(property.getName(), AsEmbedded.class, "references & collections");
		}
		if (element.isAnnotationPresent(EditAction.class)) {
			notApply(property.getName(), EditAction.class, "collections");
		}				
		if (element.isAnnotationPresent(EditActions.class)) {
			notApply(property.getName(), EditActions.class, "collections");
		}				
		if (element.isAnnotationPresent(DetailAction.class)) {
			notApply(property.getName(), DetailAction.class, "collections");
		}
		if (element.isAnnotationPresent(DetailActions.class)) {
			notApply(property.getName(), DetailActions.class, "collections");
		}
		if (element.isAnnotationPresent(ViewAction.class)) {
			notApply(property.getName(), ViewAction.class, "collections");
		}								
		if (element.isAnnotationPresent(ViewActions.class)) {
			notApply(property.getName(), ViewActions.class, "collections");
		}						
		if (element.isAnnotationPresent(NoSearch.class)) {
			notApply(property.getName(), NoSearch.class, "references");
		}
		if (element.isAnnotationPresent(Condition.class)) {
			notApply(property.getName(), Condition.class, "collections");
		}
		if (element.isAnnotationPresent(HideDetailAction.class)) {
			notApply(property.getName(), HideDetailAction.class, "collections");
		}
		if (element.isAnnotationPresent(HideDetailActions.class)) {
			notApply(property.getName(), HideDetailActions.class, "collections");
		}
		if (element.isAnnotationPresent(NewAction.class)) {
			notApply(property.getName(), NewAction.class, "collections");
		}								
		if (element.isAnnotationPresent(NewActions.class)) {
			notApply(property.getName(), NewActions.class, "collections");
		}
		if (element.isAnnotationPresent(RemoveAction.class)) {
			notApply(property.getName(), RemoveAction.class, "collections");
		}								
		if (element.isAnnotationPresent(RemoveActions.class)) {
			notApply(property.getName(), RemoveActions.class, "collections");
		}								
		if (element.isAnnotationPresent(RemoveSelectedAction.class)) {
			notApply(property.getName(), RemoveSelectedAction.class, "collections");
		}								
		if (element.isAnnotationPresent(RemoveSelectedActions.class)) {
			notApply(property.getName(), RemoveSelectedActions.class, "collections");
		}
		if (element.isAnnotationPresent(SaveAction.class)) {
			notApply(property.getName(), SaveAction.class, "collections");
		}
		if (element.isAnnotationPresent(SaveActions.class)) {
			notApply(property.getName(), SaveActions.class, "collections");
		}														
		
	}

	private void processAnnotations(MetaCollection collection, AnnotatedElement element) throws Exception {
		if (element == null) return;		
		if (element.isAnnotationPresent(OneToMany.class)) {
			collection.setMetaCalculator(null);			
			OneToMany oneToMany = element.getAnnotation(OneToMany.class);
			collection.getMetaReference().setRole(oneToMany.mappedBy());
			if (isCascade(oneToMany.cascade())) {				
				addAggregateForCollection(collection.getMetaModel(), getClassNameFor(collection.getMetaReference().getReferencedModelName()));
			}
		}
		else if (element.isAnnotationPresent(Condition.class)){			
			collection.setMetaCalculator(null); 
		}		
		// ManyToMany collections are processed as calculated one
		
		if (element.isAnnotationPresent(Size.class)) {
			Size size = element.getAnnotation(Size.class);
			collection.setMinimum(size.min());
			collection.setMaximum(size.max());
		}
		
		if (element.isAnnotationPresent(Condition.class)) {
			Condition condition = element.getAnnotation(Condition.class);
			collection.setCondition(condition.value());
		}
		
		if (element.isAnnotationPresent(OrderBy.class)) {
			OrderBy orderBy = element.getAnnotation(OrderBy.class);
			collection.setOrder(wrapWithDollars(orderBy.value()));
		}		
					
		for (Object oMetaView: collection.getMetaModel().getMetaViews()) {
			MetaView metaView = (MetaView) oMetaView;				
			MetaCollectionView collectionView = new MetaCollectionView();
			collectionView.setCollectionName(collection.getName());
			boolean mustAddMetaView = false;
			
			// ListProperties
			if (element.isAnnotationPresent(ListProperties.class)) {
				ListProperties listProperties = element.getAnnotation(ListProperties.class);
				if (isForView(metaView, listProperties.forViews(), listProperties.notForViews())) {
					collectionView.setPropertiesList(listProperties.value());
					mustAddMetaView = true;
				}
			}
			if (element.isAnnotationPresent(ListsProperties.class)) {
				ListProperties [] listsProperties = element.getAnnotation(ListsProperties.class).value();				
				for (ListProperties listProperties: listsProperties) {
					if (isForView(metaView, listProperties.forViews(), listProperties.notForViews())) {
						collectionView.setPropertiesList(listProperties.value());
						mustAddMetaView = true;
					}					
				}
			}
			
			// CollectionView
			if (element.isAnnotationPresent(CollectionView.class)) {
				CollectionView view = element.getAnnotation(CollectionView.class);
				if (isForView(metaView, view.forViews(), view.notForViews())) {
					collectionView.setViewName(view.value());
					mustAddMetaView = true;
				}
			}
			if (element.isAnnotationPresent(CollectionViews.class)) {
				CollectionView [] views = element.getAnnotation(CollectionViews.class).value();				
				for (CollectionView view: views) {
					if (isForView(metaView, view.forViews(), view.notForViews())) {
						collectionView.setViewName(view.value());
						mustAddMetaView = true;
					}
				}
			}
			
			// ListAction
			if (element.isAnnotationPresent(ListAction.class)) {
				ListAction action = element.getAnnotation(ListAction.class);
				if (isForView(metaView, action.forViews(), action.notForViews())) {
					collectionView.addActionListName(action.value());
					mustAddMetaView = true;				
				}
			}
			if (element.isAnnotationPresent(ListActions.class)) {
				ListAction [] actions = element.getAnnotation(ListActions.class).value();
				for (ListAction action: actions) {				
					if (isForView(metaView, action.forViews(), action.notForViews())) {
						collectionView.addActionListName(action.value());
						mustAddMetaView = true;
					}
				}				
			}
			
			// DetailAction
			if (element.isAnnotationPresent(DetailAction.class)) {
				DetailAction action = element.getAnnotation(DetailAction.class);
				if (isForView(metaView, action.forViews(), action.notForViews())) {
					collectionView.addActionDetailName(action.value());
					mustAddMetaView = true;				
				}
			}
			if (element.isAnnotationPresent(DetailActions.class)) {
				DetailAction [] actions = element.getAnnotation(DetailActions.class).value();
				for (DetailAction action: actions) {				
					if (isForView(metaView, action.forViews(), action.notForViews())) {
						collectionView.addActionDetailName(action.value());
						mustAddMetaView = true;
					}
				}				
			}			
			
			// NewAction
			if (element.isAnnotationPresent(NewAction.class)) {
				NewAction action = element.getAnnotation(NewAction.class);
				if (isForView(metaView, action.forViews(), action.notForViews())) {
					collectionView.setNewActionName(action.value());
					mustAddMetaView = true;				
				}
			}
			if (element.isAnnotationPresent(NewActions.class)) {
				NewAction [] actions = element.getAnnotation(NewActions.class).value();
				for (NewAction action: actions) {				
					if (isForView(metaView, action.forViews(), action.notForViews())) {
						if (Is.emptyString(collectionView.getNewActionName())) {
							collectionView.setNewActionName(action.value());
							mustAddMetaView = true;				
						}
						else {
							duplicateAnnotationForView(collection.getName(), NewAction.class, metaView.getName());
						}
					}
				}				
			}
			
			// SaveAction
			if (element.isAnnotationPresent(SaveAction.class)) {
				SaveAction action = element.getAnnotation(SaveAction.class);
				if (isForView(metaView, action.forViews(), action.notForViews())) {
					collectionView.setSaveActionName(action.value());
					mustAddMetaView = true;				
				}
			}
			if (element.isAnnotationPresent(SaveActions.class)) {
				SaveAction [] actions = element.getAnnotation(SaveActions.class).value();
				for (SaveAction action: actions) {				
					if (isForView(metaView, action.forViews(), action.notForViews())) {
						if (Is.emptyString(collectionView.getSaveActionName())) {
							collectionView.setSaveActionName(action.value());
							mustAddMetaView = true;				
						}
						else {
							duplicateAnnotationForView(collection.getName(), SaveAction.class, metaView.getName());
						}
					}
				}				
			}			
			
			// HideDetailAction
			if (element.isAnnotationPresent(HideDetailAction.class)) {
				HideDetailAction action = element.getAnnotation(HideDetailAction.class);
				if (isForView(metaView, action.forViews(), action.notForViews())) {
					collectionView.setHideActionName(action.value());
					mustAddMetaView = true;				
				}
			}
			if (element.isAnnotationPresent(HideDetailActions.class)) {
				HideDetailAction [] actions = element.getAnnotation(HideDetailActions.class).value();
				for (HideDetailAction action: actions) {				
					if (isForView(metaView, action.forViews(), action.notForViews())) {
						if (Is.emptyString(collectionView.getHideActionName())) {
							collectionView.setHideActionName(action.value());
							mustAddMetaView = true;				
						}
						else {
							duplicateAnnotationForView(collection.getName(), HideDetailAction.class, metaView.getName());
						}
					}
				}				
			}
			
			// RemoveAction
			if (element.isAnnotationPresent(RemoveAction.class)) {
				RemoveAction action = element.getAnnotation(RemoveAction.class);
				if (isForView(metaView, action.forViews(), action.notForViews())) {
					collectionView.setRemoveActionName(action.value());
					mustAddMetaView = true;				
				}
			}
			if (element.isAnnotationPresent(RemoveActions.class)) {
				RemoveAction [] actions = element.getAnnotation(RemoveActions.class).value();
				for (RemoveAction action: actions) {				
					if (isForView(metaView, action.forViews(), action.notForViews())) {
						if (Is.emptyString(collectionView.getRemoveActionName())) {
							collectionView.setRemoveActionName(action.value());
							mustAddMetaView = true;				
						}
						else {
							duplicateAnnotationForView(collection.getName(), RemoveAction.class, metaView.getName());
						}
					}
				}				
			}
			
			// RemoveSelectedAction
			if (element.isAnnotationPresent(RemoveSelectedAction.class)) {
				RemoveSelectedAction action = element.getAnnotation(RemoveSelectedAction.class);
				if (isForView(metaView, action.forViews(), action.notForViews())) {
					collectionView.setRemoveSelectedActionName(action.value());
					mustAddMetaView = true;				
				}
			}
			if (element.isAnnotationPresent(RemoveSelectedActions.class)) {
				RemoveSelectedAction [] actions = element.getAnnotation(RemoveSelectedActions.class).value();
				for (RemoveSelectedAction action: actions) {				
					if (isForView(metaView, action.forViews(), action.notForViews())) {
						if (Is.emptyString(collectionView.getRemoveSelectedActionName())) {
							collectionView.setRemoveSelectedActionName(action.value());
							mustAddMetaView = true;				
						}
						else {
							duplicateAnnotationForView(collection.getName(), RemoveSelectedAction.class, metaView.getName());
						}
					}
				}				
			}
			
			// EditAction
			if (element.isAnnotationPresent(EditAction.class)) {
				EditAction action = element.getAnnotation(EditAction.class);
				if (isForView(metaView, action.forViews(), action.notForViews())) {
					collectionView.setEditActionName(action.value());
					mustAddMetaView = true;				
				}
			}
			if (element.isAnnotationPresent(EditActions.class)) {
				EditAction [] actions = element.getAnnotation(EditActions.class).value();
				for (EditAction action: actions) {				
					if (isForView(metaView, action.forViews(), action.notForViews())) {
						if (Is.emptyString(collectionView.getEditActionName())) {
							collectionView.setEditActionName(action.value());
							mustAddMetaView = true;				
						}
						else {
							duplicateAnnotationForView(collection.getName(), EditAction.class, metaView.getName());
						}
					}
				}				
			}
			
			// ViewAction
			if (element.isAnnotationPresent(ViewAction.class)) {
				ViewAction action = element.getAnnotation(ViewAction.class);
				if (isForView(metaView, action.forViews(), action.notForViews())) {
					collectionView.setViewActionName(action.value());
					mustAddMetaView = true;				
				}
			}
			if (element.isAnnotationPresent(ViewActions.class)) {
				ViewAction [] actions = element.getAnnotation(ViewActions.class).value();
				for (ViewAction action: actions) {				
					if (isForView(metaView, action.forViews(), action.notForViews())) {
						if (Is.emptyString(collectionView.getViewActionName())) {
							collectionView.setViewActionName(action.value());
							mustAddMetaView = true;				
						}
						else {
							duplicateAnnotationForView(collection.getName(), ViewAction.class, metaView.getName());
						}
					}
				}				
			}
			
												
			// EditOnly
			if (element.isAnnotationPresent(EditOnly.class)) {
				EditOnly editOnly = element.getAnnotation(EditOnly.class);
				if (isForView(metaView, editOnly.forViews(), editOnly.notForViews())) {					
					collectionView.setEditOnly(true);
					mustAddMetaView = true;
				}
			}

			// AsAggregate
			if (element.isAnnotationPresent(AsEmbedded.class)) {
				AsEmbedded asAggregate = element.getAnnotation(AsEmbedded.class);
				if (isForView(metaView, asAggregate.forViews(), asAggregate.notForViews())) {					
					collectionView.setAsAggregate(true);
					mustAddMetaView = true;
				}
			}
			
			// ReadOnly
			if (element.isAnnotationPresent(ReadOnly.class)) {
				ReadOnly readOnly = element.getAnnotation(ReadOnly.class);
				if (isForView(metaView, readOnly.forViews(), readOnly.notForViews())) {					
					collectionView.setReadOnly(true);
					mustAddMetaView = true;
				}
			}
			
			// NoCreate
			if (element.isAnnotationPresent(NoCreate.class)) {
				NoCreate noCreate = element.getAnnotation(NoCreate.class);
				if (isForView(metaView, noCreate.forViews(), noCreate.notForViews())) {					
					collectionView.setCreateReference(false);
					mustAddMetaView = true;
				}
			}
			
			// NoModify
			if (element.isAnnotationPresent(NoModify.class)) {
				NoModify noModify = element.getAnnotation(NoModify.class);
				if (isForView(metaView, noModify.forViews(), noModify.notForViews())) {					
					collectionView.setModifyReference(false);
					mustAddMetaView = true;
				}
			}
			
			if (mustAddMetaView) {				
				metaView.addMetaViewCollection(collectionView);
			}
		} 			
		
		
		// No applicable
		if (element.isAnnotationPresent(Action.class)) {
			notApply(collection.getName(), Action.class, "properties & references");
		}
		if (element.isAnnotationPresent(Actions.class)) {
			notApply(collection.getName(), Actions.class, "properties & references");
		}		
		if (element.isAnnotationPresent(Hidden.class)) {
			notApply(collection.getName(), Hidden.class, "properties");
		}
		if (element.isAnnotationPresent(DescriptionsList.class)) {
			notApply(collection.getName(), DescriptionsList.class, "references");
		}
		if (element.isAnnotationPresent(DescriptionsLists.class)) {
			notApply(collection.getName(), DescriptionsLists.class, "references");
		}		
		if (element.isAnnotationPresent(Required.class)) {
			notApply(collection.getName(), Required.class, "properties & references");
		}
		if (element.isAnnotationPresent(Stereotype.class)) {
			notApply(collection.getName(), Stereotype.class, "properties");
		}				
		if (element.isAnnotationPresent(DefaultValueCalculator.class)) {
			notApply(collection.getName(), DefaultValueCalculator.class, "properties & references");
		}
		if (element.isAnnotationPresent(OnChange.class)) {
			notApply(collection.getName(), OnChange.class, "properties & references");
		}
		if (element.isAnnotationPresent(OnChanges.class)) {
			notApply(collection.getName(), OnChanges.class, "properties & references");
		}				
		if (element.isAnnotationPresent(SearchAction.class)) {
			notApply(collection.getName(), SearchAction.class, "references");
		}
		if (element.isAnnotationPresent(SearchActions.class)) {
			notApply(collection.getName(), SearchActions.class, "references");
		}		
		if (element.isAnnotationPresent(ReferenceView.class)) {
			notApply(collection.getName(), ReferenceView.class, "references");
		}
		if (element.isAnnotationPresent(ReferenceViews.class)) {
			notApply(collection.getName(), ReferenceViews.class, "references");
		}				
		if (element.isAnnotationPresent(NoFrame.class)) {
			notApply(collection.getName(), NoFrame.class, "references");
		}
		if (element.isAnnotationPresent(Depends.class)) {
			notApply(collection.getName(), Depends.class, "properties");
		}
		if (element.isAnnotationPresent(LabelFormat.class)) {
			notApply(collection.getName(), LabelFormat.class, "properties & references");
		}
		if (element.isAnnotationPresent(LabelFormats.class)) {
			notApply(collection.getName(), LabelFormats.class, "properties & references");
		}				
		if (element.isAnnotationPresent(PropertyValidator.class)) {
			notApply(collection.getName(), PropertyValidator.class, "properties");
		}
		if (element.isAnnotationPresent(PropertyValidators.class)) {
			notApply(collection.getName(), PropertyValidators.class, "properties");
		}
		if (element.isAnnotationPresent(Transient.class)) {
			notApply(collection.getName(), Transient.class, "properties");
		}
		if (element.isAnnotationPresent(NoSearch.class)) {
			notApply(collection.getName(), NoSearch.class, "references");
		}		
		if (element.isAnnotationPresent(Editor.class)) {
			notApply(collection.getName(), Editor.class, "properties");
		}		
		if (element.isAnnotationPresent(Editors.class)) {
			notApply(collection.getName(), Editors.class, "properties");
		}
		if (element.isAnnotationPresent(DisplaySize.class)) {
			notApply(collection.getName(), DisplaySize.class, "properties");
		}
		if (element.isAnnotationPresent(DisplaySizes.class)) {
			notApply(collection.getName(), DisplaySizes.class, "properties");
		}														
		
	}
	
	private String wrapWithDollars(String orderBy) {
		StringTokenizer st = new StringTokenizer(orderBy, " ,", true);
		StringBuffer result = new StringBuffer();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (" ".equals(token) || ",".equals(token) || 
					"desc".equalsIgnoreCase(token) || 
					"asc".equalsIgnoreCase(token))
			{
				result.append(token);
			}
			else {
				result.append("${");
				result.append(token);
				result.append('}');
			}
		}		
		return result.toString();
	}


	private void processAnnotations(MetaReference ref, AnnotatedElement element) throws XavaException {
		if (element == null) return;		

		// key
		if (element.isAnnotationPresent(Id.class)) {
			ref.setKey(true);
		}
		
		// Required
		if (element.isAnnotationPresent(Required.class)) {						
			ref.setRequired(true);
		}
		else if (element.isAnnotationPresent(ManyToOne.class)) {
			ManyToOne manyToOne = element.getAnnotation(ManyToOne.class);
			ref.setRequired(!manyToOne.optional());
		}
		
		// Default value calculator
		if (element.isAnnotationPresent(DefaultValueCalculator.class)) {
			DefaultValueCalculator calculator = element.getAnnotation(DefaultValueCalculator.class);
			MetaCalculator metaCalculator = new MetaCalculator();
			metaCalculator.setClassName(calculator.value().getName());
			for (PropertyValue put: calculator.properties()) {
				metaCalculator.addMetaSet(toMetaSet(put));
			}
			ref.setMetaCalculatorDefaultValue(metaCalculator);
		}
		
		// OnChange 
		if (element.isAnnotationPresent(OnChange.class)) {
			OnChange onChange = element.getAnnotation(OnChange.class);
			MetaPropertyView propertyView = new MetaPropertyView();
			String lastKeyProperty = (String) XCollections.last(ref.getMetaModelReferenced().getKeyPropertiesNames());
			propertyView.setPropertyName(ref.getName() + "." + lastKeyProperty);
			propertyView.setOnChangeActionClassName(onChange.value().getName());
			for (Object oview: ref.getMetaModel().getMetaViews()) {
				MetaView view = (MetaView) oview;
				if (isForView(view, onChange.forViews(), onChange.notForViews())) {
					view.addMetaViewProperty(propertyView);
				}
			}
		}				
		if (element.isAnnotationPresent(OnChanges.class)) {
			OnChange [] onChanges = element.getAnnotation(OnChanges.class).value();				
			for (OnChange onChange: onChanges) {
				MetaPropertyView propertyView = new MetaPropertyView();
				String lastKeyProperty = (String) XCollections.last(ref.getMetaModelReferenced().getKeyPropertiesNames());
				propertyView.setPropertyName(ref.getName() + "." + lastKeyProperty);
				propertyView.setOnChangeActionClassName(onChange.value().getName());
				for (Object oview: ref.getMetaModel().getMetaViews()) {
					MetaView view = (MetaView) oview;
					if (isForView(view, onChange.forViews(), onChange.notForViews())) {
						view.addMetaViewProperty(propertyView);
					}
				}				
			}			
		}
		
		
		// for View	
		for (Object oMetaView: ref.getMetaModel().getMetaViews()) {
			MetaView metaView = (MetaView) oMetaView;			
			MetaReferenceView referenceView = new MetaReferenceView();
			referenceView.setReferenceName(ref.getName());						
			
			boolean mustAddMetaView = false;
			
			// Action 
			mustAddMetaView = addAction(element, metaView, referenceView, mustAddMetaView);			

			// DescriptionsList
			if (element.isAnnotationPresent(DescriptionsList.class)) {
				DescriptionsList descriptionsList = element.getAnnotation(DescriptionsList.class);
				if (isForView(metaView, descriptionsList.forViews(), descriptionsList.notForViews())) {
					referenceView.setMetaDescriptionsList(createMetaDescriptionsList(descriptionsList));									
					mustAddMetaView = true;				
				}
			}								
			if (element.isAnnotationPresent(DescriptionsLists.class)) {
				DescriptionsList [] descriptionsLists = element.getAnnotation(DescriptionsLists.class).value();				
				for (DescriptionsList descriptionsList: descriptionsLists) {
					if (isForView(metaView, descriptionsList.forViews(), descriptionsList.notForViews())) {
						referenceView.setMetaDescriptionsList(createMetaDescriptionsList(descriptionsList));											
						mustAddMetaView = true;				
					}
				}					
			}
									
			// SearchAction
			if (element.isAnnotationPresent(SearchAction.class)) {
				SearchAction action = element.getAnnotation(SearchAction.class);
				if (isForView(metaView, action.forViews(), action.notForViews())) {
					MetaSearchAction metaSearch = new MetaSearchAction();
					metaSearch.setActionName(action.value());
					referenceView.setMetaSearchAction(metaSearch);				
					mustAddMetaView = true;				
				}
			}
			if (element.isAnnotationPresent(SearchActions.class)) {
				SearchAction [] searchActions = element.getAnnotation(SearchActions.class).value();				
				for (SearchAction action: searchActions) {
					if (isForView(metaView, action.forViews(), action.notForViews())) {
						MetaSearchAction metaSearch = new MetaSearchAction();
						metaSearch.setActionName(action.value());
						referenceView.setMetaSearchAction(metaSearch);				
						mustAddMetaView = true;				
					}
				}					
			}			
			
			// ReferenceView
			if (element.isAnnotationPresent(ReferenceView.class)) {
				ReferenceView viewName = element.getAnnotation(ReferenceView.class);
				if (isForView(metaView, viewName.forViews(), viewName.notForViews())) {
					referenceView.setViewName(viewName.value());
					mustAddMetaView = true;				
				}
			}
			if (element.isAnnotationPresent(ReferenceViews.class)) {
				ReferenceView [] viewNames = element.getAnnotation(ReferenceViews.class).value();				
				for (ReferenceView viewName: viewNames) {
					if (isForView(metaView, viewName.forViews(), viewName.notForViews())) {
						referenceView.setViewName(viewName.value());
						mustAddMetaView = true;				
					}
				}					
			}			
			
			// NoFrame
			if (element.isAnnotationPresent(NoFrame.class)) {
				NoFrame noFrame = element.getAnnotation(NoFrame.class);
				if (isForView(metaView, noFrame.forViews(), noFrame.notForViews())) {
					referenceView.setFrame(false);
					mustAddMetaView = true;				
				}
			}
						
			// NoCreate
			if (element.isAnnotationPresent(NoCreate.class)) {
				NoCreate noCreate = element.getAnnotation(NoCreate.class);
				if (isForView(metaView, noCreate.forViews(), noCreate.notForViews())) {
					referenceView.setCreate(false);
					mustAddMetaView = true;				
				}
			}
			
			// NoModify
			if (element.isAnnotationPresent(NoModify.class)) {
				NoModify noModify = element.getAnnotation(NoModify.class);
				if (isForView(metaView, noModify.forViews(), noModify.notForViews())) {
					referenceView.setModify(false);
					mustAddMetaView = true;				
				}
			}
			
			// NoSearch
			if (element.isAnnotationPresent(NoSearch.class)) {
				NoSearch noSearch = element.getAnnotation(NoSearch.class);
				if (isForView(metaView, noSearch.forViews(), noSearch.notForViews())) {
					referenceView.setSearch(false);
					mustAddMetaView = true;				
				}
			}
												
			// LabelFormat
			if (element.isAnnotationPresent(LabelFormat.class)) {
				LabelFormat labelFormat = element.getAnnotation(LabelFormat.class);
				if (isForView(metaView, labelFormat.forViews(), labelFormat.notForViews())) {
					
					MetaDescriptionsList metaDescriptionsList = referenceView.getMetaDescriptionsList();
					if (metaDescriptionsList != null) {
						metaDescriptionsList.setLabelFormat(labelFormat.value().ordinal());
					}
					else {						
						log.warn(XavaResources.getString("label_format_for_reference_requires_descriptons_list", ref.getName()));
					}
					mustAddMetaView = true;				
				}
			}					
			if (element.isAnnotationPresent(LabelFormats.class)) {
				LabelFormat [] labelFormats = element.getAnnotation(LabelFormats.class).value();				
				for (LabelFormat labelFormat: labelFormats) {
					if (isForView(metaView, labelFormat.forViews(), labelFormat.notForViews())) {
						MetaDescriptionsList metaDescriptionsList = referenceView.getMetaDescriptionsList();
						if (metaDescriptionsList != null) {
							metaDescriptionsList.setLabelFormat(labelFormat.value().ordinal());
						}
						else {
							log.warn(XavaResources.getString("label_format_for_reference_requires_descriptons_list", ref.getName()));							
						}						
					}
				}					
			}
			
			// AsAggregate
			if (element.isAnnotationPresent(AsEmbedded.class)) {
				AsEmbedded asAggregate = element.getAnnotation(AsEmbedded.class);
				if (isForView(metaView, asAggregate.forViews(), asAggregate.notForViews())) {
					referenceView.setAsAggregate(true);
					mustAddMetaView = true;				
				}
			}
			
			// ReadOnly
			if (element.isAnnotationPresent(ReadOnly.class)) {
				ReadOnly readOnly = element.getAnnotation(ReadOnly.class);
				if (isForView(metaView, readOnly.forViews(), readOnly.notForViews())) {
					referenceView.setReadOnly(true);
					mustAddMetaView = true;				
				}
			}			
												
			if (mustAddMetaView) {				
				metaView.addMetaViewReference(referenceView);
			}

		}


		// Not applicable
		if (element.isAnnotationPresent(ListProperties.class)) {
			notApply(ref.getName(), ListProperties.class, "collections");
		}
		if (element.isAnnotationPresent(ListsProperties.class)) {
			notApply(ref.getName(), ListsProperties.class, "collections");
		}		
		if (element.isAnnotationPresent(Hidden.class)) {
			notApply(ref.getName(), Hidden.class, "properties");
		}
		if (element.isAnnotationPresent(Stereotype.class)) {
			notApply(ref.getName(), Stereotype.class, "properties");
		}
		if (element.isAnnotationPresent(CollectionView.class)) {
			notApply(ref.getName(), CollectionView.class, "collections");
		}
		if (element.isAnnotationPresent(CollectionViews.class)) {
			notApply(ref.getName(), CollectionViews.class, "collections");
		}						
		if (element.isAnnotationPresent(ListAction.class)) {
			notApply(ref.getName(), ListAction.class, "collections");
		}
		if (element.isAnnotationPresent(ListActions.class)) {
			notApply(ref.getName(), ListActions.class, "collections");
		}		
		if (element.isAnnotationPresent(EditOnly.class)) {
			notApply(ref.getName(), EditOnly.class, "collections");
		}
		if (element.isAnnotationPresent(Depends.class)) {
			notApply(ref.getName(), Depends.class, "properties");
		}
		if (element.isAnnotationPresent(PropertyValidator.class)) {
			notApply(ref.getName(), PropertyValidator.class, "properties");
		}
		if (element.isAnnotationPresent(PropertyValidators.class)) {
			notApply(ref.getName(), PropertyValidators.class, "properties");
		}														
		if (element.isAnnotationPresent(Transient.class)) {
			notApply(ref.getName(), Transient.class, "properties");
		}				
		if (element.isAnnotationPresent(EditAction.class)) {
			notApply(ref.getName(), EditAction.class, "collections");
		}				
		if (element.isAnnotationPresent(EditActions.class)) {
			notApply(ref.getName(), EditActions.class, "collections");
		}
		if (element.isAnnotationPresent(DetailAction.class)) {
			notApply(ref.getName(), DetailAction.class, "collections");
		}
		if (element.isAnnotationPresent(DetailActions.class)) {
			notApply(ref.getName(), DetailActions.class, "collections");
		}
		if (element.isAnnotationPresent(ViewAction.class)) {
			notApply(ref.getName(), ViewAction.class, "collections");
		}												
		if (element.isAnnotationPresent(ViewActions.class)) {
			notApply(ref.getName(), ViewActions.class, "collections");
		}										
		if (element.isAnnotationPresent(Editor.class)) {
			notApply(ref.getName(), Editor.class, "properties");
		}
		if (element.isAnnotationPresent(Editors.class)) {
			notApply(ref.getName(), Editors.class, "properties");
		}
		if (element.isAnnotationPresent(DisplaySize.class)) {
			notApply(ref.getName(), DisplaySize.class, "properties");
		}
		if (element.isAnnotationPresent(DisplaySizes.class)) {
			notApply(ref.getName(), DisplaySizes.class, "properties");
		}
		if (element.isAnnotationPresent(Condition.class)) {
			notApply(ref.getName(), Condition.class, "collections");
		}
		if (element.isAnnotationPresent(HideDetailAction.class)) {
			notApply(ref.getName(), HideDetailAction.class, "collections");
		}
		if (element.isAnnotationPresent(HideDetailActions.class)) {
			notApply(ref.getName(), HideDetailActions.class, "collections");
		}				
		if (element.isAnnotationPresent(NewAction.class)) {
			notApply(ref.getName(), NewAction.class, "collections");
		}				
		if (element.isAnnotationPresent(NewActions.class)) {
			notApply(ref.getName(), NewActions.class, "collections");
		}
		if (element.isAnnotationPresent(RemoveAction.class)) {
			notApply(ref.getName(), RemoveAction.class, "collections");
		}				
		if (element.isAnnotationPresent(RemoveActions.class)) {
			notApply(ref.getName(), RemoveActions.class, "collections");
		}				
		if (element.isAnnotationPresent(RemoveSelectedAction.class)) {
			notApply(ref.getName(), RemoveSelectedAction.class, "collections");
		}						
		if (element.isAnnotationPresent(RemoveSelectedActions.class)) {
			notApply(ref.getName(), RemoveSelectedActions.class, "collections");
		}
		if (element.isAnnotationPresent(SaveAction.class)) {
			notApply(ref.getName(), SaveAction.class, "collections");
		}
		if (element.isAnnotationPresent(SaveActions.class)) {
			notApply(ref.getName(), SaveActions.class, "collections");
		}										
		
	}


	private boolean addAction(AnnotatedElement element, MetaView metaView, MetaMemberView memberView, boolean mustAddMetaView) {
		if (element.isAnnotationPresent(Action.class)) {
			Action action = element.getAnnotation(Action.class);
			if (isForView(metaView, action.forViews(), action.notForViews())) {
				memberView.addActionName(action.value());
				if (action.alwaysEnabled()) {
					memberView.addAlwaysEnabledActionName(action.value());
				}
				mustAddMetaView = true;				
			}
		}					
		if (element.isAnnotationPresent(Actions.class)) {
			Action [] actions = element.getAnnotation(Actions.class).value();				
			for (Action action: actions) {
				if (isForView(metaView, action.forViews(), action.notForViews())) {
					memberView.addActionName(action.value());
					if (action.alwaysEnabled()) {
						memberView.addAlwaysEnabledActionName(action.value());
					}
					mustAddMetaView = true;
				}
			}					
		}
		return mustAddMetaView;
	}

	private MetaDescriptionsList createMetaDescriptionsList(DescriptionsList descriptionsList) {
		MetaDescriptionsList metaDescriptionList = new MetaDescriptionsList();
		metaDescriptionList.setDescriptionPropertiesNames(descriptionsList.descriptionProperties());
		metaDescriptionList.setDepends(descriptionsList.depends());
		metaDescriptionList.setCondition(descriptionsList.condition());
		metaDescriptionList.setOrderByKey(descriptionsList.orderByKey());
		metaDescriptionList.setOrder(descriptionsList.order());
		return metaDescriptionList;
	}
	
	private void processAnnotations(MetaModel metaModel, AnnotatedElement element) throws XavaException {		
		if (element.isAnnotationPresent(EntityValidator.class)) {			
			EntityValidator validator = element.getAnnotation(EntityValidator.class);
			addEntityValidator(metaModel, validator);
		}
		if (element.isAnnotationPresent(EntityValidators.class)) {
			EntityValidator [] validators = element.getAnnotation(EntityValidators.class).value();
			for (EntityValidator validator: validators) {				
				addEntityValidator(metaModel, validator);				
			}
		}
		if (element.isAnnotationPresent(RemoveValidator.class)) {			
			RemoveValidator validator = element.getAnnotation(RemoveValidator.class);
			addRemoveValidator(metaModel, validator);
		}
		if (element.isAnnotationPresent(RemoveValidators.class)) {
			RemoveValidator [] validators = element.getAnnotation(RemoveValidators.class).value();
			for (RemoveValidator validator: validators) {				
				addRemoveValidator(metaModel, validator);				
			}
		}		
	}

	private void addEntityValidator(MetaModel metaModel, EntityValidator validator) {
		MetaValidator metaValidator = new MetaValidator();
		metaValidator.setClassName(validator.value().getName());
		for (PropertyValue put: validator.properties()) {
			metaValidator.addMetaSet(toMetaSet(put));
		}
		metaValidator.setOnlyOnCreate(validator.onlyOnCreate());
		metaModel.addMetaValidator(metaValidator);
	}
	
	private void addRemoveValidator(MetaModel metaModel, RemoveValidator validator) {
		MetaValidator metaValidator = new MetaValidator();
		metaValidator.setClassName(validator.value().getName());
		for (PropertyValue put: validator.properties()) {
			metaValidator.addMetaSet(toMetaSet(put));
		}		
		metaModel.addMetaValidatorRemove(metaValidator);
	}	
	
	private void addPropertyValidator(MetaProperty metaProperty, PropertyValidator validator) {
		MetaValidator metaValidator = new MetaValidator();
		metaValidator.setClassName(validator.value().getName());
		for (PropertyValue put: validator.properties()) {
			metaValidator.addMetaSet(toMetaSet(put));
		}
		metaValidator.setOnlyOnCreate(validator.onlyOnCreate());
		metaProperty.addMetaValidator(metaValidator);
	}
	
	
	private Map<String, PropertyDescriptor> getPropertyDescriptors(Class pojoClass) throws IntrospectionException {
		BeanInfo info = Introspector.getBeanInfo(pojoClass);
		Map<String, PropertyDescriptor> result = new HashMap<String, PropertyDescriptor>();
		for (PropertyDescriptor pd: info.getPropertyDescriptors()) {
			result.put(pd.getName(), pd);				
		}
		return result;
	}
	
	private String getClassNameFor(String name) throws XavaException {
		String suffix = "." + name;
		// If it's a managed entity
		for (String className: getManagedClassNames()) {
			if (className.endsWith(suffix)) return className;
		}
		// Maybe it's not a managed entity, but a transient object used for UI generation
		String className = null;
		for (String packageName: getManagedClassPackages()) {
			className = packageName + name;			
			try {
				Class.forName(className);
				return className;
			}
			catch (ClassNotFoundException ex) {				
			}
		}
		throw new XavaException("not_ejb3_entity_nor_transient_model", name);
	}
		
	private static Collection<String> getManagedClassPackages() {
		if (managedClassPackages == null) {
			managedClassPackages = new ArrayList<String>();
			for (String className: getManagedClassNames()) {
				managedClassPackages.add(Strings.noLastToken(className, "."));				
			}
		}
		return managedClassPackages;
	}


	public static Collection<String> getManagedClassNames() {
		if (managedClassNames == null) {
			// The next code is Hibernate dependent.
			// This code has to be modified in order to work with Glassfish, OpenJPA, etc.
			EntityManager manager = XPersistence.createManager();
			org.hibernate.impl.SessionImpl impl = (org.hibernate.impl.SessionImpl) manager.getDelegate();
			managedClassNames = impl.getSessionFactory().getAllClassMetadata().keySet();
			manager.close();						
		}
		return managedClassNames;
	}

	private void notApply(String memberName, Class annotation, String validMemberTypes) throws XavaException {
		if (XavaPreferences.getInstance().isFailOnAnnotationMisuse()) {			
			throw new XavaException("annotation_not_applicable", annotation.getName(), memberName, validMemberTypes);
		}
		log.warn(XavaResources.getString("annotation_not_applicable", annotation.getName(), memberName, validMemberTypes));
		
	}
	
	private void duplicateAnnotationForView(String memberName, Class annotation, String viewName) throws XavaException {
		if (XavaPreferences.getInstance().isFailOnAnnotationMisuse()) {
			throw new XavaException("duplicate_annotation_for_view", annotation.getName(), viewName, memberName);
		}
		log.warn(XavaResources.getString("duplicate_annotation_for_view", annotation.getName(), viewName, memberName));
	}
	
	private MetaSet toMetaSet(PropertyValue propertyValue) {
		MetaSet metaSet = new MetaSet();		
		metaSet.setPropertyName(propertyValue.name()); 
		metaSet.setPropertyNameFrom(propertyValue.from());
		metaSet.setValue(propertyValue.value());
		return metaSet;
	}

	private boolean isCascade(CascadeType[] types) {
		for (CascadeType type: types) {
			if (type == CascadeType.ALL || type == CascadeType.REMOVE) return true;
		}		
		return false;
	}
	
	private boolean isForView(MetaView view, String forViews, String notForViews) {
		if (Is.emptyStringAll(forViews, notForViews)) return true;
		if (!Is.emptyString(forViews) && !Is.emptyString(notForViews)) {
			log.warn(XavaResources.getString("forViews_and_notForViews_not_compatible")); 
		}
		if (!Is.emptyString(forViews)) {
			StringTokenizer st = new StringTokenizer(forViews, ",");
			while (st.hasMoreTokens()) {
				String viewName = st.nextToken().trim();
				if (view.getName().equals(viewName)) return true;
				if (Is.emptyString(view.getName()) && "DEFAULT".equals(viewName)) return true;
			}
			return false;
		}
		else {
			StringTokenizer st = new StringTokenizer(notForViews, ",");
			while (st.hasMoreTokens()) {
				String viewName = st.nextToken().trim();
				if (view.getName().equals(viewName)) return false;
				if (Is.emptyString(view.getName()) && "DEFAULT".equals(viewName)) return false;
			}	
			return true;
		}				
	}
	
		
}
