package org.openxava.controller.meta;


import java.util.*;

import org.openxava.util.*;
import org.openxava.util.meta.*;


public class MetaController extends MetaElement {

	private String className; // Only for spanish/swing version
	private Collection metaActions = new ArrayList();
	private Collection parentsNames = new ArrayList();
	private Collection parents = new ArrayList();
	private Map mapMetaActions = new HashMap();
		
	/**
	 * Only for spanish/swing version
	 */
	public String getClassName() {
		return Is.emptyString(className)?"puntocom.xava.xcontrolador.tipicos.ControladorVacio":className;
	}
	/**
	 * Only for spanish/swing version
	 */
	public void setClassName(String clase) {
		this.className = clase;
	}

	public void addMetaAction(MetaAction accion) {
		metaActions.add(accion);
		accion.setMetaController(this);
		mapMetaActions.put(accion.getName(), accion);
	}
	
	public void addParentName(String nombrePadre) {
		if (parentsNames == null) parentsNames = new ArrayList();
		parentsNames.add(nombrePadre);  	
		parents = null;	
	}
	
	
	
	
	public MetaAction getMetaAction(String nombre) throws ElementNotFoundException {
		MetaAction a = (MetaAction) mapMetaActions.get(nombre);
		if (a == null) throw new ElementNotFoundException("action_not_found", nombre, getName());		
		return a; 
	}
	
		
	
	/**
	 * @return Nunca nulo, de tipo <tt>Accion</tt> y de solo lectura.
	 */
	
	public Collection getMetaActions() {
		return Collections.unmodifiableCollection(metaActions);
	}
	
	public boolean containsMetaAction(String nombreAccion) {
		return metaActions.contains(new MetaAction(nombreAccion));
	}
	public Collection getMetaActionsForSection(String seccion) throws XavaException {
		if (Is.emptyString(seccion)) return Collections.EMPTY_LIST;
		
		List result = new ArrayList();
		// añadimos los de los padres
		Iterator itPadres = getParents().iterator();
		while (itPadres.hasNext()) {
			MetaController padre = (MetaController) itPadres.next();
			result.addAll(padre.getMetaActionsForSection(seccion));
		}
				
		// y ahora las nuestras
		Iterator it = metaActions.iterator();
		while (it.hasNext()) {
			MetaAction metaAccion = (MetaAction) it.next();			
			if (seccion.equals(metaAccion.getMode()) || Is.emptyString(metaAccion.getMode())) {
				int pos = result.indexOf(metaAccion);
				if (pos < 0) result.add(metaAccion);
				else {
					result.remove(pos);
					result.add(pos, metaAccion);
				} 
			}
		}		
		return result;
	}
	
	public String getId() {
		return getName();
	}
	public Collection getMetaActionsOnInit() {
		Iterator it = metaActions.iterator();
		Collection result = new ArrayList();
		while (it.hasNext()) {
			MetaAction metaAccion = (MetaAction) it.next();			
			if (metaAccion.isOnInit()) {
				result.add(metaAccion);
			}
		}
		return result;
	}
	public boolean hasParents() {
		return parentsNames != null;		 	
	}		
	
	/**
	 * @return de tipo MetaControlador
	 */
	public Collection getParents() throws XavaException {
		if (!hasParents()) return Collections.EMPTY_LIST;
		if (parents == null) {
			parents = new ArrayList();
			Iterator it = parentsNames.iterator();
			while (it.hasNext()) {
				String nombre = (String) it.next();
				parents.add(MetaControllers.getMetaController(nombre));
			}
		}
		return parents;
	}
	
}


