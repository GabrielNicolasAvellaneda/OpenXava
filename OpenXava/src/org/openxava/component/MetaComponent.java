package org.openxava.component;

import java.io.*;
import java.util.*;

import org.openxava.mapping.*;
import org.openxava.model.meta.*;
import org.openxava.tab.meta.*;
import org.openxava.util.*;
import org.openxava.view.meta.*;

/**
 * 
 * All meta info about business concept.<p>
 * 
 * A business component is all software artifacts
 * that have relation with a business concept.
 * For example, all view, tables, classes, models, etc.
 * about concept of Seller is the <i>Seller</i> business
 * component.<p>
 * 
 * @author Javier Paniza
 */

public class MetaComponent implements Serializable {
	
	private static Map components = new HashMap();
	private static Properties packages;
		
	private String packageNameWithSlashWithoutModel;
	private String name;
	private MetaEntity metaEntity;
	private Map metaAggregates;	
	private Map aggregatesMapping;
	private MetaTab metaTab;
	private Map metaTabs;
	private EntityMapping entityMapping;
	private String packageName;
	private String ejbPackage;

	
	/**
	 * 
	 * @exception ElementNotFoundException  If component does not exist.
	 * @exception XavaException  Any other problem. 
	 */
	public static MetaComponent get(String name) throws XavaException {		
		MetaComponent r = (MetaComponent) components.get(name);		
		if (r == null) {			
			r = ComponentParser.parse(name);		
			if (r == null) {				
				throw new ElementNotFoundException("component_not_found", name);
			}
			r.validar();						
			components.put(name, r);
		}		
		return r;
	}
	

	public static boolean exists(String name) throws XavaException {
		try {
			get(name);
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
	 * @return Not null and not empty string
	 */
	public String getName() {
		return name;
	}
	void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return Not null.
	 */
	public MetaEntity getMetaEntity() {
		return metaEntity;
	}
	
	/**
	 * @param metaEntity Not null
	 */
	void setMetaEntity(MetaEntity metaEntity) throws XavaException {
		if (this.metaEntity != null) {
			throw new XavaException("component_only_1_entity", getName());
		}
		this.metaEntity = metaEntity;
		this.metaEntity.setMetaComponent(this);
	}
	
	/**
	 * @param metaView Not null.
	 */	
	void addMetaView(MetaView metaView) throws XavaException {
		if (Is.emptyString(metaView.getModelName())) {
			getMetaEntity().addMetaView(metaView);
		}
		else if (getName().equals(metaView.getModelName())) {
			getMetaEntity().addMetaView(metaView);
		}
		else {
			getMetaAggregate(metaView.getModelName()).addMetaView(metaView);
		}
	}
			
	/**
	 * 
	 * @return Not null. 
	 * @exception ElementNotFoundException If the MetaAggregate does not exist in this component.
	 * @exception XavaException Any other problem.
	 */
	public MetaAggregate getMetaAggregate(String name) throws  XavaException {
		if (metaAggregates == null || !metaAggregates.containsKey(name)) {
			throw new ElementNotFoundException("aggregate_not_found", name, getName());
		}
		return (MetaAggregate) metaAggregates.get(name);
	}
	
	/**
	 *
	 * @return Not null
	 * @exception ElementNotFoundException If does not exist the aggregate mapping in this component.
	 * @exception XavaException Any other problem.
	 */
	public AggregateMapping getAggregateMapping(String name) throws  XavaException {
		if (aggregatesMapping == null || !aggregatesMapping.containsKey(name)) {
			throw new ElementNotFoundException("aggregate_mapping_not_found", name, getName());
		}
		return (AggregateMapping) aggregatesMapping.get(name);
	}
	
	public Collection getAggregateMappings() throws XavaException {
		return aggregatesMapping == null?Collections.EMPTY_LIST:aggregatesMapping.values(); 
	}	
	
	/**
	 * 
	 * @return Elementss <tt>instanceof MetaAggregate</tt>. Not null. 	 
	 * @exception XavaException Any other problem.
	 */
	public Collection getMetaAggregates() throws  XavaException {
		if (metaAggregates == null) return new ArrayList();
		return metaAggregates.values();			
	}	
	
	/**
	 * 
	 * @return Elements <tt>instanceof MetaAggregateBean</tt> and <tt>generate == true</tt>. Not null. 	 
	 * @exception XavaException Any other problem.
	 */
	public Collection getMetaAggregatesBeanGenerated() throws  XavaException {
		Iterator it = getMetaAggregates().iterator();
		Collection result = new ArrayList();
		while (it.hasNext()) {
			Object element = it.next();			
			if (!(element instanceof MetaAggregateBean)) continue;			
			MetaAggregateBean aggregate = (MetaAggregateBean) element;
			if (aggregate.isGenerate()) {				
				result.add(aggregate);
			}			
		}		
		return result;			
	}	
	
	/**
	 *
	 * @return Elements <tt>instanceof MetaAggregateEjb</tt> and 
	 * 			<tt>generateXDocLet	 == true</tt>. Not null.
	 * @exception XavaException Any other problem.
	 */
	public Collection getMetaAggregatesEjbXDocLet() throws  XavaException {
		Iterator it = getMetaAggregates().iterator();
		Collection result = new ArrayList();
		while (it.hasNext()) {
			Object element = it.next();
			if (!(element instanceof MetaAggregateEjb)) continue;
			MetaAggregateEjb aggregate = (MetaAggregateEjb) element;
			if (aggregate.isGenerateXDocLet()) {
				result.add(aggregate);
			}
		}
		return result;
	}	

	/**
	 * @param metaAggregate  Not null.
	 */
	void addMetaAggregate(MetaAggregate metaAggregate) {
		if (metaAggregates == null) metaAggregates = new HashMap();
		metaAggregates.put(metaAggregate.getName(), metaAggregate);
		metaAggregate.setMetaComponent(this);
	}
	
	void addAggregateMapping(AggregateMapping aggregateMapping) throws XavaException {
		if (aggregatesMapping == null) aggregatesMapping = new HashMap();
		aggregatesMapping.put(aggregateMapping.getModelName(), aggregateMapping);
		aggregateMapping.setMetaComponent(this);	
	}	
	
	/**
	 * <tt>MetaTab</tt> by default.
	 * 
	 * @return Not null.
	 */
	public MetaTab getMetaTab() throws XavaException {
		if (metaTab == null) {
			metaTab = MetaTab.createDefault(this);
		}
		return metaTab;
	}

	/**
	 * <tt>MetaTab</tt> from name.
	 * 
	 * @param name If null or empty string return default tab. 
	 * @return Not null
	 */	
	public MetaTab getMetaTab(String name) throws XavaException, ElementNotFoundException {		
		if (Is.emptyString(name)) return getMetaTab();
		if (metaTabs == null) {			
			throw new ElementNotFoundException("tab_not_found", name, getName());
		}
		MetaTab result = (MetaTab) metaTabs.get(name);
		if (result == null) {
			throw new ElementNotFoundException("tab_not_found", name, getName());
		}
		return result;
	}
	

	/**
	 * <tt>MetaTab</tt> by default.
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
		String name = metaTab.getName();
		if (Is.emptyString(name)) { // by default
			setMetaTab(metaTab);
		}
		else { // with name
			if (metaTabs == null) {
				metaTabs = new HashMap();			
			}
			metaTabs.put(name, metaTab);		
		}
	}
	
	/**
	 * 
	 * @return Not null.
	 * @exception XavaException Any problem, including that mapping for
	 * 		this component does not exist.
	 */
	public EntityMapping getEntityMapping() throws XavaException {
		if (entityMapping == null) {
			throw new XavaException("entity_mapping_not_found", getName());
		}
		return entityMapping;
	}
	
	void setEntityMapping(EntityMapping mapping) throws XavaException {
		if (mapping != null) {
			mapping.setMetaComponent(this);
		}
		this.entityMapping = mapping;
	}
	
	private void validar() throws XavaException {		
		if (Is.emptyString(getName())) {
			throw new XavaException("component_name_required");
		}
		if (metaEntity == null) {
			throw new XavaException("component_entity_required", getName());
		}
	}
				
	/**
	 * Java package where the model classes resides.
	 */
	public String getPackageName() throws XavaException {
		if (packageName==null) {
			try {
				packageName = getPackages().getProperty(getName());
				if (packageName == null) {
					packageName = getPackagesEJB().getProperty(getName());
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new XavaException("component_package_error", getName());
			}
		}
		return packageName;
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * Java package where the EJB classes resides. <p>
	 * 
	 * Usually the same of {@link #getPackageName}, but if we wish to
	 * use EJB model and POJO model in the same application it will be different.<br> 
	 */
	public String getEJBPackage() throws XavaException {
		if (ejbPackage==null) {
			try {
				ejbPackage = getPackagesEJB().getProperty(getName());
			}
			catch (Exception ex) {
				ex.printStackTrace();
				throw new XavaException("component_package_error", getName());
			}
		}		
		return ejbPackage;
	}
	
	private static Properties getPackages() throws IOException {
		if (packages == null) {
			PropertiesReader reader = new PropertiesReader(MetaComponent.class, "packages.properties");
			packages = reader.get();
		}
		return packages;
	}
	
	private static Properties getPackagesEJB() throws IOException {
		if (packages == null) {
			PropertiesReader reader = new PropertiesReader(MetaComponent.class, "packages-ejb.properties");
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
	 * Package using / instead of .	and 
	 * it does not includes the model or ejb package. 
	 */
	public String getPackageNameWithSlashWithoutModel() throws XavaException {
		if (packageNameWithSlashWithoutModel == null) {
			packageNameWithSlashWithoutModel = Strings.change(getPackageName(), ".", "/");
			packageNameWithSlashWithoutModel = packageNameWithSlashWithoutModel.substring(0, packageNameWithSlashWithoutModel.lastIndexOf('/'));
		}
		return packageNameWithSlashWithoutModel;
	}
		
}

