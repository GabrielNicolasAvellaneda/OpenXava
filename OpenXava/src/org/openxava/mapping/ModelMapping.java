package org.openxava.mapping;

import java.util.*;

import org.openxava.component.*;
import org.openxava.converters.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;

/**
 * @author Javier Paniza
 */
abstract public class ModelMapping implements java.io.Serializable {

	private MetaComponent metaComponent;
	private String table;
	private Map propertyMappings = new HashMap();
	private Map referenceMappings;
	private Collection modelProperties = new ArrayList(); // of String
	private Collection tableColumns = new ArrayList(); // of String

	abstract public String getModelName() throws XavaException;

	abstract public MetaModel getMetaModel() throws XavaException;
	
	/**
	 * Util specially to find out the type of
	 * properties that are not in model, only
	 * in mapping.
	 */
	public Class getType(String nombrePropiedad) throws XavaException {
		try {
			return getMetaModel().getMetaProperty(nombrePropiedad).getType();
		}
		catch (ElementNotFoundException ex) {
			// Try to obtain it from primary key
			if (!(getMetaModel() instanceof MetaEntityEjb))
				return java.lang.Object.class;
			MetaEntityEjb metaEntidad = (MetaEntityEjb) getMetaModel();
			Class clase = metaEntidad.getPrimaryKeyClass();
			try {
				return clase.getField(nombrePropiedad).getType();
			}
			catch (NoSuchFieldException ex2) {
				System.err.println(
						XavaResources.getString("property_type_from_mapping_warning", 
								nombrePropiedad, getMetaComponent().getName()));
				// If we does not obtain type return a default value
				return java.lang.Object.class;
			}
		}
	}

	public String getTable() {
		return table;
	}
	public void setTable(String tabla) {
		this.table = tabla;
	}
	
	public String getSchema() {
		int idx = table.indexOf('.'); 
		if (idx < 0) return null;
		return table.substring(0, idx);
	}
	
	public String getUnqualifiedTable() {
		int idx = table.indexOf('.'); 
		if (idx < 0) return table;
		return table.substring(idx+1);
	}
	

	public void addPropertyMapping(PropertyMapping propertyMapping)
		throws XavaException {
		propertyMappings.put(
			propertyMapping.getProperty(),
			propertyMapping);
		modelProperties.add(propertyMapping.getProperty());
		// To keep order
		tableColumns.add(propertyMapping.getColumn());
	}

	public void addReferenceMapping(ReferenceMapping referenceMapping)
		throws XavaException {
		if (referenceMappings == null)
			referenceMappings = new HashMap();
		referenceMappings.put(
			referenceMapping.getReference(),
			referenceMapping);
		referenceMapping.setContainer(this);
	}

	/**
	 * @return Not null
	 */
	public ReferenceMapping getReferenceMapping(String name)
		throws XavaException, ElementNotFoundException {		
		ReferenceMapping r =
			referenceMappings == null
				? null
				: (ReferenceMapping) referenceMappings.get(name);
		if (r == null) {
			throw new ElementNotFoundException("reference_mapping_not_found", name, getModelName());
		}
		return r;
	}

	/**
	 * @return Not null
	 */
	public PropertyMapping getPropertyMapping(String name)
		throws XavaException, ElementNotFoundException {
		PropertyMapping p =
			propertyMappings == null
				? null
				: (PropertyMapping) propertyMappings.get(name);
		if (p == null) {
			throw new ElementNotFoundException("property_mapping_not_found", name, getModelName());
		}
		return p;
	}

	/**
	 * In the order that they was added.
	 * @return Collection of <tt>String</tt>.
	 */
	public Collection getModelProperties() {
		return modelProperties;
	}

	/**
	 * In the order that they was added.
	 * @return Collection of <tt>String</tt>.
	 */
	public Collection getColumns() {
		return tableColumns;
	}
	
	public String getKeyColumnsAsString() throws XavaException {
		StringBuffer r = new StringBuffer();
		Collection columns = new HashSet();
		for (Iterator it=getMetaModel().getAllKeyPropertiesNames().iterator(); it.hasNext();) {
			String pr = (String) it.next();
			String column = getColumn(pr);
			if (columns.contains(column)) continue;
			columns.add(column);
			r.append(column);
			r.append(' ');
		}		
		return r.toString().trim();
	}
	
	public String getQualifiedColumn(String modelProperty)
		throws XavaException {		
		String tableColumn = getTableColumn(modelProperty, true);						
		if (Is.emptyString(tableColumn))
			return "'" + modelProperty + "'";
		// for calculated fields or created by multiple converter
		
		if (modelProperty.indexOf('.') >= 0) {			
			if (tableColumn.indexOf('.') < 0) return tableColumn;			
			if (tableColumn.startsWith(getTable() + ".")) return tableColumn;
			String reference = modelProperty.substring(0, modelProperty.lastIndexOf('.'));
			// The next code uses the alias of the table instead of its name. In order to
			// support multiple references to the same model
			if (reference.indexOf('.') >= 0) {				
				if (getMetaModel().getMetaProperty(modelProperty).isKey()) {
					reference = reference.substring(0, reference.lastIndexOf('.'));
				}				
				reference = reference.substring(reference.lastIndexOf('.') + 1);
				
			}
			return "T_" + reference + tableColumn.substring(tableColumn.lastIndexOf('.')) ;
		}
		else  {			
			return getTable() + "." + tableColumn;
		}
	}

	/**
	 * Support the use of references with dots,
	 * this is: myreference.myproperty.
	 */
	public String getColumn(String propiedadModelo)
		throws ElementNotFoundException, XavaException {
		return getTableColumn(propiedadModelo, false);
	}

	private String getTableColumn(
		String modelProperty,
		boolean qualifyReferenceMappingColumn)
		throws XavaException {
		PropertyMapping propertyMapping =
			(PropertyMapping) propertyMappings.get(modelProperty);
		if (propertyMapping == null) {
			int idx = modelProperty.indexOf('.');
			if (idx >= 0) {
				String referenceName = modelProperty.substring(0, idx);
				String propertyName = modelProperty.substring(idx + 1);
				if (getMetaModel()
					.getMetaReference(referenceName)
					.isAggregate()) {
					propertyMapping =
						(PropertyMapping) propertyMappings.get(
							referenceName + "_" + propertyName);
					if (propertyMapping == null) {
						throw new ElementNotFoundException(
								"property_mapping_not_found", 
								referenceName + "_" + propertyName, getModelName());
					}
					return propertyMapping.getColumn();
				}
				ReferenceMapping referenceMapping =
					getReferenceMapping(referenceName);
				if (referenceMapping
					.hasColumnForReferencedModelProperty(propertyName)) {
					if (qualifyReferenceMappingColumn) {
						return getTable()
							+ "."
							+ referenceMapping
								.getColumnForReferencedModelProperty(
								propertyName);
					}
					else {
						return referenceMapping
							.getColumnForReferencedModelProperty(
							propertyName);
					}
				}
				else {					
					ModelMapping referencedMapping =
						referenceMapping.getReferencedMapping();
					String tableName = referencedMapping.getTable();
					boolean secondLevel = propertyName.indexOf('.') >= 0;
					String columnName =
						referencedMapping.getTableColumn(propertyName, secondLevel);
					if (qualifyReferenceMappingColumn && !secondLevel) {						
						return tableName + "." + columnName;
					}
					else {
						return columnName;
					}
				}
			}
			throw new ElementNotFoundException("property_mapping_not_found", modelProperty, getModelName());
		}
		return propertyMapping.getColumn();
	}

	/**
	 * @exception ElementNotFoundException If property does not exist.
	 * @exception XavaException Any problem
	 * @return nulo If property exists but it does not have converter.
	 */
	public IConverter getConverter(String modelProperty)
		throws ElementNotFoundException, XavaException {
		return getPropertyMapping(modelProperty).getConverter();
	}

	/**
	 * @exception ElementNotFoundException If property does not exist.
	 * @exception XavaException Any problem
	 * @return nulo If property exists but it does not have converter.
	 */
	public IMultipleConverter getMultipleConverter(String modelProperty)
		throws ElementNotFoundException, XavaException {
		return getPropertyMapping(modelProperty).getMultipleConverter();
	}

	/**
	 * If the property exists and has converter.
	 */
	public boolean hasConverter(String propertyName) {
		try {
			return getPropertyMapping(propertyName).hasConverter();
		}
		catch (XavaException ex) {
			return false;
		}
	}

	public MetaComponent getMetaComponent() {
		return metaComponent;
	}
	public void setMetaComponent(MetaComponent componente) throws XavaException {
		this.metaComponent = componente;		
		setupDefaultConverters();
	}

	/**
	 * Change the properties inside ${ } by the database columns. <p>
	 * For example, it would change:
	 * <pre>
	 * select ${number}, ${name}
	 * </pre>
	 * by
	 * <pre>
	 * select TGRCOD, TGRDEN
	 * </pre>
	 */
	public String changePropertiesByColumns(String source)
		throws XavaException {
		StringBuffer r = new StringBuffer(source);
		int i = r.toString().indexOf("${");
		int f = 0;
		while (i >= 0) {
			f = r.toString().indexOf("}", i + 2);
			if (f < 0)
				break;
			String property = r.substring(i + 2, f);
			String column = "0"; // thus it remained if it is calculated
			if (!getMetaModel().isCalculated(property)) {				
				column = getQualifiedColumn(property);				
			}
			r.replace(i, f + 1, column);
			i = r.toString().indexOf("${");
		}
		return r.toString();
	}

	public String changePropertiesByCMPAttributes(String source)
		throws XavaException {
		StringBuffer r = new StringBuffer(source);
		int i = r.toString().indexOf("${");
		int f = 0;
		while (i >= 0) {
			f = r.toString().indexOf("}", i + 2);
			if (f < 0)
				break;
			String property = r.substring(i + 2, f);
			String cmpAttribute = null;
			if (property.indexOf('.') >= 0) {
				cmpAttribute = "o._" + Strings.firstUpper(Strings.change(property, ".", "_"));
			}
			else {			
				MetaProperty metaProperty =
					getMetaModel().getMetaProperty(property);
				if (metaProperty.getMapping().hasConverter()) {
					cmpAttribute = "o._" + Strings.firstUpper(property);
				}
				else {
					cmpAttribute = "o." + property;
				}
			}
			r.replace(i, f + 1, cmpAttribute);
			i = r.toString().indexOf("${");
		}
		return r.toString();
	}

	public boolean hasPropertyMapping(String memberName) {
		return propertyMappings.containsKey(memberName);
	}

	private void setupDefaultConverters() throws XavaException {
		Iterator it = propertyMappings.values().iterator();
		while (it.hasNext()) {
			PropertyMapping propertyMapping = (PropertyMapping) it.next();
			setDefaultConverter(propertyMapping);
		}
	}
		 
	private void setDefaultConverter(PropertyMapping propertyMapping)
		throws XavaException {
		if (propertyMapping.hasConverter() || propertyMapping.hasMultipleConverter()) 
			return;
		MetaProperty p = null;
		try {
			p =
				getMetaModel().getMetaProperty(
					Strings.change(propertyMapping.getProperty(), "_", "."));
		}
		catch (ElementNotFoundException ex) {			
			return;
		}
		
		// Converters in keys are troublesome for programmer and
		// usually disadvantages.
		// If you need a converter in key then you can put it explicitly.
	
		// And in the case of code not generated, the conversion is
		// responsability of the programmer that write the bean (in get and set).		
		if (p.isKey() || !getMetaModel().isGenerateXDocLet()) 
			return;
		
		propertyMapping.setConverterClassName(Converters.getConverterClassNameFor(p));
		propertyMapping.setCmpTypeName(Converters.getCmpTypeFor(p));
		
		if (!propertyMapping.hasConverter()) {
			// In this way every no key property will have a converter
			// this is util because in code generated for properties with converter
			// there are things needed for every property (at least in ejb implementation)
			propertyMapping.setConverterClassName(NoConversionConverter.class.getName());
			String cmpType = p.getType().isPrimitive()?Primitives.toWrapperClass(p.getType()).getName():p.getType().getName();
			if ("[B".equals(cmpType)) cmpType = "byte []";
			propertyMapping.setCmpTypeName(cmpType);
		}					
	}

	public boolean hasReferenceMapping(MetaReference metaReference) {
		if (referenceMappings == null)
			return false;
		return referenceMappings.containsKey(metaReference.getName());
	}

	public boolean isReferenceOverlappingWithSomeProperty(
		String reference,
		String propertiesOfReference)
		throws XavaException {
		String column =
			getReferenceMapping(
				reference).getColumnForReferencedModelProperty(
				propertiesOfReference);
		return getColumns().contains(column);
	}

	public boolean isReferenceOverlappingWithSomeProperty(String reference)
		throws XavaException {
		Iterator it = getReferenceMapping(reference).getDetails().iterator();
		while (it.hasNext()) {
			ReferenceMappingDetail d = (ReferenceMappingDetail) it.next();
			if (getColumns().contains(d.getColumn()))
				return true;
		}
		return false;
	}
		
	public boolean isReferencePropertyOverlappingWithSomeProperty(String qualifiedProperty)	throws XavaException {
		int idx = qualifiedProperty.indexOf('.');
		if (idx < 0) return false;
		String ref = qualifiedProperty.substring(0, idx);
		String pr = qualifiedProperty.substring(idx + 1);
		return isReferenceOverlappingWithSomeProperty(ref, pr);
	}

	/**	 
	 * @throws XavaException If it does not have a overlapped property, or any other problem.
	 */
	public String getOverlappingPropertyForReference(
		String reference,
		String propertyOfReference)
		throws XavaException {
		String column =
			getReferenceMapping(
				reference).getColumnForReferencedModelProperty(
				propertyOfReference);
		if (propertyMappings == null) {
			throw new XavaException("reference_property_not_overlapped", 				
					propertyOfReference, reference);
		}
		Iterator it = propertyMappings.values().iterator();
		while (it.hasNext()) {
			PropertyMapping mapping = (PropertyMapping) it.next();
			if (column.equals(mapping.getColumn()))
				return mapping.getProperty();
		}
		throw new XavaException("reference_property_not_overlapped", 				
				propertyOfReference, reference);
	}

	/**
	 * @return Of <tt>String</tt> and not null.
	 */
	public Collection getOverlappingPropertiesOfReference(String reference)
		throws XavaException {
		Collection overlappingPropertiesOfReference = new ArrayList();
		Iterator it = getReferenceMapping(reference).getDetails().iterator();
		while (it.hasNext()) {
			ReferenceMappingDetail d = (ReferenceMappingDetail) it.next();
			if (getColumns().contains(d.getColumn())) {
				overlappingPropertiesOfReference.add(
					d.getReferencedModelProperty());
			}
		}
		return overlappingPropertiesOfReference;
	}

	private PropertyMapping getMappingForColumn(String column) throws XavaException {
		if (propertyMappings == null) {
			throw new ElementNotFoundException("mapping_not_found_no_property_mappings", column); 
		}			
		Iterator it = propertyMappings.values().iterator();
		while (it.hasNext()) {
			PropertyMapping propertyMapping = (PropertyMapping) it.next();
			if (propertyMapping.getColumn().equals(column)) {
				return propertyMapping; 
			}
		}		 
		throw new ElementNotFoundException("mapping_for_column_not_found", column);
	}
	
	String getCMPAttributeForColumn(String column) throws XavaException {
		PropertyMapping mapping = getMappingForColumn(column);
		if (!mapping.hasConverter()) return Strings.change(mapping.getProperty(), ".", "_");
		return "_" + Strings.change(Strings.firstUpper(mapping.getProperty()), ".", "_");
	}
	
	private Collection getPropertyMappings() {
		return propertyMappings.values();
	}
	
	private Collection getReferenceMappings() { 
		return referenceMappings==null?Collections.EMPTY_LIST:referenceMappings.values();
	}
		
	public Collection getCmpFields() throws XavaException { 
		Collection r = new ArrayList();
		Collection mappedColumns = new HashSet();
		for (Iterator it=getPropertyMappings().iterator(); it.hasNext();) {
			PropertyMapping pMapping = (PropertyMapping) it.next();
			r.addAll(pMapping.getCmpFields());
			mappedColumns.add(pMapping.getColumn());
		}
		for (Iterator it=getReferenceMappings().iterator(); it.hasNext();) {
			ReferenceMapping rMapping = (ReferenceMapping) it.next();
			for (Iterator itFields=rMapping.getCmpFields().iterator(); itFields.hasNext();) {
				CmpField field = (CmpField) itFields.next();
				if (!mappedColumns.contains(field.getColumn())) {
					r.add(field);
					mappedColumns.add(field.getColumn());
				}
			}
		}
		
		return r;
	}
		
}
