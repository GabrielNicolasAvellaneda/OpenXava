package org.openxava.controller.meta;


import java.util.*;

import org.openxava.actions.*;
import org.openxava.util.*;
import org.openxava.util.meta.*;


public class MetaAction extends MetaElement {
	
	public static final int NEVER = 0;
	public static final int IF_POSSIBLE = 1;
	public static final int ALMOST_ALWAYS = 2;
	public static final int ALWAYS = 4;

	private boolean hidden = false;
	private Collection metaSets;
	private String qualifiedName;
	private String method;
	private String image;
	private String keystroke;
	private String mode;	
	private String className;
	private Collection metaUseObjects;
	private MetaController metaController;
	private int byDefault;
	private boolean onInit;
	
	public MetaAction() {
	}
	
	public MetaAction(String nombre) {
		setName(nombre);
	}
	
	public String getQualifiedName() {
		if (qualifiedName == null) {
			if (metaController == null) qualifiedName = getName();
			else qualifiedName = getMetaController().getName() + "." + getName();
		}	 
		return qualifiedName;
	}
	
	public void setName(String newNombre) {
		qualifiedName = null;
		super.setName(newNombre);
	}

		
	/**
	 * Gets the atajoDeTeclado
	 * @return Returns a String
	 */
	public String getKeystroke() {
		return keystroke;
	}
	/**
	 * Sets the atajoDeTeclado
	 * @param atajoDeTeclado The atajoDeTeclado to set
	 */
	public void setKeystroke(String atajoDeTeclado) {
		this.keystroke = atajoDeTeclado;
	}


	


	/**
	 * Gets the imagen
	 * @return Returns a String
	 */
	public String getImage() {
		return image;
	}
	/**
	 * Sets the imagen
	 * @param imagen The imagen to set
	 */
	public void setImage(String imagen) {
		this.image = imagen;
	}


	/**
	 * Gets the metodo
	 * @return Returns a String
	 */
	public String getMethod() {
		if (Is.emptyString(method)) return getName();
		return method;
	}
	/**
	 * Sets the metodo
	 * @param metodo The metodo to set
	 */
	public void setMethod(String metodo) {
		this.method = metodo;
	}

	
	public String getLabel(Locale locale) {
		return Labels.removeUnderlined(super.getLabel(locale));		
	}

	/**
	 * Gets the mnemonic
	 * @return Returns a char
	 */
	public char getMnemonic() {
		String etiqueta = super.getLabel();
		int idxSub = etiqueta.indexOf('_');
		if (idxSub >= 0) {
			int idxMnemonic = idxSub + 1;
			if (idxMnemonic < etiqueta.length()) {
				return etiqueta.charAt(idxMnemonic);
			}			
		}		
		return 0;
	}
	
	public boolean equals(Object accion) {
		if (!(accion instanceof MetaAction)) return false; // descarta los nulos también
		return getName().equals(((MetaAction) accion).getName());
	}
	/**
	 * @return
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param string
	 */
	public void setClassName(String string) {
		className = string;
	}
	
	public boolean usesObjects() {
		return metaUseObjects != null && !metaUseObjects.isEmpty();
	}
	
	public Collection getMetaUseObjects() {		
		if (!usesObjects()) return Collections.EMPTY_LIST;
		return metaUseObjects;
	}

	public void addMetaUseObject(MetaUseObject objeto) {
		if (metaUseObjects == null) metaUseObjects = new ArrayList();
		metaUseObjects.add(objeto);		
	}

	public MetaController getMetaController() {
		return metaController;
	}

	public void setMetaController(MetaController controlador) {
		metaController = controlador;
		qualifiedName = null;
	}
	
	public String getControllerName() {
		return metaController==null?"":metaController.getName();
	}
	
	public boolean hasImage() {
		return !Is.emptyString(this.image);
	}

	public void _addMetaSet(MetaSet metaPoner) {
		if (metaSets == null) {
			metaSets = new ArrayList();
		}
		metaSets.add(metaPoner);		
	}
	
	public IAction createAction() throws XavaException {
		try {
			Object o = Class.forName(getClassName()).newInstance();
			if (!(o instanceof IAction)) {
				throw new XavaException("implements_required", getClassName(), IAction.class.getName());
			}
			IAction calculador = (IAction) o;
			if (hasMetaSets()) {
				asignarValoresPropiedades(calculador);
			}						
			return calculador;
		}
		catch (XavaException ex) {
			throw ex;
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw new XavaException("create_action_error", getClassName());
		}
	}
	
	public boolean hasMetaSets() {
		return metaSets != null;
	}

	private void asignarValoresPropiedades(IAction accion) throws Exception {
		PropertiesManager mp = new PropertiesManager(accion);
		Iterator it = getMetaSets().iterator();
		while (it.hasNext()) {
			MetaSet metaPoner = (MetaSet) it.next();
			mp.executeSetFromString(metaPoner.getPropertyName(), metaPoner.getValue());			
		}		
	}

	public Collection getMetaSets() {
		return metaSets==null?new ArrayList():metaSets;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean b) {
		hidden = b;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String string) {
		mode = string;
	}

	public int getByDefault() {
		return byDefault;
	}

	public void setByDefault(int i) {
		byDefault = i;
	}
	
	public String getId() {
		return getQualifiedName();
	}


	public boolean isOnInit() {
		return onInit;
	}

	public void setOnInit(boolean b) {
		onInit = b;
	}

}


