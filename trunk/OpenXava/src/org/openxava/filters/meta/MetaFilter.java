package org.openxava.filters.meta;

import java.io.*;
import java.util.*;

import org.openxava.filters.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;

/**
 * @author Javier Paniza
 */
public class MetaFilter implements Serializable {
	
	private IFilter filter;
	private String className;
	private Collection metaSets;
	private Collection propertiesSetNames;
	
	public void _addMetaSet(MetaSet metaPoner) {
		if (metaSets == null) {
			metaSets = new ArrayList();
		}
		metaSets.add(metaPoner);
		propertiesSetNames = null;
	}
	
	public IFilter createFilter() throws XavaException {
		try {
			Object o = Class.forName(getClassName()).newInstance();
			if (!(o instanceof IFilter)) {
				throw new XavaException("implements_required", getClassName(), IFilter.class.getName());
			}
			IFilter filtro = (IFilter) o;
			if (hasMetaSets()) {
				asignarValoresPropiedades(filtro);
			}						
			return filtro;
		}
		catch (XavaException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("create_filter_error", getClassName());
		}
	}

	/**
	 * Method asignarValoresPropiedades.
	 * @param calculador
	 */
	private void asignarValoresPropiedades(IFilter filtro) throws Exception {
		PropertiesManager mp = new PropertiesManager(filtro);
		Iterator it = getMetaSets().iterator();
		while (it.hasNext()) {
			MetaSet metaPoner = (MetaSet) it.next();
			mp.executeSetFromString(metaPoner.getPropertyName(), metaPoner.getValue());			
		}		
	}

	
	/**
	 * 
	 * @return nunca nulo
	 */
	public Collection getMetaSets() {
		return metaSets==null?new ArrayList():metaSets;
	}
	
	public boolean hasMetaSets() {
		return metaSets != null;
	}

	/**
	 * Returns the nombreClase.
	 * @return String
	 */
	public String getClassName() {
		return className;
	}
	
	public String getPropertyNameForPropertyFrom(String nombrePropiedadDesde) throws ElementNotFoundException {
		if (metaSets==null) 		 
			throw new ElementNotFoundException("filter_not_value_from", nombrePropiedadDesde);
		Iterator it = metaSets.iterator();
		while (it.hasNext()) {
			MetaSet metaPoner = (MetaSet) it.next();
			if (metaPoner.getPropertyNameFrom().equals(nombrePropiedadDesde)) {
				return metaPoner.getPropertyName();
			}
		}	
		throw new ElementNotFoundException("filter_not_value_from", nombrePropiedadDesde);
	}

	/**
	 * Sets the nombreClase.
	 * @param nombreClase The nombreClase to set
	 */
	public void setClassName(String nombreClase) {
		this.className = nombreClase;
	}

	public Object filter(Object[] objects) throws FilterException, XavaException {
		return getFilter().filter(objects);		
	}
	
	public IFilter getFilter() throws XavaException {
		if (filter == null) {
			filter = createFilter();
		}
		return filter;
	}

}
