package org.openxava.tab.meta;


import java.util.*;

import org.openxava.filters.*;
import org.openxava.filters.meta.*;
import org.openxava.model.meta.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;


/**
 * Only used in spanish/swing version.
 * 
 * @author Javier Paniza
 */


public class MetaConsult extends MetaElement {
	
	private Collection metaParameters = new ArrayList();
	private String condition;
	private String conditionSQL;
	private MetaTab metaTab;
	private MetaFilter metaFilter;
	private IFilter filter;
	private String label;
				
	public void addMetaParameter(MetaParameter parametro) {
		metaParameters.add(parametro);
		parametro.setMetaConsult(this);
	}
	
	MetaModel getMetaModel() throws XavaException {
		if (metaTab == null) {
			throw new XavaException("tab_consult_required");
		}		
		return metaTab.getMetaModel();
	} 
	
	/**
	 * @return Nunca nulo, de tipo <tt>MetaParametro</tt> y de solo lectura.
	 */
	public Collection getMetaParameters() {
		return Collections.unmodifiableCollection(metaParameters);
	}
	

	
	

	/**
	 * Gets the condicion
	 * @return Nulo significa que se la condicion SQL se calcula por defecto,
	 *         y cadena vacia significa que no hay condicion(se seleccionan
	 *         todos los objetos. 
	 */
	public String getCondition() {		
		return condition==null?null:condition.trim();
	}
	/**
	 * Sets the condicion
	 * @param condicion The condicion to set
	 */
	public void setCondition(String condicion) {
		this.condition = condicion;
		this.conditionSQL = null;
	}


	/**
	 * Gets the etiqueta
	 * @return Returns a String
	 */
	public String getLabel() {
		if (has18nLabel()) return super.getLabel();
		if (Is.emptyString(label)) {
			try {
				label = crearEtiquetaDefecto();
			}
			catch (XavaException ex) {
				ex.printStackTrace();
				System.err.println("¡ADVERTENCIA! Imposible obtener etiqueta para la consulta " + getId());
			}
		}
		return label;
	}
	
	/**
	 * Sets the etiqueta
	 * @param etiqueta The etiqueta to set
	 */
	public void setLabel(String etiqueta) {
		super.setLabel(etiqueta);
		this.label = etiqueta;
	}
	
	/**
	 * La condición pero usando los nombres de columna de la tabla subyacente.
	 * Si no se ha establecido condición aquí se crea una por defecto.
	 */
	public String getConditionSQL() throws XavaException {
		if (conditionSQL == null) {
			String condicion = getCondition();			
			if (Is.emptyString(condicion)) {
				condicion = crearCondicionDefecto();
			}
			if (!condicion.trim().toUpperCase().startsWith("SELECT") &&
				(metaTab.hasReferences() || metaTab.hasBaseCondition())) { 
				if (condicion.trim().equals("")) {
					condicion = metaTab.getSelect(); 
				}
				else {
					String selectTab = metaTab.getSelect();
					String union = selectTab.toUpperCase().indexOf("WHERE")<0?" WHERE ":" AND ";
					condicion = selectTab + union + condicion; 
				}
			}
			conditionSQL = getMetaTab().getEntityMapping().changePropertiesByColumns(condicion);
		}
		return conditionSQL;
	}
	
	
	private String crearCondicionDefecto() throws XavaException {
		Iterator it = getMetaParameters().iterator();		
		StringBuffer condicion = new StringBuffer();
		while (it.hasNext()) {
			MetaParameter parametro = (MetaParameter) it.next();			
			condicion.append("${");
			condicion.append(parametro.getPropertyName());
			if (parametro.isRange()) {
				condicion.append("} between ? and ?");
			}
			else if (parametro.isLike()) {
				condicion.append("} like ?");						
			}			
			else {
				condicion.append("} = ?");
			}
			if (it.hasNext()) {
				condicion.append(" AND ");
			}
		}
		return condicion.toString();
	}
	
	
	private String crearEtiquetaDefecto() throws XavaException {
		Collection metaParametros = getMetaParameters();
		Iterator it = metaParametros.iterator();		
		int cantidad = metaParametros.size();
		StringBuffer etiqueta = new StringBuffer(XavaResources.getString("por"));
		etiqueta.append(' ');
		int c=0;
		while (it.hasNext()) {
			MetaParameter parametro = (MetaParameter) it.next();	
			String etiquetaPropiedad = null;		
			c++;			
			etiqueta.append(parametro.getMetaProperty().getLabel());
			if (c < cantidad) {
				if (c == cantidad -1) {
					etiqueta.append(' ');
					etiqueta.append(XavaResources.getString("y"));
					etiqueta.append(' ');
				}
				else {
					etiqueta.append(", ");
				}
			}
		}
		return etiqueta.toString();
	}


	/**
	 * Gets the tab
	 * @return Returns a Tab
	 */
	MetaTab getMetaTab() {
		return metaTab;
	}
	/**
	 * Sets the tab
	 * @param tab The tab to set
	 */
	void setMetaTab(MetaTab tab) {
		this.metaTab = tab;
	}
		


	/**
	 * Returns the metaFiltro.
	 * @return MetaFiltro
	 */
	public MetaFilter getMetaFilter() {
		return metaFilter;
	}

	/**
	 * Sets the metaFiltro.
	 * @param metaFiltro The metaFiltro to set
	 */
	public void setMetaFilter(MetaFilter metaFiltro) {
		this.metaFilter = metaFiltro;
	}
	
	/**
	 * Aplica el filtro asociado a esta consulta si lo hay,
	 * y también el del tab contenedor. <p> 	 
	 */
	public Object filterParameters(Object o) throws XavaException {
		Object result = o;		
		if (getMetaFilter() != null) {
			result = getFilter().filter(result);
		}
		return getMetaTab().filterParameters(result);
	}
	
	private IFilter getFilter() throws XavaException {
		if (filter == null) {
			filter = getMetaFilter().createFilter();
		}
		return filter;
	}
	/**
	 * Method usaOrderBy.
	 * @return boolean
	 */
	public boolean useOrderBy() {
		return condition != null && condition.toUpperCase().indexOf("ORDER BY") >= 0;
	}
	
	public Collection getOrderByPropertiesNames() {
		Collection result = new ArrayList();
		if (!useOrderBy()) return result;
		int i = condition.toUpperCase().indexOf("ORDER BY");
		String r = condition.substring(i+8);
		i = r.indexOf("${");
		int f = 0;		
		while (i >= 0) {			
			f = r.toString().indexOf("}", i+2);
			if (f < 0) break;
			String propiedad = r.substring(i+2, f);
			result.add(propiedad);
			i = r.indexOf("${", f);
		}
		return result;
	}

	public String getId() {
		if (Is.emptyString(getName())) return null;				
		return getMetaTab().getId() + ".consultas." + getName();
	}
	

}


