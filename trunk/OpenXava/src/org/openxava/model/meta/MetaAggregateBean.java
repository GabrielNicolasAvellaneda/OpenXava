package org.openxava.model.meta;

import java.util.*;

import org.openxava.mapping.*;
import org.openxava.util.*;

/**
 * Agregado cuya implementación es un JavaBean. <p>
 * 
 * @author: Javier Paniza
 */

public class MetaAggregateBean extends MetaAggregate {
	
	private java.lang.String beanClass;
	private boolean generate;

	private Map mapaReferenciasPropiedadesPersistente;
	
	public MetaAggregateBean() {
		super();
	}
	
	public java.lang.String getBeanClass() throws XavaException {
		if (Is.emptyString(beanClass)) {
			beanClass = getMetaComponent().getPackageName() + "." + getName();
		}
		return beanClass;
	}
	
	public void setBeanClass(java.lang.String newClase) {
		beanClass = newClase;
	}
		
	public Class getPropertiesClass() throws XavaException {
		try {
			return Class.forName(getBeanClass());
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
			throw new XavaException(
				"No se encuentra la clase "
					+ getBeanClass()
					+ " asociada al modelo "
					+ getName());
		}
	}
	/**
	 * Returns the generar.
	 * @return boolean
	 */
	public boolean isGenerate() {
		return generate;
	}

	/**
	 * Sets the generar.
	 * @param generar The generar to set
	 */
	public void setGenerate(boolean generar) {
		this.generate = generar;
	}

	public ModelMapping getMapping() throws XavaException {
		throw new XavaException("aggregate_bean_no_mapping", getName(), getMetaComponent().getName());
	}
		
	public Collection getMetaPropertiesPersistents(MetaReference ref) throws XavaException {
		Collection result = (Collection) getMapaReferenciasPropiedadesPersistente().get(ref);		
		if (result != null) {
			return result;
		}
		result = new ArrayList();
		Iterator it = getMembersNames().iterator(); // nombres miembros para que esté ordenado
		
		ModelMapping mapeo = ref.getMetaModel().getMapping();
		String prefijo = ref.getName() + "_";
		while (it.hasNext()) {
			String nombre = (String) it.next();
			if (!containsMetaProperty(nombre)) continue;			
			if (mapeo.hasPropertyMapping(prefijo + nombre)) {
				MetaProperty p = (MetaProperty) getMetaProperty(nombre);
				result.add(p);
			}
		}					
		result = Collections.unmodifiableCollection(result);
		getMapaReferenciasPropiedadesPersistente().put(ref, result);
		return result;
	}

	/**
	 * Method getMapaReferenciasPropiedadesPersistente.
	 */
	private Map getMapaReferenciasPropiedadesPersistente() {
		if (mapaReferenciasPropiedadesPersistente == null) {
			mapaReferenciasPropiedadesPersistente = new HashMap();
		}
		return mapaReferenciasPropiedadesPersistente;
	}

	
	public Collection getPersistentsPropertiesNames(MetaReference ref) throws XavaException {
		Iterator it = getMetaPropertiesPersistents(ref).iterator();
		Collection result = new ArrayList();
		while (it.hasNext()) {
			MetaProperty pr = (MetaProperty) it.next();
			result.add(pr.getName());			
		}
		return result;
	}

	public String getClassName() throws XavaException {
		return getBeanClass();
	}
	


}