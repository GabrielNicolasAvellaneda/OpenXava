package org.openxava.component;

import java.io.*;
import java.util.*;

import org.openxava.mapping.*;
import org.openxava.model.meta.*;
import org.openxava.tab.meta.*;
import org.openxava.util.*;
import org.openxava.view.meta.*;




/**
 * Aglutina toda la metainformación de un
 * concepto de negocio.<p>
 * 
 * Un componente de negocio son todos los artefactos
 * de software que tienen que ver con un concepto de
 * negocio. Por ejemplo, todas las vistas, tablas,
 * clases, modelos, etc. que tengan que ver con 
 * el concepto de Comercial sería el componente
 * de negocio <i>Comercial</i>.<p>
 * 
 * No lleva el prefijo Meta porque aquí no hay
 * que distinguir entre el elemento (por ejemplo
 * una entidad) y su descripción (que sería la
 * metaentidad).<p>
 * 
 * @author Javier Paniza
 */

public class MetaComponent implements Serializable {
	
	private String packageNameWithSlash;

	private static Map components = new HashMap();

	private String name;
	private MetaEntity metaEntity;
	private Map metaAggregates;	
	private Map aggregatesMapping;
	private MetaTab metaTab;
	private Map metaTabs;
	private EntityMapping entityMapping;
	private String packageName;
	private boolean generateJDO;

	private static Properties packages;
	
	MetaComponent() {
	}
		
	/**
	 * 
	 * @exception ElementNotFoundException  Si no existe el componente.
	 * @exception XavaException  Cualquier otro problema. 
	 */
	public static MetaComponent get(String nombre) throws XavaException {		
		MetaComponent r = (MetaComponent) components.get(nombre);		
		if (r == null) {			
			r = ComponentParser.parse(nombre);		
			if (r == null) {				
				throw new ElementNotFoundException("component_not_found", nombre);
			}
			r.validar();						
			components.put(nombre, r);
		}		
		return r;
	}
	

	public static boolean exists(String nombre) throws XavaException {
		try {
			get(nombre);
			return true;
		}
		catch (ElementNotFoundException ex) {
			return false;
		}		
	}
	
	public static Collection getAllLoaded() {
		return components.values();
	}
		
	/**
	 * @return Ni nulo ni cadena vacía.
	 */
	public String getName() {
		return name;
	}
	void setName(String nombre) {
		this.name = nombre;
	}
	
	/**
	 * Nunca nulo.
	 */
	public MetaEntity getMetaEntity() {
		return metaEntity;
	}
	
	/**
	 * @param metaEntidad No debería ser nulo.
	 */
	void setMetaEntity(MetaEntity metaEntidad) throws XavaException {
		if (this.metaEntity != null) {
			throw new XavaException("component_only_1_entity", getName());
		}
		this.metaEntity = metaEntidad;
		this.metaEntity.setMetaComponent(this);
	}
	

	/**
	 * @param metaVista No debería ser nulo.
	 */	
	void addMetaView(MetaView metaVista) throws XavaException {
		if (Is.emptyString(metaVista.getModelName())) {
			getMetaEntity().addMetaView(metaVista);
		}
		else if (getName().equals(metaVista.getModelName())) {
			getMetaEntity().addMetaView(metaVista);
		}
		else {
			getMetaAggregate(metaVista.getModelName()).addMetaView(metaVista);
		}
	}
			
	/**
	 * 
	 * @return Nunca nulo. 
	 * @exception ElementNotFoundException Si no existe el meta agregado en este componente.
	 * @exception XavaException Cualquier otro problema.
	 */
	public MetaAggregate getMetaAggregate(String nombre) throws  XavaException {
		if (metaAggregates == null || !metaAggregates.containsKey(nombre)) {
			throw new ElementNotFoundException("aggregate_not_found", nombre, getName());
		}
		return (MetaAggregate) metaAggregates.get(nombre);
	}
	
	/**
	 *
	 * @return Nunca nulo.
	 * @exception ElementNotFoundException Si no existe el mapeo agregado en
	 * este componente.
	 * @exception XavaException Cualquier otro problema.
	 */
	public AggregateMapping getAggregateMapping(String nombre) throws  XavaException {
		if (aggregatesMapping == null || !aggregatesMapping.containsKey(nombre)) {
			throw new ElementNotFoundException("aggregate_mapping_not_found", nombre, getName());
		}
		return (AggregateMapping) aggregatesMapping.get(nombre);
	}	
	
	/**
	 * 
	 * @return Elementos <tt>instanceof MetaAgregado</tt>. Nunca nulo. 	 
	 * @exception XavaException Cualquier otro problema.
	 */
	public Collection getMetaAggregates() throws  XavaException {
		if (metaAggregates == null) return new ArrayList();
		return metaAggregates.values();			
	}	
	
	/**
	 * 
	 * @return Elementos <tt>instanceof MetaAgregadoBean</tt> y <tt>generar == true</tt>. Nunca nulo. 	 
	 * @exception XavaException Cualquier otro problema.
	 */
	public Collection getMetaAggregatesBeanGenerated() throws  XavaException {
		Iterator it = getMetaAggregates().iterator();
		Collection result = new ArrayList();
		while (it.hasNext()) {
			Object element = it.next();			
			if (!(element instanceof MetaAggregateBean)) continue;			
			MetaAggregateBean agregado = (MetaAggregateBean) element;
			if (agregado.isGenerate()) {				
				result.add(agregado);
			}			
		}		
		return result;			
	}	
	
	/**
	 *
	 * @return Elementos <tt>instanceof MetaAgregadoEjb</tt> y 
	 * 			<tt>generarXDocLet	 == true</tt>. Nunca nulo.
	 * @exception XavaException Cualquier otro problema.
	 */
	public Collection getMetaAggregatesEjbXDocLet() throws  XavaException {
		Iterator it = getMetaAggregates().iterator();
		Collection result = new ArrayList();
		while (it.hasNext()) {
			Object element = it.next();
			if (!(element instanceof MetaAggregateEjb)) continue;
			MetaAggregateEjb agregado = (MetaAggregateEjb) element;
			if (agregado.isGenerateXDocLet()) {
				result.add(agregado);
			}
		}
		return result;
	}	

	/**
	 * @param metaAgregado  No debería ser nulo.
	 */
	void addMetaAggregate(MetaAggregate metaAgregado) {
		if (metaAggregates == null) metaAggregates = new HashMap();
		metaAggregates.put(metaAgregado.getName(), metaAgregado);
		metaAgregado.setMetaComponent(this);
	}
	
	void addAggregateMapping(AggregateMapping mapeoAgregado) throws XavaException {
		if (aggregatesMapping == null) aggregatesMapping = new HashMap();
		aggregatesMapping.put(mapeoAgregado.getModelName(), mapeoAgregado);
		mapeoAgregado.setMetaComponent(this);	
	}	
	
	/**
	 * <tt>MetaTab</tt> por defecto.
	 * 
	 * @return Nunca nulo.
	 */
	public MetaTab getMetaTab() throws XavaException {
		if (metaTab == null) {
			metaTab = MetaTab.createDefault(this);
		}
		return metaTab;
	}

	/**
	 * <tt>MetaTab</tt> a partir del nombre.
	 * 
	 * @param nombre Enviar nulo o cadena vacía devuelve el tab por defecto, 
	 * @return Nunca nulo
	 * @exception XavaException
	 * @exception ElementNotFoundException
	 */	
	public MetaTab getMetaTab(String nombre) throws XavaException, ElementNotFoundException {		
		if (Is.emptyString(nombre)) return getMetaTab();
		if (metaTabs == null) {			
			throw new ElementNotFoundException("tab_not_found", nombre, getName());
		}
		MetaTab result = (MetaTab) metaTabs.get(nombre);
		if (result == null) {
			throw new ElementNotFoundException("tab_not_found", nombre, getName());
		}
		return result;
	}
	

	/**
	 * <tt>MetaTab</tt> por defecto.
	 */	
	private void setMetaTab(MetaTab metaTab) throws XavaException {
		if (this.metaTab != null) {			
			throw new XavaException("no_more_1_tab", getName());
		}
		this.metaTab = metaTab;
	}
		
	void addMetaTab(MetaTab metaTab) throws XavaException {
		metaTab.setMetaComponent(this);
		metaTab.addDefaultMetaConsults();		
		String nombre = metaTab.getName();
		if (Is.emptyString(nombre)) { // por defecto
			setMetaTab(metaTab);
		}
		else { // con nombre
			if (metaTabs == null) {
				metaTabs = new HashMap();			
			}
			metaTabs.put(nombre, metaTab);		
		}
	}
	
	/**
	 * 
	 * @return Nunca nulo.
	 * @exception XavaException Si hay algún problema, lo que incluye
	 *            que no exista mapeo definido para este componente.
	 */
	public EntityMapping getEntityMapping() throws XavaException {
		if (entityMapping == null) {
			throw new XavaException("entity_mapping_not_found", getName());
		}
		return entityMapping;
	}
	
	void setEntityMapping(EntityMapping mapeo) throws XavaException {
		if (mapeo != null) {
			mapeo.setMetaComponent(this);
		}
		this.entityMapping = mapeo;
	}
	
	private void validar() throws XavaException {		
		if (Is.emptyString(getName())) {
			throw new XavaException("component_name_required");
		}
		if (metaEntity == null) {
			throw new XavaException("component_entity_required", getName());
		}
	}
				
	public String getPackageName() throws XavaException {
		if (packageName==null) {
			try {
				packageName = getPackages().getProperty(getName());
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new XavaException("component_package_error", getName());
			}
		}
		return packageName;
	}

	private static Properties getPackages() throws IOException {
		if (packages == null) {
			PropertiesReader reader = new PropertiesReader(MetaComponent.class, "packages.properties");
			packages = reader.get();
		}
		return packages;
	}
		
	/** 
	 * @param unqualifiedPackage For example, of org.openxava.test.model is test, 
	 * 			that is to say, without domain (org.openxava) and model package (model).
	 */
	public static String getQualifiedPackageForUnqualifiedPackage(String unqualifiedPackage) throws XavaException {
		try {
			return
				getPackages().getProperty("package.domain." + unqualifiedPackage, "") + "/" +
				unqualifiedPackage + "/" +
				getPackages().getProperty("package.model." + unqualifiedPackage, "");
		}
		catch (Exception ex) {			
			ex.printStackTrace();
			throw new XavaException("read_packages_error");						
		}
	}
	
	/**
	 * Sets the paquete.
	 * @param paquete The paquete to set
	 */
	public void setPackageName(String paquete) {
		this.packageName = paquete;
	}

	/**
	 * Package using / instead of .	 
	 */
	public String getPackageNameWithSlash() throws XavaException {
		if (packageNameWithSlash == null) {
			packageNameWithSlash = Strings.change(getPackageName(), ".", "/");
		}
		return packageNameWithSlash;
	}

	public boolean isGenerateJDO() {
		return generateJDO;
	}

	public void setGenerateJDO(boolean b) {
		generateJDO = b;
	}
	
}

