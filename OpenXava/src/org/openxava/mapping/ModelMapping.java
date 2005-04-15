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
	private Collection propiedadesModelo = new ArrayList(); // de String
	private Collection columnasTabla = new ArrayList(); // de String

	abstract public String getModelName() throws XavaException;

	abstract public MetaModel getMetaModel() throws XavaException;
	
	/**
	 * Util especialmente para averiguar el tipo de
	 * propiedades que no estan en el modelo, solo
	 * en esta en el mapeo. <p>
	 */
	public Class getType(String nombrePropiedad) throws XavaException {
		try {
			return getMetaModel().getMetaProperty(nombrePropiedad).getType();
		}
		catch (ElementNotFoundException ex) {
			// Lo intantamos sacar de la clave primaria
			if (!(getMetaModel() instanceof MetaEntityEjb))
				return java.lang.Object.class;
			MetaEntityEjb metaEntidad = (MetaEntityEjb) getMetaModel();
			Class clase = metaEntidad.getPrimaryKeyClass();
			try {
				return clase.getField(nombrePropiedad).getType();
			}
			catch (NoSuchFieldException ex2) {
				System.err.println(
					"ADVERTENCIA: Imposible averiguar tipo de la propiedad "
						+ nombrePropiedad
						+ " en el mapeo de "
						+ getMetaComponent().getName());
				// Si no lo conseguimos averiguar devolvemos un valor por defecto
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
	

	public void addPropertyMapping(PropertyMapping mapeoPropiedad)
		throws XavaException {
		propertyMappings.put(
			mapeoPropiedad.getProperty(),
			mapeoPropiedad);
		propiedadesModelo.add(mapeoPropiedad.getProperty());
		// Para conservar el orden
		columnasTabla.add(mapeoPropiedad.getColumn());
		// Para conservar el orden
	}

	public void addReferenceMapping(ReferenceMapping mapeoReferencia)
		throws XavaException {
		if (referenceMappings == null)
			referenceMappings = new HashMap();
		referenceMappings.put(
			mapeoReferencia.getReference(),
			mapeoReferencia);
		mapeoReferencia.setContainer(this);
	}

	/**
	 *
	 * @return Nunca nulo
	 * @param nombre java.lang.String
	 * @exception ElementNotFoundException  Si la referencia solicitada no existe o algún otro problema.
	 * @exception XavaException  Algún problema.
	 */
	public ReferenceMapping getReferenceMapping(String nombre)
		throws XavaException {		
		ReferenceMapping r =
			referenceMappings == null
				? null
				: (ReferenceMapping) referenceMappings.get(nombre);
		if (r == null) {
			throw new ElementNotFoundException("reference_mapping_not_found", nombre, getModelName());
		}
		return r;
	}

	/**
	 *
	 * @return Nunca nulo
	 * @param nombre java.lang.String
	 * @exception ElementNotFoundException  Si la referencia solicitada no existe o algún otro problema.
	 * @exception XavaException  Algún problema.
	 */
	public PropertyMapping getPropertyMapping(String nombre)
		throws XavaException {
		PropertyMapping p =
			propertyMappings == null
				? null
				: (PropertyMapping) propertyMappings.get(nombre);
		if (p == null) {
			throw new ElementNotFoundException(
				"No encontrado mapeo de propiedad "
					+ nombre
					+ " en mapeo de "
					+ getModelName());
		}
		return p;
	}

	/**
	 * En el orden en que se han añadido.
	 * @return Collection de <tt>String</tt>.
	 */
	public Collection getModelProperties() {
		return propiedadesModelo;
	}

	/**
	 * En el orden en que se han añadido.
	 * @return Collection de <tt>String</tt>.
	 */
	public Collection getColumns() {
		return columnasTabla;
	}
	
	public String getKeyColumnsAsString() throws XavaException {
		StringBuffer r = new StringBuffer();
		for (Iterator it=getMetaModel().getAllKeyPropertiesNames().iterator(); it.hasNext();) {
			String pr = (String) it.next();
			r.append(getColumn(pr));
			if (it.hasNext()) r.append(' ');
		}
		return r.toString();
	}
	
	public String getQualifiedColumn(String modelProperty)
		throws XavaException {		
		String r = null;
		String tableColumn = getTableColumn(modelProperty, true);				
		if (Is.emptyString(tableColumn))
			return "'" + modelProperty + "'";
		// for calculated fields or created by multiple coverter
		if (modelProperty.indexOf('.') >= 0) {			
			return tableColumn;
		}
		else  {			
			return getTable() + "." + tableColumn;
		}
	}

	/**
	 * Soporta el uso de referencias con puntos,
	 * esto es: mireferencia.mipropiedad.
	 *
	 * @exception ElementNotFoundException
	 * @exception XavaException
	 */
	public String getColumn(String propiedadModelo)
		throws XavaException {
		return getTableColumn(propiedadModelo, false);
	}

	private String getTableColumn(
		String propiedadModelo,
		boolean cualificarColumnaMapeoReferencia)
		throws XavaException {
		PropertyMapping mapeoPropiedad =
			(PropertyMapping) propertyMappings.get(propiedadModelo);
		if (mapeoPropiedad == null) {
			int idx = propiedadModelo.indexOf('.');
			if (idx >= 0) {
				String nombreReferencia = propiedadModelo.substring(0, idx);
				String nombrePropiedad = propiedadModelo.substring(idx + 1);
				if (getMetaModel()
					.getMetaReference(nombreReferencia)
					.isAggregate()) {
					mapeoPropiedad =
						(PropertyMapping) propertyMappings.get(
							nombreReferencia + "_" + nombrePropiedad);
					if (mapeoPropiedad == null) {
						throw new ElementNotFoundException(
								"property_mapping_not_found", 
								nombreReferencia + "_" + nombrePropiedad, getModelName());
					}
					return mapeoPropiedad.getColumn();
				}
				ReferenceMapping mapeoReferencia =
					getReferenceMapping(nombreReferencia);
				if (mapeoReferencia
					.hasColumnForReferencedModelProperty(nombrePropiedad)) {
					if (cualificarColumnaMapeoReferencia) {
						return getTable()
							+ "."
							+ mapeoReferencia
								.getColumnForReferencedModelProperty(
								nombrePropiedad);
					}
					else {
						return mapeoReferencia
							.getColumnForReferencedModelProperty(
							nombrePropiedad);
					}
				}
				else {					
					ModelMapping mapeoReferenciado =
						mapeoReferencia.getReferencedMapping();
					String nombreTabla = mapeoReferenciado.getTable();
					boolean secondLevel = nombrePropiedad.indexOf('.') >= 0;
					String nombreColumna =
						mapeoReferenciado.getTableColumn(nombrePropiedad, secondLevel);
					if (cualificarColumnaMapeoReferencia && !secondLevel) {						
						return nombreTabla + "." + nombreColumna;
					}
					else {
						return nombreColumna;
					}
				}
			}
			throw new ElementNotFoundException(
				"Error de mapeo O/R: La propiedad "
					+ propiedadModelo
					+ " no tiene campo definido en el mapeo");
		}
		return mapeoPropiedad.getColumn();
	}

	/**
	 * @exception ElementNotFoundException Si no exista la propiedad.
	 * @exception XavaException
	 * @return nulo si la propidad existe pero no tiene conversor
	 */
	public IConverter getConverter(String propiedadModelo)
		throws XavaException {
		return getPropertyMapping(propiedadModelo).getConverter();
	}

	/**
	 * @exception ElementNotFoundException Si no exista la propiedad.
	 * @exception XavaException
	 * @return nulo si la propidad existe pero no tiene conversor
	 */
	public IMultipleConverter getMultipleConverter(String propiedadModelo)
		throws XavaException {
		return getPropertyMapping(propiedadModelo).getMultipleConverter();
	}

	/**
	 * Si existe la propiedad indicada y tiene conversor.
	 */
	public boolean hasConverter(String nombrePropiedad) {
		try {
			return getPropertyMapping(nombrePropiedad).hasConverter();
		}
		catch (XavaException ex) {
			return false;
		}
	}

	/**
	 * Gets the componente
	 * @return Returns a Componente
	 */
	public MetaComponent getMetaComponent() {
		return metaComponent;
	}
	/**
	 * Sets the componente
	 * @param componente The componente to set
	 */
	public void setMetaComponent(MetaComponent componente) throws XavaException {
		this.metaComponent = componente;		
		establecerConversoresDefecto();
	}

	/**
	 * Las propiedades entre ${ }. <p>
	 * Por ejemplo, cambiaria
	 * <pre>
	 * select ${codigo}, ${nombre}
	 * </pre>
	 * por
	 * <pre>
	 * select TGRCOD, TGRDEN
	 * </pre>
	 */
	public String changePropertiesByColumns(String origen)
		throws XavaException {
		StringBuffer r = new StringBuffer(origen);
		int i = r.toString().indexOf("${");
		int f = 0;
		while (i >= 0) {
			f = r.toString().indexOf("}", i + 2);
			if (f < 0)
				break;
			String propiedad = r.substring(i + 2, f);
			String columna = "0"; // asi se quedara si es calculada
			if (!getMetaModel().isCalculated(propiedad)) {				
				columna = getQualifiedColumn(propiedad);				
			}
			r.replace(i, f + 1, columna);
			i = r.toString().indexOf("${");
		}
		return r.toString();
	}

	public String changePropertiesByCMPAttributes(String origen)
		throws XavaException {
		StringBuffer r = new StringBuffer(origen);
		int i = r.toString().indexOf("${");
		int f = 0;
		while (i >= 0) {
			f = r.toString().indexOf("}", i + 2);
			if (f < 0)
				break;
			String propiedad = r.substring(i + 2, f);
			String atributoCMP = null;
			if (propiedad.indexOf('.') >= 0) {
				atributoCMP = "o." + Strings.change(propiedad, ".", "_");
			}
			else {			
				MetaProperty metaPropiedad =
					getMetaModel().getMetaProperty(propiedad);
				if (metaPropiedad.getMapping().hasConverter()) {
					atributoCMP = "o._" + Strings.firstUpper(propiedad);
				}
				else {
					atributoCMP = "o." + propiedad;
				}
			}
			r.replace(i, f + 1, atributoCMP);
			i = r.toString().indexOf("${");
		}
		return r.toString();
	}

	/**
	 * Method tieneMapeoPropiedad.
	 * @param nombreMiembro
	 * @return boolean
	 */
	public boolean hasPropertyMapping(String nombreMiembro) {
		return propertyMappings.containsKey(nombreMiembro);
	}

	//	Sería buena idea obtener los conversores por defecto de un archivo xml en el futuro
	private void establecerConversoresDefecto() throws XavaException {
		Iterator it = propertyMappings.values().iterator();
		while (it.hasNext()) {
			PropertyMapping mapeoPropiedad = (PropertyMapping) it.next();
			establecerConversorDefecto(mapeoPropiedad);
		}
	}

	// Sería buena idea obtener los conversores por defecto de un archivo xml en el futuro		 
	private void establecerConversorDefecto(PropertyMapping mapeoPropiedad)
		throws XavaException {
		if (mapeoPropiedad.hasConverter())
			return;
		MetaProperty p = null;
		try {
			p =
				getMetaModel().getMetaProperty(
					mapeoPropiedad.getProperty());
		}
		catch (ElementNotFoundException ex) {			
			return;
		}
		if (p.isKey() || !getMetaModel().isGenerateXDocLet()) 
			return;
		// Lo conversores en las claves son engorrosos para el programador
		// y normalmente inconvenientes
		// si se quiere conversor para clave que se ponga explicitamente	
		// Y si el código es generado no la conversión la hará el programador
		// del bean dentro de su set y get
		if (java.lang.String.class.equals(p.getType())) {
			mapeoPropiedad.setConverterClassName(
				org.openxava.converters.TrimStringConverter
					.class
					.getName());
			mapeoPropiedad.setCmpTypeName("String");
		}
		else if (
			int.class.equals(p.getType())
				|| java.lang.Integer.class.equals(p.getType())) {
			mapeoPropiedad.setConverterClassName(
				IntegerNumberConverter
					.class
					.getName());
			mapeoPropiedad.setCmpTypeName("Integer");
		}
		else if (
			boolean.class.equals(p.getType())
				|| java.lang.Boolean.class.equals(p.getType())) {
			mapeoPropiedad.setConverterClassName(
				Boolean01Converter.class.getName());
			mapeoPropiedad.setCmpTypeName("Integer");
		}		
		else if (
			long.class.equals(p.getType())
				|| java.lang.Long.class.equals(p.getType())) {
			mapeoPropiedad.setConverterClassName(
				LongNumberConverter
					.class
					.getName());
			mapeoPropiedad.setCmpTypeName("Long");
		}
		else if (java.math.BigDecimal.class.equals(p.getType())) {
			mapeoPropiedad.setConverterClassName(
				BigDecimalNumberConverter
					.class
					.getName());
			mapeoPropiedad.setCmpTypeName("java.math.BigDecimal");
		}
	}

	public boolean hasReferenceMapping(MetaReference metaReferencia) {
		if (referenceMappings == null)
			return false;
		return referenceMappings.containsKey(metaReferencia.getName());
	}

	public boolean isReferenceOverlappingWithSomeProperty(
		String referencia,
		String propiedadDeReferencia)
		throws XavaException {
		String columna =
			getReferenceMapping(
				referencia).getColumnForReferencedModelProperty(
				propiedadDeReferencia);
		return getColumns().contains(columna);
	}

	public boolean isReferenceOverlappingWithSomeProperty(String referencia)
		throws XavaException {
		Iterator it = getReferenceMapping(referencia).getDetails().iterator();
		while (it.hasNext()) {
			ReferenceMappingDetail d = (ReferenceMappingDetail) it.next();
			if (getColumns().contains(d.getColumn()))
				return true;
		}
		return false;
	}
		
	public boolean isReferencePropertyOverlappingWithSomeProperty(String propiedadCualificada)	throws XavaException {
		int idx = propiedadCualificada.indexOf('.');
		if (idx < 0) return false;
		String ref = propiedadCualificada.substring(0, idx);
		String pr = propiedadCualificada.substring(idx + 1);
		return isReferenceOverlappingWithSomeProperty(ref, pr);
	}

	/**	 
	 * @throws XavaException Si no tiene una propiedad solapada, u otro problema.
	 */
	public String getOverlappingPropertyForReference(
		String referencia,
		String propiedadDeReferencia)
		throws XavaException {
		String columna =
			getReferenceMapping(
				referencia).getColumnForReferencedModelProperty(
				propiedadDeReferencia);
		if (propertyMappings == null) {
			throw new XavaException(
				"Propiedad "
					+ propiedadDeReferencia
					+ " de "
					+ referencia
					+ " no tiene una propiedad solapada");
		}
		Iterator it = propertyMappings.values().iterator();
		while (it.hasNext()) {
			PropertyMapping mapeo = (PropertyMapping) it.next();
			if (columna.equals(mapeo.getColumn()))
				return mapeo.getProperty();
		}
		throw new XavaException(
			"Propiedad "
				+ propiedadDeReferencia
				+ " de "
				+ referencia
				+ " no tiene una propiedad solapada");
	}

	/**
	 * 
	 * @return De tipo <tt>String</tt> y nunca nulo.
	 */
	public Collection getOverlappingPropertiesOfReference(String referencia)
		throws XavaException {
		Collection propiedadesSolapadasDeReferencia = new ArrayList();
		Iterator it = getReferenceMapping(referencia).getDetails().iterator();
		while (it.hasNext()) {
			ReferenceMappingDetail d = (ReferenceMappingDetail) it.next();
			if (getColumns().contains(d.getColumn())) {
				propiedadesSolapadasDeReferencia.add(
					d.getReferencedModelProperty());
			}
		}
		return propiedadesSolapadasDeReferencia;
	}

	private PropertyMapping getMappingForColumn(String columna) throws XavaException {
		if (propertyMappings == null) {
			throw new ElementNotFoundException("mapping_not_found_no_property_mappings", columna); 
		}			
		Iterator it = propertyMappings.values().iterator();
		while (it.hasNext()) {
			PropertyMapping mapeoPropiedad = (PropertyMapping) it.next();
			if (mapeoPropiedad.getColumn().equals(columna)) {
				return mapeoPropiedad; 
			}
		}		 
		throw new ElementNotFoundException("mapping_for_column_not_found", columna);
	}
	
	String getCMPAttributeForColumn(String columna) throws XavaException {
		PropertyMapping mapeo = getMappingForColumn(columna);
		if (!mapeo.hasConverter()) return Strings.change(mapeo.getProperty(), ".", "_");
		return "_" + Strings.change(Strings.firstUpper(mapeo.getProperty()), ".", "_");
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
