package org.openxava.tab.meta;

import java.util.*;

import org.openxava.component.*;
import org.openxava.filters.*;
import org.openxava.filters.meta.*;
import org.openxava.mapping.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;

/**
 * 
 * @author Javier Paniza
 */

public class MetaTab implements java.io.Serializable, Cloneable {

	private String defaultOrder;

	private String sQLDefaultOrder;

	private String sQLBaseCondition;

	private String selectSQL;

	private Collection metaPropertiesHiddenCalculated;

	private Collection metaPropertiesHidden;

	private String name;

	private MetaComponent metaComponent;

	private List metaConsults = new ArrayList();

	private List propertiesNames = null;

	private List metaProperties = null;

	private List metaPropertiesCalculated = null;

	private String properties; // separadas por comas, como en el archivo xml

	private String select;

	private Collection entityReferencesMappings;	

	private Collection tableColumns;

	private boolean excludeAll = false;

	private boolean excludeByKey = false;

	private MetaFilter metaFilter;

	private IFilter filter;

	private List hiddenPropertiesNames;

	private Collection hiddenTableColumns;

	private String baseCondition;

	private Map metaPropertiesTab;

	private Collection rowStyles;

	private String defaultPropertiesNames;
	
	public static String getTitleI18n(Locale locale, String modelName, String tabName) throws XavaException {
		String id = null;
		if (Is.emptyString(tabName)) {
			id = modelName + ".tab.title"; 
		}
		else {
			id = modelName + ".tabs." + tabName + ".title";
		}
		if (Labels.exists(id)) {
			return Labels.get(id, locale);
		}		
		else {
			return null;
		}
	}
	
	public MetaModel getMetaModel() throws XavaException {
		return getMetaComponent().getMetaEntity();
	}

	public void addMetaConsult(MetaConsult consulta) {
		metaConsults.add(consulta);
		consulta.setMetaTab(this);
	}

	/**
	 * @return Nunca nulo, de solo lectura y de tipo <tt>MetaConsulta</tt>.
	 */
	public Collection getMetaConsults() {
		return Collections.unmodifiableCollection(metaConsults);
	}

	public static MetaTab createDefault(MetaComponent componente)
			throws XavaException {
		MetaTab tab = new MetaTab();
		tab.setMetaComponent(componente);
		tab.addDefaultMetaConsults();
		return tab;
	}

	/**
	 * @return Nunca nulo, de tipo <tt>MetaPropiedad</tt> y de solo lectura.
	 */
	public List getMetaProperties() throws XavaException {
		if (metaProperties == null) {
			metaProperties = namesToMetaProperties(getPropertiesNames());
		}
		return metaProperties;
	}

	public Collection getMetaPropertiesHidden() throws XavaException {
		if (metaPropertiesHidden == null) {
			metaPropertiesHidden = namesToMetaProperties(getHiddenPropertiesNames());
		}
		return metaPropertiesHidden;
	}

	public List namesToMetaProperties(Collection nombres) throws XavaException {
		List metaPropiedades = new ArrayList();
		Iterator it = nombres.iterator();
		int i = -1;
		while (it.hasNext()) {
			i++;
			String nombre = (String) it.next();
			MetaProperty metaPropiedadTab = null;
			try {
				MetaProperty metaPropiedad = getMetaEntidad().getMetaProperty(
						nombre).cloneMetaProperty();
				metaPropiedad.setQualifiedName(nombre);
				String idEtiqueta = getId() + ".properties." + nombre;
				if (Labels.exists(idEtiqueta)) {
					metaPropiedad.setLabelId(idEtiqueta);
				} else if (metaPropertiesTab != null) {
					// De momento solo sobreescribimos desde la propiedad del
					// tab la etiqueta.
					metaPropiedadTab = (MetaProperty) metaPropertiesTab
							.get(nombre);
					if (metaPropiedadTab != null) {
						metaPropiedad = metaPropiedad.cloneMetaProperty();
						metaPropiedad.setLabel(metaPropiedadTab.getLabel());
					}
				}
				metaPropiedades.add(metaPropiedad);
			} catch (ElementNotFoundException ex) {
				MetaProperty noEnEntidad = new MetaProperty();
				noEnEntidad.setName(nombre);
				noEnEntidad.setTypeName("java.lang.Object");
				if (metaPropiedadTab != null) {
					noEnEntidad.setLabel(metaPropiedadTab.getLabel());
				}
				metaPropiedades.add(noEnEntidad);
			}
		}
		return metaPropiedades;
	}

	/**
	 * No incluye las ocultas
	 * 
	 * @return Nunca nulo, de tipo <tt>MetaPropiedad</tt> y de solo lectura.
	 */
	public Collection getMetaPropertiesCalculated() throws XavaException {
		if (metaPropertiesCalculated == null) {
			metaPropertiesCalculated = new ArrayList();
			Iterator it = getMetaProperties().iterator();
			while (it.hasNext()) {
				MetaProperty metaPropiedad = (MetaProperty) it.next();
				if (metaPropiedad.isCalculated()) {					
					metaPropertiesCalculated.add(metaPropiedad);
				}
			}
		}
		return metaPropertiesCalculated;
	}

	/**
	 * 
	 * @return Nunca nulo, de tipo <tt>MetaPropiedad</tt> y de solo lectura.
	 */
	public Collection getMetaPropertiesHiddenCalculated() throws XavaException {
		if (metaPropertiesHiddenCalculated == null) {
			metaPropertiesHiddenCalculated = new ArrayList();
			Iterator it = getMetaPropertiesHidden().iterator();
			while (it.hasNext()) {
				MetaProperty metaPropiedad = (MetaProperty) it.next();
				if (metaPropiedad.isCalculated()) {
					metaPropertiesHiddenCalculated.add(metaPropiedad);
				}
			}
		}
		return metaPropertiesHiddenCalculated;
	}

	public boolean hasCalculatedProperties() throws XavaException {
		return !getMetaPropertiesCalculated().isEmpty();
	}

	/**
	 * @return Nunca nulo, de tipo <tt>String</tt> y de solo lectura.
	 */
	public Collection getTableColumns() throws XavaException {
		if (tableColumns == null) {
			tableColumns = getColumnasTabla(getPropertiesNames());
		}
		return tableColumns;
	}

	/**
	 * @return Nunca nulo, de tipo <tt>String</tt> y de solo lectura.
	 */
	public Collection getHiddenTableColumns() throws XavaException {
		if (hiddenTableColumns == null) {
			hiddenTableColumns = getColumnasTabla(getHiddenPropertiesNames());
			hiddenTableColumns
					.addAll(getColumnasCampoCmpPropiedadesMultiples());
		}
		return hiddenTableColumns;
	}

	private Collection getColumnasTabla(Collection nombrePropiedades)
			throws XavaException {
		Collection columnasTabla = new ArrayList();
		Iterator it = nombrePropiedades.iterator();
		while (it.hasNext()) {
			String nombre = (String) it.next();
			try {
				columnasTabla
						.add(getEntityMapping().getQualifiedColumn(nombre));
			} catch (ElementNotFoundException ex) {
				columnasTabla.add("0"); // Sera sustituido
			}
		}
		return columnasTabla;
	}

	/**
	 * @return Nunca nulo, de tipo <tt>String</tt> y de solo lectura.
	 */
	public List getPropertiesNames() throws XavaException {
		if (propertiesNames == null) {
			if (!sonTodasPropiedades()) {
				propertiesNames = crearNombresPropiedades();
			} else {
				propertiesNames = crearNombresTodasPropiedades();
			}
		}
		return propertiesNames;
	}

	/**
	 * Nombres de propiedades que es necesario que existan pero no han de
	 * aparecer al usuarios.
	 * <p>
	 * 
	 * Normalmente son propiedades usadas para calcular otras. <br>
	 * Las claves no se tienen en cuenta. <br>
	 * 
	 * @return Nunca nulo, de tipo <tt>String</tt> y de solo lectura.
	 */
	public List getHiddenPropertiesNames() throws XavaException {
		if (hiddenPropertiesNames == null) {
			hiddenPropertiesNames = obtenerNombresPropiedadesUsadasParaCalcular();
			hiddenPropertiesNames
					.addAll(obtenerNombresPropiedadesUsadasEnOrderBy());
		}
		return hiddenPropertiesNames;
	}

	/**
	 * Method obtenerNombresPropiedadesUsadasEnOrderBy.
	 * 
	 * @return Collection
	 */
	private Collection obtenerNombresPropiedadesUsadasEnOrderBy()
			throws XavaException {
		List result = new ArrayList();
		Iterator itConsultas = getMetaConsults().iterator();
		while (itConsultas.hasNext()) {
			MetaConsult consulta = (MetaConsult) itConsultas.next();
			if (consulta.useOrderBy()) {
				Iterator itPropiedades = consulta.getOrderByPropertiesNames()
						.iterator();
				while (itPropiedades.hasNext()) {
					String propiedad = (String) itPropiedades.next();
					if (!getPropertiesNames().contains(propiedad)
							&& !hiddenPropertiesNames.contains(propiedad)
							&& !result.contains(propiedad)
							&& !getMetaModel().isKey(propiedad)) {
						result.add(propiedad);
					}
				}
			}
		}
		return result;
	}

	private List obtenerNombresPropiedadesUsadasParaCalcular()
			throws XavaException {
		Set result = new HashSet();
		Iterator itPropiedades = getMetaPropertiesCalculated().iterator();
		while (itPropiedades.hasNext()) {
			MetaProperty metaProperty = (MetaProperty) itPropiedades.next();
			if (!metaProperty.hasCalculator())
				continue;
			MetaSetsContainer metaCalculador = metaProperty
					.getMetaCalculator();
			if (!metaCalculador.containsMetaSets())
				continue;
			Iterator itPoners = metaCalculador.getMetaSets().iterator();
			while (itPoners.hasNext()) {
				MetaSet poner = (MetaSet) itPoners.next();
				String propertyNameFrom = poner.getPropertyNameFrom();
				if (!Is.emptyString(propertyNameFrom)
						&& !getPropertiesNames().contains(propertyNameFrom)) {
					String qualifiedName = metaProperty.getQualifiedName();
					int idx = qualifiedName.indexOf('.');
					if (idx < 0) {
						result.add(propertyNameFrom);
					}
					else {
						String ref = qualifiedName.substring(0, idx + 1);
						result.add(ref + propertyNameFrom);
					}
				}
			}
		}
		return new ArrayList(result);
	}

	private boolean sonTodasPropiedades() {
		return properties == null || properties.trim().equals("*");
	}

	// assert(!sonTodasPropiedades());
	private List crearNombresPropiedades() {
		StringTokenizer st = new StringTokenizer(properties, ",");
		List result = new ArrayList();
		while (st.hasMoreTokens()) {
			result.add(st.nextToken().trim());
		}
		return result;
	}

	private List crearNombresTodasPropiedades() throws XavaException {
		return getMetaEntidad().getPropertiesNamesWithoutHidden();
	}
	
	public void setDefaultPropertiesNames(String properties) {
		this.defaultPropertiesNames = properties;
		setPropertiesNames(properties);
	}

	/**
	 * Separados por comas.
	 */
	public void setPropertiesNames(String properties) {
		this.properties = properties;

		this.propertiesNames = null;
		this.metaProperties = null;
		this.metaPropertiesHiddenCalculated = null;
		this.metaPropertiesHidden = null;
		this.metaPropertiesCalculated = null;
		this.entityReferencesMappings = null;
		this.entityReferencesMappings = null;
		this.tableColumns = null;
		this.hiddenPropertiesNames = null;
		this.hiddenTableColumns = null;
		this.metaPropertiesTab = null;
		
		this.select = null; 
		this.selectSQL = null; 
	}

	EntityMapping getEntityMapping() throws XavaException {
		return getMetaComponent().getEntityMapping();
	}

	public String getSelect() throws XavaException {
		if (select == null) {
			select = crearSelect();
		}
		return select;
	}

	public String getSelectSQL() throws XavaException {
		if (selectSQL == null) {			
			selectSQL = getEntityMapping().changePropertiesByColumns(
					getSelect());			
		}		
		return selectSQL;
	}

	private String crearSelect() throws XavaException {
		if (hasBaseCondition()) {
			String condicionBase = getBaseCondition();
			if (condicionBase.trim().toUpperCase().startsWith("SELECT ")) {
				return condicionBase;
			}
		}
		// select básico
		StringBuffer select = new StringBuffer("select ");
		Iterator itPropiedades = getPropertiesNames().iterator();
		while (itPropiedades.hasNext()) {
			select.append("${");
			select.append(itPropiedades.next());
			select.append('}');
			if (itPropiedades.hasNext())
				select.append(", ");
		}
		Iterator itPropiedadesOcultas = getHiddenPropertiesNames().iterator();
		while (itPropiedadesOcultas.hasNext()) {
			select.append(", ");
			select.append("${");
			select.append(itPropiedadesOcultas.next());
			select.append('}');
		}
		Iterator itColumnasCampoCmpPropiedadesMultiples = getColumnasCampoCmpPropiedadesMultiples()
				.iterator();
		while (itColumnasCampoCmpPropiedadesMultiples.hasNext()) {
			select.append(", ");
			select.append(itColumnasCampoCmpPropiedadesMultiples.next());
		}
		select.append(" from ");
		select.append(getEntityMapping().getTable());
		// para las referencias		
		if (hasReferences()) {
			// las tablas

			Iterator itMapeosReferencias = getEntityReferencesMappings()
					.iterator();
			while (itMapeosReferencias.hasNext()) {
				ReferenceMapping mapeoReferencia = (ReferenceMapping) itMapeosReferencias.next();				
				select.append(" left join ");				
				select.append(mapeoReferencia.getReferencedTable());
				// el where de unión
				select.append(" on ");
				Iterator itDetalles = mapeoReferencia.getDetails().iterator();
				while (itDetalles.hasNext()) {
					ReferenceMappingDetail detalle = (ReferenceMappingDetail) itDetalles
							.next();
					select.append(detalle.getQualifiedColumn());
					select.append(" = ");
					select.append(detalle
							.getColumnaTablaReferenciadaCualificada());
					if (itDetalles.hasNext()) {
						select.append(" and ");
					}
				}
			}
		}

		select.append(' ');
		if (hasBaseCondition()) {
			select.append(" where ");
			select.append(getBaseCondition());
		}		
		return select.toString();
	}

	private Collection getColumnasCampoCmpPropiedadesMultiples()
			throws XavaException {
		Collection columnasCampoCmpPropiedadesMultiples = new ArrayList();
		Iterator it = getMetaProperties().iterator();
		String tabla = getEntityMapping().getTable();
		while (it.hasNext()) {
			MetaProperty p = (MetaProperty) it.next();
			PropertyMapping mapeo = p.getMapping();
			if (mapeo != null) {
				if (mapeo.hasMultipleConverter()) {
					Iterator itCampos = mapeo.getCmpFields().iterator();
					while (itCampos.hasNext()) {
						CmpField campo = (CmpField) itCampos.next();
						columnasCampoCmpPropiedadesMultiples.add(tabla + "."
								+ campo.getColumn());
					}
				}
			}
		}
		return columnasCampoCmpPropiedadesMultiples;
	}

	public boolean hasReferences() throws XavaException {
		return !getEntityReferencesMappings().isEmpty();
	}
	
	private Collection getEntityReferencesMappings() throws XavaException {	
		if (entityReferencesMappings == null) {
			entityReferencesMappings = new LinkedHashSet();
			for (Iterator itProperties = getPropertiesNames().iterator(); itProperties.hasNext();) {
				String property = (String) itProperties.next();
				fillEntityReferencesMappings(entityReferencesMappings, property, getMetaComponent().getMetaEntity());
			}
			for (Iterator itProperties = getHiddenPropertiesNames().iterator(); itProperties.hasNext();) {
				String property = (String) itProperties.next();
				fillEntityReferencesMappings(entityReferencesMappings, property, getMetaComponent().getMetaEntity());
			}						
		}
		return entityReferencesMappings;
	}
	
	private void fillEntityReferencesMappings(Collection result, String property, MetaModel metaModel) throws XavaException {		
		int idx = property.indexOf('.');				
		if (idx >= 0) {
			String referenceName = property.substring(0, idx);					
			MetaReference ref = metaModel.getMetaReference(referenceName);
			String memberName = property.substring(idx + 1);
			boolean hasMoreLevels = memberName.indexOf('.') >= 0;
			if (!ref.isAggregate()) {												
				if (hasMoreLevels || !ref.getMetaModelReferenced().isKey(memberName)) {
					result.add(metaModel.getMapping().getReferenceMapping(referenceName));
				}
			}			
			 
			if (hasMoreLevels) {
				fillEntityReferencesMappings(result, memberName, MetaComponent.get(ref.getReferencedModelName()).getMetaEntity());
			}
		}		
	}
		
	public void addDefaultMetaConsults() throws XavaException {
		if (!isExcludeByKey())
			añadirMetaConsultaClavePrimaria();
		if (!isExcludeAll())
			añadirMetaConsultaTodos();
	}

	private void añadirMetaConsultaTodos() {
		MetaConsult todos = new MetaConsult();
		todos.setName("todos");
		todos.setCondition("");
		addMetaConsult(todos);
	}

	private void añadirMetaConsultaClavePrimaria() throws XavaException {
		Collection propiedades = getMetaModel().getMetaPropertiesKey();
		if (propiedades.isEmpty())
			return;
		Iterator it = propiedades.iterator();
		MetaConsult porClave = new MetaConsult();
		porClave.setMetaTab(this);
		while (it.hasNext()) {
			MetaProperty propiedad = (MetaProperty) it.next();
			MetaParameter parametro = new MetaParameter();
			parametro.setPropertyName(propiedad.getName());
			porClave.addMetaParameter(parametro);
		}
		metaConsults.add(0, porClave);
	}

	private MetaEntity getMetaEntidad() throws XavaException {
		return getMetaComponent().getMetaEntity();
	}

	/**
	 * Gets the componente
	 * 
	 * @return Returns a Componente
	 */
	public MetaComponent getMetaComponent() {
		return metaComponent;
	}

	/**
	 * Sets the componente
	 * 
	 * @param componente
	 *            The componente to set
	 */
	public void setMetaComponent(MetaComponent componente) {
		this.metaComponent = componente;
	}

	/**
	 * Returns the excluirPorClave.
	 * 
	 * @return boolean
	 */
	public boolean isExcludeByKey() {
		return excludeByKey;
	}

	/**
	 * Returns the excluirTodos.
	 * 
	 * @return boolean
	 */
	public boolean isExcludeAll() {
		return excludeAll;
	}

	/**
	 * Sets the excluirPorClave.
	 * 
	 * @param excluirPorClave
	 *            The excluirPorClave to set
	 */
	public void setExcludeByKey(boolean excluirPorClave) {
		this.excludeByKey = excluirPorClave;
	}

	/**
	 * Sets the excluirTodos.
	 * 
	 * @param excluirTodos
	 *            The excluirTodos to set
	 */
	public void setExcludeAll(boolean excluirTodos) {
		this.excludeAll = excluirTodos;
	}

	/**
	 * Returns the nombre.
	 * 
	 * @return String
	 */
	public String getName() {
		return name == null ? "" : name;
	}

	private boolean tieneNombre() {
		return name != null && !name.trim().equals("");
	}

	/**
	 * Sets the nombre.
	 * 
	 * @param nombre
	 *            The nombre to set
	 */
	public void setName(String nombre) {
		this.name = nombre;
	}

	/**
	 * Returns the metaFiltro.
	 * 
	 * @return MetaFiltro
	 */
	public MetaFilter getMetaFilter() {
		return metaFilter;
	}

	/**
	 * Sets the metaFiltro.
	 * 
	 * @param metaFiltro
	 *            The metaFiltro to set
	 */
	public void setMetaFilter(MetaFilter metaFiltro) {
		this.metaFilter = metaFiltro;
	}

	public boolean hasFilter() {
		return this.metaFilter != null;
	}

	/**
	 * Aplica el filtro asociado a este tab si lo hay.
	 */
	Object filterParameters(Object o) throws XavaException {
		if (getMetaFilter() == null)
			return o;
		return getFilter().filter(o);
	}

	private IFilter getFilter() throws XavaException {
		if (filter == null) {
			filter = getMetaFilter().createFilter();
		}
		return filter;
	}

	/**
	 * Method addMetaPropiedad.
	 * 
	 * @param metaPropiedad
	 */
	public void addMetaProperty(MetaProperty metaPropiedad) {
		if (metaPropertiesTab == null) {
			metaPropertiesTab = new HashMap();
		}
		metaPropertiesTab.put(metaPropiedad.getName(), metaPropiedad);
	}

	/**
	 * For dynamically add properties to this tab
	 */
	public void addProperty(String propertyName) {
		if (propertiesNames == null)
			return;
		propertiesNames.add(propertyName);
		resetAfterAddRemoveProperty();
	}

	/**
	 * For dynamically add properties to this tab
	 */
	public void addProperty(int index, String propertyName) {
		if (propertiesNames == null)
			return;
		propertiesNames.add(index, propertyName);
		resetAfterAddRemoveProperty();
	}

	/**
	 * For dynamically remove properties to this tab
	 */
	public void removeProperty(String propertyName) {
		if (propertiesNames == null)
			return;
		propertiesNames.remove(propertyName);
		resetAfterAddRemoveProperty();
	}

	/**
	 * For dynamically remove properties to this tab
	 */
	public void removeProperty(int index) {
		if (propertiesNames == null)
			return;
		propertiesNames.remove(index);
		resetAfterAddRemoveProperty();
	}

	public void movePropertyToRight(int index) {
		if (propertiesNames == null)
			return;
		if (index >= propertiesNames.size() - 1)
			return;
		Object aux = propertiesNames.get(index);
		propertiesNames.set(index, propertiesNames.get(index + 1));
		propertiesNames.set(index + 1, aux);
		resetAfterAddRemoveProperty();
	}

	public void movePropertyToLeft(int index) {
		if (propertiesNames == null)
			return;
		if (index <= 0)
			return;
		Object aux = propertiesNames.get(index);
		propertiesNames.set(index, propertiesNames.get(index - 1));
		propertiesNames.set(index - 1, aux);
		resetAfterAddRemoveProperty();
	}

	/**
	 * For dynamically remove all properties to this tab
	 */
	public void clearProperties() { 
		if (propertiesNames == null)
			return;
		propertiesNames.clear();
		resetAfterAddRemoveProperty();
	}
	
	public void restoreDefaultProperties() { 
		setPropertiesNames(defaultPropertiesNames); 
		resetAfterAddRemoveProperty();
	}
	
	private void resetAfterAddRemoveProperty() {
		selectSQL = null;
		metaProperties = null;
		metaPropertiesCalculated = null;
		select = null;
		entityReferencesMappings = null;
		entityReferencesMappings = null;
		tableColumns = null;
		hiddenPropertiesNames = null;
		hiddenTableColumns = null;
	}

	public String getBaseCondition() {
		return baseCondition == null ? "" : baseCondition;
	}

	public void setBaseCondition(String string) {
		baseCondition = string;
		sQLBaseCondition = null;
	}

	public String getSQLBaseCondition() throws XavaException {
		if (sQLBaseCondition == null) {
			sQLBaseCondition = getEntityMapping().changePropertiesByColumns(
					getBaseCondition());
		}
		return sQLBaseCondition;
	}

	public boolean hasBaseCondition() {
		return !Is.emptyString(this.baseCondition);
	}

	/**
	 * Aplica el filtro asociado a este tab a los objetos enviado.
	 * <p>
	 * 
	 * Se usará para filtrar los argumentos. <br>
	 */
	public Object filter(Object[] objects) throws FilterException,
			XavaException {
		if (getMetaFilter() == null)
			return objects;
		return getMetaFilter().filter(objects);
	}

	public String getId() {
		if (!tieneNombre())
			return getMetaComponent().getName() + ".tab";
		return getMetaComponent().getName() + ".tabs." + getName();
	}

	public String getDefaultOrder() {
		return defaultOrder;
	}

	public void setDefaultOrder(String ordenDefecto) {
		this.defaultOrder = ordenDefecto;
		this.sQLDefaultOrder = null;
	}

	public String getSQLDefaultOrder() throws XavaException {
		if (sQLDefaultOrder == null) {
			sQLDefaultOrder = getEntityMapping().changePropertiesByColumns(
					getDefaultOrder());
		}
		return sQLDefaultOrder;
	}

	public boolean hasDefaultOrder() {
		return !Is.emptyString(this.defaultOrder);
	}

	public MetaTab cloneMetaTab() {
		try {
			MetaTab r = (MetaTab) clone();
			if (r.metaPropertiesHiddenCalculated != null) {
				r.metaPropertiesHiddenCalculated = new ArrayList(
						metaPropertiesHiddenCalculated);
			}
			if (r.metaPropertiesHidden != null) {
				r.metaPropertiesHidden = new ArrayList(metaPropertiesHidden);
			}
			if (r.metaConsults != null) {
				r.metaConsults = new ArrayList(metaConsults);
			}
			if (r.propertiesNames != null) {
				r.propertiesNames = new ArrayList(propertiesNames);
			}
			if (r.metaProperties != null) {
				r.metaProperties = new ArrayList(metaProperties);
			}
			if (r.metaPropertiesCalculated != null) {
				r.metaPropertiesCalculated = new ArrayList(
						metaPropertiesCalculated);
			}
			if (r.entityReferencesMappings != null) {
				r.entityReferencesMappings = new ArrayList(entityReferencesMappings);
			}
			if (r.entityReferencesMappings != null) {
				r.entityReferencesMappings = new ArrayList(
						entityReferencesMappings);
			}
			if (r.tableColumns != null) {
				r.tableColumns = new ArrayList(tableColumns);
			}
			if (r.hiddenPropertiesNames != null) {
				r.hiddenPropertiesNames = new ArrayList(hiddenPropertiesNames);
			}
			if (r.hiddenTableColumns != null) {
				r.hiddenTableColumns = new ArrayList(hiddenTableColumns);
			}
			if (r.metaPropertiesTab != null) {
				r.metaPropertiesTab = new HashMap(metaPropertiesTab);
			}
			return r;
		} catch (CloneNotSupportedException ex) {
			throw new RuntimeException(XavaResources.getString("clone_error",
					getClass()));
		}
	}

	public Collection getRemainingPropertiesNames() throws XavaException {
		Collection result = new ArrayList(getMetaModel().getRecursiveQualifiedPropertiesNames());
		result.removeAll(getPropertiesNames());
		return result;
	}

	public void addMetaRowStyle(MetaRowStyle style) {
		if (rowStyles == null) rowStyles = new ArrayList();
		rowStyles.add(style);
	}
	
	public boolean hasRowStyles() {
		return rowStyles != null;
	}
	
	public Collection getMetaRowStyles() {
		return rowStyles==null?Collections.EMPTY_LIST:rowStyles;
	}

}

