package org.openxava.tab.impl;

import java.rmi.*;
import java.util.*;

import javax.ejb.*;
import javax.rmi.*;

import org.openxava.component.*;
import org.openxava.converters.*;
import org.openxava.ejbx.*;
import org.openxava.mapping.*;
import org.openxava.model.meta.*;
import org.openxava.tab.meta.*;
import org.openxava.util.*;


/**
 * Implementación local del Tab (IEntityTabImpl) configurada a partir de un componente OpenXava. <p>
 * 
 * Este tab sirve para todos los beans que se quiera, solo
 * hay que indicar el nombre del modelo en el create.<br>
 * 
 * @author Javier Paniza
 */
public class EntityTab implements IEntityTabImpl {
	
	private static Map dataProviders;	 

	private String selectBase;

	// Tamaño de cada bloque que se envía
	private static final int DEFAULT_CHUNK_SIZE = 50;
	
	private int chunkSize = DEFAULT_CHUNK_SIZE;
	private String componentName;
	private String tabName;

	private JDBCTabProvider tabProvider;
	private TableModelBean table;
	private MetaTab metaTab;
	private String modelName;
	private transient MetaEntity metaEntity = null;
	private transient EntityMapping entityMapping = null;
	private int[] indexesPK = null;	
	private List propertiesNames;
	private Map keyIndexes = null;

	private List tabCalculators;
	private Collection tabConverters;
	
	private boolean	knowIfHasPropertiesWithValidValues = false;
	private boolean _hasPropertiesWithValidValues;
	
	/** Constructor por defecto. */
	public EntityTab() {
	}
	
	// Implementa IEntityTab
	public void search(int indice, Object clave)
		throws FinderException, RemoteException {
		tabProvider.search(indice, clave);
	}
	
	public void search(String condicion, Object clave) throws FinderException, RemoteException {
		try {			
			StringBuffer select = new StringBuffer(getSelectBase().toUpperCase());			
			if (!Is.emptyString(condicion)) {
				if (select.indexOf("WHERE") < 0) select.append(" WHERE "); 
				else select.append(" AND "); 				
				select.append(condicion);				
			}															
			tabProvider.search(select.toString(), clave);
		}
		catch (XavaException ex) {
			ex.printStackTrace();
			throw new RemoteException(XavaResources.getString("tab_search_error", ex.getLocalizedMessage()));
		}		
	}
	
	private String getSelectBase() throws XavaException {
		if (selectBase == null) {
			selectBase = insertarCamposClave(metaTab.getSelectSQL());
		}
		return selectBase;
	}
	

	// Implementa IEntityTab
	/**
	 * Devuelve un mapa con los valores de las claves.
	 */
	public Object findEntity(Object[] clave) throws FinderException, RemoteException {
		try {
			Map result = new HashMap();
			for (int i = 0; i < clave.length; i++) {
				int iPropiedad = getIndexesPK()[i];
				String nombre = (String) getPropertiesNames().get(iPropiedad);
				result.put(nombre, clave[i]);
			}			
			return result;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException(
				"Imposible buscar entidad "
					+ this.modelName
					+ " por:\n"
					+ ex.getLocalizedMessage());
		}
	}


	/** Cabeceras que aparecerán en la tabla. */
	private String[] getHeading() throws XavaException {
		String[] result = new String[getPropiedades().size() + getIndexesPK().length];
		Iterator it = getPropiedades().iterator();		
		int i = getIndexesPK().length;
		while (it.hasNext()) {
			MetaProperty p = (MetaProperty) it.next();
			result[i++] = p.getLabel();
		}
		return result;
	}

	/**
	 * Clase asociada a cada columna de la tabla. <br>
	 * Se especifica el nombre de la clase completo en
	 * un <code>String</code>.<br>
	 */
	private String[] getColumnsClasses() throws XavaException {
		String[] result = new String[getPropiedades().size() + getIndexesPK().length];
		Iterator it = getPropiedades().iterator();
		int i = getIndexesPK().length;
		while (it.hasNext()) {
			MetaProperty p = (MetaProperty) it.next();
			result[i++] = getColumnClass(p.getType());
		}
		return result;
	}

	private String getColumnClass(Class tipo) throws XavaException {
		String nombre = tipo.getName();
		if (!tipo.isPrimitive())
			return nombre;

		// Es primitivo
		if (nombre.equals("boolean")) {
			return "java.lang.Boolean";
		}
		else if (nombre.equals("byte")) {
			return "java.lang.Byte";
		}
		else if (nombre.equals("char")) {
			return "java.lang.Character";
		}
		else if (nombre.equals("short")) {
			return "java.lang.Short";
		}
		else if (nombre.equals("int")) {
			return "java.lang.Integer";
		}
		else if (nombre.equals("long")) {
			return "java.lang.Long";
		}
		else if (nombre.equals("float")) {
			return "java.lang.Float";
		}
		else if (nombre.equals("double")) {
			return "java.lang.Double";
		}
		throw new XavaException("primitive_type_not_recognized", nombre);
	}

	// Implementa IEntityTab
	public IXTableModel getTable()  throws RemoteException {
		try {
			table.setEntityTab(this);
			return new HiddenXTableModel(table, getIndexesPK()); 
		}
		catch (XavaException ex) {
			ex.printStackTrace();
			throw new RemoteException(XavaResources.getString("tab_tablemodel_error", ex.getLocalizedMessage()));
		}   
	}

	/**
	 * Inicialización del bean.
	 */
	public void init() throws InitException {
		try {
			if (Is.emptyString(componentName)) {
				throw new InitException("tab_component_required");
			}
			tabProvider = new JDBCTabProvider();
			table = new TableModelBean();
			table.setTranslateHeading(false);
			this.modelName = componentName;
			this.metaEntity = null;
			this.entityMapping = null;
			this.indexesPK = null;
			if (this.metaTab == null) {			
				this.metaTab = MetaComponent.get(componentName).getMetaTab(tabName);
			}
			table.setHeading(getHeading());
			table.setColumnsClasses(getColumnsClasses());
			tabProvider.setFields(getCampos());
			tabProvider.setConditions(getCondiciones());			
			tabProvider.setTable(getNombreTablaDB());
			tabProvider.setChunkSize(getChunkSize());
			table.setPKIndexes(getIndexesPK());
			table.invariant();
			//tabProvider.invariante(); No puede ser porque falta el connectionProvider que se establece en el servidor
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new InitException(
				"Error al iniciar el gestor de datos tabulares de "
					+ modelName
					+ ": "
					+ ex.getMessage());
		}
	}

	private String[] getCampos() throws XavaException {
		Collection c = new ArrayList();
		// Primero la clave
		Iterator itNombresPK = getNombresPK().iterator();
		while (itNombresPK.hasNext()) {
			c.add(getEntityMapping().getQualifiedColumn((String) itNombresPK.next()));
		}
				
		// Luego lo demas
		c.addAll(metaTab.getTableColumns());
		c.addAll(metaTab.getHiddenTableColumns());
				
		String[] result = new String[c.size()];
		c.toArray(result);		
		return result;
	}
	

	private String[] getCondiciones() throws RemoteException {
		try {
			Collection metaConsultas = metaTab.getMetaConsults();
			String[] condiciones = new String[metaConsultas.size()];
			Iterator it = metaConsultas.iterator();
			int i = 0;
			while (it.hasNext()) {
				MetaConsult consulta = (MetaConsult) it.next();
				condiciones[i++] = insertarCamposClave(consulta.getConditionSQL());
			}
			return condiciones;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException(
				"Imposible obtener condiciones del tab de "
					+ this.modelName
					+ " por:\n"
					+ ex.getLocalizedMessage());
		}
	}
	
	private String insertarCamposClave(String select) throws XavaException {				
		String s = select.trim().toUpperCase();
		if (!(s.startsWith("SELECT ") || (s.startsWith("SELECT\t")))) return select;				
		StringBuffer result = new StringBuffer("SELECT ");		
		Iterator itNombresPK = getNombresPK().iterator();
		while (itNombresPK.hasNext()) {
			result.append(getEntityMapping().getQualifiedColumn((String) itNombresPK.next()));
			result.append(", ");
		}
		result.append(s.substring(7));						
		return result.toString();
	}

	// Siempre los primeros campos, que ademas son siempre ocultos
	private int[] getIndexesPK() throws XavaException {		
		if (indexesPK == null) {
			indexesPK = new int[getNombresPK().size()];
			for (int i = 0; i < indexesPK.length; i++) {
				indexesPK[i]=i;
			}
		}
		return indexesPK;
	}

	private String getNombreTablaDB() throws XavaException {
		return getEntityMapping().getTable();
	}

	// Implementa IEntityTabImpl
	public DataChunk nextChunk() throws RemoteException {
		Collection calculadoresTab = null;
		Map indicesClave = null;
		String nombreModelo = null;
		List nombresPropiedades = null;
		try {		
			if (metaTab.hasCalculatedProperties()) {
				nombreModelo = this.componentName;
				calculadoresTab = getTabCalculators();
				indicesClave = getKeyIndexes();
				nombresPropiedades = getPropertiesNames();
			}		
			Collection conversoresTab = getTabConverters();
			if (conversoresTab.isEmpty()) conversoresTab = null;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException(XavaResources.getString("tab_next_chunk_error"));
		}
		DataChunk tv = null; 		
		try {
			tv = getDataProvider(getComponentName()).nextChunk(tabProvider, nombreModelo, nombresPropiedades, calculadoresTab, indicesClave, tabConverters);						
		}
		catch (Exception ex) {
			cancelDataProvider(getComponentName());
			tv = getDataProvider(getComponentName()).nextChunk(tabProvider, nombreModelo, nombresPropiedades, calculadoresTab, indicesClave, tabConverters);			
		}
		tabProvider.setCurrent(tv.getIndexNext());				
		List data = tv.getData();
		int l = data.size();
		
		// Conversion de valores-posibles
		try {
			if (tienePropiedadesConValoresPosibles()) {
				for (int i = 0; i < l; i++) {
					data.set(i, hacerConversionesValoresPosibles((Object[]) data.get(i)));
				}
			}
		}
		catch (XavaException ex) {
			ex.printStackTrace();
			throw new RemoteException(XavaResources.getString("tab_valid_values_error"));
		}
		
		return tv;		
	}

	/**
	 * Method tienePropiedadesConValoresPosibles.
	 * @return boolean
	 */
	private boolean tienePropiedadesConValoresPosibles() throws XavaException {
		if (!knowIfHasPropertiesWithValidValues) {
			_hasPropertiesWithValidValues = averiguarSiTienePropiedadesConValoresPosibles();
			knowIfHasPropertiesWithValidValues = true;
		}
		return _hasPropertiesWithValidValues;
	}
	
	private boolean averiguarSiTienePropiedadesConValoresPosibles() throws XavaException {
		Iterator it = getPropiedades().iterator();
		while (it.hasNext()) {
			MetaProperty metaPropiedad = (MetaProperty) it.next();
			if (metaPropiedad.hasValidValues()) {
				return true;
			}			
		}
		return false;		
	}


	
	
	private Object[] hacerConversionesValoresPosibles(Object[] fila) throws XavaException {
		int cantidad = getPropertiesNames().size();
		for (int i = indexesPK.length; i < cantidad; i++) {			
			String nombre = (String) getPropertiesNames().get(i);
			MetaProperty metaPropiedad = getMetaEntity().getMetaProperty(nombre);
			if (metaPropiedad.hasValidValues()) {		
				int valorPosible = ((Number) fila[i]).intValue();
				fila[i] = metaPropiedad.getValidValue(valorPosible); 					
			}
		}
		return fila;
	}

	/**
	 * De CalculadorTab
	 */
	private Collection getTabCalculators() throws XavaException {
		if (tabCalculators == null) {
			tabCalculators = new ArrayList();			
			Iterator it = metaTab.getMetaPropertiesHiddenCalculated().iterator();
			
			while (it.hasNext()) {
				MetaProperty metaPropiedad = (MetaProperty) it.next();
				int indicePropiedad = getIndicePropiedad(metaPropiedad.getQualifiedName());
				tabCalculators.add(new TabCalculator(metaPropiedad, indicePropiedad));
			}			
			it = metaTab.getMetaPropertiesCalculated().iterator();
			while (it.hasNext()) {
				MetaProperty metaPropiedad = (MetaProperty) it.next();
				int indicePropiedad = getIndicePropiedad(metaPropiedad.getQualifiedName());
				tabCalculators.add(new TabCalculator(metaPropiedad, indicePropiedad));
			}
		}
		return tabCalculators;
	}
	
	/**
	 * De ConversorTab
	 */
	private Collection getTabConverters() throws XavaException {
		if (tabConverters == null) {
			tabConverters = new ArrayList();			
			Iterator it = getPropertiesNames().iterator();
			int i=0;
			String tabla = getEntityMapping().getTable();
			while (it.hasNext()) {
				String nombrePropiedad = (String) it.next();
				try {															
					IConverter conversor = getEntityMapping().getConverter(nombrePropiedad);
					if (conversor != null) {
						tabConverters.add(new TabConverter(nombrePropiedad, i,  conversor));
					}
					else {
						PropertyMapping mapeoPropiedad = getEntityMapping().getPropertyMapping(nombrePropiedad);
						IMultipleConverter conversorMultiple =  mapeoPropiedad.getMultipleConverter();
						if (conversorMultiple != null) {							
							tabConverters.add(new TabConverter(nombrePropiedad, i, conversorMultiple, mapeoPropiedad.getCmpFields(), getCampos(), tabla));
						}																							
					}
				}
				catch (ElementNotFoundException ex) {
					// Asi excluimos propiedades que no estan en el mapeo
				}
				i++;
			}
		}
		return tabConverters;
	}
	

	private int getIndicePropiedad(String nombrePropiedad)
		throws XavaException {
		return getPropertiesNames().indexOf(nombrePropiedad);
	}

	/**
	 * @return Colección de objetos de tipo <tt>MetaPropiedad</tt>.
	 */
	private Collection getPropiedades() throws XavaException {
		return metaTab.getMetaProperties();
	}

	/**
	 * @return Colección de objetos de tipo <tt>String</tt>.
	 */
	private List getPropertiesNames() throws XavaException {
		if (propertiesNames == null) {
			propertiesNames = new ArrayList();
			propertiesNames.addAll(getNombresPK());
			propertiesNames.addAll(metaTab.getPropertiesNames());
			propertiesNames.addAll(metaTab.getHiddenPropertiesNames());
		}
		return propertiesNames;
	}
	
	private Collection getNombresPK() throws XavaException {		
		return getMetaEntity().getKeyFields();
	}
	
	

	private MetaEntity getMetaEntity() throws XavaException {
		if (metaEntity == null) {
			metaEntity = MetaComponent.get(this.modelName).getMetaEntity();
		}
		return metaEntity;
	}

	private Map getKeyIndexes()
		throws FinderException, XavaException, RemoteException {
		if (keyIndexes == null) {	
			Iterator it = getPropertiesNames().iterator();
			keyIndexes = new HashMap();
			int i = 0;
			while (it.hasNext()) {
				String nombrePropiedad = (String) it.next();
				if (getMetaEntity().isKey(nombrePropiedad)) {
					keyIndexes.put(nombrePropiedad, new Integer(i));
				}
				i++;
			}		
		}
		return keyIndexes;
	}

	private EntityMapping getEntityMapping() throws XavaException {
		if (entityMapping == null) {
			entityMapping = MetaComponent.get(this.modelName).getEntityMapping();
		}
		return entityMapping;
	}

	/**
	 * @return
	 */
	public String getComponentName() {
		return componentName;
	}

	/**
	 * @return
	 */
	public String getTabName() {
		return tabName;
	}

	/**
	 * @param string
	 */
	public void setComponentName(String string) {
		componentName = string;
		metaEntity = null;
		keyIndexes = null;
		selectBase = null;
	}

	/**
	 * @param string
	 */
	public void setTabName(String string) {
		tabName = string;
		selectBase = null;
	}
	
	
	private static IEntityTabDataProvider getDataProvider(String componentName) throws RemoteException {		
		try {
			String paquete = MetaComponent.get(componentName).getPackageNameWithSlashWithoutModel();			
			IEntityTabDataProvider dataProvider = (IEntityTabDataProvider) getDataProviders().get(paquete);
			if (dataProvider == null) {
				if (XavaPreferences.getInstance().isTabAsEJB()) { 
					Object ohome = BeansContext.get().lookup("ejb/"+paquete+"/EntityTab");
					EntityTabHome home = (EntityTabHome) PortableRemoteObject.narrow(ohome, EntityTabHome.class);
					dataProvider = home.create();
				}
				else {
					EntityTabDataProvider dp = new EntityTabDataProvider();																			
					dp.setComponentName(componentName);
					dataProvider = dp;
				}
				getDataProviders().put(paquete, dataProvider);				
			}		
			return dataProvider;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new RemoteException("tab_remote_error");
		}				
	}
		
	private static Map getDataProviders() {
		if (dataProviders == null) {
			dataProviders = new HashMap();
		}
		return dataProviders;
	}
	
	private static void cancelDataProvider(String componentName) {
		try {
			String packageName = MetaComponent.get(componentName).getPackageNameWithSlashWithoutModel();
			getDataProviders().remove(packageName);			
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.err.println(XavaResources.getString("warning_cancel_data_provider"));
		}		
	}	
	
	

	public int getResultSize() throws RemoteException {
		if (!XavaPreferences.getInstance().isShowCountInList()) {
			return table.getRowCount(); 
		}		
		return getDataProvider(getComponentName()).getResultSize(tabProvider);
	}

	public void reset() throws RemoteException {
		tabProvider.reset();
	}

	public MetaTab getMetaTab() {
		return metaTab;
	}

	public void setMetaTab(MetaTab tab) {
		metaTab = tab;
	}

	public int getChunkSize() {
		return chunkSize;
	}
	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}
	
}
