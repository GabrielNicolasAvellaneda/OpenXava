package org.openxava.util.meta;

import java.io.*;
import java.util.*;

import org.openxava.util.*;


/**
 * @author Javier Paniza
 */
public class MetaSetsContainer implements Serializable {
		
	private Collection metaPoners;
		
	public void addMetaSet(MetaSet metaPoner) {
		if (metaPoners == null) {
			metaPoners = new ArrayList();
		}
		metaPoners.add(metaPoner);		
	}
	
	protected void assignPropertiesValues(Object objeto) throws Exception {		
		PropertiesManager mp = new PropertiesManager(objeto);
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
		return metaPoners==null?Collections.EMPTY_LIST:metaPoners;
	}
	
	public Collection getMetaSetsWithoutValue() {		
		Collection result = new ArrayList();
		Iterator it = getMetaSets().iterator();
		while (it.hasNext()) {
			MetaSet poner = (MetaSet) it.next();
			if (!poner.hasValue()) {
				result.add(poner);
			}
		}
		return result;
	}
	
	public boolean containsMetaSets() {
		return metaPoners != null;
	}
	
	public boolean containsMetaSetsWithoutValue() {
		return containsMetaSets() && !getMetaSetsWithoutValue().isEmpty();
	}
	
	
	public String getPropertyNameForPropertyFrom(String nombrePropiedadDesde) throws ElementNotFoundException {
		if (metaPoners==null) 		 
			throw new ElementNotFoundException("not_value_from_other_property", nombrePropiedadDesde);
		Iterator it = metaPoners.iterator();
		while (it.hasNext()) {
			MetaSet metaPoner = (MetaSet) it.next();
			if (metaPoner.getPropertyNameFrom().equals(nombrePropiedadDesde)) {
				return metaPoner.getPropertyName();
			}
		}	
		throw new ElementNotFoundException("not_value_from_other_property", nombrePropiedadDesde);
	}

}
